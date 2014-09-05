package com.banker.framework.webService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.banker.framework.log.GlobalLog;


/**
 * 请求参数是一个List,其中每个参数都是一个Map,包含参数名称和参数值,基于KSoap2
 */
public class WebServiceRequest {

	/**
	 * 
	 * @param method_name
	 *            方法名
	 * @param soap_action
	 *            方法提交URL
	 * @param prams
	 *            提交参数，不同方法具体各异
	 * @return String XML
	 */
	public static SoapObject send(String serverUrl, String namespace,String methodName, HashMap<String, String> requestParams) {
		//构建Soap消息
		SoapObject _soapObject = new SoapObject(namespace, methodName);
		
		//向Soap消息中添加请求参数
		if (requestParams != null) {
			for(Map.Entry<String, String> _requestParam : requestParams.entrySet()){
				_soapObject.addProperty(_requestParam.getKey(), _requestParam.getValue());
				//调试输出
				GlobalLog.i(_requestParam.getKey() + ": " + _requestParam.getValue());
			}
		}
		
		//构造Soap信封
		SoapSerializationEnvelope _soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		_soapSerializationEnvelope.bodyOut = _soapObject;
		(new MarshalBase64()).register(_soapSerializationEnvelope);
		
		//发送请求，返回服务器响应的Soap消息
		try {
			HttpTransportSE httpTransportSE = new HttpTransportSE(serverUrl);
			httpTransportSE.debug = true;
			httpTransportSE.call(namespace + methodName,_soapSerializationEnvelope);
			
			GlobalLog.i("method_name: " +  methodName + "bodyIn: " + _soapSerializationEnvelope.bodyIn);
			
			return (SoapObject)_soapSerializationEnvelope.bodyIn;
		} catch (SoapFault e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}

}
