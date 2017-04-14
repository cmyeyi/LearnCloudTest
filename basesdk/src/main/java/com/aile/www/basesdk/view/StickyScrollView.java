package com.aile.www.basesdk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * @Description 上顶效果的scrollView，取自开源代码，在要上顶的布局上加属性 android:tag="sticky";<br>
 * 如果上顶时要透明，需在sticky后拼接"-hastransparancy";<br>
 * 如果上顶的布局的显示内容会在触摸时发生变化，需在sticky后拼接"-nonconstant-ontouch";<br>
 * 如果上顶的布局会一直变化，需要需要一直重绘，需在sticky后拼接"-nonconstant-always";<br>
 * tag示例：android:tag="sticky-nonconstant-ontouch" ;
 * 适用于纵向滚动的情形;
 * @version 1.1
 */
public class StickyScrollView extends ScrollView {

	/**
	 * Tag for views that should stick and have constant drawing. e.g.
	 * TextViews, ImageViews etc
	 */
	public static final String STICKY_TAG = "sticky";

	/**
	 * Flag for views that should stick and have non-constant drawing. e.g.
	 * Buttons, ProgressBars etc
	 */
	public static final String FLAG_NONCONSTANT_ONTOUCH = "-nonconstant";
	
	/**
	 * like {@link #FLAG_NONCONSTANT_ONTOUCH} ,but always need to be redrawn.
	 */
	public static final String FLAG_NONCONSTANT_ALAWYS = "-nonconstant_always";

	/**
	 * Flag for views that have aren't fully opaque
	 */
	public static final String FLAG_HASTRANSPARANCY = "-hastransparancy";

	private ArrayList<View> stickyViews;
	private View currentlyStickingView;
	private float stickyViewTopOffset;
	private boolean redirectTouchesToStickyView;
	private boolean clippingToPadding;
	private boolean clipToPaddingHasBeenSet;
	private int stickTopHeight;
	private OnScrollListener mOnScrollListener;
	private Rect stickingViewHitRect;
	private boolean mAlwaysRedrawStickyView = false;

	private final Runnable invalidateRunnable = new Runnable() {

		@Override
		public void run() {
			if (currentlyStickingView != null && currentlyStickingView.isDirty()) {
				Rect invildRect = getStickingViewInvalidRect();
				if(invildRect != null){
					invalidate(invildRect.left,invildRect.top, invildRect.right, invildRect.bottom);
				}
			}
			postDelayed(this, 16);
		}
	};
	
	private final Runnable watchAndStopRedrawRunnable = new Runnable() {
		
		private int watchCount = 0;
		
		@Override
		public void run() {
			if(currentlyStickingView != null){
				if(currentlyStickingView.isDirty()){
					watchCount = 0;
				}else{
					watchCount ++;
				}
				
				if(watchCount >=3){
					//超过3次都没有发生变化,停止绘制
					stopRedrawStickingView();
					watchCount = 0;
				}else{
					postDelayed(this, 1000);
				}
			}else{
				watchCount =0;
			}
			
		}
	};

	public StickyScrollView(Context context) {
		this(context, null);
	}

