package com.aile.cloud.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.aile.cloud.R;
import com.aile.cloud.adapter.HomeProductAdapter;
import com.aile.cloud.net.bean.MTProduct;
import com.aile.cloud.view.RecycleViewDecoration;

import java.io.Serializable;
import java.util.List;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String KEY_FOR_DATA = "data";

    private List<MTProduct> products;

    public static HomeFragment newInstance(List<MTProduct> items) {
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
            products = (List<MTProduct>) bundle.getSerializable(KEY_FOR_DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.layout_recycler_view, null);
        RecyclerView recyclerView = (RecyclerView) viewRoot.findViewById(R.id.home_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecycleViewDecoration());//设置分割线
        HomeProductAdapter adapter = new HomeProductAdapter(getActivity(), products);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
