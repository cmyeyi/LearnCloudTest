package com.aile.www.basesdk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.aile.www.basesdk.R;

/**
 * 圆角的FrameLayout
 */
public class RoundCornerRelativeLayout extends RelativeLayout {

    private float mLeftTopRoundRadius = -1.0f;
    private float mRightTopRoundRadius = -1.0f;
    private float mLeftBottomRoundRadius = -1.0f;
    private float mRightBottomRoundRadius = -1.0f;
    private int mBgColor = Color.WHITE;
    private Path mClipPath;
    private RectF mWHRectF = new RectF();
    private Paint mPaint;
    private DrawFilter mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG);

    public RoundCornerRelativeLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public RoundCornerRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RoundCornerRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RoundCornerRelativeLayout, defStyle, 0);
            mLeftTopRoundRadius = a.getDimension(R.styleable.RoundCornerRelativeLayout_roundCornerRadius, mLeftTopRoundRadius);
            mRightTopRoundRadius = a.getDimension(R.styleable.RoundCornerRelativeLayout_roundCornerRadius, mRightTopRoundRadius);
            mLeftBottomRoundRadius = a.getDimension(R.styleable.RoundCornerRelativeLayout_roundCornerRadius, mLeftBottomRoundRadius);
            mRightBottomRoundRadius = a.getDimension(R.styleable.RoundCornerRelativeLayout_roundCornerRadius, mRightBottomRoundRadius);
            mBgColor = a.getColor(R.styleable.RoundCornerRelativeLayout_backgroudColor, mBgColor);

            mPaint = new Paint();
            mPaint.setColor(mBgColor);
            mPaint.setDither(true);
            a.recycle();
        }
        setBackgroundColor(Color.TRANSPARENT);
        mClipPath = new Path();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() > 0) {
            mClipPath.reset();
            mWHRectF.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
            mClipPath.addRoundRect(mWHRectF, new float[] {mLeftTopRoundRadius, mLeftTopRoundRadius,
                    mRightTopRoundRadius, mRightTopRoundRadius,
                    mLeftBottomRoundRadius, mLeftBottomRoundRadius,
                    mRightBottomRoundRadius, mRightBottomRoundRadius}, Path.Direction.CW);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(mDrawFilter);
        if (mLeftTopRoundRadius > 0 || mRightTopRoundRadius > 0 || mLeftBottomRoundRadius > 0 || mRightBottomRoundRadius > 0) {
            canvas.clipPath(mClipPath);
        }
        if (mPaint != null) {
            canvas.drawRect(mWHRectF, mPaint);
        }
        super.onDraw(canvas);
    }

    public float getLeftTopRoundCornerRadius() {
        return mLeftTopRoundRadius;
    }

    public float getRightTopRoundCornerRadius() {
        return mRightTopRoundRadius;
    }

    public float getLeftBottomRoundCornerRadius() {
        return mLeftBottomRoundRadius;
    }

    public float getRightBottomRoundCornerRadius() {
        return mRightBottomRoundRadius;
    }

    public void setRoundCornerRadius(float roundCornerRadius) {
        setRoundCornerRadius(roundCornerRadius, roundCornerRadius, roundCornerRadius, roundCornerRadius);
    }

    public void setRoundCornerRadius(float leftTopRadius, float rightTopRadius, float leftBottomRadius, float rightBottomRadius) {
        mLeftTopRoundRadius = leftTopRadius;
        mRightTopRoundRadius = rightTopRadius;
        mLeftBottomRoundRadius = leftBottomRadius;
        mRightBottomRoundRadius = rightBottomRadius;
    }

}
