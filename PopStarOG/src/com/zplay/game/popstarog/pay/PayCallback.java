package com.zplay.game.popstarog.pay;

public interface PayCallback {

	public final static int OK = 1;
	public final static int FAILED = 2;
	public final static int CANCEL = 3;

	void callback(int code, String msg);
}
