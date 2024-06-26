package com.example.androidnote.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidnote.R;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.model.IMUser;
import com.example.androidnote.view.LoadingDialog;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.helper.MediaUtil;
import com.shangyizhou.develop.log.SLog;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class AboutActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = AboutActivity.class.getSimpleName();
    LoadingDialog mLoadingDialog;
    //头像
    private ImageView iv_photo;
    //昵称
    private TextView tv_name;
    //性别
    private TextView tv_sex;
    //年龄
    private TextView tv_age;
    //电话
    private TextView tv_phone;
    //简介
    private TextView tv_desc;
    //二维码
    private ImageView iv_qrcode;
    //根布局
    private LinearLayout ll_content;
    //下载
    private LinearLayout ll_download;

    // private LodingView mLodingView;

    public static void startUp(Context context) {
        SLog.i(TAG, "[AboutActivity] startUp");
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView() {

        // mLodingView = new LodingView(this);
        mLoadingDialog = new LoadingDialog(this);
        // mLodingView.setLodingText(getString(R.string.text_shar_save_ing));

        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        ll_download = (LinearLayout) findViewById(R.id.ll_download);

        ll_download.setOnClickListener(this);

        loadInfo();
    }

    /**
     * 加载个人信息
     */
    private void loadInfo() {
        IMUser imUser = BmobManager.getInstance().getUser();

        Bitmap bitmap = BitmapFactory.decodeFile(imUser.getPhoto());
        iv_photo.setImageBitmap(bitmap);

        tv_name.setText(imUser.getUserName());
        tv_sex.setText(imUser.isSex() ? R.string.text_me_info_boy : R.string.text_me_info_girl);
        tv_age.setText(imUser.getAge() + " " + getString(R.string.text_search_age));
        tv_phone.setText(imUser.getMobilePhoneNumber());
        tv_desc.setText(imUser.getDesc());

        createQRCode(imUser.getObjectId());
    }

    /**
     * 创建二维码
     */
    private void createQRCode(final String userId) {

        /**
         * View的绘制
         */

        iv_qrcode.post(new Runnable() {
            @Override
            public void run() {
                String textContent = "Meet#" + userId;
                Bitmap mBitmap = CodeUtils.createImage(textContent,
                        iv_qrcode.getWidth(), iv_qrcode.getHeight(), null);
                iv_qrcode.setImageBitmap(mBitmap);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_download:

                /**
                 * 1.View截图
                 * 2.创建一个Bitmap
                 * 3.保存到相册
                 */

                // mLodingView.show();
                mLoadingDialog.show();

                /**
                 * setDrawingCacheEnabled
                 * 保留我们的绘制副本
                 * 1.重新测量
                 * 2.重新布局
                 * 3.得到我们的DrawingCache
                 * 4.转换成Bitmap
                 */
                ll_content.setDrawingCacheEnabled(true);

                ll_content.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                ll_content.layout(0, 0, ll_content.getMeasuredWidth(),
                        ll_content.getMeasuredHeight());

                Bitmap mBitmap = ll_content.getDrawingCache();

                if (mBitmap != null) {
                    MediaUtil.getInstance().saveBitmapToAlbum(this, mBitmap);
                    // mLodingView.hide();
                    mLoadingDialog.hide();
                }
                break;
        }
    }
}