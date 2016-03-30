package com.zplay.game.popstarog.ozshape;

import com.zplay.game.popstarog.others.GameConstants;

/**
 * 竖条，长度3
 * 
 * @author Administrator
 * 
 */
public class OZShape3v extends OZShape {
	public OZShape3v() {
		this(GameConstants.BLOCK_6, new int[][] { { GameConstants.BLOCK_6 },
				{ GameConstants.BLOCK_6 }, { GameConstants.BLOCK_6 } });
	}

	public OZShape3v(int sign, int[][] shapeSigns) {
		super(sign, shapeSigns);
	}

}
