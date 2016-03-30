package com.zplay.game.popstarog.utils;

import com.zplay.game.popstarog.others.GameConstants;

public class BannerConfig {

	public static boolean isBannerShow() {
		if (GameConstants.screen_width <= 320
				&& GameConstants.screen_height <= 480) {
			return false;
		} else {
			return true;
		}
	}

}
