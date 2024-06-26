package com.shangyizhou.develop.base;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class FragmentManagerHelper {
    // 管理类FragmentManager
    private FragmentManager mFragmentManager;
    // 容器布局id containerViewId
    private int mContainerViewId;
    private List<Fragment> fragments;

    /**
     * 构造函数
     *
     * @param fragmentManager 管理类FragmentManager
     * @param containerViewId 容器布局id containerViewId
     */
    public FragmentManagerHelper(@Nullable FragmentManager fragmentManager, @IdRes int containerViewId) {
        this.mFragmentManager = fragmentManager;
        this.mContainerViewId = containerViewId;
    }

    /**
     * 添加Fragment
     */
    public void add(Fragment fragment) {
        // 开启事物
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        // 第一个参数是Fragment的容器id，需要添加的Fragment
        fragmentTransaction.add(mContainerViewId, fragment);
        // 一定要commit
        fragmentTransaction.commit();
    }

    /**
     * 切换显示Fragment
     */
    public void switchFragment(Fragment fragment) {
        // 开启事物
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        // 1.先隐藏当前所有的Fragment
        List<Fragment> childFragments = mFragmentManager.getFragments();
        for (Fragment childFragment : childFragments) {
            fragmentTransaction.hide(childFragment);
        }

        // 2.如果容器里面没有我们就添加，否则显示
        if (!childFragments.contains(fragment)) {
            fragmentTransaction.add(mContainerViewId, fragment);
        } else {
            fragmentTransaction.show(fragment);
        }

        // 替换Fragment
        // fragmentTransaction.replace(R.id.main_tab_fl,mHomeFragment);
        // 一定要commit
        fragmentTransaction.commit();
    }

    public void hideAll() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
    }
}
