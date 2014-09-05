package com.banker.framework.handler;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

public abstract class AbsActivityHandler<T extends Activity> extends Handler {

	private WeakReference<T> mWeakReference;
	
	public AbsActivityHandler(T activity){
		mWeakReference = new WeakReference<T>(activity);
	}

	@Override
	public void handleMessage(Message msg) {
		T _activity = mWeakReference.get();
		if(null != _activity){
			handleMessage(_activity,msg);
		}
	}
	
	protected abstract void handleMessage(final T theActivity,Message msg);
}
