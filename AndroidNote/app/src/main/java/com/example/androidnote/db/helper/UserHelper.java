package com.example.androidnote.db.helper;

import android.content.Context;

import com.example.androidnote.db.DaoManager;
import com.example.androidnote.db.UserDao;
import com.example.androidnote.model.User;
import com.shangyizhou.develop.log.SLog;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

public class UserHelper {
    private static volatile UserHelper instance;
    UserDao mUserDao;

    private static final String TAG = "UserHelper";

    private Context context;
    public UserHelper() {
        mUserDao = DaoManager.getInstance().getDaoSession().getUserDao();
    }

    public static UserHelper getInstance() {
        if (instance == null) {
            synchronized (DaoManager.class) {
                if (instance == null) {
                    instance = new UserHelper();
                }
            }
        }
        return instance;
    }

    private boolean isDataBaseValid() {
        return mUserDao != null;
    }

    public void insert(User user) {
        if (!isDataBaseValid()) {
            SLog.e(TAG, "insert error");
            return;
        }

        try {
            String name = user.getName();
            /**
             * 解决 Exception：
             * Cannot update entity without key - was it inserted before?
             */
            User oldUser = getUserByName(name);
            if (oldUser != null) {
                user.set_id(oldUser.get_id());
            }
            // mUserDao.save(user);
            if (getUserByName(name) == null) {
                mUserDao.insert(user);
                SLog.d(TAG, "insert success");
            } else {
                mUserDao.update(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据用户的名字得到对应的记录
     */
    public User getUserByName(String name) {
        if (!isDataBaseValid()) {
            return null;
        }
        return mUserDao.queryBuilder().where(UserDao.Properties.Name.eq(name)).unique();
    }
    public void update() {

    }

    /**
     * 根据用户的名字删除对应的记录
     */
    public void removeUser(String name) {
        if (!isDataBaseValid()) {
            return;
        }
        try {
            QueryBuilder<User> qb = mUserDao.queryBuilder();
            DeleteQuery<User> bd = qb.where(UserDao.Properties.Name.eq(name))
                    .buildDelete();
            bd.executeDeleteWithoutDetachingEntities();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