	public StickyScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.scrollViewStyle);
	}

	public StickyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup();
	}

	public void setup() {
		stickyViews = new ArrayList<View>();
	}

	private int getLeftForViewRelativeOnlyChild(View v) {
		int left = v.getLeft();
		while (v.getParent() != getChildAt(0)) {
			v = (View) v.getParent();
			left += v.getLeft();
		}
		return left;
	}

	private int getTopForViewRelativeOnlyChild(View v) {
		int top = v.getTop();
		while (v.getParent() != getChildAt(0)) {
			v = (View) v.getParent();
			top += v.getTop();
		}
		return top;
	}

	private int getRightForViewRelativeOnlyChild(View v) {
		int right = v.getRight();
//		while (v.getParent() != getChildAt(0)) {
//			v = (View) v.getParent();
//			right += v.getRight();
//		}
		right += getLeftForViewRelativeOnlyChild(v);
		return right;
	}

	private int getBottomForViewRelativeOnlyChild(View v) {
		int bottom = v.getBottom();
//		while (v.getParent() != getChildAt(0)) {
//			v = (View) v.getParent();
//			bottom += v.getBottom();
//		}
		bottom += getTopForViewRelativeOnlyChild(v);
		return bottom;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (!clipToPaddingHasBeenSet) {
			clippingToPadding = true;
		}
		notifyHierarchyChanged();
	}

	@Override
	public void setClipToPadding(boolean clipToPadding) {
		super.setClipToPadding(clipToPadding);
		clippingToPadding = clipToPadding;
		clipToPaddingHasBeenSet = true;
	}

	@Override
	public void addView(View child) {
		super.addView(child);
		findStickyViews(child);
	}

	@Override
	public void addView(View child, int index) {
		super.addView(child, index);
		findStickyViews(child);
	}

	@Override
	public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
		findStickyViews(child);
	}

	@Override
	public void addView(View child, int width, int height) {
		super.addView(child, width, height);
		findStickyViews(child);
	}

	@Override
	public void addView(View child, android.view.ViewGroup.LayoutParams params) {
		super.addView(child, params);
		findStickyViews(child);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (currentlyStickingView != null) {
			canvas.save();
			canvas.translate(getPaddingLeft(), getScrollY() + stickyViewTopOffset
					+ (clippingToPadding ? getPaddingTop() : 0));
			canvas.clipRect(0, (clippingToPadding ? -stickyViewTopOffset : 0), getWidth(),
					currentlyStickingView.getHeight());
			if (getStringTagForView(currentlyStickingView).contains(FLAG_HASTRANSPARANCY)) {
				showView(currentlyStickingView);
				currentlyStickingView.draw(canvas);
				hideView(currentlyStickingView);
			} else {
				currentlyStickingView.draw(canvas);
			}
			canvas.restore();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (shouldRedirectTouchesToStickyView(ev)) {
			ev.offsetLocation(0, -1 * ((getScrollY() + stickyViewTopOffset) - getTopForViewRelativeOnlyChild(currentlyStickingView)));
//			invalidate(getStickingViewInvalidRect());
		}else if(stickingViewHitRect != null){
			if(ev.getY() < stickingViewHitRect.top){
				return true;
			}
		}
		
		if(currentlyStickingView != null && !mAlwaysRedrawStickyView){
			startRedrawStickingView();
		}
		return super.dispatchTouchEvent(ev);
	}
	
	private boolean shouldRedirectTouchesToStickyView(MotionEvent ev){
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			redirectTouchesToStickyView = true;
		}

		if (redirectTouchesToStickyView) {
			redirectTouchesToStickyView = currentlyStickingView != null;
			stickingViewHitRect = getStickingViewHitRect();
			redirectTouchesToStickyView = redirectTouchesToStickyView && stickingViewHitRect != null && stickingViewHitRect.contains((int)ev.getX(), (int) ev.getY());
		} else if (currentlyStickingView == null) {
			redirectTouchesToStickyView = false;
		}
				
		return redirectTouchesToStickyView;
	}
	
	/**
	 * 显示StickyView的区域，其值以相对ScrollView 的边框来计算，以与传入的MotionEvent保持基准一致
	 * @return 显示StickyView的区域
	 */
	private Rect getStickingViewHitRect(){
		Rect rect = null;
		if(currentlyStickingView != null){
			rect = new Rect();
			rect.left = getLeftForViewRelativeOnlyChild(currentlyStickingView);
			rect.top = (int) stickyViewTopOffset;
			rect.right = getRightForViewRelativeOnlyChild(currentlyStickingView);
			rect.bottom = (int) (currentlyStickingView.getHeight() + stickyViewTopOffset);
		}
		return rect;
	}
	
	/**
	 * 应该重绘的StickyView区域，其值以ScrollView所包含的整个布局高度为基准来计算，以便直接使用invalidate
	 * @return 应该重绘的StickView区域
	 */
	private Rect getStickingViewInvalidRect(){
		Rect rect = null;
		if(currentlyStickingView != null){
			rect = new Rect();
			rect.left = getLeftForViewRelativeOnlyChild(currentlyStickingView);
			rect.top = getTopForViewRelativeOnlyChild(currentlyStickingView);
			rect.right = getRightForViewRelativeOnlyChild(currentlyStickingView);
			rect.bottom = (int) (getScrollY() + currentlyStickingView.getHeight() + stickyViewTopOffset);
		}
		return rect;
	}


	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean ret = false;
		if (redirectTouchesToStickyView) {
			if (stickingViewHitRect != null) {
				int x = (int) ev.getX();
				int y = (int) ev.getY();
				if (stickingViewHitRect.contains(x, y)) {
					ev.offsetLocation(0, ((getScrollY() + stickyViewTopOffset) - getTopForViewRelativeOnlyChild(currentlyStickingView)));
					ret = super.onTouchEvent(ev);
				}else if(y < stickingViewHitRect.top){
					ret = true;
				}
			}
		}else{
			ret = super.onTouchEvent(ev);
		}
		return ret;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		doTheStickyThing();
		if(mOnScrollListener != null){
			mOnScrollListener.onScrollListener(l, t, oldl, oldt);
		}
	}

	private void doTheStickyThing() {
		View viewThatShouldStick = null;
		View approachingView = null;
		for (View v : stickyViews) {
			int viewTop = getTopForViewRelativeOnlyChild(v) - getScrollY() + (clippingToPadding ? 0 : getPaddingTop());
			if (viewTop <= stickTopHeight) {
				if (viewThatShouldStick == null
						|| viewTop > (getTopForViewRelativeOnlyChild(viewThatShouldStick) - getScrollY() + (clippingToPadding ?  0
								: getPaddingTop()))) {
					viewThatShouldStick = v;
				}
			} else {
				if (approachingView == null
						|| viewTop < (getTopForViewRelativeOnlyChild(approachingView) - getScrollY() + (clippingToPadding ? 0
								: getPaddingTop()))) {
					approachingView = v;
				}
			}
		}
		if (viewThatShouldStick != null) {
			stickyViewTopOffset = approachingView == null ? stickTopHeight : Math.min(stickTopHeight,
					getTopForViewRelativeOnlyChild(approachingView) - getScrollY()
							+ (clippingToPadding ? 0 : getPaddingTop()) - viewThatShouldStick.getHeight());
			if (viewThatShouldStick != currentlyStickingView) {
				if (currentlyStickingView != null) {
					stopStickingCurrentlyStickingView();
				}
				startStickingView(viewThatShouldStick);
			}
		} else if (currentlyStickingView != null) {
			stopStickingCurrentlyStickingView();
		}
	}

	private void startStickingView(View viewThatShouldStick) {
		currentlyStickingView = viewThatShouldStick;
		if (getStringTagForView(currentlyStickingView).contains(FLAG_HASTRANSPARANCY)) {
			hideView(currentlyStickingView);
		}
		if (((String) currentlyStickingView.getTag()).contains(FLAG_NONCONSTANT_ALAWYS)) {
			mAlwaysRedrawStickyView = true;
			startRedrawStickingView();
		}else if(((String) currentlyStickingView.getTag()).contains(FLAG_NONCONSTANT_ONTOUCH)){
			startRedrawStickingView();
		}
	}

	private void startRedrawStickingView() {
		post(invalidateRunnable);
		if(!mAlwaysRedrawStickyView){
			removeCallbacks(watchAndStopRedrawRunnable);
			post(watchAndStopRedrawRunnable);
		}
	}

	private void stopStickingCurrentlyStickingView() {
		if (getStringTagForView(currentlyStickingView).contains(FLAG_HASTRANSPARANCY)) {
			showView(currentlyStickingView);
		}
		currentlyStickingView = null;
		stopRedrawStickingView();
	}

	private void stopRedrawStickingView() {
		removeCallbacks(invalidateRunnable);
	}

	/**
	 * Notify that the sticky attribute has been added or removed from one or
	 * more views in the View hierarchy
	 */
	public void notifyStickyAttributeChanged() {
		notifyHierarchyChanged();
	}

	private void notifyHierarchyChanged() {
		if (currentlyStickingView != null) {
			stopStickingCurrentlyStickingView();
		}
		stickyViews.clear();
		findStickyViews(getChildAt(0));
		doTheStickyThing();
		invalidate();
	}

	private void findStickyViews(View v) {
		if (v instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) v;
			for (int i = 0; i < vg.getChildCount(); i++) {
				String tag = getStringTagForView(vg.getChildAt(i));
				if (tag != null && tag.contains(STICKY_TAG)) {
					stickyViews.add(vg.getChildAt(i));
				} else if (vg.getChildAt(i) instanceof ViewGroup) {
					findStickyViews(vg.getChildAt(i));
				}
			}
		} else {
			String tag = (String) v.getTag();
			if (tag != null && tag.contains(STICKY_TAG)) {
				stickyViews.add(v);
			}
		}
	}

	private String getStringTagForView(View v) {
		Object tagObject = v.getTag();
		return String.valueOf(tagObject);
	}

	private void hideView(View v) {
		if (Build.VERSION.SDK_INT >= 11) {
			// v.setAlpha(0);
		} else {
			AlphaAnimation anim = new AlphaAnimation(1, 0);
			anim.setDuration(0);
			anim.setFillAfter(true);
			v.startAnimation(anim);
		}
	}

	private void showView(View v) {
		if (Build.VERSION.SDK_INT >= 11) {
			// v.setAlpha(1);
		} else {
			AlphaAnimation anim = new AlphaAnimation(0, 1);
			anim.setDuration(0);
			anim.setFillAfter(true);
			v.startAnimation(anim);
		}
	}

	/**
	 * 设置上顶的view发生上顶时，所应与ScrollView布局顶部保持的高度，默认为0
	 */
	public void setStickTopHeight(int top){
		this.stickTopHeight = top;
	}
	
	public void setOnScrollListener(OnScrollListener listener){
		mOnScrollListener = listener;
	}
	
	/**
	 * 设置是否在上顶时，一直重绘上顶的View,该设置仅在上顶的view的tag中添加了{@link StickyScrollView#FLAG_NONCONSTANT_ONTOUCH}标志时有效
	 * @param always 设为true会一直重绘该view,设为false仅会在有触摸事件发生在上顶的view上时进行重绘，默认为false
	 *//*
	public void setAlwaysRedrawStickyView(boolean always){
		this.mAlwaysRedrawStickyView = always;
	}*/
	
	public static interface OnScrollListener{
		void onScrollListener(int x, int y, int oldX, int oldY);
	}
}