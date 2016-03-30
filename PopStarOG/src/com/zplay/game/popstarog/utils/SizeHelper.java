package com.zplay.game.popstarog.utils;

import com.orange.util.size.Size;
import com.zplay.game.popstarog.others.GameConstants;

public class SizeHelper {
	public static float xToOgUnit(float px) {
		float x = px * 1.0f / GameConstants.screen_width
				* GameConstants.BASE_WIDTH;
		return x;
	}

	public static float yToOgUnit(float px) {
		float x = px * 1.0f / GameConstants.screen_height
				* GameConstants.BASE_HEIGHT;
		return x;
	}

	public static float xOGUnitToPixel(float xOGUnit) {
		return xOGUnit * 1.0f * GameConstants.screen_width
				/ GameConstants.BASE_WIDTH;
	}

	public static float yOGUnitToPixel(float yOGUnit) {
		return yOGUnit * 1.0f * GameConstants.screen_height
				/ GameConstants.BASE_HEIGHT;
	}

	public static Size ogSizeScale(float ogWidth, float ogHeight) {
		Size size = new Size();
		float scale = (float) (GameConstants.BASE_WIDTH * 1.0 / GameConstants.BASE_HEIGHT);
		// 如果设计的宽高比大于实际设备的宽高比，那么按照宽度来做基准
		if (scale > GameConstants.screen_width * 1.0f
				/ GameConstants.screen_height) {
			size.setWidth(ogWidth);
			float pxWidth = xOGUnitToPixel(ogWidth);

			float pxHeight = pxWidth * ogHeight / ogWidth;
			float ogRealHeight = yToOgUnit((int) pxHeight);
			size.setHeight(ogRealHeight);
		}
		// 按照高度来做基准
		else {
			size.setHeight(ogHeight);
			float pxHeight = yOGUnitToPixel(ogHeight);
			float pxWidth = ogWidth / ogHeight * pxHeight;
			float ogRealWidth = xToOgUnit((int) pxWidth);
			size.setWidth(ogRealWidth);
		}
		return size;
	}

}
