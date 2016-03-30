package com.zplay.game.popstarog.utils;

import com.zplay.game.popstarog.others.GameConstants;

/**
 * 用于1010模式下检查行/列是否被填充满了
 * 
 * @author Administrator
 * 
 */
public class FullHandler {

	/**
	 * 是否是一个填充满的行
	 * 
	 * @param starSigns
	 * @param y
	 * @return
	 */
	public static boolean isFullRow(int[][] starSigns, int y) {
		for (int x = 0; x < 10; x++) {
			if (starSigns[x][y] == GameConstants.BLOCK_NONE
					|| starSigns[x][y] == GameConstants.BLOCK_EXPLOADING) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否是一个填充满的列
	 * 
	 * @param starSigns
	 * @param x
	 * @return
	 */
	public static boolean isFullColumn(int[][] starSigns, int x) {
		for (int y = 0; y < 10; y++) {
			if (starSigns[x][y] == GameConstants.BLOCK_NONE
					|| starSigns[x][y] == GameConstants.BLOCK_EXPLOADING) {
				return false;
			}
		}
		return true;
	}

	public static void setFullRowInExploadeMode(int[][] starSigns, int row) {
		for (int i = 0; i < 10; i++) {
			starSigns[i][row] = GameConstants.BLOCK_EXPLOADING;
		}
	}

	public static void setFullColumnInExploadeMode(int[][] starSigns, int column) {
		for (int i = 0; i < 10; i++) {
			starSigns[column][i] = GameConstants.BLOCK_EXPLOADING;
		}
	}

}
