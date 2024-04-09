package com.example.androidnote.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.androidnote.adapter.CommonAdapter;
import com.example.androidnote.DirectToServer;
import com.example.androidnote.R;
import com.example.androidnote.model.ChatModel;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.model.EventIdCenter;
import com.shangyizhou.develop.model.MessageEvent;
import com.shangyizhou.develop.net.IResponse;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity implements View.OnClickListener {
    private List<ChatModel> mList = new ArrayList<>();
    CommonAdapter mChatAdapter = new CommonAdapter();
    private static final String TAG = "ChatActivity";
    private RecyclerView recyclerView;
    private Button sendBtn;
    private EditText editText;

    public static void startUp(Context context) {
        SLog.i(TAG, "[ChatActivity] startUp");
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_chat);
        initView();
    }

    private void initView() {
        mChatAdapter = new CommonAdapter();
        recyclerView = findViewById(R.id.mChatView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sendBtn = findViewById(R.id.btn_send_msg);
        editText = findViewById(R.id.et_input_msg);
        recyclerView.setAdapter(mChatAdapter);

        sendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        SLog.i(TAG, "sendBtn");
        if (id == R.id.btn_send_msg) {
            SLog.i(TAG, "sendBtn");
            String inputText = editText.getText().toString();
            if (TextUtils.isEmpty(inputText)) {
                return;
            }
            addText(1, inputText);
            editText.setText("");
            DirectToServer.callYiYan(inputText, new IResponse() {
                @Override
                public void onSuccess(String originJson) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addText(0, originJson);
                        }
                    });
                }

                @Override
                public void onFailure(String errorMsg) {
                    SLog.e(TAG, "DirectToServer error");
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event == null || event.message == null) {
            SLog.e(TAG, "【onMessageEvent】event is null");
            return;
        }
        switch (event.name) {
            case EventIdCenter.ROBOT_MESSAGE_ARRIVED:
                String msg = event.message;
                addText(0, msg);
                break;
        }
    }

    private void addText(int type, String text) {
        SLog.i(TAG, "ChatA:" + text);
        ChatModel model = new ChatModel("123", "张三", "", type);
        model.setMessage(text);
        baseAddItem(model);
    }

    private void baseAddItem(ChatModel model) {
        mList.add(model);
        mChatAdapter.updateDataList(mList);
        //滑动到底部
        recyclerView.scrollToPosition(mList.size() - 1);
    }


}
