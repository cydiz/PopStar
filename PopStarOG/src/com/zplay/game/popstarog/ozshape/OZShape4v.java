package com.zplay.game.popstarog.ozshape;

import com.zplay.game.popstarog.others.GameConstants;

/**
 * 竖条，长度5
 * 
 * @author Administrator
 * 
 */
public class OZShape4v extends OZShape {
	public OZShape4v() {
		this(GameConstants.BLOCK_7, new int[][] { { GameConstants.BLOCK_7 },
				{ GameConstants.BLOCK_7 }, { GameConstants.BLOCK_7 },
				{ GameConstants.BLOCK_7 } });
	}

	public OZShape4v(int sign, int[][] shapeSigns) {
		super(sign, shapeSigns);
	}

}
