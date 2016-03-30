package com.zplay.game.popstarog.ozshape;

import com.zplay.game.popstarog.others.GameConstants;

/**
 * 田形，9个
 * 
 * @author Administrator
 * 
 */
public class OZShape9Square extends OZShape {

	public OZShape9Square() {
		this(GameConstants.BLOCK_1, new int[][] {
				{ GameConstants.BLOCK_1, GameConstants.BLOCK_1,
						GameConstants.BLOCK_1 },
				{ GameConstants.BLOCK_1, GameConstants.BLOCK_1,
						GameConstants.BLOCK_1 },
				{ GameConstants.BLOCK_1, GameConstants.BLOCK_1,
						GameConstants.BLOCK_1 }, });
	}

	public OZShape9Square(int sign, int[][] shapeSigns) {
		super(sign, shapeSigns);
	}

}
