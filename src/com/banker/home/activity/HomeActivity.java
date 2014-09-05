package com.banker.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.banker.framework.activity.FrameActivity;
import com.banker.local_game.bobing.activity.LocalGameBobingActivity;
import com.banker.local_game.dice.activity.LocalGameDiceActivity;
import com.banker.more.activity.HelpActivity;
import com.banker.more.activity.MoreActivity;
import com.banker.R;

public class HomeActivity extends FrameActivity implements OnClickListener, OnCheckedChangeListener{
	
	private static final String TAG = "HomeActivity";
	
	private int diceNum = 0;
	
	private RadioGroup lay_RadioButtonCount;
	private RadioButton radioButton_1;
	private RadioButton radioButton_2;
	private RadioButton radioButton_3;
	private RadioButton radioButton_4;
	private RadioButton radioButton_5;
	
	private RadioGroup radioGroup_type;
	private RadioButton radioButton_dice;
	private RadioButton radioButton_bobing;
	
	private boolean bobing = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		appendFrameworkCenter(R.layout.activity_home);
		init();
	}

	private void init() {
		
//		((Button) findViewById(R.id.btn_createRoom)).setOnClickListener(this);	
//		((Button) findViewById(R.id.btn_intoRoom)).setOnClickListener(this);		
		((Button) findViewById(R.id.btn_localGame)).setOnClickListener(this);
		((Button) findViewById(R.id.btn_more)).setOnClickListener(this);
//		topRightBtnListener(this);
		hideTopRightBtn();
		hideTopBackBtn();
		
		radioButton_1 = (RadioButton) findViewById(R.id.RadioButton_1);		
		radioButton_2 = (RadioButton) findViewById(R.id.RadioButton_2);	
		radioButton_3 = (RadioButton) findViewById(R.id.RadioButton_3);		
		radioButton_4 = (RadioButton) findViewById(R.id.RadioButton_4);		
		radioButton_5 = (RadioButton) findViewById(R.id.RadioButton_5);

		radioButton_bobing = (RadioButton) findViewById(R.id.RadioButton_bobing);
		radioButton_dice = (RadioButton) findViewById(R.id.RadioButton_dice);		
		radioButton_dice.setOnClickListener(this);
		
		lay_RadioButtonCount = (RadioGroup) findViewById(R.id.lay_RadioButtonCount);
		lay_RadioButtonCount.setOnCheckedChangeListener(this);
		lay_RadioButtonCount.check(R.id.RadioButton_5);
		
		radioGroup_type = (RadioGroup) findViewById(R.id.lay_RadioButtonType);
		radioGroup_type.setOnCheckedChangeListener(this);
		radioGroup_type.check(R.id.RadioButton_dice);

	}
	
	/** 启动并且传数据给下个Activity **/
	private void startActivityAndSendData(Class<?> activityClass) {
		Intent _intent = new Intent(this,activityClass);
		_intent.putExtra("diceNum", diceNum);
		startActivity(_intent);
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		
		//创建房间
		case R.id.btn_createRoom:{
			
			break;
			}
		//进入房间
		case R.id.btn_intoRoom:{
			
			break;
		}
		//单机游戏
		case R.id.btn_localGame:{
			
			if(diceNum <= 0) {
				showToast(R.string.selectDiceNum);
			}else {
				if(true==bobing) {
					startActivityAndSendData(LocalGameBobingActivity.class);
				}else {
					startActivityAndSendData(LocalGameDiceActivity.class);
				}	
			}
			
			break;
		}
		
		case R.id.btn_more:{
			startActivity(MoreActivity.class);
			break;
		}
		
		//帮助
		case R.id.btn_topRight:{
			startActivity(HelpActivity.class);
			break;
		}

		default:
			break;
		}
		
	}

	/** 单选按钮事件 **/
	@Override
	public void onCheckedChanged(RadioGroup group, int checkId) {
		
		Log.i(TAG, "...onCheckedChanged..." + String.valueOf(checkId).toString());
		
		switch (checkId) {
		
		case R.id.RadioButton_dice:
			bobing = false;
			diceNum = 5;
			radioButton_1.setClickable(true);
			radioButton_2.setClickable(true);
			radioButton_3.setClickable(true);
			radioButton_4.setClickable(true);
			radioButton_5.setClickable(true);
			
			radioButton_1.setChecked(true);
			radioButton_2.setChecked(true);
			radioButton_3.setChecked(true);
			radioButton_4.setChecked(true);
			radioButton_5.setChecked(true);	
			
			lay_RadioButtonCount.check(R.id.RadioButton_5);
			radioGroup_type.check(R.id.RadioButton_dice);
			
//			showToast(radioButton_dice.getText().toString());
			break; 
			
		case R.id.RadioButton_bobing:
				bobing = true;
				radioButton_1.setChecked(false);
				radioButton_2.setChecked(false);
				radioButton_3.setChecked(false);
				radioButton_4.setChecked(false);
				radioButton_5.setChecked(false);				
				
				radioButton_1.setClickable(false);
				radioButton_2.setClickable(false);
				radioButton_3.setClickable(false);
				radioButton_4.setClickable(false);
				radioButton_5.setClickable(false);
			
//			showToast(radioButton_bobing.getText().toString());
			break;
		
		case R.id.RadioButton_1:
			diceNum = 1;
//			showToast(radioButton_1.getText().toString());
			break;
			
		case R.id.RadioButton_2:
			diceNum = 2;
//			showToast(radioButton_2.getText().toString());
			break;
			
		case R.id.RadioButton_3:
			diceNum = 3;
//			showToast(radioButton_3.getText().toString());
			break;
			
		case R.id.RadioButton_4:
			diceNum = 4;
//			showToast(radioButton_4.getText().toString());
			break;
			
		case R.id.RadioButton_5:
			diceNum = 5;
//			showToast(radioButton_5.getText().toString());
			break;

		default:
			break;
		}
	}

}
