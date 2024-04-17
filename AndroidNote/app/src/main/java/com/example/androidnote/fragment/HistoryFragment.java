package com.example.androidnote.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidnote.R;
import com.example.androidnote.adapter.HistoryAdapter;
import com.example.androidnote.db.helper.SessionHelper;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.model.Session;
import com.shangyizhou.develop.log.SLog;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HIstoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryFragment(onItemViewClickListener listener) {
        mOnItemClickListener = listener;
    }

    private static final String TAG = "HistoryFragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private HistoryAdapter mHistoryAdapter;
    private RecyclerView recyclerView;
    private List<Session> mSessions;
    private onItemViewClickListener mOnItemClickListener;
    public void setOnItemClickListener(onItemViewClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_h_istory, container, false);
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        initView(view);
        getData();
        return view;
    }

    private void initView(View view) {
        mHistoryAdapter = new HistoryAdapter();
        mHistoryAdapter.setOnItemClickListener(new HistoryAdapter.onItemViewClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Session session = mSessions.get(position);
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(session);
            }
        });
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mHistoryAdapter);
    }

    private void getData() {
        SLog.i(TAG, "getData: objectId = " + BmobManager.getInstance().getObjectId());
        mSessions = SessionHelper.getInstance().takeAllByUser(BmobManager.getInstance().getObjectId());
        SLog.i(TAG, "getData: objectId = " + mSessions);
        if (mSessions == null) {
            SLog.i(TAG, "getData: mSessions is null");
        }
        mHistoryAdapter.updateDataList(mSessions);
    }
    public interface onItemViewClickListener {
        void onItemClick(Session session);
    }
}