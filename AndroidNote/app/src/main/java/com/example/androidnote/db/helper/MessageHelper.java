package com.example.androidnote.db.helper;

import com.example.androidnote.db.DaoManager;
import com.example.androidnote.db.MessageDao;
import com.example.androidnote.db.SessionDao;
import com.example.androidnote.model.Message;
import com.shangyizhou.develop.log.SLog;

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
        SLog.i(TAG, "saveMessageList" + messageList.size());
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
}
