package com.zplay.game.popstarog.utils.sp;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.e7studio.android.e7appsdk.utils.LogUtils;

/**
 * 获取手机信息
 * 
 * @author laohuai
 */
public class PhoneInfoGetter {

	private final static String TAG = "phoneInfo";

	/**
	 * 判断手机是否有SIM卡
	 * 
	 */
	public static boolean isSimAvaliable(Context context) {
		
		boolean isAlive = false;
		android.telephony.TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
			isAlive = true;
		} else {
			isAlive = false;
		}
		return isAlive;
	}

	/**
	 * 获取手机的生产厂商跟型号
	 * 
	 * @param context
	 * @return
	 */
	public static String getManufacture() {
		return android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
	}

	/**
	 * 获取系统版本号
	 * 
	 * @return
	 */
	public static String getSysVersion() {
		return android.os.Build.VERSION.RELEASE;

	}

	/**
	 * 获取imei信息
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		imei = imei == null ? "" : imei;
		return imei;
	}

	/**
	 * 获取设备的语言设置，例如：zh、en
	 * 
	 * @param context
	 * @return
	 */
	public static String getLanguage(Context context) {
		return Locale.getDefault().getLanguage();
	}

	/**
	 * 获取运营商信息
	 * 
	 * @param context
	 * @return
	 */
	public static String getMobileSP(Context context) {
		String sp = "";
		String imsi = getIMSI(context);
		// 有imsi信息
		if (!imsi.equals("")) {
			String mnc = imsi.substring(3, 5);
			// 中国移动
			if (mnc.equals("00") || mnc.equals("02") || mnc.equals("07")) {
				sp = ConstantsHolder.CMCC;
			} else if (mnc.equals("01")||mnc.equals("06")) {
				//中国联通
				sp = ConstantsHolder.CHINA_UNICOME;
			} else if (mnc.equals("03")||mnc.equals("05")) {
				//中国电信
				sp = ConstantsHolder.CHINA_TELECOM;
			}
		} else {
			String plmn = PhoneInfoGetter.getPLMN(context);
			if (!plmn.equals("")) {
				String mnc = plmn.substring(3, 5);
				// 中国移动
				if (mnc.equals("00") || mnc.equals("02") || mnc.equals("07")) {
					sp = ConstantsHolder.CMCC;
					//中国联通
				} else if (mnc.equals("01")||mnc.equals("06")) {
					sp = ConstantsHolder.CHINA_UNICOME;
					//中国电信
				} else if (mnc.equals("03")||mnc.equals("05")) {
					sp = ConstantsHolder.CHINA_TELECOM;
				}
			}
		}
		LogUtils.v(TAG, "sp:" + sp);
		return sp;

	}

	public static String getPLMN(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String plmn = telephonyManager.getSimOperator();
		if (plmn == null) {
			plmn = "";
		}
		return plmn;
	}

	/**
	 * 获取imsi信息
	 * 
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
		LogUtils.v(TAG, "imsi:" + imsi);
		return imsi;
	}

	/**
	 * 获取设备屏幕分辩密度
	 * 
	 * @param activity
	 * @return
	 */
	public static float getDisplayDensity(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.density;
	}

}
