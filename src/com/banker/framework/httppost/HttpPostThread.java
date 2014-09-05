package com.banker.framework.httppost;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 向服务器发送请求和接收服务器返回的数据
 * @author Administrator
 *
 */
public class HttpPostThread implements Runnable {
	
	
	private String strResult;
	
	private void post() {
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		//封装传给服务器的请求参数
		ArrayList<NameValuePair> valuePairList = new ArrayList<NameValuePair>();
		valuePairList.add(new BasicNameValuePair("name", "name001"));
		valuePairList.add(new BasicNameValuePair("age", "age001"));
		
		try {
			//给请求参数设置编码
			HttpEntity entity = new UrlEncodedFormEntity(valuePairList, "utf-8");
			
			//新建一个post请求并将请求放到post请求里
			HttpPost httpPost = new HttpPost();
			httpPost.setEntity(entity);
			
			//客户端向服务器发送请求
			HttpResponse response = httpClient.execute(httpPost);
			
			if(response.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(response.getEntity());
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void parserJSON() {
//		
//		try {
////			JSONObject jsonObject = new JSONObject(strResult).getJSONObject(name);
//			
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

	@Override
	public void run() {

	}

}
