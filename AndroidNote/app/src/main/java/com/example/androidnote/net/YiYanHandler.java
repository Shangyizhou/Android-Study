package com.example.androidnote.net;

import android.content.Context;

import com.example.androidnote.App;
import com.shangyizhou.develop.log.SLog;

import java.util.Arrays;

public class YiYanHandler {
    private static final String TAG = YiYanHandler.class.getSimpleName();
    public static String[] mTips;
    public static String process(Context context, String line) {
        SLog.i(TAG, "process: " + line);

        // 使用正则表达式替换所有空白字符
        String res = line.replaceAll("\\s", "");
        res = res.replace("```json", "");
        res = res.replace(" ", "");
        res = res.replace("{", "");
        res = res.replace("}", "");
        res = res.replace("\n", "");
        res = res.replace("```", "");
        SLog.i(TAG, "process res: " + res);

        String result = "\"result\"";
        String tips = "\"tips\"";
        String resultStr = res.substring(result.length() + 2, res.indexOf(tips) - 2);
        String tipsStr = res.substring(res.indexOf(tips) + tips.length() + 2, res.length() - 1);

        resultStr = resultStr.replaceAll("\\s", "");
        tipsStr = tipsStr.replaceAll("\\s", "");

        SLog.i(TAG, "process resultStr: " + resultStr);
        SLog.i(TAG, "process tipsStr: " + tipsStr);

        String[] parts = tipsStr.split("&");
        for (String part : parts) {
            SLog.i(TAG, "process part: " + part);
        }
        mTips = parts;

        return resultStr;
    }
}
