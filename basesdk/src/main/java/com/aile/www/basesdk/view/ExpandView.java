/**  
 * @Title: ExpandView.java 
 * @Package com.expand.test 
 * @Description: TODO
 * @author zijianlu
 * @date 2012-5-23 下午3:46:21 
 * @version V1.0  
 */
package com.aile.www.basesdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * @Description: �?个可以展�?和收缩并带动画的自定义View 使用须知�?
 * 1.必须有且仅有两个子布�?，�?�且有顺序要求，第一个是上部的view，第二个是要展开和收缩的view
 * 2.可以在XML文件中指定如下自定义字段�? isExpand 初始的展�?和收缩状�? expandBg
 * 展开状�?�topView的背景资源id unExpandBg 收缩状�?�topView的背景资源id
 * animDurition 展开动画的持续时�? animType
 * 下拉的动画效果，0是减速效果也是默认的�?1是回弹效果，2是线性下�?
 * @author zijianlu
 */
public class ExpandView extends FrameLayout {
    // private static String TAG = "elife.expandview";

    private Scroller mScroller;
    private View mTopView;
    private View mBottomView;
    private boolean isExpand = false;
    private boolean isSizeSet = false;

    private int mTargetHeight = 0;
    private int mCurHeight = 0;
    private int mTopViewHeight = 0;
    private int mBottomViewHeight = 0;

    private int mChildCount = 0;
    private int mAnimDurition = 0;

    /** 资源id */
    private int mExpandResId = 0;
    private int mUnExpandResId = 0;

    private boolean isReSize = false; // 是否每次重新计算高度
    private int mMaxBottomViewHeight = 0;// BottomView的最大高�?

    private OnExpandListener mOnExpandListener;

    public ExpandView(Context context, AttributeSet attrs) {
        super(context, attrs);
        isExpand = attrs.getAttributeBooleanValue(null, "isExpand", false);
        mExpandResId = attrs.getAttributeResourceValue(null, "expandBg", 0);
        mUnExpandResId = attrs.getAttributeResourceValue(null, "unExpandBg", 0);
        mAnimDurition = attrs.getAttributeIntValue(null, "animDurition", 300);
        int animType = attrs.getAttributeIntValue(null, "animType", 0);

        Interpolator interpolator = null;
        switch (animType) {
        case 0:
            interpolator = new DecelerateInterpolator();
            break;

        case 1:
            interpolator = new BounceInterpolator();
            break;

        case 2:
            interpolator = new LinearInterpolator();
            break;

        default:
            interpolator = new DecelerateInterpolator();
            break;
        }

        mScroller = new Scroller(context, interpolator);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mChildCount = getChildCount();
        if (mChildCount != 2) {
            throw new RuntimeException("should have two child views~~");
        }
        mTopView = getChildAt(0);
        mBottomView = getChildAt(1);

        mTopView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean expandState = !isExpand;
                setExpand(expandState, true, false);
            }
        });
    }

    /** 设置topView展开和收缩状态的背景 */
    public void setTopViewBackGround(int expandResId, int unExpandResId) {
        if (expandResId > 0) {
            mExpandResId = expandResId;
        }
        if (unExpandResId > 0) {
            mUnExpandResId = unExpandResId;
        }
    }

    public View getTopView() {
        return mTopView;
    }

    public View getBottomView() {
        return mBottomView;
    }

    /** 当前的收缩状�? */
    public boolean isExpanded() {
        return isExpand;
    }

    /** 设置展开或�?�收�? */
    public void setExpand(boolean isExpand) {
        setExpand(isExpand, false, false);
    }

    public void setResize() {
        isReSize = true;
        requestLayout();
    }

    public void setMaxBottomViewHeight(int maxHeight) {
        mMaxBottomViewHeight = maxHeight;
    }

    /** 设置展开或�?�收�?, notifyExpand表示是否触发回调函数 */
    public void setExpand(boolean expandState, boolean notifyExpand,
        boolean animate) {
        if (mScroller != null) {
            mScroller.forceFinished(true);
        }

        if (expandState && !this.isExpand) {
            if (animate) {
                mCurHeight = mTopViewHeight;
                mTargetHeight = mBottomViewHeight + mTopViewHeight;
                mScroller.startScroll(0, mCurHeight, 0, mBottomViewHeight,
                    mAnimDurition);
                invalidate();
            } else {
                mCurHeight = mBottomViewHeight + mTopViewHeight;
                requestLayout();
            }

            if (mExpandResId > 0) {
                mTopView.setBackgroundResource(mExpandResId);
            }
        } else if (!expandState && this.isExpand) {
            if (animate) {
                mCurHeight = mBottomViewHeight + mTopViewHeight;
                mTargetHeight = mTopViewHeight;
                mScroller.startScroll(0, mCurHeight, 0, -mBottomViewHeight,
                    mAnimDurition);
                invalidate();
            } else {
                mCurHeight = mTopViewHeight;
                requestLayout();
                if (mUnExpandResId > 0) {
                    mTopView.setBackgroundResource(mUnExpandResId);
                }
            }
        }

        this.isExpand = expandState;

        if (mOnExpandListener != null && notifyExpand) {
            mOnExpandListener.onExpand(this, isExpand);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mCurHeight = mScroller.getCurrY();
            requestLayout();
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        if (!isSizeSet) {
            computerSize();
            isSizeSet = true;
        }

        if (isReSize) {
            computerSize();
            isReSize = false;
        }

        if (!isExpand && mCurHeight == mTargetHeight && mUnExpandResId > 0) {
            mTopView.setBackgroundResource(mUnExpandResId);
        }

        setMeasuredDimension(width, mCurHeight);
    }

    public void computerSize() {
        mBottomViewHeight = mBottomView.getMeasuredHeight();
        if (mMaxBottomViewHeight > 0
            && mBottomViewHeight > mMaxBottomViewHeight) {
            mBottomViewHeight = mMaxBottomViewHeight;
        }

        mTopViewHeight = mTopView.getMeasuredHeight();

        if (isExpand) {
            mCurHeight = mTopViewHeight + mBottomViewHeight;
            mTargetHeight = mTopViewHeight + mBottomViewHeight;

            if (mExpandResId > 0) {
                mTopView.setBackgroundResource(mExpandResId);
            }
        } else {
            mCurHeight = mTopViewHeight;
            mTargetHeight = mTopViewHeight;
            if (mUnExpandResId > 0) {
                mTopView.setBackgroundResource(mUnExpandResId);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mTopView.layout(0, 0, getMeasuredWidth(), mTopViewHeight);
        mBottomView.layout(0, mTopViewHeight, getMeasuredWidth(),
            mTopViewHeight + mBottomViewHeight);
    }

    /** 设置底部view点击事件处理 */
    public void setOnBottomViewClickListener(View.OnClickListener listener) {
        if (mBottomView != null) {
            mBottomView.setOnClickListener(listener);
        }
    }

    /** 设置展开和收缩的事件回调 */
    public void setOnExpandListener(OnExpandListener onExpandListener) {
        mOnExpandListener = onExpandListener;
    }

    /** ExpandView展开和收缩的通知接口 */
    public interface OnExpandListener {
        public void onExpand(View v, boolean isExpanded);
    }

}
