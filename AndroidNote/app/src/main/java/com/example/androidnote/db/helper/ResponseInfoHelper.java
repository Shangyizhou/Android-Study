package com.example.androidnote.db.helper;

import android.content.Context;

import com.example.androidnote.db.DaoManager;
import com.example.androidnote.db.ResponseInfoDao;
import com.example.androidnote.model.ResponseInfo;
import com.shangyizhou.develop.log.SLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResponseInfoHelper {
    private static final String TAG = ResponseInfoHelper.class.getSimpleName();
    private static volatile ResponseInfoHelper instance;
    ResponseInfoDao mResponseInfoDao;
    private Context context;

    public ResponseInfoHelper() {
        mResponseInfoDao = DaoManager.getInstance().getDaoSession().getResponseInfoDao();
    }

    public static ResponseInfoHelper getInstance() {
        if (instance == null) {
            synchronized (DaoManager.class) {
                if (instance == null) {
                    instance = new ResponseInfoHelper();
                }
            }
        }
        return instance;
    }

    private boolean isDataBaseValid() {
        return mResponseInfoDao != null;
    }

    public void save(ResponseInfo responseInfo) {
        if (!isDataBaseValid()) {
            SLog.e(TAG, "insert error");
            return;
        }

        try {
            mResponseInfoDao.insertOrReplace(responseInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveHashMap(HashMap<String, List<ResponseInfo>> responseInfos) {
        if (!isDataBaseValid()) {
            SLog.e(TAG, "insert error");
            return;
        }

        List<ResponseInfo> info = responseInfos.get("ERNIE-4.0-8K");
        SLog.d(TAG, "saveHashMap" + info.size() + info);

        try {
            for (String key : responseInfos.keySet()) {
                List<ResponseInfo> infos = responseInfos.get(key);
                if (infos != null) {
                    for (int i = 0; i < infos.size(); ++i) {
                        save(infos.get(i));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ResponseInfo> getInfoByUserName(String name) {
        if (!isDataBaseValid()) {
            return null;
        }
        return mResponseInfoDao.queryBuilder().where(ResponseInfoDao.Properties.UserName.eq(name)).list();
    }

    public HashMap<String, List<ResponseInfo>> getAllModelInfoByUserName(String name) {
        List<ResponseInfo> responseInfos = getInfoByUserName(name);
        if (responseInfos == null) {
            return null;
        }
        HashMap<String, List<ResponseInfo>> map = new HashMap<>();
        for (ResponseInfo responseInfo : responseInfos) {
            String modelName = responseInfo.getModelName();
            if (map.containsKey(modelName)) {
                List<ResponseInfo> infos = map.get(modelName);
                infos.add(responseInfo);
            } else {
                List<ResponseInfo> infos = new ArrayList<>();
                infos.add(responseInfo);
                map.put(modelName, infos);
            }
        }

        return map;
    }

    public List<ResponseInfo> getDurationInfo(long start, long end) {
        SLog.i(TAG, "getDurationInfo start: " + start + " end: " + end);
        List<ResponseInfo> data = mResponseInfoDao.queryBuilder()
                .where(ResponseInfoDao.Properties.RequestTime.ge(start),
                        ResponseInfoDao.Properties.RequestTime.le(end))
                .list();
        return data;
    }

    public void deleteData() {
        if (!isDataBaseValid()) {
            return;
        }
        mResponseInfoDao.deleteAll();
    }
}
