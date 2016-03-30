package com.zplay.game.popstarog.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;

import com.orange.opengl.texture.TextureOptions;
import com.orange.opengl.texture.bitmap.BitmapTextureFormat;
import com.orange.res.FontRes;
import com.orange.res.RegionRes;

public class ResourceManager {
	public static void loadSplashTextures() {
		RegionRes.loadTexturesFromAssetFile(640, 960, "splash", "splash.png",
				"textures/");
	}

	public static void unloadSplashTextures() {
		RegionRes.getTextureRegion("splash").getTexture().unload();
	}

	private static void loadMainSceneTextures() {
		RegionRes.loadTexturesFromAssetFile(640, 960, "bg", "bg.png");
		RegionRes.loadTexturesFromAssetFile(64, 64, "block_select",
				"block_select.png", "textures/mainScene/");
		RegionRes.loadTexturesFromAssetFile(18, 18, "blue_star",
				"blue_star.png", "textures/mainScene/",
				BitmapTextureFormat.RGBA_4444, TextureOptions.NEAREST);
		RegionRes.loadTexturesFromAssetFile(75, 72, "box", "box.png");
		RegionRes.loadTexturesFromAssetFile(75, 74, "about", "about.png");
		RegionRes.loadTexturesFromAssetFile(22, 22, "reddot", "reddot.png");

		RegionRes.loadTexturesFromAssetFile(81, 74, "btn_hammer",
				"btn_hammer.png");
		RegionRes.loadTexturesFromAssetFile(80, 80, "fly_star", "fly_star.png");
		RegionRes.loadTexturesFromAssetFile(95, 104, "newbie_gift",
				"newbie_gift.png");
		RegionRes.loadTexturesFromAssetFile(95, 104, "redPacket",
				"redPacket.png");

		RegionRes
				.loadTexturesFromAssetFile(293, 52, "btn_shop", "btn_shop.png");
		RegionRes.loadTexturesFromAssetFile(293, 52, "btn_new", "btn_new.png");
		RegionRes
				.loadTexturesFromAssetFile(294, 81, "btn_1010", "btn_1010.png");
		RegionRes.loadTexturesFromAssetFile(75, 69, "cdkey", "cdkey.png");
		RegionRes.loadTexturesFromAssetFile(18, 18, "green_star",
				"green_star.png", "textures/mainScene/",
				BitmapTextureFormat.RGBA_4444, TextureOptions.NEAREST);
		RegionRes.loadTexturesFromAssetFile(505, 52, "highscore",
				"highscore.png");
		RegionRes.loadTexturesFromAssetFile(296, 296, "light_blue",
				"light_blue.png");
		RegionRes.loadTexturesFromAssetFile(296, 296, "light_green",
				"light_green.png");
		RegionRes.loadTexturesFromAssetFile(296, 296, "light_pink",
				"light_pink.png");
		RegionRes.loadTexturesFromAssetFile(296, 296, "light_red",
				"light_red.png");
		RegionRes.loadTexturesFromAssetFile(296, 296, "light_yellow",
				"light_yellow.png");
		RegionRes.loadTexturesFromAssetFile(566, 332, "offical_sign",
				"offical_sign.png");
		RegionRes.loadTexturesFromAssetFile(37, 43, "option_button",
				"option_button.png");
		RegionRes.loadTexturesFromAssetFile(64, 64, "particle", "particle.png",
				"textures/mainScene/", BitmapTextureFormat.RGBA_4444,
				TextureOptions.NEAREST);
		RegionRes.loadTexturesFromAssetFile(18, 18, "pink_star",
				"pink_star.png", "textures/mainScene/",
				BitmapTextureFormat.RGBA_4444, TextureOptions.NEAREST);
		RegionRes.loadTexturesFromAssetFile(18, 18, "red_star", "red_star.png",
				"textures/mainScene/", BitmapTextureFormat.RGBA_4444,
				TextureOptions.NEAREST);
		RegionRes.loadTexturesFromAssetFile(480, 240, "stage_clear",
				"stage_clear.png");
		RegionRes.loadTexturesFromAssetFile(196, 61, "star_display",
				"star_display.png");
		RegionRes.loadTexturesFromAssetFile(71, 64, "switch", "switch.png");
		RegionRes.loadTexturesFromAssetFile(71, 64, "rollback", "rollback.png");
		RegionRes.loadTexturesFromAssetFile(4, 4, "white", "white.png");
		RegionRes.loadTexturesFromAssetFile(18, 18, "yellow_star",
				"yellow_star.png", "textures/mainScene/",
				BitmapTextureFormat.RGBA_4444, TextureOptions.NEAREST);
		RegionRes.loadTexturesFromAssets("textures/blocks/blocks.xml");

		RegionRes.loadTexturesFromAssetFile(600, 140, "combo_good",
				"combo_good.png", "textures/");
		RegionRes.loadTexturesFromAssetFile(600, 140, "combo_cool",
				"combo_cool.png", "textures/");
		RegionRes.loadTexturesFromAssetFile(600, 140, "combo_awesome",
				"combo_awesome.png", "textures/");
		RegionRes.loadTexturesFromAssetFile(600, 140, "combo_fantastic",
				"combo_fantastic.png", "textures/");
	}

