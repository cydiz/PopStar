package com.zplay.game.popstarog.utils;

import com.orange.entity.sprite.ButtonSprite;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.res.RegionRes;

public class ButtonMaker {

	public static ButtonSprite makeFromSingleImgFile(float centreX,
			float centreY, String regionName,
			VertexBufferObjectManager vertextBufferObjectManager) {
		ButtonSprite buttonSprite = new ButtonSprite(centreX, centreY,
				RegionRes.getTextureRegion(regionName),
				vertextBufferObjectManager);
		buttonSprite.setCentrePosition(centreX, centreY);
		buttonSprite.setIgnoreTouch(false);
		return buttonSprite;
	}

	public static ButtonSprite makeFromTPFile(float centreX, float centreY,
			String regionName,
			VertexBufferObjectManager vertextBufferObjectManager) {
		ButtonSprite buttonSprite = new ButtonSprite(centreX, centreY,
				RegionRes.getRegion(regionName), vertextBufferObjectManager);
		buttonSprite.setCentrePosition(centreX, centreY);
		buttonSprite.setIgnoreTouch(false);
		return buttonSprite;
	}

}
