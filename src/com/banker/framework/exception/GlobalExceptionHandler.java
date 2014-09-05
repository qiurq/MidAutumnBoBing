package com.banker.framework.exception;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Debug;
import android.text.format.Time;
import android.util.Log;

import com.banker.framework.constant.Constants;

public class GlobalExceptionHandler implements UncaughtExceptionHandler {
	
	private static final String TAG = "GlobalExceptionHandler";
	
	private static UncaughtExceptionHandler mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
	
	/**
	 * 日志文件过期周期(14天)
	 */
	private static final long LOG_FILE_OUT_PERIOD = 1000 * 60 * 60 * 24 * 14;
	
	/**
	 * 日志内容大小
	 */
	private static final int MAX_LOG_MESSAGE_LENGTH = 200000;
	
	/**
	 * 操作系统换行符
	 */
	private static final String OS_LINE_SEPARATOR = System.getProperty("line.separator");
	
	private String mAppVersionName;
	private int mAppVersionCode;
	
	private String mOsModel;
	private int mOsSDKInt;
	
	public GlobalExceptionHandler(Context context){
		PackageManager _pkgMgr = context.getPackageManager();
		try{
			PackageInfo _pkgInfo = _pkgMgr.getPackageInfo(context.getPackageName(), 0);
			mAppVersionName = _pkgInfo.versionName;
			mAppVersionCode = _pkgInfo.versionCode;
			mOsModel = android.os.Build.MODEL;
			mOsSDKInt = android.os.Build.VERSION.SDK_INT;
			
			new File(Constants.LOG_DIR).mkdirs();

			new Thread(new Runnable() {
				@Override
				public void run() {
					clearOutDateLogs();
				}
			}).start();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 清空过期日志
	 */
	private void clearOutDateLogs() {
		try{
			File _logsDir = new File(Constants.LOG_DIR);
			final long _currentTime = System.currentTimeMillis();
			
			//根据当前时间，过滤出已过期的日志文件
			File[] _logFiles = _logsDir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File logsDir, String logFileName) {
					File _logFile = new File(logsDir.getAbsolutePath() + "/" + logFileName);
					long _lastModifyTime = _logFile.lastModified();
					if((_currentTime - _lastModifyTime) > LOG_FILE_OUT_PERIOD){
						return true;
					}
					return false;
				}
			});
			
			//删除过滤日志
			if(null != _logFiles && _logFiles.length > 0){
				for(File _logFile : _logFiles){
					_logFile.delete();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 抛异常，则记录日志
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		final Writer _stackResult = new StringWriter(); 
		final PrintWriter _printWriter = new PrintWriter(_stackResult);
		throwable.printStackTrace(_printWriter);
		
		try{
			File _logsDir = new File(Constants.LOG_DIR);
			if(!_logsDir.isDirectory() && !_logsDir.exists()){
				_logsDir.mkdirs();
			}
			
			Time _timeTxt = new android.text.format.Time();
			_timeTxt.setToNow();
			String _timeStr = _timeTxt.format("%Y-%m-%d %H:%M:%S");
			
			File _logFile = new File(Constants.LOG_DIR + System.currentTimeMillis() + ".log");
			_logFile.createNewFile();
			
			BufferedWriter bos = new BufferedWriter(new FileWriter(_logFile));
			bos.write("\t\n==================LOG=================\t\n");
			bos.write("APP_VERSION:" + mAppVersionName + "|" + mAppVersionCode + "\t\n");
			bos.write("OS_MODEL:" + mOsModel + "\t\n");
			bos.write("OS_SDK_INT:" + mOsSDKInt + "\t\n");
			bos.write(_timeStr + "\t\n");
			bos.write(_stackResult.toString());
			bos.write("\t\n--------------------------------------\t\n");
			bos.flush();
			StringBuilder _logCatOut = getLogCatOut();
			int _keepOffset = Math.max(_logCatOut.length() - MAX_LOG_MESSAGE_LENGTH, 0);
			if (_keepOffset > 0) {
				_logCatOut.delete(0, _keepOffset);
			}
			bos.write(_logCatOut.toString());
			bos.flush();
			bos.close();
		}catch(Exception ebos){
			ebos.printStackTrace();
		}
		
		mDefaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
	}
	
	/**
	 * 获取Logcat的日志输出
	 * @return
	 */
	public StringBuilder getLogCatOut() {
		final StringBuilder log = new StringBuilder();
		try {
			ArrayList<String> commandLine = new ArrayList<String>();
			commandLine.add("logcat");//$NON-NLS-1$
			commandLine.add("-d");//$NON-NLS-1$

			Process process = Runtime.getRuntime().exec(
					commandLine.toArray(new String[0]));
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String _line;
			while ((_line = bufferedReader.readLine()) != null) {
				log.append(_line);
				log.append(OS_LINE_SEPARATOR);
			}
		} catch (IOException e) {
			Log.e("TAG", "getLog failed", e);//$NON-NLS-1$
		}
		return log;
	}
	
	/**
	 * 保存内存堆栈信息
	 */
	public static void saveHprofData(){
		try{
			Debug.dumpHprofData(Constants.LOG_DIR + System.currentTimeMillis() + ".hprof");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
