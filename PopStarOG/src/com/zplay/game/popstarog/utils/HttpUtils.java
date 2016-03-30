package com.zplay.game.popstarog.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import com.zplay.game.popstarog.utils.sp.ConstantsHolder;

public class HttpUtils {
	
	public static boolean HttpGets(){
		try {
		   //第一步，创建HttpGet对象
		   HttpGet httpGet = new HttpGet(ConstantsHolder.IS_OPEN_GOSHOP);
		   //第二步，使用execute方法发送HTTP GET请求，并返回HttpResponse对象
		   HttpResponse  httpResponse = new DefaultHttpClient().execute(httpGet);
		   if (httpResponse.getStatusLine().getStatusCode() == 200)
		   {
		        //第三步，使用getEntity方法活得返回结果
		        
					String result = EntityUtils.toString(httpResponse.getEntity());
					if(result.trim().equals("1")){
						return true;
					}else if(result.trim().equals("0")){
						return false;
					}
			return false;
		    }
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String HttpGets(String url) {
		try {
			LogUtils.i("开始联网...");
			HttpGet httpRequest = new HttpGet(url);
			BasicHttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 2 * 1000);
			HttpResponse httpResponse = new DefaultHttpClient(params).execute(httpRequest);
			int code = httpResponse.getStatusLine().getStatusCode();
			LogUtils.i("code" + code);
			if (code == 200) {
				String result = EntityUtils.toString(httpResponse.getEntity());
				LogUtils.i("服务器返回result：" + result);
				return result;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			LogUtils.e("网络ParseException...");
		} catch (IOException e) {
			e.printStackTrace();
			LogUtils.e("网络IOException...");
		}
		return "";
	}
	
	public static String getServiceData(String urlString) {
		try {
			URL url = new URL(urlString);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
			out.flush();
			out.close();
			InputStream inputStream = urlConnection.getInputStream();
			return convertStreamToString(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.e("网络异常...");
			return "";
		}
	}
	
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
