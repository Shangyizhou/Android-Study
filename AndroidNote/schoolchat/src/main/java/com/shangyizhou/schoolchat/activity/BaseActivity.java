package com.shangyizhou.schoolchat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.shangyizhou.schoolchat.App;
import com.shangyizhou.schoolchat.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        onCreateChildren(savedInstanceState);
    }

    protected abstract void onCreateChildren(Bundle bundle);

    public static void showToast(final String tip) {
        App.getApp().getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(App.getApp(), tip, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}