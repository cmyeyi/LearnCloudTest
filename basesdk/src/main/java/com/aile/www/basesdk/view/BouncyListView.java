package com.aile.www.basesdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;


public class BouncyListView extends ListView implements OnTouchListener {

	private Scroller mScroller;

	private float mFactor = 0.3f;

	private int mOverPullBackTime = 300;

	public BouncyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);
		setOnTouchListener(this);
	}

	/** 设置listview下拉距离和手指滑动距离和的比例*/
	public void setOverPullFactor(float factor) {
		if (factor > 0) {
			mFactor = factor;
		}
	}

	/** 设置回弹的时间*/
	public void setOverPullBackTime(int overPullBackTime) {
		if (overPullBackTime > 0) {
			mOverPullBackTime = overPullBackTime;
		}
	}

	private int mFirstChildTop = Integer.MAX_VALUE;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// 记录ListView第一个item的顶部高度，用来判断是否在最顶端
		if (mFirstChildTop == Integer.MAX_VALUE && getAdapter() != null
				&& getChildCount() != 0) {

			mFirstChildTop = getChildAt(0).getTop();
		}
		return super.dispatchTouchEvent(ev);
	}

	// 滑动距离及坐标?
	private float yLast;

	@Override
	public boolean onTouch(View v, MotionEvent ev) {

		ListAdapter adapter = getAdapter();
		if (adapter == null || adapter.getCount() == 0) {
			return false;
		}

		int action = ev.getAction() & MotionEvent.ACTION_MASK;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			yLast = ev.getY();
			break;

		case MotionEvent.ACTION_MOVE:
			final float curY = ev.getY();
			int delty = (int) ((curY - yLast) * mFactor);

			if (getFirstVisiblePosition() == 0
					&& getChildAt(0).getTop() >= mFirstChildTop && delty > 0) {
				scrollTo(0, -delty);
				doCancel(ev);

				return true;
			}

			if (getLastVisiblePosition() == adapter.getCount() - 1
					&& getChildAt(getChildCount() - 1).getBottom() <= getHeight()
					&& delty < 0) {
				scrollTo(0, -delty);
				doCancel(ev);
				return true;
			}

			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:

			if (getScrollY() != 0) {
				mScroller.abortAnimation();
				mScroller.startScroll(0, getScrollY(), 0, -getScrollY(),
						mOverPullBackTime);
				invalidate();
			}

			break;
		default:
			break;
		}

		return false;
	}

	/** 取消正常的事件传递 */
	private void doCancel(MotionEvent ev) {

		requestDisallowInterceptTouchEvent(true);

		MotionEvent cancelEvent = MotionEvent.obtain(ev);
		int cancelAction = (ev.getAction() & MotionEvent.ACTION_POINTER_ID_MASK)
				| MotionEvent.ACTION_CANCEL;

		cancelEvent.setAction(cancelAction);

		onTouchEvent(cancelEvent);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {

			scrollTo(0, mScroller.getCurrY());
			invalidate();

			if (mScroller.getCurrY() == mScroller.getFinalY()) {
				mScroller.forceFinished(true);
			}
		}
	}
}
