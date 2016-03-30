package com.zplay.game.popstarog.utils;

import java.util.List;

import com.e7studio.android.e7appsdk.utils.LogUtils;
import com.orange.entity.modifier.IEntityModifier.IEntityModifierListener;
import com.orange.entity.modifier.ScaleModifier;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.entity.sprite.Sprite;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.util.modifier.ease.EaseBackIn;
import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.ozshape.OZShape;
import com.zplay.game.popstarog.ozshape.OZShapeMoveListener;
import com.zplay.game.popstarog.scene.OZScene;

/**
 * 在游戏结束时候的效果
 * 
 * @author Administrator
 * 
 */
public class OZExister {

	private final static String TAG = "OZExister";

	/**
	 * 将形状移动出去
	 * 
	 * @param addedShapeList
	 * @param moveListener
	 */
	public static void moveShapeOut(List<OZShape> addedShapeList,
			OZShapeMoveListener moveListener) {
		for (OZShape shape : addedShapeList) {
			shape.moveOut(moveListener);
		}
	}

	public static void moveAllStarsOut(final int[][] starSigns,
			final AnimatedSprite[][] starSprites,
			final IEntityModifierListener listener, final OZScene ozScene) {
		new Thread(new Runnable() {
			public void run() {
				if (ozScene.isGameOn()) {
					for (int z = 0; z < 5; z++) {
						int iFrom = 4 - z;
						int iTo = 5 + z;

						int jFrom = 4 - z;
						int jTo = 5 + z;

						// 横向
						for (int i = iFrom; i <= iTo; i++) {
							int j = jFrom;
							if (starSigns[i][j] != GameConstants.BLOCK_NONE) {
								final ScaleModifier scaleModifier = new ScaleModifier(
										0.5f, 1f, 0f, listener, EaseBackIn
												.getInstance());
								starSprites[i][j]
										.registerEntityModifier(scaleModifier);
							}
							j = jTo;
							if (starSigns[i][j] != GameConstants.BLOCK_NONE) {
								final ScaleModifier scaleModifier = new ScaleModifier(
										0.5f, 1f, 0f, listener, EaseBackIn
												.getInstance());
								starSprites[i][j]
										.registerEntityModifier(scaleModifier);
							}
						}
						// 竖向
						for (int j = jFrom + 1; j <= jTo - 1; j++) {
							int i = iFrom;
							if (starSigns[i][j] != GameConstants.BLOCK_NONE) {
								final ScaleModifier scaleModifier = new ScaleModifier(
										0.5f, 1f, 0f, listener, EaseBackIn
												.getInstance());
								starSprites[i][j]
										.registerEntityModifier(scaleModifier);
							}
							i = iTo;
							if (starSigns[i][j] != GameConstants.BLOCK_NONE) {
								final ScaleModifier scaleModifier = new ScaleModifier(
										0.5f, 1f, 0f, listener, EaseBackIn
												.getInstance());
								starSprites[i][j]
										.registerEntityModifier(scaleModifier);
							}
						}
						if (ozScene.isGameOn()) {
							try {
								Thread.sleep(50);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						} else {
							break;
						}
					}
				}
			}
		}).start();

	}

	public static void moveAllContainerOut(final OZScene ozScene,
			final Sprite[][] containerSprites,
			VertexBufferObjectManager vertexBufferObjectManager,
			final IEntityModifierListener listener) {
		LogUtils.v(TAG, "星星消除完毕，开始消除星星容器...");
		new Thread(new Runnable() {
			public void run() {
				if (ozScene.isGameOn()) {
					for (int z = 0; z < 5; z++) {
						int iFrom = 4 - z;
						int iTo = 5 + z;

						int jFrom = 4 - z;
						int jTo = 5 + z;

						// 横向
						for (int i = iFrom; i <= iTo; i++) {
							int j = jFrom;
							ScaleModifier scaleModifier = new ScaleModifier(
									0.5f, 1f, 0f, listener, EaseBackIn
											.getInstance());
							containerSprites[i][j]
									.registerEntityModifier(scaleModifier);
							j = jTo;
							scaleModifier = new ScaleModifier(0.5f, 1f, 0f,
									listener, EaseBackIn.getInstance());
							containerSprites[i][j]
									.registerEntityModifier(scaleModifier);

						}
						// 竖向
						for (int j = jFrom + 1; j <= jTo - 1; j++) {
							int i = iFrom;
							ScaleModifier scaleModifier = new ScaleModifier(
									0.5f, 1f, 0f, listener, EaseBackIn
											.getInstance());
							containerSprites[i][j]
									.registerEntityModifier(scaleModifier);
							i = iTo;
							scaleModifier = new ScaleModifier(0.5f, 1f, 0f,
									listener, EaseBackIn.getInstance());
							containerSprites[i][j]
									.registerEntityModifier(scaleModifier);
						}
						if (ozScene.isGameOn()) {
							try {
								Thread.sleep(50);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						} else {
							break;
						}
					}
				}
			}
		}).start();

	}

}
