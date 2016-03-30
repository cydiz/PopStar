package com.zplay.game.popstarog.utils;

public class IntegerContainer {

	private int value;

	public IntegerContainer(int value) {
		this.value = value;
	}

	public void minus() {
		value--;
	}

	public void add() {
		value++;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
