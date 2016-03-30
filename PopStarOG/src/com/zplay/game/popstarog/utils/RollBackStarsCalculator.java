package com.zplay.game.popstarog.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;

import com.e7studio.android.e7appsdk.utils.LogUtils;
import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.sprite.StarSprite;

@SuppressLint("UseSparseArrays")
public class RollBackStarsCalculator {

	private final static String TAG = "RollBackStarsCalculator";

	public final static int HORIZONTAL = 0;
	public final static int VERTICAL = 1;

	private static Comparator<ExploadeTrackInform> comparator = new Comparator<ExploadeTrackInform>() {
		public int compare(ExploadeTrackInform arg0, ExploadeTrackInform arg1) {
			return arg0.getY() - arg1.getY();
		}
	};

	/**
	 * 计算得出需要向上移动的星星以及需要向右移动的星星以及相应的步数,同时竖向列表为从上至下，横向列表为从右至左
	 */
	public static Map<Integer, List<RollbackInfo>> calculateRollBackStars(
			int[][] starSigns, StarSprite[][] starSprites, TrackMove trackMove) {
		List<ExploadeTrackInform> exploadeList = trackMove
				.getExploadeSpriteList();
		List<Integer> emptyColumnList = trackMove.getEmptyColumnList();
		if (emptyColumnList == null) {
			emptyColumnList = new ArrayList<Integer>();
		}
		LogUtils.v(TAG, "上次消除的星星：" + exploadeList);
		LogUtils.v(TAG, "其中空列是：" + emptyColumnList);

		List<RollbackInfo> verticalList = new ArrayList<RollbackInfo>();
		List<RollbackInfo> horizontalList = new ArrayList<RollbackInfo>();

		Map<Integer, List<RollbackInfo>> result = new HashMap<Integer, List<RollbackInfo>>();
		result.put(HORIZONTAL, horizontalList);
		result.put(VERTICAL, verticalList);

		// 返回用列(x)作为key的map
		Map<Integer, List<ExploadeTrackInform>> exploadeMap = simplifyExploadeList(exploadeList);

		// small->big
		Collections.sort(emptyColumnList);

		if (emptyColumnList.size() != 0) {
			int leftColumn = emptyColumnList.get(0);
			for (int i = leftColumn; i < 10; i++) {
				int step = horizontalMoveStepCalculate(emptyColumnList, i);
				for (int j = 0; j < 10; j++) {
					if (starSprites[i][j].getType() != GameConstants.STAR_NONE) {
						horizontalList.add(new RollbackInfo(starSprites[i][j],
								step));
						starSprites[i][j].setIndexX(i + step);
					}
				}
			}
		}

		// starSprites数组以及starSigns数组向右横移
		for (int i = 0; i < emptyColumnList.size(); i++) {
			int column = emptyColumnList.get(i);
			StarSprite[] tempSprites = new StarSprite[10];
			int[] tempSigns = new int[10];
			// 之后的每一列
			for (int j = column; j < 10; j++) {
				// 每一列的每一个
				for (int z = 0; z < 10; z++) {
					// 最后一列
					if (j == 9) {
						// 说明emptyColumnList中只有第9列,什么都不用做
						if (tempSprites[z] != null) {
							starSprites[column][z] = tempSprites[z];
							starSigns[column][z] = tempSigns[z];

							starSprites[column][z].setIndexX(column);
						}
					} else {
						// 第一列，以及column标示的列
						if (tempSprites[z] == null) {
							tempSprites[z] = starSprites[j + 1][z];
							tempSigns[z] = starSigns[j + 1][z];
							starSprites[j + 1][z] = starSprites[j][z];
							starSigns[j + 1][z] = starSigns[j][z];

							starSprites[j + 1][z].setIndexX(j + 1);
						} else {
							StarSprite tem = starSprites[j + 1][z];
							starSprites[j + 1][z] = tempSprites[z];
							tempSprites[z] = tem;

							int temSign = starSigns[j + 1][z];
							starSigns[j + 1][z] = tempSigns[z];
							tempSigns[z] = temSign;

							starSprites[j + 1][z].setIndexX(j + 1);
						}
					}
				}
			}
			tempSprites = null;
		}

		// 进行竖向移动
		Set<Integer> columnSet = exploadeMap.keySet();

		// 每一列
		for (int column : columnSet) {
			List<ExploadeTrackInform> exploadeTrackList = exploadeMap
					.get(column);
			// 从Yindex=0开始
			Collections.sort(exploadeTrackList, comparator);
			LogUtils.v(TAG, "排序之后的列表数据是：" + exploadeTrackList);
			// 每一列的每一个
			for (int j = 0; j < 10; j++) {
				if (starSprites[column][j].getType() != GameConstants.STAR_NONE) {
					int moveStep = verticalMoveStepCalculate(exploadeTrackList,
							j);
					verticalList.add(new RollbackInfo(starSprites[column][j],
							moveStep));
				}
			}
		}

		// 数组进行位移
		for (int column : columnSet) {
			List<ExploadeTrackInform> exploadeTrackList = exploadeMap
					.get(column);
			// 从Yindex=0开始
			Collections.sort(exploadeTrackList, comparator);
			LogUtils.v(TAG, "排序之后的列表数据是：" + exploadeTrackList);

			// 该列的每一个被消除的
			for (int i = 0; i < exploadeTrackList.size(); i++) {

				ExploadeTrackInform exploadeInform = exploadeTrackList.get(i);
				int exploadY = exploadeInform.getY();

				StarSprite tempSprite = null;
				int tempSign = 0;

				for (int j = exploadY; j < 10; j++) {
					// 最顶部的
					if (j == 9) {
						if (tempSprite != null) {
							starSprites[column][exploadY] = tempSprite;
							starSprites[column][exploadY].setIndexY(exploadY);
							starSigns[column][exploadY] = tempSign;
						}
					} else {
						if (tempSprite == null) {
							tempSprite = starSprites[column][j + 1];
							tempSign = starSigns[column][j + 1];

							starSprites[column][j + 1] = starSprites[column][j];
							starSigns[column][j + 1] = starSigns[column][j];

							starSprites[column][j + 1].setIndexY(j + 1);
						} else {

							StarSprite temSprite;
							int temSign = 0;

							temSign = starSigns[column][j + 1];
							temSprite = starSprites[column][j + 1];

							starSigns[column][j + 1] = tempSign;
							starSprites[column][j + 1] = tempSprite;

							starSprites[column][j + 1].setIndexY(j + 1);

							tempSign = temSign;
							tempSprite = temSprite;
						}

					}

				}

			}
		}
		// starSigns变换
		for (int i = 0; i < exploadeList.size(); i++) {
			ExploadeTrackInform info = exploadeList.get(i);
			starSigns[info.getX()][info.getY()] = info.getType();
		}
		return result;
	}

