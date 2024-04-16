package com.example.androidnote.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidnote.R;
import com.example.androidnote.activity.user.UserInfoActivity;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.model.IMUser;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.helper.GlideUtil;
import com.shangyizhou.develop.log.SLog;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = UserActivity.class.getSimpleName();
    private CircleImageView iv_me_photo;
    private TextView tv_nickname;
    private LinearLayout ll_me_info;
    private LinearLayout ll_share;
    private LinearLayout ll_setting;
    private LinearLayout ll_notice;
    public static void startUp(Context context) {
        SLog.i(TAG, "[UserActivity] startUp");
        Intent intent = new Intent(context, UserActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_user);
        initView();
    }

    private void initView() {
        ll_me_info = findViewById(R.id.ll_me_info);
        ll_share = findViewById(R.id.ll_share);
        ll_setting = findViewById(R.id.ll_setting);
        ll_notice = findViewById(R.id.ll_notice);
        iv_me_photo = findViewById(R.id.iv_me_photo);
        tv_nickname = findViewById(R.id.tv_nickname);

        ll_me_info.setOnClickListener(this);
        ll_share.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        ll_notice.setOnClickListener(this);

        loadMeInfo();
    }

    /**
     * 加载我的个人信息
     */
    private void loadMeInfo() {
        IMUser imUser = BmobManager.getInstance().getUser();
        // GlideUtil.loadSmollUrl(this, imUser.getPhoto(), 100, 100, iv_me_photo);
        SLog.i(TAG, "[UserActivity] loadMeInfo" + imUser);
        if (!imUser.getPhoto().equals("")) {
            Bitmap bitmap = BitmapFactory.decodeFile(imUser.getPhoto());
            iv_me_photo.setImageBitmap(bitmap);
        }
        tv_nickname.setText(imUser.getUserName());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_me_info) {
            // 个人信息
            UserInfoActivity.startUp(this);
        } else if (id == R.id.ll_share) {
            // 分享
        } else if (id == R.id.ll_setting) {
            // 设置
        } else if (id == R.id.ll_notice) {
            // 通知
        }
    }
}