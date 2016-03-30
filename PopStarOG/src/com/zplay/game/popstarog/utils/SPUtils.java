package com.zplay.game.popstarog.utils;

import android.app.Activity;
import android.content.Context;

import com.e7studio.android.e7appsdk.utils.LogUtils;
import com.e7studio.android.e7appsdk.utils.SPValueHandler;
import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.utils.sp.ConstantsHolder;
import com.zplay.game.popstarog.utils.sp.PhoneInfoGetter;

public class SPUtils {
	private final static String TAG = "SPUtils";

	private final static String KEY_HIGH_SCORE = "high_score";
	private final static String KEY_STAR_NUM = "lucky_star_num";
	private final static String KEY_AUDIO_LEVEL = "audio_level";
	private final static String KEY_STAGE = "stage";
	private final static String KEY_SCORE = "score";
	private final static String KEY_USED_HAMMER = "used_hammer_num";
	private final static String KEY_USED_SWITCH = "used_switch_num";
	private final static String KEY_STARS = "stars";

	// 复活次数
	private final static String KEY_REBORN_NUM = "reborn_used_num";

	// 关卡开始的分数
	private final static String KEY_STAGE_START_SCORE = "key_stage_start_score";
	// 是否已经购买了一分钱
	private final static String KEY_IS_CENT_BUY = "is_cent_buy";

	// 是否已经弹出过帮助引导界面
	private final static String KEY_IS_GUIDE_SHOWED = "is_guide_showed";

	// 是否已经弹出过一个小点的帮助引导界面
	private final static String KEY_IS_ONE_BLOCK_GUIDE_SHOWED = "is_one_block_guide_showed";

	// 公告接口地址
	private final static String KEY_ANNOUNCEMENT_ADDR = "announcement_addr";
	private final static String KEY_IS_ANNOUNCEMENT_SHOW = "is_announcemet_show";
	// 公告在什么时候展示：0为关闭|1为游戏启动就打开|2为进入游戏展示
	private final static String KEY_ANNOUNCEMENT_SHOW_NODE = "announcent_show_place";
	// 每天公告自动展示的次数
	private final static String KEY_ANNOUNCEMENT_AUTO_SHOW_NUM = "announcement_auto_show_num";

	// 上次公告展示的时间
	private final static String KEY_LAST_ANNOUNCEMENT_TIME = "last_announcement_show_time";
	// 今天公告累计展示的次数
	private final static String KEY_TODAY_ANNOUNCEMENT_SHOW_NUM = "today_announcement_show_num";

	private final static String KEY_1010_HIGH_SCORE = "1010_high_score";
	private final static String KEY_1010_SCORE = "1010_score";
	private final static String KEY_1010_STARS = "1010_stars";
	private final static String KEY_1010_GROUPS = "1010_groups";
	private final static String KEY_1010_USED_REFRESH = "1010_used_refresh_num";
	private final static String KEY_1010_USED_GOON = "1010_used_goon_num";

	private final static String KEY_1010_ONE_BLOCK_USED_NUM = "1010_one_block_used_num";

	private final static String KEY_IS_SPRING_FESTIVAL_ACT_ON = "is_spring_festival_act_on";
	private final static String KEY_IS_SPRING_FESTIVAL_SHOWED = "is_spring_festival_showed";

	private final static String KEY_IS_STAGE_ONE_AEARD = "is_stage_one_award";
	private final static String KEY_IS_STAGE_TWO_AEARD = "is_stage_two_award";

	private final static String KEY_IS_STAGE_THREE_SHOWED = "is_stage_three_showed";

	// 星星联盟模式下，没到400可以整除的分数，就弹出快速购买的对话框，如果不购买会一直弹
	private final static String KEY_IS_QUICK_BUY_DIALOG_SHOWED = "is_quick_buy_dialog_showed";

	private final static String KEY_IS_STAGE_THREE_PASS = "is_stage_three_passed";
	//今日奖励幸运星数量
	private final static String TODAY_REWARDS_SHOU = "today_reward_show";
	//是否显示商城按钮
	private final static String IS_SHOW_BT_SHOP = "is_show_bt_shop";
	//是否是第一次进入游戏
	private final static String IS_FIRST_GAME = "is_first_game";
	//是否是第三关结束
	private final static String IS_THIRD_STAGE = "is_third_stage";

	//云雨道具
	private static final String POINT_PROGRESS = "point_progress";
	private final static String IS_NEW_VERSION = "is_new_version";
	
	//用户手机号码
	private static final String USER_PHONE_NUMBER = "user_phone_number";
	
