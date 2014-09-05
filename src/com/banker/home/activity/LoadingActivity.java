package com.banker.home.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;

import com.banker.framework.activity.FrameActivity;
import com.banker.R;

public class LoadingActivity extends FrameActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);

		
		final Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				LoadingActivity.this.finish();
				startActivity(HomeActivity.class);
				t.cancel();
			}
		}, 2*1000, 1*1000);
	}
}
