package com.banker.more.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.banker.R;
import com.banker.framework.activity.FrameActivity;

public class MoreActivity extends FrameActivity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendFrameworkCenter(R.layout.activity_more);
		init();
	}

	private void init() {
		initTop();
		initView();
	}

	private void initView() {
		((LinearLayout) findViewById(R.id.linLay_update))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.linLay_useHelp))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.linLay_share))
				.setOnClickListener(this);

		((LinearLayout) findViewById(R.id.linlay_feedBack))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.linLay_grade))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.linLay_softwareRecommend))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.linLay_XiaMenBanker))
				.setOnClickListener(this);
	}

	private void initTop() {
		setTopTitle(getResources().getString(R.string.more));
		hideTopRightBtn();
		topBackBtnListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		// 返回
		case R.id.btn_topBack:
			finish();
			break;
		// 版本更新
		case R.id.linLay_update:
//			gotoDownWebsite("http://shouji.360tpcdn.com/140823/082a1e13a5d5f5bb901894827c9659c3/com.banker_1.apk");
			startMarket();
			break;
		// 使用帮助
		case R.id.linLay_useHelp:
			startActivity(HelpActivity.class);
			break;
		// 分享
		case R.id.linLay_share:
			showShare();
//			saveIcon();
			break;
		// 意见反馈
		case R.id.linlay_feedBack:
			startActivity(FeedBackActivity.class);
			break;
		// 评分
		case R.id.linLay_grade:
			startMarket();
			break;
		// 软件推荐
		case R.id.linLay_softwareRecommend:
			startActivity(RecommendActivity.class);
			break;
		// 厦门蚌壳
		case R.id.linLay_XiaMenBanker:
			gotoDownWebsite("http://www.banker888.com");
			break;

		default:
			break;
		}

	}
	
	//启动本机已安装的商店软件并跳到本应用的下载界面，可下载和评分
	private void startMarket() {
		Uri uri = Uri.parse("market://details?id="+getPackageName());  
		Intent intent = new Intent(Intent.ACTION_VIEW,uri);  
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		startActivity(intent); 
	}
	
	
	//跳转到网站
	private void gotoDownWebsite(String uriPath) {
		Uri uri = Uri.parse(uriPath);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}

	
}
