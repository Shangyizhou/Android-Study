package com.example.androidnote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidnote.model.ChatModel;
import com.shangyizhou.develop.adapter.example.BaseViewHolder;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.model.EventIdCenter;
import com.shangyizhou.develop.model.MessageEvent;

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
