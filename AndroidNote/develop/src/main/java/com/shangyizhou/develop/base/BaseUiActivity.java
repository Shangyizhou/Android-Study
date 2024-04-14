package com.shangyizhou.develop.base;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toolbar;

import com.shangyizhou.develop.R;

public class BaseUiActivity extends BaseActivity {
    private static final String TAG = "BaseUiActivity";
    private Toolbar toolbar;

    @Override
    protected void onCreateChildren(Bundle bundle) {
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //     //获取最顶层的View
        //     this.getWindow().getDecorView()
        //             .setSystemUiVisibility(
        //                     View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
        //                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //     getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //     // this.getWindow().setStatusBarColor(Color.parseColor("#151419"));
        //     // this.getWindow().setNavigationBarColor(Color.parseColor("#151419"));
        // }
        // setContentView(R.layout.activity_base_ui);
        // // initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }
}