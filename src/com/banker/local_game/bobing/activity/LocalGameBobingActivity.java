package com.banker.local_game.bobing.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.banker.framework.activity.FrameActivity;
import com.banker.framework.utils.HorizontalListView;
import com.banker.framework.utils.ShakeListener;
import com.banker.framework.utils.ShakeListener.OnShakeListener;
import com.banker.framework.views.Dice;
import com.banker.framework.views.down_menu.ActionItem;
import com.banker.framework.views.down_menu.TitlePopup;
import com.banker.framework.views.down_menu.TitlePopup.OnItemOnClickListener;
import com.banker.local_game.bean.Item;
import com.banker.local_game.bobing.adapter.LocalGameBobingActivityAdapter;
import com.banker.more.activity.HelpActivity;
import com.banker.more.activity.MoreActivity;
import com.banker.R;

public class LocalGameBobingActivity extends FrameActivity implements OnClickListener, OnItemClickListener, OnShakeListener{
	
	private final int[] layArray = {R.id.lay_dice1,R.id.lay_dice2,R.id.lay_dice3,R.id.lay_dice4,R.id.lay_dice5,R.id.lay_dice6,};
	private final int[] iconArray = {R.drawable.value1,R.drawable.value2,R.drawable.value3,R.drawable.value4,R.drawable.value5,R.drawable.value6,};
	/** 小色子数组 **/
	public static final int[] DICES = { R.drawable.dice_1, R.drawable.dice_2,R.drawable.dice_3, R.drawable.dice_4, R.drawable.dice_5,R.drawable.dice_6 };
	/** 大色子数组 **/
	public static final int[] DICE_IMG = { R.drawable.white_dice_1,R.drawable.white_dice_2, R.drawable.white_dice_3,R.drawable.white_dice_4, R.drawable.white_dice_5,R.drawable.white_dice_6 };
	/** 骰子计数器 **/
	private int[] result = {0,0,0,0,0,0};
	/** 骰子动画区域 **/
	private ImageView img_diceAnimationArea;
	private ImageView img_close;
	private ImageView img_shake;
	private Button btn_start;
	/** 音效 **/
	private MediaPlayer mp;
	/** 摇晃手机类 **/
	private ShakeListener shakeListener;
	/** 横向的ListView **/
	private HorizontalListView localGameListV;
	private LocalGameBobingActivityAdapter mAdapter;
	
