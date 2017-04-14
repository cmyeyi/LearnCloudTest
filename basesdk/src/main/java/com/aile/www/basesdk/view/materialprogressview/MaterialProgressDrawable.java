package com.aile.www.basesdk.view.materialprogressview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * Copy from {@link android.support.v4.widget.MaterialProgressDrawable}. 有改动.
 *
 * Fancy progress indicator for Material theme.
 */
class MaterialProgressDrawable extends Drawable implements Animatable {
    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    static final Interpolator MATERIAL_INTERPOLATOR = new FastOutSlowInInterpolator();

    private static final float FULL_ROTATION = 1080.0f;

    private static final int[] COLORS = new int[] {
        Color.BLACK
    };

    /**
     * The value in the linear interpolator for animating the drawable at which
     * the color transition should start
     */
    private static final float COLOR_START_DELAY_OFFSET = 0.75f;
    private static final float END_TRIM_START_DELAY_OFFSET = 0.5f;
    private static final float START_TRIM_DURATION_OFFSET = 0.5f;

    /** The duration of a single progress spin in milliseconds. */
    private static final int ANIMATION_DURATION = 1332;

    /** The number of points in the progress "star". */
    private static final float NUM_POINTS = 5f;

    /** The indicator ring, used to manage animation state. */
    private final Ring mRing;

    /** Canvas rotation in degrees. */
    private float mRotation;

    /** Layout info for the arrowhead for the large spinner in dp */
    private static final float MAX_PROGRESS_ARC = .8f;

    private View mParent;
    private Animation mAnimation;
    float mRotationCount;
    private int mSize = 0;

    MaterialProgressDrawable(View parent) {
        mParent = parent;

        mRing = new Ring(mCallback);
        mRing.setColors(COLORS);

        setupAnimators();
    }

    private float mRingPaddingPercentage;
    private float mRingStrokeWidthPercentage;
    private int mAnimationDuration = ANIMATION_DURATION;

    private boolean mRunning = false;

    /**
     * 设置圆环的边距占整个背景的百分比, 设置为 <= 0f 表示圆环填满整个背景 (没有边距),
     * 设置为 >= 1f 表示圆环的边距填满整个背景 (不显示圆环).
     */
    public void setRingPaddingPercentage(float percentage) {
        float checkedPercentage = checkPercentage(percentage);

        if (mRingPaddingPercentage == checkedPercentage) {
            return;
        }

        mRingPaddingPercentage = checkedPercentage;

        invalidateRing();
    }

    /**
     * 设置圆环的宽度占圆环半径的百分比, 设置为 <=0f 表示圆环宽度为 1px, 设置为 >= 1f 表示圆环宽度为半径.
     */
    public void setRingStrokeWidthPercentage(float percentage) {
        float checkedPercentage = checkPercentage(percentage);

        if (mRingStrokeWidthPercentage == checkedPercentage) {
            return;
        }

        mRingStrokeWidthPercentage = checkedPercentage;

        invalidateRing();
    }

    /**
     * 小于 0f 的返回 0f, 大于 1f 的返回 1f.
     */
    private static float checkPercentage(float percentage) {
        return Math.min(1f, Math.max(0f, percentage));
    }

    /**
     * 设置大小.
     */
    public void setSize(int size) {
        if (mSize == size) {
            return;
        }

        mSize = size;

        invalidateRing();
    }

    private void invalidateRing() {
        // 计算除去边距之后的圆环大小.
        float ringSize = mSize * (1f - mRingPaddingPercentage);

        if (mRingStrokeWidthPercentage == 0) { // 如果百分比为 0, 则圆环宽度为 1px.
            mRing.setStrokeWidth(1f);
            mRing.setCenterRadius(ringSize * (0.5f - mRingStrokeWidthPercentage * 0.25f) - 0.5f);
        } else {
            mRing.setStrokeWidth(ringSize / 2f * mRingStrokeWidthPercentage);
            mRing.setCenterRadius(ringSize * (0.5f - mRingStrokeWidthPercentage * 0.25f));
        }
        mRing.setInsets(mSize);
    }

    /**
     * 设置转一圈的动画时长 (毫秒).
     */
    public void setAnimationDuration(int duration) {
        mAnimationDuration = duration;
    }

    /**
     * Set the start and end trim for the progress spinner arc.
     *
     * @param startAngle start angle
     * @param endAngle end angle
     */
    public void setStartEndTrim(float startAngle, float endAngle) {
        mRing.setStartTrim(startAngle);
        mRing.setEndTrim(endAngle);
    }

