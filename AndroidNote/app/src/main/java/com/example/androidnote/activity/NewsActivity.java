package com.example.androidnote.activity;

import java.lang.reflect.Type;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.androidnote.DirectToServer;
import com.example.androidnote.R;
import com.example.androidnote.adapter.NewsAdapter;
import com.example.androidnote.model.News;
import com.example.androidnote.model.NewsList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.helper.SimpleTaskExecutor;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.net.IResponse;

import java.util.List;

public class NewsActivity extends BaseActivity {
    private static final String TAG = NewsActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<News> mData;
    public static void startUp(Context context) {
        SLog.i(TAG, "[NewsActivity] startUp");
        Intent intent = new Intent(context, NewsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_news);
        initView();
        getData();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycle_view);
        adapter = new NewsAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        SimpleTaskExecutor.scheduleNow(new SimpleTaskExecutor.Task() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public void run() {
                DirectToServer.getNewsList(new IResponse() {
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
                               runOnUiThread(new Runnable() {
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