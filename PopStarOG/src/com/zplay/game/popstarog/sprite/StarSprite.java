package com.zplay.game.popstarog.sprite;

import android.view.MotionEvent;

import com.orange.entity.sprite.AnimatedSprite;
import com.orange.input.touch.TouchEvent;
import com.orange.opengl.texture.region.ITiledTextureRegion;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.res.RegionRes;

public class StarSprite extends AnimatedSprite {

	public StarSprite(float pX, float pY, float pWidth, float pHeight,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion,
				pVertexBufferObjectManager);
	}

	public StarSprite(String regionName,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		this(0, 0, 0, 0, RegionRes.getRegion(regionName),
				pVertexBufferObjectManager);
	}

	private int type;
	private int indexX;
	private int indexY;
	private TouchCallback callback;
	private boolean isExploading;

	public void setCustomAttributes(int type, int indexX, int indexY,
			boolean isExploading) {
		this.type = type;
		this.indexX = indexX;
		this.indexY = indexY;
		this.isExploading = isExploading;
	}

	public void setIndexXY(int indexX, int indexY) {
		this.indexX = indexX;
		this.indexY = indexY;
	}

	public boolean isExploading() {
		return isExploading;
	}

	public void setExploading(boolean isExploading) {
		this.isExploading = isExploading;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIndexX() {
		return indexX;
	}

	public void setIndexX(int indexX) {
		this.indexX = indexX;
	}

	public int getIndexY() {
		return indexY;
	}

	public void setIndexY(int indexY) {
		this.indexY = indexY;
	}

	public TouchCallback getCallback() {
		return callback;
	}

	public void setCallback(TouchCallback callback) {
		this.callback = callback;
	}

	public boolean onTouch(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		if (isIgnoreTouch()) {
			return false;
		} else {
			if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN
					&& contains(pTouchAreaLocalX, pTouchAreaLocalY)) {
				callback.callback(type, indexX, indexY, isExploading);
				return true;
			}
		}
		return false;
	}

	public interface TouchCallback {
		void callback(int type, int x, int y, boolean isExploading);
	}

}