    /**
     * Set the amount of rotation to apply to the progress spinner.
     *
     * @param rotation Rotation is from [0..1]
     */
    public void setProgressRotation(float rotation) {
        mRing.setRotation(rotation);
    }

    /**
     * Update the background color of the circle image view.
     */
    public void setBackgroundColor(int color) {
        mRing.setBackgroundColor(color);
    }

    public void setForegroundColor(int color) {
        mRing.setForegroundColor(color);
    }

    /**
     * Set the colors used in the progress animation from color resources.
     * The first color will also be the color of the bar that grows in response
     * to a user swipe gesture.
     *
     * @param colors
     */
    public void setColorSchemeColors(int... colors) {
        mRing.setColors(colors);
        mRing.setColorIndex(0);
    }

    @Override
    public void draw(Canvas c) {
        final Rect bounds = getBounds();
        final int saveCount = c.save();
        c.rotate(mRotation, bounds.exactCenterX(), bounds.exactCenterY());
        mRing.draw(c, bounds);
        c.restoreToCount(saveCount);
    }

    @Override
    public void setAlpha(int alpha) {
        mRing.setAlpha(alpha);
    }

    public int getAlpha() {
        return mRing.getAlpha();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mRing.setColorFilter(colorFilter);
    }

    @SuppressWarnings("unused")
    void setRotation(float rotation) {
        mRotation = rotation;
        invalidateSelf();
    }

    @SuppressWarnings("unused")
    private float getRotation() {
        return mRotation;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }

    @Override
    public void start() {
        if (mRunning) {
            return;
        }

        mAnimation.reset();
        mRing.setColorIndex(0);
        mRing.resetOriginals();
        mAnimation.setDuration(mAnimationDuration);
        mParent.startAnimation(mAnimation);

        mRunning = true;
    }

    @Override
    public void stop() {
        if (!mRunning) {
            return;
        }

        mParent.clearAnimation();
        setRotation(0);
        mRing.setColorIndex(0);
        mRing.resetOriginals();

        mRunning = false;
    }

    float getMinProgressArc(Ring ring) {
        return (float) Math.toRadians(
                ring.getStrokeWidth() / (2 * Math.PI * ring.getCenterRadius()));
    }

