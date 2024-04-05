package com.shangyizhou.develop;

import android.app.Application;
import android.os.Bundle;
import android.os.Handler;

import okhttp3.OkHttpClient;

/**
 * 底部组件库需要一些上层app的上下文来传入参数
 * 因此上层app初始化时候就可以通过AppHolder传入自己的上下文和其他属性
 * AppHolder可以让其他类和BaseApplication解耦，防止BaseApplication的改变影响多个类
 */
public class AppHolder {
    private AppHolder() {
    }

    public abstract static class Delegate {
        public abstract Application getApplication();
        public abstract Handler getMainHandler();

        public abstract OkHttpClient getGlobalOkHttpClient();

        // public Object getGlobalKey(String key) {
        //     return null;
        // }

        // public Object setGlobalKey(String key, Object value) {
        //     return null;
        // }
    }

    private static Delegate sDelegate;

    /**
     * 上层app调用setApp重写接口，传入自己持有的上下文数据和一些bundle
     * @param delegate
     * @param options
     */
    public static void setApp(Delegate delegate, Bundle options) {
        sDelegate = delegate;
    }
    public static Delegate getApp() {
        return sDelegate;
    }

}
