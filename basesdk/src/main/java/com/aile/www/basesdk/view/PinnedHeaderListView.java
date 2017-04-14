package com.aile.www.basesdk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 带有固定可改变标题的列表
 * @author tongyiguo
 */
public class PinnedHeaderListView extends ListView {

    /**
     * adapter接口，使用这个list的adapter必须实现这个接口
     */
    public interface PinnedHeaderAdapter {

        /**
         * 顶部栏状态：不显示
         */
        public static final int PINNED_HEADER_GONE = 0;

        /**
         * 顶部栏状态：显示
         */
        public static final int PINNED_HEADER_VISIBLE = 1;

        /**
         * 顶部栏状态：可以顶起
         */
        public static final int PINNED_HEADER_PUSHED_UP = 2;

        /**
         * 根据这一行在列表中的位置来确定这行对顶部栏的状态，只能是下面三个状态之一
         * {@link #PINNED_HEADER_GONE}, {@link #PINNED_HEADER_VISIBLE} 或者
         * {@link #PINNED_HEADER_PUSHED_UP}.
         */
        int getPinnedHeaderState(int position);

        /**
         * 设置顶部栏的布局以及根据position确定内容
         * @param header 顶部栏视图
         * @param position 当前可见部分第一个的position
         * @param alpha 顶部栏alpha值，取值范围0-255.
         */
        void configurePinnedHeader(View header, int position, int alpha);
    }
    

    
    /** 
     * alpha 最大值
     */
    private static final int MAX_ALPHA = 255;
    
    /** 
     * 该list的adapter
     */
    private PinnedHeaderAdapter mAdapter;
    
    /** 
     * 顶部栏view
     */
    private View mHeaderView;
    
    /**
     * 顶部栏可见属性
     */
    private boolean mHeaderViewVisible;

    /**
     * 顶部栏宽度
     */
    private int mHeaderViewWidth;

    /**
     * 顶部栏高度
     */
    private int mHeaderViewHeight;

    /**
     * 构造函数
     * @param context 上下文
     */
    public PinnedHeaderListView(Context context) {
        super(context);
    }

    /**
     * 构造函数
     * @param context 上下文
     * @param attrs 参数
     */
    public PinnedHeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 构造函数
     * @param context 上下文
     * @param attrs 参数
     * @param defStyle 默认风格
     */
    public PinnedHeaderListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置顶部栏视图
     * @param view 视图
     */
    public void setPinnedHeaderView(View view) {
        mHeaderView = view;

        // Disable vertical fading when the pinned header is present
        // TODO change ListView to allow separate measures for top and bottom fading edge;
        // in this particular case we would like to disable the top, but not the bottom edge.
        if (mHeaderView != null) {
            setFadingEdgeLength(0);
        }
        requestLayout();
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = (PinnedHeaderAdapter)adapter;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderViewWidth = mHeaderView.getMeasuredWidth();
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mHeaderView != null) {
            mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
            configureHeaderView(getFirstVisiblePosition());
        }
    }

    /**
     * 根据当前列表位置来确定顶部栏状态从而设置当前顶部栏的内容以及可见性
     * @param position 列表位置
     */
    public void configureHeaderView(int position) {
        if (mHeaderView == null) {
            return;
        }
        
        if (position >= getCount()) {
            return;
        }
        
        position -= getHeaderViewsCount();
        position -= getFooterViewsCount();
        
        int state = mAdapter.getPinnedHeaderState(position);
        switch (state) {
            case PinnedHeaderAdapter.PINNED_HEADER_GONE: {
                mHeaderViewVisible = false;
                break;
            }

            case PinnedHeaderAdapter.PINNED_HEADER_VISIBLE: {
                mAdapter.configurePinnedHeader(mHeaderView, position, MAX_ALPHA);
                if (mHeaderView.getTop() != 0) {
                    mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
                }
                mHeaderViewVisible = true;
                break;
            }

            case PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP: {
                View firstView = getChildAt(0);
                int bottom = firstView.getBottom();
                int headerHeight = mHeaderView.getHeight();
                int y;
                int alpha;
                if (bottom < headerHeight) {
                    y = (bottom - headerHeight);
                    alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
                } else {
                    y = 0;
                    alpha = MAX_ALPHA;
                }
                mAdapter.configurePinnedHeader(mHeaderView, position, alpha);
                if (mHeaderView.getTop() != y) {
                    mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
                }
                mHeaderViewVisible = true;
                break;
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderViewVisible) {
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }
    
}
