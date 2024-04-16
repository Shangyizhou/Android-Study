package com.example.androidnote.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidnote.DirectToServer;
import com.example.androidnote.R;
import com.example.androidnote.adapter.NewsAdapter;
import com.example.androidnote.model.News;
import com.example.androidnote.model.NewsList;
import com.google.gson.Gson;
import com.shangyizhou.develop.helper.SimpleTaskExecutor;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.net.IResponse;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_URL = "url_name";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mUrl;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        // args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        args.putString(ARG_URL, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // mParam1 = getArguments().getString(ARG_PARAM1);
            // mParam2 = getArguments().getString(ARG_PARAM2);
            mUrl = getArguments().getString(ARG_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_news, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private static final String TAG = NewsFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<News> mData;
    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycle_view);
        adapter = new NewsAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        getData();
    }

    private void getData() {
        SimpleTaskExecutor.scheduleNow(new SimpleTaskExecutor.Task() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public void run() {
                DirectToServer.getNewsList(mUrl, new IResponse() {
                    @Override
                    public void onSuccess(String originJson) {
                        try {
                            SLog.i(TAG, originJson);
                            // Gson无法直接解析嵌套的泛型
                            NewsList newsList = new Gson().fromJson(originJson, NewsList.class);
                            if (newsList != null && newsList.newsList != null) {
                                for (int i = 0; i < newsList.newsList.size(); i++) {
                                    SLog.i(TAG, String.valueOf(newsList.newsList.get(i)));
                                }
                                mData = newsList.newsList;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.updateDataList(mData);
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String errorMsg) {

                    }
                });
            }
        });
    }
}