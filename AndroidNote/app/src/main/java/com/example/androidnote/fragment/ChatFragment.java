package com.example.androidnote.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.androidnote.DirectToServer;
import com.example.androidnote.R;
import com.example.androidnote.adapter.ChatAdapter;
import com.example.androidnote.model.ChatModel;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.model.EventIdCenter;
import com.shangyizhou.develop.model.MessageEvent;
import com.shangyizhou.develop.net.IResponse;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    private List<ChatModel> mList = new ArrayList<>();
    ChatAdapter mChatAdapter = new ChatAdapter();
    private static final String TAG = "ChatActivity";
    private RecyclerView recyclerView;
    private Button sendBtn;
    private EditText editText;
    private LinearLayout llBottom;

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
        return view;
    }

    private void initView(View view) {
        mChatAdapter = new ChatAdapter();
        recyclerView = view.findViewById(R.id.mChatView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sendBtn = view.findViewById(R.id.btn_send_msg);
        editText = view.findViewById(R.id.et_input_msg);
        recyclerView.setAdapter(mChatAdapter);

        sendBtn.setOnClickListener(this);
        editText.setOnClickListener(this);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // 点击聊天背景的时候输入键盘隐藏，那个隐藏的view也重新显示出来
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            private Context context = getActivity();

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager manager = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
                    if (manager != null)
                        manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_send_msg) {
            String inputText = editText.getText().toString();
            if (TextUtils.isEmpty(inputText)) {
                return;
            }
            addText(1, inputText);
            editText.setText("");
            DirectToServer.callYiYan(inputText, new IResponse() {
                @Override
                public void onSuccess(String originJson) {
                    getActivity().runOnUiThread(new Runnable() {
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
        } else if (id == R.id.et_input_msg) {
            llBottom.setVisibility(View.GONE);
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