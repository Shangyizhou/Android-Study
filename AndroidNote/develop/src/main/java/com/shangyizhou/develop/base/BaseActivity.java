package com.shangyizhou.develop.base;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.shangyizhou.develop.R;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.model.EventIdCenter;
import com.shangyizhou.develop.model.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        onCreateChildren(savedInstanceState);
    }

    /**
     * 子类重写
     */
    protected abstract void onCreateChildren(Bundle bundle);

    /**
     * 注册eventbus
     */
    @Override
    protected void onResume() {
        super.onResume();
        SLog.i(TAG, "onResume");
        //注册eventbus
        EventBus.getDefault().register(this);
    }

    /**
     * 注销EventBus
     */
    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        SLog.d(TAG, "onStop");
        super.onStop();
    }



    @Override
    protected void onDestroy() {
        SLog.d(TAG, "onDestroy");
        super.onDestroy();
    }

    // 声明订阅的方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event == null || event.message == null) {
            SLog.e(TAG, "【onMessageEvent】event is null");
            return;
        }
        switch (event.name) {
            case EventIdCenter.ROS_CONNECT:
                Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}