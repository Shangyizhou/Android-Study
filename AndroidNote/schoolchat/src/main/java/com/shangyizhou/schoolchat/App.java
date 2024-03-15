package com.shangyizhou.schoolchat;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

public class App extends Application {
    private static final String TAG = "App";
    private static App app;
    public static Handler MainHandler;

    public App() {
    }

    public static App getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Log.i(TAG, "onCreate: App");
    }
    public Handler getMainHandler() {
        return MainHandler;
    }

}
