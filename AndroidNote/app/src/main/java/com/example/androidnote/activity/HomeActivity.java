package com.example.androidnote.activity;

import static com.google.android.material.internal.ViewUtils.dpToPx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidnote.R;
import com.example.androidnote.fragment.ChatFragment;
import com.example.androidnote.fragment.ChatStartFragment;
import com.example.androidnote.fragment.DataFragment;
import com.example.androidnote.fragment.NewsFragment;
import com.example.androidnote.fragment.NewsStartFragment;
import com.example.androidnote.fragment.PersonFragment;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.base.BaseUiActivity;
import com.shangyizhou.develop.base.FragmentManagerHelper;
import com.shangyizhou.develop.log.SLog;
// import com.sxu.shadowdrawable.ShadowDrawable;

import io.reactivex.disposables.Disposable;

public class HomeActivity extends BaseUiActivity implements View.OnClickListener {
    private static final String TAG = HomeActivity.class.getSimpleName();
    FragmentManagerHelper fragmentManagerHelper;

    //news
    private ImageView iv_news;
    private TextView tv_news;
    private LinearLayout ll_news;
    // private NewsFragment newsFragment = null;
    private NewsStartFragment newsStartFragment = null;

    // ai
    private ImageView iv_ai;
    private TextView tv_ai;
    private LinearLayout ll_ai;
    // private ChatFragment chatFragment = null;
    ChatStartFragment chatStartFragment = null;

    // data
    private ImageView iv_data;
    private TextView tv_data;
    private LinearLayout ll_data;
    private DataFragment dataFragment = null;


    //我的
    private ImageView iv_me;
    private TextView tv_me;
    private LinearLayout ll_me;
    private PersonFragment personFragment = null;

    private Disposable disposable;

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

        initFragment();
    }

    private void initFragment () {
        fragmentManagerHelper = new FragmentManagerHelper(getSupportFragmentManager(), R.id.mMainLayout);


        // chatFragment = new ChatFragment();
        chatStartFragment = new ChatStartFragment();
        dataFragment = new DataFragment();
        // newsFragment = new NewsFragment();
        newsStartFragment = new NewsStartFragment();
        personFragment = new PersonFragment();
        fragmentManagerHelper.switchFragment(chatStartFragment);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_ai) {
            SLog.i(TAG, "[onClick] ll_ai");
            // fragmentManagerHelper.switchFragment(chatFragment);
            fragmentManagerHelper.switchFragment(chatStartFragment);
        } else if (id == R.id.ll_news) {
            SLog.i(TAG, "[onClick] ll_news");
            fragmentManagerHelper.switchFragment(newsStartFragment);
        } else if (id == R.id.ll_data) {
            SLog.i(TAG, "[onClick] ll_data");
            fragmentManagerHelper.switchFragment(dataFragment);
        } else if (id == R.id.ll_me) {
            SLog.i(TAG, "[onClick] ll_me");
            fragmentManagerHelper.switchFragment(personFragment);
        }
    }
}