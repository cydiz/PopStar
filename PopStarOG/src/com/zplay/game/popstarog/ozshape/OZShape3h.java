package com.zplay.game.popstarog.ozshape;

import com.zplay.game.popstarog.others.GameConstants;

/**
 * 横条，三个
 * 
 * @author Administrator
 * 
 */
public class OZShape3h extends OZShape {

	public OZShape3h() {
		this(GameConstants.BLOCK_6, new int[][] { { GameConstants.BLOCK_6,
				GameConstants.BLOCK_6, GameConstants.BLOCK_6 } });
	}

	public OZShape3h(int sign, int[][] shapeSigns) {
		super(sign, shapeSigns);
	}

}
