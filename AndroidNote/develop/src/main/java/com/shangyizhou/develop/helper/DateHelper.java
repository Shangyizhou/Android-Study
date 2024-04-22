package com.shangyizhou.develop.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DateHelper {
    private static String TAG = DateHelper.class.getSimpleName();
    private static volatile DateHelper instance;

    private DateHelper() {

    }

    public static DateHelper getInstance() {
        if (instance == null) {
            synchronized (DateHelper.class) {
                if (instance == null) {
                    instance = new DateHelper();
                }
            }
        }
        return instance;
    }
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String getTime(long timestamp) {
        String time = format.format(new Date(timestamp));//这个就是把时间戳经过处理得到期望格式的时间
        return time;
    }

    public String getCurrentTime() {
        String time = format.format(new Date(System.currentTimeMillis()));//这个就是把时间戳经过处理得到期望格式的时间
        return time;
    }
}
