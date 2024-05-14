package com.example.androidnote.db.helper;

import com.example.androidnote.db.DaoManager;
import com.example.androidnote.db.FansDao;
import com.example.androidnote.model.Fans;
import com.shangyizhou.develop.log.SLog;

import java.util.List;

/**
 * 用户粉丝表
 */
public class FansHelper {
    private static final String TAG = FansHelper.class.getSimpleName();
    private static volatile FansHelper instance;
    FansDao mFansDao;

    public FansHelper() {
        mFansDao = DaoManager.getInstance().getDaoSession().getFansDao();
    }

    public static FansHelper getInstance() {
        if (instance == null) {
            synchronized (FansHelper.class) {
                if (instance == null) {
                    instance = new FansHelper();
                }
            }
        }
        return instance;
    }

    private boolean isDataBaseValid() {
        return mFansDao != null;
    }

    public void save(Fans fans) {
        SLog.i(TAG, "save fans: " + fans);
        if (!isDataBaseValid()) {
            return;
        }
        mFansDao.insertOrReplace(fans);
    }

    /**
     * 获取此robot的所有粉丝
     * @return
     */
    public List<Fans> getFansListByRobot(String robotId) {
        SLog.i(TAG, "getFansListByRobot: robotId=" + robotId);
        if (!isDataBaseValid()) {
            return null;
        }
        return mFansDao.queryBuilder().
                where(FansDao.Properties.RobotId.eq(robotId)).
                where(FansDao.Properties.IsFans.eq(true)).
                list();
    }

    /**
     *
     * @param userId
     * @param robotId
     * 存放的是robot的粉丝
     * 从robot的粉丝列表中查询userId为某某的额粉丝
     * @return
     */
    public Fans getFansByUserAndRobot(String userId, String robotId) {
        if (!isDataBaseValid()) {
            return null;
        }
        return mFansDao.queryBuilder().
                where(FansDao.Properties.RobotId.eq(robotId)).
                where(FansDao.Properties.UserId.eq(userId)).
                where(FansDao.Properties.IsFans.eq(true)).
                unique();
    }
}