    // Adapted from ArgbEvaluator.java
    private int evaluateColorChange(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24)
                | ((startR + (int) (fraction * (endR - startR))) << 16)
                | ((startG + (int) (fraction * (endG - startG))) << 8)
                | ((startB + (int) (fraction * (endB - startB))));
    }

    /**
     * Update the ring color if this is within the last 25% of the animation.
     * The new ring color will be a translation from the starting ring color to
     * the next color.
     */
    void updateRingColor(float interpolatedTime, Ring ring) {
        if (interpolatedTime > COLOR_START_DELAY_OFFSET) {
            // scale the interpolatedTime so that the full
            // transformation from 0 - 1 takes place in the
            // remaining time
            ring.setColor(evaluateColorChange((interpolatedTime - COLOR_START_DELAY_OFFSET)
                    / (1.0f - COLOR_START_DELAY_OFFSET), ring.getStartingColor(),
                    ring.getNextColor()));
        }
    }

    private void setupAnimators() {
        final Ring ring = mRing;
        final Animation animation = new Animation() {
                @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                // The minProgressArc is calculated from 0 to create an
                // angle that matches the stroke width.
                final float minProgressArc = getMinProgressArc(ring);
                final float startingEndTrim = ring.getStartingEndTrim();
                final float startingTrim = ring.getStartingStartTrim();
                final float startingRotation = ring.getStartingRotation();

                updateRingColor(interpolatedTime, ring);

                // Moving the start trim only occurs in the first 50% of a
                // single ring animation
                if (interpolatedTime <= START_TRIM_DURATION_OFFSET) {
                    // scale the interpolatedTime so that the full
                    // transformation from 0 - 1 takes place in the
                    // remaining time
                    final float scaledTime = (interpolatedTime)
                            / (1.0f - START_TRIM_DURATION_OFFSET);
                    final float startTrim = startingTrim
                            + ((MAX_PROGRESS_ARC - minProgressArc) * MATERIAL_INTERPOLATOR
                                    .getInterpolation(scaledTime));
                    ring.setStartTrim(startTrim);
                }

                // Moving the end trim starts after 50% of a single ring
                // animation completes
                if (interpolatedTime > END_TRIM_START_DELAY_OFFSET) {
                    // scale the interpolatedTime so that the full
                    // transformation from 0 - 1 takes place in the
                    // remaining time
                    final float minArc = MAX_PROGRESS_ARC - minProgressArc;
                    float scaledTime = (interpolatedTime - START_TRIM_DURATION_OFFSET)
                            / (1.0f - START_TRIM_DURATION_OFFSET);
                    final float endTrim = startingEndTrim
                            + (minArc * MATERIAL_INTERPOLATOR.getInterpolation(scaledTime));
                    ring.setEndTrim(endTrim);
                }

                final float rotation = startingRotation + (0.25f * interpolatedTime);
                ring.setRotation(rotation);

                float groupRotation = ((FULL_ROTATION / NUM_POINTS) * interpolatedTime)
                        + (FULL_ROTATION * (mRotationCount / NUM_POINTS));
                setRotation(groupRotation);
            }
        };
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(LINEAR_INTERPOLATOR);
        animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
            public void onAnimationStart(Animation animation) {
                mRotationCount = 0;
            }

                @Override
            public void onAnimationEnd(Animation animation) {
                // do nothing
            }

                @Override
            public void onAnimationRepeat(Animation animation) {
                ring.storeOriginals();
                ring.goToNextColor();
                ring.setStartTrim(ring.getEndTrim());

                mRotationCount = (mRotationCount + 1) % (NUM_POINTS);
            }
        });
        mAnimation = animation;
    }

    private final Callback mCallback = new Callback() {
        @Override
        public void invalidateDrawable(Drawable d) {
            invalidateSelf();
        }

        @Override
        public void scheduleDrawable(Drawable d, Runnable what, long when) {
            scheduleSelf(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable d, Runnable what) {
            unscheduleSelf(what);
        }
    };

    private static class Ring {
        private final RectF mTempBounds = new RectF();
        private final Paint mPaint = new Paint();

        private final Callback mCallback;

        private float mStartTrim = 0.0f;
        private float mEndTrim = 0.0f;
        private float mRotation = 0.0f;
        private float mStrokeWidth = 5.0f;
        private float mStrokeInset = 2.5f;

        private int[] mColors;
        // mColorIndex represents the offset into the available mColors that the
        // progress circle should currently display. As the progress circle is
        // animating, the mColorIndex moves by one to the next available color.
        private int mColorIndex;
        private float mStartingStartTrim;
        private float mStartingEndTrim;
        private float mStartingRotation;
        private double mRingCenterRadius;
        private int mAlpha;
        private final Paint mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private int mBackgroundColor = Color.TRANSPARENT;
        private int mForegroundColor = Color.TRANSPARENT;
        private int mCurrentColor;

        Ring(Callback callback) {
            mCallback = callback;

            mPaint.setStrokeCap(Paint.Cap.SQUARE);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Style.STROKE);
        }

        public void setBackgroundColor(int color) {
            mBackgroundColor = color;
        }

        public void setForegroundColor(int color) {
            mForegroundColor = color;
        }

        /**
         * Draw the progress spinner
         */
        public void draw(Canvas c, Rect bounds) {
            drawBackground(c, bounds);
            drawRing(c, bounds);
            drawForeground(c, bounds);
        }

        /**
         * 画圆环.
         */
        private void drawRing(Canvas c, Rect bounds) {
            final RectF arcBounds = mTempBounds;
            arcBounds.set(bounds);
            arcBounds.inset(mStrokeInset, mStrokeInset);

            final float startAngle = (mStartTrim + mRotation) * 360;
            final float endAngle = (mEndTrim + mRotation) * 360;
            float sweepAngle = endAngle - startAngle;

            mPaint.setColor(mCurrentColor);
            c.drawArc(arcBounds, startAngle, sweepAngle, false, mPaint);
        }

        /**
         * 如果背景色不透明, 画圆形背景.
         */
        private void drawBackground(Canvas c, Rect bounds) {
            if (((mBackgroundColor >> 24) & 0xff) == 0) {
                return;
            }

            mCirclePaint.setColor(mBackgroundColor);
            c.drawCircle(bounds.exactCenterX(), bounds.exactCenterY(), bounds.width() / 2,
                    mCirclePaint);
        }

        /**
         * 如果前景色不透明, 画圆形前景.
         */
        private void drawForeground(Canvas c, Rect bounds) {
            if (((mForegroundColor >> 24) & 0xff) == 0) {
                return;
            }

            mCirclePaint.setColor(mForegroundColor);
            c.drawCircle(bounds.exactCenterX(), bounds.exactCenterY(), bounds.width() / 2,
                    mCirclePaint);
        }

        /**
         * Set the colors the progress spinner alternates between.
         *
         * @param colors Array of integers describing the colors. Must be non-<code>null</code>.
         */
        public void setColors(@NonNull int[] colors) {
            mColors = colors;
            // if colors are reset, make sure to reset the color index as well
            setColorIndex(0);
        }

        /**
         * Set the absolute color of the progress spinner. This is should only
         * be used when animating between current and next color when the
         * spinner is rotating.
         *
         * @param color int describing the color.
         */
        public void setColor(int color) {
            mCurrentColor = color;
        }

        /**
         * @param index Index into the color array of the color to display in
         *            the progress spinner.
         */
        public void setColorIndex(int index) {
            mColorIndex = index;
            mCurrentColor = mColors[mColorIndex];
        }

        /**
         * @return int describing the next color the progress spinner should use when drawing.
         */
        public int getNextColor() {
            return mColors[getNextColorIndex()];
        }

        private int getNextColorIndex() {
            return (mColorIndex + 1) % (mColors.length);
        }

        /**
         * Proceed to the next available ring color. This will automatically
         * wrap back to the beginning of colors.
         */
        public void goToNextColor() {
            setColorIndex(getNextColorIndex());
        }

        public void setColorFilter(ColorFilter filter) {
            mPaint.setColorFilter(filter);
            invalidateSelf();
        }

        /**
         * @param alpha Set the alpha of the progress spinner and associated arrowhead.
         */
        public void setAlpha(int alpha) {
            mAlpha = alpha;
        }

        /**
         * @return Current alpha of the progress spinner and arrowhead.
         */
        public int getAlpha() {
            return mAlpha;
        }

        /**
         * @param strokeWidth Set the stroke width of the progress spinner in pixels.
         */
        public void setStrokeWidth(float strokeWidth) {
            mStrokeWidth = strokeWidth;
            mPaint.setStrokeWidth(strokeWidth);
            invalidateSelf();
        }

        @SuppressWarnings("unused")
        public float getStrokeWidth() {
            return mStrokeWidth;
        }

        @SuppressWarnings("unused")
        public void setStartTrim(float startTrim) {
            mStartTrim = startTrim;
            invalidateSelf();
        }

        @SuppressWarnings("unused")
        public float getStartTrim() {
            return mStartTrim;
        }

        public float getStartingStartTrim() {
            return mStartingStartTrim;
        }

        public float getStartingEndTrim() {
            return mStartingEndTrim;
        }

        public int getStartingColor() {
            return mColors[mColorIndex];
        }

        @SuppressWarnings("unused")
        public void setEndTrim(float endTrim) {
            mEndTrim = endTrim;
            invalidateSelf();
        }

        @SuppressWarnings("unused")
        public float getEndTrim() {
            return mEndTrim;
        }

        @SuppressWarnings("unused")
        public void setRotation(float rotation) {
            mRotation = rotation;
            invalidateSelf();
        }

        @SuppressWarnings("unused")
        public float getRotation() {
            return mRotation;
        }

        public void setInsets(int size) {
            float insets;
            if (mRingCenterRadius <= 0 || size < 0) {
                insets = (float) Math.ceil(mStrokeWidth / 2.0f);
            } else {
                insets = (float) (size / 2.0f - mRingCenterRadius);
            }
            mStrokeInset = insets;
        }

        @SuppressWarnings("unused")
        public float getInsets() {
            return mStrokeInset;
        }

        /**
         * @param centerRadius Inner radius in px of the circle the progress
         *            spinner arc traces.
         */
        public void setCenterRadius(double centerRadius) {
            mRingCenterRadius = centerRadius;
        }

        public double getCenterRadius() {
            return mRingCenterRadius;
        }

        /**
         * @return The amount the progress spinner is currently rotated, between [0..1].
         */
        public float getStartingRotation() {
            return mStartingRotation;
        }

        /**
         * If the start / end trim are offset to begin with, store them so that
         * animation starts from that offset.
         */
        public void storeOriginals() {
            mStartingStartTrim = mStartTrim;
            mStartingEndTrim = mEndTrim;
            mStartingRotation = mRotation;
        }

        /**
         * Reset the progress spinner to default rotation, start and end angles.
         */
        public void resetOriginals() {
            mStartingStartTrim = 0;
            mStartingEndTrim = 0;
            mStartingRotation = 0;
            setStartTrim(0);
            setEndTrim(0);
            setRotation(0);
        }

        private void invalidateSelf() {
            mCallback.invalidateDrawable(null);
        }
    }
}
