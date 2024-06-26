package com.example.androidnote.activity;

import static com.example.androidnote.constant.Constants.ROBOT_MODEL_NORMAL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.androidnote.R;
import com.example.androidnote.db.helper.RobotHelper;
import com.example.androidnote.fragment.chat.ChatStartFragment;
import com.example.androidnote.fragment.DataFragment;
import com.example.androidnote.fragment.NewsStartFragment;
import com.example.androidnote.fragment.PersonFragment;
import com.example.androidnote.fragment.SquareFragment;
import com.example.androidnote.fragment.square.SquareStartFragment;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.model.RobotModel;
import com.shangyizhou.develop.base.BaseUiActivity;
import com.shangyizhou.develop.base.FragmentManagerHelper;
import com.shangyizhou.develop.helper.PermissionUtils;
import com.shangyizhou.develop.helper.ToastUtil;
import com.shangyizhou.develop.helper.UUIDUtil;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.model.EventIdCenter;
import com.shangyizhou.develop.ui.dialog.DialogManager;
import com.shangyizhou.develop.ui.dialog.DialogView2;
// import com.sxu.shadowdrawable.ShadowDrawable;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

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

    // 我的
    private ImageView iv_square;
    private TextView tv_square;
    private LinearLayout ll_square;
    // private SquareFragment squareFragment = null;
    private SquareStartFragment squareStartFragment = null;

    public static void startUp(Context context) {
        SLog.i(TAG, "[HomeActivity] startUp");
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Login界面跳转进入后，当前任务战都会被清除
        context.startActivity(intent);
    }

    @Override
    protected void onCreateChildren(Bundle bundle) {
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

        iv_square = (ImageView) findViewById(R.id.iv_square);
        tv_square = (TextView) findViewById(R.id.tv_square);
        ll_square = (LinearLayout) findViewById(R.id.ll_square);

        ll_news.setOnClickListener(this);
        ll_data.setOnClickListener(this);
        ll_me.setOnClickListener(this);
        ll_ai.setOnClickListener(this);
        ll_square.setOnClickListener(this);

        btnCreateHome = findViewById(R.id.create_robot_home);
        btnCreateHome.setOnClickListener(this);

        initFragment();
        requestPermission();
    }
    private String[] mRequirePermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    private static final int REQUEST_PERMISSION_CODE = 200;
    private void requestPermission() {
        // 判断是否有权限
        if (!PermissionUtils.hasPermissions(this, mRequirePermissions)) {
            // 申请权限
            PermissionUtils.requestPermissions(this, REQUEST_PERMISSION_CODE, mRequirePermissions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults, new PermissionUtils.PermissionCallbacks() {
            @Override
            public void onPermissionsAllGranted(int requestCode, List<String> perms, boolean isAllGranted) {
                SLog.i(TAG, "授权成功" + String.valueOf(perms));
                ToastUtil.getInstance().showToast("授权成功" );
                SLog.i(TAG, "授权成功" + String.valueOf(perms));
            }

            @Override
            public void onPermissionsDenied(int requestCode, List<String> perms) {
                ToastUtil.getInstance().showToast("授权失败" + String.valueOf(perms));
                SLog.i(TAG, "授权失败" + String.valueOf(perms));
            }
        });
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    private void initFragment() {
        fragmentManagerHelper = new FragmentManagerHelper(getSupportFragmentManager(), R.id.mMainLayout);

        chatStartFragment = new ChatStartFragment();
        dataFragment = new DataFragment();
        newsStartFragment = new NewsStartFragment();
        personFragment = new PersonFragment();
        // squareFragment = new SquareFragment();
        squareStartFragment = new SquareStartFragment();
        tv_ai.setTextColor(getResources().getColor(R.color.black));
        fragmentManagerHelper.switchFragment(chatStartFragment);
    }

    private void setAllGrey() {
        tv_data.setTextColor(getResources().getColor(R.color.grey));
        tv_ai.setTextColor(getResources().getColor(R.color.grey));
        tv_news.setTextColor(getResources().getColor(R.color.grey));
        tv_me.setTextColor(getResources().getColor(R.color.grey));
        tv_square.setTextColor(getResources().getColor(R.color.grey));
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
            AutoCreateRobotActivity.startUp(this, null);
        } else if (id == R.id.ll_square) {
            SLog.i(TAG, "[onClick] create_robot_home");
            setAllGrey();
            tv_square.setTextColor(getResources().getColor(R.color.black));
            fragmentManagerHelper.switchFragment(squareStartFragment);
        }
    }

    private boolean isExit = false;

    /**
     * 双击返回键退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {
                this.finish();
            } else {
                ToastUtil.getInstance().showToast("再按一次退出");
                isExit = true;
                /**
                 * 两秒内连续按两次
                 */
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}