	//服务器开关
	private static final String IS_SERVER_ON = "is_server_on";
	
	//是否初始化基地
	private static final String IS_INIT_JD = "is_init_jd";
	
	//是否初始化基地
	private static final String INIT_CONTROLLER_DATE = "init_controller_date";
	
	/***
	 * 保存用户手机号码
	 */
	public static void savePhoneNumber(Context context, String phone) {
		SPValueHandler.putStringParam(context, USER_PHONE_NUMBER, phone);
	}

	/***
	 * 获取用户手机号码
	 */
	public static String getPhoneNumber(Context context) {
		return SPValueHandler.getStringParam(context, USER_PHONE_NUMBER);
	}

	
	/**
	 * 获取关卡刚开始的分数
	 * 
	 * @param context
	 * @return
	 */
	public static long getStageStartScore(Context context, int stage) {
		String savedValue = SPValueHandler.getStringParam(context,
				KEY_STAGE_START_SCORE + stage);
		long stageStartScore = 0l;
		try {
			String decryptedSavedValue = Encrypter.decryptDES(savedValue,
					GameConstants.DES_KEY);
			stageStartScore = Long.parseLong(decryptedSavedValue);
		} catch (Exception ex) {

			LogUtils.v(TAG, "保存的关卡开始时候的分数值异常，重置为0");
		}
		return stageStartScore;
	}

	/**
	 * 保存关卡刚开始时候的分数
	 * 
	 * @param context
	 * @param stageStartScore
	 */
	public static void saveStageStartScore(Context context,
			long stageStartScore, int stage) {
		SPValueHandler.putStringParam(context, KEY_STAGE_START_SCORE + stage,
				Encrypter.encryptDES(String.valueOf(stageStartScore),
						GameConstants.DES_KEY));
	}

	/**
	 * 获取最高分
	 * 
	 * @param context
	 * @return
	 */
	public static long getHighScore(Context context) {
		String savedValue = SPValueHandler.getStringParam(context,
				KEY_HIGH_SCORE);
		long highScore = 1000l;
		try {
			String decryptedSavedValue = Encrypter.decryptDES(savedValue,
					GameConstants.DES_KEY);
			highScore = Long.parseLong(decryptedSavedValue);
		} catch (Exception ex) {

			LogUtils.v(TAG, "保存的最高分数值异常，重置为1000");
		}
		return highScore;
	}

	/**
	 * 保存最高分数
	 * 
	 * @param context
	 * @param highScore
	 */
	public static void saveHighScore(Context context, long highScore) {
		SPValueHandler.putStringParam(context, KEY_HIGH_SCORE, Encrypter
				.encryptDES(String.valueOf(highScore), GameConstants.DES_KEY));
	}

