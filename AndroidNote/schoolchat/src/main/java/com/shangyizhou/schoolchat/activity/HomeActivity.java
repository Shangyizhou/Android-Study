package com.shangyizhou.schoolchat.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shangyizhou.schoolchat.R;
import com.shangyizhou.schoolchat.fragment.FirstFragment;
import com.shangyizhou.schoolchat.fragment.ChatFragment;
import com.shangyizhou.schoolchat.fragment.ThirdFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {
    private BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager;
    private List<Fragment> fragments;
    private FragmentManager manager;


    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_home);
        initView();
    }

    private void initView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.viewpager);
        fragments = new ArrayList<>();
        fragments.add(new FirstFragment());
        fragments.add(new ChatFragment());
        fragments.add(new ThirdFragment());
        manager = getSupportFragmentManager();
        MyAdapter adapter = new MyAdapter(manager, getLifecycle(), fragments);
        viewPager.setAdapter(adapter);
        // bottomNavigationView.setItemIconSize(12);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                viewPager.setCurrentItem(0);
                return true;
            } else if (id == R.id.navigation_dashboard) {
                viewPager.setCurrentItem(1);
                return true;
            } else if (id == R.id.navigation_notifications) {
                viewPager.setCurrentItem(2);
                return true;
            }
            return false;
        });
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