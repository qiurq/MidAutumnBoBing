package com.banker.more.activity;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.banker.framework.activity.FrameActivity;
import com.banker.more.adapter.MyAdapter;
import com.banker.more.bean.Item;
import com.banker.R;

public class RecommendActivity extends FrameActivity implements OnClickListener, OnItemClickListener {

	private static final String TAG = "RecommendActivity";
	private static final String URL = "http://117.25.149.158:8090/app/recommend_app.php";

	private ListView listView;

	private ArrayList<Item> datasList;

	private MyHandler myHandler = new MyHandler(this);

	private static class MyHandler extends Handler {
		private RecommendActivity mActivity;
		private WeakReference<RecommendActivity> weakReference;

		public MyHandler(RecommendActivity activity) {
			weakReference = new WeakReference<RecommendActivity>(activity);
			mActivity = activity;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			RecommendActivity activity = weakReference.get();
			if (null != activity) {

				switch (msg.what) {
				case 0:
					String str = (String) msg.obj;
					activity.datasList = activity.parseJson(str);
					activity.loadWebIcon(activity.datasList);
					break;
				case 1:
					Log.i(TAG, "...run结束...");
					activity.dismissProgressDialog();
					ArrayList<Item> list = (ArrayList<Item>) msg.obj;
					activity.listView.setAdapter(new MyAdapter(activity, list));
					break;

				default:
					break;
				}
			}
		}
	};

	private void loadWebIcon(final ArrayList<Item> datasLists) {
		Log.i(TAG, "...run...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i(TAG, "...run开始...");
				ArrayList<Item> list = new ArrayList<Item>();
				for (final Item items : datasLists) {
					URL url;
					Bitmap bitmap = null;
					try {
						url = new URL(items.iconUrl);
						items.bitmap = BitmapFactory.decodeStream(url.openStream());
						list.add(items);
						
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				Message msg = new Message();
				msg.obj = list;
				msg.what = 1;
				myHandler.sendMessage(msg);
			}
		}).start();

		// return bitmap;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendFrameworkCenter(R.layout.activity_recommend);
		setTopTitle(getResources().getString(R.string.softwareRecommend));
		topBackBtnListener(this);
		hideTopRightBtn();
		showProgressDialog(null, useStringTypeResource(R.string.loadMessage));
		init();
	}

	private void init() {
		initView();
		initData();
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
	}

	// 开启一个httppost请求的线程
	private void initData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 第一步，创建HttpPost对象
				HttpPost httpPost = new HttpPost(URL);

				// 设置HTTP POST请求参数必须用NameValuePair对象
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("device", "1"));

				HttpResponse httpResponse = null;
				try {
					// 设置httpPost请求参数
					httpPost.setEntity(new UrlEncodedFormEntity(params,
							HTTP.UTF_8));
					httpResponse = new DefaultHttpClient().execute(httpPost);
					Log.i(TAG, String.valueOf(httpResponse.getStatusLine()
							.getStatusCode()));
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						// 第三步，使用getEntity方法活得返回结果
						String jsonData = EntityUtils.toString(httpResponse
								.getEntity());
						Log.i(TAG, "result:" + jsonData);
						// parseJson(jsonData);

						Message m = new Message();
						m.obj = jsonData;
						m.what = 0;
						myHandler.sendMessage(m);
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Log.i(TAG, "end url...");
			}
		}).start();

	}

	// 解析Json
	private ArrayList<Item> parseJson(String jsonData) {
		ArrayList<Item> itemsList = new ArrayList<Item>();
		try {
			JSONObject jsonObject = new JSONObject(jsonData);// 创建JSONObject对象
			JSONArray jsonArray = jsonObject.getJSONArray("lists");// 获取JSONObject对象的值，该值就是接口返回的lists数组
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);// 获得lists数组中的每一个JSONObject对象
				Item item = new Item();
				item.name = jsonObject2.getString("name");// 获得每一个JSONObject对象中的键所对应的值
				Log.i(TAG, item.name);
				item.apkUrl = jsonObject2.getString("url");
				Log.i(TAG, item.apkUrl);
				item.id = jsonObject2.getInt("id");
				Log.i(TAG, String.valueOf(item.id).toString());
				item.detail = jsonObject2.getString("detail");
				Log.i(TAG, item.detail);
				item.iconUrl = jsonObject2.getString("icon");
				Log.i(TAG, item.iconUrl);
				itemsList.add(item);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return itemsList;
	}

	@Override
	public void onClick(View view) {
		finish();
	}

	// 跳转到网站
	private void gotoDownWebsite(String uriPath) {
		Uri uri = Uri.parse(uriPath);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		showToast("Item被点击了");
		
		Item item = (Item) parent.getAdapter().getItem(position);
		gotoDownWebsite(item.apkUrl);
		
	}

}
