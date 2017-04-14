package com.aile.www.basesdk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SimpleViewPagerIndicator extends LinearLayout {

    private static final int COLOR_TEXT_NORMAL = 0xFF000000;
    private static final int COLOR_INDICATOR_COLOR = Color.GREEN;

    protected CharSequence[] mTitles;
    protected TextView[] tvTitles;
    private int mTabCount = 1;
    private int mIndicatorColor = COLOR_INDICATOR_COLOR;
    private int mTextColor = COLOR_TEXT_NORMAL;
    private int mTextColorSelected = COLOR_TEXT_NORMAL;
    private float mTranslationX;
    private Paint mPaint = new Paint();
    private int mTabWidth;
    private int mTexTSizeSP = 16;
    private int mGravityType = Gravity.CENTER;
    private ViewPager mViewPager;
    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = indexOfChild(v);
            if (mViewPager != null) {
                mViewPager.setCurrentItem(i);
            }
        }
    };

    public SimpleViewPagerIndicator(Context context) {
        this(context, null);
    }

    public SimpleViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setColor(mIndicatorColor);
        mPaint.setStrokeWidth(9.0F);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTabWidth = w / mTabCount;
    }

    public void setTitles(CharSequence[] titles) {
        mTitles = titles;
        mTabCount = titles.length;
        mTabWidth = getMeasuredWidth() / mTabCount;
        generateTitleView();
    }

    public void setIndicatorColorAndHeight(int indicatorColor, float indicatorHeight) {
        this.mIndicatorColor = indicatorColor;
        mPaint.setColor(indicatorColor);
        mPaint.setStrokeWidth(indicatorHeight);
    }

    public void setTexTSizeSP(int texTSizeSP) {
        mTexTSizeSP = texTSizeSP;
    }

    public void setTextColor(int normalColor, int selectedColor) {
        mTextColor = normalColor;
        mTextColorSelected = selectedColor;
    }

    public void setTextColor(int color) {
        mTextColor = color;
        mTextColorSelected = color;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        canvas.translate(mTranslationX, getHeight() - mPaint.getStrokeWidth() / 2);
        canvas.drawLine(0, 0, mTabWidth, 0, mPaint);
        canvas.restore();
    }

    public void scroll(int position, float offset) {
        /**
         * <pre>
         *  0-1:position=0 ;1-0:postion=0;
         * </pre>
         */
        mTranslationX = getWidth() / mTabCount * (position + offset);
        invalidate();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 设置文字的显示方式
     *
     * @param gravity
     */
    public void setGravityType(int gravity) {
        this.mGravityType = gravity;
    }

    private void generateTitleView() {
        if (getChildCount() > 0) {
            this.removeAllViews();
        }
        int count = mTitles.length;

        setWeightSum(count);
        for (int i = 0; i < count; i++) {
            View v = getView(i);
            LayoutParams lp = new LayoutParams(0,
                    LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            v.setLayoutParams(lp);
            v.setOnClickListener(mOnClickListener);
            addView(v);
            tvTitles[i] = (TextView) v;
        }
    }


    public View getView(int index) {
        TextView tv = new TextView(getContext());
//        LayoutParams lp = new LayoutParams(0,
//                LayoutParams.MATCH_PARENT);
//        lp.weight = 1;
        tv.setGravity(mGravityType);
        tv.setTextColor(mTextColor);
        tv.setText(mTitles[index]);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTexTSizeSP);
//        tv.setLayoutParams(lp);
        return tv;
    }

    public void setTvTitleColor(int position) {
        for (int i = 0; i < tvTitles.length; i++) {
            if (i == position) {
                tvTitles[i].setTextColor(mTextColorSelected);
            } else {
                tvTitles[i].setTextColor(mTextColor);
            }
        }
    }

    public void setViewPager(ViewPager viewPager) {
        if (mViewPager == viewPager) {
            return;
        }

        mViewPager = viewPager;
        if (viewPager != null && viewPager.getAdapter() != null) {
            CharSequence titles[] = new CharSequence[viewPager.getAdapter().getCount()];
            tvTitles = new TextView[viewPager.getAdapter().getCount()];
            for (int i = 0; i < titles.length; i++) {
                titles[i] = viewPager.getAdapter().getPageTitle(i);
            }
            setTitles(titles);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    scroll(position, positionOffset);
                }

                @Override
                public void onPageSelected(int position) {
                    setTvTitleColor(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            throw new IllegalStateException("ViewPager must have a adapter to call this method!!!");
        }
    }

}
