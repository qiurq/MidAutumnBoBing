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

public class LocalGameBobingActivityAdapter extends BaseAdapter {
	
	private ArrayList<Item> mDiceBeanList;
	
	private Context mContext;
	
	public LocalGameBobingActivityAdapter(Context context) {
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
			
			view = LayoutInflater.from(mContext).inflate(R.layout.local_game_list_item_vertical, null);
			
			_mViewHolder = new ViewHolder();
			_mViewHolder.txt_localGameListItemTitle = (TextView) view.findViewById(R.id.txt_localGameListItemTitle);
			_mViewHolder.imgV_localGameListItemDice1 = (ImageView) view.findViewById(R.id.imgV_localGameListItemDice1);
			_mViewHolder.imgV_localGameListItemDice2 = (ImageView) view.findViewById(R.id.imgV_localGameListItemDice2);
			_mViewHolder.imgV_localGameListItemDice3 = (ImageView) view.findViewById(R.id.imgV_localGameListItemDice3);
			_mViewHolder.imgV_localGameListItemDice4 = (ImageView) view.findViewById(R.id.imgV_localGameListItemDice4);
			_mViewHolder.imgV_localGameListItemDice5 = (ImageView) view.findViewById(R.id.imgV_localGameListItemDice5);
			_mViewHolder.imgV_localGameListItemDice6 = (ImageView) view.findViewById(R.id.imgV_localGameListItemDice6);
			
			view.setTag(_mViewHolder);

		}else {
			_mViewHolder = (ViewHolder) view.getTag();
		}
		Item _diceBean = (Item) getItem(position);
		_mViewHolder.txt_localGameListItemTitle.setText(_diceBean.title);
		_mViewHolder.imgV_localGameListItemDice1.setImageResource(_diceBean.diceIcon1);
		_mViewHolder.imgV_localGameListItemDice2.setImageResource(_diceBean.diceIcon2);
		_mViewHolder.imgV_localGameListItemDice3.setImageResource(_diceBean.diceIcon3);
		_mViewHolder.imgV_localGameListItemDice4.setImageResource(_diceBean.diceIcon4);
		_mViewHolder.imgV_localGameListItemDice5.setImageResource(_diceBean.diceIcon5);
		_mViewHolder.imgV_localGameListItemDice6.setImageResource(_diceBean.diceIcon6);
		return view;
	}
	
	private class ViewHolder {
		public TextView txt_localGameListItemTitle;
		public ImageView imgV_localGameListItemDice1;
		public ImageView imgV_localGameListItemDice2;
		public ImageView imgV_localGameListItemDice3;
		public ImageView imgV_localGameListItemDice4;
		public ImageView imgV_localGameListItemDice5;	
		public ImageView imgV_localGameListItemDice6;	
	}

}