	public static void load1010SceneTextures() {
		RegionRes.loadTexturesFromAssetFile(59, 61, "1010_back_btn",
				"1010_back_btn.png", "textures/1010/");
		RegionRes.loadTexturesFromAssetFile(58, 59, "1010_block_container",
				"1010_block_container.png", "textures/1010/");
		RegionRes.loadTexturesFromAssetFile(201, 58, "1010_new_game",
				"1010_new_game.png", "textures/1010/");
		RegionRes.loadTexturesFromAssetFile(460, 343, "1010_tips",
				"1010_tips.png", "textures/1010/");
		RegionRes.loadTexturesFromAssetFile(137, 45, "lucky_star_bg",
				"lucky_star_bg.png", "textures/1010/");
		RegionRes.loadTexturesFromAssetFile(44, 39, "medal", "medal.png",
				"textures/1010/");
		RegionRes.loadTexturesFromAssetFile(32, 45, "pause", "pause.png",
				"textures/1010/");
		RegionRes.loadTexturesFromAssetFile(49, 44, "refresh", "refresh.png",
				"textures/1010/");
		RegionRes.loadTexturesFromAssets("textures/1010/stars.xml");
		RegionRes
				.loadTexturesFromAssets("textures/1010/1010_continue_btns.xml");
		RegionRes.loadTexturesFromAssetFile(400, 115, "1010_over_icon",
				"1010_over_icon.png", "textures/1010overDialog/");
		RegionRes.loadTexturesFromAssetFile(640, 158, "beyond",
				"beyond.png", "textures/1010/");
		
		// 加载天气道具资源 add by liufengqiang
		RegionRes.loadTexturesFromAssetFile(560, 110, "weather_container",
				"weather_container.png", "textures/1010/");
		RegionRes.loadTexturesFromAssetFile(32, 58, "black_flash",
				"black_flash.png", "textures/1010/");
		RegionRes.loadTexturesFromAssetFile(32, 58, "white_flash",
				"white_flash.png", "textures/1010/");
		RegionRes.loadTexturesFromAssetFile(50, 47, "rain",
				"rain.png", "textures/1010/");
		RegionRes.loadTexturesFromAssetFile(12, 12, "white_point",
				"white_point.png", "textures/1010/");
		RegionRes.loadTexturesFromAssetFile(62, 61, "flash_aim",
				"flash_aim.png", "textures/1010/");
		RegionRes.loadTexturesFromAssetFile(55, 486, "rain_drop",
				"rain_drop.png", "textures/1010/");
	}

