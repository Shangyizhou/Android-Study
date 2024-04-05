package com.shangyizhou.develop.helper;

import android.content.Context;
import android.widget.Toast;

import com.shangyizhou.develop.AppHolder;

/**
 * example: ToastManager.getInstance().showToast(Context, String);
 */
public class ToastUtil {

    private static volatile ToastUtil instance;
    private ToastUtil() {}

    public static ToastUtil getInstance() {
        // 第一次检查，判断 instance 是否实例化
        if (instance == null) {
            synchronized (ToastUtil.class) {
                // 第二次检查，确保只有一个线程创建实例
                if (instance == null) {
                    instance = new ToastUtil();
                }
            }
        }
        return instance;
    }

    public void showToast(String msg) {
        Toast toast = Toast.makeText(AppHolder.getApp().getApplication(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
