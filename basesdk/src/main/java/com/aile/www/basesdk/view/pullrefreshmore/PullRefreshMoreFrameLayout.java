package com.aile.www.basesdk.view.pullrefreshmore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * 封装一层，并简化加载更多的使用
 * Created by RP on 2016/4/5.
 */
public class PullRefreshMoreFrameLayout extends PtrFrameLayout {

    /**
     * 封装一层Handler,以便以后替换或重构
     */
    public interface PullRefreshHandler {

        void onRefreshBegin(PullRefreshMoreFrameLayout pullFrame);

        /**
         * 仅在使用了加载更多组件时，才会触发此方法
         * @param pullFrame
         */
        void onLoadMore(PullRefreshMoreFrameLayout pullFrame);

    }

    private PullRefreshHandler mPullRefreshHandler;
    private LoadMoreContainerBase mLoadMoreContainerBase;
    private AbsListView mAbsListView;

    public PullRefreshMoreFrameLayout(Context context) {
        super(context);
        init(null);
    }

    public PullRefreshMoreFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PullRefreshMoreFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // the following are default settings
        disableWhenHorizontalMove(true);
        setResistance(1.7f);
        setRatioOfHeaderHeightToRefresh(1.2f);
        setDurationToClose(200);
        setDurationToCloseHeader(1000);
        // default is false
        setPullToRefresh(false);
        // default is true
        setKeepHeaderWhenRefresh(true);
        setPtrHandler(new PtrHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (mAbsListView != null) {
                    content = mAbsListView;
                }
                return PtrDefaultHandler.checkContentCanBePulledDown(PullRefreshMoreFrameLayout.this, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (mPullRefreshHandler != null) {
                    mPullRefreshHandler.onRefreshBegin(PullRefreshMoreFrameLayout.this);
                }
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for(int i=0;i<getChildCount();i++){
            View v = getChildAt(i);
            if (v instanceof LoadMoreContainerBase) {
                mLoadMoreContainerBase = (LoadMoreContainerBase) v;
                mAbsListView = mLoadMoreContainerBase.retrieveAbsListView();
                mLoadMoreContainerBase.useDefaultFooter();
                mLoadMoreContainerBase.setLoadMoreHandler(new LoadMoreHandler() {
                    @Override
                    public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                        if (mPullRefreshHandler != null) {
                            mPullRefreshHandler.onLoadMore(PullRefreshMoreFrameLayout.this);
                        }
                    }
                });
                return;
            }
        }
    }

    public void setPullRefreshHandler(PullRefreshHandler pullRefreshHandler) {
        this.mPullRefreshHandler = pullRefreshHandler;
    }

    public LoadMoreContainer getLoadMoreContainer() {
        return mLoadMoreContainerBase;
    }

    public void loadMoreFinish(boolean empty,boolean hasMore){
        if(mLoadMoreContainerBase != null){
            mLoadMoreContainerBase.loadMoreFinish(empty,hasMore);
        }
    }

    public void loadMoreError(int errorCode, String errorMessage){
        if(mLoadMoreContainerBase != null){
            mLoadMoreContainerBase.loadMoreError(errorCode, errorMessage);
        }
    }
}
