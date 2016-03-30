package com.zplay.game.popstarog.ozshape;

import java.util.List;

import com.orange.entity.IEntity;
import com.orange.entity.modifier.IEntityModifier.IEntityModifierListener;
import com.orange.entity.modifier.LoopEntityModifier;
import com.orange.entity.modifier.MoveModifier;
import com.orange.entity.modifier.ParallelEntityModifier;
import com.orange.entity.modifier.RotationByModifier;
import com.orange.entity.modifier.ScaleModifier;
import com.orange.entity.modifier.SequenceEntityModifier;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.util.modifier.IModifier;
import com.orange.util.modifier.ease.EaseBounceOut;
import com.orange.util.modifier.ease.EaseExponentialOut;
import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.sprite.Position;

/**
 * 小点道具，关于类名中的‘s’，你可以理解成"special"，我实在想不到一个更加有描述性的名称了
 * 
 * @author glzlaohuai
 * 
 */
public class OZShape1s extends OZShape {

	public OZShape1s() {
		this(GameConstants.BLOCK_9, new int[][] { { GameConstants.BLOCK_9 } });
	}

	public OZShape1s(int sign, int[][] shapeSigns) {
		super(sign, shapeSigns);
	}

	public void scaleForever() {
		List<AnimatedSprite> spriteList = getSpriteList();
		LoopEntityModifier scale = new LoopEntityModifier(
				new SequenceEntityModifier(new ScaleModifier(0.5f, 1.0f, 1.2f),
						new ScaleModifier(0.5f, 1.2f, 1.0f)));
		for (AnimatedSprite sprite : spriteList) {
			sprite.registerEntityModifier(scale.deepCopy());
		}
	}
	
	public void rotateShape() {
		RotationByModifier leftRotation = new RotationByModifier(0.05f, -30f);
		RotationByModifier rightRotation = new RotationByModifier(0.05f, 30f);
		SequenceEntityModifier modifier = new SequenceEntityModifier(
				leftRotation, rightRotation, rightRotation, leftRotation,
				leftRotation, rightRotation, rightRotation, leftRotation,
				leftRotation, rightRotation);
		for (AnimatedSprite sprite : getSpriteList()) {
			sprite.registerEntityModifier(modifier.deepCopy());
		}
	}

	public void stopAllActionAndResetState() {
		List<AnimatedSprite> spriteList = getSpriteList();
		for (AnimatedSprite sprite : spriteList) {
			sprite.clearEntityModifiers();
			sprite.setScale(1);
		}
	}

	/**
	 * 通过scale动画展示出来，该方法是在
	 * {@linkplain OZShapeBuildAndIniter#buildAndInitOneBlockShape(com.orange.opengl.vbo.VertexBufferObjectManager)}
	 * 方法和{@linkplain OZShape#attach(com.orange.entity.group.IEntityGroup)}
	 * 执行之后执行的
	 */
	public void showOut() {
		List<AnimatedSprite> spriteList = getSpriteList();
		for (AnimatedSprite sprite : spriteList) {
			sprite.clearEntityModifiers();
			sprite.setScale(0);
			ScaleModifier scaleModifier = new ScaleModifier(0.5f, 0f, 1.0f);
			sprite.registerEntityModifier(scaleModifier);
		}
	}

	public void jump(float x, float y) {
		if (isMoveingOut() || isOut())
			return;
		float startX = x - getShapeSpreadWidth() / 2;
		float startY = y - GameConstants.OZ_GROUP_CONTAINER_HEIGHT / 4
				- getShapeSpreadHeight();

		for (int i = 0; i < getSpriteList().size(); i++) {
			AnimatedSprite sprite = getSpriteList().get(i);
			Position position = getPositionList().get(i);

			ScaleModifier scaleModifier = new ScaleModifier(0.4f, 1f,
					GameConstants.OZ_STAR_SIZE.getWidth()
							/ GameConstants.ONE_BLOCK_SIZE.getWidth() - 0.15f,
					1f, GameConstants.OZ_STAR_SIZE.getHeight()
							/ GameConstants.ONE_BLOCK_SIZE.getHeight() - 0.15f,
					EaseBounceOut.getInstance());
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

	public void fill(int[][] starSigns, final AnimatedSprite[][] starSprites,
			final int x, final int y, final OZShapeMoveListener fillListener) {
		for (int i = 0; i < getShapeSigns().length; i++) {
			for (int j = 0; j < getShapeSigns()[0].length; j++) {
				if (getShapeSigns()[i][j] != GameConstants.BLOCK_NONE) {
					starSigns[x + i][y + j] = getShapeSigns()[i][j];
					starSprites[x + i][y + j].setCurrentTileIndex(getType());
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
				if (finished == getSpriteList().size()) {
					for (int i = 0; i < getPositionList().size(); i++) {
						Position position = getPositionList().get(i);
						starSprites[x + position.getX()][y + position.getY()]
								.setScale(1);
					}
					if (fillListener != null) {
						fillListener.afterMove();
					}
				}
			}
		};

		for (int i = 0; i < getSpriteList().size(); i++) {
			AnimatedSprite sprite = getSpriteList().get(i);
			Position position = getPositionList().get(i);

			MoveModifier move = new MoveModifier(
					0.2f,
					sprite.getX(),
					starSprites[x + position.getX()][y + position.getY()]
							.getX()
							+ (sprite.getWidthHalf()
									* (GameConstants.OZ_STAR_SIZE.getWidth() / GameConstants.ONE_BLOCK_SIZE
											.getWidth()) - sprite
										.getWidthHalf()),
					sprite.getY(),
					starSprites[x + position.getX()][y + position.getY()]
							.getY()
							+ (sprite.getHeightHalf()
									* (GameConstants.OZ_STAR_SIZE.getHeight() / GameConstants.ONE_BLOCK_SIZE
											.getHeight()) - sprite
										.getHeightHalf()));

			ScaleModifier scaleModifier = new ScaleModifier(0.2f,
					GameConstants.OZ_STAR_SIZE.getWidth()
							/ GameConstants.ONE_BLOCK_SIZE.getWidth() - 0.15f,
					GameConstants.OZ_STAR_SIZE.getWidth()
							/ GameConstants.ONE_BLOCK_SIZE.getWidth(),
					GameConstants.OZ_STAR_SIZE.getHeight()
							/ GameConstants.ONE_BLOCK_SIZE.getHeight() - 0.15f,
					GameConstants.OZ_STAR_SIZE.getHeight()
							/ GameConstants.ONE_BLOCK_SIZE.getHeight());

			ParallelEntityModifier sequence = new ParallelEntityModifier(move,
					scaleModifier);

			sequence.addModifierListener(listener);
			sprite.registerEntityModifier(sequence);
		}
	}
}
