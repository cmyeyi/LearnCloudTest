package com.aile.www.basesdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author zijianlu
 * @version 创建时间 2013-6-20
 * 提供下拉刷新的功能的headView，作用是当View的高度变化时，将其子View放在底部的中央位置
 */
public class HeadWrapper extends FrameLayout {
	private View mChild;
	private int mInitH;

	private int mSpace;

	public HeadWrapper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mChild = getChildAt(0);
	}

	boolean isFirst = true;

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (isFirst) {
			mInitH = getMeasuredHeight();
			mSpace = (getMeasuredWidth() - mChild.getMeasuredWidth()) / 2;

			if (mSpace < 0) {
				mSpace = 0;
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		/* 将mChild放在底部的中间位置 */
		mChild.layout(mSpace, b - mInitH, getMeasuredWidth() - mSpace, b);
	}

}
