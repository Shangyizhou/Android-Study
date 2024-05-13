package com.example.androidnote.db.helper;

import com.example.androidnote.db.DaoManager;
import com.example.androidnote.db.SessionDao;

public class DataHelper {
    private static final String TAG = DataHelper.class.getSimpleName();
    private static volatile DataHelper instance;

    public DataHelper() {
    }

    public static DataHelper getInstance() {
        if (instance == null) {
            synchronized (DataHelper.class) {
                if (instance == null) {
                    instance = new DataHelper();
                }
            }
        }
        return instance;
    }





}