	public static void unload1010SceneTextures() {
		RegionRes.getTextureRegion("1010_back_btn").getTexture().unload();
		RegionRes.getTextureRegion("1010_block_container").getTexture()
				.unload();
		RegionRes.getTextureRegion("1010_new_game").getTexture().unload();
		RegionRes.getTextureRegion("1010_tips").getTexture().unload();
		RegionRes.getTextureRegion("lucky_star_bg").getTexture().unload();
		RegionRes.getTextureRegion("medal").getTexture().unload();
		RegionRes.getTextureRegion("pause").getTexture().unload();
		RegionRes.getTextureRegion("refresh").getTexture().unload();
		RegionRes.getRegion("star").getTexture().unload();
		RegionRes.getRegion("1010_continue_game_enable").getTexture().unload();
		RegionRes.getTextureRegion("1010_over_icon").getTexture().unload();
		RegionRes.getTextureRegion("beyond").getTexture().unload();
		
		// 卸载天气道具资源 add by liufengqiang
		RegionRes.getTextureRegion("weather_container").getTexture().unload();
		RegionRes.getTextureRegion("black_flash").getTexture().unload();
		RegionRes.getTextureRegion("white_flash").getTexture().unload();
		RegionRes.getTextureRegion("rain").getTexture().unload();
		RegionRes.getTextureRegion("white_point").getTexture().unload();
		RegionRes.getTextureRegion("flash_aim").getTexture().unload();
		RegionRes.getTextureRegion("rain_drop").getTexture().unload();
	}

	private static void loadMainSceneFonts() {

	}

	public static void loadMainSceneResources() {
		loadMainSceneTextures();
		loadMainSceneFonts();
	}

	public static void loadIphoneDialogTextures() {
		RegionRes.loadTexturesFromAssetFile(572, 276, "alert_bg",
				"alert_bg.png", "textures/dialogNewGame/");
		RegionRes.loadTexturesFromAssetFile(256, 88, "alert_cancel",
				"alert_cancel.png");
		RegionRes
				.loadTexturesFromAssetFile(256, 88, "alert_ok", "alert_ok.png");
	}

	public static void unloadIphoneDialogTextures() {
		RegionRes.getTextureRegion("alert_bg").getTexture().unload();
		RegionRes.getTextureRegion("alert_cancel").getTexture().unload();
		RegionRes.getTextureRegion("alert_ok").getTexture().unload();
	}

	// 继续通关对话框
	public static void loadGoonDialogTextures() {
		RegionRes.loadTexturesFromAssetFile(493, 100, "yellow_btn_long",
				"yellow_btn_long.png", "textures/");
		RegionRes.loadTexturesFromAssetFile(64, 64, "options_quit",
				"options_quit.png", "textures/");
		RegionRes.loadTexturesFromAssetFile(640, 626, "jxtg_bg", "jxtg_bg.png",
				"textures/dialogGoon/");
		RegionRes.loadTexturesFromAssetFile(227, 57, "jxtg", "jxtg.png",
				"textures/dialogGoon/");
	}

	public static void unloadGoonDialogTextures() {
		RegionRes.getTextureRegion("options_quit").getTexture().unload();
		RegionRes.getTextureRegion("jxtg").getTexture().unload();
		RegionRes.getTextureRegion("jxtg_bg").getTexture().unload();
		RegionRes.getTextureRegion("yellow_btn_long").getTexture().unload();
	}

	public static void loadBuyDialogTextures(Activity activity) {
		RegionRes.loadTexturesFromAssetFile(64, 64, "options_quit",
				"options_quit.png", "textures/");
		RegionRes.loadTexturesFromAssetFile(640, 764, "quick_buy_bg",
				"quick_buy_bg.png", "textures/dialogQuickBuy/");
		RegionRes.loadTexturesFromAssetFile(223, 70, "quick_buy_btn_ok",
				"btn_ok.png", "textures/dialogQuickBuy/");
		RegionRes.loadTexturesFromAssetFile(294, 92, "yellow_btn_qb",
				"yellow_btn.png", "textures/");
		RegionRes.loadTexturesFromAssetFile(223, 70, "btn_go_shop",
				"btn_go_shop.png", "textures/dialogQuickBuy/");
		FontRes.loadFont(512, 256,
				Typeface.createFromAsset(activity.getAssets(), "fonts/font.ttf"), 50f,
				true, Color.WHITE, "50white_qb");
	}
	
