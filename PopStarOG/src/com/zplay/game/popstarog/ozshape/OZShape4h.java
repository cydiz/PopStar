package com.zplay.game.popstarog.ozshape;

import com.zplay.game.popstarog.others.GameConstants;

/**
 * 横向4个□□□□□
 * 
 * @author Administrator
 * 
 */
public class OZShape4h extends OZShape {

	public OZShape4h() {
		this(GameConstants.BLOCK_7, new int[][] { { GameConstants.BLOCK_7,
				GameConstants.BLOCK_7, GameConstants.BLOCK_7,
				GameConstants.BLOCK_7 } });
	}

	public OZShape4h(int sign, int[][] shapeSigns) {
		super(sign, shapeSigns);
	}

}
