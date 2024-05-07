package com.example.androidnote.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidnote.R;
import com.example.androidnote.adapter.OnItemViewClickListener;
import com.example.androidnote.adapter.SquareAdapter;
import com.example.androidnote.db.helper.RobotHelper;
import com.example.androidnote.manager.SessionManager;
import com.example.androidnote.model.RobotModel;
import com.google.android.material.tabs.TabLayout;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.model.EventIdCenter;
import com.shangyizhou.develop.model.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    Toolbar toolbar;
    SearchView searchView;
    TabLayout tabLayout;
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
        EventBus.getDefault().register(this);
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

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initView(View view) {
        // toolbar = view.findViewById(R.id.toolbar);
        searchView = view.findViewById(R.id.search_view);
        recyclerView = view.findViewById(R.id.recycle_view);
        tabLayout = view.findViewById(R.id.tab_layout);
        adapter = new SquareAdapter(new OnItemViewClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void search(String text) {
        List<RobotModel> searchList = new ArrayList<>();
        for (RobotModel model : robotModelList) {
            if (model.getTitle().contains(text)) {
                searchList.add(model);
            }
        }
        updateWhenSearch(searchList);
    }

    private void getData() {
        SLog.i(TAG, "getData");
        robotModelList = RobotHelper.getInstance().takeAll();
        updateAdapter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SLog.i(TAG, "onDestroy");
        SessionManager.getInstance().saveHistoryMessage();
    }

    private void updateAdapter() {
        adapter.updateAll(robotModelList);
        // 滑动到底部
        recyclerView.scrollToPosition(0);
    }

    private void updateWhenSearch(List<RobotModel> searchList) {
        adapter.updateAll(searchList);
        // 滑动到底部
        recyclerView.scrollToPosition(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event == null || event.message == null) {
            SLog.e(TAG, "【onMessageEvent】event is null");
            return;
        }
        switch (event.name) {
            case EventIdCenter.SQUARE_FRAGMENT_UPDATE_DATA:
                SLog.i(TAG, "SQUARE_FRAGMENT_UPDATE_DATA event");
                getData();
                break;
        }
    }
}