	public static void unloadBuyDialogTextures() {
		RegionRes.getTextureRegion("quick_buy_bg").getTexture().unload();
		RegionRes.getTextureRegion("options_quit").getTexture().unload();
		RegionRes.getTextureRegion("quick_buy_btn_ok").getTexture().unload();
		RegionRes.getTextureRegion("yellow_btn_qb").getTexture().unload();
		RegionRes.getTextureRegion("btn_go_shop").getTexture().unload();
		FontRes.getFont("50white_qb").unload();
	}
	
	public static void loadStarGifDialogTextures(Activity activity) {
		RegionRes.loadTexturesFromAssetFile(579, 631, "common_bg",
				"common_bg.png", "textures/dialogCent/");
	}
	
	public static void unloadStarGifDialogTextures() {
		RegionRes.getTextureRegion("common_bg").getTexture().unload();
	}

//	public static void loadTGJLDialog(Activity activity) {
//		RegionRes.loadTexturesFromAssetFile(640, 764, "tgjl", "tgjl.png",
//				"textures/dialogTGJL/");
//		RegionRes.loadTexturesFromAssetFile(259, 81, "tgjl_btn_ok",
//				"btn_ok.png");
//		FontRes.loadFont(512, 512, Typeface.createFromAsset(
//				activity.getAssets(), "fonts/font.ttf"), 35, true, Color.WHITE,
//				"tgjl");
//	}

//	public static void unloadTGJLDialog() {
//		RegionRes.getTextureRegion("tgjl").getTexture().unload();
//		RegionRes.getTextureRegion("tgjl_btn_ok").getTexture().unload();
//		FontRes.getFont("tgjl").unload();
//	}

	public static void loadItemUseDialog(Activity activity) {
		RegionRes.loadTexturesFromAssetFile(64, 64, "options_quit_item",
				"options_quit.png", "textures/");
		RegionRes.loadTexturesFromAssetFile(518, 301, "item_used_alert_bg",
				"item_used_alert_bg.png", "textures/dialogItemuse/");
		RegionRes.loadTexturesFromAssetFile(111, 111, "item_hammer",
				"item_hammer.png", "textures/dialogItemuse/");
		RegionRes.loadTexturesFromAssetFile(111, 111, "item_switch",
				"item_switch.png", "textures/dialogItemuse/");
		RegionRes.loadTexturesFromAssetFile(493, 100, "yellow_btn_long_ud",
				"yellow_btn_long.png", "textures/");
		FontRes.loadFont(512, 256,
				Typeface.createFromAsset(activity.getAssets(), "fonts/font.ttf"), 50f,
				true, Color.WHITE, "50white_ud");
	}

	public static void unloadItemUseDialog() {
		RegionRes.getTextureRegion("options_quit_item").getTexture().unload();
		RegionRes.getTextureRegion("item_used_alert_bg").getTexture().unload();
		RegionRes.getTextureRegion("item_hammer").getTexture().unload();
		RegionRes.getTextureRegion("item_switch").getTexture().unload();
		RegionRes.getTextureRegion("yellow_btn_long_ud").getTexture().unload();
		FontRes.getFont("50white_ud").unload();
	}

	public static void loadOptionsDialogTextures() {
		RegionRes.loadTexturesFromAssetFile(293, 52, "btn_shop",
				"btn_shop.png", "textures/optionsScene/");
		RegionRes.loadTexturesFromAssetFile(80, 80, "lucky_star",
				"lucky_star.png");
		RegionRes.loadTexturesFromAssetFile(45, 44, "options_back",
				"options_back.png");
		RegionRes.loadTexturesFromAssetFile(293, 52, "options_mainmenu",
				"options_mainmenu.png");
		RegionRes.loadTexturesFromAssetFile(484, 539, "options_bg",
				"options_bg.png");
		RegionRes
				.loadTexturesFromAssets("textures/optionsScene/audio_switch.xml");
	}

