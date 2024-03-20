package com.shangyizhou.schoolchat.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.shangyizhou.schoolchat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends BaseFragment {

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
     * @return A new instance of fragment SecondFragment.
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
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        initView(view);
        return view;
    }

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    EditText inputText;
    Button sendBtn;
    List<ChatMessage> data;
    LinearLayoutManager layoutManager;
    private void initView(View view) {
        sendBtn = view.findViewById(R.id.send_btn);
        inputText = view.findViewById(R.id.input_text);
        recyclerView = view.findViewById(R.id.recycle_view);
        data = new ArrayList<>();
        adapter = new ChatAdapter(data);
        // 创建一个LinearLayoutManager，并设置其方向为垂直
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // 将创建的布局管理器应用到RecyclerView
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (content != null && !"".equals(content)) {
                    ChatMessage message = new ChatMessage(ChatMessage.TYPE_SENT, content);
                    data.add(message);
                    adapter.notifyItemInserted(data.size() - 1);
                    recyclerView.scrollToPosition(data.size() - 1);
                }
                inputText.setText("");
            }
        });
    }

    public class ChatMessage {
        int type;
        String content;
        public static final int TYPE_RECEIVED = 0;
        public static final int TYPE_SENT = 1;

        public ChatMessage(int type, String content) {
            this.type = type;
            this.content = content;
        }

        public int getType() {
            return type;
        }

        public String getContent() {
            return content;
        }
    }
}