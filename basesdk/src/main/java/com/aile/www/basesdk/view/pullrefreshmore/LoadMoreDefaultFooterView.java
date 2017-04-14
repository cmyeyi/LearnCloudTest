package com.aile.www.basesdk.view.pullrefreshmore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aile.www.basesdk.R;
import com.aile.www.basesdk.view.materialprogressview.MaterialProgressView;

public class LoadMoreDefaultFooterView extends RelativeLayout implements LoadMoreUIHandler {

    private TextView mTextView;
    private MaterialProgressView mMaterialProgressView;

    public LoadMoreDefaultFooterView(Context context) {
        this(context, null);
    }

    public LoadMoreDefaultFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreDefaultFooterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupViews();
    }

    private void setupViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.load_more_footer_default,this,true);
        mTextView = (TextView) findViewById(R.id.load_more_info_tv);
        mMaterialProgressView = (MaterialProgressView) findViewById(R.id.load_more_pg);
        mTextView.setVisibility(GONE);
    }

    @Override
    public void onLoading(LoadMoreContainer container) {
        setVisibility(VISIBLE);
        //mTextView.setText(R.string.default_load_more_loading);
        mMaterialProgressView.setVisibility(VISIBLE);
        mTextView.setVisibility(GONE);
    }

    @Override
    public void onLoadFinish(LoadMoreContainer container, boolean empty, boolean hasMore) {
        if (!hasMore) {
            setVisibility(VISIBLE);
            mMaterialProgressView.setVisibility(GONE);
            mTextView.setVisibility(VISIBLE);
            if (empty) {
                mTextView.setText(R.string.default_load_more_loaded_empty);
            } else {
                mTextView.setText(R.string.default_load_more_loaded_no_more);
            }
        } else {
            setVisibility(INVISIBLE);
            mMaterialProgressView.setVisibility(GONE);
        }
    }

    @Override
    public void onWaitToLoadMore(LoadMoreContainer container) {
        setVisibility(VISIBLE);
        mTextView.setText(R.string.default_load_more_click_to_load_more);
    }

    @Override
    public void onLoadError(LoadMoreContainer container, int errorCode, String errorMessage) {
        mTextView.setText(R.string.default_load_more_error);
    }
}