	public static void unloadOptionsDialogTextures() {
		RegionRes.getTextureRegion("btn_shop").getTexture().unload();
		RegionRes.getTextureRegion("lucky_star").getTexture().unload();
		RegionRes.getTextureRegion("options_back").getTexture().unload();
		RegionRes.getTextureRegion("options_mainmenu").getTexture().unload();
		RegionRes.getTextureRegion("options_bg").getTexture().unload();
		RegionRes.getRegion("audio_switch").getTexture().unload();
	}

	public static void loadHammerTextures() {
		RegionRes.loadTexturesFromAssets("textures/hammers/hammers.xml");
	}

	public static void unloadHammerTextures() {
		RegionRes.getRegion("hammer").getTexture().unload();
	}

	public static void loadShopTextures() {
		RegionRes.loadTexturesFromAssetFile(4, 4, "bg_shopitem",
				"bg_shopitem.png", "textures/shopScene/");
		RegionRes.loadTexturesFromAssetFile(62, 63, "icon_buy_cz",
				"icon_buy_cz.png");
		RegionRes.loadTexturesFromAssetFile(80, 80, "star", "star.png");
		RegionRes.loadTexturesFromAssetFile(54, 40, "icon_hot", "icon_hot.png");
		RegionRes.loadTexturesFromAssetFile(62, 63, "icon_feel", "icon_feel.png");
		RegionRes.loadTexturesFromAssetFile(70, 70, "option_back",
				"option_back.png");
		RegionRes.loadTexturesFromAssetFile(158, 92, "1cent", "1cent.png");
		RegionRes.loadTexturesFromAssetFile(69, 87, "5zhe", "5zhe.png");
		RegionRes.loadTexturesFromAssetFile(68, 87, "6zhe", "6zhe.png");
		RegionRes.loadTexturesFromAssetFile(68, 87, "7zhe", "7zhe.png");
		RegionRes.loadTexturesFromAssetFile(68, 87, "8zhe", "8zhe.png");
		RegionRes.loadTexturesFromAssetFile(68, 87, "9zhe", "9zhe.png");
		RegionRes.loadTexturesFromAssetFile(600, 104, "5_discount_bg",
				"5_discount_bg.png");
		RegionRes.loadTexturesFromAssetFile(294, 92, "yellow_btn",
				"yellow_btn.png", "textures/");
	}

	public static void unloadShopTextures() {
		RegionRes.getTextureRegion("bg_shopitem").getTexture().unload();
		RegionRes.getTextureRegion("star").getTexture().unload();
		RegionRes.getTextureRegion("icon_buy_cz").getTexture().unload();
		RegionRes.getTextureRegion("icon_feel").getTexture().unload();
		RegionRes.getTextureRegion("icon_hot").getTexture().unload();
		RegionRes.getTextureRegion("option_back").getTexture().unload();
		RegionRes.getTextureRegion("1cent").getTexture().unload();
		RegionRes.getTextureRegion("yellow_btn").getTexture().unload();
		RegionRes.getTextureRegion("5zhe").getTexture().unload();
		RegionRes.getTextureRegion("6zhe").getTexture().unload();
		RegionRes.getTextureRegion("7zhe").getTexture().unload();
		RegionRes.getTextureRegion("8zhe").getTexture().unload();
		RegionRes.getTextureRegion("9zhe").getTexture().unload();
		RegionRes.getTextureRegion("5_discount_bg").getTexture().unload();
	}

	public static void loadAboutSceneResources(Activity activity) {
		RegionRes.loadTexturesFromAssetFile(45, 44, "options_back",
				"options_back.png", "textures/aboutScene/");
		FontRes.loadFont(512, 512,
				Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 35, true,
				Color.WHITE, "aboutFont");
		RegionRes.loadTexturesFromAssetFile(294, 92, "cent_yellow_btn",
				"yellow_btn.png", "textures/");
		FontRes.loadFont(512, 256,
				Typeface.createFromAsset(activity.getAssets(), "fonts/font.ttf"), 40f,
				true, Color.WHITE, "rewards_new");
	}

