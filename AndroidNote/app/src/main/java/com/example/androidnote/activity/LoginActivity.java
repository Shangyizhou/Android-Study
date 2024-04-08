package com.example.androidnote.activity;

import static com.example.androidnote.activity.MainActivity.startUp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidnote.R;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.model.IMUser;
import com.example.androidnote.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.helper.ToastUtil;
import com.shangyizhou.develop.log.SLog;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";

    public static void startUp(Context context) {
        SLog.i(TAG, "[LoginActivity] startUp");
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_login);
        initView();
    }

    private EditText etPhone;
    private EditText etPasswd;
    private Button btnLogin;
    private TextView btnRegister;

    private void initView() {
        etPhone = findViewById(R.id.et_phone);
        etPasswd = findViewById(R.id.et_passwd);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.to_register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void login() {
        String name = etPhone.getText().toString();
        String passwd = etPasswd.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(passwd)) {
            ToastUtil.getInstance().showToast("用户名或者密码为空");
            return;
        }
        BmobManager.getInstance().login(name, passwd, new BmobManager.LoginCallback() {
            @Override
            public void onSuccess(BmobUser user) {
                SLog.i(TAG, "Login onSuccess");
                ChatActivity.startUp(LoginActivity.this);
            }

            @Override
            public void onFailure(BmobException e) {
                SLog.i(TAG, "Login onFailure");
                e.printStackTrace();
            }
        });
    }

    private void register() {
        RegisterActivity.startUp(this);
    }
}