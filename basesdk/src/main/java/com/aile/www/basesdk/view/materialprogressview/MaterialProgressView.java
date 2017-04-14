package com.aile.www.basesdk.view.materialprogressview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.aile.www.basesdk.R;

/**
 * Material 样式环形进度条.
 *
 * @author Ebn
 */
public class MaterialProgressView extends ImageView {
    public MaterialProgressView(Context context) {
        this(context, null, 0);
    }

    public MaterialProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mMaterialProgressDrawable = new MaterialProgressDrawable(this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialProgressView, defStyleAttr, 0);

        setRingColors(typedArray.getColor(R.styleable.MaterialProgressView_ringColor, DEFAULT_RING_COLOR));
        setRingBackgroundColor(typedArray.getColor(R.styleable.MaterialProgressView_ringBackgroundColor,
                DEFAULT_RING_BACKGROUND_COLOR));
        setRingForegroundColor(typedArray.getColor(R.styleable.MaterialProgressView_ringForegroundColor,
                DEFAULT_RING_FOREGROUND_COLOR));
        setRingPaddingPercentage(typedArray.getFloat(R.styleable.MaterialProgressView_ringPaddingPercentage,
                DEFAULT_RING_PADDING_PERCENTAGE));
        setRingStrokeWidthPercentage(typedArray.getFloat(R.styleable.MaterialProgressView_ringStrokeWidthPercentage,
                DEFAULT_RING_STROKE_WIDTH_PERCENTAGE));
        setAnimationDuration(typedArray.getInteger(R.styleable.MaterialProgressView_animationDuration,
                DEFAULT_ANIMATION_DURATION));

        typedArray.recycle();

        setImageDrawable(mMaterialProgressDrawable);
    }

    private static final int DEFAULT_RING_COLOR = 0xff4c5360; // 项目需要的自定义.
    private static final int DEFAULT_RING_BACKGROUND_COLOR = Color.TRANSPARENT;
    private static final int DEFAULT_RING_FOREGROUND_COLOR = Color.TRANSPARENT;
    private static final float DEFAULT_RING_PADDING_PERCENTAGE = 0.2f; // 项目需要的自定义.
    private static final float DEFAULT_RING_STROKE_WIDTH_PERCENTAGE = 0.2f;
    private static final int DEFAULT_ANIMATION_DURATION = 1332;

    private final MaterialProgressDrawable mMaterialProgressDrawable;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int widthWithoutPadding = w - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = h - getPaddingTop() - getPaddingBottom();

        mMaterialProgressDrawable.setSize(Math.min(widthWithoutPadding, heightWithoutPadding));
    }

    /**
     * 通过设置 visibility 控制 start 和 stop 动画.
     */
    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

        boolean visible = visibility == VISIBLE;

        mMaterialProgressDrawable.setVisible(visible, false);

        if (visible) {
            mMaterialProgressDrawable.stop();
            mMaterialProgressDrawable.start();
        } else {
            mMaterialProgressDrawable.stop();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        setVisibility(getVisibility());
        requestLayout();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        setVisibility(GONE);
    }

    /**
     * 设置圆环颜色.
     */
    public void setRingColors(int... colors) {
        mMaterialProgressDrawable.setColorSchemeColors(colors);
    }

    /**
     * 设置圆环圆形背景颜色.
     */
    public void setRingBackgroundColor(int color) {
        mMaterialProgressDrawable.setBackgroundColor(color);
    }

    /**
     * 设置圆环圆形前景颜色.
     */
    public void setRingForegroundColor(int color) {
        mMaterialProgressDrawable.setForegroundColor(color);
    }

    /**
     * 设置圆环的边距占整个背景的百分比, 设置为 <= 0f 表示圆环填满整个背景 (没有边距),
     * 设置为 >= 1f 表示圆环的边距填满整个背景 (不显示圆环), 默认值为 0f.
     */
    public void setRingPaddingPercentage(float percentage) {
        mMaterialProgressDrawable.setRingPaddingPercentage(percentage);
    }

    /**
     * 设置圆环的宽度占圆环半径的百分比, 设置为 <= 0f 表示圆环宽度为 1px, 设置为 >= 1f 表示圆环宽度为半径, 默认值为 0.2f.
     */
    public void setRingStrokeWidthPercentage(float percentage) {
        mMaterialProgressDrawable.setRingStrokeWidthPercentage(percentage);
    }

    /**
     * 设置转一圈的动画时长 (毫秒), 默认值为 1332.
     */
    public void setAnimationDuration(int duration) {
        mMaterialProgressDrawable.setAnimationDuration(duration);
    }
}
