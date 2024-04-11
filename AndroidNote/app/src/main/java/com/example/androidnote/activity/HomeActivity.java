package com.example.androidnote.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidnote.R;
import com.example.androidnote.fragment.ChatFragment;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.base.FragmentManagerHelper;

import io.reactivex.disposables.Disposable;

public class HomeActivity extends BaseActivity implements View.OnClickListener{
    FragmentManagerHelper fragmentManagerHelper;

    //星球
    private ImageView iv_star;
    private TextView tv_star;
    private LinearLayout ll_star;
    private ChatFragment chatFragment = null;

    //广场
    // private ImageView iv_square;
    // private TextView tv_square;
    // private LinearLayout ll_square;
    // private SquareFragment mSquareFragment = null;
    // private FragmentTransaction mSquareTransaction = null;
    //
    // //聊天
    // private ImageView iv_chat;
    // private TextView tv_chat;
    // private LinearLayout ll_chat;
    // private ChatFragment mChatFragment = null;
    // private FragmentTransaction mChatTransaction = null;
    //
    // //我的
    // private ImageView iv_me;
    // private TextView tv_me;
    // private LinearLayout ll_me;
    // private MeFragment mMeFragment = null;
    // private FragmentTransaction mMeTransaction = null;

    private Disposable disposable;

    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_home);
        initView();
    }

    @SuppressLint("ResourceType")
    private void initView() {
        // 初始化FragmentManagerHelper
        fragmentManagerHelper = new FragmentManagerHelper(getSupportFragmentManager(), R.id.mMainLayout);
        initFragment();

        iv_star = (ImageView) findViewById(R.id.iv_star);
        tv_star = (TextView) findViewById(R.id.tv_star);
        ll_star = (LinearLayout) findViewById(R.id.ll_star);

        // iv_square = (ImageView) findViewById(R.id.iv_square);
        // tv_square = (TextView) findViewById(R.id.tv_square);
        // ll_square = (LinearLayout) findViewById(R.id.ll_square);
        //
        // iv_chat = (ImageView) findViewById(R.id.iv_chat);
        // tv_chat = (TextView) findViewById(R.id.tv_chat);
        // ll_chat = (LinearLayout) findViewById(R.id.ll_chat);
        //
        // iv_me = (ImageView) findViewById(R.id.iv_me);
        // tv_me = (TextView) findViewById(R.id.tv_me);
        // ll_me = (LinearLayout) findViewById(R.id.ll_me);

        ll_star.setOnClickListener(this);
        // ll_square.setOnClickListener(this);
        // ll_chat.setOnClickListener(this);
        // ll_me.setOnClickListener(this);

        //设置文本
        tv_star.setText(getString(R.string.text_main_star));
        // tv_square.setText(getString(R.string.text_main_square));
        // tv_chat.setText(getString(R.string.text_main_chat));
        // tv_me.setText(getString(R.string.text_main_me));
    }

    private void initFragment () {
        chatFragment = new ChatFragment();
        fragmentManagerHelper.add(chatFragment);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_star) {
            fragmentManagerHelper.switchFragment(chatFragment);
        } else if (id == R.id.iv_square) {
        } else if (id == R.id.iv_chat) {
        } else if (id == R.id.iv_me) {
        }
    }
}