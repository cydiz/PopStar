package com.zplay.game.popstarog.others;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.e7studio.android.e7appsdk.utils.MapBuilder;
import com.orange.util.size.Size;

public class GameConstants {

	// mm的资质是宏润还是掌游
	public final static boolean IS_MM_ZIZHI_ZPLAY = true;

	public final static int BASE_WIDTH = 640;
	public final static int BASE_HEIGHT = 960;

	// 1010模式下组合方块其中每一块的尺寸
	public static Size OZ_GROUP_SIZE;
	// 1010模式下container方块相对于屏幕的距离
	public static float OZ_PADDING_X;

	// 1010模式下组合方块其实的y坐标
	public final static float OZ_GROUP_START_Y = 753;
	// 1010模式下方块的起始y坐标
	public final static float OZ_CONTAINER_START_Y = 120;

	// 1010模式，小点道具的起始x坐标
	public final static float ONE_BLOCK_START_X = 413;
	public final static float ONE_BLOCK_START_Y = 680;

	public static Size ONE_BLOCK_SIZE;

	// 广告的高度
	public static float AD_BANNER_HEIGHT = 0;

	// 1010模式下每一个组合方块的容器的尺寸
	public final static float OZ_GROUP_CONTAINER_WIDTH = (BASE_WIDTH - OZ_PADDING_X * 2 * 1.0f) / 3;
	public final static float OZ_GROUP_CONTAINER_HEIGHT = BASE_HEIGHT
			- OZ_GROUP_START_Y;

	public static Size OZ_CONTAINER_SIZE = null;
	public static Size OZ_STAR_SIZE = null;

	public final static String DES_KEY = "5082aec7";
	public final static List<Map<String, String>> productList = new ArrayList<Map<String, String>>();

	// 标示不同类型的星星
	public final static int STAR_NONE = -1;
	public final static int STAR_RED = 3;
	public final static int STAR_BLUE = 0;
	public final static int STAR_YELLOW = 4;
	public final static int STAR_GREEEN = 1;
	public final static int STAR_PURPLE = 2;

	// 1010模式下表示不同的星星
	public final static int BLOCK_NONE = -1;
	public final static int BLOCK_0 = 0;
	public final static int BLOCK_1 = 1;
	public final static int BLOCK_2 = 2;
	public final static int BLOCK_3 = 3;
	public final static int BLOCK_4 = 4;
	public final static int BLOCK_5 = 5;
	public final static int BLOCK_6 = 6;
	public final static int BLOCK_7 = 7;
	public final static int BLOCK_8 = 8;
	public final static int BLOCK_9 = 9;
	// 表示正在处于消除中的星星
	public final static int BLOCK_EXPLOADING = -2;

	public final static String ANNOUNCEMENT_ADDR = "http://popstar.zplay.cn/activity/index.html";

	public final static String GET_ANNOUNCEMENT_ADDR = "http://gg.zplay.cn/in/noticein.php";

	// 最高奖励
	public final static int MAX_BONUS = 2000;

	public final static String[] COLOR_NAMES = new String[] { "transparent",
			"red", "blue", "yellow", "green", "purple" };

	public final static int[] CLEAR_SCORE = new int[] { 1000, 2500, 4500, 7000,
		9000, 12000, 14000, 17000, 19000,22000 ,24000 ,26500, 29000,31500,34500,36500,39000,42000,45000,48000 };

	public final static String FONT_PATH = "font.ttf";

	public final static int[] hammerCost = new int[] { 5, 6, 8, 10 };
	public final static int[] switchCost = new int[] { 6, 7, 10, 15 };

	public static int screen_width = 0;
	public static int screen_height = 0;

	public final static String CENT_POINT = "zplay02900300703";
	public final static String QUICK_BUY_CHARGEPOINT_ID = "zplay02900300303";

