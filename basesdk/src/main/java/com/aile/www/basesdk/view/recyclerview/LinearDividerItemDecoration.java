package com.aile.www.basesdk.view.recyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Adds interior dividers to a {@link RecyclerView} with a
 * {@link LinearLayoutManager} or its subclass.
 */
public class LinearDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int mOrientation;
    private int mstartOffset =0;
    private int mEndOffset = 0;
    private int mSpacing = 0;

    public LinearDividerItemDecoration(Drawable divider) {
        mDivider = divider;
    }

    public LinearDividerItemDecoration() {
    }

    public LinearDividerItemDecoration(int startOffset, int endOffset, int spacing) {
        setOffsetAndSpacing(startOffset, endOffset, spacing);
    }

    public void setOffsetAndSpacing(int startOffset, int endOffset, int spacing) {
        mstartOffset = startOffset;
        mEndOffset = endOffset;
        mSpacing = spacing;
    }

    /**
     * Draws horizontal or vertical dividers onto the parent {@link RecyclerView}.
     *
     * @param canvas The {@link Canvas} onto which dividers will be drawn
     * @param parent The {@code RecyclerView} onto which dividers are being added
     * @param state  The current {@link android.support.v7.widget.RecyclerView.State}
     *               of the {@code RecyclerView}
     */
    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (mDivider != null) {
            if (mOrientation == LinearLayoutManager.HORIZONTAL) {
                drawHorizontalDividers(canvas, parent);
            } else if (mOrientation == LinearLayoutManager.VERTICAL) {
                drawVerticalDividers(canvas, parent);
            }
        }
    }

    /**
     * Determines the size and location of offsets between items in the parent
     * {@link RecyclerView}.
     *
     * @param outRect The {@link Rect} of offsets to be added around the child
     *                view
     * @param view    The child view to be decorated with an offset
     * @param parent  The {@code RecyclerView} onto which dividers are being
     *                added
     * @param state   The current {@link android.support.v7.widget.RecyclerView.State}
     *                of the {@code RecyclerView}
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);

        mOrientation = ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            if (pos == 0) {
                outRect.left = mstartOffset;
                return;
            }else if(pos == parent.getAdapter().getItemCount() - 1){
                outRect.left = mSpacing;
                outRect.right = mEndOffset;
            }else if(mDivider != null){
                outRect.left = mDivider.getIntrinsicWidth();
            }else{
                outRect.left = mSpacing;
            }
        } else if (mOrientation == LinearLayoutManager.VERTICAL) {
            if (pos == 0) {
                outRect.top = mstartOffset;
                return;
            }else if(pos == parent.getAdapter().getItemCount() - 1){
                outRect.top = mSpacing;
                outRect.bottom = mEndOffset;
            }else if(mDivider != null){
                outRect.top = mDivider.getIntrinsicWidth();
            }else{
                outRect.top = mSpacing;
            }
        }
    }

    /**
     * Adds dividers to a {@link RecyclerView} with a
     * {@link LinearLayoutManager} or its subclass oriented horizontally.
     *
     * @param canvas The {@link Canvas} onto which horizontal dividers will be
     *               drawn
     * @param parent The {@code RecyclerView} onto which horizontal dividers
     *               are being added
     */
    private void drawHorizontalDividers(Canvas canvas, RecyclerView parent) {
        int parentTop = parent.getPaddingTop();
        int parentBottom = parent.getHeight() - parent.getPaddingBottom();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int parentLeft = child.getRight() + params.rightMargin + mstartOffset;
            int parentRight = parentLeft + mDivider.getIntrinsicWidth();

            mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom);
            mDivider.draw(canvas);
        }
    }

    /**
     * Adds dividers to a {@link RecyclerView} with a
     * {@link LinearLayoutManager} or its subclass oriented vertically.
     *
     * @param canvas The {@link Canvas} onto which vertical dividers will be
     *               drawn
     * @param parent The {@code RecyclerView} onto which vertical dividers are
     *               being added
     */
    private void drawVerticalDividers(Canvas canvas, RecyclerView parent) {
        int parentLeft = parent.getPaddingLeft();
        int parentRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int parentTop = child.getBottom() + params.bottomMargin + mstartOffset;
            int parentBottom = parentTop + mDivider.getIntrinsicHeight();

            mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom);
            mDivider.draw(canvas);
        }
    }
}