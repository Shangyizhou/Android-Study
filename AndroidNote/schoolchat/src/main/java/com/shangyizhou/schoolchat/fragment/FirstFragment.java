package com.shangyizhou.schoolchat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.shangyizhou.schoolchat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends BaseFragment {
    private List<Fragment> fragments;
    private ViewPager2 viewpager;
    private TabLayout tabLayout;
    private FragmentManager manager;
    private final String[] topTitles = {"one", "two", "three"};

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FirstFragment() {
        // Required empty public constructor
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
        View view =  inflater.inflate(R.layout.fragment_first, container, false);
        // initView(view);
        return view;
    }

    private void initView(View view) {
        manager = getChildFragmentManager();
        fragments = new ArrayList<>();
        fragments.add(new FirstFragment());
        fragments.add(new ChatFragment());
        fragments.add(new ThirdFragment());
        viewpager = view.findViewById(R.id.view_pager);
        MyAdapter adapter = new MyAdapter(manager, getLifecycle(), fragments);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewpager.setAdapter(adapter);

        // 禁止左右滑动,false为禁止
        viewpager.setUserInputEnabled(true);
        new TabLayoutMediator(tabLayout, viewpager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setCustomView(R.layout.tab_top_item_view);
                TextView tvTitle = (TextView) tab.getCustomView().findViewById(R.id.textView);
                tvTitle.setText(topTitles[position]);
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