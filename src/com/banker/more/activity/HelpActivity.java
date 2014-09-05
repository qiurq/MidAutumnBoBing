package com.banker.more.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.banker.framework.activity.FrameActivity;
import com.banker.R;

public class HelpActivity extends FrameActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		setContentView(R.layout.activity_help_activity);
		appendFrameworkCenter(R.layout.activity_help_activity);
		topBackBtnListener(this);
		hideTopRightBtn();
		
//		WebView webView = (WebView) findViewById(R.id.webView);
//		WebSettings ws = webView.getSettings();
//		ws.setJavaScriptEnabled(true);
//		webView.loadUrl("file:///android_assets/help.html");
	}

	
	@Override
	public void onClick(View view) {
		finish();
	}
}
