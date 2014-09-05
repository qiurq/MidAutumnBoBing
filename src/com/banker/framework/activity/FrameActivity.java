package com.banker.framework.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.banker.framework.views.TopBarView;
import com.banker.framework.webService.WebServiceThread;
import com.banker.R;

public class FrameActivity extends BaseActivity {
	
	protected WebServiceThread mWebServiceThread;
	
	protected TopBarView topBarView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//只支持竖屏
		super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		super.setContentView(R.layout.framework);
		
		//初始化界面框架
		initViews();
	}

	/** 初始化界面框架 **/
	private void initViews() {
		topBarView = new TopBarView(this);
	}
	
	/** 设置顶部的Title **/
	protected void setTopTitle(String title) {
		topBarView.setTopTitle(title);
	}
	
	/** 隐藏顶部右边按钮 **/
	protected void hideTopRightBtn() {
		topBarView.hideTopRightBtn();
	}
	
	/** 隐藏顶部返回按钮 **/
	protected void hideTopBackBtn() {
		topBarView.hideTopBacktBtn();
	}
	
	/** 顶部返回按钮事件 **/
	protected void topBackBtnListener(View.OnClickListener OnClickListener) {
		topBarView.getBtn_topBack().setOnClickListener(OnClickListener);
	}
	
	/** 顶部右边按钮事件 **/
	protected void topRightBtnListener(View.OnClickListener OnClickListener) {
		topBarView.getBtn_topRight().setOnClickListener(OnClickListener);
	}
	
	/** 设置顶部右边按钮文本 **/
	protected void setTopRightBtnText(String text) {
		topBarView.setTopRightBtnText(text);
	}
	
	/**
	 * 为中央区添加内容
	 */
	protected void appendFrameworkCenter(int layoutResId) {
		RelativeLayout framework_center = (RelativeLayout) findViewById(R.id.framework_center);
		View _view = inflateView(layoutResId);
		RelativeLayout.LayoutParams _layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
		framework_center.addView(_view, _layoutParams);
	}

	@Override
	protected WebServiceThread getWebServiceThread() {
		return mWebServiceThread;
	}
	
}
