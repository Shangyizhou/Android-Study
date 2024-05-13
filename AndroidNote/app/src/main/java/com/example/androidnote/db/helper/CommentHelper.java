package com.example.androidnote.db.helper;

import com.example.androidnote.db.CommentDao;
import com.example.androidnote.db.DaoManager;
import com.example.androidnote.model.Comment;
import com.shangyizhou.develop.log.SLog;

import java.util.List;

public class CommentHelper {
    private static final String TAG = CommentHelper.class.getSimpleName();
    private static volatile CommentHelper instance;
    CommentDao mCommentDao;

    public CommentHelper() {
        mCommentDao = DaoManager.getInstance().getDaoSession().getCommentDao();
    }

    public static CommentHelper getInstance() {
        if (instance == null) {
            synchronized (CommentHelper.class) {
                if (instance == null) {
                    instance = new CommentHelper();
                }
            }
        }
        return instance;
    }

    private boolean isDataBaseValid() {
        return mCommentDao != null;
    }

    public void save(Comment comment) {
        SLog.i(TAG, "save comment: " + comment);
        if (!isDataBaseValid()) {
            return;
        }
        mCommentDao.insertOrReplace(comment);
    }

    public void save(List<Comment> commentList) {
        SLog.i(TAG, "save commentList size is " + commentList.size());
        if (!isDataBaseValid()) {
            return;
        }
        for (Comment comment : commentList) {
            mCommentDao.insertOrReplace(comment);
        }
    }

    public List<Comment> getCommentByRobot(String robotId) {
        SLog.i(TAG, "getBookByUser: robotId=" + robotId);
        if (!isDataBaseValid()) {
            return null;
        }

        return mCommentDao.queryBuilder()
                .where(CommentDao.Properties.RobotId.eq(robotId))
                .orderAsc(CommentDao.Properties.CreateTime)
                .list();
    }
}
