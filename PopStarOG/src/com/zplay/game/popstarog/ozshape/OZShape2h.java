package com.zplay.game.popstarog.ozshape;

import com.zplay.game.popstarog.others.GameConstants;

/**
 * 横条，两个
 * 
 * @author Administrator
 * 
 */
public class OZShape2h extends OZShape {

	public OZShape2h() {
		this(GameConstants.BLOCK_5, new int[][] { { GameConstants.BLOCK_5,
				GameConstants.BLOCK_5 } });
	}

	public OZShape2h(int sign, int[][] shapeSigns) {
		super(sign, shapeSigns);
	}

}
