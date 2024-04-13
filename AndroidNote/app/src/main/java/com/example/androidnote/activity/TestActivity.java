package com.example.androidnote.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.example.androidnote.R;
import com.example.androidnote.activity.news.NewsActivity;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.helper.PermissionUtils;
import com.shangyizhou.develop.helper.ToastUtil;
import com.shangyizhou.develop.log.SLog;

import java.util.List;

public class TestActivity extends BaseActivity implements View.OnClickListener, EventListener {
    private static final String TAG = TestActivity.class.getSimpleName();
    private Button btnData;
    private Button btnChat;
    private Button btnNews;
    private Button btnUser;
    private Button btnPermission;
    protected TextView txtResult;//识别结果
    protected Button startBtn;//开始识别  一直不说话会自动停止，需要再次打开
    protected Button stopBtn;//停止识别

    private EventManager asr;//语音识别核心库


    public static void startUp(Context context) {
        SLog.i(TAG, "[HomeActivity] startUp");
        Intent intent = new Intent(context, TestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_home2);
        initView();
        requestPermission();
        //初始化EventManager对象
        asr = EventManagerFactory.create(this, "asr");
        //注册自己的输出事件类
        asr.registerListener(this); //  EventListener 中 onEvent方法
    }

    private void initView() {
        btnChat = findViewById(R.id.btn_to_chat);
        btnData = findViewById(R.id.btn_to_data);
        btnNews = findViewById(R.id.btn_to_news);
        btnUser = findViewById(R.id.btn_to_user);
        btnPermission = findViewById(R.id.btn_test_permission);
        btnChat.setOnClickListener(this);
        btnData.setOnClickListener(this);
        btnNews.setOnClickListener(this);
        btnUser.setOnClickListener(this);
        btnPermission.setOnClickListener(this);

        /**
         * 语音识别
         */
        txtResult = (TextView) findViewById(R.id.tv_txt);
        startBtn = (Button) findViewById(R.id.btn_start);
        stopBtn = (Button) findViewById(R.id.btn_stop);

        startBtn.setOnClickListener(new View.OnClickListener() {//开始
            @Override
            public void onClick(View v) {
                asr.send(SpeechConstant.ASR_START, "{}", null, 0, 0);
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {//停止
            @Override
            public void onClick(View v) {
                asr.send(SpeechConstant.ASR_STOP, "{}", null, 0, 0);
            }
        });


    }

    private String[] mRequirePermissions = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private static final int REQUEST_PERMISSION_CODE = 200;
    private void requestPermission() {
        // 判断是否有权限
        if (!PermissionUtils.hasPermissions(this, mRequirePermissions)) {
            // 申请权限
            PermissionUtils.requestPermissions(this, REQUEST_PERMISSION_CODE, mRequirePermissions);
        }
    }

    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            // 识别相关的结果都在这里
            if (params == null || params.isEmpty()) {
                return;
            }
            if (params.contains("\"final_result\"")) {
                // 一句话的最终识别结果
                txtResult.setText(params);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //发送取消事件
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);

        //退出事件管理器
        // 必须与registerListener成对出现，否则可能造成内存泄露
        asr.unregisterListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_to_chat) {
            ChatActivity.startUp(TestActivity.this);
        } else if (id == R.id.btn_to_data) {
            DataActivity.startUp(TestActivity.this);
        } else if (id == R.id.btn_to_news) {
            NewsActivity.startUp(TestActivity.this);
        } else if (id == R.id.btn_to_user) {
            UserActivity.startUp(TestActivity.this);
        } else if (id == R.id.btn_test_permission) {
            testPermission();
        }
    }

    private static final int REQUEST_CODE = 100;
    private void testPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 先判断有没有权限
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // writeFile();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        } else {
            // writeFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults, new PermissionUtils.PermissionCallbacks() {
            @Override
            public void onPermissionsAllGranted(int requestCode, List<String> perms, boolean isAllGranted) {
                SLog.i(TAG, "授权成功" + String.valueOf(perms));
                ToastUtil.getInstance().showToast("授权成功" + String.valueOf(perms));
                SLog.i(TAG, "授权成功" + String.valueOf(perms));
            }

            @Override
            public void onPermissionsDenied(int requestCode, List<String> perms) {
                ToastUtil.getInstance().showToast("授权失败" + String.valueOf(perms));
                SLog.i(TAG, "授权失败" + String.valueOf(perms));
            }
        });
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}