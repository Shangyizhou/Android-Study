package com.example.androidnote.db.helper;

import com.example.androidnote.db.AdviceDao;
import com.example.androidnote.db.DaoManager;
import com.example.androidnote.model.Advice;
import com.shangyizhou.develop.log.SLog;

import java.util.List;

public class AdviceHelper {
    private static final String TAG = AdviceHelper.class.getSimpleName();
    private static volatile AdviceHelper instance;
    AdviceDao mAdviceDao;

    public AdviceHelper() {
        mAdviceDao = DaoManager.getInstance().getDaoSession().getAdviceDao();
    }

    public static AdviceHelper getInstance() {
        if (instance == null) {
            synchronized (AdviceHelper.class) {
                if (instance == null) {
                    instance = new AdviceHelper();
                }
            }
        }
        return instance;
    }

    private boolean isDataBaseValid() {
        return mAdviceDao != null;
    }

    public void save(Advice advice) {
        SLog.i(TAG, "save Advice: " + advice);
        if (!isDataBaseValid()) {
            return;
        }
        mAdviceDao.insertOrReplace(advice);
    }

    public void save(List<Advice> adviceList) {
        SLog.i(TAG, "save adviceList size is " + adviceList.size());
        if (!isDataBaseValid()) {
            return;
        }
        for (Advice advice : adviceList) {
            mAdviceDao.insertOrReplace(advice);
        }
    }

    public List<Advice> getAllAdvice() {
        SLog.i(TAG, "getAllAdvice: ");
        if (!isDataBaseValid()) {
            return null;
        }
        return mAdviceDao.loadAll();
    }

    public List<Advice> getAdviceByUser(String userId) {
        SLog.i(TAG, "getAllAdvice: userId=" + userId);
        if (!isDataBaseValid()) {
            return null;
        }
        return mAdviceDao.queryBuilder().where(AdviceDao.Properties.UserId.eq(userId)).list();
    }
}