	private static int verticalMoveStepCalculate(
			List<ExploadeTrackInform> list, int j) {
		int step = 0;
		for (int i = 0; i < list.size(); i++) {
			ExploadeTrackInform inform = list.get(i);
			if (inform.getY() > (j + step)) {
				break;
			}
			if (inform.getY() < (j + step)) {
				step++;
			}
			if (inform.getY() == (j + step)) {
				step++;
			}
		}
		return step;
	}

	private static int horizontalMoveStepCalculate(List<Integer> emptyList,
			int i) {
		int step = 0;
		for (int j = 0; j < emptyList.size(); j++) {
			int column = emptyList.get(j);
			if (i + step < column) {
				break;
			}
			if (i + step > column) {
				step++;
			}
			if (i + step == column) {
				step++;
			}
		}
		return step;
	}

	// 按列进行排列
	private static Map<Integer, List<ExploadeTrackInform>> simplifyExploadeList(
			List<ExploadeTrackInform> exploadeList) {
		Map<Integer, List<ExploadeTrackInform>> result = new HashMap<Integer, List<ExploadeTrackInform>>();
		for (int i = 0; i < exploadeList.size(); i++) {
			ExploadeTrackInform exploadeInfo = exploadeList.get(i);
			List<ExploadeTrackInform> list = result.get(exploadeInfo.getX());
			if (list == null) {
				list = new ArrayList<ExploadeTrackInform>();
				result.put(exploadeInfo.getX(), list);
			}
			list.add(new ExploadeTrackInform(exploadeInfo.getX(), exploadeInfo
					.getY(), exploadeInfo.getType()));
		}
		return result;
	}
}
