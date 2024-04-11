package com.example.androidnote.manager;

import com.example.androidnote.App;
import com.example.androidnote.db.DaoManager;
import com.example.androidnote.db.helper.UserHelper;
import com.example.androidnote.model.IMUser;
import com.example.androidnote.model.User;
import com.shangyizhou.develop.helper.ToastUtil;
import com.shangyizhou.develop.log.SLog;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.SaveListener;

public class BmobManager {
    private static String TAG = BmobManager.class.getSimpleName();
    private static volatile BmobManager instance;
    private static final String BMOB_SDK_ID = "3d12209ac860df8aa8678d3b3ab5ce2a"; // Application ID

    private BmobManager() {
        Bmob.initialize(App.getApp(), BMOB_SDK_ID);
    }

    public static BmobManager getInstance() {
        if (instance == null) {
            synchronized (BmobManager.class) {
                if (instance == null) {
                    instance = new BmobManager();
                }
            }
        }
        return instance;
    }

    public interface LoginCallback {
        void onSuccess(BmobUser user);
        void onFailure(BmobException e);
    }


    public interface RegisterCallback {
        void onSuccess(IMUser user);
        void onFailure(BmobException e);
    }

    private RegisterCallback registerCallback;
    /**
     * 账号密码注册
     */
    public static void signUp(String name, String passwd, RegisterCallback callback) {
        IMUser user = new IMUser();
        user.setUsername(name);
        user.setPassword(passwd);
        user.signUp(new SaveListener<IMUser>() {
            @Override
            public void done(IMUser user, BmobException e) {
                if (e == null) {
                    if (callback != null) {
                        callback.onSuccess(user);
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure(e);
                    }
                }
            }
        });
    }

    private LoginCallback loginCallback;
    /**
     * 账号密码登录
     */
    public void login(String name, String passwd, LoginCallback callback) {
        final IMUser user = new IMUser();
        //此处替换为你的用户名
        user.setUsername(name);
        //此处替换为你的密码
        user.setPassword(passwd);
        user.login(new SaveListener<IMUser>() {
            @Override
            public void done(IMUser bmobUser, BmobException e) {
                if (e == null) {
                    BmobUser user = BmobUser.getCurrentUser(BmobUser.class);
                    ToastUtil.getInstance().showToast("登录成功");
                    if (callback != null) {
                        callback.onSuccess(user);
                    }
                } else {
                    ToastUtil.getInstance().showToast("登录失败");
                    if (callback != null) {
                        callback.onFailure(e);
                    }
                }
            }
        });
    }

    /**
     * 同步控制台信息至本地缓存
     */
    public void fetchUserInfo(FetchUserInfoListener<BmobUser> listener) {
        BmobUser.fetchUserInfo(listener);
    }

    public void isLogin() {
        if (IMUser.isLogin()) {
            IMUser user = BmobUser.getCurrentUser(IMUser.class);
            ToastUtil.getInstance().showToast("登录成功");
        } else {
            ToastUtil.getInstance().showToast("尚未登录");
        }
    }

    public User getUserInfo() {
        if (BmobUser.isLogin()) {
            User user = BmobUser.getCurrentUser(User.class);
            ToastUtil.getInstance().showToast("登录成功");
            SLog.i(TAG, "[getUserInfo]" + user);
            return user;
        } else {
            ToastUtil.getInstance().showToast("尚未登录，请先登录");
        }
        return null;
    }

    public IMUser getUser() {
        return BmobUser.getCurrentUser(IMUser.class);
    }
}
