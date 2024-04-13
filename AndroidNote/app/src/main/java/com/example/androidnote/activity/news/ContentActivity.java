package com.example.androidnote.activity.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.androidnote.R;
import com.example.androidnote.model.News;
import com.google.gson.Gson;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.log.SLog;

public class ContentActivity extends BaseActivity {
    private static final String TAG = ContentActivity.class.getSimpleName();
    private static News news;
    private WebView webView;

    public static void startUp(Context context, Bundle bundle) {
        SLog.i(TAG, "[ContentActivity] startUp");
        Intent intent = new Intent(context, ContentActivity.class);
        if (bundle != null) {
            String originJson = bundle.getString("News");
            news = new Gson().fromJson(originJson, News.class);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_content);
        initView();
    }

    private void initView() {
        webView = (WebView)findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        String uri = news.getUrl();
        webView.loadUrl(uri);
    }
}