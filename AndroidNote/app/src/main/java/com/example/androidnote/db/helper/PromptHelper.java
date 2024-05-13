package com.example.androidnote.db.helper;

import com.example.androidnote.db.AdviceDao;
import com.example.androidnote.db.DaoManager;
import com.example.androidnote.db.PromptModelDao;
import com.example.androidnote.model.PromptModel;
import com.shangyizhou.develop.log.SLog;

import java.util.List;

public class PromptHelper {
    private static final String TAG = PromptHelper.class.getSimpleName();
    private static volatile PromptHelper instance;
    PromptModelDao mPromptModelDao;

    public PromptHelper() {
        mPromptModelDao = DaoManager.getInstance().getDaoSession().getPromptModelDao();
    }

    public static PromptHelper getInstance() {
        if (instance == null) {
            synchronized (PromptHelper.class) {
                if (instance == null) {
                    instance = new PromptHelper();
                }
            }
        }
        return instance;
    }

    private boolean isDataBaseValid() {
        return mPromptModelDao != null;
    }

    public void save(PromptModel promptModel) {
        SLog.i(TAG, "save promptModel: " + promptModel);
        if (!isDataBaseValid()) {
            return;
        }
        mPromptModelDao.insertOrReplace(promptModel);
    }

    public void save(List<PromptModel> promptModelList) {
        SLog.i(TAG, "save promptModelList size is " + promptModelList.size());
        if (!isDataBaseValid()) {
            return;
        }
        for (PromptModel prompt : promptModelList) {
            mPromptModelDao.insertOrReplace(prompt);
        }
    }

    public List<PromptModel> getPromptByUser(String userId) {
        SLog.i(TAG, "getBookByUser: userId=" + userId);
        if (!isDataBaseValid()) {
            return null;
        }
        return mPromptModelDao.queryBuilder().
                where(AdviceDao.Properties.UserId.eq(userId)).
                list();
    }
}
