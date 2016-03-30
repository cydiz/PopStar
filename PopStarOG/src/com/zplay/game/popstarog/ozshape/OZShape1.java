package com.zplay.game.popstarog.ozshape;

import com.zplay.game.popstarog.others.GameConstants;

/**
 * 点，一个，紫色
 * 
 * @author Administrator
 * 
 */
public class OZShape1 extends OZShape {
	public OZShape1() {
		this(GameConstants.BLOCK_0, new int[][] { { GameConstants.BLOCK_0 } });
	}
	public OZShape1(int sign, int[][] shapeSigns) {
		super(sign, shapeSigns);
	}
}
