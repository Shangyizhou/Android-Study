package com.example.androidnote.db.helper;

import com.example.androidnote.db.DaoManager;
import com.example.androidnote.db.MessageDao;
import com.example.androidnote.model.Message;
import com.example.androidnote.util.DateHelper;
import com.example.androidnote.util.DateHelper.DatePeriod;
import com.shangyizhou.develop.log.SLog;

import java.text.DecimalFormat;
import java.util.List;

public class MessageHelper {
    private static final String TAG = MessageHelper.class.getSimpleName();
    private static volatile MessageHelper instance;
    MessageDao mMessageDao;

    public MessageHelper() {
        mMessageDao = DaoManager.getInstance().getDaoSession().getMessageDao();
    }

    public static MessageHelper getInstance() {
        if (instance == null) {
            synchronized (MessageHelper.class) {
                if (instance == null) {
                    instance = new MessageHelper();
                }
            }
        }
        return instance;
    }

    private boolean isDataBaseValid() {
        return mMessageDao != null;
    }

    public void saveMessageList(List<Message> messageList) {
        SLog.i(TAG, "saveMessageList size is " + messageList.size());
        if (!isDataBaseValid()) {
            return;
        }
        for (Message message : messageList) {
            mMessageDao.insertOrReplace(message);
        }
    }

    public List<Message> getMessageListBySession(String sessionId) {
        SLog.i(TAG, "getMessageListBySession: " + sessionId);
        if (!isDataBaseValid()) {
            return null;
        }
        return mMessageDao.queryBuilder().where(MessageDao.Properties.SessionId.eq(sessionId)).list();
    }

    /**
     * DATA
     * @param begin
     * @param end
     * @return
     */
    public List<Message> getMessageListByTime(long begin, long end) {
        SLog.i(TAG, "getMessageListByTime: " + begin + "-" +  end);
        if (!isDataBaseValid()) {
            return null;
        }
        return mMessageDao.queryBuilder().where(MessageDao.Properties.SendTime.in(begin, end)).list();
    }

    //某一年的数据是否为空
    public boolean isEmpty(int year) {
        DatePeriod period = DateHelper.getYearPeriod(year);
        List<Message> messageList = getMessageListByTime(period.begin, period.end);
        return messageList.size() > 0;
    }

    //在某一年中的 某一月对应的数据是否为空
    public boolean isEmpty(int year, int month) {
        DatePeriod period = DateHelper.getMonthPeriod(year, month);
        List<Message> messageList = getMessageListByTime(period.begin, period.end);
        return messageList.size() > 0;
    }

    //计算平均值
    // public  Double getWeightAverage(DatePeriod period) {
    //     String condition = WeightDB.getInstance().makeCondition(period.begin, period.end);
    //     List<Weight> weights = WeightDB.getInstance().query(condition);
    //     Double average = 0.0;
    //     if (weights.isEmpty()) {
    //         return average;
    //     }
    //     for (int i = 0; i < weights.size(); i++) {
    //         average += Double.valueOf(weights.get(i).value);
    //     }
    //     average = average / weights.size();
    //     DecimalFormat format = new DecimalFormat("0.00");
    //     average = Double.valueOf(format.format(average).toString());
    //     return average;
    // }
}