	static {
		productList.add(MapBuilder.buildMap(new String[] { "id", "num",
				"money", "extra", "display", "type", "discount" },
				new String[] { "zplay02900300703", "10", "0.01", "0", "10", "",
						"" }));
		
		productList.add(MapBuilder.buildMap(new String[] { "id", "num",
				"money", "extra", "display", "type", "discount" },
				new String[] { "zplay02900300403", "228", "10", "68", "228",
						"feel", "" }));
						
		productList.add(MapBuilder.buildMap(new String[] { "id", "num",
				"money", "extra", "display", "type", "discount" },
				new String[] { "zplay02900300103", "30", "2", "10", "30",
						"", "" }));
		
		productList.add(MapBuilder.buildMap(new String[] { "id", "num",
				"money", "extra", "display", "type", "discount" },
				new String[] { "zplay02900300203", "68", "4", "28", "68", "",
						"" }));

		productList.add(MapBuilder.buildMap(new String[] { "id", "num",
				"money", "extra", "display", "type", "discount" },
				new String[] { "zplay02900300303", "118", "6", "38", "118", "hot",
						"" }));


		productList.add(MapBuilder.buildMap(new String[] { "id", "num",
				"money", "extra", "display", "type", "discount" },
				new String[] { "zplay02900300503", "500", "20", "180", "500",
						"", "" }));
		
		productList.add(MapBuilder.buildMap(new String[] { "id", "num",
				"money", "extra", "display", "type", "discount" },
				new String[] { "zplay02900300603", "888", "30", "248", "888",
				"ob", "" }));
	}

	/**
	 * 获得目标分数
	 * 
	 * @param stage
	 * @return
	 */
	public static long getClearScore(int stage) {
		if (stage <= CLEAR_SCORE.length) {
			return CLEAR_SCORE[stage - 1];
		} else {
			int gap = 3000;
			if (stage < 100) {
				gap = 3000;
				return CLEAR_SCORE[CLEAR_SCORE.length - 1] + gap
						* (stage - CLEAR_SCORE.length);
			}
			if (stage >= 100 && stage < 300) {
				gap = 4000;
				return CLEAR_SCORE[CLEAR_SCORE.length - 1] + 3000
						* (99 - CLEAR_SCORE.length) + gap * (stage - 99);
			}
		}
		return 0;
		// return 100;
	}

	/**
	 * 根据类型获取颜色
	 * 
	 * @param type
	 * @return
	 */
	public static String getStarColor(int type) {
		return COLOR_NAMES[type + 1];
	}

	/**
	 * 获取冲完本关卡需要的幸运星数量
	 * 
	 * @param stage
	 * @param rebornNum
	 * @return
	 */
	public static int getRebornCost(int stage, int rebornNum) {
		int starCost = 0;
		if (stage >= 100) {
			starCost = 46;
		} else {
			starCost = (stage / 5) * 2 + 6;
		}
		if (rebornNum == 2) {
			starCost = (int) (starCost * 1.3f);
		}
		if (rebornNum == 3) {
			starCost = (int) (starCost * 1.6f);
		}
		if (rebornNum >= 4) {
			starCost = (int) (starCost * 2.0f);
		}
		return starCost;
	}

	/**
	 * 消耗锤子数量
	 * 
	 * @param stage
	 * @param rebornNum
	 * @return
	 */
	public static int getHammerCost(int usedNum) {
		return hammerCost[Math.max(Math.min(hammerCost.length - 1, usedNum), 0)];
	}

	/**
	 * 小点道具使用消耗
	 * 
	 * @param usedNum
	 * @return
	 */
	public static int getOneBlockCost(int usedNum) {
		return hammerCost[Math.max(Math.min(hammerCost.length - 1, usedNum), 0)];
	}

	/**
	 * 洗牌道具消耗数量
	 * 
	 * @param usedNum
	 * @return
	 */
	public static int getSwitchCost(int usedNum) {
		return switchCost[Math.max(0, Math.min(switchCost.length - 1, usedNum))];
	}
	
	/**
	 * 计算不同手机屏幕Y轴缩放比例
	 */
	public static float screenScale() {
		float phoneScale = (float)screen_width / (float)screen_height;
		float gameScale = (float)BASE_WIDTH / (float)BASE_HEIGHT;
		return phoneScale / gameScale;
	}
}
