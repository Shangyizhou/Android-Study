package com.example.androidnote.manager;

import com.example.androidnote.db.helper.ResponseInfoHelper;
import com.example.androidnote.model.ResponseInfo;
import com.shangyizhou.develop.log.SLog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DataCollectionManager {
    private static String TAG = DataCollectionManager.class.getSimpleName();
    private static HashMap<String, String> modelCallNumber = new HashMap<>();
    private static HashMap<String, List<ResponseInfo>> data = new HashMap<>();

    private DataCollectionManager() {
    }

    public static void allModelCallNumber() {

    }

    public static long getInvokeCount() {
        if (data == null) {
            return 0;
        }

        long totalCount = 0;
        // 假设data是一个Map类型
        for (List<ResponseInfo> list : data.values()) {
            if (list != null) {
                totalCount += list.size();
            }
        }
        return totalCount;
    }

    // 返回 milliseconds
    public static float getAverageTime() {
        long totalCost = 0;
        Set<String> set = data.keySet();
        for (List<ResponseInfo> list : data.values()) {
            if (list != null) {
                for (ResponseInfo responseInfo : list) {
                    totalCost += responseInfo.getResponseTime() - responseInfo.getRequestTime();
                }
            }
        }

        return totalCost / getInvokeCount();
    }

    // 获取昨天的 24h 的调用次数
    public static int[] getYesterdayInvokeInfo() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);

        // 设置时间为昨天的开始（00:00:00）
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startOfYesterday = cal.getTimeInMillis();

        // 设置时间为昨天的结束（23:59:59）
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        long endOfYesterday = cal.getTimeInMillis();

        int[] hourArray = new int[12];
        List<ResponseInfo> responseInfos = ResponseInfoHelper.getInstance().getDurationInfo(startOfYesterday, endOfYesterday);
        if (responseInfos == null) {
            SLog.i(TAG, "responseInfos is null");
        }
        SLog.i(TAG, "responseInfos is: " + responseInfos);
        for (ResponseInfo responseInfo : responseInfos) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(responseInfo.getRequestTime());
            int hour = c.get(Calendar.HOUR_OF_DAY);
            hourArray[hour]++;
        }
        return hourArray;
    }
}
