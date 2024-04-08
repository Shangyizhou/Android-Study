package com.example.androidnote.activity;

import android.app.Dialog;
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
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.helper.SimpleTaskExecutor;
import com.shangyizhou.develop.helper.ToastUtil;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.ui.dialog.DialogView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class RegisterActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    public static void startUp(Context context) {
        SLog.i(TAG, "[LoginActivity] startUp");
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_register);
        initView();
    }

    private EditText etPhone;
    private EditText etPasswd;
    private EditText etPasswdAgain;
    private Button btnRegister;
    private DialogView dialog;

    private void initView() {
        dialog = new DialogView(this);
        etPhone = findViewById(R.id.et_phone);
        etPasswd = findViewById(R.id.et_passwd);
        btnRegister = findViewById(R.id.btn_register);
        etPasswdAgain = findViewById(R.id.et_passwd_again);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        String name = etPhone.getText().toString();
        String passwd = etPasswd.getText().toString();
        String passwdAgain = etPasswdAgain.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(passwd) || TextUtils.isEmpty(passwdAgain)) {
            ToastUtil.getInstance().showToast("用户名或者密码为空");
            return;
        }

        if (!passwd.equals(passwdAgain)) {
            ToastUtil.getInstance().showToast("两次密码输入不一致");
            return;
        }
        BmobManager.getInstance().signUp(name, passwd, new BmobManager.RegisterCallback() {
            @Override
            public void onSuccess(BmobUser user) {
                SLog.i(TAG, "Register onSuccess");
                dialog.setMessage("注册成功，即将跳转到登录页面");
                dialog.show();
                SimpleTaskExecutor.scheduleDelayed(new SimpleTaskExecutor.Task() {
                    @Override
                    public String getName() {
                        return null;
                    }

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                LoginActivity.startUp(RegisterActivity.this);                           }
                        });
                    }
                }, 1500);
            }

            @Override
            public void onFailure(BmobException e) {
                SLog.i(TAG, "Login onFailure");
                e.printStackTrace();
            }
        });
    }
}