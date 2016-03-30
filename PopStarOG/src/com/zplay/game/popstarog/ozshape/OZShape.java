package com.zplay.game.popstarog.ozshape;

import java.util.List;

import com.e7studio.android.e7appsdk.utils.LogUtils;
import com.orange.entity.IEntity;
import com.orange.entity.group.IEntityGroup;
import com.orange.entity.modifier.FadeInModifier;
import com.orange.entity.modifier.FadeOutModifier;
import com.orange.entity.modifier.IEntityModifier.IEntityModifierListener;
import com.orange.entity.modifier.IEntityModifier.IEntityModifierMatcher;
import com.orange.entity.modifier.MoveModifier;
import com.orange.entity.modifier.ParallelEntityModifier;
import com.orange.entity.modifier.ScaleModifier;
import com.orange.entity.modifier.SequenceEntityModifier;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.util.modifier.IModifier;
import com.orange.util.modifier.ease.EaseBounceOut;
import com.orange.util.modifier.ease.EaseElasticOut;
import com.orange.util.modifier.ease.EaseExponentialOut;
import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.sprite.Position;
import com.zplay.game.popstarog.utils.IntegerContainer;

/**
 * 1010模式中的组合形状
 * 
 * @author Administrator
 * 
 */
public class OZShape {

	private final static String TAG = "OZShape";

	// 匹配的阀值(在拖动过程中，该形状和方格能匹配上的最大的偏差距离，用跟blockContainer的width或者height的比率来确定)
	public final static float MATCH_THREAD = 0.5f;

	// 拖动过程中，组合形状与手指接触点直接的距离
	private final static float DISTANCE = GameConstants.OZ_GROUP_CONTAINER_HEIGHT / 4;

	private static float pickScaleX = GameConstants.OZ_STAR_SIZE.getWidth()
			/ GameConstants.OZ_GROUP_SIZE.getWidth() - 0.15f;
	private static float pickScaleY = GameConstants.OZ_STAR_SIZE.getHeight()
			/ GameConstants.OZ_GROUP_SIZE.getHeight() - 0.15f;

	private static float dropScaleX = pickScaleX + 0.15f;
	private static float dropScaleY = pickScaleY + 0.15f;

	// 代表星星的类型
	private int type;
	// 代表形状
	private int[][] shapeSigns;

	// 展示形状的AnimatedSprite;
	private List<AnimatedSprite> spriteList;
	// positionList与spriteList一一对应，代表标示该sprite的int在shapeSigns的数组下标
	private List<Position> positionList;

	// 初始位置
	private float startX;
	private float startY;

	// 底部有三个，代表形状所处的位置:0|1|2
	private int index;

	// 当被拖动时候（亦即展开之后的）宽度和高度
	private float shapeSpreadWidth;
	private float shapeSpreadHeight;

	// 标示，该形状是在handleUpOrMove中因为没有匹配上，正在原路返回
	private boolean isBacking = false;

	// 是否正在处理move事件
	private boolean isInMoveState = false;

	private boolean isMoveingOut = false;
	private boolean isOut = true;

	public boolean contains(float x, float y) {
		for (AnimatedSprite sprite : spriteList) {
			if (sprite.contains(x, y)) {
				return true;
			}
		}
		return false;
	}

	public OZShape() {
	}

	public boolean isMoveingOut() {
		return isMoveingOut;
	}

	public void setMoveingOut(boolean isMoveingOut) {
		this.isMoveingOut = isMoveingOut;
	}

	public OZShape(int type, int[][] shapeSigns) {
		this.type = type;
		this.shapeSigns = shapeSigns;
	}

	public void init(int index, float startX, float startY,
			List<AnimatedSprite> spriteList, List<Position> positionList) {
		this.index = index;
		this.startX = startX;
		this.startY = startY;
		this.spriteList = spriteList;
		this.positionList = positionList;

		shapeSpreadWidth = shapeSigns.length
				* GameConstants.OZ_STAR_SIZE.getWidth()
				+ (shapeSigns.length - 1)
				* ((GameConstants.OZ_CONTAINER_SIZE.getWidth() - GameConstants.OZ_STAR_SIZE
						.getWidth()) / 2);

		shapeSpreadHeight = shapeSigns[0].length
				* GameConstants.OZ_STAR_SIZE.getHeight()
				+ (shapeSigns[0].length - 1)
				* ((GameConstants.OZ_CONTAINER_SIZE.getHeight() - GameConstants.OZ_STAR_SIZE
						.getHeight()) / 2);
	}

	public float getShapeSpreadWidth() {
		return shapeSpreadWidth;
	}

