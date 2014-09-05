package com.banker.framework.webService;

import java.util.HashMap;

import org.ksoap2.serialization.SoapObject;

import android.os.Handler;
import android.os.Message;

import com.banker.framework.bean.WebServiceBean;
import com.banker.framework.constant.Constants;
import com.banker.framework.log.GlobalLog;

/**
 * webService请求相关的线程类
 * @author zhxl
 *
 */
public class WebServiceThread extends Thread {
	
	/** 线程终止标志 **/
	private boolean mIsCancel;
	
	private WebServiceBean mWebServiceBean;
	private HashMap<String,String> mRequestParams;
	private Handler mRequestHandler;
	private int mMsgWhat;

	public WebServiceThread(WebServiceBean webServiceBean,HashMap<String,String> requestParams,Handler requestHandler,int msgWhat){
		mWebServiceBean = webServiceBean;
		mRequestParams = requestParams;
		mRequestHandler = requestHandler;
		mMsgWhat = msgWhat; 
	}

	@Override
	public void run() {
		if(mIsCancel){
			mRequestHandler.sendEmptyMessage(Constants.HANDLER_MSG_THREAD_CANCEL);
			return;
		}
		
		//发起WebService请求,获得响应
		SoapObject _respSoapObj = WebServiceRequest.send(mWebServiceBean.getServiceUrl(), mWebServiceBean.getNamespace(), mWebServiceBean.getMethodName(), mRequestParams);
		//将响应消息解析，并且返回给请求线程
		if(null != mRequestHandler){
			if(mIsCancel){
				mRequestHandler.sendEmptyMessage(Constants.HANDLER_MSG_THREAD_CANCEL);
				return;
			}
			
			//解析消息
			mWebServiceBean.obtainDataInSoapObj(_respSoapObj);
			//构造消息
			Message _msg = new Message();
			_msg.what = mMsgWhat;
			_msg.obj = mWebServiceBean;
		
			//返回给主线程
			mRequestHandler.sendMessage(_msg);
		}
	}
	
	public void cancel(){
		mIsCancel = true;
	}
	
	
}
