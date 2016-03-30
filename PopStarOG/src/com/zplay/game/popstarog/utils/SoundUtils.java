package com.zplay.game.popstarog.utils;

import com.e7studio.android.e7appsdk.utils.LogUtils;
import com.orange.audio.sound.Sound;
import com.orange.audio.sound.SoundFactory;
import com.orange.res.MusicRes;
import com.orange.res.SoundRes;

public class SoundUtils {

	private final static String TAG = "SoundUtils";

	public static void preLoad() {
		SoundFactory.setAssetBasePath("sounds/");
		SoundRes.loadSoundFromAssets("firework_1", "fireworks_01.mp3");
		SoundRes.loadSoundFromAssets("firework_2", "fireworks_02.mp3");
		SoundRes.loadSoundFromAssets("firework_3", "fireworks_03.mp3");
		SoundRes.loadSoundFromAssets("button", "button.mp3");
		SoundRes.loadSoundFromAssets("landing", "landing.mp3");
		SoundRes.loadSoundFromAssets("popstar", "pop_star.wav");
		SoundRes.loadSoundFromAssets("applause", "applause.mp3");
		SoundRes.loadSoundFromAssets("gameover", "gameover.mp3");
		SoundRes.loadSoundFromAssets("cheers", "cheers.mp3");
		SoundRes.loadSoundFromAssets("logo", "logo.mp3");
	}

	public static void preload1010Sounds() {
		new Thread(new Runnable() {
			public void run() {
				SoundFactory.setAssetBasePath("sounds/1010/");
				for (int i = 1; i <= 3; i++) {
					SoundRes.loadSoundFromAssets("1010_1_" + i, "1010_1_" + i
							+ ".mp3");
				}
				for (int i = 1; i <= 3; i++) {
					SoundRes.loadSoundFromAssets("1010_23_" + i, "1010_23_" + i
							+ ".mp3");
				}
				for (int i = 1; i <= 3; i++) {
					SoundRes.loadSoundFromAssets("1010_4_" + i, "1010_4_" + i
							+ ".mp3");
				}
				SoundRes.loadSoundFromAssets("1010_5", "1010_5.mp3");

				SoundRes.loadSoundFromAssets("1010_begin", "1010_begin.mp3");
				SoundRes.loadSoundFromAssets("1010_brick_refresh",
						"1010_brick_refresh.mp3");
				SoundRes.loadSoundFromAssets("1010_failure", "1010_failure.mp3");
				SoundRes.loadSoundFromAssets("1010_gameover",
						"1010_gameover.mp3");
				SoundRes.loadSoundFromAssets("1010_select", "1010_select.wav");
			}
		}).start();

	}

	public static void setVolumn(float volumn) {
		SoundRes.setVolume(volumn);
		MusicRes.setVolume(volumn);
	}

	public static void playStart() {
		SoundRes.playSound("logo");
	}

	public static void playFireWork(int number) {
		SoundRes.playSound("firework_" + number);
	}

	public static void playButtonClick() {
		SoundRes.playSound("button");
	}

	public static void playLanding() {
		SoundRes.playSound("landing");
	}

	public static void playPop() {
		playPop(1);
	}

	public static void playPop(float rate) {
		Sound sound = SoundRes.getSound("popstar");
		if (sound != null) {
			sound.setRate(rate);
			sound.play();
		} else {
			LogUtils.v(TAG, "playPop error, sound is null...");
		}
	}

	public static void playStageClear() {
		SoundRes.playSound("button");
	}

	public static void playCheers() {
		SoundRes.playSound("cheers");
	}

	public static void playApplause() {
		SoundRes.playSound("applause");
	}

	public static void playGameOver() {
		SoundRes.playSound("gameover");
	}

	public static void play1010Begin() {
		SoundRes.playSound("1010_begin");
	}

	public static void play1010Refresh() {
		SoundRes.playSound("1010_brick_refresh");
	}

	public static void play1010PutFail() {
		SoundRes.playSound("1010_failure");
	}

	public static void play1010GameOver() {
		SoundRes.playSound("1010_gameover");
	}

	public static void play1010Drop() {
		SoundRes.playSound("1010_select");
	}

	public static void play1010(int rowAndColumnNums) {
		switch (rowAndColumnNums) {
		case 1:
			SoundRes.playSound("1010_1_" + MathUtils.getRandom(1, 3));
			break;
		case 2:
		case 3:
			SoundRes.playSound("1010_23_" + MathUtils.getRandom(1, 3));
			break;
		case 4:
			SoundRes.playSound("1010_4_" + MathUtils.getRandom(1, 3));
			break;
		case 5:
			SoundRes.playSound("1010_5");
			break;
		default:
		}
	}
}
