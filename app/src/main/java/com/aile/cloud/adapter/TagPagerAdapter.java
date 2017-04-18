package com.aile.cloud.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.aile.cloud.fragment.HomeFragment;
import com.aile.cloud.net.bean.MTProduct;

import java.util.List;

/**
 * Created by yeyi on 17/4/18.
 */

public class TagPagerAdapter extends FragmentStatePagerAdapter {
    private SparseArray<List<MTProduct>> data;
    private List<String> titles;

    public TagPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(SparseArray<List<MTProduct>> data, List<String> titles) {
        if (null == data) {
            return;
        }
        this.data = data;
        this.titles = titles;
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        try {
            title = titles.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return title;
    }

    @Override
    public Fragment getItem(int position) {
        return HomeFragment.newInstance(data.get(position));
    }

    @Override
    public int getCount() {
        if (null == data) {
            return 0;
        }

        return data.size();
    }
}