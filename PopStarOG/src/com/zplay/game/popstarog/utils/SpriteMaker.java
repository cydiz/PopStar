package com.zplay.game.popstarog.utils;

import com.orange.entity.sprite.Sprite;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.res.RegionRes;

/**
 * 构建一个Sprite
 * 
 * @author Administrator
 * 
 */
public class SpriteMaker {

	public static Sprite makeSpriteWithTPFile(String name,
			VertexBufferObjectManager vertexBufferObjectManager) {
		Sprite sprite = new Sprite(0, 0, RegionRes.getRegion(name),
				vertexBufferObjectManager);
		return sprite;
	}

	public static Sprite makeSpriteWithSingleImageFile(String name,
			VertexBufferObjectManager vertexBufferObjectManager) {
		Sprite sprite = new Sprite(0, 0, RegionRes.getTextureRegion(name),
				vertexBufferObjectManager);
		return sprite;
	}

}