	public static void unloadAboutSceneResources() {
		RegionRes.getTextureRegion("options_back").getTexture().unload();
		FontRes.getFont("aboutFont").unload();
	}

	public static void load1010GameOverDialogTextures() {
		RegionRes.loadTexturesFromAssetFile(452, 259, "1010_over_little_bg",
				"1010_over_little_bg.png", "textures/1010overDialog/");
		RegionRes.loadTexturesFromAssetFile(94, 87, "big_medal",
				"big_medal.png", "textures/1010overDialog/");
		RegionRes.loadTexturesFromAssetFile(400, 115, "1010_over_icon",
				"1010_over_icon.png", "textures/1010overDialog/");
		RegionRes.loadTexturesFromAssetFile(212, 123, "1010_over_home",
				"1010_home.png", "textures/");
		RegionRes.loadTexturesFromAssetFile(212, 123, "1010_over_new",
				"1010_new.png", "textures/");
		RegionRes.loadTexturesFromAssetFile(452, 102, "1010_over_shop",
				"1010_shop.png", "textures/");
	}

	public static void unload1010GameOverDialogTextures() {
		RegionRes.getTextureRegion("1010_over_home").getTexture().unload();
		RegionRes.getTextureRegion("1010_over_new").getTexture().unload();
		RegionRes.getTextureRegion("1010_over_shop").getTexture().unload();
		RegionRes.getTextureRegion("big_medal").getTexture().unload();
//		RegionRes.getTextureRegion("1010_over_big_bg").getTexture().unload();
		RegionRes.getTextureRegion("1010_over_little_bg").getTexture().unload();
		RegionRes.getTextureRegion("1010_over_icon").getTexture().unload();
	}

	public static void load1010PauseDialogTextures() {
		RegionRes.loadTexturesFromAssetFile(212, 123, "1010_pause_home",
				"1010_home.png", "textures/");
		RegionRes.loadTexturesFromAssetFile(212, 123, "1010_pause_new",
				"1010_new.png", "textures/");
		RegionRes.loadTexturesFromAssetFile(212, 123, "1010_pause_resume",
				"1010_pause_resume.png", "textures/1010pauseDialog/");
		RegionRes.loadTexturesFromAssetFile(452, 102, "1010_pause_shop",
				"1010_shop.png", "textures/");
		RegionRes
				.loadTexturesFromAssets("textures/1010pauseDialog/1010_pause_audio.xml");
	}

	public static void unload1010PauseDialogTextures() {
		RegionRes.getTextureRegion("1010_pause_home").getTexture().unload();
		RegionRes.getTextureRegion("1010_pause_new").getTexture().unload();
		RegionRes.getTextureRegion("1010_pause_resume").getTexture().unload();
		RegionRes.getTextureRegion("1010_pause_shop").getTexture().unload();
		RegionRes.getRegion("1010_pause_audio").getTexture().unload();
	}

	public static void load1010ContinueDialogTextures() {
		RegionRes.loadTexturesFromAssetFile(504, 466, "1010_continue_bg",
				"1010_continue_bg.png", "textures/1010continueDialog/");
		RegionRes.loadTexturesFromAssetFile(43, 43, "1010_continue_cancel",
				"1010_continue_cancel.png");
		RegionRes.loadTexturesFromAssetFile(179, 50, "1010_continue_ok",
				"1010_continue_ok.png");
	}

	public static void unload1010ContinueDialogTextures() {
		RegionRes.getTextureRegion("1010_continue_bg").getTexture().unload();
		RegionRes.getTextureRegion("1010_continue_cancel").getTexture()
				.unload();
		RegionRes.getTextureRegion("1010_continue_ok").getTexture().unload();
	}

