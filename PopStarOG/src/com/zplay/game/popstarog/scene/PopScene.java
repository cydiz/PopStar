package com.zplay.game.popstarog.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;

import com.e7studio.android.e7appsdk.utils.LogUtils;
import com.orange.content.SceneBundle;
import com.orange.engine.handler.IUpdateHandler;
import com.orange.engine.handler.timer.ITimerCallback;
import com.orange.engine.handler.timer.TimerHandler;
import com.orange.entity.IEntity;
import com.orange.entity.modifier.DelayModifier;
import com.orange.entity.modifier.IEntityModifier.IEntityModifierListener;
import com.orange.entity.sprite.ButtonSprite;
import com.orange.entity.sprite.ButtonSprite.OnClickListener;
import com.orange.entity.sprite.Sprite;
import com.orange.entity.text.Text;
import com.orange.entity.util.FPSLogger;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.util.HorizontalAlign;
import com.orange.util.color.Color;
import com.orange.util.modifier.IModifier;
import com.tendcloud.tenddata.TCAgent;
import com.zplay.game.popstarog.PopStar;
import com.zplay.game.popstarog.custom.CustomBaseScene;
import com.zplay.game.popstarog.custom.Dialog;
import com.zplay.game.popstarog.custom.DialogDismissListener;
import com.zplay.game.popstarog.layer.PopGameLayer;
import com.zplay.game.popstarog.layer.PopMenuLayer;
import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.particle.FireworkParticle;
import com.zplay.game.popstarog.particle.FireworkParticleMaker;
import com.zplay.game.popstarog.utils.AnnouncementShower;
import com.zplay.game.popstarog.utils.ButtonMaker;
import com.zplay.game.popstarog.utils.MathUtils;
import com.zplay.game.popstarog.utils.ResourceManager;
import com.zplay.game.popstarog.utils.SPUtils;
import com.zplay.game.popstarog.utils.SoundUtils;
import com.zplay.game.popstarog.utils.SpriteMaker;
import com.zplay.game.popstarog.utils.TextMaker;

/**
 * 主菜单场景
 * 
 * @author glzlaohuai
 * @version 2014-6-25
 */
public class PopScene extends CustomBaseScene {

	// 从OptionsScene返回MainScene之后，如果resultCode是10，表明是点击的主菜单按钮
	public final static int GO_MAIN_MENU = 10;

	private final static String TAG = "PopScene";
	private Random random = new Random();

	private PopMenuLayer menuLayer;
	private PopGameLayer gameLayer;

	private boolean isGameOn = false;

	private VertexBufferObjectManager vertexBufferObjectManager;

	private final static int MAX_FIREWORKS = 20;

	private List<FireworkParticle> fireworkParticleList = new ArrayList<FireworkParticle>();

	@Override
	public void onSceneCreate(SceneBundle bundle) {
		super.onSceneCreate(bundle);
		setSize(GameConstants.BASE_WIDTH, GameConstants.BASE_HEIGHT);
		LogUtils.v(TAG, "onSceneCreate...");
		vertexBufferObjectManager = getVertexBufferObjectManager();
		addBg();
		showFireworks(1, 8);
		gameLayer = new PopGameLayer(this);
		menuLayer = new PopMenuLayer(this);
		attachChild(menuLayer);
		attachChild(gameLayer);
		
		SoundUtils.playStart();
//		addFPSText();
		registerUpdateHandler(new IUpdateHandler() {
			public void reset() {
			}

			public void onUpdate(float pSecondsElapsed) {
				updateFireworkParticleList(pSecondsElapsed);
			}
		});
		AnnouncementShower.showAnnouncement(getActivity(), PopScene.this,
				AnnouncementShower.GAME);
	}

	private void updateFireworkParticleList(float delta) {
		for (int i = 0; i < fireworkParticleList.size(); i++) {
			FireworkParticle particle = fireworkParticleList.get(i);
			particle.addDelta(delta);
			if (particle.isExpire()) {
				particle.detachSelf();
				particle.dispose();
				fireworkParticleList.remove(i);
				i--;
			}
		}
	}

	private void addFPSText() {
		 final Text fpsText = TextMaker.make("12345123", "30white", 640, 20,
		 HorizontalAlign.RIGHT, vertexBufferObjectManager);
		 attachChild(fpsText);
		 fpsText.setColor(Color.YELLOW);
		 final FPSLogger fpsLogger = new FPSLogger();
		 registerUpdateHandler(fpsLogger);
		 registerUpdateHandler(new TimerHandler(1 / 20.0f, true,
		 new ITimerCallback() {
		 public void onTimePassed(TimerHandler pTimerHandler) {
		 fpsText.setText(String.valueOf(fpsLogger.getFPS()));
		 }
		 }));
	}

	@Override
	public void onSceneDestroy() {
		super.onSceneDestroy();
	}