	public float getShapeSpreadHeight() {
		return shapeSpreadHeight;
	}

	public boolean isOut() {
		return isOut;
	}

	public void setOut(boolean isOut) {
		this.isOut = isOut;
	}

	public boolean isBacking() {
		return isBacking;
	}

	public List<Position> getPositionList() {
		return positionList;
	}

	public boolean isMatch(int[][] starSigns) {
		for (int i = 0; i + shapeSigns.length <= starSigns.length; i++) {
			for (int j = 0; j + shapeSigns[0].length <= starSigns[0].length; j++) {
				int matchNum = 0;
				boolean isShouldBreak = false;
				for (int ii = 0; ii < shapeSigns.length; ii++) {
					for (int jj = 0; jj < shapeSigns[0].length; jj++) {
						if (shapeSigns[ii][jj] == GameConstants.BLOCK_NONE) {
							matchNum++;
						} else {
							if (starSigns[i + ii][j + jj] == GameConstants.BLOCK_NONE
									|| starSigns[i + ii][j + jj] == GameConstants.BLOCK_EXPLOADING) {
								matchNum++;
								if (matchNum == shapeSigns.length
										* shapeSigns[0].length) {
									return true;
								}
							} else {
								isShouldBreak = true;
								break;
							}
						}
					}
					if (isShouldBreak) {
						break;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 在进行按下-拖动-放开以后，通过调用方法{@link #getNearX()}以及{@link #getNearY()}
	 * 来获取到最接近的{x,y}索引(相对于starSigns来说)，然后通过调用该方法来确定，starSigns是否能容下这个形状
	 * 
	 * @param starSigns
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isMatch(int[][] starSigns, int x, int y) {
		LogUtils.v(TAG, "开始进行匹配...，需要的数量：" + shapeSigns.length
				* shapeSigns[0].length + ",匹配开始的位置：" + x + "," + y + ",形状的长度："
				+ shapeSigns.length + ",形状的高度：" + shapeSigns[0].length);

		if (x == GameConstants.BLOCK_NONE || y == GameConstants.BLOCK_NONE) {
			return false;
		} else {
			if (x + shapeSigns.length <= starSigns.length
					&& y + shapeSigns[0].length <= starSigns[0].length) {
				int matchNum = 0;
				boolean isShouldBreak = false;
				for (int i = 0; i < shapeSigns.length; i++) {
					for (int j = 0; j < shapeSigns[0].length; j++) {
						if (shapeSigns[i][j] == GameConstants.BLOCK_NONE) {
							matchNum++;
						} else {
							if (starSigns[x + i][y + j] == GameConstants.BLOCK_NONE) {
								matchNum++;
								if (matchNum == shapeSigns.length
										* shapeSigns[0].length) {
									LogUtils.v(TAG, "match数量不匹配...match了："
											+ matchNum + ",总共需要:"
											+ shapeSigns.length
											* shapeSigns[0].length);
									return true;
								}
							} else {
								isShouldBreak = true;
								break;
							}
						}
					}
					if (isShouldBreak) {
						break;
					}
				}
			}
			LogUtils.v(TAG, "最后就出来结束了...");
			return false;
		}
	}

	/**
	 * 在确定{@link #isMatch(int[][], int, int)}返回true之后，调用该方法来填充空缺的位置
	 * 
	 * @param starSigns
	 * @param x
	 * @param y
	 */
	public void fill(int[][] starSigns, final AnimatedSprite[][] starSprites,
			final int x, final int y, final OZShapeMoveListener fillListener) {
		for (int i = 0; i < shapeSigns.length; i++) {
			for (int j = 0; j < shapeSigns[0].length; j++) {
				if (shapeSigns[i][j] != GameConstants.BLOCK_NONE) {
					starSigns[x + i][y + j] = shapeSigns[i][j];
					starSprites[x + i][y + j].setCurrentTileIndex(type);
				}
			}
		}

		IEntityModifierListener listener = new IEntityModifierListener() {
			int finished = 0;

			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
			}

			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				finished++;
				if (finished == spriteList.size()) {
					for (int i = 0; i < positionList.size(); i++) {
						Position position = positionList.get(i);
						starSprites[x + position.getX()][y + position.getY()]
								.setScale(1);
					}
					if (fillListener != null) {
						fillListener.afterMove();
					}
				}
			}
		};

		for (int i = 0; i < spriteList.size(); i++) {
			AnimatedSprite sprite = spriteList.get(i);
			Position position = positionList.get(i);

			MoveModifier move = new MoveModifier(0.2f, sprite.getX(),
					starSprites[x + position.getX()][y + position.getY()]
							.getX()
							+ (sprite.getWidthHalf() * dropScaleX - sprite
									.getWidthHalf()), sprite.getY(),
					starSprites[x + position.getX()][y + position.getY()]
							.getY()
							+ (sprite.getHeightHalf() * dropScaleY - sprite
									.getHeightHalf()));

			ScaleModifier scaleModifier = new ScaleModifier(0.2f, pickScaleX,
					dropScaleX, pickScaleY, dropScaleY);

			ParallelEntityModifier sequence = new ParallelEntityModifier(move,
					scaleModifier);

			sequence.addModifierListener(listener);
			sprite.registerEntityModifier(sequence);
		}
		isInMoveState = true;
	}

	public int getType() {
		return type;
	}

	public int[][] getShapeSigns() {
		return shapeSigns;
	}

	public List<AnimatedSprite> getSpriteList() {
		return spriteList;
	}

	public float getStartX() {
		return startX;
	}

	public float getStartY() {
		return startY;
	}

	public int getIndex() {
		return index;
	}

	public void detachSelf() {
		for (AnimatedSprite sprite : spriteList) {
			sprite.clearEntityModifiers();
			sprite.detachSelf();
			sprite.dispose();
		}
		spriteList = null;
		positionList = null;
	}

	public void attach(IEntityGroup entityGroup) {
		for (AnimatedSprite sprite : spriteList) {
			entityGroup.attachChild(sprite);
		}
	}

	/**
	 * 从右侧进入
	 */
	public void moveIn(final OZShapeMoveListener moveListener) {
		final int totalTemp = spriteList.size();
		final IntegerContainer intContainer = new IntegerContainer(0);
		IEntityModifierListener modifierListener = new IEntityModifierListener() {
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
			}

			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				isOut = false;
				intContainer.add();
				if (intContainer.getValue() == totalTemp
						&& moveListener != null) {
					moveListener.afterMove();
				}
			}
		};
		for (int i = 0; i < spriteList.size(); i++) {
			AnimatedSprite sprite = spriteList.get(i);
			MoveModifier move = new MoveModifier(0.5f, sprite.getX(),
					sprite.getX() - GameConstants.BASE_WIDTH, sprite.getY(),
					sprite.getY(), modifierListener,
					EaseExponentialOut.getInstance());
			sprite.registerEntityModifier(move);
		}
	}

	/**
	 * 从左侧滑出
	 */
	public void moveOut(final OZShapeMoveListener moveListener) {
		isMoveingOut = true;
		final int totalTemp = spriteList.size();
		final IntegerContainer intContainer = new IntegerContainer(0);
		IEntityModifierListener modifierListener = new IEntityModifierListener() {
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
			}

			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				intContainer.add();
				if (intContainer.getValue() == totalTemp
						&& moveListener != null) {
					moveListener.afterMove();
				}
			}
		};
		for (int i = 0; i < spriteList.size(); i++) {
			AnimatedSprite sprite = spriteList.get(i);
			MoveModifier move = new MoveModifier(0.5f, sprite.getX(),
					sprite.getX() - GameConstants.BASE_WIDTH, sprite.getY(),
					sprite.getY(), modifierListener,
					EaseExponentialOut.getInstance());
			sprite.registerEntityModifier(move);
		}
	}

