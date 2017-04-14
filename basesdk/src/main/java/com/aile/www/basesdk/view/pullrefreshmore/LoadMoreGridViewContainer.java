package com.aile.www.basesdk.view.pullrefreshmore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

public class LoadMoreGridViewContainer extends LoadMoreContainerBase {

    private GridViewWithHeaderAndFooter mListView;

    public LoadMoreGridViewContainer(Context context) {
        super(context);
    }

    public LoadMoreGridViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addFooterView(View view) {
        mListView.addFooterView(view);
    }

    @Override
    public void removeFooterView(View view) {
        mListView.removeFooterView(view);
    }

    @Override
    public AbsListView retrieveAbsListView() {
        mListView = (GridViewWithHeaderAndFooter) getChildAt(0);
        return mListView;
    }
}