package com.shangyizhou.develop.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.shangyizhou.develop.AppHolder;

public class SPUtil {
    private static volatile SPUtil instance;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    Context mContext;

    private SPUtil(Context context) {
        mContext = context;
        sharedPreferences = context.getSharedPreferences("Filename", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SPUtil getInstance() {
        // 第一次检查，判断 instance 是否实例化
        if (instance == null) {
            synchronized (SPUtil.class) {
                // 第二次检查，确保只有一个线程创建实例
                if (instance == null) {
                    instance = new SPUtil(AppHolder.getApp().getApplication());
                }
            }
        }
        return instance;
    }

    public void saveIntData(String key, int data) {
        editor.putInt(key, data);
        editor.commit();
    }

    public int getIntData(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public int getIntData(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void saveStringData(String key, String data) {
        editor.putString(key, data);
        editor.commit();
    }

    public String getStringData(String key) {
        return sharedPreferences.getString(key, "");
    }

    public String getStringData(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void saveBooleanData(String key, boolean data) {
        editor.putBoolean(key, data);
        editor.commit();
    }

    public boolean getBooleanData(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public boolean getBooleanData(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void saveFloatData(String key, float data) {
        editor.putFloat(key, data);
        editor.commit();
    }

    public float getFloatData(String key) {
        return sharedPreferences.getFloat(key, 0);
    }

    public float getFloatData(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }


}
