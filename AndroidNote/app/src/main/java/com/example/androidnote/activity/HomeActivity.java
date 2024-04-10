package com.example.androidnote.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidnote.R;
import com.example.androidnote.model.News;
import com.google.gson.Gson;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.log.SLog;

public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private Button btnData;
    private Button btnChat;
    private Button btnNews;
    private Button btnUser;
    public static void startUp(Context context) {
        SLog.i(TAG, "[HomeActivity] startUp");
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_home);
        initView();
    }

    private void initView() {
        btnChat = findViewById(R.id.btn_to_chat);
        btnData = findViewById(R.id.btn_to_data);
        btnNews = findViewById(R.id.btn_to_news);
        btnUser = findViewById(R.id.btn_to_user);
        btnChat.setOnClickListener(this);
        btnData.setOnClickListener(this);
        btnNews.setOnClickListener(this);
        btnUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_to_chat) {
            ChatActivity.startUp(HomeActivity.this);
        } else if (id == R.id.btn_to_data) {
            DataActivity.startUp(HomeActivity.this);
        } else if (id == R.id.btn_to_news) {
            NewsActivity.startUp(HomeActivity.this);
        } else if (id == R.id.btn_to_user) {
            UserActivity.startUp(HomeActivity.this);
        }
    }
}