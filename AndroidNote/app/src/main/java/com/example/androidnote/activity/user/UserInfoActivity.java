package com.example.androidnote.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.androidnote.R;
import com.example.androidnote.activity.UserActivity;
import com.example.androidnote.constant.Constants;
import com.example.androidnote.db.helper.UserInfoHelper;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.model.IMUser;
import com.example.androidnote.model.User;
import com.example.androidnote.model.UserInfo;
import com.shangyizhou.develop.adapter.common.CommonAdapter;
import com.shangyizhou.develop.adapter.common.CommonViewHolder;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.helper.GlideUtil;
import com.shangyizhou.develop.helper.MediaUtil;
import com.shangyizhou.develop.helper.PermissionUtils;
import com.shangyizhou.develop.helper.ToastUtil;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.model.EventIdCenter;
import com.shangyizhou.develop.ui.dialog.DialogManager;
import com.shangyizhou.develop.ui.dialog.DialogView;
import com.shangyizhou.develop.ui.dialog.DialogView2;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener{

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
    private DialogView2 mPhotoDialog;
    private TextView tv_camera;
    private TextView tv_ablum;
    private TextView tv_photo_cancel;

    //性别选择框
    private DialogView2 mSexDialog;
    private TextView tv_boy;
    private TextView tv_girl;
    private TextView tv_sex_cancel;

    //年龄选择框
    private DialogView2 mAgeDialog;
    private RecyclerView mAgeView;
    private TextView tv_age_cancel;
    private CommonAdapter<Integer> mAgeAdapter;
    private List<Integer> mAgeList = new ArrayList<>();

    //生日选择框
    private DialogView2 mBirthdayDialog;
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
    // private UserInfo userInfo;


    //加载View
    // private LodingView mLodingView;

    private void initView() {
        // mLodingView = new LodingView(this);

        iv_user_photo = (CircleImageView) findViewById(R.id.iv_user_photo);

        et_nickname = (EditText) findViewById(R.id.et_nickname);

        tv_user_sex = (TextView) findViewById(R.id.tv_user_sex);
        ll_user_sex = (LinearLayout) findViewById(R.id.ll_user_sex);

        tv_user_age = (TextView) findViewById(R.id.tv_user_age);
        ll_user_age = (LinearLayout) findViewById(R.id.ll_user_age);

        et_user_desc = (EditText) findViewById(R.id.et_user_desc);

        tv_user_birthday = (TextView) findViewById(R.id.tv_user_birthday);
        ll_user_birthday = (LinearLayout) findViewById(R.id.ll_user_birthday);

        tv_user_constellation = (TextView) findViewById(R.id.tv_user_constellation);
        ll_user_constellation = (LinearLayout) findViewById(R.id.ll_user_constellation);

        tv_user_hobby = (TextView) findViewById(R.id.tv_user_hobby);
        ll_user_hobby = (LinearLayout) findViewById(R.id.ll_user_hobby);

        tv_user_status = (TextView) findViewById(R.id.tv_user_status);
        ll_user_status = (LinearLayout) findViewById(R.id.ll_user_status);

        ll_photo = (RelativeLayout) findViewById(R.id.ll_photo);

        iv_user_photo.setOnClickListener(this);
        ll_user_sex.setOnClickListener(this);
        ll_user_age.setOnClickListener(this);
        ll_user_birthday.setOnClickListener(this);
        ll_user_constellation.setOnClickListener(this);
        ll_user_hobby.setOnClickListener(this);
        ll_user_status.setOnClickListener(this);
        ll_photo.setOnClickListener(this);

        initPhotoDialog();
        initSexDialog();
        initAgeDialog();
        initBirthdayDialog();
    }

    // private IMUser imUser;

    private void getData() {
        SLog.i(TAG, "[getData]");
        IMUser imUser = BmobManager.getInstance().getUser();
        SLog.i(TAG, "[getData] " + imUser);
        if (!imUser.getPhoto().equals("")) {
            Bitmap bitmap = BitmapFactory.decodeFile(imUser.getPhoto());
            iv_user_photo.setImageBitmap(bitmap);
        }
        et_nickname.setText(imUser.getUserName());
        tv_user_sex.setText(imUser.isSex() ? getString(R.string.text_me_info_boy) : getString(R.string.text_me_info_girl));
        tv_user_age.setText(imUser.getAge() + "");
        et_user_desc.setText(imUser.getDesc());

        tv_user_birthday.setText(imUser.getBirthday());
        tv_user_constellation.setText(imUser.getConstellation());
        tv_user_hobby.setText(imUser.getHobby());

        // userInfo = UserInfoHelper.getInstance().getUserByName(name);
        // if (userInfo == null) {
        //     userInfo = new UserInfo();
        // }
        // SLog.i(TAG, "[loadUserInfo getData] " + userInfo);
        // Bitmap bitmap = BitmapFactory.decodeFile(userInfo.getImageFilePath());
        // iv_user_photo.setImageBitmap(bitmap);
        // et_nickname.setText(userInfo.getUserName());
        // tv_user_sex.setText(userInfo.isSex() ? getString(R.string.text_me_info_boy) : getString(R.string.text_me_info_girl));
        // tv_user_age.setText(userInfo.getAge() + "");
        // et_user_desc.setText(userInfo.getDesc());
        // tv_user_birthday.setText(userInfo.getBirthday());
        // tv_user_constellation.setText(userInfo.getConstellation());
        // tv_user_hobby.setText(userInfo.getHobby());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();getData();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        SLog.i(TAG, "[onBackPressed]");
        super.onBackPressed();
        updateUser();
    }

    private void updateUser() {
        //名称不能为空
        String nickName = et_nickname.getText().toString().trim();
        if (TextUtils.isEmpty(nickName)) {
            Toast.makeText(this, getString(R.string.text_update_nickname_null), Toast.LENGTH_SHORT).show();
            // mLodingView.hide();
            return;
        }

        String desc = et_user_desc.getText().toString().trim();
        String sex = tv_user_sex.getText().toString();
        String age = tv_user_age.getText().toString();
        String birthday = tv_user_birthday.getText().toString();
        String constellation = tv_user_constellation.getText().toString();
        String hobby = tv_user_hobby.getText().toString();
        String status = tv_user_status.getText().toString();

        IMUser imUser = BmobManager.getInstance().getUser();
        if (uploadPhotoFile != null) {
            // 更新头像
            SLog.i(TAG, "[updateUser photo image] " + uploadPhotoFile.getPath());
            imUser.setPhoto(uploadPhotoFile.getPath());
        }
        imUser.setUserName(nickName);
        imUser.setDesc(desc);
        imUser.setSex(sex.equals(getString(R.string.text_me_info_boy)) ? true : false);
        imUser.setAge(Integer.parseInt(age));
        imUser.setBirthday(birthday);
        imUser.setConstellation(constellation);
        imUser.setHobby(hobby);
        // imUser.setStatus(status);
        imUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                // mLodingView.hide();
                if (e == null) {
                    //同步缓存
                    SLog.i(TAG, "[updateUser] " + imUser);
                } else {
                    ToastUtil.getInstance().showToast(e.toString());
                }
            }
        });
        // try {
        //     countDownLatch.await();
        // } catch (InterruptedException e) {
        //     throw new RuntimeException(e);
        // }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_user_photo || id == R.id.ll_photo) {
            requestPermission();
            DialogManager.getInstance().show(mPhotoDialog);
        } else if (id == R.id.ll_user_sex) {
            DialogManager.getInstance().show(mSexDialog);
        } else if (id == R.id.ll_user_age) {
            DialogManager.getInstance().show(mAgeDialog);
        } else if (id == R.id.ll_user_birthday) {
            DialogManager.getInstance().show(mBirthdayDialog);
        }
        // } else if (id == R.id.ll_user_constellation) {
        //     DialogManager.getInstance().show(mConstellationDialog);
        // } else if (id == R.id.ll_user_hobby) {
        //     DialogManager.getInstance().show(mHobbyDialog);
        // } else if (id == R.id.ll_user_status) {
        //     DialogManager.getInstance().show(mStatusDialog);
        // }
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
                ToastUtil.getInstance().showToast("授权成功" + String.valueOf(perms));
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

    /**
     * 头像选择
     */
    private void initPhotoDialog() {
        mPhotoDialog = DialogManager.getInstance().initView(this, R.layout.dialog_select_photo, Gravity.BOTTOM);

        tv_camera = (TextView) mPhotoDialog.findViewById(R.id.tv_camera);
        tv_ablum = (TextView) mPhotoDialog.findViewById(R.id.tv_ablum);
        tv_photo_cancel = (TextView) mPhotoDialog.findViewById(R.id.tv_cancel);

        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogManager.getInstance().hide(mPhotoDialog);
                MediaUtil.getInstance().toCamera(UserInfoActivity.this);
            }
        });
        // 跳转到相册
        tv_ablum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogManager.getInstance().hide(mPhotoDialog);
                MediaUtil.getInstance().toAlbum(UserInfoActivity.this);
            }
        });
        tv_photo_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogManager.getInstance().hide(mPhotoDialog);
            }
        });
    }

    /**
     * 性别选择
     */
    private void initSexDialog() {
        mSexDialog = DialogManager.getInstance().initView(this, R.layout.dialog_select_sex, Gravity.BOTTOM);
        tv_boy = (TextView) mSexDialog.findViewById(R.id.tv_boy);
        tv_girl = (TextView) mSexDialog.findViewById(R.id.tv_girl);
        tv_sex_cancel = (TextView) mSexDialog.findViewById(R.id.tv_cancel);

        tv_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogManager.getInstance().hide(mSexDialog);
                tv_user_sex.setText("男");
            }
        });
        tv_girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogManager.getInstance().hide(mSexDialog);
                tv_user_sex.setText("女");
            }
        });
        tv_sex_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogManager.getInstance().hide(mSexDialog);
            }
        });
    }

    /**
     * 年龄选择
     */
    private void initAgeDialog() {

        for (int i = 0; i < 100; i++) {
            mAgeList.add(i);
        }

        mAgeDialog = DialogManager.getInstance().initView(this, R.layout.dialog_select_age, Gravity.BOTTOM);
        mAgeView = (RecyclerView) mAgeDialog.findViewById(R.id.mAgeView);
        tv_age_cancel = (TextView) mAgeDialog.findViewById(R.id.tv_cancel);

        mAgeView.setLayoutManager(new LinearLayoutManager(this));
        mAgeView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAgeAdapter = new CommonAdapter<>(mAgeList, new CommonAdapter.OnBindDataListener<Integer>() {
            @Override
            public void onBindViewHolder(final Integer model, CommonViewHolder hodler, int type, int position) {
                hodler.setText(R.id.tv_age_text, model + "");

                hodler.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogManager.getInstance().hide(mAgeDialog);
                        tv_user_age.setText(model + "");
                    }
                });
            }

            @Override
            public int getLayoutId(int viewType) {
                return R.layout.layout_me_age_item;
            }
        });
        mAgeView.setAdapter(mAgeAdapter);

        tv_age_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogManager.getInstance().hide(mAgeDialog);
            }
        });
    }

    /**
     * 生日选择
     */
    private void initBirthdayDialog() {

        //自定义主题
        //DPTManager.getInstance().initCalendar(new DatePickerTheme());

        mBirthdayDialog = DialogManager.getInstance().initView(this, R.layout.dialog_select_birthday, Gravity.BOTTOM);
        mDatePicker = mBirthdayDialog.findViewById(R.id.mDatePicker);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mDatePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String text = year + "年" + monthOfYear + 1 + "月" + dayOfMonth + "日";
                    tv_user_birthday.setText(text);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == MediaUtil.CAMEAR_REQUEST_CODE) {
                MediaUtil.getInstance().startPhotoZoom(this, MediaUtil.getInstance().getTempFile());
            } else if (requestCode == MediaUtil.ALBUM_REQUEST_CODE) { // 相册
                Uri uri = data.getData();
                if (uri != null) {
                    String path = MediaUtil.getInstance().getRealPathFromURI(this, uri);
                    if (!TextUtils.isEmpty(path)) {
                        uploadPhotoFile = new File(path);
                        MediaUtil.getInstance().startPhotoZoom(this, uploadPhotoFile);
                    }
                }
            } else if (requestCode == MediaUtil.CAMERA_CROP_RESULT) {
                uploadPhotoFile = new File(MediaUtil.getInstance().getCropPath());
                SLog.i(TAG, "uploadPhotoFile:" + uploadPhotoFile.getPath());
                // Uri uri = Uri.fromFile(uploadPhotoFile);
                // if (uploadPhotoFile.exists() && uploadPhotoFile.isFile()) {
                //     SLog.i(TAG, "uploadPhotoFile is true" );
                //     Glide.with(this).load(uri).into(iv_user_photo);
                // }

                // GlideUtil.loadUrl(this, uri, iv_user_photo);
            }
            if (uploadPhotoFile != null) {
                SLog.i(TAG, "uploadPhotoFile Bitmap iv_user_photo" );
                // imUser.setPhoto(uploadPhotoFile.getPath());
                Bitmap bitmap = BitmapFactory.decodeFile(uploadPhotoFile.getPath());
                iv_user_photo.setImageBitmap(bitmap);
            }
        }
    }
}