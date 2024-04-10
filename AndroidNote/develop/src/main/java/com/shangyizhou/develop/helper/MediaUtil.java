package com.shangyizhou.develop.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import com.shangyizhou.develop.log.SLog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 多媒体帮助类，关于访问相册，相机拍照等功能的封装
 */
public class MediaUtil {
    //相机
    public static final int CAMEAR_REQUEST_CODE = 1004;
    //相册
    public static final int ALBUM_REQUEST_CODE = 1005;
    //音乐
    public static final int MUSIC_REQUEST_CODE = 1006;
    //视频
    public static final int VIDEO_REQUEST_CODE = 1007;

    //裁剪结果
    public static final int CAMERA_CROP_RESULT = 1008;
    private File tempFile = null;
    private Uri imageUri;
    //裁剪文件
    private String cropPath;
    private static MediaUtil instance = null;

    public MediaUtil() {
    }

    public static MediaUtil getInstance() {
        if (instance == null) {
            synchronized (MediaUtil.class) {
                if (instance == null) {
                    instance = new MediaUtil();
                }
            }
        }
        return instance;
    }

    public File getTempFile() {
        return tempFile;
    }

    public String getCropPath() {
        return cropPath;
    }

    /**
     * 相机
     * 如果头像上传，可以支持裁剪，自行增加
     */
    public void toCamera(Activity activity) {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String fileName = new SimpleDateFormat().format(new Date());
            tempFile = new File(Environment.getExternalStorageDirectory(), fileName + ".jpg");
            // 兼容Android N
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUri = Uri.fromFile(tempFile);
            } else {
                // 利用FileProvider
                imageUri = FileProvider.getUriForFile(activity,
                        activity.getPackageName() + ".fileprovider", tempFile);
                // 添加权限
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            activity.startActivityForResult(intent, CAMEAR_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(activity, "无法打开相机", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 跳转到相册
     */
    public void toAlbum(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }

    /**
     * 通过Uri去系统查询真实地址
     *
     * @param uri
     */
    public String getRealPathFromURI(Context mContext, Uri uri) {
        String realPath = "";
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader cursorLoader = new CursorLoader(mContext, uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    realPath = cursor.getString(index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return realPath;
    }

    /**
     * 裁剪
     *
     * @param mActivity
     * @param file
     */
    public void startPhotoZoom(Activity mActivity, File file) {
        SLog.i("MediaUtil", "startPhotoZoom" + file.getPath());
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            getUriForFile() 是 Android FileProvider 类中的一个方法，用于将一个文件转换为对应的 Content Uri。这样做的原因是因为从 Android N（7.0）开始出于安全考虑，Android 系统禁止直接使用 file:// 格式的 Uri 来访问文件，所以我们需要使用 FileProvider 来生成合适的 Uri。
//            然后再通过intent访问
//            比如
//            File imagePath = new File(Context.getFilesDir(), "Pictures");
//            File newFile = new File(imagePath, "default_image.jpg");
//            Uri contentUri = FileProvider.getUriForFile(getContext(), "com.example.myapp.fileprovider", newFile);
            uri = FileProvider.getUriForFile(mActivity, "com.example.androidnote.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }

        if (uri == null) {
            return;
        }

        // "com.android.camera.action.CROP" 这个 Intent action 用于裁剪图片，虽然很多 Android 设备上的应用都实现了它，
        // 但实际上它并不是 Android 官方 SDK 的一部分，也就是说并不是所有的 Android 设备或者所有的图片应用都支持这个功能。
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        //intent.putExtra("return-data", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        //单独存储裁剪文件，解决手机兼容性问题
        cropPath = Environment.getExternalStorageDirectory().getPath() + "/" + "meet.jpg";
        Uri mUriTempFile = Uri.parse("file://" + "/" + cropPath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriTempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        mActivity.startActivityForResult(intent, CAMERA_CROP_RESULT);
    }
}
