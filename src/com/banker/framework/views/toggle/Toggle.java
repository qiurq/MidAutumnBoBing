package com.banker.framework.views.toggle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Toggle extends View{

	private Rect mOnToggleSliderStayArea;
	private Rect mOffToggleSliderStayArea;

	private Bitmap mOnToggleImg;
	private Bitmap mOffToggleImg;
	private Bitmap mSliderImg;
	
	/** 滑动事件发生的X坐标 **/
	private float mTouchEventActionX;
	/** 滑动状态 :初始没有滑动**/
	private boolean mNowSlipping = false;
	/** 开关状态 :初始关闭**/
	private boolean mToggleStatus;
	

	public Toggle(Context context) {
		super(context);
	}

	public Toggle(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Toggle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void init(boolean toggleStatus,int sliderImgResId,int onToggleImgResId,int offToggleImgResId){
		mSliderImg = BitmapFactory.decodeResource(getResources(),sliderImgResId);
		mOnToggleImg = BitmapFactory.decodeResource(getResources(),onToggleImgResId);
		mOffToggleImg = BitmapFactory.decodeResource(getResources(),offToggleImgResId);
		
		mOffToggleSliderStayArea = new Rect(0, 0, mSliderImg.getWidth(), mSliderImg.getHeight());
		mOnToggleSliderStayArea = new Rect(mOffToggleImg.getWidth() - mSliderImg.getWidth(), 0,mOffToggleImg.getWidth(), mSliderImg.getHeight());
		
		toggleSwitch(toggleStatus);
	}
	
	public void toggleSwitch(boolean newToggleStatus){
		mToggleStatus = newToggleStatus;
		if(true == mToggleStatus){
			mTouchEventActionX = mOnToggleSliderStayArea.left;//随意设一个比背景一半小的值即可
		}else {
			mTouchEventActionX = mOffToggleSliderStayArea.left;
		}
	}
	
	/**
	 * 绘图方法
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Matrix _matrix = new Matrix();
		Paint _paint = new Paint();
		
		// 画Toggle背景
		if (mTouchEventActionX < (mOnToggleImg.getWidth() / 2)){
			canvas.drawBitmap(mOffToggleImg, _matrix, _paint);// 画出关闭时的背景
		}else {
			canvas.drawBitmap(mOnToggleImg, _matrix, _paint);// 画出打开时的背景
		}

		//计算滑动块的位置
		float _sliderLeftX = 0f; //滑动块左边界X坐标
		if (mNowSlipping){//滑动时，设置坐标
			// 是否划出指定范围,不能让滑块跑到外头
			if (mTouchEventActionX >= mOnToggleImg.getWidth()){
				_sliderLeftX = mOnToggleImg.getWidth() - mSliderImg.getWidth();// 减去游标1/2的长度...
			}else if (mTouchEventActionX < 0) {
				_sliderLeftX = 0;
			} else {
				_sliderLeftX = mTouchEventActionX - mSliderImg.getWidth() / 2;
			}
		} else {// 非滑动状态时，根据Toggle设置滑块位置
			if (true == mToggleStatus){
				_sliderLeftX = mOnToggleSliderStayArea.left;
			} else
				_sliderLeftX = mOffToggleSliderStayArea.left;
		}

		// 对滑块的位置，作最后检查
		if (_sliderLeftX < mOffToggleSliderStayArea.left){
			_sliderLeftX = mOffToggleSliderStayArea.left;
		}else if (_sliderLeftX > mOnToggleSliderStayArea.left)
			_sliderLeftX = mOnToggleSliderStayArea.left;
		
		// 画滑动块
		canvas.drawBitmap(mSliderImg, _sliderLeftX, 0, _paint);
	}
	
	public interface OnToggleStatusChangeListener {
		public void onToggleStatusChanged(boolean toggleStatus);
	}
	
	private OnToggleStatusChangeListener mOnToggleStatusChangeListener;
	
	public void setOnToggleStatusChangeListener(OnToggleStatusChangeListener listener){
		mOnToggleStatusChangeListener = listener;
	}

	/**
	 * 触摸整个toggle的事件,根据触摸的坐标，计算toggle的状态
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()){
		//接触
		case MotionEvent.ACTION_DOWN:
			mNowSlipping = true;
			mTouchEventActionX = event.getX();
			break;
		
		//在屏幕上滑动
		case MotionEvent.ACTION_MOVE:
			mTouchEventActionX = event.getX();
			break;

		//离开屏幕	
		case MotionEvent.ACTION_UP:
			mNowSlipping = false;
			
			//记录之前的开关状态
			boolean _lastToggleStatus = mToggleStatus;

			//根据手指离开的位置，计算现在开关的状态
			if (event.getX() >= (mOnToggleImg.getWidth() / 2)) {
				mToggleStatus = true;
			}else {
				mToggleStatus = false;
			}
			// 如果设置了监听器并且状态已切换,就调用其方法..
			if(null != mOnToggleStatusChangeListener && (mToggleStatus != _lastToggleStatus)){
				mOnToggleStatusChangeListener.onToggleStatusChanged(mToggleStatus);
			}
			break;
		default:
			break;
		}
		
		// 通知系统，重绘控件
		invalidate();
		
		//true表示事件不用向上传递
		return true;
	}

	/**
	 * 计算View实际宽高时调用
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(mOnToggleImg.getWidth(), mOnToggleImg.getHeight());
	}

}
