package com.example.androidnote.manager;

import java.util.HashMap;

public class DataCollectionManager {
    private static String TAG = DataCollectionManager.class.getSimpleName();
    private static volatile DataCollectionManager instance;
    private static HashMap<String, String> modelCallNumber = new HashMap<>();

    private DataCollectionManager() {
    }

    public static DataCollectionManager getInstance() {
        if (instance == null) {
            synchronized (DataCollectionManager.class) {
                if (instance == null) {
                    instance = new DataCollectionManager();
                }
            }
        }
        return instance;
    }

    public static void allModelCallNumber() {

    }
}
