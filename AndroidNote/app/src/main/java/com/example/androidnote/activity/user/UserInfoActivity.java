package com.example.androidnote.activity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.androidnote.R;
import com.example.androidnote.activity.UserActivity;
import com.example.androidnote.constant.Constants;
import com.shangyizhou.develop.adapter.common.CommonAdapter;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.ui.dialog.DialogView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends BaseActivity {

    private static final String TAG = UserActivity.class.getSimpleName();
    public static void startUp(Context context) {
        SLog.i(TAG, "[UserInfoActivity] startUp");
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_user_info);
        initView();
    }

    //基本信息
    private CircleImageView iv_user_photo;
    private EditText et_nickname;
    private TextView tv_user_sex;
    private LinearLayout ll_user_sex;
    private TextView tv_user_age;
    private LinearLayout ll_user_age;
    private EditText et_user_desc;
    private TextView tv_user_birthday;
    private LinearLayout ll_user_birthday;
    private TextView tv_user_constellation;
    private LinearLayout ll_user_constellation;
    private TextView tv_user_hobby;
    private LinearLayout ll_user_hobby;
    private TextView tv_user_status;
    private LinearLayout ll_user_status;
    private RelativeLayout ll_photo;

    //头像选择框
    private DialogView mPhotoDialog;
    private TextView tv_camera;
    private TextView tv_ablum;
    private TextView tv_photo_cancel;

    //性别选择框
    private DialogView mSexDialog;
    private TextView tv_boy;
    private TextView tv_girl;
    private TextView tv_sex_cancel;

    //年龄选择框
    private DialogView mAgeDialog;
    private RecyclerView mAgeView;
    private TextView tv_age_cancel;
    private CommonAdapter<Integer> mAgeAdapter;
    private List<Integer> mAgeList = new ArrayList<>();

    //生日选择框
    private DialogView mBirthdayDialog;
    private DatePicker mDatePicker;

    //星座选择框
    private DialogView mConstellationDialog;
    private RecyclerView mConstellationnView;
    private TextView tv_constellation_cancel;
    private CommonAdapter<String> mConstellationAdapter;
    private List<String> mConstellationList = new ArrayList<>();

    //状态选择框
    private DialogView mStatusDialog;
    private RecyclerView mStatusView;
    private TextView tv_status_cancel;
    private CommonAdapter<String> mStatusAdapter;
    private List<String> mStatusList = new ArrayList<>();

    //爱好选择框
    private DialogView mHobbyDialog;
    private RecyclerView mHobbyView;
    private TextView tv_hobby_cancel;
    private CommonAdapter<String> mHobbyAdapter;
    private List<String> mHobbyList = new ArrayList<>();

    //头像文件
    private File uploadPhotoFile;

    //加载View
    // private LodingView mLodingView;
    private String userId;
    private RelativeLayout ll_back;
    private TextView tv_nickname;
    private TextView tv_desc;
    //用户信息列表
    private RecyclerView mUserInfoView;
    private List<UserInfoModel> mUserInfoList = new ArrayList<>();
    private CommonAdapter<UserInfoModel> mUserInfoAdapter;
    public static final int ADD_FRIEND = 100;
    public static final int CHAT = 200;
    public static final int AUDIO_CHAT =

    private void initView() {
        //获取用户ID
        userId = getIntent().getStringExtra(Constants.INTENT_USER_ID);

        ll_back = (RelativeLayout) findViewById(R.id.ll_back);
        iv_user_photo = (CircleImageView) findViewById(R.id.iv_user_photo);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        mUserInfoView = (RecyclerView) findViewById(R.id.mUserInfoView);
        btn_add_friend = (Button) findViewById(R.id.btn_add_friend);
        btn_chat = (Button) findViewById(R.id.btn_chat);
        btn_audio_chat = (Button) findViewById(R.id.btn_audio_chat);
        btn_video_chat = (Button) findViewById(R.id.btn_video_chat);
        ll_is_friend = (LinearLayout) findViewById(R.id.ll_is_friend);

        ll_back.setOnClickListener(this);
        btn_add_friend.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        btn_audio_chat.setOnClickListener(this);
        btn_video_chat.setOnClickListener(this);
        iv_user_photo.setOnClickListener(this);

        //列表
        mUserInfoAdapter = new CommonAdapter<>(mUserInfoList, new CommonAdapter.OnBindDataListener<UserInfoModel>() {
            @Override
            public void onBindViewHolder(UserInfoModel model, CommonViewHolder viewHolder, int type, int position) {
                //viewHolder.setBackgroundColor(R.id.ll_bg, model.getBgColor());
                viewHolder.getView(R.id.ll_bg).setBackgroundColor(model.getBgColor());
                viewHolder.setText(R.id.tv_type, model.getTitle());
                viewHolder.setText(R.id.tv_content, model.getContent());
            }

            @Override
            public int getLayoutId(int type) {
                return R.layout.layout_user_info_item;
            }
        });
        mUserInfoView.setLayoutManager(new GridLayoutManager(this, 3));
        mUserInfoView.setAdapter(mUserInfoAdapter);

        queryUserInfo();
    }
}