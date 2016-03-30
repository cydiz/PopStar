package com.zplay.game.popstarog.ozshape;

import com.zplay.game.popstarog.others.GameConstants;

/**
 * 竖条，长度2
 * 
 * @author Administrator
 * 
 */
public class OZShape2v extends OZShape {
	public OZShape2v() {
		this(GameConstants.BLOCK_5, new int[][] { { GameConstants.BLOCK_5 },
				{ GameConstants.BLOCK_5 } });
	}

	public OZShape2v(int sign, int[][] shapeSigns) {
		super(sign, shapeSigns);
	}

}
