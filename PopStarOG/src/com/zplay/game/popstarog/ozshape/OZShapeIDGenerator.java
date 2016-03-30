package com.zplay.game.popstarog.ozshape;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.e7studio.android.e7appsdk.utils.LogUtils;
import com.zplay.game.popstarog.utils.MathUtils;

/**
 * 生成三个组合形状{@linkplain OZShape}，当然，必须要保证其中至少有一个是可以放的，亦即，不能生成出来就导致游戏失败了
 * 
 * @author Administrator
 * 
 */
public class OZShapeIDGenerator {

	// 在第一次产生只有一个“点”的形状之后，要进行一个概率计算，有70%的概率直接使用，否则，从池中重新进行选取
	private final static int SHAPE_ONE_PROB = 3;

	private final static String TAG = "OZShapeGenerator";

	public static int[] generateOZShapeID(int[][] starSigns) {

		int[] shapes = new int[3];
		Set<Entry<Integer, Class<? extends OZShape>>> entrySet = OZShapeMapper
				.getShapeMap().entrySet();
		List<Integer> usableShapeList = new ArrayList<Integer>();
		// 生成第一个
		for (Entry<Integer, Class<? extends OZShape>> entry : entrySet) {
			try {
				OZShape shape = entry.getValue().newInstance();
				if (shape.isMatch(starSigns)) {
					usableShapeList.add(entry.getKey());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		LogUtils.v(TAG, "可以放置的组合形状有：" + usableShapeList);

		int usableShapeKey = usableShapeList.get(MathUtils.getRandom(0,
				usableShapeList.size() - 1));

		// 是一个点的形状&可以放置的形状不止一个
		if (usableShapeKey == OZShapeMapper.ONE_DOT_SHAPE_KEY
				&& usableShapeList.size() != 1) {
			usableShapeKey = (isProbOK() ? usableShapeKey : usableShapeList
					.get(MathUtils.getRandom(0, usableShapeList.size() - 1)));
		}
		shapes[0] = usableShapeKey;
		shapes[1] = MathUtils.getRandom(0, entrySet.size() - 1);
		if (shapes[1] == OZShapeMapper.ONE_DOT_SHAPE_KEY) {
			shapes[1] = (isProbOK() ? shapes[1] : MathUtils.getRandom(0,
					entrySet.size() - 1));
		}
		shapes[2] = MathUtils.getRandom(0, entrySet.size() - 1);
		
		if (shapes[2] == OZShapeMapper.ONE_DOT_SHAPE_KEY) {
			shapes[2] = (isProbOK() ? shapes[2] : MathUtils.getRandom(0,
					entrySet.size() - 1));
		}
		return shapes;
	}

	// 该小点形状此次是否可以进行使用
	private static boolean isProbOK() {
		return MathUtils.getRandom(1, 10) > (10 - SHAPE_ONE_PROB);
	}

	/**
	 * 生成三个都能放置的形状
	 * 
	 * @param starSigns
	 * @return
	 */
	public static int[] generateAllMatchedShapeID(int[][] starSigns) {

		int[] shapes = new int[3];
		Set<Entry<Integer, Class<? extends OZShape>>> entrySet = OZShapeMapper
				.getShapeMap().entrySet();
		List<Integer> firstShapeList = new ArrayList<Integer>();
		// 生成第一个
		for (Entry<Integer, Class<? extends OZShape>> entry : entrySet) {
			try {
				OZShape shape = entry.getValue().newInstance();
				if (shape.isMatch(starSigns)) {
					firstShapeList.add(entry.getKey());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		LogUtils.v(TAG, "可以放置的组合形状有：" + firstShapeList);
		shapes[0] = firstShapeList.get(MathUtils.getRandom(0,
				firstShapeList.size() - 1));
		shapes[1] = firstShapeList.get(MathUtils.getRandom(0,
				firstShapeList.size() - 1));
		shapes[2] = firstShapeList.get(MathUtils.getRandom(0,
				firstShapeList.size() - 1));
		return shapes;
	}
}
