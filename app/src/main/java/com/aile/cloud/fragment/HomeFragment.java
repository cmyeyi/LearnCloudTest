package com.aile.cloud.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.aile.cloud.adapter.HomeProductAdapter;
import com.aile.cloud.net.bean.HomeProduct;

import java.io.Serializable;
import java.util.List;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String KEY_FOR_DATA = "data";

    private List<HomeProduct.Product> products;

    public static HomeFragment newInstance(List<HomeProduct.Product> items) {
        if (null == items) {
            throw new IllegalArgumentException("Any param can not be null");
        }
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_FOR_DATA, (Serializable) items);
        fragment.setArguments(bundle);
        return fragment;
    }

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (null == bundle) {
            return;
        }
        if (!bundle.containsKey(KEY_FOR_DATA)) {
            return;
        }

        try {
            products = (List<HomeProduct.Product>) bundle.getSerializable(KEY_FOR_DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GridView gridView = new GridView(getActivity());
        ViewGroup.LayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        gridView.setLayoutParams(params);
        gridView.setNumColumns(3);
        gridView.setHorizontalSpacing(1);
        gridView.setVerticalSpacing(1);
        gridView.setOnItemClickListener(this);
        HomeProductAdapter adapter = new HomeProductAdapter(getActivity(), products);
        gridView.setAdapter(adapter);
        return gridView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
