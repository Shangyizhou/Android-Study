package com.example.androidnote;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.androidnote.db.helper.UserHelper;
import com.example.androidnote.model.User;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.helper.SPUtil;
import com.shangyizhou.develop.helper.ToastUtil;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.ui.dialog.DialogView;

public class MainActivity extends BaseActivity {
    private final String TAG = this.getClass().getSimpleName();;

    DialogView dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onCreateChildren(Bundle bundle) {
        initView();
        getData();
    }

    private void initView() {
        dialog = new DialogView(this);
        dialog.setMessage("提示信息");
        dialog.setOperation(new DialogView.OperationListener() {
            @Override
            public void onCancel() {
                ToastUtil.getInstance().showToast("取消");
                boolean flag = SPUtil.getInstance().getBooleanData("QUEREN");
                SLog.d(TAG, "取消 flag:" + flag);
                User user = new User();
                user.setName("syz");
                user.setPassword("123456");
                user.setAccount("syzjkld");
                UserHelper.getInstance().insert(user);
            }

            @Override
            public void onOk() {
                ToastUtil.getInstance().showToast("确认");
                SPUtil.getInstance().saveBooleanData("QUEREN", true);
                User user = UserHelper.getInstance().getUserByName("syz");
                SLog.d(TAG, "User: " + user.toString());
            }
        });
        dialog.show();
    }

    private void getData() {

    }

    public static void startUp(Activity activity, boolean newFlag) {
        SLog.i("MainActivity", newFlag + " startUp: ");
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    public static void startUpForRequest(Activity activity, boolean newFlag) {
        SLog.i("MainActivity", newFlag + " startUp: ");
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }
}