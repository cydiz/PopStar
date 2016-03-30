package com.zplay.game.popstarog.utils;

public class OZEarnedScoreCalculator {

	private final static int BASE_SCORE = 10;
	private final static int INCREASE_SCORE = 5;

	/**
	 * 在1010模式下，消除
	 * 
	 * @param totalExploadedRowAndColumns
	 * @return
	 */
	public static int getEarnedScore(int totalExploadedRowAndColumns) {
		int score = BASE_SCORE + (totalExploadedRowAndColumns - 1)
				* INCREASE_SCORE;
		return score * totalExploadedRowAndColumns;
	}

}
