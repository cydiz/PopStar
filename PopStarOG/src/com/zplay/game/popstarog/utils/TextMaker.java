package com.zplay.game.popstarog.utils;

import com.orange.entity.text.Text;
import com.orange.entity.text.TextOptions;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.res.FontRes;
import com.orange.util.HorizontalAlign;

public class TextMaker {

	public static Text make(String text, String fontID, float centreX,
			float centreY, HorizontalAlign textAlignment,
			VertexBufferObjectManager vertextBufferObjectManager) {
		Text result = new Text(0, 0, FontRes.getFont(fontID), text,
				vertextBufferObjectManager);
		result.setTextOptions(new TextOptions(HorizontalAlign.CENTER));
		result.setCentrePosition(centreX, centreY);
		return result;
	}
}