	// public void showShop() {
	// LogUtils.v(TAG, "跳转到商城...");
	// Director.getInstance().pushScene(
	// TopPushInTransition.make(0.5f, new ShopScene(this)));
	// }

	// gameLayer在进行消除动画之前就已经计算出了当前分数，调用该方法通知menuLayer中记录的用于记录用的savedHighScore的值是否需要进行重新设定
	public void notifyCurrentScore(int score) {
		menuLayer.receiveCurrentScore(score);
	}

	public void setGameOn(boolean isGameOn) {
		this.isGameOn = isGameOn;
	}

	public boolean isGameOn() {
		return isGameOn;
	}

	@Override
	public void onSceneResume() {
		super.onSceneResume();
		LogUtils.v(TAG, "onSceneResume...");
		menuLayer.onResume();
		gameLayer.onResume();
		if (isGameOn) {
			((PopStar) getActivity()).showBanner();
		}
	}

	@Override
	public void onScenePause() {
		super.onScenePause();
		LogUtils.v(TAG, "onScenePause...");
		menuLayer.onPause();
		gameLayer.onPause();

		((PopStar) getActivity()).hideBanner();
	}

	// 添加背景
	private void addBg() {
		Sprite sprite = SpriteMaker.makeSpriteWithSingleImageFile("bg",
				vertexBufferObjectManager);
		sprite.setPosition(0, 0);
		attachChild(sprite);
	}

	// 烟花
	public void showFireworks(float delay, int fireNums) {
		fireNums = Math.min(fireNums, MAX_FIREWORKS);
		for (int i = 0; i < fireNums; i++) {
			float delayTimeExtra = 0;
			if (i != 0) {
				delayTimeExtra = MathUtils.getValue(random, 0, 50000) * 0.0001f;
			}
			delayTimeExtra += delay;
			DelayModifier delayModifier = new DelayModifier(delayTimeExtra,
					new IEntityModifierListener() {
						public void onModifierStarted(
								IModifier<IEntity> pModifier, IEntity pItem) {
						}

						public void onModifierFinished(
								IModifier<IEntity> pModifier, IEntity pItem) {
							doShowFireworks();
						}
					});
			registerEntityModifier(delayModifier);
		}
	}

	public void doShowFireworks() {
		int sound = MathUtils.getValue(random, 1, 3);
		int type = MathUtils.getValue(random, 0, 4);
		int x = MathUtils.getValue(random, 80, 560);
		int y = MathUtils.getValue(random, 200, 440);
		FireworkParticle particle = FireworkParticleMaker.make(
				getVertexBufferObjectManager(), x, y, type);
		particle.setPosition(x, y);
		attachChild(particle, 1);
		fireworkParticleList.add(particle);
		particle = null;
		SoundUtils.playFireWork(sound);
	}

	public void onBackKeyDown() {
		if (isGameOn) {
			showMenu();
		} else {
			showExitConfirmDialog();
		}
	}

	public void showMenu() {
		if (isGameOn) {
			if (!gameLayer.isInResultState()) {
				// TODO 从游戏界面返回到初始界面
				if (!gameLayer.isInHammerState()) {
					gameLayer.showMainMenu();
					menuLayer.showMainMenu();
					isGameOn = false;
				} else {
					gameLayer.showMainMenu();
				}
			}
		}
	}

	//
	public void doNewGameStuff() {
		gameLayer.doNewGameStuff();
	}

	public void doContinueGameStuff() {
		gameLayer.doContinueGameStuff();
	}

	public void clearSavedGameData() {
		gameLayer.clearStageAndScoreData();
	}

