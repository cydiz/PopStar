package com.zplay.game.popstarog.utils;

import com.orange.util.size.Size;
import com.zplay.game.popstarog.others.GameConstants;

/**
 * 用于计算1010模式中，组合方块的大小
 * 
 * @author glzlaohuai
 */
public class OZGroupBlockSizeCalculator {

	public static Size calculate() {
		float width = GameConstants.BASE_WIDTH - GameConstants.OZ_PADDING_X * 2;
		float height = GameConstants.OZ_GROUP_CONTAINER_HEIGHT;

		// 去除掉banner的高度
		height -= GameConstants.AD_BANNER_HEIGHT;

		// 首先已宽来作为基准
		Size wSize = SizeHelper.ogSizeScale(width / 3 / 5, width / 3 / 5);
		Size hSize = SizeHelper.ogSizeScale(height / 5, height / 5);

		if (wSize.getHeight() * 5 > height) {
			GameConstants.OZ_GROUP_SIZE = hSize;
			return hSize;
		} else {
			GameConstants.OZ_GROUP_SIZE = wSize;
			return wSize;
		}
	}

}
