package com.aile.www.basesdk.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * fragment切换控制器, 初始化时直接add全部fragment, 然后利用show和hide进行切换控制
 */
public class FragmentController {

    private int mContainerId;
    private FragmentManager mFragmentManager;
    private ArrayList<Fragment> mFragments;

    public FragmentController(AppCompatActivity activity, int containerId, ArrayList<Fragment> fragments) {
        this.mContainerId = containerId;
        this.mFragments = fragments;
        this.mFragmentManager = activity.getSupportFragmentManager();
        initFragment();
    }

    public void initFragment() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        for (int i = 0; i < mFragments.size(); i++) {
            ft.add(mContainerId, mFragments.get(i), String.valueOf(i));
        }
        ft.commit();
    }

    public void showFragment(int position) {
        hideFragments();
        Fragment fragment = mFragments.get(position);
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.show(fragment);
        ft.commit();
    }

    public void hideFragments() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        for (Fragment fragment : mFragments) {
            if (fragment != null) {
                ft.hide(fragment);
            }
        }
        ft.commit();
    }

    public Fragment getFragment(int position) {
        return mFragments.get(position);
    }

}