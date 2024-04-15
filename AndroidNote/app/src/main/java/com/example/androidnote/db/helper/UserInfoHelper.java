package com.example.androidnote.db.helper;

import android.content.Context;

import com.example.androidnote.db.DaoManager;
import com.example.androidnote.db.UserInfoDao;
import com.example.androidnote.model.UserInfo;
import com.shangyizhou.develop.log.SLog;

public class UserInfoHelper {
    private static final String TAG = UserInfoHelper.class.getSimpleName();
    private static volatile UserInfoHelper instance;
    UserInfoDao mUserInfoDao;
    private Context context;

    public UserInfoHelper() {
        mUserInfoDao = DaoManager.getInstance().getDaoSession().getUserInfoDao();
    }

    public static UserInfoHelper getInstance() {
        if (instance == null) {
            synchronized (DaoManager.class) {
                if (instance == null) {
                    instance = new UserInfoHelper();
                }
            }
        }
        return instance;
    }
    private boolean isDataBaseValid() {
        return mUserInfoDao != null;
    }

    public void save(UserInfo userInfo) {
        if (!isDataBaseValid()) {
            SLog.e(TAG, "insert error");
            return;
        }

        try {
            mUserInfoDao.insertOrReplace(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserInfo getUserByName(String name) {
        if (!isDataBaseValid()) {
            SLog.e(TAG, "insert error");
            return null;
        }

        return mUserInfoDao.queryBuilder().where(UserInfoDao.Properties.UserName.eq(name)).unique();
    }
}
