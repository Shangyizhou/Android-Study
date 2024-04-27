package com.example.androidnote.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidnote.R;
import com.example.androidnote.db.helper.RobotHelper;
import com.example.androidnote.fragment.chat.ChatStartFragment;
import com.example.androidnote.fragment.DataFragment;
import com.example.androidnote.fragment.NewsStartFragment;
import com.example.androidnote.fragment.PersonFragment;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.model.RobotModel;
import com.shangyizhou.develop.base.BaseUiActivity;
import com.shangyizhou.develop.base.FragmentManagerHelper;
import com.shangyizhou.develop.helper.UUIDUtil;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.ui.dialog.DialogManager;
import com.shangyizhou.develop.ui.dialog.DialogView2;
// import com.sxu.shadowdrawable.ShadowDrawable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class HomeActivity extends BaseUiActivity implements View.OnClickListener {
    private static final String TAG = HomeActivity.class.getSimpleName();
    FragmentManagerHelper fragmentManagerHelper;

    // news
    private ImageView iv_news;
    private TextView tv_news;
    private LinearLayout ll_news;
    // private NewsFragment newsFragment = null;
    private NewsStartFragment newsStartFragment = null;

    // ai
    private ImageView iv_ai;
    private TextView tv_ai;
    private LinearLayout ll_ai;
    ChatStartFragment chatStartFragment = null;

    // data
    private ImageView iv_data;
    private TextView tv_data;
    private LinearLayout ll_data;
    private DataFragment dataFragment = null;

    // 我的
    private ImageView iv_me;
    private TextView tv_me;
    private LinearLayout ll_me;
    private PersonFragment personFragment = null;
    private DialogView2 createRobotDialog;

    public static void startUp(Context context) {
        SLog.i(TAG, "[HomeActivity] startUp");
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateChildren(Bundle bundle) {
        super.onCreateChildren(bundle);
        setContentView(R.layout.activity_home);
        initView();
    }

    @SuppressLint("ResourceType")
    private void initView() {
        View view = findViewById(R.id.ll_bottom);
        iv_news = (ImageView) findViewById(R.id.iv_news);
        tv_news = (TextView) findViewById(R.id.tv_news);
        ll_news = (LinearLayout) findViewById(R.id.ll_news);

        iv_ai = (ImageView) findViewById(R.id.iv_ai);
        tv_ai = (TextView) findViewById(R.id.tv_ai);
        ll_ai = (LinearLayout) findViewById(R.id.ll_ai);

        iv_data = (ImageView) findViewById(R.id.iv_data);
        tv_data = (TextView) findViewById(R.id.tv_data);
        ll_data = (LinearLayout) findViewById(R.id.ll_data);

        iv_me = (ImageView) findViewById(R.id.iv_me);
        tv_me = (TextView) findViewById(R.id.tv_me);
        ll_me = (LinearLayout) findViewById(R.id.ll_me);

        ll_news.setOnClickListener(this);
        ll_data.setOnClickListener(this);
        ll_me.setOnClickListener(this);
        ll_ai.setOnClickListener(this);

        btnCreateHome = findViewById(R.id.create_robot_home);
        btnCreateHome.setOnClickListener(this);

        initCreateRobotView();
        initFragment();
    }

    EditText editName;
    EditText editDesc;
    EditText editStartSpeak;
    EditText editQuery1;
    EditText editQuery2;
    EditText editQuery3;
    Button btnCreate;
    List<String> queryList = new ArrayList<>();
    Button btnCreateHome;

    private void initCreateRobotView() {
        createRobotDialog = DialogManager.getInstance().initView(this, R.layout.dialog_create_robot, Gravity.BOTTOM);
        editName = createRobotDialog.findViewById(R.id.edit_name);
        editDesc = createRobotDialog.findViewById(R.id.edit_desc);
        editStartSpeak = createRobotDialog.findViewById(R.id.edit_start_spek);
        editQuery1 = createRobotDialog.findViewById(R.id.edit_query_1);
        editQuery2 = createRobotDialog.findViewById(R.id.edit_query_2);
        editQuery3 = createRobotDialog.findViewById(R.id.edit_query_3);
        btnCreate = createRobotDialog.findViewById(R.id.create_robot_dialog);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RobotModel model = new RobotModel();
                queryList.add(editQuery1.getText().toString());
                queryList.add(editQuery2.getText().toString());
                queryList.add(editQuery3.getText().toString());

                // 设置model属性
                model.setTitle(editName.getText().toString());
                model.setDesc(editDesc.getText().toString());
                model.setRobotId(UUIDUtil.getUUID());
                model.setOwnerId(BmobManager.getInstance().getObjectId());
                model.setBeginSay(editStartSpeak.getText().toString());
                model.setQuestions(queryList);
                model.setCreateTime(System.currentTimeMillis());
                model.setUpdateTime(System.currentTimeMillis());
                model.setImageUrl("");

                RobotHelper.getInstance().save(model);
                createRobotDialog.hide();

                Bundle bundle = new Bundle();
                bundle.putString("model", "create");
                bundle.putSerializable("robot_data", model);
                ChatActivity.startUp(HomeActivity.this, bundle);
                // fragmentManagerHelper.switchFragment(chatStartFragment);
            }
        });
    }

    private void initFragment() {
        fragmentManagerHelper = new FragmentManagerHelper(getSupportFragmentManager(), R.id.mMainLayout);

        // chatFragment = new ChatFragment();
        chatStartFragment = new ChatStartFragment();
        dataFragment = new DataFragment();
        // newsFragment = new NewsFragment();
        newsStartFragment = new NewsStartFragment();
        personFragment = new PersonFragment();
        tv_ai.setTextColor(getResources().getColor(R.color.black));
        fragmentManagerHelper.switchFragment(chatStartFragment);
    }

    private void setAllGrey() {
        tv_data.setTextColor(getResources().getColor(R.color.grey));
        tv_ai.setTextColor(getResources().getColor(R.color.grey));
        tv_news.setTextColor(getResources().getColor(R.color.grey));
        tv_me.setTextColor(getResources().getColor(R.color.grey));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_ai) {
            SLog.i(TAG, "[onClick] ll_ai");
            setAllGrey();
            tv_ai.setTextColor(getResources().getColor(R.color.black));
            fragmentManagerHelper.switchFragment(chatStartFragment);
        } else if (id == R.id.ll_news) {
            SLog.i(TAG, "[onClick] ll_news");
            setAllGrey();
            tv_news.setTextColor(getResources().getColor(R.color.black));
            fragmentManagerHelper.switchFragment(newsStartFragment);
        } else if (id == R.id.ll_data) {
            SLog.i(TAG, "[onClick] ll_data");
            setAllGrey();
            tv_data.setTextColor(getResources().getColor(R.color.black));
            fragmentManagerHelper.switchFragment(dataFragment);
        } else if (id == R.id.ll_me) {
            SLog.i(TAG, "[onClick] ll_me");
            setAllGrey();
            tv_me.setTextColor(getResources().getColor(R.color.black));
            fragmentManagerHelper.switchFragment(personFragment);
        } else if (id == R.id.create_robot_home) {
            SLog.i(TAG, "[onClick] create_robot_home");
            createRobotDialog.show();
        }
    }
}