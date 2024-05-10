package com.example.androidnote.activity;

import static com.example.androidnote.constant.Constants.YIYAN_HANDLER_INTENT;
import static com.example.androidnote.constant.Constants.YIYAN_HANDLER_NORMAL;
import static com.example.androidnote.constant.Constants.YIYAN_HANDLER_QUERY;
import static com.example.androidnote.net.YiYanHandler.mIntent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidnote.db.helper.SessionHelper;
import com.example.androidnote.model.MessageExtern;
import com.example.androidnote.net.DirectToServer;
import com.example.androidnote.R;
import com.example.androidnote.adapter.ChatAdapterMessage;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.manager.SessionManager;
import com.example.androidnote.model.ChatModel;
import com.example.androidnote.model.Message;
import com.example.androidnote.model.ResponseInfo;
import com.example.androidnote.model.RobotModel;
import com.example.androidnote.model.Session;
import com.example.androidnote.net.YiYanHandler;
import com.google.gson.Gson;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.helper.SnowFlakeUtil;
import com.shangyizhou.develop.helper.ToastUtil;
import com.shangyizhou.develop.helper.UUIDUtil;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.model.EventIdCenter;
import com.shangyizhou.develop.model.MessageEvent;
import com.shangyizhou.develop.net.IResponse;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ChatActivity.class.getSimpleName();

    private List<Message> mMessageList;
    ChatAdapterMessage mChatAdapter;
    private RecyclerView recyclerView;
    private Button sendBtn;
    private EditText editText;
    public static HashMap<String, List<ResponseInfo>> data;

    public static final int  LOADING = 0;
    public static final int  START_SHOW = 1;
    public static final int  HAS_SHOW = 2;

    private Session mCurrentSession;
    private List<Session> mSessionsList;

    private static final int ROBOT_TEXT = 0;
    private static final int PERSON_TEXT = 1;

    private String startMode;
    private RobotModel mCurrentRobot;
    private List<String> mQueryList;

    private Button parseBtn;

    @Override
    protected void onCreateChildren(Bundle bundle) {
        // super.onCreateChildren(bundle);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        if (intent != null) {
            startMode = intent.getStringExtra("model");
            // if (startMode.equals("create")) {
            //     mCurrentRobot = (RobotModel) intent.getSerializableExtra("robot_data");
            // }
            mCurrentRobot = (RobotModel) intent.getSerializableExtra("robot_data");
        }
        initView();
    }

    @Override
    protected void onResume() {
        SLog.i(TAG, "[ChatActivity] onResume");
        super.onResume();
        getData();
    }

    public static void startUp(Context context, Bundle bundle) {
        SLog.i(TAG, "[ChatActivity] startUp");
        Intent intent = new Intent(context, ChatActivity.class);
        if (bundle != null) {
            intent.putExtra("model", bundle.getString("model"));
            intent.putExtra("robot_data", (RobotModel) bundle.getSerializable("robot_data"));
        }
        context.startActivity(intent, bundle);
    }


    private void initView() {
        mMessageList = new ArrayList<>();
        mChatAdapter = new ChatAdapterMessage(mMessageList);
        recyclerView = findViewById(R.id.mChatView);
        parseBtn = findViewById(R.id.ai_parse);
        sendBtn = findViewById(R.id.btn_send_msg);
        editText = findViewById(R.id.et_input_msg);
        recyclerView.setAdapter(mChatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sendBtn.setOnClickListener(this);
        parseBtn.setOnClickListener(this);
        parseBtn.setVisibility(View.VISIBLE);

        mChatAdapter.setOnItemClickListener(new ChatAdapterMessage.OnItemClickListener() {
            @Override
            public void onItemClick(String text) {
                editText.setText(text);
            }
        });
    }
    private boolean isInit = false;
    private void getData() {
        SLog.i(TAG, "getData");
        if (isInit) {
            SLog.i(TAG, "has init");
            mCurrentSession = SessionManager.getInstance().getSessionByRobotId(mCurrentRobot.getRobotId());
            mMessageList = SessionManager.getInstance().getSessionMessages(mCurrentSession);
            updateAdapterAll();
        }
        if (startMode.equals("create")) {
            /**
             * 新建会话
             */
            Session session = new Session();
            session.setSessionId(UUIDUtil.getUUID());
            session.setName(mCurrentRobot.getTitle());
            session.setDesc(mCurrentRobot.getDesc());
            session.setRobotId(mCurrentRobot.getRobotId());
            session.setUserId(BmobManager.getInstance().getObjectId());
            session.setUrl("");
            session.setCreateTime(System.currentTimeMillis());
            session.setUpdateTime(System.currentTimeMillis());
            SessionManager.getInstance().addNewSession(session);
            // 没有则新建一个空的 mMessageList = new ArrayList<>();
            mMessageList = SessionManager.getInstance().getSessionMessages(session);
            mCurrentSession = session;
            // 加入一个新的开场白
            addRobotStartSpeak();
            isInit = true;
        } else if (startMode.equals("load")) {
            /**
             * 加载会话
             * 内部封装了会话列表的获取
             * 1.如果缓存没有则去数据库获取
             * 2.如果数据库没有则创建一个新的会话
             */
            mSessionsList = SessionManager.getInstance().getSessionList();
            if (mSessionsList != null && mSessionsList.size() > 0) {
                SLog.i(TAG, "sessions exist, take last session");
                mCurrentSession = SessionManager.getInstance().getSessionByRobotId(mCurrentRobot.getRobotId());
                mMessageList = SessionManager.getInstance().getSessionMessages(mCurrentSession);
                if (mMessageList.size() == 0) {
                    addRobotStartSpeak();
                } else {
                    SLog.i(TAG, "reload messageList" + mMessageList);
                    updateAdapterAll();
                }
            }
            isInit = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SLog.i(TAG, "onDestroy");
        SessionManager.getInstance().saveHistoryMessage();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event == null || event.message == null) {
            SLog.e(TAG, "【onMessageEvent】event is null");
            return;
        }
        switch (event.name) {
            case EventIdCenter.EVENT_TEXT_CLICK:
                SLog.i(TAG, "EVENT_TEXT_CLICK");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        SLog.i(TAG, "sendBtn");
        if (id == R.id.btn_send_msg) {
            if (TextUtils.isEmpty(editText.getText().toString())) {
                ToastUtil.getInstance().showToast("输入内容不能为空");
                return;
            }
            addPerson();
            addRobot();
            callYiyan();
        } else if (id == R.id.et_input_msg) {
            // TODO: 点击输入框，隐藏软键盘

        } else if (id == R.id.ai_parse) {
            callYiyanForQuery();
        }
    }

    private void addPerson() {
        Message message = new Message();
        message.setType(PERSON_TEXT);
        message.setSessionId(mCurrentSession.getSessionId());
        message.setMessage(editText.getText().toString());
        message.setMessageId(SnowFlakeUtil.getSnowFlakeId() + "");
        message.setStatus(ChatModel.START_SHOW);
        message.setSendTime(System.currentTimeMillis());
        mMessageList.add(message);

        updateAdapter();
    }

    private void addRobotStartSpeak() {
        Message message = new Message();
        message.setType(ROBOT_TEXT);
        message.setSessionId(mCurrentSession.getSessionId());
        message.setMessage(mCurrentRobot.getBeginSay());
        message.setMessageId(SnowFlakeUtil.getSnowFlakeId() + "");
        message.setStatus(START_SHOW);
        message.setSendTime(System.currentTimeMillis()); // responseTime
        mMessageList.add(message);
        ChatAdapterMessage.mQuery = mCurrentRobot.getQuestions();
        updateAdapter();
    }

    private void addRobot() {
        Message message = new Message();
        message.setType(ROBOT_TEXT);
        message.setSessionId(mCurrentSession.getSessionId());
        message.setMessage("");
        message.setMessageId(SnowFlakeUtil.getSnowFlakeId() + "");
        message.setStatus(LOADING);
        message.setSendTime(System.currentTimeMillis()); // responseTime
        mMessageList.add(message);

        updateAdapter();
    }

    // TODO: callYiyan callEB
    public void callYiyan() {
        SLog.i(TAG, "sendBtn");
        // 获取输入的问题
        String inputText = editText.getText().toString();
        if (TextUtils.isEmpty(inputText)) {
            return;
        }
        // 清空输入框
        editText.setText("");
        // callEBStream(inputText, YIYAN_HANDLER_NORMAL);
        callEBStream(inputText, YIYAN_HANDLER_INTENT);
    }

    public void callYiyanForQuery() {
        List<String> queryList = new ArrayList<>();
        List<Message> messageList = SessionManager.getInstance().getSessionMessages(mCurrentSession);
        for (Message message : messageList) {
            if (message.getType() == 1) {
                queryList.add(message.getMessage());
            }
        }

        String jsonQuery = new Gson().toJson(queryList);
        SLog.i(TAG, "jsonQuery: " + jsonQuery);
        // callEBStream(jsonQuery, YIYAN_HANDLER_QUERY);
        Bundle bundle = new Bundle();
        bundle.putString("query", jsonQuery);
        ParseActivity.startUp(this, bundle);
    }

    // 历史对话，需要按照user,assistant
    static List<Map<String, String>> messages = new ArrayList<>();

    private void callEBStream(String text, String type) {
        DirectToServer.callYiYanERNIELiteStream(text, type, new IResponse() {
            @Override
            public void onSuccess(String originJson) {
                // 将回复的内容添加到消息中
                HashMap<String, String> assistant = new HashMap<>();
                assistant.put("role", "assistant");
                assistant.put("content", "");
                // 取出我们需要的内容,也就是result部分
                String[] answerArray = originJson.split("data: ");
                for (int i = 1; i < answerArray.length; ++i) {
                    answerArray[i] = answerArray[i].substring(0, answerArray[i].length() - 2);
                    SLog.i(TAG, "answerArray: " + answerArray[i]);
                    try {
                        assistant.put("content", assistant.get("content") + new JSONObject(answerArray[i]).getString("result"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                messages.add(assistant);
                String res = "";
                if (type.equals(YIYAN_HANDLER_NORMAL)) {
                     res = YiYanHandler.process(getApplication(), assistant.get("content"));
                } else if (type.equals(YIYAN_HANDLER_QUERY)) {
                    YiYanHandler.processQuery(getApplication(), assistant.get("content"));
                    ParseActivity.startUp(ChatActivity.this);
                    return;
                } else if (type.equals(YIYAN_HANDLER_INTENT)) {
                    res = YiYanHandler.processIntent(getApplication(), assistant.get("content"));
                    if (mMessageList != null) {
                        Message message = mMessageList.get(mMessageList.size() - 2);
                        MessageExtern extern = new MessageExtern();
                        extern.setIntent(YiYanHandler.mIntent);
                        message.setExt(new Gson().toJson(extern));
                    }
                }
                // SLog.i(TAG, String.valueOf(messages));
                String finalRes = res;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showResponse(finalRes);
                    }
                });
            }

            @Override
            public void onFailure(String errorMsg) {
                SLog.e(TAG, "DirectToServer error");
            }
        });
    }

    private void showResponse(String text) {
        SLog.i(TAG, "Robot showResponse:" + text);
        Message message = mMessageList.get(mMessageList.size() - 1);
        message.setStatus(START_SHOW);
        message.setMessage(text);
        message.setSendTime(System.currentTimeMillis());
        updateAdapter();
    }


    private void updateAdapter() {
        mChatAdapter.updateAll(mMessageList);
        // 滑动到底部
        recyclerView.scrollToPosition(mMessageList.size() - 1);
    }

    private void updateAdapterAll() {
        mChatAdapter.updateAll(mMessageList);
        // 滑动到底部
        recyclerView.scrollToPosition(mMessageList.size() - 1);
    }


}