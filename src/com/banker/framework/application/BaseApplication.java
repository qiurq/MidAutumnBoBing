package com.banker.framework.application;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.banker.framework.constant.Constants;
import com.banker.framework.exception.GlobalExceptionHandler;

public class BaseApplication extends Application {
	
	/** 全局上下文 **/
	public static Context appContext;
	public static Handler appHandler;
	
	/** 屏幕尺寸 **/
	public static int screenHeight;
	public static int screenWidth;
	public static float screenDensity;
	public static int screenDensityDpi;
	
	@Override
	public void onCreate() {
		super.onCreate();

		appContext = getApplicationContext();
		appHandler = new Handler();
		
		initDisplayMetrics();
		
		Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler(this));
		
		mkDirs();
	}


	/**
	 * 初始化应用所在的手机屏幕的相关参数
	 */
	private void initDisplayMetrics() {
		DisplayMetrics _displayMetrics = new DisplayMetrics();
		
		WindowManager _windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		_windowManager.getDefaultDisplay().getMetrics(_displayMetrics);
		
		//是否竖屏
		boolean _isPortrait = _displayMetrics.widthPixels < _displayMetrics.heightPixels;
		screenWidth = _isPortrait? _displayMetrics.widthPixels : _displayMetrics.heightPixels;
		screenHeight = _isPortrait? _displayMetrics.heightPixels : _displayMetrics.widthPixels;
		
		screenDensity = _displayMetrics.density;
		screenDensityDpi = _displayMetrics.densityDpi;
	}
	
	@Override
	public void onTerminate() {
		appContext = null;
		appHandler = null;
		
		super.onTerminate();
	}

	/**
	 * 建立相关目录
	 */
	private void mkDirs() {
		File _baseDir = new File(Constants.BASE_PATH);
		if(!_baseDir.isDirectory()){
			_baseDir.mkdir();
		}
	}
	
	/**
	 * 应用是否退出
	 */
	private static Toast mToast;
	
	public static void showToast(final String text){
		appHandler.post(new Runnable() {
			@Override
			public void run() {
				if(null == mToast){
					mToast = Toast.makeText(appContext, text, Toast.LENGTH_SHORT);
				}else {
					mToast.setText(text);
				}
				mToast.show();
			}
		});
	}
	
}
