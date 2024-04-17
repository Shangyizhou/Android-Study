package com.example.androidnote.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.androidnote.DirectToServer;
import com.example.androidnote.R;
import com.example.androidnote.adapter.ChatAdapter;
import com.example.androidnote.adapter.ChatAdapterMessage;
import com.example.androidnote.db.helper.MessageHelper;
import com.example.androidnote.db.helper.ResponseInfoHelper;
import com.example.androidnote.db.helper.SessionHelper;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.model.ChatModel;
import com.example.androidnote.model.Message;
import com.example.androidnote.model.ResponseInfo;
import com.example.androidnote.model.Session;
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
    private Session mSession;

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
    private List<Message> mMessageList = new ArrayList<>();
    ChatAdapterMessage mChatAdapter = new ChatAdapterMessage();
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
        saveHistoryMessage();
    }

    private void initView(View view) {
        mChatAdapter = new ChatAdapterMessage();
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
    public void reload(Session session) {
        if (session == null) {
            SLog.i(TAG, "reload session is null");
            return;
        }
        // 更新当前session修改时间
        session.setUpdateTime(System.currentTimeMillis());
        // 保存历史消息和会话
        saveHistoryMessage();
        // 更新当前session
        mSession = session;
        // 根据新sessionId获取历史消息
        List<Message> messageList = MessageHelper.getInstance().getMessageListBySession(session.getSessionId());
        SLog.i(TAG, "reload messageList" + messageList);
        mMessageList.clear();
        mMessageList.addAll(messageList);
        SLog.i(TAG, "reload messageList" + messageList);
        updateAdapterAll();
    }

    private void getData() {
        // // ResponseInfoHelper.getInstance().deleteData();
        // data = ResponseInfoHelper.getInstance().getAllModelInfoByUserName(BmobManager.getInstance().getUser().getUserName());
        // if (data.keySet().size() <= 0) {
        //     SLog.i(TAG, "getData" + data);
        //     data.put("ERNIE-4.0-8K", new ArrayList<ResponseInfo>());
        // }
        // SLog.i(TAG, "getData" + data);
        SLog.i(TAG, "getData");
        /**
         * 加载会话
         */
        if (mSessionId != null && mSession == null) {
            SLog.i(TAG, "mSessionId is exist");
            mSession = SessionHelper.getInstance().takeBySessionID(mSessionId);
        } else {
            SLog.i(TAG, "mSessionId is null");
            List<Session> sessions = SessionHelper.getInstance().takeAllByUser(BmobManager.getInstance().getObjectId());
            if (sessions != null && sessions.size() > 0) {
                SLog.i(TAG, "sessions exist, take last session");
                mSession = sessions.get(0);
                // 根据新sessionId获取历史消息
                List<Message> messageList = MessageHelper.getInstance().getMessageListBySession(mSession.getSessionId());
                SLog.i(TAG, "from sqlite get messageList" + messageList);
                mMessageList.clear();
                mMessageList.addAll(messageList);
                SLog.i(TAG, "reload messageList" + messageList);
                updateAdapterAll();
            } else {
                SLog.i(TAG, "sessions no exist, create new Session");
                mSession = new Session();
                mSession.setName("" + new Date(System.currentTimeMillis()));
                mSession.setDesc("hello ~");
                mSession.setRobotId("");
                mSession.setUserId(BmobManager.getInstance().getObjectId());
                mSession.setUrl("");
                mSession.setSessionId(UUIDUtil.getUUID());
                mSession.setIsDel(false);
                mSession.setCreateTime(System.currentTimeMillis());
                mSession.setUpdateTime(System.currentTimeMillis());
            }
        }
    }

    /**
     * 保存旧有的会话和历史消息
     * reload
     * destroy
     */
    private void saveHistoryMessage() {
        SLog.i(TAG, "saveHistoryMessage");
        SessionHelper.getInstance().save(mSession);
        MessageHelper.getInstance().saveMessageList(mMessageList);
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
            // 先加入Person的Text
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
        message.setSessionId(mSession.getSessionId());
        message.setMessage(editText.getText().toString());
        message.setMessageId(SnowFlakeUtil.getSnowFlakeId() + "");
        message.setStatus(ChatModel.START_SHOW);
        message.setSendTime(System.currentTimeMillis());
        mMessageList.add(message);


        // ChatModel model = new ChatModel();
        // model.setId(UUIDUtil.getUUID());
        // model.setName("syz");
        // model.setImageUrl("");
        // model.setMessage(editText.getText().toString());
        // model.setType(PERSON_TEXT);
        // mList.add(model);
        updateAdapter();
    }

    private void addRobot() {
        Message message = new Message();
        message.setType(ROBOT_TEXT);
        message.setSessionId(mSession.getSessionId());
        message.setMessage("");
        message.setMessageId(SnowFlakeUtil.getSnowFlakeId() + "");
        message.setStatus(LOADING);
        message.setSendTime(System.currentTimeMillis()); // responseTime
        mMessageList.add(message);
        updateAdapter();

        // ChatModel model = new ChatModel();
        // model.setId(UUIDUtil.getUUID());
        // model.setName("");
        // model.setImageUrl("");
        // model.setMessage("");
        // model.setStatus(ChatModel.LOADING);
        // model.setType(ROBOT_TEXT);
        // mMessageList.add(model);
        // updateAdapter();
    }

    // TODO: callYiyan callEB
    public void callYiyan() {
        callEBStream();
    }

    // private void callEB() {
    //     SLog.i(TAG, "sendBtn");
    //     String inputText = editText.getText().toString();
    //     if (TextUtils.isEmpty(inputText)) {
    //         return;
    //     }
    //     showSpeak(PERSON_TEXT, inputText);
    //     editText.setText("");
    //     DirectToServer.callYiYan(inputText, new IResponse() {
    //         @Override
    //         public void onSuccess(String originJson) {
    //             getActivity().runOnUiThread(new Runnable() {
    //                 @Override
    //                 public void run() {
    //
    //                     showSpeak(ROBOT_TEXT, originJson);
    //                 }
    //             });
    //         }
    //
    //         @Override
    //         public void onFailure(String errorMsg) {
    //             SLog.e(TAG, "DirectToServer error");
    //         }
    //     });
    // }


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
        DirectToServer.callYiYanERNIEStream(inputText, new IResponse() {
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
                SLog.i(TAG, String.valueOf(messages));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showResponse(ROBOT_TEXT, assistant.get("content"));
                    }
                });
            }

            @Override
            public void onFailure(String errorMsg) {
                SLog.e(TAG, "DirectToServer error");
            }
        });
    }

    /**
     * @param type 0:机器人 1:用户
     * @param text
     */
    private void showResponse(int type, String text) {
        SLog.i(TAG, "Robot showResponse:" + text);
        // ChatModel model = mMessageList.get(mMessageList.size() - 1);
        Message message = mMessageList.get(mMessageList.size() - 1);
        message.setStatus(START_SHOW);
        message.setMessage(text);
        message.setSendTime(System.currentTimeMillis());
        updateAdapter();
    }

    private void updateAdapter() {
        mChatAdapter.updateDataList(mMessageList);
        // 滑动到底部
        recyclerView.scrollToPosition(mMessageList.size() - 1);
    }

    private void updateAdapterAll() {
        mChatAdapter.updateDataListAll(mMessageList);
        // 滑动到底部
        recyclerView.scrollToPosition(mMessageList.size() - 1);
    }
}