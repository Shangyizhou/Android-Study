package com.example.androidnote.fragment.chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.androidnote.DirectToServer;
import com.example.androidnote.R;
import com.example.androidnote.adapter.ChatAdapterMessage;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.manager.SessionManager;
import com.example.androidnote.model.ChatModel;
import com.example.androidnote.model.Message;
import com.example.androidnote.model.ResponseInfo;
import com.example.androidnote.model.Session;
import com.shangyizhou.develop.helper.DateHelper;
import com.shangyizhou.develop.helper.SnowFlakeUtil;
import com.shangyizhou.develop.helper.ToastUtil;
import com.shangyizhou.develop.helper.UUIDUtil;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.net.IResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ChatFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "session_id";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mSessionId;
    private Session mCurrentSession;
    private List<Session> mSessionsList;

    private static final int ROBOT_TEXT = 0;
    private static final int PERSON_TEXT = 1;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private Map<Integer, ChatModel> chatModelIndex = new HashMap<>();
    private List<Message> mMessageList;
    private List<String> mQueryList;
    ChatAdapterMessage mChatAdapter;
    private RecyclerView recyclerView;
    private Button sendBtn;
    private EditText editText;
    public static HashMap<String, List<ResponseInfo>> data;

    public static final int  LOADING = 0;
    public static final int  START_SHOW = 1;
    public static final int  HAS_SHOW = 2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            // mSessionId = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_chat, container, false);
        initView(view);
        getData();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SLog.i(TAG, "onDestroy");
        SessionManager.getInstance().saveHistoryMessage();
    }

    private void initView(View view) {
        mMessageList = new ArrayList<>();
        mChatAdapter = new ChatAdapterMessage(mMessageList);
        recyclerView = view.findViewById(R.id.mChatView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sendBtn = view.findViewById(R.id.btn_send_msg);
        editText = view.findViewById(R.id.et_input_msg);
        recyclerView.setAdapter(mChatAdapter);
        sendBtn.setOnClickListener(this);
    }

    /**
     * 其他会话页面点击打开，重新加载页面
     */
    public void reload(Session session, boolean isNew) {
        if (session == null) {
            SLog.i(TAG, "reload session is null");
            return;
        }

        if (!isNew) {
            // 根据新sessionId获取历史消息
            // List<Message> messageList = MessageHelper.getInstance().getMessageListBySession(mCurrentSession.getSessionId());
            mMessageList = SessionManager.getInstance().getSessionMessages(session);
            SLog.i(TAG, "reload messageList" + mMessageList);
            updateAdapterAll();
        } else {
            SLog.i(TAG, "add new session need not database");
            mMessageList = SessionManager.getInstance().getSessionMessages(session);
            SLog.i(TAG, mMessageList.size() + " reload messageList" + mMessageList);
            updateAdapterAll();
        }
    }

    public void addNewSession() {
        if (mMessageList.size() <= 0) {
            ToastUtil.getInstance().showToast("当前已经是最新会话");
            return;
        }
        SLog.i(TAG, "addNewSession ");
        Session session = new Session();
        session.setName(DateHelper.getInstance().getCurrentTime());
        session.setDesc("hello world");
        session.setRobotId("");
        session.setUserId(BmobManager.getInstance().getObjectId());
        session.setUrl("");
        session.setSessionId(UUIDUtil.getUUID());
        session.setIsDel(false);
        session.setCreateTime(System.currentTimeMillis());
        session.setUpdateTime(System.currentTimeMillis());
        SessionManager.getInstance().addNewSession(session);

        // 更新当前session
        mCurrentSession = session;
        reload(mCurrentSession, true);
    }

    private void getData() {
        SLog.i(TAG, "getData");

        /**
         * 加载会话
         * 内部封装了会话列表的获取
         * 1.如果缓存没有则去数据库获取
         * 2.如果数据库没有则创建一个新的会话
         */
        mSessionsList = SessionManager.getInstance().getSessionList();
        if (mSessionsList != null && mSessionsList.size() > 0) {
            SLog.i(TAG, "sessions exist, take last session");
            mCurrentSession = SessionManager.getInstance().getLastSession();
            mMessageList = SessionManager.getInstance().getSessionMessages(mCurrentSession);
            SLog.i(TAG, "reload messageList" + mMessageList);
            updateAdapterAll();
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
        callEBStream();
    }

    // 历史对话，需要按照user,assistant
    static List<Map<String, String>> messages = new ArrayList<>();

    private void callEBStream() {
        SLog.i(TAG, "sendBtn");
        // 获取输入的问题
        String inputText = editText.getText().toString();
        if (TextUtils.isEmpty(inputText)) {
            return;
        }
        // 清空输入框
        editText.setText("");
        DirectToServer.callYiYanERNIELiteStream(inputText, new IResponse() {
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
                // SLog.i(TAG, String.valueOf(messages));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showResponse(assistant.get("content"));
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