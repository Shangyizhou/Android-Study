package com.example.androidnote.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidnote.R;
import com.shangyizhou.develop.base.BaseActivity;

public class UserActivity extends BaseActivity {

    private static final String TAG = UserActivity.class.getSimpleName();
    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_user);
    }

    private void initView() {

    }
}