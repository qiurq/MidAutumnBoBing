package com.banker.framework.views;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.banker.R;

public class TopBarView {
	
	private LinearLayout layout;
	private Button btn_topBack;
	private Button btn_topRight;
	private TextView txtV_topTitle;

	public TopBarView(Activity activity) {
		layout = (LinearLayout) activity.findViewById(R.id.framework_top);
		initView();	
	}

	private void initView() {
		btn_topBack = (Button) layout.findViewById(R.id.btn_topBack);
		btn_topRight = (Button) layout.findViewById(R.id.btn_topRight);
		txtV_topTitle = (TextView) layout.findViewById(R.id.txtV_topTitle);
	}
	
	/** 设置顶部title文本 **/
	public void setTopTitle(String title) {
		txtV_topTitle.setText(title);
	}
	
	/** 设置右边按钮的文字 **/
	public void setTopRightBtnText(String text) {
		btn_topRight.setText(text);
	}
	
	/** 隐藏右边按钮 **/
	public void hideTopRightBtn() {
		btn_topRight.setVisibility(View.INVISIBLE);
	}
	
	/** 隐藏顶部返回按钮 **/
	public void hideTopBacktBtn() {
		btn_topBack.setVisibility(View.INVISIBLE);
	}

	public Button getBtn_topBack() {
		return btn_topBack;
	}

	public Button getBtn_topRight() {
		return btn_topRight;
	}		
}