	public static void loadGuideTextures() {
		RegionRes.loadTexturesFromAssetFile(640, 960, "guide1", "guide1.png",
				"textures/guides/");
		RegionRes.loadTexturesFromAssetFile(640, 960, "guide2", "guide2.png",
				"textures/guides/");
		RegionRes.loadTexturesFromAssetFile(481, 121, "guide_border",
				"guide_border.png", "textures/guides/");
	}

	public static void unloadGuideTextures() {
		RegionRes.getTextureRegion("guide1").getTexture().unload();
		RegionRes.getTextureRegion("guide2").getTexture().unload();
		RegionRes.getTextureRegion("guide_border").getTexture().unload();
	}

	public static void loadOneBlockGuideTextures() {
		RegionRes.loadTexturesFromAssetFile(640, 960, "guide3", "guide3.png",
				"textures/guides/");
	}

	public static void unloadOneBlockGuideTextures() {
		RegionRes.getTextureRegion("guide3").getTexture().unload();
	}

	public static void loadGuideCompleteTextures() {
		RegionRes.loadTexturesFromAssetFile(385, 360, "guide_complete_bg",
				"guide_complete_bg.png", "textures/guideCompleteDialog/");
		RegionRes.loadTexturesFromAssetFile(154, 48, "guide_complete_btn",
				"guide_complete_btn.png", "textures/guideCompleteDialog/");
	}

	public static void unloadGuideCompleteTextures() {
		RegionRes.getTextureRegion("guide_complete_bg").getTexture().unload();
		RegionRes.getTextureRegion("guide_complete_btn").getTexture().unload();
	}
	
	/**
	 * 加载幸运星不够界面资源
	 */
	public static void loadLuckyStarNotEnoughDialog(Activity activity) {
		RegionRes.loadTexturesFromAssetFile(493, 100, "yellow_btn_long_ne",
				"yellow_btn_long.png", "textures/");
		RegionRes.loadTexturesFromAssetFile(579, 631, "common_bg_ne",
				"common_bg.png", "textures/dialogCent/");
		RegionRes.loadTexturesFromAssetFile(579, 631, "starCry_ne",
				"starCry.png", "textures/mainScene/");
		RegionRes.loadTexturesFromAssetFile(64, 64, "quit_ne",
				"options_quit.png", "textures/");
		FontRes.loadFont(512, 256,
				Typeface.createFromAsset(activity.getAssets(), "fonts/font.ttf"), 25f,
				true, Color.WHITE, "25white_ne");
		FontRes.loadFont(512, 256,
				Typeface.createFromAsset(activity.getAssets(), "fonts/font.ttf"), 40f,
				true, Color.WHITE, "40white_ne");
		
	}
	
	/**
	 * 释放幸运星不够界面资源
	 */
	public static void unloadLuckyStarNotEnoughDialog() {
		RegionRes.getTextureRegion("yellow_btn_long_ne").getTexture().unload();
		RegionRes.getTextureRegion("common_bg_ne").getTexture().unload();
		RegionRes.getTextureRegion("starCry_ne").getTexture().unload();
		RegionRes.getTextureRegion("quit_ne").getTexture().unload();
		FontRes.getFont("25white_ne").unload();
		FontRes.getFont("40white_ne").unload();
	}
	
	/**
	 * 加载奖励界面资源
	 * 
	 * @param activity
	 */
	public static void loadRewardsDialog(Activity activity) {
		RegionRes.loadTexturesFromAssetFile(493, 100, "yellow_btn_long",
				"yellow_btn_long.png", "textures/");
		RegionRes.loadTexturesFromAssetFile(579, 631, "common_bg",
				"common_bg.png", "textures/dialogCent/");
		FontRes.loadFont(512, 256,
				Typeface.createFromAsset(activity.getAssets(), "fonts/font.ttf"), 120f,
				true, Color.WHITE, "rewards");
		
		
	}
	
	
	/**
	 * 释放奖励界面资源
	 */
	public static void unloadRewardsDialog() {
		RegionRes.getTextureRegion("yellow_btn_long").getTexture().unload();
		RegionRes.getTextureRegion("common_bg").getTexture().unload();
		FontRes.getFont("rewards").unload();
		
	}
	
