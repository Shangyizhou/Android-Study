package com.shangyizhou.develop.helper;

import com.shangyizhou.develop.AppHolder;

public class LottieUtil {
    private static volatile LottieUtil instance;
    private LottieUtil() {
    }
    public static LottieUtil getInstance() {
        // 第一次检查，判断 instance 是否实例化
        if (instance == null) {
            synchronized (LottieUtil.class) {
                // 第二次检查，确保只有一个线程创建实例
                if (instance == null) {
                    instance = new LottieUtil();
                }
            }
        }
        return instance;
    }
}