	public void jump(float x, float y) {
		if (isMoveingOut || isOut)
			return;
		float startX = x - shapeSpreadWidth / 2;
		float startY = y - DISTANCE - shapeSpreadHeight;

		for (int i = 0; i < spriteList.size(); i++) {
			AnimatedSprite sprite = spriteList.get(i);
			Position position = positionList.get(i);

			ScaleModifier scaleModifier = new ScaleModifier(0.4f, 1f,
					pickScaleX, 1f, pickScaleY, EaseBounceOut.getInstance());
			MoveModifier moveTo = new MoveModifier(0.2f, sprite.getX(), startX
					+ position.getX()
					* GameConstants.OZ_CONTAINER_SIZE.getWidth(),
					sprite.getY(), startY + position.getY()
							* GameConstants.OZ_CONTAINER_SIZE.getHeight(),
					EaseExponentialOut.getInstance());
			sprite.registerEntityModifier(moveTo);
			sprite.registerEntityModifier(scaleModifier);
		}
	}

	public void move(float x, float y) {
		if (isMoveingOut) {
			return;
		}
		float startX = x - shapeSpreadWidth / 2;
		float startY = y - DISTANCE - shapeSpreadHeight;
		for (int i = 0; i < spriteList.size(); i++) {
			AnimatedSprite sprite = spriteList.get(i);
			Position position = positionList.get(i);
			if (!isInMoveState) {
				sprite.unregisterEntityModifiers(new IEntityModifierMatcher() {
					public boolean matches(IModifier<IEntity> pObject) {
						if (pObject instanceof MoveModifier)
							return true;
						return false;
					}
				});
			}
			sprite.setPosition(
					startX + position.getX()
							* GameConstants.OZ_CONTAINER_SIZE.getWidth(),
					startY + position.getY()
							* GameConstants.OZ_CONTAINER_SIZE.getHeight());
		}
		isInMoveState = true;
	}

