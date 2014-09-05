package com.banker.framework.utils;

import android.content.Context;

/** 像素单位转化工具 **/
public class UnitTransformUtils {
	
	/**
	 *  根据手机的分辨率从 dp 的单位 转成为 px(像素) 
	 *  
	 *  */
	public static int dip2px(Context context, float dpvalue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpvalue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
	 * 
	 * */
	public static int px2dip(Context context, float pxvalue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxvalue / scale + 0.5f);
	}
	
}