package com.banker.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.text.TextUtils;

/**
 * 缓存对象工具
 */
public class ObjectCacheUtils {

	/**
	 * 缓存对象
	 * 
	 * @param filePath: 一个文件，缓存一个对象
	 * @param cachedObj
	 * @return
	 */
	public static boolean cacheObject(String filePath,Object cachedObj){
		boolean _isObjectCached = false;
		
		if(null != cachedObj){
			File _cachedFile = new File(filePath);
			if(_cachedFile.exists()){
				_cachedFile.delete();
			}
			
			ObjectOutputStream _objOutStream = null;
			try{
				_objOutStream = new ObjectOutputStream(new FileOutputStream(_cachedFile));
				_objOutStream.writeObject(cachedObj);
				
				_isObjectCached = true;
			}catch(Exception e){
				e.printStackTrace();
				_cachedFile.delete();
			}finally {
				if(null != _objOutStream){
					try{
						_objOutStream.close();
						_objOutStream = null;
					}catch(IOException e){
						e.printStackTrace();
					}
				}
			}
		}
		
		return _isObjectCached;
	}
	
	/**
	 * 从缓存文件中读取对象
	 * @param filePath
	 * @return
	 */
	public static Object readObject(String filePath){
		Object _obj = null;
		
		if(!TextUtils.isEmpty(filePath)){
			File _cachedFile = new File(filePath);
			
			ObjectInputStream _objInStream = null;
			try {
				_objInStream = new ObjectInputStream(new FileInputStream(_cachedFile));
				//读取对象
				_obj = _objInStream.readObject();
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}finally {
				if(null != _objInStream){
					try{
						_objInStream.close();
						_objInStream = null;
					}catch(IOException e){
						e.printStackTrace();
					}
				}
			}
		}
		
		return _obj;
	}
}
