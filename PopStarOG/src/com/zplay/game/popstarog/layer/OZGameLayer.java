package com.zplay.game.popstarog.layer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;
import android.widget.Toast;

import com.e7studio.android.e7appsdk.utils.LogUtils;
import com.orange.content.SceneBundle;
import com.orange.entity.Entity;
import com.orange.entity.IEntity;
import com.orange.entity.layer.Layer;
import com.orange.entity.modifier.DelayModifier;
import com.orange.entity.modifier.FadeInModifier;
import com.orange.entity.modifier.FadeOutModifier;
import com.orange.entity.modifier.IEntityModifier.IEntityModifierListener;
import com.orange.entity.modifier.LoopEntityModifier;
import com.orange.entity.modifier.MoveByModifier;
import com.orange.entity.modifier.MoveModifier;
import com.orange.entity.modifier.ParallelEntityModifier;
import com.orange.entity.modifier.RotationByModifier;
import com.orange.entity.modifier.ScaleModifier;
import com.orange.entity.modifier.SequenceEntityModifier;
import com.orange.entity.scene.Scene;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.entity.sprite.ButtonSprite;
import com.orange.entity.sprite.ButtonSprite.OnClickListener;
import com.orange.entity.sprite.Sprite;
import com.orange.entity.text.Text;
import com.orange.input.touch.TouchEvent;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.util.HorizontalAlign;
import com.orange.util.adt.list.SmartList;
import com.orange.util.color.Color;
import com.orange.util.modifier.IModifier;
import com.orange.util.modifier.IModifier.IModifierListener;
import com.orange.util.modifier.ease.EaseBackIn;
import com.orange.util.modifier.ease.EaseExponentialOut;
import com.orange.util.size.Size;
import com.tendcloud.tenddata.TCAgent;
import com.zplay.game.popstarog.PopStar;
import com.zplay.game.popstarog.custom.Dialog;
import com.zplay.game.popstarog.custom.DialogDismissListener;
import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.ozshape.OZShape;
import com.zplay.game.popstarog.ozshape.OZShape1s;
import com.zplay.game.popstarog.ozshape.OZShapeBuildAndIniter;
import com.zplay.game.popstarog.ozshape.OZShapeIDGenerator;
import com.zplay.game.popstarog.ozshape.OZShapeMoveListener;
import com.zplay.game.popstarog.particle.OZExplodeParticleMaker;
import com.zplay.game.popstarog.particle.OZParticle;
import com.zplay.game.popstarog.particle.OZParticleMaker;
import com.zplay.game.popstarog.particle.PopParticle;
import com.zplay.game.popstarog.particle.PopParticleMaker;
import com.zplay.game.popstarog.pay.PayCallback;
import com.zplay.game.popstarog.scene.OZScene;
import com.zplay.game.popstarog.scene.ShopScene;
import com.zplay.game.popstarog.utils.BlinkModifierMaker;
import com.zplay.game.popstarog.utils.ButtonMaker;
import com.zplay.game.popstarog.utils.DataProtecterUtils;
import com.zplay.game.popstarog.utils.FullHandler;
import com.zplay.game.popstarog.utils.IUpdater;
import com.zplay.game.popstarog.utils.IntegerContainer;
import com.zplay.game.popstarog.utils.NineHandler;
import com.zplay.game.popstarog.utils.OZEarnedScoreCalculator;
import com.zplay.game.popstarog.utils.OZExister;
import com.zplay.game.popstarog.utils.OZExploader;
import com.zplay.game.popstarog.utils.OZGroupBlockSizeCalculator;
import com.zplay.game.popstarog.utils.ResourceManager;
import com.zplay.game.popstarog.utils.SPUtils;
import com.zplay.game.popstarog.utils.SaveGameData;
import com.zplay.game.popstarog.utils.SizeHelper;
import com.zplay.game.popstarog.utils.SoundUtils;
import com.zplay.game.popstarog.utils.SpriteMaker;
import com.zplay.game.popstarog.utils.TextMaker;

@SuppressLint({ "UseSparseArrays", "DefaultLocale" })
public class OZGameLayer extends Layer implements IUpdater {

	private final static String TAG = "OZGameLayer";
	private OZScene mainScene;

	private final static int QUICK_BUY_NUM = 118;
	private BUY_TYPE buyType;
	
	private Dialog buyDialog;

	private enum BUY_TYPE {
		REFRESH, CONTINUE, ONE_BLOCK, NONE, QUICK_FORCE_SHOW
	};

	private final static Color REFRESH_ENABLE_COLOR = new Color(249.0f / 255,
			238.0f / 255, 48.0f / 255);
	private Activity activity;
	private VertexBufferObjectManager vertexBufferObjectManager;

	// 暂停，购买幸运星
	private ButtonSprite pauseBtn;
	private ButtonSprite luckyStarBtn;

	List<Entity> fadeInList = new ArrayList<Entity>();

	// 分数,幸运星数量
	private Text currentScoreLabel;
	private Text highScoreLabel;
	private Text luckyStarLabel;

	// 刷新
	private ButtonSprite refreshBtn;
	// 小点点道具
	private OZShape1s oneBlockShape;

	private Sprite[][] containerSprites = new Sprite[10][10];
	private AnimatedSprite[][] starSprites = new AnimatedSprite[10][10];

	// 代表星星的状态
	private int[][] starSigns = new int[10][10];

	// 组合的标号
	private int[] shapeSigns;

	// int
	private String savedScore = DataProtecterUtils.intToDesString(0);
	// float
	private String visualScore = DataProtecterUtils.floatToDesString(0);
	private int lastLength = 0;

	private boolean isInResultState = false;

	// 需要展示的星星的数量，用于在所有星星展示完动画之后进行下一步的动作--底部的‘组合方块’进行展示
	private int shouldDisplayedStarsNum = 0;

	private Size groupSize;

	// 亦即横向边距
	private float containerBlockStartX;

	private List<OZShape> addedShapeList = new ArrayList<OZShape>();

	private OZShape handledShape;

	// 放置10次之后显示引导界面
	private final static int SHOW_GUIDE_PLACE_NUM_THREADHOLD = 10;
	private boolean isGuideShowed = false;
	// 该局游戏形状放置次数
	private int placeNum = 0;
	private boolean isInRefreshGuide = false;
	private boolean isInOneBlockGuideState = false;

	private Sprite guide1Sprite;
	private Sprite guide2Sprite;
	private Sprite guide22Sprite;
	private Sprite oneBlockGuideSprite;

	private boolean isOneBlockGuideShowed = false;

	private Text costTips;
	
	// 新增天气道具 add by liufengqiang
	private int weatherProgress = 0;
	private List<Sprite> pointLists;
	
	private ButtonSprite flashAim;
	private Sprite weatherContainer;
	private Sprite blackFlashProp;
	private Sprite whiteFlashProp;
	private Sprite cloudProp;
	private Sprite rain_drop;
	
	private boolean isPlayGame = false;      //游戏是否继续标记
	private boolean flashCanBeMoved = false; //标记闪电是否可以move
	private boolean cloudCanBeMoved = false; //标记云雨是否可以move
	private boolean isChangeProgress = false;
	private boolean isContinueGame = false;
	
	private boolean isChangeScoreAnim = true; //分数状态标志
	private Sprite beyond;
	private List<PopParticle> particleList = new ArrayList<PopParticle>();

	public OZGameLayer(Scene pScene) {
		super(pScene);
		setIgnoreTouch(false);
		vertexBufferObjectManager = getVertexBufferObjectManager();
		this.mainScene = (OZScene) pScene;
		activity = getActivity();
		buildUI();
		setComponentValue();
	}

