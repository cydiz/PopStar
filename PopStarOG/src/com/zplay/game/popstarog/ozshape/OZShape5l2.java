package com.zplay.game.popstarog.ozshape;

import com.zplay.game.popstarog.others.GameConstants;

/**
 * L形，5个，形状2
 * 
 * @author Administrator
 * 
 */
public class OZShape5l2 extends OZShape {

	public OZShape5l2() {
		this(GameConstants.BLOCK_4, new int[][] {
				{ GameConstants.BLOCK_4, GameConstants.BLOCK_4,
						GameConstants.BLOCK_4 },
				{ GameConstants.BLOCK_NONE, GameConstants.BLOCK_NONE,
						GameConstants.BLOCK_4 },
				{ GameConstants.BLOCK_NONE, GameConstants.BLOCK_NONE,
						GameConstants.BLOCK_4 }, });
	}

	public OZShape5l2(int sign, int[][] shapeSigns) {
		super(sign, shapeSigns);
	}

}
