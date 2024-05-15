package com.example.androidnote.net;

import android.content.Context;
import android.os.Build;

import com.example.androidnote.App;
import com.shangyizhou.develop.log.SLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class YiYanHandler {
    private static final String TAG = YiYanHandler.class.getSimpleName();
    private static ArrayList<String> mTips = new ArrayList<>();
    public static String process(Context context, String line) {
        SLog.i(TAG, "process: " + line);
        // 清空数据
        mTips.clear();
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
        if (!res.contains(result) || !res.contains(tips)) {
            return "格式不合法";
        }
        String resultStr = res.substring(result.length() + 2, res.indexOf(tips) - 2);
        String tipsStr = res.substring(res.indexOf(tips) + tips.length() + 2, res.length() - 1);

        resultStr = resultStr.replaceAll("\\s", "");
        tipsStr = tipsStr.replaceAll("\\s", "");

        SLog.i(TAG, "process resultStr: " + resultStr);
        SLog.i(TAG, "process tipsStr: " + tipsStr);

        String[] parts = tipsStr.split("&");
        for (String part : parts) {
            SLog.i(TAG, "process part: " + part);
            mTips.add(part);
        }

        return resultStr;
    }

    public static String mIntent;
    public static HashMap<String, Integer> map = new HashMap<>();
    public static String processIntent(Context context, String line) {
        SLog.i(TAG, "processIntent: " + line);
        // 清空数据
        mTips.clear();
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
        String intent = "\"intent\"";
        if (!res.contains(result) || !res.contains(tips) || !res.contains(intent)) {
            return "格式不合法";
        }
        String resultStr = res.substring(result.length() + 2, res.indexOf(tips) - 2);
        String tipsStr = res.substring(res.indexOf(tips) + tips.length() + 2, res.indexOf(intent) - 2);
        String intentStr = res.substring(res.indexOf(intent) + intent.length() + 2, res.length() - 1);

        resultStr = resultStr.replaceAll("\\s", "");
        tipsStr = tipsStr.replaceAll("\\s", "");
        intentStr = intentStr.replaceAll("\\s", "");

        SLog.i(TAG, "process resultStr: " + resultStr);
        SLog.i(TAG, "process tipsStr: " + tipsStr);
        SLog.i(TAG, "process intentStr: " + intentStr);

        String[] parts = tipsStr.split("&");
        for (String part : parts) {
            SLog.i(TAG, "process part: " + part);
            mTips.add(part);
        }

        mIntent = intentStr;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            map.put(intentStr, map.getOrDefault(intentStr, 0) + 1);
        }

        return resultStr;
    }

    public static String mQueryStr;
    public static String mAdviceStr;
    public static String mBookStr;
    public static void processQuery(Context context, String line) {
        SLog.i(TAG, "processQuery: " + line);
        // 清空数据
        mTips.clear();
        // 使用正则表达式替换所有空白字符
        String res = line.replaceAll("\\s", "");
        res = res.replace("```json", "");
        res = res.replace(" ", "");
        res = res.replace("{", "");
        res = res.replace("}", "");
        res = res.replace("[", "");
        res = res.replace("]", "");
        res = res.replace("\n", "");
        res = res.replace("```", "");
        SLog.i(TAG, "process res: " + res);

        String query = "\"query\"";
        String advice = "\"advice\"";
        String book = "\"book\"";
        if (!res.contains(query) || !res.contains(advice) || !res.contains(book))  {
            return;
        }

        try {
            String queryStr = res.substring(query.length() + 2, res.indexOf(advice) - 2);
            String adviceStr = res.substring(res.indexOf(advice) + advice.length() + 2, res.indexOf(book) - 2);
            String bookStr = res.substring(res.indexOf(book) + book.length() + 2, res.length() - 1);

            queryStr = queryStr.replaceAll("\\s", "");
            adviceStr = adviceStr.replaceAll("\\s", "");
            bookStr = bookStr.replaceAll("\\s", "");

            SLog.i(TAG, "process queryStr: " + queryStr);
            SLog.i(TAG, "process adviceStr: " + adviceStr);
            SLog.i(TAG, "process bookStr: " + bookStr);

            mQueryStr = queryStr;
            mAdviceStr = adviceStr;
            mBookStr = bookStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // public static String mCreateRobotStr;
    public static String processCreateRobot(Context context, String line) {
        SLog.i(TAG, "processQuery: " + line);

        // 使用正则表达式替换所有空白字符
        String res = line.replace("```json", "");
        res = res.replace("```", "");
        SLog.i(TAG, "process res: " + res);

        return res;
    }

    public static ArrayList<String> getTips() {
        return mTips;
    }
}
