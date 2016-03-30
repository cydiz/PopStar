package com.zplay.game.popstarog.ozshape;

import com.zplay.game.popstarog.others.GameConstants;

/**
 * 竖条，长度5
 * 
 * @author Administrator
 * 
 */
public class OZShape5v extends OZShape {
	public OZShape5v() {
		this(GameConstants.BLOCK_8, new int[][] { { GameConstants.BLOCK_8 },
				{ GameConstants.BLOCK_8 }, { GameConstants.BLOCK_8 },
				{ GameConstants.BLOCK_8 }, { GameConstants.BLOCK_8 } });
	}

	public OZShape5v(int sign, int[][] shapeSigns) {
		super(sign, shapeSigns);
	}

}
