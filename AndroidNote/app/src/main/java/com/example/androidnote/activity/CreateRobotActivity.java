package com.example.androidnote.activity;

import static com.example.androidnote.constant.Constants.ROBOT_MODEL_NORMAL;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidnote.R;
import com.example.androidnote.db.helper.RobotHelper;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.model.RobotModel;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.helper.ToastUtil;
import com.shangyizhou.develop.helper.UUIDUtil;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.model.EventIdCenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class CreateRobotActivity extends BaseActivity {
    private static final String TAG = CreateRobotActivity.class.getSimpleName();
    public static void startUp(Context context) {
        SLog.i(TAG, "[CreateRobotActivity] startUp");
        Intent intent = new Intent(context, CreateRobotActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_create_robot);
        initView();
    }

    EditText editName;
    EditText editDesc;
    EditText editStartSpeak;
    EditText editQuery1;
    EditText editQuery2;
    EditText editQuery3;
    Button btnCreate;
    List<String> queryList = new ArrayList<>();
    private void initView() {
        editName = findViewById(R.id.edit_name);
        editDesc = findViewById(R.id.edit_desc);
        editStartSpeak = findViewById(R.id.edit_start_spek);
        editQuery1 = findViewById(R.id.edit_query_1);
        editQuery2 = findViewById(R.id.edit_query_2);
        editQuery3 = findViewById(R.id.edit_query_3);
        btnCreate = findViewById(R.id.create_robot_dialog);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editName.getText().toString().isEmpty() ||
                        editDesc.getText().toString().isEmpty() ||
                        editStartSpeak.getText().toString().isEmpty() ||
                        editQuery1.getText().toString().isEmpty() ||
                        editQuery2.getText().toString().isEmpty() ||
                        editQuery3.getText().toString().isEmpty()) {
                    ToastUtil.getInstance().showToast("请完善后再发布");
                    return;
                }

                RobotModel model = new RobotModel();
                queryList.add(editQuery1.getText().toString());
                queryList.add(editQuery2.getText().toString());
                queryList.add(editQuery3.getText().toString());

                // 设置model属性
                model.setTitle(editName.getText().toString());
                model.setDesc(editDesc.getText().toString());
                model.setRobotId(UUIDUtil.getUUID());
                model.setOwnerId(BmobManager.getInstance().getObjectId());
                model.setBeginSay(editStartSpeak.getText().toString());
                model.setQuestions(queryList);
                model.setCreateTime(System.currentTimeMillis());
                model.setUpdateTime(System.currentTimeMillis());
                model.setImageUrl("");
                model.setType(ROBOT_MODEL_NORMAL);

                RobotHelper.getInstance().save(model);
                // 通知SquareFragment刷新社区机器人
                EventBus.getDefault().postSticky(EventIdCenter.SQUARE_FRAGMENT_UPDATE_DATA);

                Bundle bundle = new Bundle();
                bundle.putString("model", "create");
                bundle.putSerializable("robot_data", model);
                ChatActivity.startUp(CreateRobotActivity.this, bundle);
                finish();
            }
        });
    }

    private void getData() {

    }
}