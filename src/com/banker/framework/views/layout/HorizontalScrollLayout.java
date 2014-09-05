package com.banker.framework.views.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class HorizontalScrollLayout extends ViewGroup {
	
	/** X轴速度基值，大于该值时进行切换 表示每秒移动了600个像素**/
	private static final int SCROLL_VELOCITY = 600; 
	
	private int mCurrentScreenIndex;
	private int mDefaultScreenIndex = 0;
	
	/** 用于判断甩动手势  **/
	private VelocityTracker mVelocityTracker;
	
	private Scroller mScroller;

	public HorizontalScrollLayout(Context context) {
		super(context);
		init();
	}

	public HorizontalScrollLayout(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public HorizontalScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init(){
		mCurrentScreenIndex = mDefaultScreenIndex;
		mScroller = new Scroller(getContext());
	}

	/**
	 * Position all children within this layout.
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//当该View的尺寸变化时
		if(changed){
			final int _childCount = super.getChildCount();
			
			int _childLeftPosition = 0;
			for(int i = 0; i < _childCount; i ++){
				View _childView = super.getChildAt(i);
				if(_childView.getVisibility() != View.GONE){
					final int _childWidth = _childView.getMeasuredWidth();
					_childView.layout(_childLeftPosition, 0, _childLeftPosition + _childWidth, _childView.getMeasuredHeight());
				
					_childLeftPosition += _childWidth;
				}
				
			}
		}
				
	}

	/** 当父控件调用该View的measure()方法时，执行以下方法，并把
	 * 
	 *  When overriding this method, you must call setMeasuredDimension(int, int) to store the measured width and height of this view.
	 *  **/
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int _measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
		int _measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
		
		int _childCount = super.getChildCount();
		for(int i = 0; i < _childCount; i ++){
			super.getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		
		//左移还是右移，用滚动条来理解
		super.scrollTo(mCurrentScreenIndex * _measuredWidth, 0);
		
		//如果不用特殊处理，则直接调用父类的onMeasure()方法就行
		super.setMeasuredDimension(_measuredWidth, _measuredHeight);
	}
	
	/** 滚动屏幕 //使屏幕移动到第screenIndex+1屏**/
	public void scrollScreenByScreenIndex(int screenIndex){
		if (getScrollX() != (screenIndex * getWidth())) {
			final int delta = screenIndex * getWidth() - getScrollX();
			
			mScroller.startScroll(getScrollX(), 0, delta, 0,Math.abs(delta) * 2);
			
			mCurrentScreenIndex = screenIndex;
			
			//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果  
			invalidate(); //To force a view to draw, call invalidate().

			if (mOnViewChangeListener != null) {
				mOnViewChangeListener.OnViewChange(mCurrentScreenIndex);
			}
		}
	}
	
	/** 由系统回调，根据滚动情况，执行实际的图像控制功能 **/
	@Override
	public void computeScroll() {
		//只有调用了mScroller的startScroll()方法时，返回为真
		if(mScroller.computeScrollOffset()){
			
			//这里调用View的scrollTo()完成实际的滚动  
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			
			//必须调用该方法，否则不一定能看到滚动效果  
			postInvalidate();
		}
		
	}

	/** 记录动作的X坐标，因为是水平移动 **/
	private float mStartMotionX;
	
	/** View的屏幕接收事件，可以通过组合，实现各种手势动作 **/
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float _eventActionX = event.getX();
		float _eventActionY = event.getY();
		
		int _eventAction = event.getAction();
		switch(_eventAction){
		case MotionEvent.ACTION_DOWN :
			if(null == mVelocityTracker){
				mVelocityTracker = VelocityTracker.obtain();
				mVelocityTracker.addMovement(event);
			}
			
			//记录
			mStartMotionX = _eventActionX;
			
			break;
		case MotionEvent.ACTION_MOVE :
			int _deltaX = (int)(_eventActionX - mStartMotionX);
			if(isCanMove(_deltaX)){
				if(null != mVelocityTracker){
					mVelocityTracker.addMovement(event);
				}
				
				super.scrollBy(-_deltaX, 0);
				
				mStartMotionX = _eventActionX;//控制View均匀的移动
				
			}
			
			break;
		case MotionEvent.ACTION_UP :
			float _xVelocity = 0;
			if(null != mVelocityTracker){
				mVelocityTracker.addMovement(event);
				//初始化速率的单位1s
				mVelocityTracker.computeCurrentVelocity(1 * 1000);
				//得到X轴方向手指移动的速度
				_xVelocity = mVelocityTracker.getXVelocity();
			}
			//_xVelocity为正值说明手指向右滑动，为负值说明手指向左滑动，每秒是否滑动了600个像素的速度
			if(_xVelocity > SCROLL_VELOCITY && mCurrentScreenIndex > 0){//手向右滑动，screen-1
				scrollScreenByScreenIndex(mCurrentScreenIndex - 1);
			}else if(_xVelocity < -SCROLL_VELOCITY && mCurrentScreenIndex < (getChildCount() - 1)){
				scrollScreenByScreenIndex(mCurrentScreenIndex + 1);
			}else {
				
			}
			
			if(null != mVelocityTracker){
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			
			break;
		default: 
			break;
		}
		
		return true;//返回true，表示事件已经处理
	}
	
	/** 监测View是否可以移动 **/
	private boolean isCanMove(int deltaX){
		//如果groupView已经滑动到了最左边，并且手势向右滑动
		if(getScrollX() <= 0 && deltaX > 0 ){//手指向右滑动
			return false;
		}
		
		//如果GroupView已经滑动到了最右边，并且手势向左滑动
		if(getScrollX() >= (getChildCount() * getWidth()) && deltaX < 0){
			return false;
		}
		
		return true;
	}
	
	public interface OnViewChangeListener {
		public void OnViewChange(int viewIndex);
	}

	private OnViewChangeListener mOnViewChangeListener;
	
	public void setOnViewChangeListener(OnViewChangeListener mOnViewChangeListener) {
		this.mOnViewChangeListener = mOnViewChangeListener;
	}

}
