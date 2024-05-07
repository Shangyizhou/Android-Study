package com.example.androidnote.fragment.square;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidnote.R;
import com.example.androidnote.activity.SearchActivity;
import com.example.androidnote.fragment.SquareFragment;
import com.example.androidnote.fragment.chat.ChatStartFragment;
import com.example.androidnote.fragment.chat.HistoryFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SquareStartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SquareStartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SquareStartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SquareStartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SquareStartFragment newInstance(String param1, String param2) {
        SquareStartFragment fragment = new SquareStartFragment();
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
        View view = inflater.inflate(R.layout.fragment_square_start, container, false);
        initView(view);
        getData();
        return view;
    }

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView search;
    private List<String> titleList;
    private List<Fragment> fragmentList;
    private void initView(View view) {
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        search = view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.startUp(getActivity());
            }
        });
    }

    private void getData() {
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        titleList.add("通用");
        titleList.add("就业");
        titleList.add("语言");
        titleList.add("考研");
        titleList.add("代码");
        titleList.add("安卓");
        titleList.add("后端");
        titleList.add("前端");
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        // 设置标题
        /**
         * 记得要先设置默认的 app:tabTextAppearance="@style/TabLayoutTextStyle"
         */
        for (int i = 0; i < titleList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titleList.get(i)));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView == null) {
                    tab.setCustomView(R.layout.tab_text_layout);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextAppearance(getActivity(), R.style.TabLayoutTextSelected);
                // 选中更新Data
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView == null) {
                    tab.setCustomView(R.layout.tab_text_layout);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextAppearance(getActivity(), R.style.TabLayoutTextUnSelected);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fragmentList.add(new SquareFragment());
        fragmentList.add(new SquareFragment());
        fragmentList.add(new SquareFragment());
        fragmentList.add(new SquareFragment());
        fragmentList.add(new SquareFragment());
        fragmentList.add(new SquareFragment());
        fragmentList.add(new SquareFragment());
        fragmentList.add(new SquareFragment());

        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        //给viewPager设置adapter
        viewPager.setAdapter(adapter);
        //tabLayout与viewPager绑定
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(1);
    }

    //ViewPager适配器
    class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getCount() {
            return fragmentList.size();//页卡数
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);//页卡标题
        }
    }
}