package com.zplay.game.popstarog.utils;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;

public class ControllerUtils {

	/**
	 * 日期控制，到达指定日期后返回true
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static boolean dateController(Context context) {
		Date nowDate = new Date();
		String date = readFileData(context, "init_date");
		if (date == null || "".equals(date) || "null".equals(date)) {
			LogUtils.v("没有日期存档，使用默认mm计费");
			return false;
		}
		LogUtils.d("读到的配置日期：" + date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date controlDate = null;
		try {
			controlDate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		LogUtils.i("是否在指定日期之后：" + controlDate.before(nowDate));
		return controlDate.before(nowDate);
	}
	
	/**
	 * 联网控制，返回1初始化基地，返回0初始化mm
	 * @return
	 */
	public static String httpController(Context context) {
		String url = "http://7xnxcz.com1.z0.glb.clouddn.com/popstarzw2.3.5.json?ran=" + new Random().nextInt();
		LogUtils.i(url);
		String result = HttpUtils.getServiceData(url);
		LogUtils.i("服务器返回result:" + result);
		try {
			JSONObject jsonObject = new JSONObject(result);
			String date = jsonObject.getString("date");
			LogUtils.i("控制日期：" + date);
			return date;
//			SPUtils.setInitControllerDate(context, date);
		} catch (JSONException e) {
			e.printStackTrace();
			LogUtils.e("json解析异常");
			return "";
		}
	}
	
	public static String readFileData(Context context, String fileName) {
		String res = "";
		try {
			FileInputStream fin = context.openFileInput(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}
