package com.example.androidnote.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.androidnote.R;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.helper.ToastUtil;
import com.shangyizhou.develop.log.SLog;

public class Home2Activity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = Home2Activity.class.getSimpleName();
    private Button btnData;
    private Button btnChat;
    private Button btnNews;
    private Button btnUser;
    private Button btnPermission;

    public static void startUp(Context context) {
        SLog.i(TAG, "[HomeActivity] startUp");
        Intent intent = new Intent(context, Home2Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_home2);
        initView();
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
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_to_chat) {
            ChatActivity.startUp(Home2Activity.this);
        } else if (id == R.id.btn_to_data) {
            DataActivity.startUp(Home2Activity.this);
        } else if (id == R.id.btn_to_news) {
            NewsActivity.startUp(Home2Activity.this);
        } else if (id == R.id.btn_to_user) {
            UserActivity.startUp(Home2Activity.this);
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    ToastUtil.getInstance().showToast("权限" + permissions[i] + "被拒绝");
                } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.getInstance().showToast("权限" + permissions[i] + "被允许");
                }
            }
        }
    }
}