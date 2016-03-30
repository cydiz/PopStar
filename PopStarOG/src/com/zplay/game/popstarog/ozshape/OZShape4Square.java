package com.zplay.game.popstarog.ozshape;

import com.zplay.game.popstarog.others.GameConstants;

/**
 * 田形，4个
 * 
 * @author Administrator
 * 
 */
public class OZShape4Square extends OZShape {

	public OZShape4Square() {
		this(GameConstants.BLOCK_2, new int[][] {
				{ GameConstants.BLOCK_2, GameConstants.BLOCK_2 },
				{ GameConstants.BLOCK_2, GameConstants.BLOCK_2 } });
	}

	public OZShape4Square(int sign, int[][] shapeSigns) {
		super(sign, shapeSigns);
	}

}
