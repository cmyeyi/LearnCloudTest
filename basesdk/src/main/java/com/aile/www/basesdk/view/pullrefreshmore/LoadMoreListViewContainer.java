package com.aile.www.basesdk.view.pullrefreshmore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class LoadMoreListViewContainer extends LoadMoreContainerBase {

    private ListView mListView;

    public LoadMoreListViewContainer(Context context) {
        super(context);
    }

    public LoadMoreListViewContainer(Context context, AttributeSet attrs) {
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
        mListView = (ListView) getChildAt(0);
        return mListView;
    }
}