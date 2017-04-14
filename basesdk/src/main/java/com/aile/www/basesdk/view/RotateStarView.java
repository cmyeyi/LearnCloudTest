package com.aile.www.basesdk.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

/**
 * @Description: 旋转弹出的菜单控件使用须知
 * 1.必须有多于3个子view，并且有顺序要求，第一个是固定不动的子view，其余的是要弹出的子view
 * 2.可以在XML文件中指定如下自定义字段：isExpand 初始的展开和收缩状态, 默认是收缩的
 * @author zijianlu
 * @date 2012-6-5
 */
public class RotateStarView extends FrameLayout {
    private int mRadus = 0;
    private int mStarCount = 0;
    private double mAngle;

    private View mRootStarView;
    private int mRootStarSize = 0;

    private boolean isSizeInited = false;
    private boolean isExpanded = false;

    public RotateStarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        isExpanded = attrs.getAttributeBooleanValue(null, "isExpand", false);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mStarCount = getChildCount() - 1;
        if (mStarCount < 2) {
            throw new RuntimeException("must have 2 child star view or more~~");
        }

        mAngle = Math.PI * 0.5 / (mStarCount + 1);
        mRootStarView = getChildAt(0);
        mRootStarView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isExpanded) {
                    unExpandStars();
                } else {
                    expandStars();
                }

                invalidate();
                requestLayout();
            }
        });
        mRootStarView.setVisibility(View.VISIBLE);

        if (isExpanded) {
            for (int i = 1; i <= mStarCount; i++) {
                getChildAt(i).setVisibility(View.VISIBLE);
            }
        } else {
            for (int i = 1; i <= mStarCount; i++) {
                getChildAt(i).setVisibility(View.GONE);
            }
        }

    }

    /** 展开 */
    public void expandStars() {
        if (!isExpanded) {
            for (int i = 1; i <= mStarCount; i++) {
                final View child = getChildAt(i);
                int w = child.getMeasuredWidth();
                int left = child.getLeft();
                int top = child.getTop();

                int durition = i * 120;

                AnimationSet animSet = new AnimationSet(false);

                TranslateAnimation transAnim = new TranslateAnimation(-left, 0,
                    (mRadus - top - w), 0);
                transAnim.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        child.setVisibility(View.VISIBLE);
                    }
                });
                transAnim.setDuration(durition);
                transAnim.setInterpolator(new OvershootInterpolator());

                AlphaAnimation alphaAnim = new AlphaAnimation(0.2f, 1.0f);
                alphaAnim.setDuration(100);

                animSet.addAnimation(transAnim);
                animSet.addAnimation(alphaAnim);

                child.setVisibility(View.INVISIBLE);
                child.startAnimation(animSet);
            }

            isExpanded = !isExpanded;
            if (mOnStarClickListener != null) {
                mOnStarClickListener.onRootStarClick(mRootStarView, isExpanded);
            }
        }
    }

    /** 收拢 */
    public void unExpandStars() {
        if (isExpanded) {
            for (int i = 1; i <= mStarCount; i++) {
                final View child = getChildAt(i);
                int w = child.getMeasuredWidth();
                int left = child.getLeft();
                int top = child.getTop();

                int durition = i * 80;
                AnimationSet animSet = new AnimationSet(false);

                AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.5f);
                alphaAnim.setDuration(durition);

                RotateAnimation rotateAnim = new RotateAnimation(0, 360, w / 2,
                    -w / 2);
                rotateAnim.setDuration(durition);

                TranslateAnimation transAnim = new TranslateAnimation(0,
                    -(left - (mRootStarSize - w) / 2), 0, mRadus - top - w
                        - (mRootStarSize - w) / 2);
                transAnim.setDuration(durition);
                transAnim.setInterpolator(new DecelerateInterpolator());
                transAnim.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        child.setVisibility(View.GONE);
                    }
                });

                animSet.addAnimation(alphaAnim);
                // animSet.addAnimation(rotateAnim);
                animSet.addAnimation(transAnim);
                // animSet.setFillAfter(true);

                child.setVisibility(View.INVISIBLE);
                child.startAnimation(animSet);
            }

            isExpanded = !isExpanded;
            if (mOnStarClickListener != null) {
                mOnStarClickListener.onRootStarClick(mRootStarView, isExpanded);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        mRadus = w < h ? w : h;
        setMeasuredDimension(mRadus, mRadus);

        if (!isSizeInited) {
            mRootStarSize = (mRootStarView.getMeasuredHeight() + mRootStarView
                .getMeasuredWidth()) / 2;
            isSizeInited = true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
        int bottom) {
        mRootStarView.layout(0, mRadus - mRootStarSize, mRootStarSize, mRadus);

        for (int i = 1; i <= mStarCount; i++) {
            double angle = mAngle * i;
            View view = getChildAt(i);
            int size = view.getMeasuredWidth();
            int radus = mRadus - size;
            int vLeft = (int) (Math.cos(angle) * radus) - size;
            int vTop = mRadus - (int) (Math.sin(angle) * radus);
            view.layout(vLeft, vTop, vLeft + size, vTop + size);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDector.onTouchEvent(event);
    }

    /** 手势处理 */
    private GestureDetector mGestureDector = new GestureDetector(
        new OnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (isExpanded) {
                    boolean isChildStarClick = false;
                    for (int i = 1; i <= mStarCount; i++) {
                        View v = getChildAt(i);

                        if (isEventValid(e, v)) {
                            AnimationSet animSet = new AnimationSet(false);

                            float pivot = v.getWidth() / 2.0f;

                            ScaleAnimation scaleAnim = new ScaleAnimation(1.0f,
                                2f, 1.0f, 2f, pivot, pivot);
                            scaleAnim.setDuration(300);

                            RotateAnimation rotateAnim = new RotateAnimation(0,
                                720, pivot, pivot);
                            rotateAnim.setDuration(500);

                            AlphaAnimation alphaAnim = new AlphaAnimation(1.0f,
                                0.4f);
                            alphaAnim.setDuration(500);

                            animSet.addAnimation(scaleAnim);
                            animSet.addAnimation(rotateAnim);
                            animSet.addAnimation(alphaAnim);
                            v.startAnimation(animSet);

                            if (mOnStarClickListener != null) {
                                mOnStarClickListener.onChildStarClick(v, i - 1);
                            }

                            isChildStarClick = true;
                            break;
                        }
                    }
                    if (!isChildStarClick) {
                        unExpandStars();
                    }
                }
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY) {

                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {

                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {

                if (isExpanded) {
                    for (int i = 1; i <= mStarCount; i++) {
                        View v = getChildAt(i);
                        if (isEventValid(e, v)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });

    /** 事件处理 */
    private boolean isEventValid(MotionEvent e, View v) {
        int w = v.getWidth() / 2;
        int h = v.getHeight() / 2;
        Rect rect = new Rect();
        v.getHitRect(rect);
        rect.left -= w;
        rect.right += w;
        rect.top -= h;
        rect.bottom += h;
        return rect.contains((int) e.getX(), (int) e.getY());
    }

    private OnStarClickListener mOnStarClickListener = null;

    /** 设置事件响应监听 */
    public void setOnStarClickListener(OnStarClickListener listener) {
        mOnStarClickListener = listener;
    }

    public interface OnStarClickListener {
        /** 周围的星星点击响应*/
        public void onChildStarClick(View v, int position);

        /** 主星星点击响应 */
        public void onRootStarClick(View v, boolean isExpanded);
    }
}