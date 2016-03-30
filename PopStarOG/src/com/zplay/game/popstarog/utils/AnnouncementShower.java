package com.zplay.game.popstarog.utils;

import android.app.Activity;

import com.e7studio.android.e7appsdk.utils.LogUtils;
import com.example.zplay.AnnouncentConfig;

/**
 * 公告展示
 * 
 * @author Administrator
 * 
 */
public class AnnouncementShower {

	private final static String TAG = "AnnouncementShower";

	// 分别代表：0-游戏启动|1-进入游戏|2-自己点击的公告按钮
	public final static int OPEN = 0;
	public final static int GAME = 1;
	public final static int CLICK = 2;

	public static void showAnnouncement(Activity activity,
			com.orange.entity.scene.Scene scene, int code) {
		LogUtils.v(TAG, "展示公告...");
		int place = SPUtils.getAnnouncementShowPlace(activity);

		// 游戏打开就展示
		if ((place == 1 && code == OPEN) || (place == 2 && code == GAME)) {
			AnnouncentConfig.showAnnouncement(activity, 1);
		}
		if (code == CLICK) {
			AnnouncentConfig.showAnnouncement(activity, 2);
		}
	}
}
