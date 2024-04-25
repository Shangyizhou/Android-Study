package com.example.androidnote.fragment.chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidnote.R;
import com.example.androidnote.adapter.OnItemViewClickListener;
import com.example.androidnote.adapter.SquareAdapter;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.manager.SessionManager;
import com.example.androidnote.model.RobotModel;
import com.shangyizhou.develop.helper.UUIDUtil;
import com.shangyizhou.develop.log.SLog;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SquareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SquareFragment extends Fragment {
    private static final String TAG = "SquareFragment";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private SquareAdapter adapter;
    private List<RobotModel> robotModelList = new ArrayList<>();;


    public SquareFragment() {
        // Required empty public constructor
    }
    public static SquareFragment newInstance(String param1, String param2) {
        SquareFragment fragment = new SquareFragment();
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
        View view = inflater.inflate(R.layout.fragment_square, container, false);
        initView(view);
        getData();
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycle_view);
        adapter = new SquareAdapter(new OnItemViewClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        List<String> querys = new ArrayList<>();
        querys.add("数组和链表的区别是什么？");
        querys.add("什么是时间复杂度？");
        querys.add("什么时候使用链表？");

        RobotModel model = new RobotModel();;
        model.setRobotId(UUIDUtil.getUUID());
        model.setTitle("数据结构智能体机器人");
        model.setOwnerId(BmobManager.getInstance().getObjectId());
        model.setDesc("帮助学生更好理解数据结构课程");
        model.setBeginSay("您好，我是数据结构智能体机器人，欢迎使用");
        model.setQuestions(querys);
        model.setCreateTime(System.currentTimeMillis());
        model.setUpdateTime(System.currentTimeMillis());

        robotModelList.add(model);
        robotModelList.add(model);
        adapter.updateDataList(robotModelList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SLog.i(TAG, "onDestroy");
        SessionManager.getInstance().saveHistoryMessage();
    }
}