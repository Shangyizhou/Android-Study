package com.example.androidnote.db.helper;

import static com.example.androidnote.constant.Constants.DEFAULT_USER_ID;
import static com.example.androidnote.constant.Constants.INIT_ROBOT_MODEL;
import static com.example.androidnote.constant.Constants.ROBOT_MODEL_BASIC;
import static com.example.androidnote.constant.Constants.ROBOT_MODEL_LANGUAGE;
import static com.example.androidnote.constant.Constants.ROBOT_MODEL_NORMAL;

import com.example.androidnote.db.DaoManager;
import com.example.androidnote.db.RobotModelDao;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.model.RobotModel;
import com.shangyizhou.develop.helper.SPUtil;
import com.shangyizhou.develop.helper.UUIDUtil;
import com.shangyizhou.develop.log.SLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RobotHelper {
    private static final String TAG = RobotHelper.class.getSimpleName();
    private static volatile RobotHelper instance;
    RobotModelDao mRobotModelDao;

    public RobotHelper() {
        mRobotModelDao = DaoManager.getInstance().getDaoSession().getRobotModelDao();
        init();
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

    public List<RobotModel> takeAll() {
        if (isDataBaseValid()) {
            return mRobotModelDao.queryBuilder()
                    .where(RobotModelDao.Properties.IsDel.eq(false))
                    .orderDesc(RobotModelDao.Properties.CreateTime)
                    .list();
        }
        return null;
    }

    public List<RobotModel> takeAllByType(String type) {
        if (isDataBaseValid()) {
            return mRobotModelDao.queryBuilder()
                    .where(RobotModelDao.Properties.IsDel.eq(false))
                    .where(RobotModelDao.Properties.Type.eq(type))
                    .orderDesc(RobotModelDao.Properties.CreateTime)
                    .list();
        }
        return null;
    }
    List<RobotModel> modelList = new ArrayList<>();
    List<String> queryList = new ArrayList<>();


    String[] titles = {
            "数据库学习机器人",
            "操作系统学习机器人",
            "数据结构与算法学习机器人",
            "计算机网络学习机器人",
            "JAVA语言学习机器人",
            "C++语言学习机器人",
            "Golang语言学习机器人"
    };

    String[] descs = {
            "数据库学习辅助机器人，帮助理解数据库的概念和使用",
            "操作系统学习辅助机器人，帮助理解操作系统的概念和使用",
            "数据结构与算法学习辅助机器人，帮助理解数据结构与算法的概念和算法实现",
            "计算机网络学习辅助机器人，帮助理解计算机网络的概念和各类网络协议实现",
            "JAVA语言学习机器人，帮助理解JAVA编程语言的使用和相关高级语法",
            "C++语言学习机器人，帮助理解C++编程语言的使用和相关高级语法",
            "Golang语言学习机器人，帮助理解Go编程语言的使用和相关高级语法"
    };

    String[][] questions = {
            {"数据库是什么", "数据库的分类", "数据库可以做什么"},
            {"操作系统是什么", "操作系统的分类", "操作系统可以做什么"},
            {"数据结构是什么", "什么是时间复杂度", "什么是空间复杂度"},
            {"计算机网络是什么", "计算机网络有哪些协议", "TCP和UDP区别是什么"},
            {"JAVA是什么", "什么是JDK", "JAVA开发可以做什么"},
            {"C++语言是什么", "C++和C语言区别", "C++开发可以做什么"},
            {"Go语言是什么", "Go语言和Java语言的区别", "Go语言开发可以做什么"},
    };

    String[] beginSays = {
            "您好，我是数据库学习辅助机器人，可以帮助您理解数据库的概念和使用，有什么要问我的呀",
            "您好，我是操作系统学习辅助机器人，可以帮助您理解操作系统的概念和使用，有什么要问我的呀",
            "您好，我是数据结构与算法学习辅助机器人，可以帮助您理解数据结构与算法的概念和使用，有什么要问我的呀",
            "您好，我是计算机网络学习辅助机器人，可以帮助您计算机网络的概念，有什么要问我的呀",
            "您好，我是JAVA编程语言学习辅助机器人，可以帮助您学习使用JAVA语言来开发程序，有什么要问我的呀",
            "您好，我是C++编程语言学习辅助机器人，可以帮助您学习使用C++语言来开发程序，有什么要问我的呀",
            "您好，我是Golang编程语言学习辅助机器人，可以帮助您学习使用Golang语言来开发程序，有什么要问我的呀"
    };

    String[] types = {
        ROBOT_MODEL_BASIC,
        ROBOT_MODEL_BASIC,
        ROBOT_MODEL_BASIC,
        ROBOT_MODEL_BASIC,
        ROBOT_MODEL_LANGUAGE,
        ROBOT_MODEL_LANGUAGE,
        ROBOT_MODEL_LANGUAGE,
    };

    public void init() {
        if (SPUtil.getInstance().getBooleanData(INIT_ROBOT_MODEL, false)) {
            return;
        }
        for (int i = 0; i < titles.length; i++) {
            RobotModel model = new RobotModel();
            List<String> queryList = Arrays.asList(questions[i]);
            model.setTitle(titles[i]);
            model.setDesc(descs[i]);
            model.setRobotId(UUIDUtil.getUUID());
            model.setOwnerId(DEFAULT_USER_ID);
            model.setBeginSay(beginSays[i]);
            model.setQuestions(queryList);
            model.setCreateTime(System.currentTimeMillis());
            model.setUpdateTime(System.currentTimeMillis());
            model.setImageUrl("");
            model.setType(types[i]);

            save(model);
        }
        SPUtil.getInstance().saveBooleanData(INIT_ROBOT_MODEL, true);
    }


}
