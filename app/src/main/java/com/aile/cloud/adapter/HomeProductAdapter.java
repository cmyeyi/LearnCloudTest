package com.aile.cloud.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.aile.cloud.net.bean.HomeProduct;
import com.aile.www.basesdk.utils.DimensionUtils;
import com.aile.www.basesdk.utils.ScreenUtils;

import java.util.List;


public class HomeProductAdapter extends BaseAdapter {
    private List<HomeProduct.Product> data;
    private Activity activity;
    private int length;


    public HomeProductAdapter(Activity activity, List<HomeProduct.Product> items) {
        this.activity = activity;
        data = items;
        int width = ScreenUtils.getScreenW(activity);
        int gap = DimensionUtils.dpToPx(activity, 1);
        length = (width - 2*gap) / 3;
    }

    @Override
    public int getCount() {
        if (null == data) {
            return 0;
        }

        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            ImageView imageView = new ImageView(activity);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(length, length));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            convertView = imageView;
        }
        String url = null;
        try {
            url = data.get(position).getIcon_url();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
