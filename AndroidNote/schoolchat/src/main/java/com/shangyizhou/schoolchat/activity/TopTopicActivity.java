package com.shangyizhou.schoolchat.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.shangyizhou.schoolchat.R;
import com.shangyizhou.schoolchat.fragment.FirstFragment;
import com.shangyizhou.schoolchat.fragment.SecondFragment;
import com.shangyizhou.schoolchat.fragment.ThirdFragment;

import java.util.ArrayList;
import java.util.List;

public class TopTopicActivity extends BaseActivity {
    private List<Fragment> fragments;
    private ViewPager2 viewpager;
    private TabLayout tabLayout;
    private FragmentManager manager;
    private final String[] TAB_TITLES = new String[]{"one", "two", "three"};
    private final int[] TAB_IMGS = new int[]{R.drawable.refresh,R.drawable.refresh,R.drawable.refresh,R.drawable.refresh};

    @Override
    protected void onCreateChildren(Bundle bundle) {
        initView();
    }

    @SuppressLint("MissingInflatedId")
    private void initView() {
        setContentView(R.layout.activity_top_topic);
        manager = getSupportFragmentManager();
        viewpager = (ViewPager2) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        fragments = new ArrayList<>();
        fragments.add(new FirstFragment());
        fragments.add(new SecondFragment());
        fragments.add(new ThirdFragment());

        //设置viewpager的适配器

        //设置导航图片数据
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View view =  LayoutInflater.from(this).inflate(R.layout.tab_top_item_view, null);
            tabLayout.getTabAt(i).setCustomView(view);

            TextView tvTitle = (TextView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.textView);
            tvTitle.setText(TAB_TITLES[i]);

            ImageView imgTab = (ImageView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.imageView);
            imgTab.setImageDrawable(getResources().getDrawable(TAB_IMGS[i]));
        }

        MyAdapter adapter = new MyAdapter(manager, getLifecycle(), fragments);
        viewpager.setAdapter(adapter);

        // 禁止左右滑动,false为禁止
        viewpager.setUserInputEnabled(true);

        new TabLayoutMediator(tabLayout, viewpager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setCustomView(R.layout.tab_top_item_view);
                TextView tvTitle = (TextView) tab.getCustomView().findViewById(R.id.textView);
                tvTitle.setText(TAB_TITLES[position]);
                ImageView imgTab = (ImageView) tab.getCustomView().findViewById(R.id.imageView);
                imgTab.setImageDrawable(getResources().getDrawable(TAB_IMGS[position]));
            }
        }).attach();

    }

    private class MyAdapter extends FragmentStateAdapter {
        List<Fragment> fragments;

        public MyAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Fragment> fragments) {
            super(fragmentManager, lifecycle);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }

}