	private void showExitConfirmDialog() {
//		dialog添加资源加载和释放方法,避免部分内存小的机型回收资源造成部分图片不显示问题
		LogUtils.v(TAG, "showExitConfirmDialog...");
		ResourceManager.loadIphoneDialogTextures();
		final Dialog dialog = new Dialog(this);
		dialog.setSize(640, 960);
		Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile("alert_bg",
				vertexBufferObjectManager);
		bgSprite.setCentrePosition(320, 480);
		dialog.attachChild(bgSprite);

		Text titleLabel = TextMaker.make("退出游戏!", "systemFont40", 320, 400,
				HorizontalAlign.CENTER, vertexBufferObjectManager);
		dialog.attachChild(titleLabel);

		Text contentLabel = TextMaker.make("你是否想退出游戏?", "systemFont30", 320,
				462, HorizontalAlign.CENTER, vertexBufferObjectManager);
		dialog.attachChild(contentLabel);

		ButtonSprite okBtn = ButtonMaker.makeFromSingleImgFile(190, 552,
				"alert_ok", vertexBufferObjectManager);
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// quit
				dialog.dismissWithoutAnimation();
				detachSelf();
				getActivity().finish();
//				System.exit(0);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
		Text okLabel = TextMaker.make("确定", "systemFont30", 190, 552,
				HorizontalAlign.CENTER, vertexBufferObjectManager);

		dialog.attachChild(okBtn);
		dialog.attachChild(okLabel);

		ButtonSprite cancelBtn = ButtonMaker.makeFromSingleImgFile(458, 552,
				"alert_cancel", vertexBufferObjectManager);
		cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				dialog.dismissWithAnimamtion();
			}
		});
		Text cancelLabel = TextMaker.make("取消", "systemFont30", 458, 552,
				HorizontalAlign.CENTER, vertexBufferObjectManager);
		dialog.attachChild(cancelBtn);
		dialog.attachChild(cancelLabel);
		dialog.showWithAnimation();
		dialog.setDialogDismissListener(new DialogDismissListener() {
			public void onDialogDismiss() {
				ResourceManager.unloadIphoneDialogTextures();
			}
		});
	}

	@Override
	public void onSceneResult(int requestCode, int resultCode, SceneBundle data) {
		super.onSceneResult(requestCode, resultCode, data);
		if (resultCode == GO_MAIN_MENU) {
			showMenu();
		}else if(resultCode == 20){
			gameLayer.buyback();
		}
	}

	// 菜单对话框
	public void showOptions() {
//		添加加载释放资源方法，避免部分机型因内存小的问题造成资源回收，图片显示缺失
		final Context context = PopScene.this.getActivity();
		ResourceManager.loadOptionsDialogTextures();
		final Dialog dialog = new Dialog(this);
		dialog.setSize(640, 960);
		Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"options_bg", vertexBufferObjectManager);
		bgSprite.setPosition(79, 178);
		dialog.attachChild(bgSprite);

		ButtonSprite goMainMenuBtn = ButtonMaker.makeFromSingleImgFile(320,
				409, "options_mainmenu", vertexBufferObjectManager);
		goMainMenuBtn.setScale(0.8f);
		goMainMenuBtn.setCentrePosition(320, 425);
		goMainMenuBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SoundUtils.playButtonClick();
				dialog.dismiss();
				showMenu();
			}
		});
		dialog.attachChild(goMainMenuBtn);

		ButtonSprite goShopBtn = ButtonMaker.makeFromSingleImgFile(320, 571,
				"btn_shop", vertexBufferObjectManager);
		goShopBtn.setScale(0.8f);
		goShopBtn.setCentrePosition(320, 564);
		goShopBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				dialog.dismiss();
				SoundUtils.playButtonClick();
				startScene(ShopScene.class);
			}
		});
		dialog.attachChild(goShopBtn);

		ButtonSprite quitBtn = ButtonMaker.makeFromSingleImgFile(604, 0,
				"options_back", vertexBufferObjectManager);
		quitBtn.setPosition(498, 201);
		quitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SoundUtils.playButtonClick();
				dialog.dismiss();
			}
		});
		dialog.attachChild(quitBtn);

		final ButtonSprite audioSwitchBtn = ButtonMaker.makeFromTPFile(320,
				490, "audio_switch", vertexBufferObjectManager);
		audioSwitchBtn.setScale(0.8f);
		audioSwitchBtn.setCentrePosition(320, 498);
		audioSwitchBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SPUtils.toggleAudio(context);
				if (SPUtils.isAudioOpen(context)) {
					TCAgent.onEvent(context, "打开音效");
					audioSwitchBtn.setCurrentTileIndex(0);
					SoundUtils.setVolumn(1);
					SoundUtils.playButtonClick();
				} else {
					TCAgent.onEvent(context, "关闭音效");
					audioSwitchBtn.setCurrentTileIndex(1);
					SoundUtils.setVolumn(0);
				}
			}
		});
		audioSwitchBtn
				.setCurrentTileIndex(SPUtils.isAudioOpen(context) ? 0 : 1);
		dialog.attachChild(audioSwitchBtn);

		Sprite starNumSprite = (Sprite) SpriteMaker
				.makeSpriteWithSingleImageFile("lucky_star",
						vertexBufferObjectManager);
		starNumSprite.setScale(0.87f);
		starNumSprite.setPosition(225, 618);
		dialog.attachChild(starNumSprite);

		long starNum = SPUtils.getLuckStarNum(context);
		LogUtils.v(TAG, "幸运星：" + starNum);
		Text starNumLabel = TextMaker.make("× 0000000000", "40white", 320, 665,
				HorizontalAlign.LEFT, vertexBufferObjectManager);
		starNumLabel.setText("× " + starNum);
		starNumLabel.setPositionX(310);
		dialog.attachChild(starNumLabel);

		dialog.setDialogDismissListener(new DialogDismissListener() {
			public void onDialogDismiss() {
				ResourceManager.unloadOptionsDialogTextures();
				((PopStar) getActivity()).showBanner();
			}
		});

		dialog.show();
	}

}
