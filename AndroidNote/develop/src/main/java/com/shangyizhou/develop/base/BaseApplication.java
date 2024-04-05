package com.shangyizhou.develop.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.shangyizhou.develop.log.HttpLoggingInterceptor;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.net.RetryInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 方便应用层快速写一个App启动类
 * 但是必须要写完App类，然后在App类中setAppHolder设置好上下文
 * 不能直接用BaseApplication的上下文，因为可能会开多个产品线，也就是每个产品都有个App继承BaseApplication这个时候的上下文就混乱了
 */
public class BaseApplication extends Application {
    private static BaseApplication application;
    private static Context mAppContext;
    public static Handler MainHandler;
    OkHttpClient mGlobalOkHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        SLog.i("BaseApplication", "BaseApplication_onCreate ");
        application = this;
        MainHandler = new Handler(Looper.getMainLooper());
        mAppContext = getApplicationContext();
        mGlobalOkHttpClient = initGlobalOkHttpClient();
    }

    public static BaseApplication getInstance() {
        return application;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public Handler getMainHandler() {
        return MainHandler;
    }

    public OkHttpClient getGlobalOkHttpClient() {
        return mGlobalOkHttpClient;
    }

    public static OkHttpClient initGlobalOkHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(20000L, TimeUnit.MILLISECONDS).readTimeout(20000L,
                        TimeUnit.MILLISECONDS)
                .addInterceptor(new HttpLoggingInterceptor())
                .addInterceptor(new RetryInterceptor(3, 1500));
        return clientBuilder.build();
    }
}
