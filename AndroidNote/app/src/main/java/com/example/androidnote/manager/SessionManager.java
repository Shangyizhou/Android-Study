package com.example.androidnote.manager;

import com.example.androidnote.db.helper.MessageHelper;
import com.example.androidnote.db.helper.SessionHelper;
import com.example.androidnote.model.Message;
import com.example.androidnote.model.Session;
import com.shangyizhou.develop.helper.UUIDUtil;
import com.shangyizhou.develop.log.SLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SessionManager {
    private static String TAG = SessionManager.class.getSimpleName();
    private static volatile SessionManager instance;

    private static List<Session> sessionsList;
    private static HashMap<Session, List<Message>> messageMap;
    private static Session mCurrentSession;

    private SessionManager() {
        init();
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化操作不需要将所有消息都加载出来，只需要将当前会话的messageList加载出来即可
     */
    public void init() {
        SLog.i(TAG, "init");
        loadSessionList();
        messageMap = new HashMap<>();
    }

    /**
     * 数据库没有会话，则初始化一个会话
     */
    private void addFirstSession() {
        SLog.i(TAG, "addFirstSession");
        Session session = new Session();
        session.setName("" + new Date(System.currentTimeMillis()));
        session.setDesc("hello ~");
        session.setRobotId("");
        session.setUserId(BmobManager.getInstance().getObjectId());
        session.setUrl("");
        session.setSessionId(UUIDUtil.getUUID());
        session.setIsDel(false);
        session.setCreateTime(System.currentTimeMillis());
        session.setUpdateTime(System.currentTimeMillis());
        sessionsList.add(session);
    }

    /**
     * 从数据库加载所有会话
     */
    private void loadSessionList() {
        SLog.i(TAG, "loadSessionList");
        sessionsList = SessionHelper.getInstance().takeAllByUser(BmobManager.getInstance().getObjectId());
        SLog.i(TAG, "sessionsList size = " + sessionsList.size());
        if (sessionsList == null || sessionsList.size() == 0) {
            // 数据库没有会话
            sessionsList = new ArrayList<>();
            addFirstSession();
        }
    }

    /**
     * 从数据库加载某会话的历史消息
     * @param session
     */
    private void loadSessionMessage(Session session) {
        SLog.i(TAG, "loadSessionMessage");
        List<Message> messageList = MessageHelper.getInstance().getMessageListBySession(session.getSessionId());
        if  (messageList == null) {
            messageList = new ArrayList<>();
            messageMap.put(session, messageList);
            return;
        }
        messageMap.put(session, messageList);
    }

    /**
     * 保存旧有的会话和历史消息到数据库
     * reload
     * destroy
     */
    public void saveHistoryMessage() {
        SLog.i(TAG, "saveHistoryMessage");
        // 更新当前session修改时间
        // mCurrentSession.setUpdateTime(System.currentTimeMillis());
        SessionHelper.getInstance().save(sessionsList);

        // 根据messageMap保存所有会话历史消息
        for (Session session : messageMap.keySet()) {
            MessageHelper.getInstance().saveMessageList(messageMap.get(session));
        }
    }

    /**
     * 创建新会话
     */
    public void addNewSession(Session session) {
        if (session != null && sessionsList != null) {
            sessionsList.add(session);
        }
    }

    /**
     * 从缓存获取所有会话
     */
    public List<Session> getSessionList() {
        if (sessionsList == null) {
            // 还未从数据库加载会话
            loadSessionList();
        }
        return sessionsList;
    }

    /**
     * 从缓存获取某会话的历史消息
     */
    public List<Message> getSessionMessages(Session session) {
        if (messageMap == null) {
            messageMap = new HashMap<>();
            loadSessionMessage(session);
        } else if (messageMap.get(session) == null) {
            loadSessionMessage(session);
        }
        return messageMap.get(session);
    }

    public Session getLastSession() {
        if (sessionsList == null) {
            loadSessionList();
        }

        // 按照sessionList的中的session的create_time时间顺序获取距离现在最近的一个session
        Session lastSession = null;
        for (Session session : sessionsList) {
            if (lastSession == null) {
                lastSession = session;
            } else if (session.getCreateTime() > lastSession.getCreateTime()) {
                lastSession = session;
            }
        }
        return lastSession;
    }
}
