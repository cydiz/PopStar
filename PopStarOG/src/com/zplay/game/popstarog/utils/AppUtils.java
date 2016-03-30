package com.zplay.game.popstarog.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.util.Log;

public class AppUtils {

	public static final int SP_CMCC = 0;
	public static final int SP_UNICOM = 1;
	public static final int SP_TELECOM = 2;
	public static final int SP_NO_CARD = 3;
	public static final int SP_UNKNOW = 4;
	
	public static String getAppVersion(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
        return packInfo.versionName;
	}
	
	/**
	 * 获取sim卡运营商类型
	 * @param context
	 * @return
	 */
	public static int getOperator(Context context){
		int sp = SP_UNKNOW;
		String imsi = getIMSI(context);
		if (!imsi.equals("")) {
			String mnc = imsi.substring(3, 5);
			if (mnc.equals("00") || mnc.equals("02") || mnc.equals("07")) {
				sp = SP_CMCC;
			} else if (mnc.equals("01")) {
				sp = SP_UNICOM;
			} else if (mnc.equals("03")) {
				sp = SP_TELECOM;
			}
		} else {
			sp = SP_NO_CARD;
		}
		return sp;
	}
	
	/**
	 * 获取imsi信息
	 * @param context
	 * @return
	 */
	public static String getIMSI(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telephonyManager.getSubscriberId();
		if (imsi == null) {
			imsi = "";
		}
		Log.v("Helper", "imsi:" + imsi);
		return imsi;
	}
}
