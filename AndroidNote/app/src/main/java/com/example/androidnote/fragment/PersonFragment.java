package com.example.androidnote.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidnote.R;
import com.example.androidnote.activity.UserActivity;
import com.example.androidnote.activity.user.UserInfoActivity;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.model.IMUser;
import com.shangyizhou.develop.helper.GlideUtil;

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

    private static final String TAG = UserActivity.class.getSimpleName();
    private CircleImageView iv_me_photo;
    private TextView tv_nickname;
    private LinearLayout ll_me_info;
    private LinearLayout ll_share;
    private LinearLayout ll_setting;
    private LinearLayout ll_notice;

    private void initView(View view) {
        ll_me_info = view.findViewById(R.id.ll_me_info);
        ll_share = view.findViewById(R.id.ll_share);
        ll_setting = view.findViewById(R.id.ll_setting);
        ll_notice = view.findViewById(R.id.ll_notice);
        iv_me_photo = view.findViewById(R.id.iv_me_photo);
        tv_nickname = view.findViewById(R.id.tv_nickname);

        ll_me_info.setOnClickListener(this);
        ll_share.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        ll_notice.setOnClickListener(this);
    }

    /**
     * 加载我的个人信息
     */
    private void loadMeInfo() {
        IMUser imUser = BmobManager.getInstance().getUser();
        GlideUtil.loadSmollUrl(getActivity(), imUser.getPhoto(), 100, 100, iv_me_photo);
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
        } else if (id == R.id.ll_setting) {
            // 设置
        } else if (id == R.id.ll_notice) {
            // 通知
        }
    }


}