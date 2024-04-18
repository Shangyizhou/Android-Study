package com.example.androidnote.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidnote.R;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.log.SLog;

public class SettingActivity extends BaseActivity {

    private static final String TAG = UserInfoActivity.class.getSimpleName();
    public static void startUp(Context context) {
        SLog.i(TAG, "[UserInfoActivity] startUp");
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {

    }

    private void setData() {

    }
}