	/**
	 * 获取最接近的方格的x下标
	 * 
	 * @return
	 */
	public int getNearX() {
		AnimatedSprite firstSprite = spriteList.get(0);
		float x = firstSprite.getLeftX()
				- (firstSprite.getWidthScaledHalf() - firstSprite
						.getWidthHalf());
		float diff = x - GameConstants.OZ_PADDING_X;
		LogUtils.v(TAG, "shape的x坐标为:" + x);
		if (diff >= 0) {
			float result = diff / GameConstants.OZ_CONTAINER_SIZE.getWidth();
			LogUtils.v(TAG, "diff除以方块的宽度得到的结果为：" + result);
			int index = (int) (result);
			float missMatch = result - index;
			if (missMatch > MATCH_THREAD) {
				if (1 - missMatch < MATCH_THREAD) {
					return ++index;
				} else {
					return -1;
				}
			} else {
				return index;
			}
		} else {
			if (Math.abs(diff) > GameConstants.OZ_CONTAINER_SIZE.getWidth()
					* MATCH_THREAD) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	/**
	 * 获取最接近的方格的y下标
	 * 
	 * @return
	 */
	public int getNearY() {
		AnimatedSprite firstSprite = spriteList.get(0);
		float y = firstSprite.getTopY()
				- (firstSprite.getHeightScaledHalf() - firstSprite
						.getHeightHalf());
		LogUtils.v(TAG, "shape的y坐标为：" + y);
		float diff = y - GameConstants.OZ_CONTAINER_START_Y;
		if (diff >= 0) {
			float result = diff / GameConstants.OZ_CONTAINER_SIZE.getHeight();
			int index = (int) (result);
			float missMatch = result - index;
			if (missMatch > MATCH_THREAD) {
				if (1 - missMatch > MATCH_THREAD) {
					return -1;
				} else {
					return ++index;
				}
			} else {
				return index;
			}
		} else {
			if (Math.abs(diff) > GameConstants.OZ_CONTAINER_SIZE.getHeight()
					* MATCH_THREAD) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	public void goBack() {
		isBacking = true;
		isInMoveState = false;
		AnimatedSprite exampleSprite = spriteList.get(0);

		float nowScaleX = exampleSprite.getScaleX();
		float nowScaleY = exampleSprite.getScaleY();

		IEntityModifierListener modifierListener = new IEntityModifierListener() {
			int num = 0;

			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
			}

			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				num++;
				if (num == spriteList.size()) {
					isBacking = false;
				}
			}
		};

		for (int i = 0; i < spriteList.size(); i++) {
			AnimatedSprite sprite = spriteList.get(i);
			sprite.clearEntityModifiers();
			Position position = positionList.get(i);

			ScaleModifier scale = new ScaleModifier(0.2f, nowScaleX, 1,
					nowScaleY, 1, EaseElasticOut.getInstance());
			MoveModifier moveTo = new MoveModifier(0.2f, sprite.getX(), startX
					+ position.getX() * GameConstants.OZ_GROUP_SIZE.getWidth(),
					sprite.getY(), startY + position.getY()
							* GameConstants.OZ_GROUP_SIZE.getHeight(),
					EaseExponentialOut.getInstance());
			moveTo.addModifierListener(modifierListener);
			sprite.registerEntityModifier(moveTo);
			sprite.registerEntityModifier(scale);
		}

	}

	//add by liufengqiang
	public void flashAnim() {
		FadeInModifier fadeIn = new FadeInModifier(0.05f);
		FadeOutModifier fadeOut = new FadeOutModifier(0.05f);
		SequenceEntityModifier modifier = new SequenceEntityModifier(fadeOut,
				fadeIn, fadeOut, fadeIn, fadeOut, fadeIn, fadeOut, fadeIn,
				fadeOut, fadeIn);
		
		for (AnimatedSprite sprite : getSpriteList()) {
			sprite.registerEntityModifier(modifier.deepCopy());
		}
	}
}
