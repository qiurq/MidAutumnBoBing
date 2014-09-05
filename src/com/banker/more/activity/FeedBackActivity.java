package com.banker.more.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.banker.framework.activity.FrameActivity;
import com.banker.R;

public class FeedBackActivity extends FrameActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendFrameworkCenter(R.layout.activity_feed_back);
		setTopTitle(getResources().getString(R.string.feedBack));
		topBackBtnListener(this);
		hideTopRightBtn();
		init();
	}

	private void init() {
		Button btn_send = (Button) findViewById(R.id.btn_send);
		btn_send.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {		
		
		switch (view.getId()) {
		case R.id.btn_topBack:
			finish();
			break;
		case R.id.btn_send:
			EditText edt_feedbackText = (EditText) findViewById(R.id.edt_feedbackText);
			String text = edt_feedbackText.getText().toString().trim();
			
			if(null == text || "".equals(text)) {
				showToast(R.string.feedbackText);
			}else {
				Intent intent = new Intent(Intent.ACTION_SENDTO);
				intent.setData(Uri.parse("mailto:+banker888_com@163.com"));
		        intent.putExtra(Intent.EXTRA_SUBJECT, "来自中秋博饼软件的反馈意见");
		        intent.putExtra(Intent.EXTRA_TEXT, text);
		        startActivity(intent);
		        
		        finish();
			}
			
			
			break;

		default:
			break;
		}
		
	}
}