	/** 用于发送到BobingListViewActivity的listView **/
	private ArrayList<Item> toListViewActivityList;
	/** 开始状态 **/
	private boolean start;
	/** 摇晃状态 **/
	private boolean shake;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		appendFrameworkCenter(R.layout.activity_local_game_bobing_main);
		init();
	}

	private void init() {	
		initView();
		initVariable();		
	}

	private void initVariable() {
		shakeListener = new ShakeListener(this);
		shakeListener.setOnShakeListener(this);
		toListViewActivityList = new ArrayList<Item>();		
		diceIdList = new ArrayList<Integer>();	
		start = false;
		shake = false;
	}

	private void initView() {
		
		setTopTitle(getResources().getString(R.string.bobing));
		
		img_close = (ImageView) findViewById(R.id.img_close);
		img_close.setOnClickListener(this);	
		img_diceAnimationArea = (ImageView) findViewById(R.id.img_diceAnimationArea);
		img_shake = (ImageView) findViewById(R.id.img_shake);

		btn_start = (Button) findViewById(R.id.btn_start);
		btn_start.setOnClickListener(this);
		
		topBackBtnListener(this);
		topRightBtnListener(this);
		setTopRightBtnText(getResources().getString(R.string.more));
		
		mAdapter = new LocalGameBobingActivityAdapter(this);
		mAdapter.initDatas(new ArrayList<Item>());
		localGameListV = (HorizontalListView) findViewById(R.id.localGameListV);
		localGameListV.setAdapter(mAdapter);
		localGameListV.setOnItemClickListener(this);
	}
	
	
	
	/** 开始博饼 **/
	private void startBobing() {
		if(false == start) {
			startVibrator();
			diceAnimation();
			hideDice();
			startMP();
			//定时器任务
			TimerTask task = new TimerTask() {		
				@Override
				public void run() {					
					mHandler.sendEmptyMessage(1);
				}
			};
			//新建一个定时器并安排定时器任务
			Timer t = new Timer();
			t.schedule(task, 1*1000);
			start = true;
			shake = true;
		}
	}

	@Override
	public void onClick(View view) {	
		switch (view.getId()) {
		case R.id.img_close:
			RelativeLayout r = (RelativeLayout) findViewById(R.id.lay_addAdvertisement);
			r.setVisibility(View.GONE);
			break;
		case R.id.btn_start:
			startBobing();
			break;
		case R.id.btn_topBack:
			if(toListViewActivityList.isEmpty()) {
				finish();
			}else {
					dialog(Gravity.CENTER);
					txtV_dialogText.setText(getResources().getString(R.string.backMessage));
					txtV_dialogText.setTextSize(20);
			}
			break;
		case R.id.btn_topRight:
//			gotoNextActivity();
//			显示下垃框
			TitlePopup popup = new TitlePopup(this, view.getWidth()+50, LayoutParams.WRAP_CONTENT);
			//给标题栏弹窗添加子类
			popup.addAction(new ActionItem(null, useStringTypeResource(R.string.resultList)));
			popup.addAction(new ActionItem(null, useStringTypeResource(R.string.share)));
			popup.addAction(new ActionItem(null, useStringTypeResource(R.string.help)));
			popup.addAction(new ActionItem(null, useStringTypeResource(R.string.more)));
			popup.show(view);
			
			popup.setItemOnClickListener(new OnItemOnClickListener() {
				
				@Override
				public void onItemClick(ActionItem item, int position) {
					
					switch (position) {
//					战绩
					case 0:
						gotoNextActivity();
						break;
//					分享
					case 1:
						showShare();
						break;
//					帮助
					case 2:
						LocalGameBobingActivity.this.startActivity(HelpActivity.class);
						break;
//					更多	
					case 3:
						LocalGameBobingActivity.this.startActivity(MoreActivity.class);
						break;

					default:
						break;
					}
					
				}
			});
			
			break;
		default:
			break;
		}
	}
	
	/** 摇晃手机事件 **/
	@Override
	public void onShake() {
		if(false == shake) {
			startBobing();
		}
	}
	
	/** 开始音效 **/
	private void startMP() {
		mp = MediaPlayer.create(this, R.raw.bobing);
		mp.setLooping(true);
		mp.start();	
	}
	
	/** 停止音效 **/
	private void stopMP() {
			mp.stop();
			mp.release();	
	}

	/** 色子动画 **/
	private void diceAnimation() {
		img_diceAnimationArea.setBackgroundResource(R.anim.frame_animation);
		img_diceAnimationArea.setVisibility(View.VISIBLE);
		AnimationDrawable animationDrawable = (AnimationDrawable) img_diceAnimationArea.getBackground();
		animationDrawable.start();
	}
	
	/** 色子的结果 **/
	private void resultDice() {
		diceIdList.clear();
		Bitmap bitmap;
		Random random = new Random();
		for(int i=0; i<layArray.length; i++) {
			int id = random.nextInt(6);
			bitmap = BitmapFactory.decodeResource(getResources(),iconArray[id]);	
			LinearLayout l = (LinearLayout) findViewById(layArray[i]);
			l.removeAllViews();
			l.addView(new Dice(this, l.getWidth(), l.getHeight(), bitmap));
			l.setVisibility(View.VISIBLE);
			result(id);
			diceIdList.add(id);
		}
//		img_shake.setVisibility(View.INVISIBLE);
		dialog(Gravity.BOTTOM);
		showResult();
		addTopList();
		shake = true;
	}
	
	private TextView txtV_dialogText;	
	private void dialog(int gravity) {
		shake = true;
		View _dialogView = inflateView(R.layout.dialog);
		final AlertDialog _alertDialog = new AlertDialog.Builder(this).create();
		_alertDialog.show();	
		
		Window window = _alertDialog.getWindow();
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		window.setGravity(gravity);
		
		_alertDialog.setContentView(_dialogView);
		_alertDialog.setCanceledOnTouchOutside(false);
		
		setAlertDialogClosable(_alertDialog, false);
		
		txtV_dialogText = (TextView) _dialogView.findViewById(R.id.txtV_dialogText);
		Button btn_dialogCancel = (Button) _dialogView.findViewById(R.id.btn_dialogCancel);
		if(Gravity.CENTER == gravity) {
			btn_dialogCancel.setVisibility(View.VISIBLE);
		}
		btn_dialogCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				setAlertDialogClosable(_alertDialog, true);
				_alertDialog.dismiss();
				shake = false;
			}
		});
		
		Button btn_dialogConfirm = (Button) _dialogView.findViewById(R.id.btn_dialogConfirm);
		btn_dialogConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(getResources().getString(R.string.backMessage).equals(txtV_dialogText.getText().toString())) {
					shakeListener.stop();
					LocalGameBobingActivity.this.finish();
				}else {
					shake = false;
					setAlertDialogClosable(_alertDialog, true);
					_alertDialog.dismiss();
				}
			}
		});
	}

	private String title = "没有";
	private ArrayList<Integer> diceIdList;
	/** 添加、更新顶部List **/
	private void addTopList() {

		Item _bean = new Item();
		_bean.title = " " + title + " ";
		_bean.diceIcon1 = DICES[diceIdList.get(0)];
		_bean.diceIcon2 = DICES[diceIdList.get(1)];
		_bean.diceIcon3 = DICES[diceIdList.get(2)];
		_bean.diceIcon4 = DICES[diceIdList.get(3)];
		_bean.diceIcon5 = DICES[diceIdList.get(4)];
		_bean.diceIcon6 = DICES[diceIdList.get(5)];
		
		Item _nextBean = new Item();
		_nextBean.title = " " + title + " ";
		_nextBean.diceIcon1 = DICE_IMG[diceIdList.get(0)];
		_nextBean.diceIcon2 = DICE_IMG[diceIdList.get(1)];
		_nextBean.diceIcon3 = DICE_IMG[diceIdList.get(2)];
		_nextBean.diceIcon4 = DICE_IMG[diceIdList.get(3)];
		_nextBean.diceIcon5 = DICE_IMG[diceIdList.get(4)];
		_nextBean.diceIcon5 = DICE_IMG[diceIdList.get(5)];

		mAdapter.updateList(_bean);
		toListViewActivityList.add(_nextBean);
		title = "没有";
		
//		localGameListV.setVisibility(View.INVISIBLE);
//		addListView();
	}
	private void addListView() {
//		HorizontalScrollView lay_horizontalList = (HorizontalScrollView) findViewById(R.id.lay_horizontalList);
		
		LinearLayout layout = new LinearLayout(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setLayoutDirection(LinearLayout.VERTICAL);
		layout.setLayoutParams(params);
		
		TextView title = new TextView(this);
		title.setText("AAAA");
		layout.addView(title, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		ImageView dice1 = new ImageView(this);
		dice1.setImageResource(diceIdList.get(0));
		layout.addView(dice1, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		ImageView dice2 = new ImageView(this);
		dice1.setImageResource(diceIdList.get(1));
		layout.addView(dice2, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		ImageView dice3 = new ImageView(this);
		dice1.setImageResource(diceIdList.get(2));
		layout.addView(dice3, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		ImageView dice4 = new ImageView(this);
		dice1.setImageResource(diceIdList.get(3));
		layout.addView(dice4, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		ImageView dice5 = new ImageView(this);
		dice1.setImageResource(diceIdList.get(4));
		layout.addView(dice5, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		ImageView dice6 = new ImageView(this);
		dice1.setImageResource(diceIdList.get(5));
		layout.addView(dice6, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
//		lay_horizontalList.addView(layout);
	}
	
	/** 显示结果 **/
	private void showResult() {	
//		int[] result = {0,4,0,0,0,0};//用来测试逻辑		
		if(result[3] == 6) {
			txtV_dialogText.setText("六杯红");
			title = "六杯红";
		}else if(result[3] == 5) {
			txtV_dialogText.setText("五红");
			title = "五红";
		}else if(result[3] == 4) {
			if(result[0] == 2 && result[1] == 0 && result[2] == 0 && result[4] == 0 && result[5] == 0){
				txtV_dialogText.setText("状元插金花");
				title = "状元插金花";
			}
			else {
				txtV_dialogText.setText("四点红");
				title = "四点红";
			}
		}else if(result[3] == 3) {
			txtV_dialogText.setText("三红");
			title = "三红";
		}else  if(result[0] == 6) {
			txtV_dialogText.setText("遍地锦");
			title = "遍地锦";
		}else if(result[1] == 6 || result[2] == 6 || result[4] == 6 || result[5] == 6) {
			txtV_dialogText.setText("黑六勃");
			title = "黑六勃";
		}else  if(result[0] == 5 || result[1] == 5 || result[2] == 5 || result[4] == 5 || result[5] == 5) {
			if(result[3] == 1) {
				txtV_dialogText.setText("五子带一秀");
				title = "五子带一秀";
			}else {
				txtV_dialogText.setText("五子登科");
				title = "五子登科";
			}
		} else if(result[0]==1 && result[1]==1 && result[2]==1 && result[3]==1 && result[4]==1 && result[5]==1) {
			txtV_dialogText.setText("对堂"); 
			title = "对堂";
		}else if(result[0] == 4 || result[1] == 4 || result[2] == 4 || result[4] == 4 || result[5] == 4) {
			txtV_dialogText.setText("四进");
			title = "四进";
		}else if(result[3] == 2 && (result[0] != 4 || result[1] != 4 || result[2] != 4 || result[4] != 4 || result[5] != 4)) {
			txtV_dialogText.setText("二举");
			title = "二举";
		}else if(result[3] == 1 && (result[0] != 4 || result[1] != 4 || result[2] != 4 || result[4] != 4 || result[5] != 4)) {
			txtV_dialogText.setText("一秀");
			title = "一秀";
		}else {
			txtV_dialogText.setText("什么也没有！");
		}
		//复位骰子计数器
		for(int i=0; i<result.length; i++) {
			result[i] = 0;
		}
	}
	
	/** 每随机生成一个骰子,相对应的骰子计数器里相对应的骰子计数就加 1 **/
	private void result(int id) {
		switch (id) {
		case 0: result[0]++; break;
		case 1: result[1]++; break;
		case 2: result[2]++; break;
		case 3: result[3]++; break;
		case 4: result[4]++; break;
		case 5: result[5]++; break;
		default:
			break;
		}
	}
	
	/** 隐藏色子 **/
	private void hideDice() {
		for(int i=0; i<layArray.length; i++) {			
				LinearLayout l = (LinearLayout) findViewById(layArray[i]);
				l.setVisibility(View.INVISIBLE);		
			}
	}
	
	/** 跳转到结果列表Activity **/
	private void gotoNextActivity() {
		Intent _intent = new Intent(this, LocalGameBobingListViewActivity.class);
		_intent.putExtra("toListViewActivityList", toListViewActivityList);
		startActivity(_intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		gotoNextActivity();
	}
	
	private MyHandler mHandler = new MyHandler(this);
	private class MyHandler extends Handler{
		private WeakReference<LocalGameBobingActivity> weakReference;
		public MyHandler(LocalGameBobingActivity activity) {
			weakReference = new WeakReference<LocalGameBobingActivity>(activity);
		}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			LocalGameBobingActivity mActivity = weakReference.get();
			if( null != mActivity) {
				if(msg.what == 1) {
					img_diceAnimationArea.setVisibility(View.GONE);
					resultDice();
					stopMP();
					start = false;
				}
			}
		}		
	}

	/** 返回键事件 **/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			return super.onKeyDown(keyCode, event);
//			break;

		case KeyEvent.KEYCODE_VOLUME_UP:
			return super.onKeyDown(keyCode, event);
//			break;
			
		case KeyEvent.KEYCODE_BACK:
			if(toListViewActivityList.isEmpty()) {
				finish();
			}else {
//				outAlertDialog();
				dialog(Gravity.CENTER);
				txtV_dialogText.setText(getResources().getString(R.string.backMessage));
				txtV_dialogText.setTextSize(20);
				return true;
			}
			break;
		default:
			break;
		}
		
		// 继续执行父类的其他点击事件
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		shakeListener.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		shakeListener.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		shakeListener.stop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		shakeListener.stop();
	}

}
