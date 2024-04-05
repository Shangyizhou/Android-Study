package com.example.androidnote;

import android.app.Application;
import android.os.Handler;

import com.shangyizhou.develop.AppHolder;
import com.shangyizhou.develop.base.BaseApplication;
import com.shangyizhou.develop.log.SLog;

import okhttp3.OkHttpClient;

/**
 * 可以通过 BaseApplication 获取 Context
 *
 *     <application
 *         android:name=".App" // 这里填写你的自定义Application的类名
 *         android:allowBackup="true"
 *         android:icon="@mipmap/ic_launcher"
 *         android:label="@string/app_name"
 *         android:roundIcon="@mipmap/ic_launcher_round"
 *         android:supportsRtl="true"
 *         android:theme="@style/AppTheme">
 *         ...
 *     </application>
 */
public class App extends BaseApplication {

    private static final String TAG = "App";
    private static App app;

    public App() {
    }

    public static App getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        SLog.d(TAG, "onCreate: start App");
        AppHolder.setApp(new AppHolder.Delegate() {
            @Override
            public Application getApplication() {
                return app;
            }

            @Override
            public Handler getMainHandler() {
                return app.getMainHandler();
            }

            @Override
            public OkHttpClient getGlobalOkHttpClient() {
                return app.getGlobalOkHttpClient();
            }
        }, null);
    }
}
