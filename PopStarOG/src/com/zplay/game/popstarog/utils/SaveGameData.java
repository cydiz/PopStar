package com.zplay.game.popstarog.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SaveGameData {

	public static void saveJsonData(final Context context) {
		System.out.println("手机号码：" + SPUtils.getPhoneNumber(context));
		if ("null".equals(SPUtils.getPhoneNumber(context))) {
			return;
		}
		final JSONObject jsonObject = generateJsonData(context);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					URL url = new URL("http://popstar.zplay.cn/cloud/in/in.php");
					URLConnection urlConnection = url.openConnection();
					urlConnection.setDoOutput(true);
					urlConnection.setRequestProperty("content-type",
							"application/x-www-form-urlencoded");
					OutputStreamWriter out = new OutputStreamWriter(
							urlConnection.getOutputStream());
					out.write(jsonObject.toString());
					out.flush();
					out.close();
					InputStream inputStream = urlConnection.getInputStream();
					System.out.println("输出为"
							+ convertStreamToString(inputStream));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private static JSONObject generateJsonData(Context context) {
		try {
			/** valueObject */
			JSONObject valueObject = new JSONObject();
			valueObject.put("luckStarNum", SPUtils.getLuckStarNum(context));
			// 保存经典模式游戏数据
			valueObject.put("PopGameSign", SPUtils.getStars(context));
			valueObject.put("PopGameCurrentScore",
					SPUtils.getCurrentScore(context));
			valueObject.put("PopGameStage", SPUtils.getStage(context));
			valueObject.put("PopGameRebornNum", SPUtils.getRebornNum(context));
			valueObject.put("PopHighScore", SPUtils.getHighScore(context));
			// 保存星星连萌游戏数据
			valueObject.put("OZGameSign", SPUtils.get1010Stars(context));
			valueObject.put("OZGameCurrentScore",
					SPUtils.get1010CurrentScore(context));
			valueObject.put("OZGameHighScore",
					SPUtils.get1010HighScore(context));
			valueObject.put("OZGameGroupSign",
					SPUtils.get1010GroupSigns(context));
			valueObject.put("OZPointProgress",
					SPUtils.getPointProgress(context));
			// 保存手机信息
			TelephonyManager tManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String line1Number = tManager.getLine1Number() == null ? "未知"
					: tManager.getLine1Number().toString();
			String simSerialNumber = tManager.getSimSerialNumber() == null ? "未知"
					: tManager.getSimSerialNumber().toString();
			String IMEI = tManager.getDeviceId();
			valueObject.put("simSerialNumber", simSerialNumber);
			String number = SPUtils.getPhoneNumber(context);
			if (null == number || "".equals(number) || "null".equals(number)) {
				valueObject.put("phoneNumber", line1Number);
			} else {
				valueObject.put("phoneNumber", number);
			}
			/** dataObject */
			JSONObject dataObject = new JSONObject();
			dataObject.put("action", "set");
			dataObject.put("key", IMEI + SPUtils.getPhoneNumber(context));
			dataObject.put("value", valueObject.toString());
			dataObject.put("ts", "1440653514");
			/** jsonObject */
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("data", dataObject);
			jsonObject.put("sign",
					MD5Utils.MD5(IMEI + SPUtils.getPhoneNumber(context) + "setzplay888"));
			System.out.println(jsonObject);
			return jsonObject;
		} catch (JSONException e) {
			e.printStackTrace();
			System.out.println("Json异常");
		}
		return null;
	}

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
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