	/**
	 * 加载电信营销页面   add  by lvjibin
	 * 
	 * 
	 * @param activity
	 */
	public static void loadCTBuyDialog(Activity activity) {
		RegionRes.loadTexturesFromAssetFile(493, 100, "yellow_btn_long",
				"yellow_btn_long.png", "textures/");
		RegionRes.loadTexturesFromAssetFile(579, 631, "common_bg",
				"common_bg.png", "textures/dialogCent/");
		FontRes.loadFont(512, 256,
				Typeface.createFromAsset(activity.getAssets(), "fonts/font.ttf"), 120f,
				true, Color.WHITE, "rewards");
		RegionRes.loadTexturesFromAssetFile(64, 64, "quit",
				"options_quit.png", "textures/");
		
	}
	
	/**
	 * 释放电信营销页面资源
	 */
	public static void unloadCTBuysDialog() {
		RegionRes.getTextureRegion("yellow_btn_long").getTexture().unload();
		RegionRes.getTextureRegion("common_bg").getTexture().unload();
		FontRes.getFont("rewards").unload();
		RegionRes.getTextureRegion("quit").getTexture().unload();
	}

	public static void loadCentDialogTextures() {
		RegionRes.loadTexturesFromAssetFile(294, 92, "cent_yellow_btn",
				"yellow_btn.png", "textures/");
		RegionRes.loadTexturesFromAssetFile(64, 64, "cent_options_quit",
				"options_quit.png", "textures/");
		RegionRes.loadTexturesFromAssetFile(579, 631, "common_bg",
				"common_bg.png", "textures/dialogCent/");
		RegionRes.loadTexturesFromAssetFile(207, 185, "test_zeng_cdkey",
				"test_zeng_cdkey.png", "textures/dialogCent/");
		RegionRes.loadTexturesFromAssetFile(315, 59, "text_libao_cdkey",
				"text_libao_cdkey.png", "textures/dialogCent/");
		RegionRes.loadTexturesFromAssetFile(162, 70, "text_lingqu",
				"text_lingqu.png", "textures/dialogCent/");
		FontRes.loadFont(512, 512,
				Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 25, true,
				Color.WHITE, "centFont");
	}

	public static void unloadCentDialogTextures() {
		RegionRes.getTextureRegion("cent_yellow_btn").getTexture().unload();
		RegionRes.getTextureRegion("cent_options_quit").getTexture().unload();
		RegionRes.getTextureRegion("common_bg").getTexture().unload();
		RegionRes.getTextureRegion("test_zeng_cdkey").getTexture().unload();
		RegionRes.getTextureRegion("text_libao_cdkey").getTexture().unload();
		RegionRes.getTextureRegion("text_lingqu").getTexture().unload();
		FontRes.getFont("centFont").unload();
	}

	public static void unloadNewversionDownLoadTextures() {
		RegionRes.getTextureRegion("yellow_btn_new").getTexture().unload();
		RegionRes.getTextureRegion("common_bg_new").getTexture().unload();
		FontRes.getFont("rewards_new").unload();
		RegionRes.getTextureRegion("quit_new").getTexture().unload();
//		RegionRes.getTextureRegion("star").getTexture().unload();
		
	}
	public static void loadNewversionDownLoadTextures(Activity activity) {
		RegionRes.loadTexturesFromAssetFile(493, 100, "yellow_btn_new",
				"yellow_btn_long.png", "textures/");
		RegionRes.loadTexturesFromAssetFile(80, 80, "star", "star.png","textures/shopScene/");
		RegionRes.loadTexturesFromAssetFile(579, 631, "common_bg_new",
				"common_bg.png", "textures/dialogCent/");
		FontRes.loadFont(512, 256,
				Typeface.createFromAsset(activity.getAssets(), "fonts/font.ttf"), 40f,
				true, Color.WHITE, "rewards_new");
		RegionRes.loadTexturesFromAssetFile(64, 64, "quit_new",
				"options_quit.png", "textures/");
		
		
	}
}
