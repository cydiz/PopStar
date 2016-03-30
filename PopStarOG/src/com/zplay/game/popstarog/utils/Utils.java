package com.zplay.game.popstarog.utils;

import android.content.Context;

public class Utils {
	/**
	 * 通过id获取资源
	 * @param context
	 * @param name 资源ID
	 * @param deftype 资源类型
	 * @return
	 */
	public static int getResByID(Context context, String name, String deftype){
		String packagename = context.getPackageName();
		return context.getResources().getIdentifier(name, deftype.toString(), packagename);
	}
}
