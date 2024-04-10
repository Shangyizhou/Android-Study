package com.shangyizhou.develop.ui.dialog;

import android.content.Context;
import android.view.Gravity;

import com.shangyizhou.develop.R;

/**
 * FileName: DialogManager
 * Founder: LiuGuiLin
 * Profile: 提示框管理类
 */
public class DialogManager {

    private static volatile DialogManager mInstance = null;

    private DialogManager() {

    }

    public static DialogManager getInstance() {
        if (mInstance == null) {
            synchronized (DialogManager.class) {
                if (mInstance == null) {
                    mInstance = new DialogManager();
                }
            }
        }
        return mInstance;
    }

    public DialogView2 initView(Context mContext, int layout) {
        return new DialogView2(mContext, layout, R.style.Theme_Dialog, Gravity.CENTER);
    }

    public DialogView2 initView(Context mContext, int layout, int gravity) {
        return new DialogView2(mContext, layout, R.style.Theme_Dialog, gravity);
    }

    public void show(DialogView2 view) {
        if (view != null) {
            if (!view.isShowing()) {
                view.show();
            }
        }
    }

    public void hide(DialogView2 view) {
        if (view != null) {
            if (view.isShowing()) {
                view.dismiss();
            }

        }
    }
}