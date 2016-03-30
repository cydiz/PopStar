package com.zplay.game.popstarog.utils;
import com.zplay.game.popstarog.sprite.StarSprite;

public class RollbackInfo {
	private StarSprite starSprite;
	private int step;

	public RollbackInfo(StarSprite starSprite, int step) {
		this.starSprite = starSprite;
		this.step = step;
	}

	public StarSprite getStarSprite() {
		return starSprite;
	}

	public int getStep() {
		return step;
	}
	
}