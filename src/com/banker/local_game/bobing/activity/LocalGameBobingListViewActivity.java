package com.banker.local_game.bobing.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.banker.framework.activity.FrameActivity;
import com.banker.local_game.bean.Item;
import com.banker.local_game.dice.adapter.LocalGameListViewActivityAdapter;
import com.banker.R;

public class LocalGameBobingListViewActivity extends FrameActivity implements OnClickListener {
	
	
	private ArrayList<Item>  adapterDatas;
	
	private ListView listViewActivity_listV;
	
	private LocalGameListViewActivityAdapter listViewActivityAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_local_game_result_list);
		
		appendFrameworkCenter(R.layout.activity_local_game_result_list);
		
		init();
	}

	private void init() {

		initDatas();
		initView();
		
	}

	@SuppressWarnings("unchecked")
	private void initDatas() {
		adapterDatas = (ArrayList<Item>) getIntent().getSerializableExtra("toListViewActivityList");
	}

	private void initView() {
//		((Button) findViewById(R.id.btn_topBack)).setOnClickListener(this);
//		Button btn_topRight = (Button) findViewById(R.id.btn_topRight);
//		btn_topRight.setVisibility(View.GONE);
		listViewActivity_listV = (ListView) findViewById(R.id.listViewActivity_listV);
		listViewActivityAdapter = new LocalGameListViewActivityAdapter(this);
		listViewActivityAdapter.initDatas(adapterDatas);	
		listViewActivity_listV.setAdapter(listViewActivityAdapter);
		
		topBackBtnListener(this);
		hideTopRightBtn();
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_topBack:
			finish();
			break;
		default:
			break;
		}
	}
}
