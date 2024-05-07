package com.example.androidnote.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidnote.R;
import com.example.androidnote.adapter.OnItemViewClickListener;
import com.example.androidnote.adapter.SquareAdapter;
import com.example.androidnote.db.helper.RobotHelper;
import com.example.androidnote.model.RobotModel;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.base.BaseUiActivity;
import com.shangyizhou.develop.log.SLog;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();

    public static void startUp(Context context) {
        SLog.i(TAG, "[SearchActivity] startUp");
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_search);
        initView();
        getData();
    }

    private List<RobotModel> robotModelList = new ArrayList<>();
    private List<RobotModel> searchList = new ArrayList<>();
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private SquareAdapter adapter;
    private void initView() {
        recyclerView = findViewById(R.id.recycle_view);
        searchView = findViewById(R.id.search_view);
        toolbar = findViewById(R.id.tool_bar);
        searchView.setIconified(false); // 这将使搜索框默认展开

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        SLog.i(TAG, "getData");
        robotModelList = RobotHelper.getInstance().takeAll();
        updateWhenSearch(searchList);
    }

    private void search(String text) {
        List<RobotModel> searchList = new ArrayList<>();
        if (text.equals("")) {
            updateWhenSearch(searchList);
            return;
        }
        for (RobotModel model : robotModelList) {
            if (model.getTitle().contains(text)) {
                searchList.add(model);
            }
        }
        updateWhenSearch(searchList);
    }

    private void updateWhenSearch(List<RobotModel> searchList) {
        SLog.i(TAG, "updateWhenSearch: " + searchList);
        adapter.updateAll(searchList);
        // 滑动到底部
        recyclerView.scrollToPosition(0);
    }

}