package com.example.androidnote.db.helper;

import com.example.androidnote.db.DaoManager;
import com.example.androidnote.db.StarDao;
import com.example.androidnote.model.Star;
import com.shangyizhou.develop.log.SLog;

import java.util.List;

public class StarHelper {
    private static final String TAG = StarHelper.class.getSimpleName();
    private static volatile StarHelper instance;
    StarDao mStarDao;

    public StarHelper() {
        mStarDao = DaoManager.getInstance().getDaoSession().getStarDao();
    }

    public static StarHelper getInstance() {
        if (instance == null) {
            synchronized (StarHelper.class) {
                if (instance == null) {
                    instance = new StarHelper();
                }
            }
        }
        return instance;
    }

    private boolean isDataBaseValid() {
        return mStarDao != null;
    }

    public void save(Star star) {
        SLog.i(TAG, "save Star: " + star);
        if (!isDataBaseValid()) {
            return;
        }
        mStarDao.insertOrReplace(star);
    }

    public List<Star> getStarByUser(String userId) {
        SLog.i(TAG, "getStarByUser: userId=" + userId);
        if (!isDataBaseValid()) {
            return null;
        }
        return mStarDao.queryBuilder().
                where(StarDao.Properties.UserId.eq(userId)).
                where(StarDao.Properties.IsStar.eq(true)).
                list();
    }
}
