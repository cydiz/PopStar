package com.zplay.game.popstarog.ozshape;

import com.zplay.game.popstarog.others.GameConstants;

/**
 * 横向5个□□□□□
 * 
 * @author Administrator
 * 
 */
public class OZShape5h extends OZShape {

	public OZShape5h() {
		this(GameConstants.BLOCK_8, new int[][] { { GameConstants.BLOCK_8,
				GameConstants.BLOCK_8, GameConstants.BLOCK_8,
				GameConstants.BLOCK_8, GameConstants.BLOCK_8 } });
	}

	public OZShape5h(int sign, int[][] shapeSigns) {
		super(sign, shapeSigns);
	}

}
