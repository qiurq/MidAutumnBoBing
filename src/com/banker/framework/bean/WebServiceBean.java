package com.banker.framework.bean;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.ksoap2.serialization.SoapObject;
import org.xml.sax.helpers.DefaultHandler;

import com.banker.framework.constant.Constants;
import com.banker.framework.log.GlobalLog;

public abstract class WebServiceBean extends BaseBean {

	private static final long serialVersionUID = 135889134607703605L;

	/** 返回值状态，标识接口调用成功或者失败 true为成功，false为失败 **/
	public boolean state = false;
	
	/** 状态码
	 * 成功：影响的数据个数；
	 *  失败：错误码
	**/
	public int code;
	
	/**
	 * 错误信息
	 */
	public String errorMsg;
	
	/**
	 * 返回WebService的serviceUrl
	 */
	public abstract String getServiceUrl();

	/**
	 * 返回WebService的Namespace
	 */
	public abstract String getNamespace();

	/**
	 * 返回WebService的调用方法名
	 */
	public abstract String getMethodName();
	
	public abstract DefaultHandler getXmlParserHandler();
	
	/**
	 * 解析响应Soap 
	 */
	public void obtainDataInSoapObj(SoapObject soapObj) {
		String _responseStr = soapObj.getProperty("return").toString();
		
		GlobalLog.i("responseStr: " + _responseStr);
		
		try{
			SAXParserFactory _parserFactory = SAXParserFactory.newInstance();
			SAXParser _parser = _parserFactory.newSAXParser();
			_parser.parse(new ByteArrayInputStream(_responseStr.getBytes(Constants.CHARACTER_ENCODING)), getXmlParserHandler());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
