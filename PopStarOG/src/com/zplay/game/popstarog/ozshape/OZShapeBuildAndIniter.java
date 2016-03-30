package com.zplay.game.popstarog.ozshape;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.orange.entity.sprite.AnimatedSprite;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.util.size.Size;
import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.sprite.Position;
import com.zplay.game.popstarog.utils.SizeHelper;

/**
 * 根据{@linkplain OZShapeIDGenerator}生成的组合形状id(也就是在{@linkplain OZShapeMapper}
 * 中wrap的{@linkplain Map}<{@linkplain Integer},{@linkplain OZShape} >结构的key)来构造
 * {@linkplain OZShape}的实例，并且对该实例进行初始化操作
 * 
 * @author Administrator
 * 
 */
public class OZShapeBuildAndIniter {

	/**
	 * 构建{@linkplain OZShape}并对其做初始化操作
	 * 
	 * @param shapeIDs
	 * @return
	 */
	public static List<OZShape> buildAndInitOZShape(int[] groupSigns,
			VertexBufferObjectManager vertexBufferObjectManager) {
		List<OZShape> shapeList = new ArrayList<OZShape>();
		for (int i = 0; i < groupSigns.length; i++) {
			if (groupSigns[i] != GameConstants.BLOCK_NONE) {
				OZShape shape = null;
				try {
					shape = OZShapeMapper.getShapeMap().get(groupSigns[i])
							.newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
				shapeList.add(shape);
				int[][] shapeSigns = shape.getShapeSigns();
				float shapeWidth = shapeSigns.length
						* GameConstants.OZ_GROUP_SIZE.getWidth();
				float shapeHeight = shapeSigns[0].length
						* GameConstants.OZ_GROUP_SIZE.getHeight();

				float shapeStartX = GameConstants.OZ_GROUP_CONTAINER_WIDTH * i
						+ (GameConstants.OZ_GROUP_CONTAINER_WIDTH - shapeWidth)
						/ 2;
				float shapeStartY = GameConstants.OZ_GROUP_START_Y
						+ (GameConstants.OZ_GROUP_CONTAINER_HEIGHT
								- GameConstants.AD_BANNER_HEIGHT - shapeHeight)
						/ 2;
				int index = i;
				List<AnimatedSprite> spriteList = new ArrayList<AnimatedSprite>();
				List<Position> positionList = new ArrayList<Position>();
				shape.init(index, shapeStartX, shapeStartY, spriteList,
						positionList);
				for (int ii = 0; ii < shapeSigns.length; ii++) {
					for (int jj = 0; jj < shapeSigns[ii].length; jj++) {
						if (shapeSigns[ii][jj] != GameConstants.BLOCK_NONE) {
							AnimatedSprite sprite = new AnimatedSprite(0, 0,
									"star", vertexBufferObjectManager);
							sprite.setCurrentTileIndex(shape.getType());
							Position position = new Position(ii, jj);
							sprite.setPosition(
									shapeStartX
											+ ii
											* GameConstants.OZ_GROUP_SIZE
													.getWidth()
											+ GameConstants.BASE_WIDTH,
									shapeStartY
											+ jj
											* GameConstants.OZ_GROUP_SIZE
													.getHeight());
							sprite.setSize(
									GameConstants.OZ_GROUP_SIZE.getWidth(),
									GameConstants.OZ_GROUP_SIZE.getHeight());
							sprite.setScaleCenter(sprite.getWidthHalf(),
									sprite.getHeightHalf());
							spriteList.add(sprite);
							positionList.add(position);
						}
					}
				}
			}
		}
		return shapeList;
	}

	/**
	 * 构建{@linkplain OZShape1}并对其做初始化操作
	 * 
	 * @param shapeIDs
	 * @return
	 */
	public static OZShape1s buildAndInitOneBlockShape(
			VertexBufferObjectManager vertexBufferObjectManager) {
		OZShape1s shape = new OZShape1s();
		int[][] shapeSigns = shape.getShapeSigns();

		GameConstants.ONE_BLOCK_SIZE = SizeHelper.ogSizeScale(44, 44);
		Size shapeSize = GameConstants.ONE_BLOCK_SIZE;

		float shapeStartX = GameConstants.ONE_BLOCK_START_X;
		float shapeStartY = GameConstants.ONE_BLOCK_START_Y;

		List<AnimatedSprite> spriteList = new ArrayList<AnimatedSprite>();
		List<Position> positionList = new ArrayList<Position>();
		shape.init(0, shapeStartX, shapeStartY, spriteList, positionList);
		for (int ii = 0; ii < shapeSigns.length; ii++) {
			for (int jj = 0; jj < shapeSigns[ii].length; jj++) {
				if (shapeSigns[ii][jj] != GameConstants.BLOCK_NONE) {
					AnimatedSprite sprite = new AnimatedSprite(0, 0, "star",
							vertexBufferObjectManager);
					sprite.setCurrentTileIndex(shape.getType());
					Position position = new Position(ii, jj);
					sprite.setPosition(shapeStartX + ii * shapeSize.getWidth(),
							shapeStartY + jj * shapeSize.getHeight());
					sprite.setSize(shapeSize.getWidth(), shapeSize.getHeight());
					sprite.setScaleCenter(sprite.getWidthHalf(),
							sprite.getHeightHalf());
					sprite.setScale(0);
					spriteList.add(sprite);
					positionList.add(position);
				}
			}
		}
		shape.setOut(false);
		return shape;
	}

}
