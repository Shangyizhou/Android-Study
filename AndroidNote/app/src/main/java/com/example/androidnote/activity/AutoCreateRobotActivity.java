package com.example.androidnote.activity;

import static com.example.androidnote.constant.Constants.YIYAN_HANDLER_ACQUIRE_QUERY;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidnote.R;
import com.example.androidnote.model.RobotModel;
import com.example.androidnote.net.DirectToServer;
import com.example.androidnote.net.YiYanHandler;
import com.example.androidnote.view.LoadingDialog;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.net.IResponse;

public class AutoCreateRobotActivity extends BaseActivity implements View.OnClickListener {
    public static void startUp(Context context, Bundle bundle) {
        SLog.i(TAG, "[AutoCreateRobotActivity] startUp");
        Intent intent = new Intent(context, AutoCreateRobotActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent, bundle);
    }

    private static final String TAG = AutoCreateRobotActivity.class.getSimpleName();
    @Override
    protected void onCreateChildren(Bundle bundle) {
        // super.onCreateChildren(bundle);
        setContentView(R.layout.activity_auto_create_robot);
        Intent intent = getIntent();
        if (intent != null) {
            bundle = intent.getExtras();
        }
        initView();
    }

    Button freeBtn;
    Button aiBtn;
    TextView prompt;
    LoadingDialog mLoadingDialog;
    private void initView() {
        mLoadingDialog = new LoadingDialog(this);
        freeBtn = findViewById(R.id.free_btn);
        aiBtn = findViewById(R.id.ai_btn);
        prompt = findViewById(R.id.prompt_text);
        freeBtn.setOnClickListener(this);
        aiBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.free_btn) {

        } else if (id == R.id.ai_btn) {
            SLog.i(TAG, "call ai for prompt");
            String text = "你是一个JAVA编程专家";
            mLoadingDialog.show();
            DirectToServer.callYiYanERNIELiteStream(text, YIYAN_HANDLER_ACQUIRE_QUERY, new IResponse() {
                @Override
                public void onSuccess(String content) {
                    String res = YiYanHandler.processCreateRobot(getApplication(), content);
                    SLog.i(TAG, "prompt res: " + res);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLoadingDialog.cancel();
                            prompt.setText(res);
                        }
                    });
                    Bundle bundle = new Bundle();
                    bundle.putString("prompt", res);
                    CreateRobotActivity.startUp(AutoCreateRobotActivity.this, bundle);
                    finish();
                }

                @Override
                public void onFailure(String errorMsg) {

                }
            });
        }
    }
}