	// 如果分数不为0的话,从sp文件中读取starSigns数组
	private void loadStarSigns() {
		LogUtils.v(TAG, "生成标示每一个位置的星星类型的数组...");
		if (SPUtils.get1010CurrentScore(activity) != 0) {
			shouldDisplayedStarsNum = 0;
			LogUtils.v(TAG, "当前分数不为0，说明sp文件中有保存数据，从sp文件中读取...");
			String starData = SPUtils.get1010Stars(activity);
			String[] columnsData = starData.split(";");
			for (int i = 0; i < columnsData.length; i++) {
				String[] rowsData = columnsData[i].split(",");
				for (int j = 0; j < rowsData.length; j++) {
					int starSign = Integer.parseInt(rowsData[j]);
					if (starSign != GameConstants.BLOCK_NONE) {
						shouldDisplayedStarsNum++;
					}
					starSigns[i][j] = starSign;
				}
			}
			shapeSigns = SPUtils.get1010GroupSignsWithFormat(activity);
			addedShapeList = OZShapeBuildAndIniter.buildAndInitOZShape(
					shapeSigns, getVertexBufferObjectManager());
			LogUtils.v(TAG,
					"读取的组合形状标号数组为：" + SPUtils.get1010GroupSigns(activity));
		} else {
			LogUtils.v(TAG,
					"当前分数为0，那么说明是重新开始的新游戏，那么自动生成一个代表星星全部是‘空’的数组以及生成一个组合形状的标号数组...");
			shouldDisplayedStarsNum = 0;
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					starSigns[i][j] = GameConstants.BLOCK_NONE;
				}
			}
			shapeSigns = OZShapeIDGenerator.generateOZShapeID(starSigns);
			addedShapeList = OZShapeBuildAndIniter.buildAndInitOZShape(
					shapeSigns, vertexBufferObjectManager);
		}
	}

	public boolean isInResultState() {
		return isInResultState;
	}

	private void disableAllBtns() {
		LogUtils.v(TAG, "禁用所有的事件...");
		pauseBtn.setEnabled(false);
		pauseBtn.setIgnoreTouch(true);
		pauseBtn.clearEntityModifiers();

		luckyStarBtn.setEnabled(false);
		luckyStarBtn.setIgnoreTouch(true);
		luckyStarBtn.clearEntityModifiers();

		refreshBtn.setEnabled(false);
		refreshBtn.setIgnoreTouch(true);
		refreshBtn.clearEntityModifiers();
		
		setIgnoreTouch(true);
	}
	private void restart_over() {
		isDied = false;
		SPUtils.reset1010GameData(activity);
		removeAllAddedShape();
		isChangeScoreAnim = true;
		doGameStuff(false);
	}
	private void restart() {
		isDied = false;
		SPUtils.reset1010GameData(activity);
		removeAllAddedShape();
		doGameStuff_restart();
		isChangeScoreAnim = true;
		System.out.println("重新开始游戏");
		weatherProgress = 0;
		isChangeProgress = true;
	}

	private void removeAllAddedShape() {
		for (OZShape shape : addedShapeList) {
			shape.detachSelf();
		}
		addedShapeList.clear();
	}

	private void clearAllModifiers() {
		SmartList<IEntity> children = getChildren();
		for (IEntity entity : children) {
			entity.clearEntityModifiers();
		}
	}

	private void removeCostTips() {
		if (costTips != null && !costTips.isDisposed()) {
			costTips.detachSelf();
			costTips.dispose();
			costTips = null;
		}
	}

	// 回到主菜单   回到之前的页面
	public void showMainMenu() {
		isPlayGame = false;
		SPUtils.setPointProgress(activity, weatherProgress);
		handledShape = null;
		clearAllModifiers();
		removeCostTips();
		disableAllBtns();
		removeAllAddedShape();
		saveGameData();
		for (int i = 0; i < fadeInList.size(); i++) {
			Entity entity = fadeInList.get(i);
			entity.setAlpha(0);
		}
		for (Sprite sprite : pointLists) {
			sprite.setAlpha(0);
		}
//		weatherBtn.setAlpha(0);
		for (int i = 0; i < 10; i++) {
			if (!mainScene.isGameOn()) {
				for (int j = 0; j < 10; j++) {
					if (!mainScene.isGameOn()) {
						containerSprites[i][j].clearEntityModifiers();
						containerSprites[i][j].setVisible(false);
						starSprites[i][j].clearEntityModifiers();
						starSprites[i][j].setVisible(false);
					} else {
						break;
					}
				}
			} else {
				break;
			}
		}
		if (oneBlockShape != null) {
			oneBlockShape.detachSelf();
			oneBlockShape = null;
		}
	}

	public void setInResultState(boolean isInResultState) {
		this.isInResultState = isInResultState;
	}

	public void doGameStuff(boolean flag) {
		if (!flag) {
			isChangeScoreAnim = true;
		}
		isPlayGame = true;
		setAlpha(1);
		isGuideShowed = SPUtils.isGuideShowed(activity);
		isOneBlockGuideShowed = SPUtils.isOneBlockGuideShowed(activity);
		isInRefreshGuide = false;
		isInOneBlockGuideState = false;
		mainScene.setInGuideState(false);
		setComponentValue();
		initComponentState();
		
		weatherProgress = SPUtils.getPointProgress(activity);
		if (weatherProgress < 7) {
			blackFlashProp.setVisible(true);
			cloudProp.setVisible(false);
			whiteFlashProp.setVisible(false);
		} else if (weatherProgress < 12) {
			blackFlashProp.setVisible(false);
			cloudProp.setVisible(true);
			whiteFlashProp.setVisible(false);
		} else {
			blackFlashProp.setVisible(false);
			cloudProp.setVisible(false);
			whiteFlashProp.setVisible(true);
			attachChild(flashAim);
		}
		isContinueGame = true;
		showAndRefreshPointProgress();
		isContinueGame = false;
		
		// 获取星星状态数组
		loadStarSigns();
		FadeInModifier fadeIn = new FadeInModifier(1.5f);
		for (int i = 0; i < fadeInList.size(); i++) {
			Entity entity = fadeInList.get(i);
			entity.clearEntityModifiers();
			entity.registerEntityModifier(fadeIn.deepCopy());
		}
		displayContainerAndStarsAndAfterThatEnableAllEvents();
		((PopStar) getActivity()).showBanner();
		addOneBlockItem();   
	}
	/**
	 * 这个方法的添加是为了在点击暂停里的刷新时不会产生新的小点点道具
    */
	public void doGameStuff_restart() {
		setAlpha(1);
		isGuideShowed = SPUtils.isGuideShowed(activity);
		isOneBlockGuideShowed = SPUtils.isOneBlockGuideShowed(activity);
		isInRefreshGuide = false;
		isInOneBlockGuideState = false;
		mainScene.setInGuideState(false);
		setComponentValue();
		initComponentState();
		// 获取星星状态数组
		loadStarSigns();
		weatherProgress = SPUtils.getPointProgress(activity);
		if (weatherProgress < 7) {
			blackFlashProp.setVisible(true);
			cloudProp.setVisible(false);
			whiteFlashProp.setVisible(false);
		} else if (weatherProgress < 12) {
			blackFlashProp.setVisible(false);
			cloudProp.setVisible(true);
			whiteFlashProp.setVisible(false);
		} else {
			blackFlashProp.setVisible(false);
			cloudProp.setVisible(false);
			whiteFlashProp.setVisible(true);
			attachChild(flashAim);
		}
		FadeInModifier fadeIn = new FadeInModifier(1.5f);
		for (int i = 0; i < fadeInList.size(); i++) {
			Entity entity = fadeInList.get(i);
			entity.clearEntityModifiers();
			entity.registerEntityModifier(fadeIn.deepCopy());
		}
		displayContainerAndStarsAndAfterThatEnableAllEvents();
		((PopStar) getActivity()).showBanner();
	}

	/**
	 * 添加小点点道具
	 */
	private void addOneBlockItem() {
		// 不消耗道具
		if (isInOneBlockGuideState) {
		}
		oneBlockShape = OZShapeBuildAndIniter
				.buildAndInitOneBlockShape(vertexBufferObjectManager);
		oneBlockShape.attach(this);
		oneBlockShape.showOut();
	}

	private void initComponentState() {
		LogUtils.v(TAG, "初始化所有精灵的状态为初始状态...");
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				containerSprites[i][j].setScale(0);
				containerSprites[i][j].setAlpha(1);
				starSprites[i][j].setScale(0);
				starSprites[i][j].setAlpha(1);
				containerSprites[i][j].setVisible(true);
				starSprites[i][j].setVisible(true);
			}
		}
		refreshBtn.setColor(REFRESH_ENABLE_COLOR);
		refreshBtn.setScale(1);
		refreshBtn.setEnabled(true);
		refreshBtn.setIgnoreTouch(false);
	}

	// 设置组件显示的内容
	private void setComponentValue() {
		luckyStarLabel
				.setText(String.valueOf(SPUtils.getLuckStarNum(activity)));
		luckyStarLabel.setCentrePositionX(luckyStarBtn.getCentreX()+10);

		savedScore = DataProtecterUtils.longToDesString(SPUtils
				.get1010CurrentScore(activity));
		visualScore = savedScore;
		currentScoreLabel.setText(String.valueOf(DataProtecterUtils
				.desStringToLong(savedScore)));
		currentScoreLabel.setRightPositionX(251);

		highScoreLabel.setText(String.valueOf(SPUtils
				.get1010HighScore(activity)));
		highScoreLabel.setLeftPositionX(340);
	}

	private void enableAllEvents() {
		LogUtils.v(TAG, "使能所有的按钮（refreshBtn除外）...");

		pauseBtn.setIgnoreTouch(false);
		pauseBtn.setEnabled(true);

		luckyStarBtn.setIgnoreTouch(false);
		luckyStarBtn.setEnabled(true);

		setIgnoreTouch(false);
	}

	public void hideAndDisableAllEvents() {
		setAlpha(0);
		pauseBtn.setIgnoreTouch(true);
		refreshBtn.setIgnoreTouch(true);
		luckyStarBtn.setIgnoreTouch(true);
	}

	private void buildUI() {
		pauseBtn = ButtonMaker.makeFromSingleImgFile(0, 0, "pause",
				vertexBufferObjectManager);
		pauseBtn.setPosition(50, 47);
		attachChild(pauseBtn);
		pauseBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
//				if (weatherProgress < 12) {
//					weatherProgress+=6;
//					isChangeProgress = true;
//				}
				TCAgent.onEvent(activity, "点击连萌暂停按钮");
				SoundUtils.play1010Drop();       
				showPauseDialog();
				((PopStar) activity).showPop();
				((PopStar) activity).hideBanner();
			}
		});

		Sprite medalSprite = SpriteMaker.makeSpriteWithSingleImageFile("medal",
				vertexBufferObjectManager);
		medalSprite.setPosition(277, 44);
		attachChild(medalSprite);

		luckyStarBtn = ButtonMaker.makeFromSingleImgFile(0, 0, "lucky_star_bg",
				vertexBufferObjectManager);
		luckyStarBtn.setPosition(465, 41);
		luckyStarBtn.setScale(1.1f);
		attachChild(luckyStarBtn);

		luckyStarBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SoundUtils.play1010Drop();
				TCAgent.onEvent(activity, "点击连萌星星商城");
				SoundUtils.playButtonClick();

				SceneBundle bundle = new SceneBundle();
				bundle.putBooleanExtra("fromPop", false);
				mainScene.startScene(ShopScene.class, bundle);
			}
		});

		luckyStarLabel = TextMaker.make("1234567890", "1010num", 0, 0,
				HorizontalAlign.CENTER, vertexBufferObjectManager);
		luckyStarLabel
				.setText(String.valueOf(SPUtils.getLuckStarNum(activity)));
		luckyStarLabel.setCentrePositionX(luckyStarBtn.getCentreX()+10);
		luckyStarLabel.setCentrePositionY(62);
		luckyStarLabel.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		luckyStarLabel.setScale(0.9f);
		attachChild(luckyStarLabel);

		currentScoreLabel = TextMaker.make("01234567890000000", "1010num", 0,
				0, HorizontalAlign.RIGHT, vertexBufferObjectManager);
		currentScoreLabel.setText(String.valueOf(SPUtils
				.get1010CurrentScore(activity)));
		currentScoreLabel.setCentrePositionY(65);
		currentScoreLabel.setRightPositionX(251);
		currentScoreLabel
				.setColor(new Color(201f / 255, 231f / 255, 229f / 255));
		currentScoreLabel.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		attachChild(currentScoreLabel);

		highScoreLabel = TextMaker.make("01234567890000000", "1010num", 0, 0,
				HorizontalAlign.RIGHT, vertexBufferObjectManager);
		highScoreLabel.setText(String.valueOf(SPUtils
				.get1010HighScore(activity)));
		highScoreLabel.setCentrePositionY(65);
		highScoreLabel.setLeftPositionX(340);
		highScoreLabel.setColor(new Color(67f / 255, 179f / 255, 203f / 255));
		highScoreLabel.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		attachChild(highScoreLabel);
		
		// 天气道具容器 add by liufengqiang
		weatherContainer = SpriteMaker.makeSpriteWithSingleImageFile(
				"weather_container", vertexBufferObjectManager);
		weatherContainer.setCentrePositionX(mainScene.getCentreX());
		weatherContainer.setCentrePositionY(GameConstants.ONE_BLOCK_START_Y + 18);
		weatherContainer.setScaleY(GameConstants.screenScale() );
		attachChild(weatherContainer);
		
		// 加载天气道具资源
		blackFlashProp = SpriteMaker.makeSpriteWithSingleImageFile("black_flash",
				vertexBufferObjectManager);
		blackFlashProp.setCentrePosition(weatherContainer.getLeftX() + 57,
				weatherContainer.getCentreY());
		blackFlashProp.setScaleY(GameConstants.screenScale());
		attachChild(blackFlashProp);
		
		whiteFlashProp = SpriteMaker.makeSpriteWithSingleImageFile("white_flash",
				vertexBufferObjectManager);
		whiteFlashProp.setCentrePosition(weatherContainer.getLeftX() + 57,
				weatherContainer.getCentreY());
		whiteFlashProp.setScaleY(GameConstants.screenScale());
		attachChild(whiteFlashProp);
		whiteFlashProp.setVisible(false);
		
		cloudProp = ButtonMaker.makeFromSingleImgFile(0, 0, "rain",
				vertexBufferObjectManager);
		cloudProp.setCentrePosition(weatherContainer.getLeftX() + 57,
				weatherContainer.getCentreY());
		cloudProp.setScaleCenter(cloudProp.getWidthHalf(), cloudProp.getHeightHalf());
		cloudProp.setScaleY(GameConstants.screenScale());

		// 小点点进度显示
		showAndRefreshPointProgress();
		
		// 瞄准器
		flashAim = ButtonMaker.makeFromSingleImgFile(0, 0, "flash_aim", vertexBufferObjectManager);
		flashAim.setCentrePosition(whiteFlashProp.getCentreX(), whiteFlashProp.getCentreY());
		flashAim.setScaleY(GameConstants.screenScale());
		flashAim.setAlpha(0);
		
		// 雨滴条
		rain_drop = SpriteMaker.makeSpriteWithSingleImageFile(
				"rain_drop", vertexBufferObjectManager);
		rain_drop.setPositionY(120);
		rain_drop.setAlpha(0);
		attachChild(rain_drop);
		
		refreshBtn = ButtonMaker.makeFromSingleImgFile(0, 0, "refresh",
				vertexBufferObjectManager);
		Size size = SizeHelper.ogSizeScale(44, 44);
		refreshBtn.setSize(size.getWidth(), size.getHeight());
		refreshBtn.setRightPositionX(weatherContainer.getRightX() - 36);
		refreshBtn.setCentrePositionY(weatherContainer.getCentreY());
		refreshBtn.setScaleCenter(refreshBtn.getWidthHalf(),
				refreshBtn.getHeightHalf());

		refreshBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				TCAgent.onEvent(activity, "点击连萌刷新道具");
				if (isInRefreshGuide) {
					LogUtils.v(TAG, "教程引导模式下点击，不消耗幸运星...");
					doRefresh(false, true);

					refreshBtn.clearEntityModifiers();
					refreshBtn.setScale(1);
				} else {
					checkIfEnoughLuckyStarToDoRefresh();
				}
			}
		});

		attachChild(refreshBtn);

		Size containerSize = SizeHelper.ogSizeScale(58, 58);
		GameConstants.OZ_CONTAINER_SIZE = containerSize;
		LogUtils.v(TAG, "容器方格的尺寸为：{" + containerSize.getWidth() + ","
				+ containerSize.getHeight() + "}");
		containerBlockStartX = (GameConstants.BASE_WIDTH - 10 * containerSize
				.getWidth()) / 2;
		GameConstants.OZ_PADDING_X = containerBlockStartX;
		float containerBlockStartY = GameConstants.OZ_CONTAINER_START_Y;
		LogUtils.v(TAG, "容器方格的起始位置为：{" + containerBlockStartX + ","
				+ containerBlockStartY + "}");

		LogUtils.v(TAG, "计算组合方格的大小...");
		groupSize = OZGroupBlockSizeCalculator.calculate();
		LogUtils.v(
				TAG,
				"组合方格的大小为：" + groupSize.getWidth() + ","
						+ groupSize.getHeight());

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				containerSprites[i][j] = SpriteMaker
						.makeSpriteWithSingleImageFile("1010_block_container",
								vertexBufferObjectManager);
				containerSprites[i][j].setPosition(i * containerSize.getWidth()
						+ containerBlockStartX, j * containerSize.getHeight()
						+ containerBlockStartY);
				containerSprites[i][j].setSize(containerSize.getWidth(),
						containerSize.getHeight());
				containerSprites[i][j].setScaleCenter(
						containerSprites[i][j].getWidthHalf(),
						containerSprites[i][j].getHeightHalf());
				attachChild(containerSprites[i][j]);
			}
		}
		
		Size starSize = SizeHelper.ogSizeScale(50, 50);
		GameConstants.OZ_STAR_SIZE = starSize;
		LogUtils.v(TAG,
				"星星方格的尺寸为:{" + starSize.getWidth() + "," + starSize.getHeight()
						+ "}");
		float paddingX = (containerSize.getWidth() - starSize.getWidth()) / 2;
		float paddingY = (containerSize.getHeight() - starSize.getHeight()) / 2;
		LogUtils.v(TAG, "星星与容器之间的间距是：{" + paddingX + "," + paddingY + "}");
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				starSprites[i][j] = new AnimatedSprite(0, 0, "star",
						vertexBufferObjectManager);
				starSprites[i][j].setPosition(i * containerSize.getWidth()
						+ containerBlockStartX + paddingX,
						j * containerSize.getHeight() + containerBlockStartY
								+ paddingY);
				starSprites[i][j].setSize(starSize.getWidth(),
						starSize.getHeight());
				starSprites[i][j].setScaleCenter(starSize.getWidth() / 2,
						starSize.getHeight() / 2);
				attachChild(starSprites[i][j]);
			}
		}
		// 增加beyond图片
		beyond = SpriteMaker.makeSpriteWithSingleImageFile("beyond",
				vertexBufferObjectManager);
		beyond.setCentrePosition(mainScene.getCentreX(), mainScene.getCentreY());
		attachChild(beyond);
		beyond.setVisible(false);
				
		attachChild(cloudProp);
		cloudProp.setVisible(false);
		
		fadeInList.add(luckyStarBtn);
		fadeInList.add(luckyStarLabel);
		fadeInList.add(pauseBtn);
		fadeInList.add(medalSprite);
		fadeInList.add(currentScoreLabel);
		fadeInList.add(highScoreLabel);
		fadeInList.add(refreshBtn);
		
		fadeInList.add(weatherContainer);
		fadeInList.add(blackFlashProp);
		fadeInList.add(whiteFlashProp);
		fadeInList.add(cloudProp);
		for (Sprite sprite : pointLists) {
			fadeInList.add(sprite);
		}
	}

	private void showPauseDialog() {
		ResourceManager.load1010PauseDialogTextures();
		final Dialog dialog = new Dialog(mainScene);
		dialog.setBackKeyResponsed(true);

		ButtonSprite shopBtn = ButtonMaker.makeFromSingleImgFile(187, 547,
				"1010_pause_shop", vertexBufferObjectManager);
		shopBtn.setCentrePosition(mainScene.getCentreX(), mainScene.getCentreY());
		shopBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SoundUtils.play1010Drop();
				mainScene.startScene(ShopScene.class);
			}
		});
		dialog.attachChild(shopBtn);
		dialog.show();
		dialog.setDialogDismissListener(new DialogDismissListener() {
			public void onDialogDismiss() {
				ResourceManager.unload1010PauseDialogTextures();
				((PopStar) activity).showBanner();
			}
		});
		
		ButtonSprite homeBtn = ButtonMaker.makeFromSingleImgFile(187, 547,
				"1010_pause_home", vertexBufferObjectManager);
		homeBtn.setLeftPositionX(shopBtn.getLeftX());
		homeBtn.setBottomPositionY(shopBtn.getTopY()-20);
		homeBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				dialog.dismiss();
				SoundUtils.play1010Drop();
				//如果自己存在就删除掉自己
				if(null!=oneBlockShape){
//					oneBlockShape.detachSelf();
				}
				mainScene.showMenu();
			}
		});
		dialog.attachChild(homeBtn);

		final ButtonSprite audioBtn = ButtonMaker.makeFromTPFile(320, 490,
				"1010_pause_audio", vertexBufferObjectManager);
		audioBtn.setRightPositionX(shopBtn.getRightX());
		audioBtn.setBottomPositionY(homeBtn.getBottomY());
		audioBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SPUtils.toggleAudio(activity);
				if (SPUtils.isAudioOpen(activity)) {
					SoundUtils.play1010Drop();
					TCAgent.onEvent(activity, "连萌打开音效");
					audioBtn.setCurrentTileIndex(0);
					SoundUtils.setVolumn(1);
				} else {
					TCAgent.onEvent(activity, "连萌关闭音效");
					audioBtn.setCurrentTileIndex(1);
					SoundUtils.setVolumn(0);
				}
			}
		});
		if (SPUtils.isAudioOpen(activity)) {
			audioBtn.setCurrentTileIndex(0);
		} else {
			audioBtn.setCurrentTileIndex(1);
		}

		dialog.attachChild(audioBtn);

		ButtonSprite restartBtn = ButtonMaker.makeFromSingleImgFile(187, 547,
				"1010_pause_new", vertexBufferObjectManager);
		restartBtn.setLeftPositionX(shopBtn.getLeftX());
		restartBtn.setTopPositionY(shopBtn.getBottomY()+20);
		restartBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				dialog.dismiss();
				//点击了暂停按钮中的刷新图片
				restart();
				
			}
		});
		dialog.attachChild(restartBtn);

		ButtonSprite resumeBtn = ButtonMaker.makeFromSingleImgFile(187, 547,
				"1010_pause_resume", vertexBufferObjectManager);
		resumeBtn.setRightPositionX(shopBtn.getRightX());
		resumeBtn.setTopPositionY(restartBtn.getTopY());
		resumeBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				dialog.dismiss();
				SoundUtils.play1010Drop();
			}
		});
		dialog.attachChild(resumeBtn);
	}

	private void checkIfEnoughLuckyStarToDoRefresh() {
		int cost = GameConstants.getSwitchCost(SPUtils
				.get1010RefreshUsedNum(activity));
		int starNum = (int) SPUtils.getLuckStarNum(activity);
		if (cost > starNum) {
		/*	if (SPUtils.isShowLuckyStarNotEnoughDialog(activity)) {
				showLuckyStarNotEnoughDialog(BUY_TYPE.REFRESH);
			} else {
				LogUtils.v(TAG, "想要使用刷新道具但是钱不够，展示快捷购买对话框...");
				showQuickBuyDialog(BUY_TYPE.REFRESH);
			}*/
			showQuickBuyDialog(BUY_TYPE.REFRESH);
		} else {
			doRefresh(true, true);
		}
	}

	private void showCostTips(int cost) {
		costTips = TextMaker.make(String.format("本次消耗%d枚幸运星", cost),
				"1010costTips", 0, 0, HorizontalAlign.RIGHT,
				vertexBufferObjectManager);
		costTips.setLeftPositionX(GameConstants.BASE_WIDTH);
		costTips.setCentrePositionY(657);
		attachChild(costTips);

		MoveModifier moveIn = new MoveModifier(0.5f, GameConstants.BASE_WIDTH,
				GameConstants.BASE_WIDTH / 2 - costTips.getWidthHalf(),
				costTips.getY(), costTips.getY(),
				EaseExponentialOut.getInstance());
		DelayModifier delay = new DelayModifier(0.5f);
		MoveModifier moveOut = new MoveModifier(0.5f, GameConstants.BASE_WIDTH
				/ 2 - costTips.getWidthHalf(), -costTips.getWidth(),
				costTips.getY(), costTips.getY(),
				EaseExponentialOut.getInstance());

		costTips.registerEntityModifier(new SequenceEntityModifier(
				new IEntityModifierListener() {
					public void onModifierStarted(IModifier<IEntity> pModifier,
							IEntity pItem) {
					}

					public void onModifierFinished(
							IModifier<IEntity> pModifier, IEntity pItem) {
						costTips.detachSelf();
						costTips.dispose();
						costTips = null;
					}
				}, moveIn, delay, moveOut));
	}

	private void doRefresh(boolean isCost, final boolean isShouldAllMatch) {
		refreshBtn.setEnabled(false);
		if (isCost) {
			int cost = GameConstants.getSwitchCost(SPUtils
					.get1010RefreshUsedNum(activity));
			int starNum = (int) SPUtils.getLuckStarNum(activity);
			SPUtils.saveLuckStarNum(activity, starNum - cost);
			SPUtils.save1010RefreshUsedNum(activity,
					SPUtils.get1010RefreshUsedNum(activity) + 1);
			luckyStarLabel.setText(String.valueOf(starNum - cost));
			luckyStarLabel.setCentrePositionX(luckyStarBtn.getCentreX()+10);

			showCostTips(cost);
		}
		if (addedShapeList.size() > 0) {
			OZShapeMoveListener listener = new OZShapeMoveListener() {
				int num = 0;

				public void afterMove() {
					num++;
					if (num == addedShapeList.size()) {
						for (OZShape shape : addedShapeList) {
							shape.detachSelf();
						}
						addedShapeList.clear();
						if (!isShouldAllMatch) {
							shapeSigns = OZShapeIDGenerator
									.generateOZShapeID(starSigns);
						} else {
							shapeSigns = OZShapeIDGenerator
									.generateAllMatchedShapeID(starSigns);
						}
						addedShapeList = OZShapeBuildAndIniter
								.buildAndInitOZShape(shapeSigns,
										vertexBufferObjectManager);
						displayShapes();
					}
				}
			};
			for (OZShape shape : addedShapeList) {
				shape.moveOut(listener);
			}
		} else {
			shapeSigns = OZShapeIDGenerator.generateOZShapeID(starSigns);
			addedShapeList = OZShapeBuildAndIniter.buildAndInitOZShape(
					shapeSigns, vertexBufferObjectManager);
			displayShapes();
		}

	}
	
	/**
	 * 显示幸运星不足提示界面
	 *添加资源加载和释放的方法，避免部分机型内存不够回收资源造成的显示缺失问题 
	 */
	private void showLuckyStarNotEnoughDialog_delete(BUY_TYPE buyTypey) {
		final BUY_TYPE buy_type = buyTypey;
		ResourceManager.loadLuckyStarNotEnoughDialog(activity);
		final Dialog dialog = new Dialog(mainScene);
		dialog.setSize(GameConstants.BASE_WIDTH, GameConstants.BASE_HEIGHT);
		
		// 背景图
		Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"common_bg_ne", vertexBufferObjectManager);
		bgSprite.setPosition(31, 165);
		bgSprite.setScale(0.8f);
		dialog.attachChild(bgSprite);
		
		float gapV = 15.0f;
		
		// 标题
		Text title = TextMaker.make(
				"哎呀！幸运星不够了", "40white_ne",
				bgSprite.getCentreX(), bgSprite.getCentreY() - 200f * bgSprite.getScaleY(), HorizontalAlign.CENTER,
				vertexBufferObjectManager);
		dialog.attachChild(title);
		
		// 退出按钮
		ButtonSprite quitBtn = ButtonMaker.makeFromSingleImgFile(563, 266,
				"quit_ne", vertexBufferObjectManager);
		quitBtn.setCentrePositionX(bgSprite.getCentreX() + bgSprite.getWidthHalf() * bgSprite.getScaleX() - 20);
		quitBtn.setCentrePositionY(bgSprite.getCentreY() - bgSprite.getHeightHalf() * bgSprite.getScaleY() + 10);
		quitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				dialog.dismissWithAnimamtion();
				//需要添加方法的 bug 点
			}
		});
		quitBtn.setScale(0.8f);
		dialog.attachChild(quitBtn);
		
		// 哭泣的星星
		Sprite cryStarSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"starCry_ne", vertexBufferObjectManager);
		cryStarSprite.setScale(0.6f);
		cryStarSprite.setCentrePositionX(GameConstants.BASE_WIDTH / 2);
		cryStarSprite.setCentrePositionY(bgSprite.getCentreY() - gapV);
		dialog.attachChild(cryStarSprite);
		
		// 商城购买说明
		gapV = 0.0f;
		Text tipsLabel = TextMaker.make(
				"点击按钮购买大礼包补充幸运星\n您也可以从右上角进入商城购买 ", "25white_ne",
				cryStarSprite.getCentreX(), cryStarSprite.getBottomY() + gapV, HorizontalAlign.CENTER,
				vertexBufferObjectManager);
		dialog.attachChild(tipsLabel);
		
		// 购买按钮
		gapV = 8.0f;
		ButtonSprite okBtn = ButtonMaker.makeFromSingleImgFile(320,
				730, "yellow_btn_long_ne", vertexBufferObjectManager);
		okBtn.setScale(0.85f);
		okBtn.setCentrePositionX(GameConstants.BASE_WIDTH / 2);
		okBtn.setBottomPositionY(bgSprite.getCentreY() + bgSprite.getHeightHalf() * bgSprite.getScaleY()- gapV);
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				dialog.dismissWithoutAnimation();
				showQuickBuyDialog(buy_type);
			}
		});
		dialog.attachChild(okBtn);

		
		Text oKText = TextMaker.make(
				"现在就购买", "50white",
				320, 705, HorizontalAlign.CENTER,
				vertexBufferObjectManager);
		oKText.setCentrePosition(okBtn.getCentreX(), okBtn.getCentreY());
		dialog.attachChild(oKText);
		
		dialog.setDialogDismissListener(new DialogDismissListener() {
					public void onDialogDismiss() {
						ResourceManager.unloadLuckyStarNotEnoughDialog();

						if (buy_type == BUY_TYPE.CONTINUE) {
							gameOver();
						}
						if (buy_type == BUY_TYPE.REFRESH) {
						}
						if (buy_type == BUY_TYPE.ONE_BLOCK) {
							SoundUtils.play1010PutFail();
							handledShape.goBack();
							handledShape = null;
						}
						if (buy_type == BUY_TYPE.QUICK_FORCE_SHOW) {
						}
					
					}
					
				});
		LoopEntityModifier loop = new LoopEntityModifier(
				new SequenceEntityModifier(new ScaleModifier(0.5f,
						0.7f, 0.8f),
						new ScaleModifier(0.5f, 0.8f, 0.7f)));
		okBtn.registerEntityModifier(loop);
		oKText.registerEntityModifier(loop.deepCopy());
		dialog.showWithAnimation();
	}

	private void showQuickBuyDialog(BUY_TYPE buyType) {
		this.buyType = buyType;
		buildBuyDialog();
		buyDialog.show();
	}

	private void buildBuyDialog() {
		ResourceManager.loadBuyDialogTextures(activity);
		buyDialog = new Dialog(mainScene);

		Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"quick_buy_bg", vertexBufferObjectManager);
		bgSprite.setPosition(0, 91);
		buyDialog.attachChild(bgSprite);

		ButtonSprite quitBtn = ButtonMaker.makeFromSingleImgFile(563, 266,
				"options_quit", vertexBufferObjectManager);
		quitBtn.setRightPositionX(640);
		quitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				buyDialog.dismissWithAnimamtion();    //这里正常显示
			}
		});
		quitBtn.setScale(0.8f);
		buyDialog.attachChild(quitBtn);

		// 328/186
		ButtonSprite okBtn = ButtonMaker.makeFromSingleImgFile(320, 774,
				"quick_buy_btn_ok", vertexBufferObjectManager);
		okBtn.setPosition(330, 667);
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				TCAgent.onEvent(activity, "点击连萌大礼包");
				quickBuy();
			}
		});
		
		//去商城按钮
		ButtonSprite goshopBtn = ButtonMaker.makeFromSingleImgFile(320, 774,
				"btn_go_shop", vertexBufferObjectManager);
		goshopBtn.setPosition(65, 667); 
		
		if(SPUtils.getIsShowBtShop(activity)){
			buyDialog.attachChild(goshopBtn);
		}else {
			okBtn.setPosition(205, 667);
		}
		
		
		goshopBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				SoundUtils.play1010Drop();
				TCAgent.onEvent(activity, "点击连萌星星商城");
				SoundUtils.playButtonClick();

				SceneBundle bundle = new SceneBundle();
				bundle.putBooleanExtra("isFromMenu_big", true);
				mainScene.startScene(ShopScene.class, bundle);
			}
		});
		
		
		buyDialog.attachChild(okBtn);
		buyDialog.setDialogDismissListener(new DialogDismissListener() {
			public void onDialogDismiss() {
				ResourceManager.unloadBuyDialogTextures();
				if (buyType == BUY_TYPE.CONTINUE) {
					gameOver();
				}
				if (buyType == BUY_TYPE.REFRESH) {
				}
				if (buyType == BUY_TYPE.ONE_BLOCK) {
					SoundUtils.play1010PutFail();
					handledShape.goBack();
					handledShape = null;
				}
				if (buyType == BUY_TYPE.QUICK_FORCE_SHOW) {
				}
			}
		});
	}

	public void quickBuy() {
		((PopStar) getActivity()).pay(GameConstants.QUICK_BUY_CHARGEPOINT_ID, new PayCallback() {
			public void callback(int code, String msg) {
				if (code == PayCallback.OK) {
					buyDialog.setDialogDismissListener(new DialogDismissListener() {
						public void onDialogDismiss() {
							ResourceManager.unloadBuyDialogTextures();
							SPUtils.saveLuckStarNum(activity, SPUtils.getLuckStarNum(activity) + QUICK_BUY_NUM);
							if (buyType == BUY_TYPE.REFRESH) {
								doRefresh(true, true);
							}
							if (buyType == BUY_TYPE.CONTINUE) {
								doContinueGame();
							}
							if (buyType == BUY_TYPE.ONE_BLOCK) {
								handleTouchUpOrCalcel();
							}
							if (buyType == BUY_TYPE.QUICK_FORCE_SHOW) {
								SPUtils.setQuickBuyDialogShowed(activity, true);
							}
						}
					});
					buyDialog.dismissWithAnimamtion();
				}
				Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 显示背景block的动画，完毕之后显示已经有的星星的动画(如果之前星星的数量不为0，亦即
	 * {@linkplain #shouldDisplayedStarsNum}的值不为0的话),再之后展示组合方块，然后使能所有的按钮
	 */
	private void displayContainerAndStarsAndAfterThatEnableAllEvents() {
		SoundUtils.play1010Begin();
		new Thread(new Runnable() {
			public void run() {
				final IntegerContainer container = new IntegerContainer(0);
				IEntityModifierListener listener = new IEntityModifierListener() {
					public void onModifierStarted(IModifier<IEntity> pModifier,
							IEntity pItem) {
					}

					public void onModifierFinished(
							IModifier<IEntity> pModifier, IEntity pItem) {
						container.add();
						// 全部动画展示完毕了
						if (container.getValue() == 100 && mainScene.isGameOn()) {
							displayAlreadyAddedStars();
						}
					}
				};
				if (mainScene.isGameOn()) {
					for (int index = 0; index < 10; index++) {
						for (int i = index; i >= 0; i--) {
							containerSprites[i][index].setScale(0);
							containerSprites[i][index].setAlpha(1);
							final ScaleModifier scaleModifier = new ScaleModifier(
									0.5f, 0f, 1.0f, listener);
							containerSprites[i][index]
									.registerEntityModifier(scaleModifier);
						}
						for (int j = index - 1; j >= 0; j--) {
							containerSprites[index][j].setScale(0);
							containerSprites[index][j].setAlpha(1);

							final ScaleModifier scaleModifier = new ScaleModifier(
									0.5f, 0f, 1.0f, listener);
							containerSprites[index][j]
									.registerEntityModifier(scaleModifier);
						}
						if (mainScene.isGameOn()) {
							try {
								Thread.sleep(50);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						} else {
							break;
						}
					}
				}
			}
		}).start();
	}

	private void displayShapes() {
		if (addedShapeList.size() != 0) {
			SoundUtils.play1010Refresh();
			OZShapeMoveListener moveListener = new OZShapeMoveListener() {
				int num = 0;

				public void afterMove() {
					num++;
					if (num == addedShapeList.size()) {
						LogUtils.v(TAG,
								"所有的组合形状都已经moveIn，那么，如果组合形状数量是三个，展示refreshBtn的动画，同时refreshBtn是可以点击的");
						refreshBtn.setEnabled(true);
						refreshBtn.setIgnoreTouch(false);
						if (addedShapeList.size() != 3) {
							LogUtils.v(
									TAG,
									"展示的组合形状不足三个，那么，有可能是在EZExploader进行消除方块的过程中返回的主菜单，亦即有可能检查游戏是否结束这个操作没有完成，所以，这里加上检查游戏是否能继续的逻辑");
							checkIfCanGoon();
						}
						if (isInRefreshGuide) {
							guide1Sprite.detachSelf();
							guide1Sprite.dispose();
							guide1Sprite = null;
							showGuide2();
						}
					}
				}
			};
			for (OZShape shape : addedShapeList) {
				shape.attach(OZGameLayer.this);
				shape.moveIn(moveListener);
			}
			enableAllEvents();

		} else {
			doRefresh(false, false);
		}
	}

	/**
	 * 从左到右展示之前已经有的星星
	 */
	private void displayAlreadyAddedStars() {
		if (shouldDisplayedStarsNum != 0) {
			LogUtils.v(TAG, "之前屏幕上有已经添加的星星，展示星星出现的动画..");
			new Thread(new Runnable() {
				IntegerContainer intContainer = new IntegerContainer(0);
				IEntityModifierListener listener = new IEntityModifierListener() {
					public void onModifierStarted(IModifier<IEntity> pModifier,
							IEntity pItem) {
					}

					public void onModifierFinished(
							IModifier<IEntity> pModifier, IEntity pItem) {
						intContainer.add();
						if (intContainer.getValue() == shouldDisplayedStarsNum) {
							LogUtils.v(TAG, "星星动画展示完毕，开始展示组合方块...");
							displayShapes();
						}
					}
				};

				public void run() {
					if (mainScene.isGameOn()) {
						// 每一列
						for (int index = 0; index < 10; index++) {
							// 每一列的每一个
							for (int j = 0; j < 10; j++) {
								if (starSigns[index][j] != GameConstants.BLOCK_NONE) {
									starSprites[index][j]
											.setCurrentTileIndex(starSigns[index][j]);
									final ScaleModifier scaleModifier = new ScaleModifier(
											0.5f, 0f, 1.0f, listener);
									starSprites[index][j]
											.registerEntityModifier(scaleModifier);
								}
							}
							if (mainScene.isGameOn()) {
								try {
									Thread.sleep(50);
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							} else {
								break;
							}
						}
					}
				}
			}).start();
		} else {
			LogUtils.v(TAG, "之前屏幕上没有已经添加的星星，直接显示组合方块...");
			displayShapes();
		}

	}

	private void refreshLuckStarNum() {
		luckyStarLabel
				.setText(String.valueOf(SPUtils.getLuckStarNum(activity)));
		luckyStarLabel.setCentrePositionX(luckyStarBtn.getCentreX()+10);
	}

	private void saveGameData() {
		try {
			LogUtils.v(TAG, "保存当前游戏数据...");
			SPUtils.save1010CurrentScore(activity,
					DataProtecterUtils.desStringToLong(savedScore));
			SPUtils.save1010HighScore(activity,
					Long.parseLong(highScoreLabel.getText().toString()));
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					int sign = starSigns[i][j];
					// 如果是正在进行或者等待进行爆炸的星星，那么直接保存为none类型，亦即，可以理解为跳过动画，直接保存为动画结束之后的场景数据(用于处理，正在爆炸过程中，用户点击返回键返回主菜单的情况)
					if (sign == GameConstants.BLOCK_EXPLOADING) {
						sign = GameConstants.BLOCK_NONE;
					}
					sb.append(sign);
					sb.append(",");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append(";");
			}
			SPUtils.save1010GroupSignsWithFormat(activity, shapeSigns);
			sb.deleteCharAt(sb.length() - 1);
			SPUtils.save1010Stars(activity, sb.toString());
			LogUtils.v(TAG, "存储的shape为:" + shapeSigns[0] + "," + shapeSigns[1]
					+ "," + shapeSigns[2]);
			SaveGameData.saveJsonData(activity);
		} catch (Exception ex) {
		}
	}

	public void onResume() {
		refreshLuckStarNum();
	}

	public void onPause() {
		if (mainScene.isGameOn()) {
			saveGameData();
		}
	}

	/**
	 * 获取touchDown事件产生的shape所在区域的索引
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private OZShape getTouchedShape(float x, float y) {
		try {
			if (!isInOneBlockGuideState
					&& x > GameConstants.OZ_PADDING_X
					&& x < GameConstants.BASE_WIDTH
							- GameConstants.OZ_PADDING_X
					&& y > GameConstants.OZ_GROUP_START_Y
					&& y < GameConstants.BASE_HEIGHT) {
				int index = (int) ((x - GameConstants.OZ_PADDING_X) / ((GameConstants.BASE_WIDTH - 2 * GameConstants.OZ_PADDING_X) / 3));
				index = Math.min(index, 2);
				if (shapeSigns[index] == GameConstants.BLOCK_NONE) {
					return null;
				} else {
					for (OZShape shape : addedShapeList) {
						if (shape.getIndex() == index) {
							return shape;
						}
					}
					return null;
				}
			} else {
				// 触摸的是小点点道具，小点点道具，小点点道具……
				if (x >= GameConstants.ONE_BLOCK_START_X
						&& x <= GameConstants.ONE_BLOCK_START_X
								+ GameConstants.ONE_BLOCK_SIZE.getWidth()
						&& y >= GameConstants.ONE_BLOCK_START_Y
						&& y <= GameConstants.ONE_BLOCK_START_Y
								+ GameConstants.ONE_BLOCK_SIZE.getHeight()) {

					Log.v("aaaaaaa", "点击的是小点道具");

					return oneBlockShape;
				}
			}
		} catch (Exception ex) {
		}
		return null;
	}

	private void handleTouchDown(float x, float y) {
		LogUtils.v(TAG, "处理down事件...");
		OZShape shape = getTouchedShape(x, y);
		if (shape != null && !shape.isBacking() && !shape.isOut()) {
			shape.jump(x, y);
			handledShape = shape;
		}
	}

	private void handleTouchMove(float x, float y) {
		if (handledShape != null) {
			handledShape.move(x, y);
		}
	}

	private void showOneBlockGuide() {
		mainScene.setInGuideState(true);
		isInOneBlockGuideState = true;
		ResourceManager.loadOneBlockGuideTextures();
		oneBlockGuideSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"guide3", vertexBufferObjectManager);
		oneBlockGuideSprite.setPosition(0, 0);
		attachChild(oneBlockGuideSprite);
		oneBlockShape.scaleForever();
		refreshBtn.setEnabled(false);
		pauseBtn.setEnabled(false);

		for (Sprite sprite : oneBlockShape.getSpriteList()) {
			sprite.setZIndex(Integer.MAX_VALUE);
		}
		sortChildren();

	}

	private boolean fullRowFlag = false; //满行标志
	private boolean isDied = false;
	
	// 在每次放置之后，执行该方法判断是否能进行消除，如果能进行消除，首先消除，消除之后判断能否继续进行放置，如果不能，游戏结束
	private void doExploadeIfNeedAfterCheckIfCanGoon(OZShape addedShape, int x,
			int y) {
		int[][] shapeSigns = addedShape.getShapeSigns();
		int shapeColumnNum = shapeSigns.length;
		int shapeRowNum = shapeSigns[0].length;

		List<Integer> fullRowList = new ArrayList<Integer>();
		List<Integer> fullColumnList = new ArrayList<Integer>();

		// 检查列
		for (int i = x; i < x + shapeColumnNum; i++) {
			if (FullHandler.isFullColumn(starSigns, i)) {
				LogUtils.v(TAG, "满列：" + i);
				fullColumnList.add(i);
			}
			if (!isOneBlockGuideShowed
					&& NineHandler.isNineBlockColumn(starSigns, i)) {
				boolean isEnoughBlocks = false;
				for (OZShape shape : addedShapeList) {
					if (shape.isMatch(starSigns)) {
						isEnoughBlocks = true;
					}
				}
				if (isEnoughBlocks) {
					if (!fullRowFlag) {
						isOneBlockGuideShowed = true;
						showOneBlockGuide();
						fullRowFlag = false;
					}
				} else { //满足小点点的同时死亡
					isDied = true;
				}
			}
		}
		// 检查行
		for (int j = y; j < y + shapeRowNum; j++) {
			if (FullHandler.isFullRow(starSigns, j)) {
				LogUtils.v(TAG, "满行：" + j);
				fullRowList.add(j);
			}
			if (!isOneBlockGuideShowed
					&& NineHandler.isNineBlockRow(starSigns, j)) {
				boolean isEnoughBlocks = false;
				for (OZShape shape : addedShapeList) {
					if (shape.isMatch(starSigns)) {
						isEnoughBlocks = true;
					}
				}
				if (isEnoughBlocks) {
					if (!fullRowFlag) {
						isOneBlockGuideShowed = true;
						showOneBlockGuide();
						fullRowFlag = false;
					}
				} else { //满足小点点的同时死亡
					isDied = true;
				}
			}
		}

		for (int i = 0; i < fullRowList.size(); i++) {
			FullHandler.setFullRowInExploadeMode(starSigns, fullRowList.get(i));
		}
		for (int i = 0; i < fullColumnList.size(); i++) {
			FullHandler.setFullColumnInExploadeMode(starSigns,
					fullColumnList.get(i));
		}

		int totalExploadeNums = fullRowList.size() + fullColumnList.size();
		
		// 更新小点点进度  add by liufengqiang
		if (totalExploadeNums != 0) {
			weatherProgress += totalExploadeNums;
			if (weatherProgress > 12) {
				weatherProgress = 12;
			}
			isChangeProgress = true;
		}
		
		int earcScore = OZEarnedScoreCalculator
				.getEarnedScore(totalExploadeNums);
		LogUtils.v(TAG, "需要消除的行列的总数是：" + totalExploadeNums + "，总的得分为："
				+ earcScore);
		addScore(earcScore);
		if (totalExploadeNums == 0) {
			checkIfCanGoon();
		} else {

			TCAgent.onEvent(activity, "同时消除" + totalExploadeNums + "行/列");

			SoundUtils.play1010(totalExploadeNums);
			OZExploader.exploade(mainScene, OZGameLayer.this, starSigns,
					starSprites, x, y, addedShape, fullRowList, fullColumnList,
					vertexBufferObjectManager, new OZShapeMoveListener() {
						public void afterMove() {
							checkIfCanGoon();
						}
					});
		}
	}

	// 检查是否能继续放置，如果不能，游戏结束
	private void checkIfCanGoon() {
		boolean isEnoughBlocks = false;
		for (OZShape shape : addedShapeList) {
			if (shape.isMatch(starSigns)) {
				isEnoughBlocks = true;
			}
		}
		// 游戏结束
		if (!isEnoughBlocks) {
			blinkAndShowContinueDialog();
		} else {
			LogUtils.v(TAG, "游戏没有结束，当前放置次数:" + placeNum + ",是否显示过引导?:"
					+ isGuideShowed);
			// 游戏没有结束，如果放置次数》=SHOW_GUIDE_PLACE_NUM_THREADHOLD&&没有展示过引导，那么进入引导模式
			if (placeNum >= SHOW_GUIDE_PLACE_NUM_THREADHOLD && !isGuideShowed
					&& !isInRefreshGuide && !isInOneBlockGuideState
					&& addedShapeList.size() != 0 && addedShapeList.size() != 3) {
				showGuide1();
			}
			//add by liufengqiang
			if (addedShapeList.size() == 2) {
				boolean isGameOverTip = false;
				for (OZShape shape : addedShapeList) {
					if (!shape.isMatch(starSigns)) {
						isGameOverTip = true;
					}
				}
				if (isGameOverTip) {
					gameOverTip();
				}
			}
		}
	}

	//提示用户，马上要结束游戏了
	private void gameOverTip() {
		SoundUtils.play1010(5);
		
		RotationByModifier leftRotation = new RotationByModifier(0.05f, -30f);
		RotationByModifier rightRotation = new RotationByModifier(0.05f, 30f);
		SequenceEntityModifier modifier = new SequenceEntityModifier(
				leftRotation, rightRotation, rightRotation, leftRotation,
				leftRotation, rightRotation, rightRotation, leftRotation,
				leftRotation, rightRotation);
		refreshBtn.registerEntityModifier(modifier.deepCopy());
		oneBlockShape.rotateShape();
		
		for (OZShape ozShape : addedShapeList) {
			ozShape.flashAnim();
		}
	}

	private void doContinueGame() {
		isInResultState = false;
		int goonNum = SPUtils.get1010GoonNum(activity);
		int costNum = GameConstants.getSwitchCost(goonNum);
		SPUtils.save1010GoonNum(activity, goonNum + 1);
		SPUtils.saveLuckStarNum(activity, SPUtils.getLuckStarNum(activity)
				- costNum);
		refreshLuckStarNum();
		doRefresh(false, true);
	}

	// 进行闪烁并弹出继续通关的对话框
	private void blinkAndShowContinueDialog() {
		isInResultState = true;
		LoopEntityModifier loop = BlinkModifierMaker.make(1.5f, 8);
		loop.addModifierListener(new IModifierListener<IEntity>() {
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
			}

			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				ResourceManager.load1010ContinueDialogTextures();
				final Dialog dialog = new Dialog(mainScene);
				dialog.setBackKeyResponsed(false);

				Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile(
						"1010_continue_bg", vertexBufferObjectManager);
				bgSprite.setPosition(68, 247);
				dialog.attachChild(bgSprite);

				final ButtonSprite closeBtn = ButtonMaker
						.makeFromSingleImgFile(187, 547,
								"1010_continue_cancel",
								vertexBufferObjectManager);
				closeBtn.setPosition(535, 239);
				closeBtn.setOnClickListener(new OnClickListener() {
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						closeBtn.setEnabled(false);
						dialog.dismiss();
						gameOver();
					}
				});
				dialog.attachChild(closeBtn);

				Text costTips = TextMaker.make(String.valueOf(GameConstants
						.getSwitchCost(SPUtils.get1010GoonNum(activity))),
						"1010costTips", 0, 0, HorizontalAlign.LEFT,
						vertexBufferObjectManager);
				costTips.setScale(0.6f);
				costTips.setCentrePosition(298, 568);
				dialog.attachChild(costTips);

				final ButtonSprite continueBtn = ButtonMaker
						.makeFromSingleImgFile(235, 602, "1010_continue_ok",
								vertexBufferObjectManager);
				continueBtn.setPosition(230, 613);
				continueBtn.setOnClickListener(new OnClickListener() {
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						TCAgent.onEvent(activity, "星星连萌点击继续通关");
						dialog.dismiss();
						continueBtn.setEnabled(false);
						if (isDied && !isOneBlockGuideShowed) {
							isOneBlockGuideShowed = true;
							showOneBlockGuide();
							isDied = false;
						}
						int costNum = GameConstants.getSwitchCost(SPUtils
								.get1010GoonNum(activity));
						if (SPUtils.getLuckStarNum(activity) >= costNum) {
							doContinueGame();
						} else {
							/*if (SPUtils.isShowLuckyStarNotEnoughDialog(activity)) {
								showLuckyStarNotEnoughDialog(BUY_TYPE.CONTINUE);
							} else {
								showQuickBuyDialog(BUY_TYPE.CONTINUE);
							}*/
							showQuickBuyDialog(BUY_TYPE.CONTINUE);
						}
					}
				});
				dialog.attachChild(continueBtn);

				SoundUtils.play1010Drop();
				dialog.showWithAnimation();
				dialog.setDialogDismissListener(new DialogDismissListener() {
					public void onDialogDismiss() {
						ResourceManager.unload1010ContinueDialogTextures();
						TCAgent.onEvent(activity, "星星连萌关闭继续通关");
					}
				});

			}
		});
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (i == 0 && j == 0) {
					containerSprites[i][j].registerEntityModifier(loop);
				} else {
					containerSprites[i][j].registerEntityModifier(loop
							.deepCopy());
				}
				if (starSigns[i][j] != GameConstants.BLOCK_NONE) {
					starSprites[i][j].registerEntityModifier(loop.deepCopy());
				}
			}
		}

	}

	private void showGameOverDialog(long currentScore) {

		ResourceManager.load1010GameOverDialogTextures();
		final Dialog dialog = new Dialog(mainScene);
		dialog.setBackKeyResponsed(false);
		
		Sprite littleBgSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"1010_over_little_bg", vertexBufferObjectManager);
		littleBgSprite.setCentrePosition(320, 390);
		dialog.attachChild(littleBgSprite);

		Sprite medalSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"big_medal", vertexBufferObjectManager);
		medalSprite.setCentrePosition(320, 0);
		medalSprite.setCentrePositionY(littleBgSprite.getCentreY());
		medalSprite.setScale(0.7f);
		dialog.attachChild(medalSprite);

		Text tipsLabel = TextMaker.make("超越自己 力量在心中！", "systemFont30", 320, 320,
				HorizontalAlign.CENTER, vertexBufferObjectManager);
		tipsLabel.setCentrePositionX(320);
		tipsLabel.setBottomPositionY(medalSprite.getTopY()-15);
