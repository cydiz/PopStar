package com.zplay.game.popstarog.utils;

import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.ozshape.OZShape1s;

/**
 * 在每次放置之后，检查行/列是不是只有九个，如果是的话，将弹出使用小点道具{@linkplain OZShape1s}的引导提示界面
 * 
 * @author glzlaohuai
 */
public class NineHandler {

	/**
	 * 是否是一个九个点的行
	 * 
	 * @param starSigns
	 * @param y
	 * @return
	 */
	public static boolean isNineBlockRow(int[][] starSigns, int y) {
		int blankNum = 0;
		for (int x = 0; x < 10; x++) {
			if (starSigns[x][y] == GameConstants.BLOCK_NONE
					|| starSigns[x][y] == GameConstants.BLOCK_EXPLOADING) {
				blankNum++;
			}
			if (blankNum >= 2) {
				break;
			}
		}
		return blankNum == 1;
	}

	/**
	 * 是否是一个填充满的列
	 * 
	 * @param starSigns
	 * @param x
	 * @return
	 */
	public static boolean isNineBlockColumn(int[][] starSigns, int x) {
		int blankNum = 0;
		for (int y = 0; y < 10; y++) {
			if (starSigns[x][y] == GameConstants.BLOCK_NONE
					|| starSigns[x][y] == GameConstants.BLOCK_EXPLOADING) {
				blankNum++;
				if (blankNum >= 2) {
					break;
				}
			}
		}
		return blankNum == 1;
	}

}
