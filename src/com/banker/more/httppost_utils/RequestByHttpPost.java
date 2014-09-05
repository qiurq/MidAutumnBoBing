package com.banker.more.httppost_utils;
import java.util.List;  
  
import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.NameValuePair;  
import org.apache.http.client.HttpClient;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.conn.ConnectTimeoutException;  
import org.apache.http.impl.client.DefaultHttpClient;  
import org.apache.http.params.CoreConnectionPNames;  
import org.apache.http.protocol.HTTP;  
import org.apache.http.util.EntityUtils;  
  
import android.util.Log;  
  
public class RequestByHttpPost {  
  
    public static String TIME_OUT = "操作超时";  
      
    public static String doPost(List<NameValuePair> params,String url) throws Exception{  
        String result = null;  
            // 新建HttpPost对象  
            HttpPost httpPost = new HttpPost(url);  
            // 设置字符集  
            HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);  
            // 设置参数实体  
            httpPost.setEntity(entity);  
            // 获取HttpClient对象  
            HttpClient httpClient = new DefaultHttpClient();  
            //连接超时  
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);  
            //请求超时  
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);  
            try {  
                // 获取HttpResponse实例  
                HttpResponse httpResp = httpClient.execute(httpPost);  
                // 判断是够请求成功  
                if (httpResp.getStatusLine().getStatusCode() == 200) {  
                    // 获取返回的数据  
                    result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");  
                    Log.i("HttpPost", "HttpPost方式请求成功，返回数据如下：");  
                    Log.i("result", result);  
                } else {  
                    Log.i("HttpPost", "HttpPost方式请求失败");  
                }  
            } catch (ConnectTimeoutException e){  
                result = TIME_OUT;  
            }  
          
            return result;  
    }  
}  