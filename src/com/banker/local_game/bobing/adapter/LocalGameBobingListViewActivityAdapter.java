package com.banker.local_game.bobing.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.banker.local_game.bean.Item;
import com.banker.R;

public class LocalGameBobingListViewActivityAdapter extends BaseAdapter {
	
	private ArrayList<Item> mDiceBeanList;
	
	private Context mContext;
	
	public LocalGameBobingListViewActivityAdapter(Context context) {
		mContext = context;
		
	}
	
	/** 初使化数据 **/
	public void initDatas(ArrayList<Item> diceBeanList) {
		mDiceBeanList = diceBeanList;
	}
	
	/** 添加更新数据 **/
	public void updateList(Item diceBean) {
		mDiceBeanList.add(diceBean);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mDiceBeanList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDiceBeanList.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder _mViewHolder;
		
		if(null == view){
			
			view = LayoutInflater.from(mContext).inflate(R.layout.local_game_list_item_horizontal, null);
			
			_mViewHolder = new ViewHolder();
			_mViewHolder.txt_localGameListItemActivityTitle = (TextView) view.findViewById(R.id.txt_localGameListItemActivityTitle);
			_mViewHolder.imgV_localGameListItemActivityDice1 = (ImageView) view.findViewById(R.id.imgV_localGameListItemActivityDice1);
			_mViewHolder.imgV_localGameListItemActivityDice2 = (ImageView) view.findViewById(R.id.imgV_localGameListItemActivityDice2);
			_mViewHolder.imgV_localGameListItemActivityDice3 = (ImageView) view.findViewById(R.id.imgV_localGameListItemActivityDice3);
			_mViewHolder.imgV_localGameListItemActivityDice4 = (ImageView) view.findViewById(R.id.imgV_localGameListItemActivityDice4);
			_mViewHolder.imgV_localGameListItemActivityDice5 = (ImageView) view.findViewById(R.id.imgV_localGameListItemActivityDice5);
			_mViewHolder.imgV_localGameListItemActivityDice6 = (ImageView) view.findViewById(R.id.imgV_localGameListItemActivityDice6);
			view.setTag(_mViewHolder);

		}else {
			_mViewHolder = (ViewHolder) view.getTag();
		}
		Item _diceBean = (Item) getItem(position);
		_mViewHolder.txt_localGameListItemActivityTitle.setText(_diceBean.title);
		_mViewHolder.imgV_localGameListItemActivityDice1.setImageResource(_diceBean.diceIcon1);
		_mViewHolder.imgV_localGameListItemActivityDice2.setImageResource(_diceBean.diceIcon2);
		_mViewHolder.imgV_localGameListItemActivityDice3.setImageResource(_diceBean.diceIcon3);
		_mViewHolder.imgV_localGameListItemActivityDice4.setImageResource(_diceBean.diceIcon4);
		_mViewHolder.imgV_localGameListItemActivityDice5.setImageResource(_diceBean.diceIcon5);
		_mViewHolder.imgV_localGameListItemActivityDice6.setImageResource(_diceBean.diceIcon6);
		return view;
	}
	
	private class ViewHolder {
		public TextView txt_localGameListItemActivityTitle;
		public ImageView imgV_localGameListItemActivityDice1;
		public ImageView imgV_localGameListItemActivityDice2;
		public ImageView imgV_localGameListItemActivityDice3;
		public ImageView imgV_localGameListItemActivityDice4;
		public ImageView imgV_localGameListItemActivityDice5;	
		public ImageView imgV_localGameListItemActivityDice6;	
	}

}
