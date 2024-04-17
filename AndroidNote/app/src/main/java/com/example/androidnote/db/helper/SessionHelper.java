package com.example.androidnote.db.helper;

import com.example.androidnote.db.DaoManager;
import com.example.androidnote.db.SessionDao;
import com.example.androidnote.model.Session;
import com.shangyizhou.develop.log.SLog;

import java.util.List;

public class SessionHelper {
    private static final String TAG = SessionHelper.class.getSimpleName();
    private static volatile SessionHelper instance;
    SessionDao mSessionDao;

    public SessionHelper() {
        mSessionDao = DaoManager.getInstance().getDaoSession().getSessionDao();
    }

    public static SessionHelper getInstance() {
        if (instance == null) {
            synchronized (SessionHelper.class) {
                if (instance == null) {
                    instance = new SessionHelper();
                }
            }
        }
        return instance;
    }

    private boolean isDataBaseValid() {
        return mSessionDao != null;
    }

    public void save(Session session) {
        SLog.i(TAG, "save: session" + session);
        if (isDataBaseValid()) {
            mSessionDao.insertOrReplace(session);
        }
    }

    public void save(List<Session> sessions) {
        SLog.i(TAG, "save: sessionList" + sessions);
        if (isDataBaseValid()) {
            for (Session session : sessions) {
                mSessionDao.insertOrReplace(session);
            }
        }
    }


    /**
     * 取出某个会话
     * @param sessionId
     * @return
     */
    public Session takeBySessionID(String sessionId) {
        SLog.i(TAG, "take: session");
        if (isDataBaseValid()) {
            return mSessionDao.queryBuilder()
                    .where(SessionDao.Properties.SessionId.eq(sessionId))
                    .unique();
        }
        return null;
    }

    /**
     * BmobUser.getObjectId() 获取当前用户的唯一标示
     * 取出该用户的所有历史会话
     * 按照时间倒序排列
     * @param userId
     * @return
     */
    public List<Session> takeAllByUser(String userId) {
        if (isDataBaseValid()) {
            return mSessionDao.queryBuilder()
                    .where(SessionDao.Properties.UserId.eq(userId))
                    .where(SessionDao.Properties.IsDel.eq(false))
                    .orderDesc(SessionDao.Properties.CreateTime)
                    .list();
        }
        return null;
    }

    /**
     * 设置此session删除，实际不可见
     * @param sessionId
     */
    public void setIsDelTrue(String sessionId) {
        if (isDataBaseValid()) {
            Session session = takeBySessionID(sessionId);
            if (session != null) {
                session.setIsDel(true);
                mSessionDao.update(session);
            }
        }
    }
}