	/**
	 * 获取音量设置
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAudioOpen(Context context) {
		boolean savedValue = SPValueHandler.getBooleanParam(context,
				KEY_AUDIO_LEVEL);
		return savedValue;
	}

	public static void toggleAudio(Context context) {
		SPValueHandler.putBooleanParam(context, KEY_AUDIO_LEVEL,
				!isAudioOpen(context));
	}

	/**
	 * 是否已经购买一分钱的计费点
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isCentAlreadyBuy(Context context) {
		boolean isBuy = SPValueHandler
				.getBooleanParam(context, KEY_IS_CENT_BUY);
		return !isBuy;
	}
	
	/**
	 * 是否显示幸运星不够时弹出的对话框
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isShowLuckyStarNotEnoughDialog(Context context) {
		if (PhoneInfoGetter.getMobileSP(context).equals(ConstantsHolder.CHINA_TELECOM)) {
			return true;
		}
		return false;
	}
	/**
	 * 是否直接显示红包，设定为联通和电信的都是直接显示红包，移动的先显示1分钱活动，购买之后再显示红包
	 * @param context
	 * @return
	 */
	public static boolean isShowRedBtnExpectOneCent(Context context) {
		if (!PhoneInfoGetter.isSimAvaliable(context)
				|| ((PhoneInfoGetter.getMobileSP(context).equals(
						ConstantsHolder.CHINA_UNICOME) || PhoneInfoGetter
						.getMobileSP(context).equals(
								ConstantsHolder.CHINA_TELECOM)))
				|| SPUtils.isCentAlreadyBuy(context)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 是否显示红包
	 * 如果是电信或者联通或者没有手机卡就直接显示，否则就代表是移动的，那么先判断有没有购买1cent礼包
	 * @param context
	 * @return
	 */
	public static boolean isShowRedPacket(Context context) {
		
		if(isShowRedBtnExpectOneCent(context))
		{
				return true;
		}
		return isCentAlreadyBuy(context);
	}
	

	/**
	 * 保存已经购买一分钱的计费点
	 * 
	 * @param context
	 */
	public static void setCentPointAlreadyBuy(Context context) {
		SPValueHandler.putBooleanParam(context, KEY_IS_CENT_BUY, false);
	}

	/**
	 * 已经弹出过引导界面
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isGuideShowed(Context context) {
		boolean isGuideShowed = SPValueHandler.getBooleanParam(context,
				KEY_IS_GUIDE_SHOWED);
		return !isGuideShowed;
	}

	/**
	 * 保存是否已经弹出过引导界面
	 * 
	 * @param context
	 */
	public static void setGuideShowed(Context context, boolean value) {
		SPValueHandler.putBooleanParam(context, KEY_IS_GUIDE_SHOWED, !value);
	}

	/**
	 * 已经弹出过小点引导界面
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isOneBlockGuideShowed(Context context) {
		boolean isGuideShowed = SPValueHandler.getBooleanParam(context,
				KEY_IS_ONE_BLOCK_GUIDE_SHOWED);
		return !isGuideShowed;
	}

	/**
	 * 保存是否已经弹出过小点引导界面
	 * 
	 * @param context
	 */
	public static void setOneBlockGuideShowed(Context context, boolean value) {
		SPValueHandler.putBooleanParam(context, KEY_IS_ONE_BLOCK_GUIDE_SHOWED,
				!value);
	}

	/**
	 * 获取幸运星数量
	 * 
	 * @param context
	 * @return
	 */
	public static long getLuckStarNum(Context context) {
		String starNum = SPValueHandler.getStringParam(context, KEY_STAR_NUM);
		long num = 0;
		try {
			if ((null != starNum) && (!starNum.equals("null"))) {
				String decryptedSavedValue = Encrypter.decryptDES(starNum,
						GameConstants.DES_KEY);
				num = Long.parseLong(decryptedSavedValue);
			} else {
				num = 40;
				saveLuckStarNum(context, num);
			}
			
		} catch (Exception ex) {

			LogUtils.v(TAG, "保存的幸运星数量异常，重置为20");
		}
		return num;
	}

	/**
	 * 保存幸运星数量
	 * 
	 * @param context
	 * @param starNum
	 */
	public static void saveLuckStarNum(Context context, long starNum) {
		SPValueHandler.putStringParam(context, KEY_STAR_NUM, Encrypter
				.encryptDES(String.valueOf(starNum), GameConstants.DES_KEY));
	}

	/**
	 * 获取当前所在关卡
	 * 
	 * @param context
	 * @return
	 */
	public static long getStage(Context context) {
		String savedValue = SPValueHandler.getStringParam(context, KEY_STAGE);
		long stage = 1l;
		try {
			String decryptedSavedValue = Encrypter.decryptDES(savedValue,
					GameConstants.DES_KEY);
			stage = Long.parseLong(decryptedSavedValue);
		} catch (Exception ex) {

			LogUtils.v(TAG, "保存的关卡数据异常，重置为1");
		}
		return stage;
	}

	/**
	 * 保存关卡
	 * 
	 * @param context
	 * @param stage
	 */
	public static void saveStage(Context context, long stage) {
		SPValueHandler.putStringParam(context, KEY_STAGE, Encrypter.encryptDES(
				String.valueOf(stage), GameConstants.DES_KEY));
	}

	/**
	 * 获取使用的锤子的数量
	 * 
	 * @param context
	 * @return
	 */
	public static int getUsedHammerNum(Context context) {
		String savedValue = SPValueHandler.getStringParam(context,
				KEY_USED_HAMMER);
		int usedHammer = 100;
		try {
			String decryptedSavedValue = Encrypter.decryptDES(savedValue,
					GameConstants.DES_KEY);
			usedHammer = Integer.parseInt(decryptedSavedValue);
		} catch (Exception ex) {

			LogUtils.v(TAG, "保存的锤子数据异常，重置为最大值");
		}
		return usedHammer;
	}

	/**
	 * 保存使用的锤子数量
	 * 
	 * @param context
	 * @param stage
	 */
	public static void saveHammerUsedNum(Context context, int hammerUsedNum) {
		SPValueHandler.putStringParam(context, KEY_USED_HAMMER, Encrypter
				.encryptDES(String.valueOf(hammerUsedNum),
						GameConstants.DES_KEY));
	}

	/**
	 * 获取复活道具使用次数
	 * 
	 * @param context
	 * @return
	 */
	public static int getRebornNum(Context context) {
		String savedValue = SPValueHandler.getStringParam(context,
				KEY_REBORN_NUM);
		int rebornNum = 1;
		try {
			String decryptedSavedValue = Encrypter.decryptDES(savedValue,
					GameConstants.DES_KEY);
			rebornNum = Integer.parseInt(decryptedSavedValue);
		} catch (Exception ex) {
		}
		return rebornNum;
	}

	/**
	 * 保存复活道具使用次数
	 * 
	 * @param context
	 * @param stage
	 */
	public static void saveRebornNum(Context context, int rebornNUm) {
		SPValueHandler.putStringParam(context, KEY_REBORN_NUM, Encrypter
				.encryptDES(String.valueOf(rebornNUm), GameConstants.DES_KEY));
	}

	/**
	 * 获取使用洗牌道具的次数
	 * 
	 * @param context
	 * @return
	 */
	public static int getSwitchUsedNum(Context context) {
		String savedValue = SPValueHandler.getStringParam(context,
				KEY_USED_SWITCH);
		int switchUsedNum = 100;
		try {
			String decryptedSavedValue = Encrypter.decryptDES(savedValue,
					GameConstants.DES_KEY);
			switchUsedNum = Integer.parseInt(decryptedSavedValue);
		} catch (Exception ex) {
			LogUtils.v(TAG, "保存的洗牌道具使用次数异常，重置为最大值");
		}
		return switchUsedNum;
	}

	/**
	 * 保存使用的锤子数量
	 * 
	 * @param context
	 * @param stage
	 */
	public static void saveSwitchUsedNum(Context context, int switchUsedNum) {
		SPValueHandler.putStringParam(context, KEY_USED_SWITCH, Encrypter
				.encryptDES(String.valueOf(switchUsedNum),
						GameConstants.DES_KEY));
	}

	/**
	 * 获取当前分数
	 * 
	 * @param context
	 * @return
	 */
	public static long getCurrentScore(Context context) {
		String savedValue = SPValueHandler.getStringParam(context, KEY_SCORE);
		long stage = 0l;
		try {
			String decryptedSavedValue = Encrypter.decryptDES(savedValue,
					GameConstants.DES_KEY);
			stage = Long.parseLong(decryptedSavedValue);
		} catch (Exception ex) {
			LogUtils.v(TAG, "保存的分数数据异常，重置为0");
		}
		return stage;
	}

	/**
	 * 保存当前分数
	 * 
	 * @param context
	 * @param stage
	 */
	public static void saveCurrentScore(Context context, long score) {
		SPValueHandler.putStringParam(context, KEY_SCORE, Encrypter.encryptDES(
				String.valueOf(score), GameConstants.DES_KEY));
	}

	/***
	 * 保存星星数据
	 * 
	 * @param context
	 * @param star
	 */
	public static void saveStars(Context context, String star) {
		SPValueHandler.putStringParam(context, KEY_STARS, star);
	}

	/***
	 * 获取星星数据
	 * 
	 * @param context
	 * @param star
	 */
	public static String getStars(Context context) {
		return SPValueHandler.getStringParam(context, KEY_STARS);
	}

	/**
	 * 获取当前分数
	 * 
	 * @param context
	 * @return
	 */
	public static long get1010CurrentScore(Context context) {
		String savedValue = SPValueHandler.getStringParam(context,
				KEY_1010_SCORE);
		long score = 0l;
		try {
			String decryptedSavedValue = Encrypter.decryptDES(savedValue,
					GameConstants.DES_KEY);
			score = Long.parseLong(decryptedSavedValue);
		} catch (Exception ex) {
			LogUtils.v(TAG, "保存的分数数据异常，重置为0");
		}
		return score;
	}

	/**
	 * 保存当前分数
	 * 
	 * @param context
	 * @param stage
	 */
	public static void save1010CurrentScore(Context context, long score) {
		SPValueHandler.putStringParam(context, KEY_1010_SCORE, Encrypter
				.encryptDES(String.valueOf(score), GameConstants.DES_KEY));
	}

	public static long get1010HighScore(Context context) {
		String savedValue = SPValueHandler.getStringParam(context,
				KEY_1010_HIGH_SCORE);
		long score = 0l;
		try {
			String decryptedSavedValue = Encrypter.decryptDES(savedValue,
					GameConstants.DES_KEY);
			score = Long.parseLong(decryptedSavedValue);
		} catch (Exception ex) {
			LogUtils.v(TAG, "保存的分数数据异常，重置为0");
		}
		return score;
	}

	/**
	 * 保存当前分数
	 * 
	 * @param context
	 * @param stage
	 */
	public static void save1010HighScore(Context context, long score) {
		SPValueHandler.putStringParam(context, KEY_1010_HIGH_SCORE, Encrypter
				.encryptDES(String.valueOf(score), GameConstants.DES_KEY));
	}

	/***
	 * 保存星星数据
	 * 
	 * @param context
	 * @param star
	 */
	public static void save1010Stars(Context context, String star) {
		SPValueHandler.putStringParam(context, KEY_1010_STARS, star);
	}

	/***
	 * 获取星星数据
	 * 
	 * @param context
	 * @param star
	 */
	public static String get1010Stars(Context context) {
		return SPValueHandler.getStringParam(context, KEY_1010_STARS);
	}

	/***
	 * 保存组合星星
	 * 
	 * @param context
	 * @param groups
	 */
	public static void save1010GroupSigns(Context context, String groups) {
		SPValueHandler.putStringParam(context, KEY_1010_GROUPS, groups);
	}

	/***
	 * 获取组合星星
	 * 
	 * @param context
	 * @param star
	 */
	public static String get1010GroupSigns(Context context) {
		return SPValueHandler.getStringParam(context, KEY_1010_GROUPS);
	}

	/**
	 * 获取组合星星，同时将获取的String转换为int[]
	 * 
	 * @param context
	 * @return
	 */
	public static int[] get1010GroupSignsWithFormat(Context context) {
		String groupSignsStr = get1010GroupSigns(context);
		String[] splits = groupSignsStr.split(";");
		int[] signs = new int[splits.length];
		for (int i = 0; i < splits.length; i++) {
			signs[i] = Integer.parseInt(splits[i]);
		}
		return signs;
	}

	/**
	 * 将星星组合存到sp文件中
	 * 
	 * @param context
	 * @param groups
	 */
	public static void save1010GroupSignsWithFormat(Context context,
			int[] groups) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < groups.length; i++) {
			sb.append(groups[i]);
			sb.append(";");
		}
		sb.deleteCharAt(sb.length() - 1);
		save1010GroupSigns(context, sb.toString());
	}

