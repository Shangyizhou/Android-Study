package com.example.androidnote.db.helper;

import com.example.androidnote.db.DaoManager;
import com.example.androidnote.db.RobotModelDao;
import com.example.androidnote.model.RobotModel;
import com.shangyizhou.develop.log.SLog;

import java.util.List;

public class RobotHelper {
    private static final String TAG = RobotHelper.class.getSimpleName();
    private static volatile RobotHelper instance;
    RobotModelDao mRobotModelDao;

    public RobotHelper() {
        mRobotModelDao = DaoManager.getInstance().getDaoSession().getRobotModelDao();
    }

    public static RobotHelper getInstance() {
        if (instance == null) {
            synchronized (RobotHelper.class) {
                if (instance == null) {
                    instance = new RobotHelper();
                }
            }
        }
        return instance;
    }

    private boolean isDataBaseValid() {
        return mRobotModelDao != null;
    }

    public void save(List<RobotModel> models) {
        SLog.i(TAG, "save: RobotModelList" + models);
        if (isDataBaseValid()) {
            for (RobotModel model : models) {
                mRobotModelDao.insertOrReplace(model);
            }
        }
    }

    public void save(RobotModel model) {
        SLog.i(TAG, "save: RobotModel" + model);
        if (isDataBaseValid()) {
            mRobotModelDao.insertOrReplace(model);
        }
    }

    /**
     * 取出某个模型
     *
     * @param robotId
     * @return
     */
    public RobotModel takeByRobotID(String robotId) {
        SLog.i(TAG, "take: robot");
        if (isDataBaseValid()) {
            return mRobotModelDao.queryBuilder()
                    .where(RobotModelDao.Properties.RobotId.eq(robotId))
                    .unique();
        }
        return null;
    }

    public List<RobotModel> takeAllByUser(String robotId) {
        if (isDataBaseValid()) {
            return mRobotModelDao.queryBuilder()
                    .where(RobotModelDao.Properties.RobotId.eq(robotId))
                    .where(RobotModelDao.Properties.IsDel.eq(false))
                    .orderDesc(RobotModelDao.Properties.CreateTime)
                    .list();
        }
        return null;
    }
}
