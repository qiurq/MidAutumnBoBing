package com.banker.framework.views.dialogs.loadingprogress;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.banker.R;

public class LoadingProgressDialog extends Dialog {

	public LoadingProgressDialog(Context context) {
		super(context);
		
//		getContext().setTheme(R.style.LoadingDialogProgressStyle);
		
		View _view = LayoutInflater.from(context).inflate(R.layout.dialog_loading_progress, null);
		
		ViewGroup.LayoutParams _layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
		
		addContentView(_view, _layoutParams);
	}
	
	public void dismiss(){
		if(isShowing()){
			super.dismiss();
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}

	
}
