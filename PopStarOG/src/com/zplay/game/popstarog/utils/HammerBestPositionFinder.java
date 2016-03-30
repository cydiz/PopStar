package com.zplay.game.popstarog.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.annotation.SuppressLint;

import com.e7studio.android.e7appsdk.utils.LogUtils;
import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.sprite.Position;

@SuppressLint("UseSparseArrays")
public class HammerBestPositionFinder {

	private final static String TAG = "HammerBestPositionFinder";

	/**
	 * 获取最多的连接数
	 * 
	 * @param starSigns
	 * @return
	 */
	public static Position findBestPosition(int[][] starSigns) {
		int maxNums = 0;
		Position position = null;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (starSigns[i][j] != GameConstants.STAR_NONE) {
					int[][] temp = copyStarSign(starSigns);
					temp[i][j] = GameConstants.STAR_NONE;
					gatherStarSigns(temp);
					int nums = getMaxLinkedNumIfHammerUsed(temp);
					if (nums >= maxNums) {
						maxNums = nums;
						position = new Position(i, j);
					}
				}
			}
		}
		LogUtils.v(TAG, "最多连接数：" + maxNums);
		return position;
	}

	private static void initStarMap(Map<Integer, Integer> map) {
		map.clear();
		map.put(GameConstants.STAR_BLUE, 0);
		map.put(GameConstants.STAR_GREEEN, 0);
		map.put(GameConstants.STAR_PURPLE, 0);
		map.put(GameConstants.STAR_RED, 0);
		map.put(GameConstants.STAR_YELLOW, 0);
	}

	private static void initStarOKMap(Map<Integer, Boolean> map) {
		map.clear();
		map.put(GameConstants.STAR_BLUE, false);
		map.put(GameConstants.STAR_GREEEN, false);
		map.put(GameConstants.STAR_PURPLE, false);
		map.put(GameConstants.STAR_RED, false);
		map.put(GameConstants.STAR_YELLOW, false);
	}

	public static boolean isOnlyOneInThisRow(int[][] starigns, int x, int y) {
		int num = 0;
		for (int j = 0; j <= 9; j++) {
			if (starigns[x][j] != GameConstants.STAR_NONE) {
				num++;
				if (num > 1) {
					break;
				}
			}
		}
		if (num > 1) {
			return false;
		} else {
			return true;
		}
	}

	public static Position findBestPosition2(int[][] starSigns) {
		// 周围最多同色数量
		int maxNums = 0;
		Position position = null;
		Map<Integer, Integer> starTypeNumMap = new HashMap<Integer, Integer>();

		// 该map是为了保证消除之后上面有相同的能落下来或者右边有相同的，能移动过去，亦即消除之后同色的能聚集
		Map<Integer, Boolean> starOKMap = new HashMap<Integer, Boolean>();
		int[] starTypes = new int[] { GameConstants.STAR_BLUE,
				GameConstants.STAR_GREEEN, GameConstants.STAR_PURPLE,
				GameConstants.STAR_RED, GameConstants.STAR_YELLOW };

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				initStarMap(starTypeNumMap);
				initStarOKMap(starOKMap);
				if (starSigns[i][j] != GameConstants.STAR_NONE) {
					Position tempPosition = new Position(i, j);
					// 上
					if (j + 1 <= 9
							&& starSigns[i][j + 1] != GameConstants.STAR_NONE
							&& starSigns[i][j + 1] != starSigns[i][j]) {
						starTypeNumMap.put(starSigns[i][j + 1],
								starTypeNumMap.get(starSigns[i][j + 1]) + 1);
						starOKMap.put(starSigns[i][j + 1], true);
					}
					// 下
					if (j - 1 >= 0
							&& starSigns[i][j - 1] != GameConstants.STAR_NONE
							&& starSigns[i][j - 1] != starSigns[i][j]) {

						starTypeNumMap.put(starSigns[i][j - 1],
								starTypeNumMap.get(starSigns[i][j - 1]) + 1);
					}
					// 左
					if (i - 1 >= 0
							&& starSigns[i - 1][j] != GameConstants.STAR_NONE
							&& starSigns[i - 1][j] != starSigns[i][j]) {
						starTypeNumMap.put(starSigns[i - 1][j],
								starTypeNumMap.get(starSigns[i - 1][j]) + 1);
					}
					// 右
					if (i + 1 <= 9
							&& starSigns[i + 1][j] != GameConstants.STAR_NONE
							&& starSigns[i + 1][j] != starSigns[i][j]) {
						// 该列只有一个或者上面有一个跟右边的同色的
						if (isOnlyOneInThisRow(starSigns, i, j)
								|| starOKMap.get(starSigns[i + 1][j])) {
							starOKMap.put(starSigns[i + 1][j], true);
						}
						starTypeNumMap.put(starSigns[i + 1][j],
								starTypeNumMap.get(starSigns[i + 1][j]) + 1);
					}
					for (int z = 0; z < starTypes.length; z++) {
						int temp = starTypeNumMap.get(z);
						if (temp >= maxNums && starOKMap.get(z)) {
							maxNums = temp;
							position = tempPosition;
						}
					}
				}
			}
		}
		LogUtils.v(TAG, "最多连接数：" + maxNums);
		return position;
	}

	private static int[][] copyStarSign(int[][] starSigns) {
		int[][] signs = new int[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				signs[i][j] = starSigns[i][j];
			}
		}
		return signs;
	}

	private static void gatherStarSigns(int[][] starSigns) {
		Map<Integer, List<Map<Integer, Position>>> moveDownPositionList = new HashMap<Integer, List<Map<Integer, Position>>>();
		for (int i = 0; i < 10; i++) {
			int moveStep = 0;
			List<Map<Integer, Position>> positionList = new ArrayList<Map<Integer, Position>>();
			for (int j = 0; j < 10; j++) {
				if (starSigns[i][j] == GameConstants.STAR_NONE) {
					moveStep++;
				} else {
					Map<Integer, Position> map = new HashMap<Integer, Position>();
					map.put(moveStep, new Position(i, j));
					positionList.add(map);
				}
			}
			if (positionList.size() != 0) {
				moveDownPositionList.put(i, positionList);
			}
		}
		final Set<Entry<Integer, List<Map<Integer, Position>>>> moveDownPositionEntrySet = moveDownPositionList
				.entrySet();
		if (moveDownPositionEntrySet.size() == 0) {
			gatherHorizontalStarSigns(starSigns);
		} else {
			for (Entry<Integer, List<Map<Integer, Position>>> moveDownPositionEntry : moveDownPositionEntrySet) {
				final int x = moveDownPositionEntry.getKey();
				final List<Map<Integer, Position>> positionList = moveDownPositionEntry
						.getValue();
				for (int i = 0; i < positionList.size(); i++) {
					final Map<Integer, Position> positionMap = positionList
							.get(i);
					final int moveStep = (Integer) positionMap.keySet()
							.toArray()[0];
					final Position starSprite = positionMap.get(moveStep);
					final int y = starSprite.getY();
					final int downY = y - moveStep;
					int temp = starSigns[x][downY];
					starSigns[x][downY] = starSigns[x][y];
					starSigns[x][y] = temp;
				}
			}
			gatherHorizontalStarSigns(starSigns);
		}
	}

	private static int getMaxLinkedNumIfHammerUsed(int[][] signs) {
		int linkedNum = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (signs[i][j] != GameConstants.STAR_NONE) {
					List<Position> tempList = new ArrayList<Position>();
					trackSprite(new Position(i, j), tempList, signs);
					if (tempList.size() > linkedNum) {
						linkedNum = tempList.size();
					}
				}
			}
		}
		return linkedNum;
	}

	// 检查每一个的上下左右
	private static void trackSprite(Position position,
			List<Position> trackSpriteList, int[][] signs) {
		int compareX = position.getX();
		int compareY = position.getY();
		int compareType = signs[compareX][compareY];

		// 右边
		for (int i = compareX + 1; i < 10; i++) {
			if (signs[i][compareY] == compareType) {
				if (!trackSpriteList.contains(new Position(i, compareY))) {
					trackSpriteList.add(new Position(i, compareY));
					trackSprite(new Position(i, compareY), trackSpriteList,
							signs);
				}
			} else {
				break;
			}
		}
		// 左边
		for (int i = compareX - 1; i >= 0; i--) {
			if (signs[i][compareY] == compareType) {
				if (!trackSpriteList.contains(new Position(i, compareY))) {
					trackSpriteList.add(new Position(i, compareY));
					trackSprite(new Position(i, compareY), trackSpriteList,
							signs);
				}
			} else {
				break;
			}

		}

		// 上边
		for (int i = compareY + 1; i < 10; i++) {
			if (signs[compareX][i] == compareType) {
				if (!trackSpriteList.contains(new Position(compareX, i))) {
					trackSpriteList.add(new Position(compareX, i));
					trackSprite(new Position(compareX, i), trackSpriteList,
							signs);
				}
			} else {
				break;
			}

		}
		// 下边
		for (int i = compareY - 1; i >= 0; i--) {
			if (signs[compareX][i] == compareType) {
				if (!trackSpriteList.contains(new Position(compareX, i))) {
					trackSpriteList.add(new Position(compareX, i));
					trackSprite(new Position(compareX, i), trackSpriteList,
							signs);
				}
			} else {
				break;
			}
		}
	}

	private static void gatherHorizontalStarSigns(int[][] starSigns) {
		int[] xs = new int[10];
		for (int i = 0; i < xs.length; i++) {
			xs[i] = i;
		}
		List<Integer> nullList = new ArrayList<Integer>();
		for (int i = 0; i < xs.length; i++) {
			boolean isNullColumn = true;
			for (int j = 0; j < 10; j++) {
				if (starSigns[xs[i]][j] != GameConstants.STAR_NONE) {
					isNullColumn = false;
					break;
				}
			}
			if (isNullColumn) {
				nullList.add((Integer) xs[i]);
			}
		}
		if (nullList.size() != 0) {
			List<Map<Integer, Position>> moveLeftPositionList = new ArrayList<Map<Integer, Position>>();
			Collections.sort(nullList);
			int nullX = nullList.get(0);
			int tempStep = 1;
			for (int i = nullX + 1; i < 10; i++) {
				if (nullList.contains(i)) {
					tempStep++;
				} else {
					for (int j = 0; j < 10; j++) {
						if (starSigns[i][j] != GameConstants.STAR_NONE) {
							Map<Integer, Position> map = new HashMap<Integer, Position>();
							map.put(tempStep, new Position(i, j));
							moveLeftPositionList.add(map);
						}
					}
				}
			}
			if (moveLeftPositionList.size() != 0) {
				for (Map<Integer, Position> item : moveLeftPositionList) {
					final int moveStep = (Integer) item.keySet().toArray()[0];
					Position position = item.get(moveStep);
					int x = position.getX();
					int y = position.getY();
					int moveX = x - moveStep;
					// 转换
					int temp = starSigns[x][y];
					starSigns[x][y] = starSigns[moveX][y];
					starSigns[moveX][y] = temp;
				}
			}
		}
	}

}
