package com.zplay.game.popstarog.utils;

import java.util.Random;

public class MathUtils {
	private final static Random random = new Random(System.nanoTime());

	/**
	 * 获取一个处于min~max之间的正整数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getValue(Random random, int min, int max) {
		int value = Math.abs(random.nextInt() % (max + 1));
		while (value < min) {
			value = Math.abs(random.nextInt() % (max + 1));
		}
		return value;
	}

	/**
	 * 包含min以及max之内的随机整数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandom(int min, int max) {
		return min + random.nextInt(max - min + 1);
	}

}
