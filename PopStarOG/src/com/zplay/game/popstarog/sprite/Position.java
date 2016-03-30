package com.zplay.game.popstarog.sprite;

/**
 * 保存星星的位置
 * 
 * @author glzlaohuai
 * @date 2014-7-2
 */
public class Position {
	private int x;
	private int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Position) {
			Position des = (Position) obj;
			return this.x == des.x && this.y == des.y;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "[x:" + x + ",y:" + y + "]";

	}

}