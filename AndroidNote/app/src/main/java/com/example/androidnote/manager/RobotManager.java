package com.example.androidnote.manager;

import com.example.androidnote.model.RobotModel;
import com.shangyizhou.develop.log.SLog;

import java.util.HashMap;
import java.util.List;

public class RobotManager {
    private static String TAG = RobotManager.class.getSimpleName();
    private static volatile RobotManager instance;

    private static List<RobotModel> robotModelList;

    private RobotManager() {
        init();
    }

    public static RobotManager getInstance() {
        if (instance == null) {
            synchronized (RobotManager.class) {
                if (instance == null) {
                    instance = new RobotManager();
                }
            }
        }
        return instance;
    }


    public void init() {
        SLog.i(TAG, "init");
    }

    /**
     * 获取所有公开的Robot
     */


}
