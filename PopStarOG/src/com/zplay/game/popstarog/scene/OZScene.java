package com.zplay.game.popstarog.scene;

import com.e7studio.android.e7appsdk.utils.LogUtils;
import com.orange.content.SceneBundle;
import com.orange.engine.handler.IUpdateHandler;
import com.orange.engine.handler.timer.ITimerCallback;
import com.orange.engine.handler.timer.TimerHandler;
import com.orange.entity.sprite.Sprite;
import com.orange.entity.text.Text;
import com.orange.entity.util.FPSLogger;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.util.HorizontalAlign;
import com.orange.util.color.Color;
import com.zplay.game.popstarog.PopStar;
import com.zplay.game.popstarog.custom.CustomBaseScene;
import com.zplay.game.popstarog.layer.OZGameLayer;
import com.zplay.game.popstarog.layer.OZMenuLayer;
import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.utils.ResourceManager;
import com.zplay.game.popstarog.utils.SpriteMaker;
import com.zplay.game.popstarog.utils.TextMaker;

/**
 * 1010模式
 * 
 * @author glzlaohuai
 * @version 2014-6-25
 */
public class OZScene extends CustomBaseScene {

	private final static String TAG = "OZScene";

	private OZMenuLayer menuLayer;
	private OZGameLayer gameLayer;

	private boolean isGameOn = false;
	private boolean isInGuideState = false;
	private boolean isInAnmite = false;
	private VertexBufferObjectManager vertexBufferObjectManager;

	@Override
	public void onSceneCreate(SceneBundle bundle) {
		super.onSceneCreate(bundle);
		setSize(GameConstants.BASE_WIDTH, GameConstants.BASE_HEIGHT);
		LogUtils.v(TAG, "onSceneCreate...");
		vertexBufferObjectManager = getVertexBufferObjectManager();
		addBg();
		gameLayer = new OZGameLayer(this);
		menuLayer = new OZMenuLayer(this);
		attachChild(menuLayer);
		attachChild(gameLayer);

//		addFPSText();

		registerUpdateHandler(new IUpdateHandler() {
			public void reset() {
			}

			public void onUpdate(float pSecondsElapsed) {
				gameLayer.doUpdate(pSecondsElapsed);
			}
		});
		gameLayer.hideAndDisableAllEvents();
	}

	private void addFPSText() {
		 final Text fpsText = TextMaker.make("12345123", "30white", 640, 20,
		 HorizontalAlign.CENTER, vertexBufferObjectManager);
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
		ResourceManager.unload1010SceneTextures();
	}

	public void setGameOn(boolean isInNewMode) {
		this.isGameOn = isInNewMode;
	}

	public boolean isGameOn() {
		return isGameOn;
	}

	public void setInGuideState(boolean isInGuideState) {
		this.isInGuideState = isInGuideState;
	}
	public void setisInAnminte(boolean isInAnmite) {
		this.isInAnmite = isInAnmite;
	}

	@Override
	public void onSceneResume() {
		super.onSceneResume();
		LogUtils.v(TAG, "onSceneResume...");
		gameLayer.onResume();
		menuLayer.onResume();
		if (isGameOn) {
			((PopStar) getActivity()).showBanner();
		}
	}

	@Override
	public void onScenePause() {
		super.onScenePause();
		LogUtils.v(TAG, "onScenePause...");
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

	public void onBackKeyDown() {
		// 处于教程引导模式下，不处理返回事件
		if (!isInGuideState&&!isInAnmite) {
			if (isGameOn) {
				showMenu();
			} else {
				finish();
				dispose();
			}
		}
	}

	public void showMenu() {
		if (isGameOn) {
			if (!gameLayer.isInResultState()) {
				((PopStar) getActivity()).hideBanner();
				isGameOn = false;
				gameLayer.showMainMenu();
				menuLayer.showMainMenu();
			}
		}
	}

	public void doGameStuff(boolean flag) {
		gameLayer.doGameStuff(flag);
	}

	@Override
	public void onSceneResult(int requestCode, int resultCode, SceneBundle data) {
		super.onSceneResult(requestCode, resultCode, data);
		if (resultCode == 30) {
			gameLayer.buyback();
		}
	}
	
}
