package com.zplay.game.popstarog.ozshape;

import com.zplay.game.popstarog.others.GameConstants;

/**
 * L形，3个，形状1
 * 
 * @author Administrator
 * 
 */
public class OZShape3l1 extends OZShape {

	public OZShape3l1() {
		this(GameConstants.BLOCK_3, new int[][] {
				{ GameConstants.BLOCK_3, GameConstants.BLOCK_NONE },
				{ GameConstants.BLOCK_3, GameConstants.BLOCK_3 } });
	}

	public OZShape3l1(int sign, int[][] shapeSigns) {
		super(sign, shapeSigns);
	}

}
