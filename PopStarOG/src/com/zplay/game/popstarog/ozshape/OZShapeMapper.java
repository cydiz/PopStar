package com.zplay.game.popstarog.ozshape;

import android.annotation.SuppressLint;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("UseSparseArrays")
public class OZShapeMapper {

	public final static int ONE_DOT_SHAPE_KEY = 0;

	private final static Map<Integer, Class<? extends OZShape>> shapeMap = new HashMap<Integer, Class<? extends OZShape>>();
	static {
		shapeMap.put(ONE_DOT_SHAPE_KEY, OZShape1.class);
		shapeMap.put(1, OZShape2h.class);
		shapeMap.put(2, OZShape2v.class);
		shapeMap.put(3, OZShape3h.class);
		shapeMap.put(4, OZShape3l1.class);
		shapeMap.put(5, OZShape3l2.class);
		shapeMap.put(6, OZShape3v.class);
		shapeMap.put(7, OZShape4h.class);
		shapeMap.put(8, OZShape4v.class);
		shapeMap.put(9, OZShape4Square.class);
		shapeMap.put(10, OZShape5h.class);
		shapeMap.put(11, OZShape5l1.class);
		shapeMap.put(12, OZShape5l2.class);
		shapeMap.put(13, OZShape5v.class);
		shapeMap.put(14, OZShape9Square.class);
	}

	public static Map<Integer, Class<? extends OZShape>> getShapeMap() {
		return shapeMap;
	}

}
