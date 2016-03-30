package com.zplay.game.popstarog.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
	/**
	 * 获取活动奖励json
	 * 
	 * @param strJson
	 * @return
	 */
	public static String getVerifyCode(String strJson) {

		try {
			JSONArray jsonArray = new JSONArray(strJson);
			JSONObject jsonObject = jsonArray.getJSONObject(0);
			return jsonObject.getString("num");
		} catch (JSONException e) {
		}

		return null;
	}
}
