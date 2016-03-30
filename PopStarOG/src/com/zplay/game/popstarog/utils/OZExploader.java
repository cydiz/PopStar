package com.zplay.game.popstarog.utils;

import java.util.ArrayList;
import java.util.List;

import com.e7studio.android.e7appsdk.utils.LogUtils;
import com.orange.entity.modifier.ScaleModifier;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.util.modifier.ease.EaseBackIn;
import com.zplay.game.popstarog.layer.OZGameLayer;
import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.ozshape.OZShape;
import com.zplay.game.popstarog.ozshape.OZShapeMoveListener;
import com.zplay.game.popstarog.scene.OZScene;
import com.zplay.game.popstarog.sprite.Position;

/**
 * 在1010游戏模式下，对“满”了的行和列的星星进行消除
 * 
 * @author glzlaohuai
 * @version 2014-11-8
 */
public class OZExploader {

	private final static long INTERVAL = 50;

	private final static String TAG = "OZExploader";

	/**
	 * 
	 * @param mainScene
	 * @param starSigns
	 * @param x
	 * @param y
	 * @param shape
	 * @param exploadeListener
	 */
	public static void exploade(final OZScene mainScene,
			final OZGameLayer modeLayer, final int[][] starSigns,
			final AnimatedSprite[][] sprites, int x, int y,
			final OZShape shape, final List<Integer> rowList,
			final List<Integer> columnList,
			final VertexBufferObjectManager vertexBufferObjectManager,
			final OZShapeMoveListener exploadeListener) {

		if (mainScene.isGameOn()) {
			// 只有行
			if (rowList.size() != 0 && columnList.size() == 0) {
				int containFrom = x;
				int containTo = x + shape.getShapeSigns().length - 1;
				// 从最左边开始爆炸
				if (containFrom == 0) {
					new Thread(new Runnable() {
						public void run() {
							for (int i = 0; i < 10 && mainScene.isGameOn(); i++) {
								for (Integer j : rowList) {
									doAddParticle(sprites, i, j, modeLayer,
											starSigns,
											vertexBufferObjectManager);
								}
								if (mainScene.isGameOn()) {
									try {
										Thread.sleep(INTERVAL);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
							if (mainScene.isGameOn()
									&& exploadeListener != null) {
								exploadeListener.afterMove();
							}
						}
					}).start();
				}

				// 从最右边开始爆炸
				if (containTo == 9) {
					new Thread(new Runnable() {
						public void run() {
							for (int i = 9; i >= 0 && mainScene.isGameOn(); i--) {
								for (Integer j : rowList) {
									doAddParticle(sprites, i, j, modeLayer,
											starSigns,
											vertexBufferObjectManager);
								}
								if (mainScene.isGameOn()) {
									try {
										Thread.sleep(INTERVAL);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
							if (mainScene.isGameOn()
									&& exploadeListener != null) {
								exploadeListener.afterMove();
							}
						}
					}).start();
				}

				// 从中间向两边
				if (containFrom != 0 && containTo != 9) {
					int startX = 0;
					// 偏右
					if (containFrom > 5) {
						startX = containFrom;
					}
					// 偏左
					if (containTo < 5) {
						startX = containTo;
					}
					// 包含5
					if (containFrom <= 5 && containTo >= 5) {
						startX = 5;
					}
					final int startXTemp = startX;
					new Thread(new Runnable() {
						public void run() {
							for (int i = 0; i < 10 && mainScene.isGameOn(); i++) {
								int leftX = startXTemp - i;
								int rightX = startXTemp + i;
								if (leftX < 0 && rightX > 9) {
									break;
								}
								for (Integer j : rowList) {
									if (leftX == rightX) {
										doAddParticle(sprites, leftX, j,
												modeLayer, starSigns,
												vertexBufferObjectManager);
									}
									if (leftX != rightX) {
										if (leftX >= 0) {
											doAddParticle(sprites, leftX, j,
													modeLayer, starSigns,
													vertexBufferObjectManager);
										}
										if (rightX <= 9) {
											doAddParticle(sprites, rightX, j,
													modeLayer, starSigns,
													vertexBufferObjectManager);
										}
									}
								}
								if (mainScene.isGameOn()) {
									try {
										Thread.sleep(INTERVAL);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
							if (mainScene.isGameOn()
									&& exploadeListener != null) {
								exploadeListener.afterMove();
							}

						}
					}).start();
				}
			}
			// 只有列
			if (rowList.size() == 0 && columnList.size() != 0) {
				int containFrom = y;
				int containTo = y + shape.getShapeSigns()[0].length - 1;
				// 从最上边
				if (containFrom == 0) {
					new Thread(new Runnable() {
						public void run() {
							for (int j = 0; j < 10 && mainScene.isGameOn(); j++) {
								for (Integer i : columnList) {
									doAddParticle(sprites, i, j, modeLayer,
											starSigns,
											vertexBufferObjectManager);
								}
								if (mainScene.isGameOn()) {
									try {
										Thread.sleep(INTERVAL);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
							if (mainScene.isGameOn()
									&& exploadeListener != null) {
								exploadeListener.afterMove();
							}
						}
					}).start();
				}

				// 从最下边开始爆炸
				if (containTo == 9) {
					new Thread(new Runnable() {
						public void run() {
							for (int j = 9; j >= 0 && mainScene.isGameOn(); j--) {
								for (Integer i : columnList) {
									doAddParticle(sprites, i, j, modeLayer,
											starSigns,
											vertexBufferObjectManager);
								}
								if (mainScene.isGameOn()) {
									try {
										Thread.sleep(INTERVAL);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
							if (mainScene.isGameOn()
									&& exploadeListener != null) {
								exploadeListener.afterMove();
							}
						}
					}).start();
				}

				// 从中间向两边
				if (containFrom != 0 && containTo != 9) {
					int startY = 0;
					// 偏右
					if (containFrom > 5) {
						startY = containFrom;
					}
					// 偏左
					if (containTo < 5) {
						startY = containTo;
					}
					// 包含5
					if (containFrom <= 5 && containTo >= 5) {
						startY = 5;
					}
					final int startYTemp = startY;
					new Thread(new Runnable() {
						public void run() {
							for (int z = 0; z < 10 && mainScene.isGameOn(); z++) {
								int topY = startYTemp - z;
								int bottomY = startYTemp + z;
								if (topY < 0 && bottomY > 9) {
									break;
								}
								for (Integer i : columnList) {
									if (topY == bottomY) {
										doAddParticle(sprites, i, topY,
												modeLayer, starSigns,
												vertexBufferObjectManager);
									}
									if (topY != bottomY) {
										if (topY >= 0) {
											doAddParticle(sprites, i, topY,
													modeLayer, starSigns,
													vertexBufferObjectManager);
										}
										if (bottomY <= 9) {
											doAddParticle(sprites, i, bottomY,
													modeLayer, starSigns,
													vertexBufferObjectManager);
										}
									}
								}
								if (mainScene.isGameOn()) {
									try {
										Thread.sleep(INTERVAL);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
							if (mainScene.isGameOn()
									&& exploadeListener != null) {
								exploadeListener.afterMove();
							}

						}
					}).start();
				}
			}

			// 行跟列都能消。。。。。。。。
			if (rowList.size() != 0 && columnList.size() != 0) {
				LogUtils.v(TAG, "消除交叉形状...");
				new Thread(new Runnable() {
					public void run() {
						List<Position> positionList = new ArrayList<Position>();
						for (int z = 0; z < 10 && mainScene.isGameOn(); z++) {

							for (int ii = 0; ii < columnList.size(); ii++) {
								int startX = columnList.get(ii);
								for (int jj = 0; jj < rowList.size(); jj++) {
									int startY = rowList.get(jj);
									int topY = startY - z;
									int downY = startY + z;
									if (topY < 0 && downY > 9) {
										break;
									}
									if (topY == downY
											&& !positionList
													.contains(new Position(
															startX, topY))) {
										doAddParticle(sprites, startX, topY,
												modeLayer, starSigns,
												vertexBufferObjectManager);
										positionList.add(new Position(startX,
												topY));
									}
									if (topY != downY) {
										if (topY >= 0
												&& !positionList
														.contains(new Position(
																startX, topY))) {
											doAddParticle(sprites, startX,
													topY, modeLayer, starSigns,
													vertexBufferObjectManager);
											positionList.add(new Position(
													startX, topY));
										}
										if (downY <= 9
												&& !positionList
														.contains(new Position(
																startX, downY))) {
											doAddParticle(sprites, startX,
													downY, modeLayer,
													starSigns,
													vertexBufferObjectManager);
											positionList.add(new Position(
													startX, downY));

										}
									}

								}
							}

							for (int jj = 0; jj < rowList.size(); jj++) {
								int startY = rowList.get(jj);
								LogUtils.v(TAG, "消除第:" + startY + "行的星星...");
								for (int ii = 0; ii < columnList.size(); ii++) {
									int startX = columnList.get(ii);
									int leftX = startX - z;
									int rightX = startX + z;
									if (leftX < 0 && rightX > 9) {
										break;
									}
									if (leftX == rightX
											&& !positionList
													.contains(new Position(
															leftX, startY))) {
										doAddParticle(sprites, leftX, startY,
												modeLayer, starSigns,
												vertexBufferObjectManager);
										positionList.add(new Position(leftX,
												startY));
									}
									if (leftX != rightX) {
										if (leftX >= 0
												&& !positionList
														.contains(new Position(
																leftX, startY))) {
											doAddParticle(sprites, leftX,
													startY, modeLayer,
													starSigns,
													vertexBufferObjectManager);
											positionList.add(new Position(
													leftX, startY));
										}
										if (rightX <= 9
												&& !positionList
														.contains(new Position(
																rightX, startY))) {
											doAddParticle(sprites, rightX,
													startY, modeLayer,
													starSigns,
													vertexBufferObjectManager);
											positionList.add(new Position(
													rightX, startY));
										}
									}
								}
							}
							if (mainScene.isGameOn()) {
								try {
									Thread.sleep(INTERVAL);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							} else {
								break;
							}
						}

						int needExploade = (rowList.size() + columnList.size())
								* 10 - rowList.size() * columnList.size();

						LogUtils.v(TAG, "清楚的数量为：" + positionList.size()
								+ ",需要清除的数量为：" + needExploade);

						if (mainScene.isGameOn() && exploadeListener != null
								&& positionList.size() == needExploade) {
							exploadeListener.afterMove();
						}
					}
				}).start();

			}
		}
	}

	private static void doAddParticle(AnimatedSprite[][] sprites, int i, int j,
			OZGameLayer modeLayer, int[][] starSigns,
			VertexBufferObjectManager vertexBufferObjectManager) {
		// OZParticle particle = OZParticleMaker.make(vertexBufferObjectManager,
		// sprites[i][j].getCentreX() - sprites[i][j].getWidthHalf(),
		// sprites[i][j].getCentreY() - sprites[i][j].getHeightHalf(),
		// sprites[i][j].getCurrentTileIndex());
		// particle.setCentrePosition(sprites[i][j].getCentreX(),
		// sprites[i][j].getCentreY());
		// modeLayer.attachChild(particle);
		// sprites[i][j].setScale(0);
		// starSigns[i][j] = GameConstants.BLOCK_NONE;

		// TODO
		sprites[i][j].registerEntityModifier(new ScaleModifier(0.3f, 1.0f, 0f,
				EaseBackIn.getInstance()));
		starSigns[i][j] = GameConstants.BLOCK_NONE;

	}
}