//		tipsLabel.setColor(129.0f / 255, 160.0f / 255, 217.0f / 255);
		dialog.attachChild(tipsLabel);

		ButtonSprite shopBtn = ButtonMaker.makeFromSingleImgFile(187, 547,
				"1010_over_shop", vertexBufferObjectManager);
		shopBtn.setCentrePosition(320, 0);
		shopBtn.setTopPositionY(littleBgSprite.getBottomY()+20);
		shopBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				TCAgent.onEvent(activity, "点击结束界面商城");
				SoundUtils.play1010Drop();
				mainScene.startScene(ShopScene.class);
			}
		});
		dialog.attachChild(shopBtn);

		ButtonSprite homeBtn = ButtonMaker.makeFromSingleImgFile(187, 547,
				"1010_over_home", vertexBufferObjectManager);
		homeBtn.setPositionX(littleBgSprite.getLeftX());
		homeBtn.setTopPositionY(shopBtn.getBottomY()+20);
		homeBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				TCAgent.onEvent(activity, "点击结束界面主菜单按钮");
				SoundUtils.play1010Drop();
				dialog.dismiss();
				isInResultState = false;
				mainScene.showMenu();
			}
		});
		dialog.attachChild(homeBtn);

		ButtonSprite newBtn = ButtonMaker.makeFromSingleImgFile(187, 547,
				"1010_over_new", vertexBufferObjectManager);
		newBtn.setRightPositionX(littleBgSprite.getRightX());
		newBtn.setTopPositionY(shopBtn.getBottomY()+20);
		newBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				TCAgent.onEvent(activity, "点击结束界面再玩一次");
				oneBlockShape.detachSelf();
				oneBlockShape = null;
				SoundUtils.play1010Drop();
				dialog.dismiss();
				isInResultState = false;
				restart_over();
				weatherProgress = 0;
				isChangeProgress = true;
			}
		});
		dialog.attachChild(newBtn);

		Text scoreLabel = TextMaker.make(String.valueOf(currentScore),
				"1010numAdd5", 0, 0, HorizontalAlign.RIGHT,
				vertexBufferObjectManager);
		scoreLabel.setCentrePositionX(320);
		scoreLabel.setTopPositionY(medalSprite.getBottomY());
		dialog.attachChild(scoreLabel);
		dialog.show();
		dialog.setDialogDismissListener(new DialogDismissListener() {
			public void onDialogDismiss() {
				ResourceManager.unload1010GameOverDialogTextures();
			}
		});
		
		Sprite iconSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"1010_over_icon", vertexBufferObjectManager);
		iconSprite.setCentrePositionX(320);
		iconSprite.setBottomPositionY(littleBgSprite.getTopY() - 10);
		iconSprite.setScale(0.95f);
		dialog.attachChild(iconSprite);
		
		iconSprite.registerEntityModifier(new LoopEntityModifier(
				new SequenceEntityModifier(new ScaleModifier(0.5f, 1f, 0.95f),
						new ScaleModifier(0.5f, 0.95f, 1f))));
	}

	// 游戏结束了
	private void gameOver() {
		((PopStar) activity).showPop();
		isInResultState = true;
		long highScore = Long.parseLong(highScoreLabel.getText().toString());
		if (highScore < DataProtecterUtils.desStringToLong(savedScore)) {
			highScoreLabel.setText(String.valueOf(DataProtecterUtils
					.desStringToLong(savedScore)));
			highScoreLabel.setLeftPositionX(340);
		}
		final long tempScore = DataProtecterUtils.desStringToLong(savedScore);
		if (tempScore >= 100) {
			long score = tempScore
					/ ((long) Math.pow(10,
							String.valueOf(tempScore).length() - 1))
					* ((long) Math.pow(10,
							String.valueOf(tempScore).length() - 1));
			TCAgent.onEvent(activity, "星星连萌得分" + score + "以及以上");
		}
		savedScore = DataProtecterUtils.longToDesString(0);
		saveGameData();

		OZShapeMoveListener moveListener = new OZShapeMoveListener() {
			int num = 0;

			public void afterMove() {
				num++;
				if (num == addedShapeList.size()) {
					LogUtils.v(TAG, "游戏结束，清除之前屏幕上已经有的星星...");
					shouldDisplayedStarsNum = 0;
					for (int i = 0; i < starSigns.length; i++) {
						for (int j = 0; j < starSigns[0].length; j++) {
							if (starSigns[i][j] != GameConstants.BLOCK_NONE) {
								shouldDisplayedStarsNum++;
							}
						}
					}

					final IntegerContainer intContainer = new IntegerContainer(
							0);
					IEntityModifierListener listener = new IEntityModifierListener() {
						public void onModifierStarted(
								IModifier<IEntity> pModifier, IEntity pItem) {
						}

						public void onModifierFinished(
								IModifier<IEntity> pModifier, IEntity pItem) {
							intContainer.add();
							if (intContainer.getValue() == shouldDisplayedStarsNum) {
								LogUtils.v(TAG, "星星消除完毕，开始消除星星容器...");

								final IntegerContainer container = new IntegerContainer(
										0);
								IEntityModifierListener listener = new IEntityModifierListener() {
									public void onModifierStarted(
											IModifier<IEntity> pModifier,
											IEntity pItem) {
									}

									public void onModifierFinished(
											IModifier<IEntity> pModifier,
											IEntity pItem) {
										container.add();
										// 全部动画展示完毕了
										if (container.getValue() == 100
												&& mainScene.isGameOn()) {
											new Thread(new Runnable() {
												public void run() {
													showGameOverDialog(tempScore);
												}
											}).start();
										}
									}
								};
								OZExister.moveAllContainerOut(mainScene,
										containerSprites,
										vertexBufferObjectManager, listener);
							}
						}
					};
					OZExister.moveAllStarsOut(starSigns, starSprites, listener,
							mainScene);
				}
			}
		};
		OZExister.moveShapeOut(addedShapeList, moveListener);
		SoundUtils.play1010GameOver();
	}

	// 显示“点击刷新”的引导界面
	private void showGuide1() {
		SPUtils.setGuideShowed(activity, true);
		isGuideShowed = true;
		isInRefreshGuide = true;
		mainScene.setInGuideState(true);

		ResourceManager.loadGuideTextures();
		guide1Sprite = SpriteMaker.makeSpriteWithSingleImageFile("guide1",
				vertexBufferObjectManager);
		guide1Sprite.setPosition(0, 0);
		guide1Sprite.setZIndex(Integer.MAX_VALUE - 1);
		sortChildren();
		attachChild(guide1Sprite);

		luckyStarBtn.setEnabled(false);
		pauseBtn.setEnabled(false);

		ScaleModifier scale1 = new ScaleModifier(0.5f, 1.0f, 1.3f);
		ScaleModifier scale2 = new ScaleModifier(0.5f, 1.3f, 1.0f);
		SequenceEntityModifier sequence = new SequenceEntityModifier(scale1,
				scale2);
		LoopEntityModifier loop = new LoopEntityModifier(sequence);
		refreshBtn.registerEntityModifier(loop);
		refreshBtn.setZIndex(Integer.MAX_VALUE);
		sortChildren();
	}

	// 显示“刷新成功”的引导界面
	private void showGuide2() {
		guide2Sprite = SpriteMaker.makeSpriteWithSingleImageFile("guide2",
				vertexBufferObjectManager);
		guide2Sprite.setPosition(0, 0);
		attachChild(guide2Sprite);

		guide22Sprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"guide_border", vertexBufferObjectManager);
		guide22Sprite.setSize(GameConstants.BASE_WIDTH
				- GameConstants.OZ_PADDING_X * 2,
				GameConstants.OZ_GROUP_CONTAINER_HEIGHT
						- GameConstants.AD_BANNER_HEIGHT);
		guide22Sprite.setPosition(GameConstants.OZ_PADDING_X,
				GameConstants.BASE_HEIGHT
						- GameConstants.OZ_GROUP_CONTAINER_HEIGHT);
		attachChild(guide22Sprite);
	}

	// 显示引导完成的奖励对话框
	private void showGuideCompleteDialog() {
		ResourceManager.loadGuideCompleteTextures();
		final Dialog dialog = new Dialog(mainScene);
		dialog.setBackKeyResponsed(false);
		Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"guide_complete_bg", vertexBufferObjectManager);
		bgSprite.setPosition(127, 300);
		dialog.attachChild(bgSprite);

		final ButtonSprite okBtn = ButtonMaker.makeFromSingleImgFile(187, 547,
				"guide_complete_btn", vertexBufferObjectManager);
		okBtn.setPosition(243, 564);
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				okBtn.setEnabled(false);
				refreshBtn.clearEntityModifiers();
				dialog.dismiss();

				SPUtils.saveLuckStarNum(activity,
						SPUtils.getLuckStarNum(activity) + 5);
				refreshLuckStarNum();
				//showQuickBuyDialogAfterGuideComplete();
			}
		});
		dialog.attachChild(okBtn);

		dialog.setDialogDismissListener(new DialogDismissListener() {
			public void onDialogDismiss() {
				ResourceManager.unloadGuideCompleteTextures();
			}
		});
		dialog.showWithAnimation();
		SPUtils.setGuideShowed(activity, true);
	}

	private void handleTouchUpOrCalcel() {
		LogUtils.v(TAG, "处理up事件...");
		if (handledShape != null && !handledShape.isMoveingOut()) {
			// TODO 如果能容纳该形状，放置，否则，返回
			LogUtils.v(TAG, "当前所处的位置为：" + handledShape.getNearX() + ","
					+ handledShape.getNearY());

			final int x = handledShape.getNearX();
			final int y = handledShape.getNearY();

			if (handledShape.isMatch(starSigns, x, y)) {
				// 使用小点道具&不是在引导中
				if (handledShape == oneBlockShape && !isInOneBlockGuideState) {
					TCAgent.onEvent(activity, "使用填充道具");
					int cost = GameConstants.getOneBlockCost(SPUtils
							.get1010OneBlockUsedNum(activity));
					//添加使用幸运星的TD统计   add  by lvjibin
					TCAgent.onEvent(activity, "此次填充道具使用"+cost+"颗幸运星");
					
					long nowStar = SPUtils.getLuckStarNum(activity);
					if (cost > nowStar) {
					/*	if (SPUtils.isShowLuckyStarNotEnoughDialog(activity)) {
							showLuckyStarNotEnoughDialog(BUY_TYPE.ONE_BLOCK);//电信手机下有bug的情况   需要修改
						} else {
							showQuickBuyDialog(BUY_TYPE.ONE_BLOCK);
						}*/
						showQuickBuyDialog(BUY_TYPE.ONE_BLOCK);
						return;
					} else {
						//星星充足的情况下使用小点点道具
						SPUtils.set1010OneBlockUsedNum(activity,
								SPUtils.get1010OneBlockUsedNum(activity) + 1);
						SPUtils.saveLuckStarNum(activity, nowStar - cost);

						refreshLuckStarNum();
						mainScene.setisInAnminte(true);//不能允许finish
						final Text oneBlockCostTips = TextMaker.make(
								String.format("本次消耗%d枚幸运星", cost),
								"1010costTips", 0, 0, HorizontalAlign.RIGHT,
								vertexBufferObjectManager);
						oneBlockCostTips
								.setCentrePositionX(GameConstants.BASE_WIDTH / 2);
						oneBlockCostTips.setCentrePositionY(627);
						attachChild(oneBlockCostTips);

						MoveModifier moveIn = new MoveModifier(0.5f,
								GameConstants.BASE_WIDTH,
								GameConstants.BASE_WIDTH / 2
										- oneBlockCostTips.getWidthHalf(),
								oneBlockCostTips.getY(),
								oneBlockCostTips.getY(),
								EaseExponentialOut.getInstance());
						DelayModifier delay = new DelayModifier(0.5f);
						MoveModifier moveOut = new MoveModifier(0.5f,
								GameConstants.BASE_WIDTH / 2
										- oneBlockCostTips.getWidthHalf(),
								-oneBlockCostTips.getWidth(),
								oneBlockCostTips.getY(),
								oneBlockCostTips.getY(),
								EaseExponentialOut.getInstance());

						oneBlockCostTips.registerEntityModifier(new SequenceEntityModifier(
										new IEntityModifierListener() {
											public void onModifierStarted(
													IModifier<IEntity> pModifier,
													IEntity pItem) {
											}

											public void onModifierFinished(
													IModifier<IEntity> pModifier,
													IEntity pItem) {
												oneBlockCostTips.detachSelf();
												mainScene.setisInAnminte(false);//执行完成动画后设置可以点击返回键
											}
										}, moveIn, delay, moveOut));
					}
					
					
				}

				if (handledShape == oneBlockShape) {
					oneBlockShape = null;
					addOneBlockItem();
					if (isInOneBlockGuideState) {
						SPUtils.setOneBlockGuideShowed(activity, true);
						isInOneBlockGuideState = false;
						oneBlockGuideSprite.detachSelf();
						mainScene.setInGuideState(false);
						refreshBtn.setEnabled(true);
						pauseBtn.setEnabled(true);
						ResourceManager.unloadOneBlockGuideTextures();
					}

				} else {
					shapeSigns[handledShape.getIndex()] = GameConstants.BLOCK_NONE;
				}

				placeNum++;
				SoundUtils.play1010Drop();
				LogUtils.v(TAG, "当前拖动的形状可以放置到容器中...");

				handledShape.fill(starSigns, starSprites, x, y,
						new OZShapeMoveListener() {
							OZShape temp = handledShape;

							public void afterMove() {
								temp.detachSelf();
								addedShapeList.remove(temp);
								if (addedShapeList.size() == 0) {
									doRefresh(false, false);
								}
								// 2、检查是否可以进行消除
								// 3、如果不能进行消除，检查是否游戏结束
								doExploadeIfNeedAfterCheckIfCanGoon(temp, x, y);
							}
						});
				addScore(handledShape.getSpriteList().size());

			} else {
				SoundUtils.play1010PutFail();
				LogUtils.v(TAG, "不能匹配当前形状，返回原位...");
				handledShape.goBack();
			}
			handledShape = null;
		}
	}

	private void addScore(int score) {
		savedScore = DataProtecterUtils.longToDesString(DataProtecterUtils
				.desStringToLong(savedScore) + score);
	}

	public boolean onTouch(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		if (!mainScene.isGameOn()) {
			return false;
		}

		if (isInRefreshGuide && guide2Sprite != null) {
			isInRefreshGuide = false;
			guide2Sprite.detachSelf();
			guide2Sprite.dispose();
			guide2Sprite = null;
			guide22Sprite.detachSelf();
			guide22Sprite.dispose();
			guide22Sprite = null;
			ResourceManager.unloadGuideTextures();

			pauseBtn.setEnabled(true);
			luckyStarBtn.setEnabled(true);
			mainScene.setInGuideState(false);

			showGuideCompleteDialog();
			return false;
		}
		// 只有不在引导模式下，才能自有移动方块
		if (!isInRefreshGuide) {
			int action = pSceneTouchEvent.getAction();
			if (action == TouchEvent.ACTION_DOWN) {
				handleTouchDown(pSceneTouchEvent.getX(),
						pSceneTouchEvent.getY());
				weatherTouchDown(pSceneTouchEvent.getX(),
						pSceneTouchEvent.getY());
			}
			if (action == TouchEvent.ACTION_MOVE) {
				handleTouchMove(pSceneTouchEvent.getX(),
						pSceneTouchEvent.getY());
				weatherTouchMove(pSceneTouchEvent.getX(),
						pSceneTouchEvent.getY());
			}
			if (action == TouchEvent.ACTION_UP
					|| action == TouchEvent.ACTION_CANCEL) {
				handleTouchUpOrCalcel();
				weatherTouchUp(pSceneTouchEvent.getX(),
						pSceneTouchEvent.getY());
			}
		}

		return super.onTouch(pSceneTouchEvent, pTouchAreaLocalX,
				pTouchAreaLocalY);
	}

	/**
	 *  处理天气道具按下事件 
	 *  add by liufengqiang
	 */
	private void weatherTouchDown(float x, float y) {
		if (weatherProgress >= 7 && weatherProgress < 12) { //处理云雨道具事件 
			if (isContainCloud(x, y)) {
//				cloudCanBeMoved = true;
//				cloudProp.setPosition(cloudProp.getX(), cloudProp.getY() - 80);
			}
		}
		if (weatherProgress >= 12) { //处理闪电道具事件
			if (isContainFlashAim(x, y)) {
				flashCanBeMoved = true;
				flashAim.registerEntityModifier(new FadeInModifier(0.5f));
				flashAim.setPosition(cloudProp.getX(), cloudProp.getY() - 80);
			}
		}
	}
	
	/**
	 *  处理天气道具移动事件 
	 *  add by liufengqiang
	 */
	private void weatherTouchMove(float x, float y) {
		if (weatherProgress >= 7 && weatherProgress < 12) { //处理云雨道具事件
			if (cloudCanBeMoved) {
//				cloudProp.setCentrePosition(x, y - 80);
			}
		}
		if (weatherProgress >= 12) { //处理闪电道具事件
			if (flashCanBeMoved) {
				flashAim.setCentrePosition(x, y - 80);
			}
		}
	}

	/**
	 *  处理天气道具抬起事件 
	 *  add by liufengqiang
	 */
	private void weatherTouchUp(float x, float y) {
		if (weatherProgress >= 7 && weatherProgress < 12) { //处理云雨道具事件
//			if (isContainCloud(x, y - 80)) {
//				if (isContainStarArray(x, y - 80)) { // 当云雨道具移动到星星阵列时，消除
//				} 
//				// 否则，回原点
//				cloudCanBeMoved = false;
//				cloudProp.setCentrePosition(weatherContainer.getLeftX() + 57,
//						weatherContainer.getCentreY());
//			}
			if (isContainCloud(x, y)) {
				TCAgent.onEvent(activity, "使用云雨道具");
				doRemoveThreeLine();
				weatherProgress = 0;
				isChangeProgress = true;
			}
		}
		if (weatherProgress >= 12) { //处理闪电道具事件
			if (isContainFlashAim(x, y - 80)) {
				if (isContainStarArray(x, y - 80)) { //当在星星阵列的时候才去判断，否则抛异常
					int posX = getAimNearX(x);   //获取最近的星星的坐标
					int posY = getAimNearY(y - 80);
					if (starSigns[posX][posY] != GameConstants.BLOCK_NONE) { // 瞄准器在星星上时，消除
						System.out.println("X：" + posX + "---Y:" + posY);
						TCAgent.onEvent(activity, "使用闪电道具");
						SoundUtils.play1010PutFail();
						doRemoveOneStar(posX, posY);
						weatherProgress = 0;
						isChangeProgress = true;
					} 
				}
				flashCanBeMoved = false;
				flashAim.registerEntityModifier(new FadeOutModifier(0.5f));
				flashAim.setCentrePosition(whiteFlashProp.getCentreX(), whiteFlashProp.getCentreY());
			}
		}
	}
	
	// 检测按下区域是否在云雨道具范围内
	private boolean isContainCloud(float x, float y) {
		if (x < cloudProp.getLeftX() || x > cloudProp.getRightX()) {
			return false;
		}
		if (y < cloudProp.getTopY() || y > cloudProp.getBottomY()) {
			return false;
		}
		return true;
	}

	// 检测按下区域是否在瞄准器道具范围内
	private boolean isContainFlashAim(float x, float y) {
		if (x < flashAim.getLeftX() || x > flashAim.getRightX()) {
			return false;
		}
		if (y < flashAim.getTopY() || y > flashAim.getBottomY()) {
			return false;
		}
		return true;
	}
	
	// 随机消除一列星星
	private void doRemoveThreeLine() {
		SoundUtils.play1010(1);
		Random random = new Random();
		final int col = random.nextInt(10);
		
		rain_drop.setPositionX(GameConstants.OZ_PADDING_X + col
				* GameConstants.OZ_CONTAINER_SIZE.getWidth());
		FadeInModifier fadeIn = new FadeInModifier(0.5f, new IEntityModifierListener() {
			
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}
			
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				for (int j = 0; j < 10; j++) {
					starSprites[col][j]
							.registerEntityModifier(new ScaleModifier(
									0.3f, 1.0f, 0f, EaseBackIn
											.getInstance()));
					starSigns[col][j] = GameConstants.BLOCK_NONE;
				}
			}
		});
		FadeOutModifier fadeOut = new FadeOutModifier(1.5f);
		rain_drop.registerEntityModifier(new SequenceEntityModifier(fadeIn, fadeOut));
	}

	// 判断云雨道具是否移出星星阵列
	private boolean isContainStarArray(float x, float y) {
		if (x < GameConstants.OZ_PADDING_X
				|| x > GameConstants.OZ_PADDING_X
						+ GameConstants.OZ_CONTAINER_SIZE.getWidth() * 10) {
			return false;
		}
		if (y < GameConstants.OZ_CONTAINER_START_Y
				|| y > GameConstants.OZ_CONTAINER_START_Y
						+ GameConstants.OZ_CONTAINER_SIZE.getHeight() * 10) {
			return false;
		}
		return true;
	}

	// 消除指定瞄准器上的星星
	private void doRemoveOneStar(int row, int col) {
		System.out.println("星星的状态标志："+starSigns[row][col]);
		PopParticle particle = OZExplodeParticleMaker.make(vertexBufferObjectManager,
				starSprites[row][col].getCentreX(),
				starSprites[row][col].getCentreY(), starSigns[row][col]);
		particle.setCentrePositionX(starSprites[row][col].getCentreX());
		particle.setCentrePositionY(starSprites[row][col].getCentreY());
		attachChild(particle);
		particleList.add(particle);
		
		starSprites[row][col].registerEntityModifier(new ScaleModifier(0.3f, 1.0f, 0f,
				EaseBackIn.getInstance()));
		starSigns[row][col] = GameConstants.BLOCK_NONE;
	}
	
	// 计算瞄准器准心最近的星星的X位置
	private int getAimNearX(float x) {
		float padding = x - GameConstants.OZ_PADDING_X;
		return (int) (padding / GameConstants.OZ_CONTAINER_SIZE.getWidth());
	}
	
	// 计算瞄准器准心最近的星星的Y位置
	private int getAimNearY(float y) {
		float padding = y - GameConstants.OZ_CONTAINER_START_Y;
		return (int) (padding / GameConstants.OZ_CONTAINER_SIZE.getHeight());
	}

	public void doUpdate(float elapsedSeconds) {
		updateVisualScoreShow(elapsedSeconds);
		// 更新天气道具
		updateWeatherProp();
		// 更新小点点进度
		showAndRefreshPointProgress();
		updatePopParticleList(elapsedSeconds);
	}
	
	private void updatePopParticleList(float delta) {
		for (int i = 0; i < particleList.size(); i++) {
			PopParticle particle = particleList.get(i);
			particle.addDelta(delta);
			if (particle.isExpire()) {
				particle.detachSelf();
				particle.dispose();
				particleList.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * 显示&刷新小点点进度
	 * add by liufengqiang
	 */
	private void showAndRefreshPointProgress() {
		if (pointLists == null) { // 说明还未初始化
			createPointProp();
		} else {  // 已经初始化了
			if (isPlayGame) {
				if (isChangeProgress || isContinueGame) {
					for (Sprite sprite : pointLists) {
						sprite.detachSelf();
					}
					createPointProp();
					isChangeProgress = false;
				}
			}
		}
	}

	// 创建小点点进度
	private void createPointProp() {
		float radiusX = 43;
		float radiusY = radiusX * GameConstants.screenScale();
		pointLists = new ArrayList<Sprite>();
		for (int i = 0; i < weatherProgress; i++) {
			Sprite whitePoint = SpriteMaker.makeSpriteWithSingleImageFile(
					"white_point", vertexBufferObjectManager);
			
			float pointX = (float) (blackFlashProp.getCentreX() - radiusX
					* Math.sin(30 * i * Math.PI / 180));
			float pointY = (float) (blackFlashProp.getCentreY() - radiusY
					* Math.cos(30 * i * Math.PI / 180));
			whitePoint.setCentrePosition(pointX, pointY) ;
			
			whitePoint.setScaleY(GameConstants.screenScale());
			whitePoint.setScale(0.5f);
			pointLists.add(whitePoint);
			attachChild(whitePoint);
		}
	}

	private void updateWeatherProp() {
		if (isPlayGame) {
			if (isChangeProgress) {
				SPUtils.setPointProgress(activity, weatherProgress);
				flashAim.detachSelf();
				if (weatherProgress < 7) {
					blackFlashProp.setVisible(true);
					cloudProp.setVisible(false);
					whiteFlashProp.setVisible(false);
				} else if (weatherProgress < 12) {
					blackFlashProp.setVisible(false);
					cloudProp.setVisible(true);
					whiteFlashProp.setVisible(false);
				} else {
					blackFlashProp.setVisible(false);
					cloudProp.setVisible(false);
					whiteFlashProp.setVisible(true);
					attachChild(flashAim);
				}
			}
		}
	}

	public void updateVisualScoreShow(float delta) {
		int desScoreInt = DataProtecterUtils.desStringToInt(savedScore);
		float visualScoreFloat = DataProtecterUtils
				.desStringToFloat(visualScore);
		if (desScoreInt == 0) {
			visualScoreFloat = 0;
			visualScore = DataProtecterUtils.floatToDesString(visualScoreFloat);
		} else {
			float step = (desScoreInt - visualScoreFloat) * 0.04f;
			if (Math.abs(step) < 0.05f) {
				step = 0.05f * ((step > 0) ? 1 : -1);
			}
			visualScoreFloat += step;
			visualScore = DataProtecterUtils.floatToDesString(visualScoreFloat);
			if (Math.abs(desScoreInt - visualScoreFloat) < 0.1f) {
				visualScoreFloat = desScoreInt;
				visualScore = DataProtecterUtils
						.floatToDesString(visualScoreFloat);
			}
		}
		
		// 分数显示模式  add by liufengqiang
		if (visualScoreFloat > SPUtils.get1010HighScore(activity)) {
			currentScoreLabel.setScale(1.15f);
			highScoreLabel.setScale(0.95f);
			if (isChangeScoreAnim && SPUtils.get1010HighScore(activity) != 0) {
				displayScoreAnim();
			}
		} else if (visualScoreFloat < SPUtils.get1010HighScore(activity)) {
			currentScoreLabel.setScale(0.95f);
			highScoreLabel.setScale(1.15f);
		} else {
			currentScoreLabel.setScale(1.0f);
			highScoreLabel.setScale(1.0f);
		}

		currentScoreLabel.setText(String.valueOf((int) (visualScoreFloat)));
		currentScoreLabel.setRightPositionX(251);
	}

	//显示分数动画
	private void displayScoreAnim() {
		final IEntityModifierListener listener = new IEntityModifierListener() {
			
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}
			
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				beyond.setVisible(false);
				beyond.setCentrePosition(mainScene.getCentreX(), mainScene.getCentreY());
			}
		};
		FadeInModifier fadeIn = new FadeInModifier(0.1f);
		FadeOutModifier fadeOut = new FadeOutModifier(0.1f);
		LoopEntityModifier loop = new LoopEntityModifier(
				new SequenceEntityModifier(fadeOut, fadeIn), 6);
		currentScoreLabel.registerEntityModifier(loop);
		
		beyond.setVisible(true);
		MoveModifier move = new MoveModifier(0.5f, beyond.getX(), beyond.getX(),
				beyond.getY(), -beyond.getHeight());
		beyond.registerEntityModifier(new SequenceEntityModifier(listener,
				loop, new DelayModifier(0.8f) ,move));
		isChangeScoreAnim = false;
	}

	public void buyback() {
		buyDialog
		.setDialogDismissListener(new DialogDismissListener() {
			public void onDialogDismiss() {
				ResourceManager
						.unloadBuyDialogTextures();
				if (buyType == BUY_TYPE.REFRESH) {
					doRefresh(true, true);
				}
				if (buyType == BUY_TYPE.CONTINUE) {
					doContinueGame();
				}
				if (buyType == BUY_TYPE.ONE_BLOCK) {
					handleTouchUpOrCalcel();
				}
				if (buyType == BUY_TYPE.QUICK_FORCE_SHOW) {
					SPUtils.setQuickBuyDialogShowed(
							activity, true);
				}
			}
		});
buyDialog.dismissWithAnimamtion();
	}
}
