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
import com.example.androidnote.model.RobotModel;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.log.SLog;

public class AutoCreateRobotActivity extends BaseActivity implements View.OnClickListener {
    public static void startUp(Context context, Bundle bundle) {
        SLog.i(TAG, "[AutoCreateRobotActivity] startUp");
        Intent intent = new Intent(context, AutoCreateRobotActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent, bundle);
    }

    private static final String TAG = AutoCreateRobotActivity.class.getSimpleName();
    @Override
    protected void onCreateChildren(Bundle bundle) {
        // super.onCreateChildren(bundle);
        setContentView(R.layout.activity_auto_create_robot);
        Intent intent = getIntent();
        if (intent != null) {
            bundle = intent.getExtras();
        }
        initView();
    }

    Button freeBtn;
    Button aiBtn;
    private void initView() {
        freeBtn = findViewById(R.id.free_btn);
        aiBtn = findViewById(R.id.ai_btn);
        freeBtn.setOnClickListener(this);
        aiBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.free_btn) {

        } else if (id == R.id.ai_btn) {
            SLog.i(TAG, "call ai for prompt");
        }
    }
}