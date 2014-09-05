package com.banker.local_game.dice.activity;

import java.util.ArrayList;
import java.util.Random;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.banker.R;
import com.banker.framework.activity.FrameActivity;
import com.banker.framework.utils.HorizontalListView;
import com.banker.framework.utils.ShakeListener;
import com.banker.framework.utils.ShakeListener.OnShakeListener;
import com.banker.framework.views.Dice;
import com.banker.framework.views.down_menu.ActionItem;
import com.banker.framework.views.down_menu.TitlePopup;
import com.banker.framework.views.down_menu.TitlePopup.OnItemOnClickListener;
import com.banker.local_game.bean.Item;
import com.banker.local_game.dice.adapter.LocalGameActivityAdapter;
import com.banker.more.activity.MoreActivity;

public class LocalGameDiceActivity extends FrameActivity implements
		OnShakeListener, OnClickListener, OnItemClickListener{

	public static final String TAG = "MainActivity";

	private static int num;

	/** 大色子数组 **/
	public static final int[] DICE_IMG = { R.drawable.white_dice_1,
			R.drawable.white_dice_2, R.drawable.white_dice_3,
			R.drawable.white_dice_4, R.drawable.white_dice_5,
			R.drawable.white_dice_6 };

	/** 小色子数组 **/
	public static final int[] DICES = { R.drawable.dice_1, R.drawable.dice_2,
			R.drawable.dice_3, R.drawable.dice_4, R.drawable.dice_5,
			R.drawable.dice_6 };

	/** layout数组 **/
	private int[] layout = { R.id.l1, R.id.l2, R.id.l3, R.id.l4, R.id.l5 };

	/** 中间要显示的色子数组 **/
	private int[] centerDice = { R.id.img_centerDice1, R.id.img_centerDice2,
			R.id.img_centerDice3, R.id.img_centerDice4, R.id.img_centerDice5 };

	/** 摇晃手机类 **/
	private ShakeListener shakeListener;

	private boolean screen;// 是否在本应用
	private boolean isCup;// 盖子是否在
	private boolean isStart;// 是否开始
	private boolean shake;// 摇晃状态

	/** 顶部的ListView **/
	private HorizontalListView localGameListV;

	/** 色钟 **/
	private ImageView img_cup;
	
	/** 摇晃图 **/
	private ImageView img_shake;

	/** 开始 **/
	private Button btn_start;

	private LinearLayout lay_centerDice;

	/** 用于存放每生成一个色子出来的ID **/
	private ArrayList<Integer> diceIdList;

	/** adapter **/
	private LocalGameActivityAdapter mAdapter;

	/** 用于发送到ListViewActivity的listView **/
	private ArrayList<Item> toListViewActivityList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		appendFrameworkCenter(R.layout.activity_local_game_dice_main);
		init();

	}

	private void init() {
		initView();
		initVariable();
		initDiceLayout();
	}

	private LinearLayout[] diceLayout = new LinearLayout[5];

	private void initDiceLayout() {
		for (int i = 0; i < diceLayout.length; i++) {
			LinearLayout l = (LinearLayout) findViewById(layout[i]);
			diceLayout[i] = l;
		}

		switch (num) {
		case 1: {
			LinearLayout lay_dice456 = (LinearLayout) findViewById(R.id.lay_dice456);
			lay_dice456.setVisibility(View.GONE);
			diceLayout[0].setVisibility(View.VISIBLE);
			break;
		}
		case 2: {
			LinearLayout lay_dice456 = (LinearLayout) findViewById(R.id.lay_dice456);
			lay_dice456.setVisibility(View.GONE);
			diceLayout[0].setVisibility(View.VISIBLE);
			diceLayout[1].setVisibility(View.VISIBLE);
			break;
		}

		case 3: {
			LinearLayout lay_dice456 = (LinearLayout) findViewById(R.id.lay_dice456);
			lay_dice456.setVisibility(View.GONE);
			diceLayout[0].setVisibility(View.VISIBLE);
			diceLayout[1].setVisibility(View.VISIBLE);
			diceLayout[2].setVisibility(View.VISIBLE);
			break;
		}
		case 4:
			diceLayout[0].setVisibility(View.VISIBLE);
			diceLayout[1].setVisibility(View.VISIBLE);
			diceLayout[2].setVisibility(View.VISIBLE);
			diceLayout[3].setVisibility(View.VISIBLE);
			break;
		case 5:
			diceLayout[0].setVisibility(View.VISIBLE);
			diceLayout[1].setVisibility(View.VISIBLE);
			diceLayout[2].setVisibility(View.VISIBLE);
			diceLayout[3].setVisibility(View.VISIBLE);
			diceLayout[4].setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	private void initVariable() {

		num = getIntent().getExtras().getInt("diceNum");

		isStart = false;
		screen = true;
		diceIdList = new ArrayList<Integer>();
		toListViewActivityList = new ArrayList<Item>();

		shakeListener = new ShakeListener(getApplicationContext());
		shakeListener.setOnShakeListener(this);

	}

	private void initView() {
		
		setTopTitle(getResources().getString(R.string.shakeDice));
		
		setTopRightBtnText(getResources().getString(R.string.more));
		topBackBtnListener(this);
		topRightBtnListener(this);

		lay_centerDice = (LinearLayout) findViewById(R.id.lay_centerDice);
		lay_centerDice.setVisibility(View.INVISIBLE);

		FrameLayout lay_diceBase = (FrameLayout) findViewById(R.id.lay_diceBase);
		lay_diceBase.setOnClickListener(this);

		btn_start = (Button) findViewById(R.id.btn_start);
		btn_start.setOnClickListener(this);

		img_cup = (ImageView) findViewById(R.id.img_cup);
		img_cup.setOnClickListener(this);
		
		img_shake = (ImageView) findViewById(R.id.imgV_shake);

		mAdapter = new LocalGameActivityAdapter(this);
		mAdapter.initDatas(new ArrayList<Item>());
		localGameListV = (HorizontalListView) findViewById(R.id.localGameListV);
		localGameListV.setAdapter(mAdapter);
		localGameListV.setOnItemClickListener(this);

		ImageView close_btn_src_normal = (ImageView) findViewById(R.id.close_btn_src_normal);
		close_btn_src_normal.setOnClickListener(this);

		hideCenterDice();

	}

	public static void setNum(int diceNum) {
		num = diceNum;
	}

	private int[] diceIdArray = new int[6];

	/** 生成色子 **/
	private void addDice() {
		startSound();
		startVibrator();
		hideCenterDice();

		Random random = new Random();
		Bitmap bitmap = null;
		diceIdList.clear();

		for (int i = 0; i < num; i++) {
			int id = random.nextInt(6);
			diceIdList.add(id);
			diceIdArray[i] = id;
			bitmap = BitmapFactory.decodeResource(getResources(), DICE_IMG[id]);

			LinearLayout l = diceLayout[i];
			l.removeAllViews();
			Dice dice = new Dice(this, l.getWidth(), l.getHeight(), bitmap);
			l.addView(dice);

		}

	}

	/** 隐藏中间的色子 **/
	private void hideCenterDice() {
		for (int i = 0; i < num; i++) {
			ImageView imgV = (ImageView) findViewById(centerDice[i]);
			imgV.setVisibility(View.GONE);
		}
	}

	/** 显示顶部和中间的色子 **/
	private void showTopCenterDice() {

		if (true == shake) {

			addListItem();

			// 显示中间的色子
			for (int i = 0; i < num; i++) {
				ImageView imgV = (ImageView) findViewById(centerDice[i]);
				imgV.setImageResource(DICE_IMG[diceIdList.get(i)]);
				imgV.setVisibility(View.VISIBLE);
			}
		}
		img_cup.setVisibility(View.INVISIBLE);
	}

	/** 音效 **/
	private void startSound() {
		MediaPlayer mp = MediaPlayer.create(getApplicationContext(),
				R.raw.sound_dice);
		mp.start();
	}

	private int count = 0;

	/** 添加、更新listView **/
	private void addListItem() {
		count++;
		Item _bean = new Item();
		_bean.title = String.valueOf(count);

		Item _nextBean = new Item();
		_nextBean.title = String.valueOf(count);

		switch (num) {
		case 1: {
			_bean.diceIcon1 = DICES[diceIdArray[0]];
			_nextBean.diceIcon1 = DICE_IMG[diceIdArray[0]];
			break;
		}
		case 2: {
			_bean.diceIcon1 = DICES[diceIdArray[0]];
			_bean.diceIcon2 = DICES[diceIdArray[1]];

			_nextBean.diceIcon1 = DICE_IMG[diceIdArray[0]];
			_nextBean.diceIcon2 = DICE_IMG[diceIdArray[1]];
			break;
		}
		case 3: {
			_bean.diceIcon1 = DICES[diceIdArray[0]];
			_bean.diceIcon2 = DICES[diceIdArray[1]];
			_bean.diceIcon3 = DICES[diceIdArray[2]];

			_nextBean.diceIcon1 = DICE_IMG[diceIdArray[0]];
			_nextBean.diceIcon2 = DICE_IMG[diceIdArray[1]];
			_nextBean.diceIcon3 = DICE_IMG[diceIdArray[2]];
			break;
		}
		case 4: {
			_bean.diceIcon1 = DICES[diceIdArray[0]];
			_bean.diceIcon2 = DICES[diceIdArray[1]];
			_bean.diceIcon3 = DICES[diceIdArray[2]];
			_bean.diceIcon4 = DICES[diceIdArray[3]];

			_nextBean.diceIcon1 = DICE_IMG[diceIdArray[0]];
			_nextBean.diceIcon2 = DICE_IMG[diceIdArray[1]];
			_nextBean.diceIcon3 = DICE_IMG[diceIdArray[2]];
			_nextBean.diceIcon4 = DICE_IMG[diceIdArray[3]];
			break;
		}
		case 5: {
			_bean.diceIcon1 = DICES[diceIdArray[0]];
			_bean.diceIcon2 = DICES[diceIdArray[1]];
			_bean.diceIcon3 = DICES[diceIdArray[2]];
			_bean.diceIcon4 = DICES[diceIdArray[3]];
			_bean.diceIcon5 = DICES[diceIdArray[4]];

			_nextBean.diceIcon1 = DICE_IMG[diceIdArray[0]];
			_nextBean.diceIcon2 = DICE_IMG[diceIdArray[1]];
			_nextBean.diceIcon3 = DICE_IMG[diceIdArray[2]];
			_nextBean.diceIcon4 = DICE_IMG[diceIdArray[3]];
			_nextBean.diceIcon5 = DICE_IMG[diceIdArray[4]];
			break;
		}

		default:
			break;
		}

		mAdapter.updateList(_bean);
		toListViewActivityList.add(_nextBean);
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		// 打开盖子
		case R.id.img_cup: {
			if (btn_start.getText().equals("开始")) {
				Toast.makeText(this, "请开始...", Toast.LENGTH_SHORT).show();
				break;
			}

			if (isStart) {
				showTopCenterDice();
			} else {
				img_cup.setVisibility(View.INVISIBLE);
			}

			isStart = false;
			lay_centerDice.setVisibility(View.VISIBLE);
			btn_start.setText("继续");
			img_shake.setVisibility(View.GONE);
			btn_start.setVisibility(View.VISIBLE);
			break;
		}
		// 盖上盖子
		case R.id.lay_diceBase: {
			img_cup.setVisibility(View.VISIBLE);
			lay_centerDice.setVisibility(View.INVISIBLE);
			break;
		}
		// 开始/继续
		case R.id.btn_start: {
			lay_centerDice.setVisibility(View.INVISIBLE);
			img_cup.setVisibility(View.VISIBLE);
			img_shake.setVisibility(View.VISIBLE);
			btn_start.setVisibility(View.GONE);
			isCup = false;
			isStart = true;
			shake = false;
			break;
		}
		// 顶部ListView
		case R.id.lay_listV: {
			Intent _intent = new Intent(this, LocalGameListViewActivity.class);
			_intent.putExtra("toListViewActivityList", toListViewActivityList);
			startActivity(_intent);
			break;
		}

		case R.id.close_btn_src_normal: {
			((RelativeLayout) findViewById(R.id.lay_bottom_advertisement)).setVisibility(View.GONE);
			break;
		}
		case R.id.btn_topBack:
			if(toListViewActivityList.isEmpty()) {
				finish();
			}else {
					dialog();
					txtV_dialogText.setText(getResources().getString(R.string.backMessage));
					txtV_dialogText.setTextSize(20);
			}
			break;
			
		case R.id.btn_topRight:

			
//			显示下垃框
			TitlePopup popup = new TitlePopup(this, view.getWidth()+50, LayoutParams.WRAP_CONTENT);
			//给标题栏弹窗添加子类
			popup.addAction(new ActionItem(null, useStringTypeResource(R.string.resultList)));
			popup.addAction(new ActionItem(null, useStringTypeResource(R.string.share)));
			popup.addAction(new ActionItem(null, useStringTypeResource(R.string.more)));
			popup.show(view);
			
			popup.setItemOnClickListener(new OnItemOnClickListener() {
				
				@Override
				public void onItemClick(ActionItem item, int position) {
					
					switch (position) {
//					战绩
					case 0:
						Intent _intent = new Intent(LocalGameDiceActivity.this, LocalGameListViewActivity.class);
						_intent.putExtra("toListViewActivityList", toListViewActivityList);
						startActivity(_intent);
						break;
//					分享
					case 1:
						showShare();
						break;
//					更多	
					case 2:
						LocalGameDiceActivity.this.startActivity(MoreActivity.class);
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

	/** 摇晃手机 **/
	@Override
	public void onShake() {
		shake = true;

		if (screen && isStart) {
			img_cup.setVisibility(View.VISIBLE);
			isCup = false;
			isStart = true;
			addDice();
			btn_start.setText("继续");
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent _intent = new Intent(this, LocalGameListViewActivity.class);
		_intent.putExtra("toListViewActivityList", toListViewActivityList);
		startActivity(_intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			return super.onKeyDown(keyCode, event);

		case KeyEvent.KEYCODE_VOLUME_UP:
			return super.onKeyDown(keyCode, event);
			
		case KeyEvent.KEYCODE_BACK:
			if(toListViewActivityList.isEmpty()) {
				finish();
			}else {
					dialog();
					txtV_dialogText.setText(getResources().getString(R.string.backMessage));
					txtV_dialogText.setTextSize(20);
					return true;
			}
			break;
		default:
			break;
		}

//		// 继续执行父类的其他点击事件
		return super.onKeyDown(keyCode, event);
	}
	
	private TextView txtV_dialogText;	
	private void dialog() {
		View _dialogView = inflateView(R.layout.dialog);
		final Dialog _alertDialog = new AlertDialog.Builder(this).create();
		_alertDialog.show();
		
		Window window = _alertDialog.getWindow();
		_alertDialog.setContentView(_dialogView);
		txtV_dialogText = (TextView) _dialogView.findViewById(R.id.txtV_dialogText);
		Button btn_dialogConfirm = (Button) _dialogView.findViewById(R.id.btn_dialogConfirm);
		btn_dialogConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(getResources().getString(R.string.backMessage).equals(txtV_dialogText.getText().toString())) {
					shakeListener.stop();
					LocalGameDiceActivity.this.finish();
				}else {
					_alertDialog.dismiss();
				}
			}
		});
		
		Button btn_dialogCancel = (Button) _dialogView.findViewById(R.id.btn_dialogCancel);
		btn_dialogCancel.setVisibility(View.VISIBLE);
		btn_dialogCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				_alertDialog.dismiss();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		screen = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		screen = true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		screen = false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		screen = false;
	}

}