	/**
	 * 获取使用刷新道具的次数
	 * 
	 * @param context
	 * @return
	 */
	public static int get1010RefreshUsedNum(Context context) {
		String savedValue = SPValueHandler.getStringParam(context,
				KEY_1010_USED_REFRESH);
		int refreshUsedNum = 100;
		try {
			String decryptedSavedValue = Encrypter.decryptDES(savedValue,
					GameConstants.DES_KEY);
			refreshUsedNum = Integer.parseInt(decryptedSavedValue);
		} catch (Exception ex) {
			LogUtils.v(TAG, "保存的刷新道具使用次数异常，重置为最大值");
		}
		return refreshUsedNum;
	}

	/**
	 * 保存使用刷新道具的次数
	 * 
	 * @param context
	 * @param stage
	 */
	public static void save1010RefreshUsedNum(Context context,
			int refreshUsedNum) {
		SPValueHandler.putStringParam(context, KEY_1010_USED_REFRESH, Encrypter
				.encryptDES(String.valueOf(refreshUsedNum),
						GameConstants.DES_KEY));
	}

	public static int get1010OneBlockUsedNum(Context context) {
		String savedValue = SPValueHandler.getStringParam(context,
				KEY_1010_ONE_BLOCK_USED_NUM);
		int oneBLockUsedNum = 0;
		try {
			String decryptedSavedValue = Encrypter.decryptDES(savedValue,
					GameConstants.DES_KEY);
			oneBLockUsedNum = Integer.parseInt(decryptedSavedValue);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return oneBLockUsedNum;
	}

	/**
	 * 保存使用刷新道具的次数
	 * 
	 * @param context
	 * @param stage
	 */
	public static void set1010OneBlockUsedNum(Context context,
			int refreshUsedNum) {
		SPValueHandler.putStringParam(context, KEY_1010_ONE_BLOCK_USED_NUM,
				Encrypter.encryptDES(String.valueOf(refreshUsedNum),
						GameConstants.DES_KEY));
	}

	/**
	 * 使用继续通过关的次数
	 * 
	 * @param context
	 * @return
	 */
	public static int get1010GoonNum(Context context) {
		String savedValue = SPValueHandler.getStringParam(context,
				KEY_1010_USED_GOON);
		int refreshUsedNum = 100;
		try {
			String decryptedSavedValue = Encrypter.decryptDES(savedValue,
					GameConstants.DES_KEY);
			refreshUsedNum = Integer.parseInt(decryptedSavedValue);
		} catch (Exception ex) {
			LogUtils.v(TAG, "保存的继续通关道具适量次数异常，重置为最大值");
		}
		return refreshUsedNum;
	}

	/**
	 * 保存使用刷新道具的次数
	 * 
	 * @param context
	 * @param stage
	 */
	public static void save1010GoonNum(Context context, int goonNum) {
		SPValueHandler.putStringParam(context, KEY_1010_USED_GOON, Encrypter
				.encryptDES(String.valueOf(goonNum), GameConstants.DES_KEY));
	}

	public static void reset1010GameData(Context context) {
		SPUtils.save1010CurrentScore(context, 0);
		SPUtils.save1010RefreshUsedNum(context, 0);
		SPUtils.set1010OneBlockUsedNum(context, 0);
		SPUtils.save1010GoonNum(context, 0);
		SPUtils.setPointProgress(context, 0);
	}

	/***
	 * 设置公告地址
	 * 
	 * @param context
	 * @param star
	 */
	public static void setAnnouncementAddr(Context context,
			String announcementAddr) {
		SPValueHandler.putStringParam(context, KEY_ANNOUNCEMENT_ADDR,
				announcementAddr);
	}

	/***
	 * 获取公告地址
	 * 
	 * @param context
	 * @param star
	 */
	public static String getAnnouncementAddr(Context context) {
		String addr = SPValueHandler.getStringParam(context,
				KEY_ANNOUNCEMENT_ADDR);
		if (addr.equalsIgnoreCase("null") || !addr.startsWith("http")) {
			addr = GameConstants.ANNOUNCEMENT_ADDR;
		}
		return addr;
	}

	/**
	 * 是否开启公告
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAnnouncementShow(Context context) {
		boolean isShow = SPValueHandler.getBooleanParam(context,
				KEY_IS_ANNOUNCEMENT_SHOW);
		return isShow;
	}

	/**
	 * 开启/关闭公告
	 * 
	 * @param context
	 */
	public static void setAnnouncementShow(Context context, boolean isShow) {
		SPValueHandler.putBooleanParam(context, KEY_IS_ANNOUNCEMENT_SHOW,
				isShow);
	}

	/**
	 * 在什么时候显示
	 * 
	 * @param context
	 * @return
	 */
	public static int getAnnouncementShowPlace(Context context) {
		int showPlace = SPValueHandler.getIntParam(context,
				KEY_ANNOUNCEMENT_SHOW_NODE);
		return showPlace;
	}

	/**
	 * 
	 * 显示的位置
	 * 
	 * @param context
	 * @param stage
	 */
	public static void setAnnouncementShowPlace(Context context, int place) {
		SPValueHandler.putIntParams(context, KEY_ANNOUNCEMENT_SHOW_NODE, place);
	}

	/**
	 * 公告显示的次数
	 * 
	 * @param context
	 * @return
	 */
	public static int getAnnouncementShowNum(Context context) {
		int showedNum = SPValueHandler.getIntParam(context,
				KEY_ANNOUNCEMENT_AUTO_SHOW_NUM);
		return showedNum;
	}

	/**
	 * 
	 * 显示的位置 <b>因为服务器定义的次数为：[0-0|1-无穷|2-1|3-2|4-3],所以，这里进行了一下处理</b>
	 * 
	 * @param context
	 * @param stage
	 */
	public static void setAnnouncementShowNum(Context context, int num) {
		switch (num) {
		case 0:
			num = 0;
			break;
		case 1:
			num = Integer.MAX_VALUE;
			break;
		default:
			num--;
			break;
		}
		SPValueHandler.putIntParams(context, KEY_ANNOUNCEMENT_AUTO_SHOW_NUM,
				num);
	}

	/**
	 * 获取上次公告展示的时间
	 * 
	 * @param context
	 * @return
	 */
	public static long getLastAnnouncementShowTime(Context context) {
		return SPValueHandler.getLongParam(context, KEY_LAST_ANNOUNCEMENT_TIME);
	}

	/**
	 * 设置上次公告展示时间
	 * 
	 * @param context
	 * @param time
	 * @return
	 */
	public static void setLastAnnouncementShowTime(Context context, long time) {
		SPValueHandler.putLongParam(context, KEY_LAST_ANNOUNCEMENT_TIME, time);
	}

	/**
	 * 获取上次公告展示的时间
	 * 
	 * @param context
	 * @return
	 */
	public static int getTodayAnnouncementNum(Context context) {
		int value = SPValueHandler.getIntParam(context,
				KEY_TODAY_ANNOUNCEMENT_SHOW_NUM);
		return value < 0 ? 0 : value;
	}

	/**
	 * 设置上次公告展示时间
	 * 
	 * @param context
	 * @param time
	 * @return
	 */
	public static void setTodayAnnouncementNum(Context context, int num) {
		SPValueHandler.putIntParams(context, KEY_TODAY_ANNOUNCEMENT_SHOW_NUM,
				num);
	}

	/**
	 * 中文版春节活动是否开启
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isSpringFestivalActOn(Context context, String stage) {
		return !SPValueHandler.getBooleanParam(context,
				KEY_IS_SPRING_FESTIVAL_ACT_ON + stage);
	}

	/**
	 * 保存从左德峰的接口“中文版春节活动开关”中读取到的配置
	 * 
	 * @param context
	 * @param isOn
	 */
	public static void setSpringFestivalActOn(Context context, String stage,
			boolean isOn) {
		SPValueHandler.putBooleanParam(context, KEY_IS_SPRING_FESTIVAL_ACT_ON
				+ stage, !isOn);
	}

	/**
	 * 春节活动是否已经参加过了
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isSpringFestivalShowed(Context context, String stage) {
		return !SPValueHandler.getBooleanParam(context,
				KEY_IS_SPRING_FESTIVAL_SHOWED + stage);
	}

	/**
	 * 春节活动是否已经参加过了
	 * 
	 * @param context
	 * @param isOn
	 */
	public static void setSpringFestivalShowed(Context context, String stage,
			boolean isShowed) {
		SPValueHandler.putBooleanParam(context, KEY_IS_SPRING_FESTIVAL_SHOWED
				+ stage, !isShowed);
	}

	/**
	 * 是否已经领取过第一关的奖励
	 * 
	 * @param context
	 * @param isOn
	 */
	public static void setIsStageOneAward(Context context, boolean isAwarded) {
		SPValueHandler.putBooleanParam(context, KEY_IS_STAGE_ONE_AEARD,
				!isAwarded);
	}

	/**
	 * 是否已经领取过第二关的奖励
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isStageTwoAward(Context context) {
		return !SPValueHandler.getBooleanParam(context, KEY_IS_STAGE_TWO_AEARD);
	}

	/**
	 * 是否已经领取过第二关的奖励
	 * 
	 * @param context
	 * @param isOn
	 */
	public static void setIsStageTwoAward(Context context, boolean isAwarded) {
		SPValueHandler.putBooleanParam(context, KEY_IS_STAGE_TWO_AEARD,
				!isAwarded);
	}

	/**
	 * 是否已经领取过第一关的奖励
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isStageOneAward(Context context) {
		return !SPValueHandler.getBooleanParam(context, KEY_IS_STAGE_ONE_AEARD);
	}

	/**
	 * 第三关关卡完成是否已经弹出过对话框了
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isStageLBShowed(Context context, String stage) {
		boolean isGuideShowed = SPValueHandler.getBooleanParam(context,
				KEY_IS_STAGE_THREE_SHOWED + stage);
		return !isGuideShowed;
	}

	/**
	 * 保存是否已经弹出过小点引导界面
	 * 
	 * @param context
	 */
	public static void setStageLBShowed(Context context, boolean value,
			String stage) {
		SPValueHandler.putBooleanParam(context, KEY_IS_STAGE_THREE_SHOWED
				+ stage, !value);
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isQuickBuyDialogShowed(Context context) {
		boolean isQuickBuyDialogShowed = SPValueHandler.getBooleanParam(
				context, KEY_IS_QUICK_BUY_DIALOG_SHOWED);
		return !isQuickBuyDialogShowed;
	}

	/**
	 * 
	 * @param context
	 */
	public static void setQuickBuyDialogShowed(Context context, boolean value) {
		SPValueHandler.putBooleanParam(context, KEY_IS_QUICK_BUY_DIALOG_SHOWED,
				!value);
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isStageThreePassed(Context context) {
		boolean isQuickBuyDialogShowed = SPValueHandler.getBooleanParam(
				context, KEY_IS_STAGE_THREE_PASS);
		return !isQuickBuyDialogShowed;
	}

	/**
	 * 
	 * @param context
	 */
	public static void setStageThreePassed(Context context, boolean value) {
		SPValueHandler
				.putBooleanParam(context, KEY_IS_STAGE_THREE_PASS, !value);
	}

	/**
	 * 是否显示今日奖励
	 */
	public static void setTodayRewards(Context context, String str) {
		SPValueHandler.putStringParam(context, TODAY_REWARDS_SHOU,
				Encrypter.encryptDES(str, GameConstants.DES_KEY));
	}
/**
 * 获取今日奖励幸运星数量
 * @param context
 * @return
 */
	public static String getTodayRewards(Context context) {
		String num = "";
		try {
			num = Encrypter.decryptDES(
					SPValueHandler.getStringParam(context, TODAY_REWARDS_SHOU),
					GameConstants.DES_KEY);

		} catch (Exception e) {
			// 保存数据异常
		}
		return num;
	}
	/**
	 * 是否显示营销页面，目前设定为电信和移动购买时显示营销页
	 * @param context
	 * @return
	 */
	public static boolean isShowYingXiaoDialog(Context context){
		if(PhoneInfoGetter.getMobileSP(context).equals(ConstantsHolder.CHINA_TELECOM)){
			return true;
		}else {
			return false;
		}
		
	}
	/**
	 * 保存是否显示商城按钮
	 * 
	 * @param context
	 */
	public static void setIsShowBtShop(Context context,boolean value) {
		SPValueHandler.putBooleanParam(context, IS_SHOW_BT_SHOP, value);
	}
	/**
	 * 获取是否显示商城按钮
	 * 
	 * @param context
	 */
	public static boolean getIsShowBtShop(Context context) {
		return SPValueHandler.getBooleanParam(context, IS_SHOW_BT_SHOP);
	}

	/**
	 * 获取是否是第一次进入游戏
	 */
	public static boolean getIsFirstGame(Context context) {
		return SPValueHandler.getBooleanParam(context, IS_FIRST_GAME);
	}
	
	/**
	 * 设置是否时第一次进入游戏
	 */
	public static void setIsFirstGame(Context context,boolean value) {
		SPValueHandler.putBooleanParam(context, IS_FIRST_GAME, value);
	}

	/**
	 * 获取第三关是否结束
	 */
	public static boolean isStageThirdEnd(Context context) {
		return !SPValueHandler.getBooleanParam(context, IS_THIRD_STAGE);
	}
	
	/**
	 * 设置第三关是否结束
	 */
	public static void setIsStageThirdEnd(Context context, boolean value) {
		SPValueHandler.putBooleanParam(context, IS_THIRD_STAGE, !value);
	}
	
	/**
	 * 获取云雨道具小点点进度
	 */
	public static int getPointProgress(Context context) {
		return SPValueHandler.getIntParam(context, POINT_PROGRESS);
	}
	
	/**
	 * 设置云雨道具小点点进度
	 */
	public static void setPointProgress(Context context, int value) {
		SPValueHandler.putIntParams(context, POINT_PROGRESS, value);
	}
	/**
	 * 是否是最新版
	 * @param context
	 * @param boo
	 */
	public static void setisnewversion(Context context,boolean boo) {
		SPValueHandler.putBooleanParam(context, IS_NEW_VERSION,
				boo);
	}
	public static boolean getisnewversion(Context context) {
		boolean booleanParam = SPValueHandler.getBooleanParam(context,
				IS_NEW_VERSION );
		return booleanParam;
	}
	
	/**
	 * 获取服务器开关
	 */
	public static boolean getServerOnOff(Context context) {
		return !SPValueHandler.getBooleanParam(context, IS_SERVER_ON);
	}
	
	/**
	 * 设置服务器开关
	 */
	public static void setServerOnOff(Context context, boolean value) {
		SPValueHandler.putBooleanParam(context, IS_SERVER_ON, !value);
	}
	
	/**
	 * 获取是否初始化基地
	 */
	public static boolean getIsInitJD(Context context) {
		return !SPValueHandler.getBooleanParam(context, IS_INIT_JD);
	}
	
	/**
	 * 设置是否初始化基地
	 */
	public static void setIsInitJD(Context context, boolean value) {
		SPValueHandler.putBooleanParam(context, IS_INIT_JD, !value);
	}
	
	/**
	 * 获取初始化控制日期
	 */
	public static String getInitControllerDate(Context context) {
		String value = SPValueHandler.getStringParam(context, INIT_CONTROLLER_DATE);
		if (value == null || "".equals(value) || "null".equals(value)) {
			return "2016-2-20";
		}
		return value;
	}
	
	/**
	 * 设置初始化控制日期
	 */
	public static void setInitControllerDate(Context context, String value) {
		SPValueHandler.putStringParam(context, INIT_CONTROLLER_DATE, value);
	}
}
