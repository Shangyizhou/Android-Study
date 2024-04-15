package com.example.androidnote.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.androidnote.DirectToServer;
import com.example.androidnote.R;
import com.example.androidnote.adapter.ChatAdapter;
import com.example.androidnote.db.helper.ResponseInfoHelper;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.model.ChatModel;
import com.example.androidnote.model.ResponseInfo;
import com.shangyizhou.develop.helper.ToastUtil;
import com.shangyizhou.develop.helper.UUIDUtil;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.net.IResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


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
    private List<ChatModel> mList = new ArrayList<>();
    ChatAdapter mChatAdapter = new ChatAdapter();
    private RecyclerView recyclerView;
    private Button sendBtn;
    private EditText editText;
    public static HashMap<String, List<ResponseInfo>> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
        SLog.i(TAG, "onDestroy saveHashMap");
        ResponseInfoHelper.getInstance().saveHashMap(data);
    }

    private void initView(View view) {
        mChatAdapter = new ChatAdapter();
        recyclerView = view.findViewById(R.id.mChatView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sendBtn = view.findViewById(R.id.btn_send_msg);
        editText = view.findViewById(R.id.et_input_msg);
        recyclerView.setAdapter(mChatAdapter);
        sendBtn.setOnClickListener(this);
    }

    private void getData() {
        // ResponseInfoHelper.getInstance().deleteData();
        data = ResponseInfoHelper.getInstance().getAllModelInfoByUserName(BmobManager.getInstance().getUser().getUserName());
        if (data.keySet().size() <= 0) {
            SLog.i(TAG, "getData" + data);
            data.put("ERNIE-4.0-8K", new ArrayList<ResponseInfo>());
        }
        SLog.i(TAG, "getData" + data);
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
        }
    }

    private void addPerson() {
        ChatModel model = new ChatModel();
        model.setId(UUIDUtil.getUUID());
        model.setName("syz");
        model.setImageUrl("");
        model.setMessage(editText.getText().toString());
        model.setType(PERSON_TEXT);
        mList.add(model);
        updateAdapter();
    }

    private void addRobot() {
        ChatModel model = new ChatModel();
        model.setId(UUIDUtil.getUUID());
        model.setName("");
        model.setImageUrl("");
        model.setMessage("");
        model.setStatus(ChatModel.LOADING);
        model.setType(ROBOT_TEXT);
        mList.add(model);
        updateAdapter();
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
        ChatModel model = mList.get(mList.size() - 1);
        model.setStatus(ChatModel.START_SHOW);
        model.setMessage(text);
        updateAdapter();
    }

    private void updateAdapter() {
        mChatAdapter.updateDataList(mList);
        // 滑动到底部
        recyclerView.scrollToPosition(mList.size() - 1);
    }
}