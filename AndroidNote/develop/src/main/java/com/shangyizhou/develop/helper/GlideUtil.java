package com.shangyizhou.develop.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shangyizhou.develop.R;

import java.io.File;

public class GlideUtil {
    /**
     * 加载图片Url
     *
     * @param mContext
     * @param url
     * @param imageView
     */
    public static void loadUrl(Context mContext, String url, ImageView imageView) {
        if (mContext != null) {
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.drawable.img_glide_load_ing)
                    .error(R.drawable.img_glide_load_error)
                    .format(DecodeFormat.PREFER_RGB_565)
                    // 取消动画，防止第一次加载不出来
                    .dontAnimate()
                    //加载缩略图
                    .thumbnail(0.3f)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
    }

    /**
     * 加载图片Url
     *
     * @param mContext
     * @param url
     * @param imageView
     */
    public static void loadSmollUrl(Context mContext, String url, int w, int h, ImageView imageView) {
        if (mContext != null) {
            Glide.with(mContext)
                    .load(url)
                    .override(w, h)
                    .placeholder(R.drawable.img_glide_load_ing)
                    .error(R.drawable.img_glide_load_error)
                    .format(DecodeFormat.PREFER_RGB_565)
                    // 取消动画，防止第一次加载不出来
                    .dontAnimate()
                    //加载缩略图
                    .thumbnail(0.3f)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
    }

    /**
     * 加载图片File
     *
     * @param mContext
     * @param file
     * @param imageView
     */
    public static void loadFile(Context mContext, File file, ImageView imageView) {
        if (mContext != null) {
            Glide.with(mContext)
                    .load(file)
                    .placeholder(R.drawable.img_glide_load_ing)
                    .error(R.drawable.img_glide_load_error)
                    .format(DecodeFormat.PREFER_RGB_565)
                    // 取消动画，防止第一次加载不出来
                    .dontAnimate()
                    //加载缩略图
                    .thumbnail(0.3f)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
    }


    /**
     * 加载头像
     *
     * @param mContext
     * @param url
     * @param listener
     */
    public static void loadUrlToBitmap(Context mContext, String url, final OnGlideBitmapResultListener listener) {
        if (mContext != null) {
            Glide.with(mContext).asBitmap().load(url).centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .format(DecodeFormat.PREFER_RGB_565)
                    // 取消动画，防止第一次加载不出来
                    .dontAnimate()
                    //加载缩略图
                    .thumbnail(0.3f)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            if (null != listener) {
                                listener.onResourceReady(resource);
                            }
                        }
                    });
        }
    }

    /**
     * 加载本地图片
     */
    public static void loadLocalFile(Context mContext, File file, ImageView imageView) {
        //加载图片
        Glide.with(mContext).load(file).into(imageView);
    }

    public interface OnGlideBitmapResultListener {
        void onResourceReady(Bitmap resource);
    }
}
