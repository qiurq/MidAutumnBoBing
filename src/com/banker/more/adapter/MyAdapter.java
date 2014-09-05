package com.banker.more.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.banker.R;
import com.banker.more.bean.Item;

public class MyAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Item> itemsList;
	
	public MyAdapter(Context context,ArrayList<Item> itemsList) {
		this.context = context;
		this.itemsList = itemsList;
	}
	@Override
	public int getCount() {
		return itemsList.size();
	}

	@Override
	public Object getItem(int position) {
		return itemsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View converView, ViewGroup parent) {
		ViewHolder _viewHolder;
		if(null == converView) {
			converView = LayoutInflater.from(context).inflate(R.layout.recommend_list_item, null);
			
			_viewHolder = new ViewHolder();
			_viewHolder.tv_appTitle = (TextView) converView.findViewById(R.id.tv_appTitle);
			_viewHolder.tv_appSubtitle = (TextView) converView.findViewById(R.id.tv_appSubtitle);
			_viewHolder.imgV_appLogo = (ImageView) converView.findViewById(R.id.imgV_appLogo);
			_viewHolder.btn_download = (Button) converView.findViewById(R.id.btn_download);
			
			converView.setTag(_viewHolder);
		}else {
			_viewHolder = (ViewHolder) converView.getTag();
		}
		Item _item = (Item) getItem(position);
		_viewHolder.tv_appTitle.setText(_item.name);
		_viewHolder.tv_appSubtitle.setText(_item.detail);
		_viewHolder.imgV_appLogo.setImageBitmap(_item.bitmap);
		return converView;
	}
	
	private class ViewHolder{
		public TextView tv_appTitle;
		public TextView tv_appSubtitle;
		public ImageView imgV_appLogo;
		public Button btn_download;
	}
	
}
