package com.banker.framework.log;

import android.util.Log;

import com.banker.framework.constant.Constants;

/**
 * 全局日志输出类
 * @author zhxl
 *
 */
public class GlobalLog {
	
	private static final String TAG = "COM.BANKER";
	
	public static void i(String msg){
		if(Constants.DEBUG){
			Log.i(TAG, msg);
		}
	}
	
	public static void i(String tag,String msg){
		if(Constants.DEBUG){
			Log.i(tag, msg);
		}
	}
	
	public static void e(String msg){
		Log.e(TAG, msg);
	}

}
