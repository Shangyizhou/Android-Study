package com.projectnote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.projectnote.framelayout.FrameLayoutDemo;
import com.projectnote.permission.PermissionActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        Button freshButton = findViewById(R.id.btn_fresh);
        Button frameLayoutButton = findViewById(R.id.btn_frame_layout);
        Button fragmentButton = findViewById(R.id.btn_fragment);
        Button permissionButton = findViewById(R.id.btn_permission);
        freshButton.setOnClickListener(this);
        frameLayoutButton.setOnClickListener(this);
        fragmentButton.setOnClickListener(this);
        permissionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fresh:
                Log.d("sss", "onClick: R.id.btn_fresh");
                break;
            case R.id.btn_frame_layout:
                Log.d("sss", "onClick: R.id.btn_frame_layout");
                Intent frameIntent = new Intent(this, FrameLayoutDemo.class);
                startActivity(frameIntent);
                break;
            case R.id.btn_fragment:
                Log.d("sss", "onClick: R.id.btn_fragment");
                Intent fragmentIntent = new Intent(this, FragmentActivity.class);
                startActivity(fragmentIntent);
                break;
            case R.id.btn_permission:
                Log.d("sss", "onClick: R.id.btn_permission");
                Intent permissionIntent = new Intent(this, PermissionActivity.class);
                startActivity(permissionIntent);
                break;
            default:
                Log.d("sss", "onClick: default choice");
        }
    }
}