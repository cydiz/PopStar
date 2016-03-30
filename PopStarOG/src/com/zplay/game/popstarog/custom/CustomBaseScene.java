package com.zplay.game.popstarog.custom;

import java.util.Stack;

import android.view.KeyEvent;

import com.e7studio.android.e7appsdk.utils.LogUtils;
import com.orange.entity.scene.Scene;

public class CustomBaseScene extends Scene {
	private Stack<Dialog> dialogStack = new Stack<Dialog>();

	private final static String TAG = "CustomBaseScene";

	@Override
	public void onSceneResume() {
		super.onSceneResume();
		setIgnoreUpdate(false);
	}

	@Override
	public void onScenePause() {
		super.onScenePause();
		setIgnoreUpdate(true);
	}

	public Stack<Dialog> getDialogStack() {
		return dialogStack;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			LogUtils.v(TAG, "按下了返回键...");
			if (dialogStack.size() == 0) {
				LogUtils.v(TAG, "dialogStack为空，不做处理，丢给上层处理");
				return super.onKeyDown(keyCode, event);
			} else {
				LogUtils.v(TAG, "dialogStack不为空，自己进行处理...");
				Dialog dialog = dialogStack.peek();
				if (dialog.isBackKeyResponsed()) {
					dialog.dismiss();
				}
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
