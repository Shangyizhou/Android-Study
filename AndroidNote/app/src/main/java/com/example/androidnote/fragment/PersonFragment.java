package com.example.androidnote.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidnote.App;
import com.example.androidnote.R;
import com.example.androidnote.activity.AboutActivity;
import com.example.androidnote.activity.DataActivity;
import com.example.androidnote.activity.LoginActivity;
import com.example.androidnote.activity.user.UserInfoActivity;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.manager.SessionManager;
import com.example.androidnote.model.IMUser;
import com.shangyizhou.develop.helper.ToastUtil;
import com.shangyizhou.develop.log.SLog;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PersonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonFragment newInstance(String param1, String param2) {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_user, container, false);
        initView(view);
        return view;
    }

    private static final String TAG = PersonFragment.class.getSimpleName();
    private CircleImageView iv_me_photo;
    private TextView tv_nickname;
    private LinearLayout ll_me_info;
    private LinearLayout ll_share;
    private LinearLayout ll_setting;
    private LinearLayout ll_notice;
    private LinearLayout ll_logout;
    private LinearLayout ll_data;
    TextView tvVersion;

    private void initView(View view) {
        ll_me_info = view.findViewById(R.id.ll_me_info);
        ll_share = view.findViewById(R.id.ll_share);
        ll_setting = view.findViewById(R.id.ll_setting);
        ll_notice = view.findViewById(R.id.ll_version);
        iv_me_photo = view.findViewById(R.id.iv_me_photo);
        tv_nickname = view.findViewById(R.id.tv_nickname);
        ll_logout = view.findViewById(R.id.ll_logout);
        ll_data = view.findViewById(R.id.ll_data);

        ll_me_info.setOnClickListener(this);
        ll_share.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        ll_notice.setOnClickListener(this);
        ll_logout.setOnClickListener(this);
        ll_data.setOnClickListener(this);

        ll_setting.setVisibility(View.GONE);

        tvVersion = view.findViewById(R.id.tv_version);
        try {
            PackageInfo pInfo = App.getApp().getPackageManager().getPackageInfo(App.getApp().getPackageName(), 0);
            String version = pInfo.versionName;
            tvVersion.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
            UserInfoActivity.startUp(getActivity());
        } else if (id == R.id.ll_share) {
            // 分享
            AboutActivity.startUp(getActivity());
        } else if (id == R.id.ll_setting) {
            // 设置
        } else if (id == R.id.ll_version) {
            ToastUtil.getInstance().showToast(tvVersion.getText().toString());
            // 通知
        } else if (id == R.id.ll_logout) {
            // 退出登录
            logout();
        } else if (id == R.id.ll_data) {
            DataActivity.startUp(getActivity());
        }
    }

    private void logout() {
        //删除Token
        //Bmob退出登录
        BmobUser.logOut();
        SessionManager.getInstance().resetSession();

        //跳转到登录页
        Intent intent_login = new Intent();
        intent_login.setClass(getActivity(), LoginActivity.class);
        intent_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent_login);
    }
}