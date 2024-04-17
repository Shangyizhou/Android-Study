package com.example.androidnote.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.example.androidnote.R;
import com.example.androidnote.model.Session;
import com.google.android.material.tabs.TabLayout;
import com.shangyizhou.develop.log.SLog;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatStartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatStartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatStartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatStartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatStartFragment newInstance(String param1, String param2) {
        ChatStartFragment fragment = new ChatStartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private static final String TAG = "ChatStartFragment";
    TabLayout tabLayout;
    ViewPager viewPager;
    private List<String> titleList;
    private List<Fragment> fragmentList;

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
        View view = inflater.inflate(R.layout.fragment_chat_start, container, false);
        initView(view);
        getData();
        return view;
    }

    private void initView(View view) {
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        titleList.add("聊天");
        titleList.add("历史");
        // 设置标题
        for (int i = 0; i < titleList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titleList.get(i)));
        }

        fragmentList.add(new ChatFragment());
        fragmentList.add(new HistoryFragment(new HistoryFragment.onItemViewClickListener() {
            @Override
            public void onItemClick(Session session) {
                SLog.i(TAG, "onItemClick: ChatFragment reload");
                ChatFragment chatFragment = (ChatFragment) fragmentList.get(0);
                chatFragment.reload(session);
                viewPager.setCurrentItem(0);
            }
        }));

        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        //给viewPager设置adapter
        viewPager.setAdapter(adapter);
        //tabLayout与viewPager绑定
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(1);
    }

    private void getData() {
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