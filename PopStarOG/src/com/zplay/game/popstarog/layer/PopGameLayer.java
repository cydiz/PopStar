package com.zplay.game.popstarog.layer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.TypedValue;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.e7studio.android.e7appsdk.utils.InstalledAppInfoHandler;
import com.e7studio.android.e7appsdk.utils.JSONBuilder;
import com.e7studio.android.e7appsdk.utils.LogUtils;
import com.e7studio.android.e7appsdk.utils.MapBuilder;
import com.e7studio.android.e7appsdk.utils.WebParamsMapBuilder;
import com.e7studio.android.e7appsdk.utils.WebTask;
import com.e7studio.android.e7appsdk.utils.WebTaskHandler;
import com.orange.content.SceneBundle;
import com.orange.engine.handler.IUpdateHandler;
import com.orange.entity.Entity;
import com.orange.entity.IEntity;
import com.orange.entity.layer.Layer;
import com.orange.entity.modifier.ColorModifier;
import com.orange.entity.modifier.DelayModifier;
import com.orange.entity.modifier.FadeInModifier;
import com.orange.entity.modifier.FadeOutModifier;
import com.orange.entity.modifier.LoopEntityModifier;
import com.orange.entity.modifier.MoveByModifier;
import com.orange.entity.modifier.MoveModifier;
import com.orange.entity.modifier.ParallelEntityModifier;
import com.orange.entity.modifier.RotationByModifier;
import com.orange.entity.modifier.ScaleModifier;
import com.orange.entity.modifier.SequenceEntityModifier;
import com.orange.entity.sprite.AnimatedSprite;
import com.orange.entity.sprite.ButtonSprite;
import com.orange.entity.sprite.ButtonSprite.OnClickListener;
import com.orange.entity.sprite.Sprite;
import com.orange.entity.text.Text;
import com.orange.input.touch.TouchEvent;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.res.RegionRes;
import com.orange.util.HorizontalAlign;
import com.orange.util.color.Color;
import com.orange.util.modifier.IModifier;
import com.orange.util.modifier.IModifier.IModifierListener;
import com.orange.util.modifier.ease.EaseBackIn;
import com.orange.util.modifier.ease.EaseBounceOut;
import com.orange.util.modifier.ease.EaseExponentialInOut;
import com.orange.util.modifier.ease.EaseExponentialOut;
import com.tendcloud.tenddata.TCAgent;
import com.zplay.game.popstarog.PopStar;
import com.zplay.game.popstarog.custom.ClickThroughAbsoluteLayout;
import com.zplay.game.popstarog.custom.Dialog;
import com.zplay.game.popstarog.custom.DialogDismissListener;
import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.particle.PopParticle;
import com.zplay.game.popstarog.particle.PopParticleMaker;
import com.zplay.game.popstarog.pay.PayCallback;
import com.zplay.game.popstarog.scene.PopScene;
import com.zplay.game.popstarog.scene.ShopScene;
import com.zplay.game.popstarog.sprite.Position;
import com.zplay.game.popstarog.sprite.StarSprite;
import com.zplay.game.popstarog.sprite.StarSprite.TouchCallback;
import com.zplay.game.popstarog.utils.BlinkModifierMaker;
import com.zplay.game.popstarog.utils.ButtonMaker;
import com.zplay.game.popstarog.utils.ConfigValueHandler;
import com.zplay.game.popstarog.utils.Encrypter;
import com.zplay.game.popstarog.utils.ExploadeTrackInform;
import com.zplay.game.popstarog.utils.HammerBestPositionFinder;
import com.zplay.game.popstarog.utils.IntegerContainer;
import com.zplay.game.popstarog.utils.MathUtils;
import com.zplay.game.popstarog.utils.ResourceManager;
import com.zplay.game.popstarog.utils.RollBackStarsCalculator;
import com.zplay.game.popstarog.utils.RollbackInfo;
import com.zplay.game.popstarog.utils.SPUtils;
import com.zplay.game.popstarog.utils.SizeHelper;
import com.zplay.game.popstarog.utils.SoundUtils;
import com.zplay.game.popstarog.utils.SpriteMaker;
import com.zplay.game.popstarog.utils.TextMaker;
import com.zplay.game.popstarog.utils.TrackMove;
import com.zplay.game.popstarog.utils.TrackMove.TrackMoveType;
import com.zplay.game.popstarog.utils.Utils;
import com.zplay.game.popstarog.utils.sp.PhoneInfoGetter;

@SuppressWarnings("deprecation")
@SuppressLint({ "UseSparseArrays", "DefaultLocale" })
public class PopGameLayer extends Layer {
	private final static int QUICK_BUY_NUM = 118;

	private final static String DES_KEY = GameConstants.DES_KEY;
	private float updateDelta = 0f;

	private enum BUY_TYPE {
		REBORN, HAMMER, SWITCH, ROLL_BACK, NONE, STAGE_6, END_SWITCH;
	};

	private final static String TAG = "PopGameLayer";
	private final static int MAX_LINE = 10;

	// 星星下落速度
	private final static float DURATION = 0.20f;
	// // 横向移动速度
	// private final static float HORIZONTAL_SPEED = 320f;
	// 横向移动时间
	private final static float HORIZONTAL_DURATION = 0.25f;

	// 移动的关卡、目标分数
	private Text moveStageLabel;
	private Text moveScoreLabel;

	// 关卡
	private Text stageLabel;
	// 目标
	private Text clearScoreLabel;
	// 当前得分
	private Text currentScoreLabel;
	// 星星
	private StarSprite[][] starSprites;
	// 星星外边的框
	private Sprite[][] starSpriteBorders;

	// 五种星星的type样本
	private List<Integer> sampleTypeList;
	// 星星的标示
	private int[][] starSigns;

	private Random random;
	private Activity activity;
	private PopScene popScene;
	private Text scoreTipsLabel;
	private TouchCallback touchCallback;
	// 被选中的星星的坐标的集合
	private List<Position> selectedStarList;
	// 所有的星星是静止的
	private boolean isStarsStatic = false;

	private int currentScoreLabelX;
	private int currentScoreLabelY;

	// 因为八门神器可以修改内存中的数据，所以，所有敏感的实例变量都作为String并作des加密

	// int
	private int stage = 0;
	// float
	private String visualScore = floatToDesString(0);
	// long
	private String stageClearScore = longToDesString(0);
	// int
	private String desScore = intToDesString(0);

	// int
	private int savedStage = 0;
	// int
	private String savedScore = intToDesString(0);

	// stageClear是否已经显示过了
	private boolean isStageClearShown = false;
	private boolean isStageClearShowEnabled = false;

	private Text bonusScoreLabel;
	private Text remainNumLabel;

	private int rebornNum = 1;

	private Sprite whiteSprite;

	private Sprite stageClearSprite;

	private ButtonSprite optionsBtn;
	// private ButtonSprite autoBtn;
	private ButtonSprite switchBtn;
	private ButtonSprite backSpaceBtn;
	private ButtonSprite hammerBtn;
	private ButtonSprite luckyStarBuyBtn;
	private Text starNumLabel;

	private boolean isInHammerState = false;
	private Text hammerTips;
	private AnimatedSprite hammerSprite;

	// 因为最后做blink时候因为通过jni方式调用的action时间间隔太长了，虽然从java端来看，100个sprite执行blink的时间是2ms，但是实际时间比这要长一些，还需要
	private Layer starsLayer;

	private float borderWidth = 64;
	private float borderHeight = 64;

	private float starWidth = 63;
	private float starHeight = 63;

	// 关卡结束中
	private boolean isInResultState = false;

	// 因为微云的dialog有bug，每次dismiss/show有内存泄露，所以，这里只能做重用了
	private Dialog goonDialog;
	private Dialog buyDialog;

	private BUY_TYPE buyType;

	// 测试用
	private boolean isAutoClick = false;

	private List<Position> exploadeList = new ArrayList<Position>();
	private float exploadeDelta;
	private int exploadeIndex = 0;

	private List<Integer> emptyColumnList = new ArrayList<Integer>();

	private List<StarSprite> remainList = new ArrayList<StarSprite>();
	private int remainIndex = 0;
	private float remainDelta = 0;

	private boolean isBonusStep = false;
	private int bonusScore = 0;

	// OGengine的Text组件在每次setText之后执行setCentrePositionX位置会有细微的错位（因为0~9不同数字的宽度不同），这里忽略宽度的细微差别。只有位数改变时候才执行setCentrePositionX方法
	private int lastCurrentScoreLength = 0;

	private VertexBufferObjectManager vertextBufferObjectManager = getVertexBufferObjectManager();
	private Stack<TrackMove> trackmoveStack = new Stack<TrackMove>();
	private boolean isRollBackEnabled = false;

	// 是否有显示过道具使用提醒
	private boolean isItemUsedTriggerShowed = false;

	// 在结算之前，点击确定使用刷新道具，是否有弹出大礼包的对话框
	private boolean isQuickBuyDialogShowed = false;
	private boolean isSceneTouch = false;//锤子的状态下响应星星点击还是scene点击呢
	
	//爆炸粒子数量减半
	private boolean showParticle = true;
	
	/**
	 * 保存着所有的{@linkplain PopParticleMaker}
	 */
	private List<PopParticle> particleList = new ArrayList<PopParticle>();
	
	// 玩家当前玩过的关卡数
	private int stageNum = 1;
	
	public PopGameLayer(PopScene scene) {
		super(GameConstants.BASE_WIDTH, GameConstants.BASE_HEIGHT, scene);
		activity = getActivity();
		this.popScene = scene;
		borderHeight = borderWidth * 1.0f / GameConstants.BASE_WIDTH
				* GameConstants.screen_width / GameConstants.screen_height
				* GameConstants.BASE_HEIGHT;
		starHeight = starWidth * 1.0f / GameConstants.BASE_WIDTH
				* GameConstants.screen_width / GameConstants.screen_height
				* GameConstants.BASE_HEIGHT;
		LogUtils.v(TAG, "边框的高度：" + borderHeight + ",星星的高度：" + starHeight);
		random = new Random();
		starSprites = new StarSprite[10][10];
		starSpriteBorders = new Sprite[10][10];

		// 设置banner高度
		setBannerHeight();

		addScoreLabel();
		addMoveLabel();
		addScoreTips();
		addStageClear();
		addStarBtnAndLabel();

		initStarSignSampleList();
		initStars();

		addBonusScore();
		addWhiteBox();

		selectedStarList = new ArrayList<Position>();
		registerUpdateHandler(new IUpdateHandler() {
			public void reset() {
			}

			public void onUpdate(float pSecondsElapsed) {
				updateStuff(pSecondsElapsed);
			}
		});
	}

	private void setBannerHeight() {
		final RelativeLayout bannerLayout = (RelativeLayout) activity
				.findViewById(Utils.getResByID(activity, "adlayout", "id"));
		GameConstants.AD_BANNER_HEIGHT = borderHeight * 1.3f;
		activity.runOnUiThread(new Runnable() {
			public void run() {
				bannerLayout.getLayoutParams().height = (int) SizeHelper
						.yOGUnitToPixel(GameConstants.AD_BANNER_HEIGHT);
				LogUtils.v(TAG, "banner的高度：" + GameConstants.AD_BANNER_HEIGHT);
			}
		});

	}

	private String intToDesString(int num) {
		return Encrypter.doDESEncode(String.valueOf(num), DES_KEY);
	}

	private String floatToDesString(float num) {
		return Encrypter.doDESEncode(String.valueOf(num), DES_KEY);
	}

	private String longToDesString(long num) {
		return Encrypter.doDESEncode(String.valueOf(num), DES_KEY);
	}

	private int desStringToInt(String desedString) {
		return Integer.parseInt(Encrypter.doDESDecode(desedString, DES_KEY));
	}

	private float desStringToFloat(String desedString) {
		return Float.parseFloat(Encrypter.doDESDecode(desedString, DES_KEY));
	}

	private long desStringToLong(String desedString) {
		return Long.parseLong(Encrypter.doDESDecode(desedString, DES_KEY));
	}

	private void buildGoonDialog(int costNum) {
		ResourceManager.loadGoonDialogTextures();
		goonDialog = new Dialog(popScene);
		goonDialog.setSize(640, 960);
		Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile("jxtg_bg",
				vertextBufferObjectManager);
		bgSprite.setPosition(0, 183);
		goonDialog.attachChild(bgSprite);

		ButtonSprite quitBtn = ButtonMaker.makeFromSingleImgFile(563, 266,
				"options_quit", vertextBufferObjectManager);
		quitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				goonDialog.dismissWithAnimamtion();
			}
		});
		quitBtn.setPosition(577, 259);
		goonDialog.attachChild(quitBtn);

		// 继续通关按钮230
		ButtonSprite yellowBtn = ButtonMaker.makeFromSingleImgFile(320, 730,
				"yellow_btn_long", vertextBufferObjectManager);
		yellowBtn.setScale(0.9f);
		yellowBtn.setPosition(180, 687);
		yellowBtn.setCentrePositionX(320);
		yellowBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				goOn();//继续通关的按钮
			}
		});
		goonDialog.attachChild(yellowBtn);

		Sprite goonTextSprit = SpriteMaker.makeSpriteWithSingleImageFile(
				"jxtg", vertextBufferObjectManager);
		goonTextSprit.setScaleCenter(goonTextSprit.getCentreX(),
				goonTextSprit.getCentreY());
		goonTextSprit.setScale(0.9f);
		goonTextSprit.setCentrePosition(320, 735);
		goonDialog.attachChild(goonTextSprit);

		Text costTipsLabel = TextMaker.make(String.valueOf(costNum), "25white",
				320, 820, HorizontalAlign.CENTER, vertextBufferObjectManager);
		costTipsLabel.setCentrePosition(337, 661);
		goonDialog.attachChild(costTipsLabel);

		goonDialog.setDialogDismissListener(new DialogDismissListener() {
			public void onDialogDismiss() {
				TCAgent.onEvent(activity, "关闭继续通关");
				ResourceManager.unloadGoonDialogTextures();
				gameOver();
			}
		});
	}

	// 购买大礼包的按钮
	private void buildBuyDialog() {
		ResourceManager.loadBuyDialogTextures(activity);
		buyDialog = new Dialog(popScene);

		Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"quick_buy_bg", vertextBufferObjectManager);
		bgSprite.setPosition(0, 112);
		buyDialog.attachChild(bgSprite);

		ButtonSprite quitBtn = ButtonMaker.makeFromSingleImgFile(563, 266,
				"options_quit", vertextBufferObjectManager);
		quitBtn.setRightPositionX(640);
		quitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				buyDialog.dismissWithAnimamtion();
				((PopStar) activity).showPop();
			}
		});
		quitBtn.setScale(0.8f);
		buyDialog.attachChild(quitBtn);

		// 328/186  购买按钮
		ButtonSprite okBtn = ButtonMaker.makeFromSingleImgFile(320, 774,
				"quick_buy_btn_ok", vertextBufferObjectManager);
		okBtn.setPosition(330, 717);
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				quickBuy();
			}
		});
	

		//去商城按钮
		ButtonSprite goshopBtn = ButtonMaker.makeFromSingleImgFile(320, 774,
				"btn_go_shop", vertextBufferObjectManager);
		goshopBtn.setPosition(65, 717);

		if (SPUtils.getIsShowBtShop(activity)) {
			buyDialog.attachChild(goshopBtn);
		} else {
			okBtn.setPosition(205, 717);
		}

		goshopBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				showShop_big();
			}
		});

		buyDialog.attachChild(okBtn);
		buyDialog.setDialogDismissListener(new DialogDismissListener() {
			public void onDialogDismiss() {
				ResourceManager.unloadBuyDialogTextures();
				if (buyType == BUY_TYPE.REBORN) {
					gameOver();
				}
				if (buyType == BUY_TYPE.HAMMER || buyType == BUY_TYPE.SWITCH
						|| buyType == BUY_TYPE.END_SWITCH) {
					isQuickBuyDialogShowed = true;
					checkIfNoBlockPopableAndDoStuff();
				}
				if (buyType == BUY_TYPE.NONE) {
					popScene.onSceneResume();
				}
				if (buyType == BUY_TYPE.STAGE_6) {
					int costNum = GameConstants.getRebornCost(stage, rebornNum);
					buildGoonDialog(costNum);
					goonDialog.show();
				}
			}
		});
	}

	public boolean isInResultState() {
		return isInResultState;
	}

	public void setInResultState(boolean isInResultState) {
		this.isInResultState = isInResultState;
	}

	public boolean isInHammerState() {
		return isInHammerState;
	}

	public void setInHammerState(boolean isInHammerState) {
		this.isInHammerState = isInHammerState;
	}

	public void removeNode(Entity node) {
		node.detachSelf();
		node.dispose();
	}

	public void removeNodeWithTextures(Entity node, String textureRegionName) {
		node.detachSelf();
		node.dispose();
		RegionRes.getTextureRegion(textureRegionName).getTexture().unload();
	}

	private void addStarBtnAndLabel() {
		luckyStarBuyBtn = ButtonMaker.makeFromSingleImgFile(640, 40,
				"star_display", vertextBufferObjectManager);
		luckyStarBuyBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				showShop();
			}
		});
		luckyStarBuyBtn.setRightPositionX(640);
		luckyStarBuyBtn.setVisible(false);
		attachChild(luckyStarBuyBtn);

		starNumLabel = TextMaker.make("1234567890", "20white", 580, 45,
				HorizontalAlign.RIGHT, vertextBufferObjectManager);
		starNumLabel.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		starNumLabel.setRightPositionX(580);
		starNumLabel.setVisible(false);
		starNumLabel.setColor(171 * 1.0f / 255, 37 * 1.0f / 255, 0);
		attachChild(starNumLabel);

		optionsBtn = ButtonMaker.makeFromSingleImgFile(34, 157,
				"option_button", vertextBufferObjectManager);
		optionsBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				((PopStar) activity).showPop();
				((PopStar) activity).hideBanner();
				showOptions();
//				showStarGifDialog();
			}
		});
		optionsBtn.setLeftPositionX(34);
		optionsBtn.setVisible(false);
		attachChild(optionsBtn);

		// autoBtn = ButtonMaker.makeFromSingleImgFile(100, 157, "auto_click",
		// vertextBufferObjectManager);
		// autoBtn.setOnClickListener(new OnClickListener() {
		// public void onClick(ButtonSprite pButtonSprite,
		// float pTouchAreaLocalX, float pTouchAreaLocalY) {
		// autoClickToggle();
		// }
		// });
		// autoBtn.setPositionX(100);
		// autoBtn.setVisible(false);
		// attachChild(autoBtn);

		switchBtn = ButtonMaker.makeFromSingleImgFile(505, 157, "switch",
				vertextBufferObjectManager);
		switchBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				showSwitch();
			}
		});
		switchBtn.setVisible(false);
		attachChild(switchBtn);

		switchBtn.setScale(0.75f);
		switchBtn.setCentrePosition(505, 157);

		backSpaceBtn = ButtonMaker.makeFromSingleImgFile(440, 157, "rollback",
				vertextBufferObjectManager);
		backSpaceBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				showRollBack();
			}
		});
		backSpaceBtn.setVisible(false);
		attachChild(backSpaceBtn);

		backSpaceBtn.setScale(0.75f);
		backSpaceBtn.setCentrePosition(440, 157);

		hammerBtn = ButtonMaker.makeFromSingleImgFile(619, 157, "btn_hammer",
				vertextBufferObjectManager);
		hammerBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				showHammer();
			}
		});
		hammerBtn.setVisible(false);
		attachChild(hammerBtn);

		hammerBtn.setScale(0.75f);
		hammerBtn.setRightPositionX(619);
		hammerBtn.setCentrePositionY(157);
	}

	public void showShop() {
		TCAgent.onEvent(activity, "点击星星商城");

		SoundUtils.playButtonClick();

		SceneBundle bundle = new SceneBundle();
		bundle.putBooleanExtra("fromPop", true);
		popScene.startScene(ShopScene.class, bundle);
	}

	public void showShop_big() {
		TCAgent.onEvent(activity, "点击星星商城");

		SoundUtils.playButtonClick();

		SceneBundle bundle = new SceneBundle();
		bundle.putBooleanExtra("Frompop_big", true);
		// popScene.startScene(ShopScene.class, bundle);
		popScene.startSceneForResult(ShopScene.class, bundle, 20);
	}

	public void doAddHammer() {
		int cost = GameConstants.getHammerCost(SPUtils
				.getUsedHammerNum(activity));
		clearCurrentSelectedStarsState();
		isInHammerState = true;
		optionsBtn.setEnabled(false);
		luckyStarBuyBtn.setEnabled(false);
		// autoBtn.setEnabled(false);
		switchBtn.setEnabled(false);
		backSpaceBtn.setEnabled(false);

		ScaleModifier scaleTo = new ScaleModifier(1.0f, 0.75f, 1f);
		ScaleModifier scaleToReverse = new ScaleModifier(1.0f, 1f, 0.75f);
		SequenceEntityModifier sequence = new SequenceEntityModifier(scaleTo,
				scaleToReverse);
		hammerBtn.registerEntityModifier(new LoopEntityModifier(sequence));
		hammerTips = TextMaker.make(String.format("本次使用此道具将消耗%d个幸运星", cost),
				"30white", 320, 280, HorizontalAlign.CENTER,
				vertextBufferObjectManager);
		hammerTips.setPositionY(280 - hammerTips.getHeight() / 2);
		attachChild(hammerTips);

		hammerSprite = new AnimatedSprite(0, 0, RegionRes.getRegion("hammer"),
				vertextBufferObjectManager);
		hammerSprite.animate(100);
		Position hammerPostion = getBenifitedHammerPosition();
		if (hammerPostion == null) {
			hammerPostion = new Position(0, 0);
		}
		int hammerX = hammerPostion.getX();
		int hammerY = hammerPostion.getY();
		hammerSprite.setPosition(starSprites[hammerX][hammerY].getX() + 32,
				starSprites[hammerX][hammerY].getY());
		if (hammerX > 5) {
			hammerSprite.setFlippedHorizontal(true);
			hammerSprite.setPosition(starSprites[hammerX][hammerY].getX() + 32,
					starSprites[hammerX][hammerY].getY()
							- GameConstants.AD_BANNER_HEIGHT);
		} else {
			hammerSprite.setFlippedHorizontal(false);
			hammerSprite.setPosition(starSprites[hammerX][hammerY].getX() + 32,
					starSprites[hammerX][hammerY].getY()
							- GameConstants.AD_BANNER_HEIGHT);
		}
		attachChild(hammerSprite);
		handleStarTouch(starSprites[hammerX][hammerY].getType(), hammerX,
				hammerY, false);
	}

	/**
	 * 显示幸运星不足提示界面
	 */
	private void showLuckyStarNotEnoughDialog_delete(BUY_TYPE buyType) {
		final BUY_TYPE buy_type = buyType;
		ResourceManager.loadLuckyStarNotEnoughDialog(activity);
		final Dialog dialog = new Dialog(popScene);
		dialog.setSize(GameConstants.BASE_WIDTH, GameConstants.BASE_HEIGHT);

		// 背景图
		Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"common_bg_ne", vertextBufferObjectManager);
		bgSprite.setPosition(31, 165);
		bgSprite.setScale(0.8f);
		dialog.attachChild(bgSprite);

		float gapV = 15.0f;

		// 标题
		Text title = TextMaker.make("哎呀！幸运星不够了", "40white",
				bgSprite.getCentreX(),
				bgSprite.getCentreY() - 200f * bgSprite.getScaleY(),
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		dialog.attachChild(title);

		// 退出按钮
		ButtonSprite quitBtn = ButtonMaker.makeFromSingleImgFile(563, 266,
				"quit_ne", vertextBufferObjectManager);
		quitBtn.setCentrePositionX(bgSprite.getCentreX()
				+ bgSprite.getWidthHalf() * bgSprite.getScaleX() - 20);
		quitBtn.setCentrePositionY(bgSprite.getCentreY()
				- bgSprite.getHeightHalf() * bgSprite.getScaleY() + 10);
		quitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				dialog.dismissWithAnimamtion();
				 checkIfNoBlockPopableAndDoStuff();
				if (isDead()) {
					gameOver();// 此时点击退出应该显示的是退出游戏

				} else {
					checkIfNoBlockPopableAndDoStuff();
				}
			}
		});
		quitBtn.setScale(0.8f);
		dialog.attachChild(quitBtn);

		// 哭泣的星星
		Sprite cryStarSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"starCry_ne", vertextBufferObjectManager);
		cryStarSprite.setScale(0.6f);
		cryStarSprite.setCentrePositionX(GameConstants.BASE_WIDTH / 2);
		cryStarSprite.setCentrePositionY(bgSprite.getCentreY() - gapV);
		dialog.attachChild(cryStarSprite);

		// 商城购买说明
		gapV = 0.0f;
		Text tipsLabel = TextMaker.make("点击按钮购买大礼包补充幸运星\n您也可以从右上角进入商城购买 ",
				"25white_ne", cryStarSprite.getCentreX(),
				cryStarSprite.getBottomY() + gapV, HorizontalAlign.CENTER,
				vertextBufferObjectManager);
		dialog.attachChild(tipsLabel);

		// 购买按钮
		gapV = 8.0f;
		ButtonSprite okBtn = ButtonMaker.makeFromSingleImgFile(320, 730,
				"yellow_btn_long_ne", vertextBufferObjectManager);
		okBtn.setScale(0.85f);
		okBtn.setCentrePositionX(GameConstants.BASE_WIDTH / 2);
		okBtn.setBottomPositionY(bgSprite.getCentreY()
				+ bgSprite.getHeightHalf() * bgSprite.getScaleY() - gapV);
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				dialog.dismissWithoutAnimation();
				showQuickBuyDialog(buy_type);
			}
		});
		dialog.attachChild(okBtn);

		Text oKText = TextMaker.make("现在就购买", "50white", 320, 705,
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		oKText.setCentrePosition(okBtn.getCentreX(), okBtn.getCentreY());
		dialog.attachChild(oKText);

		dialog.setDialogDismissListener(new DialogDismissListener() {
			public void onDialogDismiss() {
				ResourceManager.unloadLuckyStarNotEnoughDialog();
			}
		});
		dialog.showWithAnimation();
	}

	public void showSwitch() {
		if (isStarsStatic) {
			int cost = GameConstants.getSwitchCost(SPUtils
					.getSwitchUsedNum(activity));
			int starNum = (int) SPUtils.getLuckStarNum(activity);
			if (cost > starNum) {
				/*
				 * if (SPUtils.isShowLuckyStarNotEnoughDialog(activity)) { //
				 * showLuckyStarNotEnoughDialog(BUY_TYPE.SWITCH);去掉
				 * buildBuyDialog(); } else {
				 * showQuickBuyDialog(BUY_TYPE.SWITCH); }
				 */
				LogUtils.v(TAG, "想要使用转换但是钱不够，展示快捷购买对话框...");
				showQuickBuyDialog(BUY_TYPE.SWITCH);
			} else {
				doSwitch();
			}
		}
	}

	// 结算之前的使用刷新道具的提示
	public void showEndSwitch() {
		if (isStarsStatic) {
			int cost = GameConstants.getSwitchCost(SPUtils
					.getSwitchUsedNum(activity));
			int starNum = (int) SPUtils.getLuckStarNum(activity);
			if (cost > starNum) {
				// if (SPUtils.isShowLuckyStarNotEnoughDialog(activity)) {
				// showLuckyStarNotEnoughDialog(BUY_TYPE.END_SWITCH);
				// 去掉哎呀星星不够了页面
				// } else {
				// LogUtils.v(TAG, "想要使用转换但是钱不够，展示快捷购买对话框...");
				// showQuickBuyDialog(BUY_TYPE.END_SWITCH);
				// }
				showQuickBuyDialog(BUY_TYPE.END_SWITCH);
			} else {
				doSwitch();
			}
		}
	}

	// 进行转换
	private void doSwitch() {

		TCAgent.onEvent(activity, "使用重排道具");

		isStarsStatic = false;
		int cost = GameConstants.getSwitchCost(SPUtils
				.getSwitchUsedNum(activity));
		// 加入使用幸运星的TD统计 add by lvjibin
		TCAgent.onEvent(activity, "此次重排道具使用" + cost + "颗幸运星");
		final Text switchTips = TextMaker.make(
				String.format("本次使用此道具消耗%d个幸运星", cost), "30white", 320, 280,
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		switchTips.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		FadeOutModifier fadeOut = new FadeOutModifier(3.0f);
		switchTips.registerEntityModifier(fadeOut);
		fadeOut.addModifierListener(new IModifierListener<IEntity>() {
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
			}

			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				removeNode(switchTips);
			}
		});
		attachChild(switchTips);

		int starNum = (int) SPUtils.getLuckStarNum(activity);
		SPUtils.saveLuckStarNum(activity, starNum - cost);
		SPUtils.saveSwitchUsedNum(activity,
				SPUtils.getSwitchUsedNum(activity) + 1);
		starNumLabel.setText(String.valueOf(starNum - cost));
		starNumLabel.setRightPositionX(580);

		final List<Position> originalSpriteList = new ArrayList<Position>();
		final List<Position> randomSpritelist = new ArrayList<Position>();
		List<StarSprite> showedList = new ArrayList<StarSprite>();

		for (int i = 0; i < MAX_LINE; i++) {
			for (int j = 0; j < MAX_LINE; j++) {
				if (starSprites[i][j].getType() != GameConstants.STAR_NONE) {
					Position position = new Position(i, j);
					originalSpriteList.add(position);
					randomSpritelist.add(position);
					showedList.add(starSprites[i][j]);
				}
			}
		}
		if (originalSpriteList.size() == 0) {
			isStarsStatic = true;
		} else {
			randomListOrder(randomSpritelist);

			final int totalNums = originalSpriteList.size();
			final IntegerContainer container = new IntegerContainer(0);
			for (int i = 0; i < totalNums; i++) {
				Position startPosition = originalSpriteList.get(i);
				int index = randomSpritelist.indexOf(startPosition);
				Position desPosition = originalSpriteList.get(index);
				StarSprite starSprite = showedList.get(i);

				MoveModifier modifier = new MoveModifier(1.0f, borderWidth / 2
						+ startPosition.getX() * borderWidth - starWidth / 2,
						borderWidth / 2 + desPosition.getX() * borderWidth
								- starWidth / 2, 960 - borderHeight
								* (startPosition.getY() + 1)
								+ (borderHeight - starHeight) / 2
								- GameConstants.AD_BANNER_HEIGHT, 960
								- borderHeight * (desPosition.getY() + 1)
								+ (borderHeight - starHeight) / 2
								- GameConstants.AD_BANNER_HEIGHT,
						EaseBackIn.getInstance());

				starSprite.registerEntityModifier(modifier);
				modifier.addModifierListener(new IModifierListener<IEntity>() {
					public void onModifierStarted(IModifier<IEntity> pModifier,
							IEntity pItem) {
					}

					public void onModifierFinished(
							IModifier<IEntity> pModifier, IEntity pItem) {
						container.add();
						if (container.getValue() == totalNums) {
							trackmoveStack.push(new TrackMove(
									TrackMoveType.SWITCH, originalSpriteList,
									randomSpritelist, null, null));
							// TODO 使能回退道具
							if (!isRollBackEnabled) {
								showEnableRollbackBtn();
							}
							isStarsStatic = true;
							checkIfNoBlockPopableAndDoStuff();
						}
					}
				});
				starSprites[desPosition.getX()][desPosition.getY()] = starSprite;
				starSigns[desPosition.getX()][desPosition.getY()] = starSprite
						.getType();
				starSprite.setIndexXY(desPosition.getX(), desPosition.getY());
			}
		}

	}

	private void showEnableRollbackBtn() {
		isRollBackEnabled = true;
		backSpaceBtn.clearEntityModifiers();
		ColorModifier colorModifier = new ColorModifier(1.0f, new Color(0.5f,
				0.5f, 0.5f), new Color(1.0f, 1.0f, 1.0f));
		backSpaceBtn.registerEntityModifier(colorModifier);
	}

	private void showDisableRollbackBtn() {
		isRollBackEnabled = false;
		backSpaceBtn.clearEntityModifiers();
		ColorModifier colorModifier = new ColorModifier(1.0f, new Color(1.0f,
				1.0f, 1.0f), new Color(0.5f, 0.5f, 0.5f));
		backSpaceBtn.registerEntityModifier(colorModifier);
	}

	// 点击使用回滚道具
	public void showRollBack() {
		if (isStarsStatic) {
			if (trackmoveStack.size() > 0) {
				int cost = 5;
				int starNum = (int) SPUtils.getLuckStarNum(activity);
				if (cost > starNum) {
					/*
					 * if (SPUtils.isShowLuckyStarNotEnoughDialog(activity)) {
					 * showLuckyStarNotEnoughDialog(BUY_TYPE.ROLL_BACK); } else
					 * { LogUtils.v(TAG, "想要使用回滚但是钱不够，展示快捷购买对话框...");
					 * showQuickBuyDialog(BUY_TYPE.ROLL_BACK); }
					 */
					showQuickBuyDialog(BUY_TYPE.ROLL_BACK);
				} else {
					doRollBack();
				}
			} else {
				System.out.println("rollBack disable, no move record...");
			}

		}
	}

	private void verticalExploadeRollback(List<RollbackInfo> verticalList,
			final TrackMove trackMove) {
		final IntegerContainer container = new IntegerContainer(0);
		final int verticalTotal = verticalList.size();
		for (int i = 0; i < verticalList.size(); i++) {
			RollbackInfo info = verticalList.get(i);
			MoveByModifier move = new MoveByModifier(DURATION, 0,
					-info.getStep() * borderHeight);
			move.addModifierListener(new IModifierListener<IEntity>() {
				public void onModifierStarted(IModifier<IEntity> pModifier,
						IEntity pItem) {
				}

				public void onModifierFinished(IModifier<IEntity> pModifier,
						IEntity pItem) {
					container.add();
					if (container.getValue() == verticalTotal) {
						afterExploadeRollBackMove(trackMove);
					}
				}
			});
			info.getStarSprite().registerEntityModifier(move);
		}

	}

	// 上一步中消除的星星scale动作出来
	private void afterExploadeRollBackMove(TrackMove trackMove) {
		List<ExploadeTrackInform> exploadeTrackFormList = trackMove
				.getExploadeSpriteList();
		final int total = exploadeTrackFormList.size();
		final IntegerContainer container = new IntegerContainer(0);
		for (int i = 0; i < exploadeTrackFormList.size(); i++) {
			ExploadeTrackInform infor = exploadeTrackFormList.get(i);
			starSprites[infor.getX()][infor.getY()].setScale(0);
			starSprites[infor.getX()][infor.getY()].setVisible(true);
			starSprites[infor.getX()][infor.getY()].setCurrentTileIndex(infor
					.getType());
			starSprites[infor.getX()][infor.getY()].setIgnoreTouch(false);
			starSprites[infor.getX()][infor.getY()].setCustomAttributes(
					infor.getType(), infor.getX(), infor.getY(), false);

			starSprites[infor.getX()][infor.getY()].setScaleCenter(
					starWidth / 2, starHeight / 2);
			starSprites[infor.getX()][infor.getY()].setPosition(borderWidth / 2
					+ infor.getX() * borderWidth - starWidth / 2, 960
					- borderHeight * (infor.getY() + 1)
					+ (borderHeight - starHeight) / 2
					- GameConstants.AD_BANNER_HEIGHT);

			ScaleModifier scale = new ScaleModifier(0.5f, 0, 1,
					EaseBounceOut.getInstance());
			scale.addModifierListener(new IModifierListener<IEntity>() {
				public void onModifierStarted(IModifier<IEntity> pModifier,
						IEntity pItem) {
				}

				public void onModifierFinished(IModifier<IEntity> pModifier,
						IEntity pItem) {
					container.add();
					if (container.getValue() == total) {
						isStarsStatic = true;
					}
				}
			});
			starSprites[infor.getX()][infor.getY()]
					.registerEntityModifier(scale);
		}
	}

	// 进行操作回滚
	private void doRollBack() {

		TCAgent.onEvent(activity, "使用撤销道具");

		isStarsStatic = false;
		final TrackMove trackMove = trackmoveStack.pop();
		if (trackmoveStack.isEmpty()) {
			showDisableRollbackBtn();
		}
		int cost = 5;
		final Text rollBackTips = TextMaker.make(
				String.format("本次使用此道具消耗%d个幸运星", cost), "30white", 320, 280,
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		// 加入使用幸运星的TD统计 add by lvjibin
		TCAgent.onEvent(activity, "本次撤销道具使用" + cost + "颗幸运星");
		rollBackTips.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		FadeOutModifier fadeOut = new FadeOutModifier(3.0f);
		rollBackTips.registerEntityModifier(fadeOut);
		fadeOut.addModifierListener(new IModifierListener<IEntity>() {
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
			}

			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				removeNode(rollBackTips);
			}
		});
		attachChild(rollBackTips);

		int starNum = (int) SPUtils.getLuckStarNum(activity);
		SPUtils.saveLuckStarNum(activity, starNum - cost);
		starNumLabel.setText(String.valueOf(starNum - cost));
		starNumLabel.setRightPositionX(580);

		TrackMoveType moveType = trackMove.getTrackMoveType();

		// 回溯操作是消除
		if (moveType == TrackMoveType.EXPLOADE
				|| moveType == TrackMoveType.HAMMER) {
			List<ExploadeTrackInform> lastExploadeTrackInformList = trackMove
					.getExploadeSpriteList();

			savedScore = intToDesString(desStringToInt(savedScore)
					- getEarnScore(lastExploadeTrackInformList.size()));
			desScore = savedScore;
			Map<Integer, List<RollbackInfo>> rollBackMap = RollBackStarsCalculator
					.calculateRollBackStars(starSigns, starSprites, trackMove);

			List<RollbackInfo> horizontalList = rollBackMap
					.get(RollBackStarsCalculator.HORIZONTAL);
			final List<RollbackInfo> verticalList = rollBackMap
					.get(RollBackStarsCalculator.VERTICAL);

			if (horizontalList.size() != 0) {
				final IntegerContainer container = new IntegerContainer(0);
				final int horizontalTotal = horizontalList.size();
				for (int i = 0; i < horizontalList.size(); i++) {
					RollbackInfo info = horizontalList.get(i);
					StarSprite starSprite = info.getStarSprite();
					int step = info.getStep();
					// 向右移动

					MoveByModifier moveBy = new MoveByModifier(
							HORIZONTAL_DURATION, step * borderWidth, 0);

					moveBy.addModifierListener(new IModifierListener<IEntity>() {
						public void onModifierStarted(
								IModifier<IEntity> pModifier, IEntity pItem) {

						}

						public void onModifierFinished(
								IModifier<IEntity> pModifier, IEntity pItem) {
							container.add();
							if (container.getValue() == horizontalTotal) {
								if (verticalList.size() == 0) {
									afterExploadeRollBackMove(trackMove);

								} else {
									verticalExploadeRollback(verticalList,
											trackMove);
								}
							}
						}
					});
					starSprite.registerEntityModifier(moveBy);
				}
			} else {
				if (verticalList.size() == 0) {
					afterExploadeRollBackMove(trackMove);

				} else {
					verticalExploadeRollback(verticalList, trackMove);
				}
			}
			// 如果是用的锤子，返还锤子道具消耗的星星
			if (moveType == TrackMoveType.HAMMER) {
				SPUtils.saveLuckStarNum(
						activity,
						SPUtils.getLuckStarNum(activity)
								+ GameConstants.getHammerCost(SPUtils
										.getUsedHammerNum(activity) - 1));
				SPUtils.saveHammerUsedNum(activity,
						SPUtils.getUsedHammerNum(activity) - 1);
				starNumLabel.setText(String.valueOf(SPUtils
						.getLuckStarNum(activity)));
				starNumLabel.setRightPositionX(580);
			}
		}
		// 回溯操作是转换
		if (moveType == TrackMoveType.SWITCH) {
			// 返还使用转换道具的消耗
			SPUtils.saveSwitchUsedNum(activity,
					SPUtils.getSwitchUsedNum(activity) - 1);
			SPUtils.saveLuckStarNum(
					activity,
					SPUtils.getLuckStarNum(activity)
							+ GameConstants.getSwitchCost(SPUtils
									.getSwitchUsedNum(activity)));
			starNumLabel.setText(String.valueOf(SPUtils
					.getLuckStarNum(activity)));
			starNumLabel.setRightPositionX(580);

			List<Position> randomSpritelist = trackMove.getOriginalSpriteList();
			List<Position> originalSpriteList = trackMove.getRandomSpriteList();
			List<StarSprite> showedList = new ArrayList<StarSprite>();
			for (int i = 0; i < originalSpriteList.size(); i++) {
				Position position = originalSpriteList.get(i);
				showedList.add(starSprites[position.getX()][position.getY()]);
			}

			final int totalNums = originalSpriteList.size();
			final IntegerContainer container = new IntegerContainer(0);
			for (int i = 0; i < totalNums; i++) {
				Position startPosition = originalSpriteList.get(i);
				int index = randomSpritelist.indexOf(startPosition);
				Position desPosition = originalSpriteList.get(index);
				StarSprite starSprite = showedList.get(i);

				MoveModifier modifier = new MoveModifier(1.0f, borderWidth / 2
						+ startPosition.getX() * borderWidth - starWidth / 2,
						borderWidth / 2 + desPosition.getX() * borderWidth
								- starWidth / 2, 960 - borderHeight
								* (startPosition.getY() + 1)
								+ (borderHeight - starHeight) / 2
								- GameConstants.AD_BANNER_HEIGHT, 960
								- borderHeight * (desPosition.getY() + 1)
								+ (borderHeight - starHeight) / 2
								- GameConstants.AD_BANNER_HEIGHT,
						EaseBackIn.getInstance());

				starSprite.registerEntityModifier(modifier);
				modifier.addModifierListener(new IModifierListener<IEntity>() {
					public void onModifierStarted(IModifier<IEntity> pModifier,
							IEntity pItem) {
					}

					public void onModifierFinished(
							IModifier<IEntity> pModifier, IEntity pItem) {
						container.add();
						if (container.getValue() == totalNums) {
							isStarsStatic = true;
							checkIfNoBlockPopableAndDoStuff();
						}
					}
				});
				starSprites[desPosition.getX()][desPosition.getY()] = starSprite;
				starSigns[desPosition.getX()][desPosition.getY()] = starSprite
						.getType();
				starSprite.setIndexXY(desPosition.getX(), desPosition.getY());
			}
			randomSpritelist.clear();
			originalSpriteList.clear();
			showedList.clear();
		}
	}

	public void showHammer() {
		if (isStarsStatic) {
			// 当前处于锤子展示状态中&&!isHamTouch
			if (isInHammerState) {
				quitHammerState();
				checkIfNoBlockPopableAndDoStuff();
				clearCurrentSelectedStarsState();
			}
//			if (isHamTouch) {
//				isHamTouch = true;
//			}
			// 当前没有处于锤子展示状态
			else {
				int cost = GameConstants.getHammerCost(SPUtils
						.getUsedHammerNum(activity));
				int starNum = (int) SPUtils.getLuckStarNum(activity);
				if (cost > starNum) {
					/*
					 * if (SPUtils.isShowLuckyStarNotEnoughDialog(activity)) {
					 * showLuckyStarNotEnoughDialog(BUY_TYPE.HAMMER); } else {
					 * LogUtils.v(TAG, "想要用锤子，但是钱不够，展示快捷购买对话框...");
					 * showQuickBuyDialog(BUY_TYPE.HAMMER); }
					 */
					showQuickBuyDialog(BUY_TYPE.HAMMER);
				} else {
					doAddHammer();
				}
			}
		}
	}

	private Position getBenifitedHammerPosition() {
		return HammerBestPositionFinder.findBestPosition2(starSigns);
	}

	public void showOptions() {
		TCAgent.onEvent(activity, "点击暂停按钮");
		SoundUtils.playButtonClick();
		popScene.showOptions();
	}

	private void addStageClear() {
		stageClearSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"stage_clear", vertextBufferObjectManager);
		stageClearSprite.setScale(0.25f);
		stageClearSprite.setCentrePosition(562, 210);
		stageClearSprite.setVisible(false);
		attachChild(stageClearSprite);
	}

	private void addWhiteBox() {
		whiteSprite = SpriteMaker.makeSpriteWithSingleImageFile("white",
				vertextBufferObjectManager);
		whiteSprite
				.setSize(GameConstants.BASE_WIDTH, GameConstants.BASE_HEIGHT);
		whiteSprite.setCentrePosition(320, 480);
		attachChild(whiteSprite);
		whiteSprite.setVisible(false);
	}

	private void flash() {
		whiteSprite.setVisible(true);
		whiteSprite.setAlpha(1);
		whiteSprite.clearEntityModifiers();
		whiteSprite.registerEntityModifier(new FadeOutModifier(0.2f));
	}

	public void onResume() {
		refreshStarNum();
	}

	public void onPause() {
		if (popScene.isGameOn()) {
			saveGameData();
		}
	}

	private void addBonusScore() {
		bonusScoreLabel = TextMaker.make("奖励" + GameConstants.MAX_BONUS,
				"35white", 320, 960 - 356, HorizontalAlign.CENTER,
				vertextBufferObjectManager);
		remainNumLabel = TextMaker.make("剩余100个星星", "25white", 320, 960 - 299,
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		attachChild(bonusScoreLabel);
		attachChild(remainNumLabel);

		bonusScoreLabel.setVisible(false);
		remainNumLabel.setVisible(false);
	}

	private void initStarSignSampleList() {
		sampleTypeList = new ArrayList<Integer>();
		sampleTypeList.add(GameConstants.STAR_BLUE);
		sampleTypeList.add(GameConstants.STAR_GREEEN);
		sampleTypeList.add(GameConstants.STAR_PURPLE);
		sampleTypeList.add(GameConstants.STAR_RED);
		sampleTypeList.add(GameConstants.STAR_YELLOW);
	}

	// 添加分数label
	private void addScoreLabel() {
		// 205\105
		stageLabel = TextMaker.make("关卡：1234567890", "35white", 35, -151,
				HorizontalAlign.LEFT, vertextBufferObjectManager);

		stageLabel.setLeftPositionX(35);
		attachChild(stageLabel);
		stageLabel.setVisible(false);

		// 目标分数
		clearScoreLabel = TextMaker.make("目标：12345678901234561231231232",
				"35white", 320, -151, HorizontalAlign.CENTER,
				vertextBufferObjectManager);
		clearScoreLabel.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		attachChild(clearScoreLabel);
		clearScoreLabel.setVisible(false);

		// 当前分数
		currentScoreLabel = TextMaker.make("1234567890123456",
				"currentScoreFont", 320, 180, HorizontalAlign.CENTER,
				vertextBufferObjectManager);
		// currentX以及currentY是分数飞到的位置
		currentScoreLabelX = 320;
		currentScoreLabelY = 180;
		currentScoreLabel.setCentrePosition(320, -151);
		currentScoreLabel.setPositionX(320 - currentScoreLabel.getWidthHalf());
		attachChild(currentScoreLabel);
		currentScoreLabel.setVisible(false);
	}

	private void addMoveLabel() {
		moveStageLabel = TextMaker.make("关卡1234567890" + stage, "moveStage",
				-500, 438, HorizontalAlign.CENTER, vertextBufferObjectManager);
		attachChild(moveStageLabel);
		moveStageLabel.setVisible(false);
		moveScoreLabel = TextMaker.make("目标分数12345678901234567890123456789"
				+ stageClearScore, "30white", -500, 518,
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		attachChild(moveScoreLabel);
		moveScoreLabel.setVisible(false);
	}

	private void addScoreTips() {
		scoreTipsLabel = TextMaker.make("xxx连消xxxxx分", "30white",
				GameConstants.BASE_WIDTH / 2, 216, HorizontalAlign.CENTER,
				vertextBufferObjectManager);
		scoreTipsLabel.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		scoreTipsLabel.setVisible(false);
		attachChild(scoreTipsLabel);
	}

	// 展示x连消xx分的提示
	private void showScoreTips(int num) {
		scoreTipsLabel.clearEntityModifiers();
		scoreTipsLabel.setScale(0);
		scoreTipsLabel.setAlpha(1);
		scoreTipsLabel.setVisible(true);
		scoreTipsLabel
				.setText(String.format("%d连消%d分", num, getEarnScore(num)));
		scoreTipsLabel.setCentrePositionX(320);

		ScaleModifier scaleModifier = new ScaleModifier(0.2f, 0f, 1.0f);
		FadeOutModifier fadeOut = new FadeOutModifier(1f);
		SequenceEntityModifier sequence = new SequenceEntityModifier(
				scaleModifier, new DelayModifier(2f), fadeOut);
		scoreTipsLabel.registerEntityModifier(sequence);
	}

	private void handleStarTouch(int type, int x, int y, boolean isExploading) {
		if (isStarsStatic) {
			if (type == GameConstants.STAR_NONE) {
				LogUtils.v(TAG, "点击的是正在消除中的或者已经被消除的星星，不进行处理");
				if (isInHammerState) {
					quitHammerState();
					checkIfNoBlockPopableAndDoStuff();
					clearCurrentSelectedStarsState();
				}
			}
			if (isExploading || type == GameConstants.STAR_NONE) {
				LogUtils.v(TAG, "点击的是正在消除中的或者已经被消除的星星，不进行处理");
				if (isInHammerState) {
					quitHammerState();
					checkIfNoBlockPopableAndDoStuff();
					clearCurrentSelectedStarsState();
				}
			} else {
				if (isInHammerState) {
					if (selectedStarList.contains(new Position(x, y))) {
						int cost = GameConstants.getHammerCost(SPUtils
								.getUsedHammerNum(activity));
						int starNum = (int) SPUtils.getLuckStarNum(activity);
						if (cost > starNum) {
							showQuickBuyDialog(BUY_TYPE.HAMMER);
						} else {
							TCAgent.onEvent(activity, "使用小锤道具");
							LogUtils.v(TAG, "锤掉一个星星...");
							SPUtils.saveLuckStarNum(
									activity,
									SPUtils.getLuckStarNum(activity)
											- GameConstants.getHammerCost(SPUtils
													.getUsedHammerNum(activity)));
							SPUtils.saveHammerUsedNum(activity,
									SPUtils.getUsedHammerNum(activity) + 1);
							// 加入使用幸运星的TD统计
							TCAgent.onEvent(
									activity,
									"此次小锤道具使用"
											+ GameConstants.getHammerCost(SPUtils
													.getUsedHammerNum(activity))
											+ "颗幸运星");
							refreshStarNum();
							starSprites[x][y].setScale(1);
							starSpriteBorders[x][y].setScale(1);
							starSprites[x][y].setZIndex(0);
							quitHammerState();
							disableStarTouch();
							exploadeSelectedStars(TrackMoveType.HAMMER);
						}
					} else {
						LogUtils.v(TAG, "锤子移动位置...");
						LogUtils.v(TAG, "清除上次锤子位置的星星状态");
						clearLastHammerPositionSpriteState();
						if (x > 5) {
							hammerSprite.setFlippedHorizontal(true);
							hammerSprite.setCentrePosition(x * borderWidth, 960
									- (borderHeight / 2 + borderHeight * y)
									- GameConstants.AD_BANNER_HEIGHT);
						} else {
							hammerSprite.setFlippedHorizontal(false);
							hammerSprite.setCentrePosition(borderWidth + x
									* borderWidth, 960
									- (borderHeight / 2 + borderHeight * y)
									- GameConstants.AD_BANNER_HEIGHT);
						}
						selectedStarList.clear();
						selectedStarList.add(new Position(x, y));
						starSprites[x][y].setScale(1.2f);
						starSprites[x][y].setScaleCenter(starWidth / 2,
								starHeight / 2);
						// TODO 使其不被别人遮挡，在进行scale的时候
						starSprites[x][y].setZIndex(100);
						starsLayer.sortChildren();
						starSpriteBorders[x][y].setScale(1.2f);
						starSpriteBorders[x][y].setVisible(true);
					}
				} else {
					LogUtils.v(TAG, "清除当前保存的处于选中状态的星星记录，收集需要被选中的星星，清除掉...");
					clearCurrentSelectedStarsState();
					collectSpritesAndShowBorderAndExploadeThem(type, x, y);
					isSceneTouch = false; //这里要设置scene点击为false
				}
			}
		} else {
			LogUtils.v(TAG, "星星在此状态下不能响应点击事件");
		}
	}

	private List<StarSprite> getMaxLinkedStarSpriteList() {
		List<StarSprite> linkedSpriteList = new ArrayList<StarSprite>();
		int linkedNum = 0;
		for (int i = 0; i < MAX_LINE; i++) {
			for (int j = 0; j < MAX_LINE; j++) {
				StarSprite starSprite = starSprites[i][j];
				if (starSprite.getType() != GameConstants.STAR_NONE) {
					List<StarSprite> tempList = new ArrayList<StarSprite>();
					trackSprite(starSprite, tempList);
					if (tempList.size() > linkedNum) {
						linkedSpriteList = tempList;
						linkedNum = tempList.size();
					}
				}

			}
		}
		return linkedSpriteList;
	}

	// 检查每一个的上下左右
	private void trackSprite(StarSprite starSprite,
			List<StarSprite> trackSpriteList) {

		int compareX = starSprite.getIndexX();
		int compareY = starSprite.getIndexY();
		int compareType = starSprite.getType();

		// 右边
		for (int i = compareX + 1; i < MAX_LINE; i++) {
			if (starSprites[i][compareY].getType() == compareType
					&& !starSprites[i][compareY].isExploading()) {
				if (!trackSpriteList.contains(starSprites[i][compareY])) {
					trackSpriteList.add(starSprites[i][compareY]);
					trackSprite(starSprites[i][compareY], trackSpriteList);
				}
			} else {
				break;
			}
		}
		// 左边
		for (int i = starSprite.getIndexX() - 1; i >= 0; i--) {
			if (starSprites[i][compareY].getType() == compareType
					&& !starSprites[i][compareY].isExploading()) {
				if (!trackSpriteList.contains(starSprites[i][compareY])) {
					trackSpriteList.add(starSprites[i][compareY]);
					trackSprite(starSprites[i][compareY], trackSpriteList);
				}
			} else {
				break;
			}

		}

		// 上边
		for (int i = starSprite.getIndexY() + 1; i < MAX_LINE; i++) {
			if (starSprites[compareX][i].getType() == compareType
					&& !starSprites[compareX][i].isExploading()) {
				if (!trackSpriteList.contains(starSprites[compareX][i])) {
					trackSpriteList.add(starSprites[compareX][i]);
					trackSprite(starSprites[compareX][i], trackSpriteList);
				}
			} else {
				break;
			}

		}
		// 下边
		for (int i = starSprite.getIndexY() - 1; i >= 0; i--) {
			if (starSprites[compareX][i].getType() == compareType
					&& !starSprites[compareX][i].isExploading()) {
				if (!trackSpriteList.contains(starSprites[compareX][i])) {
					trackSpriteList.add(starSprites[compareX][i]);
					trackSprite(starSprites[compareX][i], trackSpriteList);
				}
			} else {
				break;
			}
		}
	}

	// 当前点击的星星是否能被选中，能的话，选中他们
	private void collectSpritesAndShowBorderAndExploadeThem(int type, int x,
			int y) {
		List<StarSprite> trackSpriteList = new ArrayList<StarSprite>();
		trackSpriteList.add(starSprites[x][y]);
		LogUtils.v(
				TAG,
				"从数组中获取到的starSprite的属性是：[type:"
						+ starSprites[x][y].getType()
						+ ",color:"
						+ GameConstants.getStarColor(starSprites[x][y]
								.getType()) + "]");
		trackSprite(starSprites[x][y], trackSpriteList);
		if (trackSpriteList.size() >= 2) {
			// SoundUtils.playSelect();
			showScoreTips(trackSpriteList.size());
			LogUtils.v(TAG, "满足被选中的条件，清除上次选中的星星的状态，选中该次的星星");
			fillSelectedListAndShowBorders(trackSpriteList);
			disableStarTouch();
			exploadeSelectedStars(TrackMoveType.EXPLOADE);
		} else {
			LogUtils.v(TAG, "不满足被选中的条件");
		}
	}

	private void clearLastHammerPositionSpriteState() {
		for (Position selectedPosition : selectedStarList) {
			starSpriteBorders[selectedPosition.getX()][selectedPosition.getY()]
					.setVisible(false);
			starSpriteBorders[selectedPosition.getX()][selectedPosition.getY()]
					.setScale(1);
			starSprites[selectedPosition.getX()][selectedPosition.getY()]
					.setScale(1);
			starSprites[selectedPosition.getX()][selectedPosition.getY()]
					.setZIndex(0);
		}
		selectedStarList.clear();
	}

	// 清除上次选中的星星的状态
	private void clearCurrentSelectedStarsState() {
		for (Position selectedPosition : selectedStarList) {
			starSpriteBorders[selectedPosition.getX()][selectedPosition.getY()]
					.setVisible(false);
		}
		selectedStarList.clear();
	}

	// 选中星星
	private void fillSelectedListAndShowBorders(List<StarSprite> starSpriteList) {
		for (int i = 0; i < starSpriteList.size(); i++) {
			StarSprite starSprite = starSpriteList.get(i);
			starSprite.setZIndex(0);
			selectedStarList.add(new Position(starSprite.getIndexX(),
					starSprite.getIndexY()));
			starSpriteBorders[starSprite.getIndexX()][starSprite.getIndexY()]
					.setVisible(true);
		}
	}

	// 根据消除的星星获取得到的分数
	private int getEarnScore(int starNums) {
		return starNums * starNums * 5;
	}

	// 获得每一个星星消除之后得到的分数
	public int getEachStarScoreEarned(int star) {
		return star * star * 5 - (star - 1) * (star - 1) * 5;
	}

	public void updateStuff(float delta) {
		updateExploade(delta);
		updateMoveLeft();
		updateRemainExploade(delta);
		updateDelta += delta;
		if (updateDelta >= 0.0167f) {
			updateDelta = 0;
			updateBonusScore(delta);
			updateVisualScoreShow(delta);
		}
		updatePopParticleList(delta);
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

	private void updateRemainExploade(float delta) {
		if (remainList.size() > 0 && remainIndex < remainList.size()) {
			if (remainDelta >= 0.18f * remainIndex) {
				if (remainIndex <= 8) {
					ScaleModifier scaleTo = new ScaleModifier(0.1f, 1.0f, 1.5f);
					ScaleModifier scaleToReverse = new ScaleModifier(0.1f,
							1.5f, 1.0f);
					SequenceEntityModifier sequence = new SequenceEntityModifier(
							scaleTo, scaleToReverse);
					int bonusScore = getBonus(remainIndex + 1);
					final int iIndex = remainIndex;
					StarSprite starSprite = remainList.get(iIndex);
					starSprite.setVisible(false);

					// TODO 添加例子效果
					// ParticleSystem particle = PopParticle.make(starSprite
					// .getType());
					// particle.autoRelease();
					// particle.setPosition(starSprite.getPositionX(),
					// starSprite.getPositionY());
					// addChild(particle, 2);

					PopParticle particle = PopParticleMaker
							.make(vertextBufferObjectManager,
									starSprite.getCentreX()
											- starSprite.getWidthHalf(),
									starSprite.getCentreY()
											- starSprite.getHeightHalf(),
									starSprite.getType());
					particle.setCentrePosition(starSprite.getCentreX(),
							starSprite.getCentreY());
					attachChild(particle);
					particleList.add(particle);

					bonusScoreLabel.setText("奖励" + bonusScore);
					bonusScoreLabel.setCentrePositionX(320);
					bonusScoreLabel.registerEntityModifier(sequence);
					SoundUtils.playPop();
					remainIndex++;
					if (remainIndex == remainList.size()) {
						LogUtils.v(TAG, "所有剩余星星全部消除完毕....");
						afterExploadRemainStars(remainList.size());
						remainList.clear();
						remainDelta = 0;
						remainIndex = 0;
					}
				} else {
					int j = remainIndex;
					for (int i = j; i < remainList.size(); i++) {
						StarSprite starSprite = remainList.get(i);
						starSprite.setVisible(false);
						// TODO 添加例子效果
						// ParticleSystem particle = PopParticle.make(starSprite
						// .getType());
						// particle.autoRelease();
						// particle.setPosition(starSprite.getPositionX(),
						// starSprite.getPositionY());
						// addChild(particle, 2);

						PopParticle particle = PopParticleMaker.make(
								vertextBufferObjectManager,
								starSprite.getCentreX()
										- starSprite.getWidthHalf(),
								starSprite.getCentreY()
										- starSprite.getHeightHalf(),
								starSprite.getType());
						particle.setCentrePosition(starSprite.getCentreX(),
								starSprite.getCentreY());
						attachChild(particle);
						particleList.add(particle);
					}
					int bonusScore = getBonus(remainIndex + 1);
					bonusScoreLabel.setText("奖励" + bonusScore);
					bonusScoreLabel.setCentrePositionX(320);

					ScaleModifier scaleTo = new ScaleModifier(0.1f, 1.0f, 1.5f);
					ScaleModifier scaleToReverse = new ScaleModifier(0.1f,
							1.5f, 1.0f);
					SequenceEntityModifier sequence = new SequenceEntityModifier(
							scaleTo, scaleToReverse);
					bonusScoreLabel.registerEntityModifier(sequence);
					SoundUtils.playPop();
					afterExploadRemainStars(remainList.size());
					remainList.clear();
					remainIndex = 0;
					remainDelta = 0;
				}
			}
			remainDelta += delta;
		}
	}

	private void updateMoveLeft() {
		if (emptyColumnList.size() != 0) {
			List<Integer> nullList = new ArrayList<Integer>();
			nullList.addAll(emptyColumnList);
			emptyColumnList.clear();
			LogUtils.v(TAG, "空列是：" + nullList);
			MoveLeftSpriteAndNums moveLeftSpriteAndMaxStep = getMoveLeftStarSpriteListAndNums(nullList);
			List<Map<Integer, StarSprite>> leftMoveStarSpriteList = moveLeftSpriteAndMaxStep
					.getMoveLeftStarSpriteList();
			final int totalNums = moveLeftSpriteAndMaxStep.getNums();

			if (leftMoveStarSpriteList.size() != 0) {
				final IntegerContainer intContainer = new IntegerContainer(0);
				LogUtils.v(TAG, "需要向左移动的列的数据是：" + leftMoveStarSpriteList);
				for (Map<Integer, StarSprite> item : leftMoveStarSpriteList) {
					final int moveStep = (Integer) item.keySet().toArray()[0];
					StarSprite starSprite = item.get(moveStep);
					int x = starSprite.getIndexX();
					int y = starSprite.getIndexY();
					int moveX = x - moveStep;

					MoveByModifier moveBy = new MoveByModifier(
							HORIZONTAL_DURATION, -moveStep * borderWidth, 0);

					moveBy.addModifierListener(new IModifierListener<IEntity>() {
						public void onModifierStarted(
								IModifier<IEntity> pModifier, IEntity pItem) {
						}

						public void onModifierFinished(
								IModifier<IEntity> pModifier, IEntity pItem) {
							intContainer.add();
							if (totalNums == intContainer.getValue()) {
								isStarsStatic = true;
								LogUtils.v(TAG, "横向移动结束...");
								checkIfNoBlockPopableAndDoStuff();
							}
						}
					});
					// 转换
					StarSprite temp = starSprites[x][y];
					starSprites[x][y] = starSprites[moveX][y];
					starSprites[moveX][y] = temp;
					starSprites[x][y].setIndexXY(x, y);
					starSprites[moveX][y].setIndexXY(moveX, y);
					starSprite.registerEntityModifier(moveBy);
				}
			} else {
				LogUtils.v(TAG, "没有需要向做移动的列");
				isStarsStatic = true;
				checkIfNoBlockPopableAndDoStuff();
			}
		}
	}

	// bad mood, if done, think do sth else maybe time to leave.
	private void updateExploade(float delta) {
		if (exploadeList.size() > 0 && exploadeIndex < exploadeList.size()) {
			if (exploadeDelta >= exploadeIndex * 0.08f) {
				final float iRate = 1.05f + 0.15f * exploadeIndex;
				final int iIndex = exploadeIndex;
				Position selectedPosition = exploadeList.get(iIndex);
				SoundUtils.playPop(iRate);
				StarSprite handledSprite = starSprites[selectedPosition.getX()][selectedPosition
						.getY()];
				handledSprite.setVisible(false);
				starSpriteBorders[selectedPosition.getX()][selectedPosition
						.getY()].setVisible(false);

				// TODO yeah,particles, but the engine is simple
				// ParticleSystem particle = PopParticle
				// .make(starSprites[selectedPosition.getX()][selectedPosition
				// .getY()].getType());
				// particle.autoRelease();
				// particle.setPosition(handledSprite.getPositionX(),
				// handledSprite.getPositionY());
				// addChild(particle, 2);
				if (showParticle) {
					PopParticle particle = PopParticleMaker.make(
							vertextBufferObjectManager,
							handledSprite.getCentreX()
									- handledSprite.getWidthHalf(),
							handledSprite.getCentreY()
									- handledSprite.getHeightHalf(),
							handledSprite.getType());
					particle.setCentrePositionX(handledSprite.getCentreX());
					particle.setBottomPositionY(handledSprite.getTopY());
					attachChild(particle);
					particleList.add(particle);
					showParticle = false;
				} else {
					showParticle = true;
				}

				starSprites[selectedPosition.getX()][selectedPosition.getY()]
						.setType(GameConstants.STAR_NONE);

				final Text eachStarScore = TextMaker.make(
						String.valueOf(getEachStarScoreEarned(iIndex + 1)),
						"100white", handledSprite.getCentreX(),
						handledSprite.getCentreY(), HorizontalAlign.CENTER,
						vertextBufferObjectManager);
				eachStarScore.setScaleCenter(eachStarScore.getWidthHalf(),
						eachStarScore.getHeightHalf());
				eachStarScore.setScale(0.6f);

				MoveModifier moveTo = new MoveModifier(2.0f,
						eachStarScore.getX(), currentScoreLabelX
								- eachStarScore.getWidthHalf(),
						eachStarScore.getY(), currentScoreLabelY
								- eachStarScore.getHeightHalf(),
						EaseExponentialOut.getInstance());
				ScaleModifier scaleTo = new ScaleModifier(2.0f, 0.6f, 0.3f);

				ParallelEntityModifier spawn = new ParallelEntityModifier(
						moveTo, scaleTo);
				attachChild(eachStarScore);

				spawn.addModifierListener(new IModifierListener<IEntity>() {
					public void onModifierStarted(IModifier<IEntity> pModifier,
							IEntity pItem) {
					}

					public void onModifierFinished(
							IModifier<IEntity> pModifier, IEntity pItem) {
						removeNode(eachStarScore);
					}
				});
				eachStarScore.registerEntityModifier(spawn);
				desScore = intToDesString(desStringToInt(desScore)
						+ getEachStarScoreEarned(iIndex + 1));
				exploadeIndex++;
				if (exploadeIndex == exploadeList.size()) {
					displayObtainScore(exploadeList.get(0),
							getEarnScore(exploadeList.size()));
					doCheers(exploadeList.size());
					LogUtils.v(TAG, "星星消除完毕，上边的挪动下来，右边的向左边靠拢");
					List<Position> tempList = new ArrayList<Position>();
					tempList.addAll(exploadeList);
					exploadeList.clear();
					exploadeIndex = 0;
					exploadeDelta = 0;
					gatherStarSprites(tempList);
				}

			}
			exploadeDelta += delta;
		}
	}

	public void updateVisualScoreShow(float delta) {

		int desScoreInt = desStringToInt(desScore);
		float visualScoreFloat = desStringToFloat(visualScore);
		long stageClearScoreLong = desStringToLong(stageClearScore);

		if (desScoreInt == 0) {
			visualScoreFloat = 0;
			visualScore = floatToDesString(visualScoreFloat);
		} else {
			float step = (desScoreInt - visualScoreFloat) * 0.04f;
			if (Math.abs(step) < 0.05f) {
				step = 0.05f * ((step > 0) ? 1 : -1);
			}
			visualScoreFloat += step;
			visualScore = floatToDesString(visualScoreFloat);
			if (Math.abs(desScoreInt - visualScoreFloat) < 0.1f) {
				visualScoreFloat = desScoreInt;
				visualScore = floatToDesString(visualScoreFloat);
			}
		}
		currentScoreLabel.setText(String.valueOf((int) (visualScoreFloat)));
		if (String.valueOf((int) (visualScoreFloat)).length() != lastCurrentScoreLength) {
			lastCurrentScoreLength = String.valueOf((int) (visualScoreFloat))
					.length();
			if (lastCurrentScoreLength < 6) {
				currentScoreLabel.setPositionX(320 - currentScoreLabel
						.getWidthHalf());
			} else {
				currentScoreLabel
						.setRightPositionX(backSpaceBtn.getLeftX() - 5);
			}
		}

		if (popScene.isGameOn() && !isStageClearShown
				&& visualScoreFloat >= stageClearScoreLong
				&& stageClearScoreLong > 0 && isStageClearShowEnabled) {
			isStageClearShown = true;
			SoundUtils.playStageClear();
			showStageClear();
		}
		if (isStageClearShown && visualScoreFloat < stageClearScoreLong
				&& stageClearScoreLong > 0) {
			isStageClearShown = false;
			hideStageClearSprite();
		}

	}

	private void hideStageClearSprite() {
		if (stageClearSprite.isVisible()) {
			ScaleModifier scale = new ScaleModifier(0.5f, 0.25f, 0,
					EaseBounceOut.getInstance());
			stageClearSprite.clearEntityModifiers();
			stageClearSprite.registerEntityModifier(scale);
		}
	}

	private void updateBonusScore(float delta) {
		if (isBonusStep) {
			float step = -bonusScore * 0.04f;
			if (Math.abs(step) < 0.05f) {
				step = 0.05f * ((step > 0) ? 1 : -1);
			}
			bonusScore += step;
			if (Math.abs(-bonusScore) < 0.1f) {
				bonusScore = 0;
			}
			bonusScoreLabel.setText("奖励" + String.valueOf((int) (bonusScore)));
			bonusScoreLabel.setCentrePositionX(320);
			if (bonusScore == 0) {
				isBonusStep = false;
				if (popScene.isGameOn()) {
					// delay 0.5s then check showNextStage or showGoonDialog
					DelayModifier delay = new DelayModifier(1f);
					registerEntityModifier(delay);
					delay.addModifierListener(new IModifierListener<IEntity>() {
						public void onModifierStarted(
								IModifier<IEntity> pModifier, IEntity pItem) {
						}

						public void onModifierFinished(
								IModifier<IEntity> pModifier, IEntity pItem) {
							hideBonusScoreAndNextStageOrGameOver();
						}
					});
				}
			}
		}
	}

	@SuppressLint("UseValueOf")
	private void randomListOrder(List<? extends Object> list) {
		Collections.sort(list, new Comparator<Object>() {
			HashMap<Object, Double> map = new HashMap<Object, Double>();

			public int compare(Object lhs, Object rhs) {
				initOrderValue(lhs);
				initOrderValue(rhs);
				double value1 = map.get(lhs);
				double value2 = map.get(rhs);

				if (value1 > value2) {
					return 1;
				}
				if (value1 == value2) {
					return 0;
				}
				if (value1 < value2) {
					return -1;
				}
				return 0;
			}

			private void initOrderValue(Object object) {
				if (map.get(object) == null) {
					map.put(object, new Double(random.nextDouble()));
				}
			}
		});
	}

	private boolean isEmptyColumn(int columnIndex) {
		boolean isEmpty = true;
		for (int i = 0; i < 10; i++) {
			if (starSigns[columnIndex][i] != GameConstants.STAR_NONE) {
				isEmpty = false;
				break;
			}
		}
		return isEmpty;
	}

	// 记录下当前操作，以便在使用回滚道具时候追溯之前的操作
	private void saveTrackMove(TrackMoveType type) {
		List<ExploadeTrackInform> trackInformList = new ArrayList<ExploadeTrackInform>();
		List<Integer> emptyColumnList = new ArrayList<Integer>();
		List<Integer> checkedColumnList = new ArrayList<Integer>();
		for (int i = 0; i < exploadeList.size(); i++) {
			Position position = exploadeList.get(i);
			trackInformList.add(new ExploadeTrackInform(position.getX(),
					position.getY(), starSprites[position.getX()][position
							.getY()].getType()));

			int column = position.getX();

			if (!checkedColumnList.contains(column)) {
				checkedColumnList.add(column);
				// 检查是否是空列
				if (isEmptyColumn(column)) {
					emptyColumnList.add(column);
				}
			}
		}
		trackmoveStack.add(new TrackMove(type, null, null, trackInformList,
				emptyColumnList.size() != 0 ? emptyColumnList : null));
	}

	// 消除选中的星星
	private void exploadeSelectedStars(TrackMoveType trackMoveType) {
		LogUtils.v(TAG, "消除选中的星星");

		exploadeList.addAll(selectedStarList);
		randomListOrder(exploadeList);
		selectedStarList.clear();
		exploadeIndex = 0;
		exploadeDelta = 0;

		for (Position selectedPosition : exploadeList) {
			starSprites[selectedPosition.getX()][selectedPosition.getY()]
					.setExploading(true);
			starSprites[selectedPosition.getX()][selectedPosition.getY()]
					.setIgnoreTouch(true);
		}
		LogUtils.v(TAG, "首先记录现在星星的位置、当前分数等数据...");
		// 先将清除之后的星星的位置记录下来
		savedScore = intToDesString(desStringToInt(savedScore)
				+ getEarnScore(exploadeList.size()));
		popScene.notifyCurrentScore(desStringToInt(savedScore));
		for (int i = 0; i < exploadeList.size(); i++) {
			Position selectedPosition = exploadeList.get(i);
			starSigns[selectedPosition.getX()][selectedPosition.getY()] = GameConstants.STAR_NONE;
		}

		saveTrackMove(trackMoveType);
		if (!isRollBackEnabled) {
			showEnableRollbackBtn();
		}

		gatherStarSigns();
		LogUtils.v(TAG, "展示消除的粒子效果，分数飞入");
		LogUtils.v(TAG, "位置数据：" + exploadeList);
	}

	private void displayObtainScore(Position position, int score) {
		final Text obtainScoreLabel = TextMaker.make(String.valueOf(score),
				"100white", position.getX() * borderWidth + borderWidth / 2, 0,
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		obtainScoreLabel.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		obtainScoreLabel.setPositionY(960
				- (borderHeight / 2 + borderHeight * position.getY() + 20)
				- obtainScoreLabel.getHeight() / 2
				- GameConstants.AD_BANNER_HEIGHT);
		obtainScoreLabel.setScale(1);
		if (obtainScoreLabel.getX() < 0) {
			obtainScoreLabel.setX(0);
		}
		if (obtainScoreLabel.getX() > 640) {
			obtainScoreLabel.setX(640);
		}
		FadeOutModifier fadeOut = new FadeOutModifier(0.5f);

		fadeOut.addModifierListener(new IModifierListener<IEntity>() {
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
			}

			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				removeNode(obtainScoreLabel);
			}
		});
		obtainScoreLabel.registerEntityModifier(fadeOut);
		attachChild(obtainScoreLabel);
	}

	private void showStageClear() {
		clearScoreLabel.registerEntityModifier(BlinkModifierMaker
				.make(4.0f, 12));
		for (int i = 0; i < 5; i++) {
			final int iIndex = i;
			final Sprite sprite = SpriteMaker.makeSpriteWithSingleImageFile(
					"stage_clear", vertextBufferObjectManager);
			sprite.setCentrePosition(320, 520);
			sprite.setScale(3.0f);
			if (i > 1) {
				sprite.setAlpha(0.3f);
			}
			DelayModifier delay = new DelayModifier(i * 0.06f);
			ScaleModifier scale = new ScaleModifier(0.15f, 3f, 0.8f);
			DelayModifier delay2 = new DelayModifier(0.5f);
			MoveModifier move = new MoveModifier(0.12f, sprite.getX(),
					stageClearSprite.getX(), sprite.getY(),
					stageClearSprite.getY(), EaseBackIn.getInstance());
			ScaleModifier scaleDownAction = new ScaleModifier(0.12f, 0.8f,
					0.25f);
			SequenceEntityModifier sequence = new SequenceEntityModifier(delay,
					scale, delay2, new ParallelEntityModifier(move,
							scaleDownAction));

			sequence.addModifierListener(new IModifierListener<IEntity>() {
				public void onModifierStarted(IModifier<IEntity> pModifier,
						IEntity pItem) {
				}

				public void onModifierFinished(IModifier<IEntity> pModifier,
						IEntity pItem) {
					if (iIndex == 0) {
						if (popScene.isGameOn() && isStageClearShown) {
							stageClearSprite.clearEntityModifiers();
							stageClearSprite.setScale(0.25f);
							stageClearSprite.setAlpha(1);
							stageClearSprite.setVisible(true);
						}
					}
					removeNode(sprite);
				}
			});
			sprite.registerEntityModifier(sequence);
			attachChild(sprite);
		}
	}

	// 移除“不合法”的位置数据，亦即没一列只留下最底部的位置数据
	@SuppressLint("UseSparseArrays")
	private Map<Integer, Position> getValidEmilinatePositionList(
			List<Position> emilinatePositionList) {
		Map<Integer, Position> positionMap = new HashMap<Integer, Position>();
		for (Position position : emilinatePositionList) {
			Position savedPosition = positionMap.get(position.getX());
			if (savedPosition == null || savedPosition.getY() > position.getY()) {
				positionMap.put(position.getX(), position);
			}
		}
		return positionMap;
	}

	// 根据移除的位置数据来判定每一列上需要移动的步数
	// private Map<Integer, Integer> getDownStep(
	// List<Position> emilinatePositionList) {
	// Map<Integer, Integer> stepMap = new HashMap<Integer, Integer>();
	// for (Position position : emilinatePositionList) {
	// if (stepMap.get(position.getX()) == null) {
	// stepMap.put(position.getX(), 1);
	// } else {
	// stepMap.put(position.getX(), stepMap.get(position.getX()) + 1);
	// }
	// }
	// return stepMap;
	// }

	/**
	 * 向下下落的星星&需要移动的步数，以及其中最大下落的步数，取到最大下落步数这个值是为了控制什么时候进行横向移动的判断
	 * 
	 * @author Administrator
	 * 
	 */
	class MoveDownSpriteAndNums {
		private Map<Integer, List<Map<Integer, StarSprite>>> moveDownSpriteMap;
		private int nums;

		public MoveDownSpriteAndNums(
				Map<Integer, List<Map<Integer, StarSprite>>> moveDownSpriteMap,
				int nums) {
			this.moveDownSpriteMap = moveDownSpriteMap;
			this.nums = nums;
		}

		public Map<Integer, List<Map<Integer, StarSprite>>> getMoveDownSpriteMap() {
			return moveDownSpriteMap;
		}

		public int getNums() {
			return nums;
		}
	}

	// 获取每一列需要做移动的星星和移动的步数以及其中的最大步数
	private MoveDownSpriteAndNums getMoveDownSpriteAndNums(
			Map<Integer, Position> validPosition) {
		Map<Integer, List<Map<Integer, StarSprite>>> moveDownSpriteList = new HashMap<Integer, List<Map<Integer, StarSprite>>>();
		Set<Entry<Integer, Position>> entrySet = validPosition.entrySet();
		int nums = 0;

		for (Entry<Integer, Position> entry : entrySet) {
			Position position = entry.getValue();
			int x = entry.getKey();
			int moveStep = 1;
			List<Map<Integer, StarSprite>> starSpriteList = new ArrayList<Map<Integer, StarSprite>>();

			for (int i = position.getY() + 1; i < MAX_LINE; i++) {
				if (starSprites[x][i].isExploading()
						|| starSprites[x][i].getType() == GameConstants.STAR_NONE) {
					moveStep++;
				} else {
					Map<Integer, StarSprite> map = new HashMap<Integer, StarSprite>();
					map.put(moveStep, starSprites[x][i]);
					starSpriteList.add(map);
					nums++;
				}
			}
			if (starSpriteList.size() != 0) {
				moveDownSpriteList.put(x, starSpriteList);
			}
		}
		return new MoveDownSpriteAndNums(moveDownSpriteList, nums);
	}

	// 向左移动，然后检查是否已经没有可以点击的星星了，如果是的话，消除现在剩下的所有的，看是否能过关，不能过关，进入失败逻辑
	private void moveLeftIfShouldAndCheckIfShouldClearAll(
			Map<Integer, Position> validPositionMap) {
		LogUtils.v(TAG, "从当前消除的列中检查是否有空列...");
		Object[] xs = validPositionMap.keySet().toArray();
		final List<Integer> nullList = new ArrayList<Integer>();

		for (int i = 0; i < xs.length; i++) {
			boolean isNullColumn = true;
			for (int j = 0; j < MAX_LINE; j++) {
				if (starSprites[(Integer) xs[i]][j].getType() != GameConstants.STAR_NONE) {
					isNullColumn = false;
					break;
				}
			}
			if (isNullColumn) {
				nullList.add((Integer) xs[i]);
			}
		}
		if (nullList.size() != 0) {
			emptyColumnList.clear();
			emptyColumnList.addAll(nullList);
		} else {
			LogUtils.v(TAG, "没有空列");
			isStarsStatic = true;
			checkIfNoBlockPopableAndDoStuff();
		}
	}

	// 是否有没有点击的星星
	private boolean isRemainStars() {
		for (int j = MAX_LINE - 1; j >= 0; j--) {
			for (int i = MAX_LINE - 1; i >= 0; i--) {
				if (starSigns[i][j] != GameConstants.STAR_NONE) {
					return true;
				}
			}
		}
		return false;
	}

	// 炸掉所有剩余的星星，加分，然后判断是成功还是失败
	private void exploadeAllRemainStars() {
		final List<StarSprite> remainStarList = new ArrayList<StarSprite>();
		for (int j = MAX_LINE - 1; j >= 0; j--) {
			for (int i = MAX_LINE - 1; i >= 0; i--) {
				if (starSigns[i][j] != GameConstants.STAR_NONE) {
					remainStarList.add(starSprites[i][j]);
					starSigns[i][j] = GameConstants.STAR_NONE;
				}
			}
		}
		bonusScoreLabel.setText("奖励" + GameConstants.MAX_BONUS);
		bonusScoreLabel.setCentrePositionX(320);
		bonusScoreLabel.setPositionY(550);
		remainNumLabel.setText("剩余" + remainStarList.size() + "个星星");
		remainNumLabel.setCentrePositionX(320);
		bonusScoreLabel.setScale(1.0f);
		bonusScoreLabel.setVisible(true);
		remainNumLabel.setVisible(true);

		LogUtils.v(TAG, "设置待保存的当前分数值");
		savedScore = intToDesString(desStringToInt(savedScore)
				+ getBonus(remainStarList.size()));
		popScene.notifyCurrentScore(desStringToInt(savedScore));

		if (remainStarList.size() != 0) {
			LoopEntityModifier blink = BlinkModifierMaker.make(1.0f, 10);
			blink.addModifierListener(new IModifierListener<IEntity>() {
				public void onModifierStarted(IModifier<IEntity> pModifier,
						IEntity pItem) {
				}

				public void onModifierFinished(IModifier<IEntity> pModifier,
						IEntity pItem) {
					exploadeRemainStars(0f, remainStarList);
				}
			});
			for (int i = 0; i < remainStarList.size(); i++) {
				if (i == 0) {
					remainStarList.get(i).registerEntityModifier(blink);
				} else {
					remainStarList.get(i).registerEntityModifier(
							blink.deepCopy());
				}
			}
			remainNumLabel.registerEntityModifier(blink.deepCopy());
		} else {
			DelayModifier delay = new DelayModifier(1.0f);
			delay.addModifierListener(new IModifierListener<IEntity>() {
				public void onModifierStarted(IModifier<IEntity> pModifier,
						IEntity pItem) {
				}

				public void onModifierFinished(IModifier<IEntity> pModifier,
						IEntity pItem) {
					exploadeRemainStars(0f, remainStarList);
				}
			});
			registerEntityModifier(delay);
		}

	}

	public void exploadeRemainStars(float delta, Object object) {
		@SuppressWarnings("unchecked")
		final List<StarSprite> remainStarList = (List<StarSprite>) object;
		remainList.clear();
		remainDelta = 0;
		remainIndex = 0;
		remainList.addAll(remainStarList);
		if (remainList.size() == 0) {
			afterExploadRemainStars(0);
		}
	}

	private void clearUpdateVariables() {
		exploadeList.clear();
		exploadeIndex = 0;
		exploadeDelta = 0;

		remainList.clear();
		remainIndex = 0;
		remainDelta = 0;

		emptyColumnList.clear();

		isBonusStep = false;
		bonusScore = 0;
	}

	// 步进添加分数
	public void stepAddBonusScore(final int bonus) {
		desScore = intToDesString(desStringToInt(desScore) + bonus);
		isBonusStep = true;
		bonusScore = bonus;
	}

	private void afterExploadRemainStars(int nums) {
		final int bonus = getBonus(nums);
		// 有加到分数
		if (bonus > 0) {
			DelayModifier delay = new DelayModifier(1.0f);

			MoveModifier moveTo = new MoveModifier(1.0f,
					320 - bonusScoreLabel.getWidth() / 2,
					320 - bonusScoreLabel.getWidth() / 2,
					bonusScoreLabel.getY(),
					300 + bonusScoreLabel.getHeight() / 2,
					EaseExponentialOut.getInstance());

			SequenceEntityModifier sequence = new SequenceEntityModifier(delay,
					moveTo);

			sequence.addModifierListener(new IModifierListener<IEntity>() {
				public void onModifierStarted(IModifier<IEntity> pModifier,
						IEntity pItem) {
				}

				public void onModifierFinished(IModifier<IEntity> pModifier,
						IEntity pItem) {
					stepAddBonusScore(bonus);
				}
			});
			bonusScoreLabel.registerEntityModifier(sequence);
		}
		// 没有加到分数
		else {
			DelayModifier delayTime = new DelayModifier(1.0f);
			delayTime.addModifierListener(new IModifierListener<IEntity>() {
				public void onModifierStarted(IModifier<IEntity> pModifier,
						IEntity pItem) {
				}

				public void onModifierFinished(IModifier<IEntity> pModifier,
						IEntity pItem) {
					hideBonusScoreAndNextStageOrGameOver();
				}
			});
			bonusScoreLabel.registerEntityModifier(delayTime);
		}
		showFireworks(nums);
	}

	public void hideBonusScoreAndNextStageOrGameOver() {
		bonusScoreLabel.setVisible(false);
		remainNumLabel.setVisible(false);
		String number = SPUtils.getPhoneNumber(activity);
//		passLevel = true;
		if (SPUtils.getServerOnOff(activity) && stageNum == 2 && "null".equals(number)) {
			showStarGifDialog();
		} else {
			stageNum++;
			nextStageOrGameOver();
		}
	}
	
	// 显示星星有礼对话框
	private void showStarGifDialog() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				final android.app.Dialog dialog = new android.app.Dialog(
						activity, Utils.getResByID(activity, "zplayDialogFull",
								"style"));
				dialog.setCancelable(false);
				ClickThroughAbsoluteLayout contentView = new ClickThroughAbsoluteLayout(
						activity);
				dialog.setContentView(contentView);
				dialog.getWindow().setWindowAnimations(
						Utils.getResByID(activity, "zplayDialogAnimScale",
								"style"));
				
				ImageView bgView = new ImageView(activity);
				bgView.setBackgroundResource(Utils.getResByID(activity,
						"alert_tips_bg", "drawable"));

				AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
						(int) SizeHelper.xOGUnitToPixel(579), (int) SizeHelper
								.yOGUnitToPixel(631), (int) SizeHelper
								.xOGUnitToPixel(30), (int) SizeHelper
								.yOGUnitToPixel(205));
				contentView.addView(bgView, params);
				
				ImageView tipsView = new ImageView(activity);
				tipsView.setBackgroundResource(Utils.getResByID(activity, "xxyl", "drawable"));

				params = new AbsoluteLayout.LayoutParams((int) SizeHelper
						.xOGUnitToPixel(220), (int) SizeHelper
						.yOGUnitToPixel(60), (int) SizeHelper
						.xOGUnitToPixel(210), (int) SizeHelper
						.yOGUnitToPixel(288));
				contentView.addView(tipsView, params);

				ImageView tipsView2 = new ImageView(activity);
				tipsView2.setBackgroundResource(Utils.getResByID(activity, "xxyl_tips", "drawable"));
				params = new AbsoluteLayout.LayoutParams(
						(int) SizeHelper.xOGUnitToPixel(450), 
						(int) SizeHelper.yOGUnitToPixel(160),
						(int) SizeHelper.xOGUnitToPixel(95), 
						(int) SizeHelper.yOGUnitToPixel(403));
				contentView.addView(tipsView2, params);

				Button closeBtn = new Button(activity);
				closeBtn.setBackgroundResource(Utils.getResByID(activity,
						"options_quit", "drawable"));
				params = new AbsoluteLayout.LayoutParams((int) SizeHelper
						.xOGUnitToPixel(48), (int) SizeHelper
						.yOGUnitToPixel(48), (int) SizeHelper
						.xOGUnitToPixel(536), (int) SizeHelper
						.yOGUnitToPixel(213));
				contentView.addView(closeBtn, params);
				
				final EditText textBtn = new EditText(activity);
				textBtn.setBackgroundColor(android.graphics.Color.WHITE);
				textBtn.setTextColor(android.graphics.Color.RED);
				textBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						SizeHelper.xOGUnitToPixel(30));
				params = new AbsoluteLayout.LayoutParams((int) SizeHelper
						.xOGUnitToPixel(482),
						AbsoluteLayout.LayoutParams.WRAP_CONTENT,
						(int) SizeHelper.xOGUnitToPixel(79), (int) SizeHelper
								.yOGUnitToPixel(596));
				textBtn.setHint("请输入存档密码");
				contentView.addView(textBtn, params);
				
				closeBtn.setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {
						dialog.dismiss();
						nextStageOrGameOver();
					}
				});
				
				RelativeLayout layout = new RelativeLayout(activity);
				layout.setPadding((int) SizeHelper.xOGUnitToPixel(55), 
						(int) SizeHelper.xOGUnitToPixel(10), 
						(int) SizeHelper.xOGUnitToPixel(55), 
						(int) SizeHelper.xOGUnitToPixel(10));
				layout.setBackgroundResource(Utils.getResByID(activity, "yellow_btn", "drawable"));
				params = new AbsoluteLayout.LayoutParams(
						AbsoluteLayout.LayoutParams.WRAP_CONTENT,
						AbsoluteLayout.LayoutParams.WRAP_CONTENT,
						(int) SizeHelper.xOGUnitToPixel(176),
						(int) SizeHelper.yOGUnitToPixel(700));
				
				Button lqBtn = new Button(activity);
				lqBtn.setBackgroundResource(Utils.getResByID(activity, "lq", "drawable"));
				LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layout.addView(lqBtn, params2);
				contentView.addView(layout, params);

				lqBtn.setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {
						String phone = textBtn.getText().toString().trim();
						if (phone.isEmpty()) {
							Toast.makeText(activity, "存档密码不能为空！", Toast.LENGTH_SHORT).show();
							return;
						}
						Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
						Matcher m = p.matcher(phone);
						if (m.matches()) {
							SPUtils.savePhoneNumber(activity, phone);
							Toast.makeText(activity, "输入成功！获得30个幸运星", Toast.LENGTH_SHORT).show();
							int starNum = (int) SPUtils.getLuckStarNum(activity);
							SPUtils.saveLuckStarNum(activity, starNum + 30);
							refreshStarNum();
							dialog.dismiss();
							nextStageOrGameOver();
						} else {
							Toast.makeText(activity, "存档密码格式有误，请重新输入！", Toast.LENGTH_SHORT).show();
						}
					}
				});
				dialog.show();
			}
		});
	}

	// 根据最后剩下的星星数量来决定是否要放烟花
	private void showFireworks(int nums) {
		if (nums < 10) {
			popScene.showFireworks(0, 10);
			SoundUtils.playCheers();
			SoundUtils.playApplause();
		}
	}

	// 进入下一关还是失败，同时如果能进入下一关
	private void nextStageOrGameOver() {
		hammerBtn.setIgnoreTouch(false);
		if (desStringToInt(savedScore) >= desStringToInt(stageClearScore)) {
			isInResultState = false;
			LogUtils.v(TAG, "达到过关条件，保存该次通关的分数，进入下一关卡");
			SPUtils.saveStageStartScore(activity, desStringToInt(savedScore),
					stage + 1);
			showStage(stage + 1, false);
			hammerBtn.setIgnoreTouch(false);
		} else {
			if (stage == 6 && !isQuickBuyDialogShowed) {
				showQuickBuyDialog(BUY_TYPE.STAGE_6);
				TCAgent.onEvent(activity, "弹出第6关弹窗");
			} else {
				int costNum = GameConstants.getRebornCost(stage, rebornNum);
				buildGoonDialog(costNum);
				goonDialog.show();
			}
		}
	}

	// 点击了继续通关按钮
	public void goOn() {
		TCAgent.onEvent(activity, "点击继续通关");
		goonDialog.setDialogDismissListener(new DialogDismissListener() {
			@Override
			public void onDialogDismiss() {
				LogUtils.v(TAG, "点击了继续通关的按钮...");
				ResourceManager.unloadGoonDialogTextures();
				int costNum = GameConstants.getRebornCost(stage, rebornNum);
				int starNum = (int) SPUtils.getLuckStarNum(activity);
				if (starNum >= costNum) {
					SPUtils.saveLuckStarNum(activity, starNum - costNum);
					refreshStarNum();
					showStage(stage, true);
				} else {
					/*
					 * if (SPUtils.isShowLuckyStarNotEnoughDialog(activity)) {
					 * showLuckyStarNotEnoughDialog(BUY_TYPE.REBORN); } else {
					 * showQuickBuyDialog(BUY_TYPE.REBORN); }
					 */
					showQuickBuyDialog(BUY_TYPE.REBORN);
				}
			}
		});
		goonDialog.dismissWithAnimamtion();
	}

	private void showQuickBuyDialog(BUY_TYPE buyType) {
		this.buyType = buyType;
		buildBuyDialog();
		buyDialog.show();
	}

	public void quickBuy() {
		((PopStar) getActivity()).pay(GameConstants.QUICK_BUY_CHARGEPOINT_ID, new PayCallback() {
			public void callback(int code, String msg) {
				if (code == PayCallback.OK) {
					buyDialog.setDialogDismissListener(new DialogDismissListener() {
						public void onDialogDismiss() {
							ResourceManager.unloadBuyDialogTextures();
							if (buyType == BUY_TYPE.REBORN) {
								SPUtils.saveLuckStarNum(activity, SPUtils.getLuckStarNum(activity) + QUICK_BUY_NUM - GameConstants.getRebornCost(stage, rebornNum));
								refreshStarNum();
								showStage(stage, true);
							}
							if (buyType == BUY_TYPE.HAMMER) {
								SPUtils.saveLuckStarNum(activity, SPUtils.getLuckStarNum(activity) + QUICK_BUY_NUM);
								refreshStarNum();
								doAddHammer();
							}
							if (buyType == BUY_TYPE.SWITCH || buyType == BUY_TYPE.END_SWITCH) {
								SPUtils.saveLuckStarNum(activity, SPUtils.getLuckStarNum(activity) + QUICK_BUY_NUM);
								doSwitch();
								if (buyType == BUY_TYPE.END_SWITCH) {
									isQuickBuyDialogShowed = true;
								}
							}
							if (buyType == BUY_TYPE.ROLL_BACK) {
								SPUtils.saveLuckStarNum(activity, SPUtils.getLuckStarNum(activity) + QUICK_BUY_NUM);
								doRollBack();
							}
							if (buyType == BUY_TYPE.NONE) {
								popScene.onSceneResume();
								SPUtils.saveLuckStarNum(activity,SPUtils.getLuckStarNum(activity) + QUICK_BUY_NUM);
								refreshStarNum();
							}
							if (buyType == BUY_TYPE.STAGE_6) {
								SPUtils.saveLuckStarNum(activity, SPUtils.getLuckStarNum(activity) + QUICK_BUY_NUM);
								refreshStarNum();
								int costNum = GameConstants.getRebornCost(stage, rebornNum);
								buildGoonDialog(costNum);
								goonDialog.show();
							}
						}
					});
					buyDialog.dismissWithAnimamtion();
				}
				Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void gameOver() {
		visualScore = floatToDesString(0);
		savedScore = intToDesString(0);
		desScore = intToDesString(0);
		stage = 0;
		savedStage = 0;
		trackmoveStack.clear();
		final Text gameOverLabel = TextMaker.make("游戏结束", "50white", 320, 480,
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		// 失败的音效
		SoundUtils.playGameOver();
		RotationByModifier rotateBy = new RotationByModifier(1.0f, 720);
		ScaleModifier scaleTo = new ScaleModifier(1.0f, 0f, 1f);
		DelayModifier delayTime = new DelayModifier(1.5f);
		FadeOutModifier fadeOut = new FadeOutModifier(0.5f);

		SequenceEntityModifier sequence = new SequenceEntityModifier(
				new ParallelEntityModifier(rotateBy, scaleTo), delayTime,
				fadeOut);
		sequence.addModifierListener(new IModifierListener<IEntity>() {
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
			}

			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				removeNode(gameOverLabel);
				callMainSceneShowMainMenu();
			}
		});
		gameOverLabel.setScale(0);
		gameOverLabel.registerEntityModifier(sequence);
		attachChild(gameOverLabel, 1);

		((PopStar) getActivity()).showPop();
	}

	public void callMainSceneShowMainMenu() {
		isInResultState = false;
		popScene.showMenu();
	}

	private void printDebugInfo() {
	}

	// 第一关通关奖励
	private void doStage1AwardStuff() {
		// ResourceManager.loadTGJLDialog(getActivity());
		final Dialog dialog = new Dialog(popScene);
		dialog.setBackKeyResponsed(false);

		Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile("tgjl",
				vertextBufferObjectManager);
		bgSprite.setPosition(0, 98);
		dialog.attachChild(bgSprite);

		// 328/186
		final ButtonSprite okBtn = ButtonMaker.makeFromSingleImgFile(320, 774,
				"tgjl_btn_ok", vertextBufferObjectManager);
		okBtn.setPosition(190, 718);
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				okBtn.setEnabled(false);
				popScene.onSceneResume();
				dialog.dismiss();
				// 幸运星数量+8
				SPUtils.saveLuckStarNum(activity,
						SPUtils.getLuckStarNum(activity) + 8);
				refreshStarNum();
				SPUtils.setIsStageOneAward(activity, true);
			}
		});
		dialog.attachChild(okBtn);

		Text num1 = TextMaker.make("1", "tgjl", 0, 0, HorizontalAlign.CENTER,
				vertextBufferObjectManager);
		Text num2 = TextMaker.make("1", "tgjl", 0, 0, HorizontalAlign.CENTER,
				vertextBufferObjectManager);
		Text num3 = TextMaker.make("1", "tgjl", 0, 0, HorizontalAlign.CENTER,
				vertextBufferObjectManager);
		Text num4 = TextMaker.make("1", "tgjl", 0, 0, HorizontalAlign.CENTER,
				vertextBufferObjectManager);
		Text num5 = TextMaker.make("1", "tgjl", 0, 0, HorizontalAlign.CENTER,
				vertextBufferObjectManager);

		num1.setColor(251 / 255f, 205 / 255f, 3 / 255f);
		num2.setColor(251 / 255f, 205 / 255f, 3 / 255f);
		num3.setColor(251 / 255f, 205 / 255f, 3 / 255f);
		num4.setColor(251 / 255f, 205 / 255f, 3 / 255f);
		num5.setColor(251 / 255f, 205 / 255f, 3 / 255f);

		num1.setRightPositionX(48);
		num1.setCentrePositionY(649);

		num2.setRightPositionX(181);
		num2.setCentrePositionY(649);

		num3.setRightPositionX(304);
		num3.setCentrePositionY(649);

		num4.setRightPositionX(432);
		num4.setCentrePositionY(649);

		num5.setRightPositionX(563);
		num5.setCentrePositionY(649);

		Text tips1 = TextMaker.make("恭喜您!顺利通过第一关", "tgjl", 0, 0,
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		tips1.setColor(251 / 255f, 205 / 255f, 3 / 255f);
		tips1.setCentrePositionX(320);
		tips1.setTopPositionY(324);

		Text tips2 = TextMaker.make("获得8枚幸运星奖励，", "tgjl", 0, 0,
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		tips2.setCentrePositionX(320);
		tips2.setTopPositionY(365);

		Text tips3 = TextMaker.make("幸运星可以让您体验更多神奇道具，", "tgjl", 0, 0,
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		tips3.setCentrePositionX(320);
		tips3.setTopPositionY(406);

		Text tips4 = TextMaker.make("获得更高积分!点击领取吧!", "tgjl", 0, 0,
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		tips4.setColor(251 / 255f, 205 / 255f, 3 / 255f);
		tips4.setCentrePositionX(320);
		tips4.setTopPositionY(447);
		tips4.setColor(251 / 255f, 205 / 255f, 3 / 255f);

		dialog.attachChild(num5);
		dialog.attachChild(num4);
		dialog.attachChild(num3);
		dialog.attachChild(num2);
		dialog.attachChild(num1);

		dialog.attachChild(tips4);
		dialog.attachChild(tips3);
		dialog.attachChild(tips2);
		dialog.attachChild(tips1);

		dialog.setDialogDismissListener(new DialogDismissListener() {
			public void onDialogDismiss() {
				// ResourceManager.unloadTGJLDialog();
			}
		});
		dialog.showWithAnimation();
	}

	private void doStage2AwardStuff() {
		// ResourceManager.loadTGJLDialog(getActivity());
		final Dialog dialog = new Dialog(popScene);
		dialog.setBackKeyResponsed(false);

		Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile("tgjl",
				vertextBufferObjectManager);
		bgSprite.setPosition(0, 98);
		dialog.attachChild(bgSprite);

		// 328/186
		final ButtonSprite okBtn = ButtonMaker.makeFromSingleImgFile(320, 774,
				"tgjl_btn_ok", vertextBufferObjectManager);
		okBtn.setPosition(190, 718);
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				okBtn.setEnabled(false);
				popScene.onSceneResume();
				dialog.dismiss();
				// 幸运星数量+23
				SPUtils.saveLuckStarNum(activity,
						SPUtils.getLuckStarNum(activity) + 22);
				refreshStarNum();
				SPUtils.setIsStageTwoAward(activity, true);
			}
		});
		dialog.attachChild(okBtn);

		Text num1 = TextMaker.make("5", "tgjl", 0, 0, HorizontalAlign.CENTER,
				vertextBufferObjectManager);
		Text num2 = TextMaker.make("4", "tgjl", 0, 0, HorizontalAlign.CENTER,
				vertextBufferObjectManager);
		Text num3 = TextMaker.make("4", "tgjl", 0, 0, HorizontalAlign.CENTER,
				vertextBufferObjectManager);
		Text num4 = TextMaker.make("4", "tgjl", 0, 0, HorizontalAlign.CENTER,
				vertextBufferObjectManager);
		Text num5 = TextMaker.make("4", "tgjl", 0, 0, HorizontalAlign.CENTER,
				vertextBufferObjectManager);

		num1.setColor(251 / 255f, 205 / 255f, 3 / 255f);
		num2.setColor(251 / 255f, 205 / 255f, 3 / 255f);
		num3.setColor(251 / 255f, 205 / 255f, 3 / 255f);
		num4.setColor(251 / 255f, 205 / 255f, 3 / 255f);
		num5.setColor(251 / 255f, 205 / 255f, 3 / 255f);

		num1.setRightPositionX(48);
		num1.setCentrePositionY(649);

		num2.setRightPositionX(181);
		num2.setCentrePositionY(649);

		num3.setRightPositionX(304);
		num3.setCentrePositionY(649);

		num4.setRightPositionX(432);
		num4.setCentrePositionY(649);

		num5.setRightPositionX(563);
		num5.setCentrePositionY(649);

		Text tips1 = TextMaker.make("恭喜您!顺利通过第二关", "tgjl", 0, 0,
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		tips1.setColor(251 / 255f, 205 / 255f, 3 / 255f);
		tips1.setCentrePositionX(320);
		tips1.setTopPositionY(324);

		Text tips2 = TextMaker.make("获得22枚幸运星奖励，", "tgjl", 0, 0,
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		tips2.setCentrePositionX(320);
		tips2.setTopPositionY(365);

		Text tips3 = TextMaker.make("幸运星可以让您体验更多神奇道具，", "tgjl", 0, 0,
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		tips3.setCentrePositionX(320);
		tips3.setTopPositionY(406);

		Text tips4 = TextMaker.make("获得更高积分!点击领取吧!", "tgjl", 0, 0,
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		tips4.setColor(251 / 255f, 205 / 255f, 3 / 255f);
		tips4.setCentrePositionX(320);
		tips4.setTopPositionY(447);
		tips4.setColor(251 / 255f, 205 / 255f, 3 / 255f);

		dialog.attachChild(num5);
		dialog.attachChild(num4);
		dialog.attachChild(num3);
		dialog.attachChild(num2);
		dialog.attachChild(num1);

		dialog.attachChild(tips4);
		dialog.attachChild(tips3);
		dialog.attachChild(tips2);
		dialog.attachChild(tips1);

		dialog.setDialogDismissListener(new DialogDismissListener() {
			public void onDialogDismiss() {
				// ResourceManager.unloadTGJLDialog();
			}
		});
		dialog.showWithAnimation();

	}

	// 进入新的关卡
	private void showStage(final int newStage, boolean isReborn) {
		LogUtils.d(TAG, "stage:" + newStage);

		// 通过第一关的奖励
		// if (newStage == 2 && !SPUtils.isStageOneAward(activity)) {
		// doStage1AwardStuff();
		// }
		// 通过第二关的奖励
		// if (newStage == 3 && !SPUtils.isStageTwoAward(activity)) {
		// doStage2AwardStuff();
		// }

		// 第三关或者第9关完成之后需要弹出大礼包对话框
				if ((newStage == 4 && !SPUtils.isStageLBShowed(activity, "4"))
				|| (newStage == 13) || (newStage == 16)	|| (newStage == 18)) {
					SPUtils.setStageLBShowed(activity, true, String.valueOf(newStage));
					showQuickBuyDialog(BUY_TYPE.NONE);
					TCAgent.onEvent(activity, "弹出第" + (newStage - 1) + "关弹窗");
				}

		if (newStage == 3) {
			SPUtils.setIsStageThirdEnd(activity, true);
		}

		if (newStage == 4) {
			SPUtils.setStageThreePassed(activity, true);
		}

		printDebugInfo();
		setInResultState(false);
		trackmoveStack.clear();
		enableAllBtnsWhitouSetColor();
		if (isRollBackEnabled) {
			showDisableRollbackBtn();
		}
		stage = newStage;
		savedStage = stage;
		if (isReborn) {
			desScore = intToDesString((int) SPUtils.getStageStartScore(
					activity, newStage));
			rebornNum++;
			TCAgent.onEvent(activity, "使用" + (rebornNum - 1) + "次复活");
		} else {
			rebornNum = 1;
			TCAgent.onEvent(activity, "完成关卡" + (stage - 1));
		}
		savedScore = desScore;
		stageClearScore = longToDesString(GameConstants.getClearScore(newStage));
		isStageClearShown = false;
		isQuickBuyDialogShowed = false;
		isStageClearShowEnabled = false;
		isItemUsedTriggerShowed = false;
		stageClearSprite.setVisible(false);
		generateStarSigns();
		saveGameData();
		initTotalLabelStringsShowWithoutCurrentScoreLabel();
		showNextStageActionAndLoadStars();
	}

	private void showNextStageActionAndLoadStars() {
		MoveModifier moveModifier = new MoveModifier(0.5f, 640,
				320 - moveStageLabel.getWidth() / 2, moveStageLabel.getY(),
				moveStageLabel.getY(), EaseExponentialInOut.getInstance());
		DelayModifier delay = new DelayModifier(0.5f);

		SequenceEntityModifier sequence = new SequenceEntityModifier(delay,
				moveModifier);

		sequence.addModifierListener(new IModifierListener<IEntity>() {
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {

			}

			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				showScoreMoveInAnimation();
			}
		});
		moveStageLabel.registerEntityModifier(sequence);
		sequence.addModifierListener(new IModifierListener<IEntity>() {
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
				moveStageLabel.setVisible(true);
			}

			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
			}
		});
	}

	private void generateStarSigns() {
		int typeListMaxIndex = sampleTypeList.size() - 1;
		starSigns = new int[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				// 测试用
//				 starSigns[i][j] = GameConstants.STAR_BLUE;
				int typeIndex = MathUtils.getValue(random, 0, typeListMaxIndex);
				starSigns[i][j] = sampleTypeList.get(typeIndex);
			}
		}
	}

	// 获取剩余奖励
	private int getBonus(int remainNums) {
		return Math.max(0, GameConstants.MAX_BONUS - remainNums * remainNums
				* 20);
	}

	private void checkIfNoBlockPopableAndDoStuff() {
//			System.out.println("是否是第一关" + SPUtils.isStageThirdEnd(activity));
//			if ((!isItemUsedTriggerShowed && SPUtils.getIsFirstGame(activity) && SPUtils
//					.isStageThirdEnd(activity))
//					|| (!isItemUsedTriggerShowed && isRemainStars() && isRemainStarNum(
//							7, 25))) {
//				isItemUsedTriggerShowed = true;
//				Log.v(TAG, "没有提示道具使用，弹出道具使用提示对话框...");
//				SPUtils.setIsFirstGame(activity, false);
//				ResourceManager.loadItemUseDialog(activity);
//				final Dialog itemUseDialog = new Dialog(popScene);
//				itemUseDialog.setBackKeyResponsed(false);
//				Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile(
//						"item_used_alert_bg", vertextBufferObjectManager);
//				bgSprite.setPosition(61, 329);
//				itemUseDialog.attachChild(bgSprite);
//
//				// ButtonSprite quitBtn = ButtonMaker.makeFromSingleImgFile(563,
//				// 266, "options_quit_item", vertextBufferObjectManager);
//				// quitBtn.setPosition(535, 310);
//				// quitBtn.setOnClickListener(new OnClickListener() {
//				// public void onClick(ButtonSprite pButtonSprite,
//				// float pTouchAreaLocalX, float pTouchAreaLocalY) {
//				// itemUseDialog.dismissWithAnimamtion();
//				// // 检查是否游戏结束了
//				// checkIfNoBlockPopableAndDoStuff();
//				// }
//				// });
//				// quitBtn.setScale(0.8f);
//				// itemUseDialog.attachChild(quitBtn);
//
//				Size size = SizeHelper.ogSizeScale(111, 111);
//				float divide = (518 - size.getWidth() * 2) / 3;
//
//				// 328/186
//				final ButtonSprite hammerBtn = ButtonMaker
//						.makeFromSingleImgFile(320, 774, "item_hammer",
//								vertextBufferObjectManager);
//				hammerBtn.setSize(size.getWidth(), size.getHeight());
//				hammerBtn.setCentrePosition(200.5f, 498);
//				hammerBtn.setLeftPositionX(61 + divide);
//				hammerBtn.setOnClickListener(new OnClickListener() {
//					public void onClick(ButtonSprite pButtonSprite,
//							float pTouchAreaLocalX, float pTouchAreaLocalY) {
//						hammerBtn.setEnabled(false);
//						itemUseDialog.dismiss();
//						showHammer();
//					}
//				});
//				itemUseDialog.attachChild(hammerBtn);
//
//				final ButtonSprite switchBtn = ButtonMaker
//						.makeFromSingleImgFile(320, 774, "item_switch",
//								vertextBufferObjectManager);
//				switchBtn.setSize(size.getWidth(), size.getHeight());
//				switchBtn.setCentrePosition(439.5f, 498);
//				switchBtn.setLeftPositionX(61 + divide + size.getWidth()
//						+ divide);
//				switchBtn.setOnClickListener(new OnClickListener() {
//					public void onClick(ButtonSprite pButtonSprite,
//							float pTouchAreaLocalX, float pTouchAreaLocalY) {
//						switchBtn.setEnabled(false);
//						itemUseDialog.dismiss();
//						showSwitch();
//					}
//				});
//				itemUseDialog.attachChild(switchBtn);
//
//				itemUseDialog
//						.setDialogDismissListener(new DialogDismissListener() {
//							public void onDialogDismiss() {
//								ResourceManager.unloadItemUseDialog();
//							}
//						});
//				itemUseDialog.showWithAnimation();
//
//				float gapV = 5.0f;
//				// 继续游戏
//				final ButtonSprite okBtn = ButtonMaker.makeFromSingleImgFile(
//						320, 730, "yellow_btn_long_ud",
//						vertextBufferObjectManager);
//				okBtn.setCentrePositionX(GameConstants.BASE_WIDTH / 2);
//				okBtn.setPositionY(bgSprite.getBottomY() + gapV);
//				okBtn.setOnClickListener(new OnClickListener() {
//					public void onClick(ButtonSprite pButtonSprite,
//							float pTouchAreaLocalX, float pTouchAreaLocalY) {
//						okBtn.setEnabled(false);
//						itemUseDialog.dismissWithAnimamtion();
//						// 检查是否游戏结束了
//						checkIfNoBlockPopableAndDoStuff();
//					}
//				});
//
//				itemUseDialog.attachChild(okBtn);
//
//				Text oKText = TextMaker.make("继续游戏", "50white_ud", 320, 705,
//						HorizontalAlign.CENTER, vertextBufferObjectManager);
//				oKText.setCentrePosition(okBtn.getCentreX(), okBtn.getCentreY());
//				itemUseDialog.attachChild(oKText);
//				LoopEntityModifier loop = new LoopEntityModifier(
//						new SequenceEntityModifier(new ScaleModifier(0.5f,
//								0.9f, 1.0f),
//								new ScaleModifier(0.5f, 1.0f, 0.9f)));
//				// okBtn.registerEntityModifier(loop);
//				hammerBtn.registerEntityModifier(loop.deepCopy());
//				switchBtn.registerEntityModifier(loop.deepCopy());
//				// oKText.registerEntityModifier(loop.deepCopy());
//			} else {
//				// 这里可能是结果
		if (isDead()) {
			setInResultState(true);
			disableAllBtns();
			isStarsStatic = false;
			LogUtils.v(TAG, "没有可以点击的了，炸掉所有剩余的星星");
//			passLevel = true;
			exploadeAllRemainStars();
		} else {
			LogUtils.v(TAG, "还可以继续");
			autoClick();
		}
	}

	/**
	 * 剩余星星的数量是否在[7,25]之间 add by liufengqiang
	 */
	private boolean isRemainStarNum(int a, int b) {
		final List<StarSprite> remainStarNum = new ArrayList<StarSprite>();
		for (int j = MAX_LINE - 1; j >= 0; j--) {
			for (int i = MAX_LINE - 1; i >= 0; i--) {
				if (starSigns[i][j] != GameConstants.STAR_NONE) {
					remainStarNum.add(starSprites[i][j]);
				}
			}
		}
		System.out.println("remainStarNum.size()---" + remainStarNum.size());
		return (remainStarNum.size() >= a && remainStarNum.size() <= b);
	}

	// 检查是否已经没有可以点击的了
	private boolean isDead() {
		boolean isDead = true;
		for (int i = 0; i < MAX_LINE && isDead; i++) {
			for (int j = 0; j < MAX_LINE; j++) {
				int type = starSigns[i][j];
				if (type == GameConstants.STAR_NONE) {
					break;
				}
				if (i - 1 >= 0 && starSigns[i - 1][j] == type) {
					isDead = false;
					break;
				}
				if (i + 1 < MAX_LINE && starSigns[i + 1][j] == type) {
					isDead = false;
					break;
				}
				if (j + 1 < MAX_LINE && starSigns[i][j + 1] == type) {
					isDead = false;
					break;
				}
				if (j - 1 >= 0 && starSigns[i][j - 1] == type) {
					isDead = false;
					break;
				}
			}
		}
		return isDead;
	}

	/**
	 * 需要横向移动的星星&移动步数以及最大步数
	 * 
	 * @author Administrator
	 * 
	 */
	class MoveLeftSpriteAndNums {
		private List<Map<Integer, StarSprite>> moveLeftStarSpriteList;
		private int nums;

		public MoveLeftSpriteAndNums(
				List<Map<Integer, StarSprite>> moveLeftStarSpriteList, int nums) {
			this.moveLeftStarSpriteList = moveLeftStarSpriteList;
			this.nums = nums;
		}

		public List<Map<Integer, StarSprite>> getMoveLeftStarSpriteList() {
			return moveLeftStarSpriteList;
		}

		public int getNums() {
			return nums;
		}
	}

	// 根据空列获取需要向左移动的starSprite以及需要移动的步数
	private MoveLeftSpriteAndNums getMoveLeftStarSpriteListAndNums(
			List<Integer> nullList) {
		List<Map<Integer, StarSprite>> moveLeftStarSpriteList = new ArrayList<Map<Integer, StarSprite>>();
		Collections.sort(nullList);
		int x = nullList.get(0);
		int moveStep = 1;
		int nums = 0;
		for (int i = x + 1; i < MAX_LINE; i++) {
			if (nullList.contains(i)) {
				moveStep++;
			} else {
				for (int j = 0; j < MAX_LINE; j++) {
					if (starSprites[i][j].getType() != GameConstants.STAR_NONE) {
						Map<Integer, StarSprite> map = new HashMap<Integer, StarSprite>();
						map.put(moveStep, starSprites[i][j]);
						moveLeftStarSpriteList.add(map);
						nums++;
					}
				}
			}
		}
		return new MoveLeftSpriteAndNums(moveLeftStarSpriteList, nums);
	}

	private void gatherStarSprites(List<Position> emilinatePositionList) {
		LogUtils.v(TAG, "填补位置：" + emilinatePositionList + "处的空挡");
		final Map<Integer, Position> validPositionMap = getValidEmilinatePositionList(emilinatePositionList);
		LogUtils.v(TAG, "计算出的validPositionMap：" + validPositionMap);
		// 待落下的星星
		LogUtils.v(TAG, "计算需要移动下来的星星以及相应需要移动的步数以及其中移动的最大步数...");
		MoveDownSpriteAndNums moveDownSpriteAndNums = getMoveDownSpriteAndNums(validPositionMap);
		Map<Integer, List<Map<Integer, StarSprite>>> moveDownSpriteMap = moveDownSpriteAndNums
				.getMoveDownSpriteMap();
		final int totalMoveNums = moveDownSpriteAndNums.getNums();
		final Set<Entry<Integer, List<Map<Integer, StarSprite>>>> moveDownSpriteEntrySet = moveDownSpriteMap
				.entrySet();
		if (moveDownSpriteEntrySet.size() == 0) {
			LogUtils.v(TAG, "没有星星掉落，进行横向检查...");
			moveLeftIfShouldAndCheckIfShouldClearAll(validPositionMap);
		} else {
			final IntegerContainer intContainer = new IntegerContainer(0);
			for (Entry<Integer, List<Map<Integer, StarSprite>>> moveDownSpriteEntry : moveDownSpriteEntrySet) {
				final int x = moveDownSpriteEntry.getKey();
				final List<Map<Integer, StarSprite>> starSpriteList = moveDownSpriteEntry
						.getValue();
				// 每一列的每一个，冒泡形式互换，starSpriteList必须保证顺序
				for (int i = 0; i < starSpriteList.size(); i++) {
					final int iIndex = i;
					final Map<Integer, StarSprite> starSpriteMap = starSpriteList
							.get(i);
					final int moveStep = (Integer) starSpriteMap.keySet()
							.toArray()[0];
					final StarSprite starSprite = starSpriteMap.get(moveStep);
					final int y = starSprite.getIndexY();
					final int downY = y - moveStep;
					MoveModifier moveTo = new MoveModifier(DURATION,
							starSprite.getX(), starSprite.getX(),
							starSprite.getY(), starSprite.getY() + moveStep
									* borderHeight);
					StarSprite temp = starSprites[x][downY];
					starSprites[x][downY] = starSprites[x][y];
					starSprites[x][y] = temp;
					starSprites[x][downY].setIndexXY(x, downY);
					starSprites[x][y].setIndexXY(x, y);

					moveTo.addModifierListener(new IModifierListener<IEntity>() {
						public void onModifierStarted(
								IModifier<IEntity> pModifier, IEntity pItem) {
						}

						public void onModifierFinished(
								IModifier<IEntity> pModifier, IEntity pItem) {
							intContainer.add();
							// 每一列的最后一个播放落地的声音
							if (iIndex == starSpriteList.size() - 1) {
								SoundUtils.playLanding();
							}
							if (intContainer.getValue() == totalMoveNums) {
								LogUtils.v(TAG, "竖向掉落完毕，进行横向检查...");
								moveLeftIfShouldAndCheckIfShouldClearAll(validPositionMap);
							}
						}
					});
					starSprite.registerEntityModifier(moveTo);
				}
			}
		}
	}

	// 实例化100个starSprite以及被选择的边框并添加到layer中，只是设置为不可见
	private void initStars() {
		touchCallback = new TouchCallback() {
			public void callback(int type, int x, int y, boolean isExploading) {
				//这时要响应星星点击事件 而不是scene点击事件
				isSceneTouch = true;
				LogUtils.v(TAG, "type:" + type + ",x:" + x + ",y:" + y
						+ ",isExploading:" + isExploading + ",color:"
						+ GameConstants.getStarColor(type));
				handleStarTouch(type, x, y, isExploading);
			}
		};
		starsLayer = new Layer(popScene);
		attachChild(starsLayer);

		for (int i = 0; i < 10; i++) {
			for (int j = 9; j >= 0; j--) {
				starSpriteBorders[i][j] = (Sprite) SpriteMaker
						.makeSpriteWithSingleImageFile("block_select",
								vertextBufferObjectManager);
				starSpriteBorders[i][j].setSize(borderWidth, borderHeight);
				starSpriteBorders[i][j].setCentrePosition(borderWidth * i
						+ borderWidth / 2, 960
						- (borderHeight * j + borderHeight / 2)
						- GameConstants.AD_BANNER_HEIGHT);
				starSpriteBorders[i][j].setVisible(false);
				starSprites[i][j] = new StarSprite("block",
						vertextBufferObjectManager);
				starSprites[i][j].setSize(starWidth, starHeight);
				starSprites[i][j].setCentrePosition(borderWidth * i
						+ borderWidth / 2, 960
						- (borderHeight * j + borderHeight / 2)
						- GameConstants.AD_BANNER_HEIGHT);
				starSprites[i][j].setVisible(false);
				starSprites[i][j].setCallback(touchCallback);
				starSprites[i][j].setIgnoreTouch(true);
				starsLayer.attachChild(starSprites[i][j]);
				attachChild(starSpriteBorders[i][j]);
			}
		}
	}

	// 生成星星的标示数组，并设置其初始状态（x、y坐标，type），然后赋值给tempStarSprites新游戏时候执行该方法
	private void generateStarSignArraysAndInitSpriteState() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				// TODO 设置展示哪一帧，
				starSprites[i][j].setCurrentTileIndex(starSigns[i][j]);
				starSprites[i][j].clearEntityModifiers();
				starSprites[i][j].setCustomAttributes(starSigns[i][j], i, j,
						false);
				starSprites[i][j].setScale(1);
			}
		}
	}

	// 新游戏，动画加载星星
	private void loadStars() {
		starsLayer.setVisible(true);
		final IntegerContainer container = new IntegerContainer(0);
		for (int j = 0; j < 10 && popScene.isGameOn(); j++) {
			final int jIndex = j;
			for (int i = 0; i < 10 && popScene.isGameOn(); i++) {
				final int iIndex = i;
				final float fromX = borderWidth / 2 + iIndex * borderWidth
						- starWidth / 2;
				final float toX = fromX;
				final float toY = 960 - borderHeight * (j + 1)
						+ (borderHeight - starHeight) / 2
						- GameConstants.AD_BANNER_HEIGHT;
				final float fromY = toY - 800 - j * 120 - i * 10
						- MathUtils.getValue(random, 0, 100);
				MoveModifier moveModifier = new MoveModifier((Math.abs(fromY
						- toY)) / 3000, fromX, toX, fromY, toY);

				moveModifier
						.addModifierListener(new IModifierListener<IEntity>() {
							public void onModifierStarted(
									IModifier<IEntity> pModifier, IEntity pItem) {
								starSprites[iIndex][jIndex].setVisible(true);
							}

							public void onModifierFinished(
									IModifier<IEntity> pModifier, IEntity pItem) {
								if (iIndex == 9) {
									SoundUtils.playLanding();
								}
								container.setValue(container.getValue() + 1);
								if (container.getValue() == 100
										&& popScene.isGameOn()) {
									enableAllStarSpritesTouch();
									isStarsStatic = true;
									isStageClearShowEnabled = true;
									autoClick();
									doAfterLoadStarsStuff();
								}
							}
						});
				starSprites[iIndex][jIndex]
						.registerEntityModifier(moveModifier);
			}
		}
	}

	// 星星下落之后做的事
	private void doAfterLoadStarsStuff() {
		// 到达第十关,如果开启了春节活动而且还没有展示过春节活动
		if (stage == 10 && SPUtils.isSpringFestivalActOn(activity, "10")
				&& !SPUtils.isSpringFestivalShowed(activity, "10")) {
			showSpringFestivalActDialog();
		}

		if (stage == 15 && SPUtils.isSpringFestivalActOn(activity, "15")
				&& !SPUtils.isSpringFestivalShowed(activity, "15")) {
			showStage15Awards();
		}
	}

	// 关闭春节活对话框，给予5幸运星
	private void closeSpringFestivalActDialogAndGive5Stars(
			android.app.Dialog dialog, final String stage) {
		SPUtils.setSpringFestivalShowed(activity, stage, true);
		dialog.dismiss();
		final Sprite starSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"fly_star", vertextBufferObjectManager);
		attachChild(starSprite);
		starSprite.setCentrePosition(320, 480);

		ScaleModifier scale1 = new ScaleModifier(0.15f, 5f, 1f);
		MoveModifier moveTo = new MoveModifier(1f, starSprite.getX(),
				starSprite.getY(), 455, 19, EaseExponentialOut.getInstance());
		ScaleModifier scaleTo = new ScaleModifier(1.0f, 0.6f, 0.3f);
		ParallelEntityModifier spawn = new ParallelEntityModifier(moveTo,
				scaleTo);
		SequenceEntityModifier sequenceEntityModifier = new SequenceEntityModifier(
				scale1, spawn);

		sequenceEntityModifier
				.addModifierListener(new IModifierListener<IEntity>() {
					public void onModifierStarted(IModifier<IEntity> pModifier,
							IEntity pItem) {
					}

					public void onModifierFinished(
							IModifier<IEntity> pModifier, IEntity pItem) {
						starSprite.detachSelf();
						// 通过第10关给10个，通过第15关给20个
						SPUtils.saveLuckStarNum(
								activity,
								SPUtils.getLuckStarNum(activity)
										+ (stage.equals("10") ? 10 : 20));
						refreshStarNum();
					}
				});
		starSprite.registerEntityModifier(sequenceEntityModifier);
	}

	// 显示春节活动对话框
	private void showSpringFestivalActDialog() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				final android.app.Dialog dialog = new android.app.Dialog(
						activity, Utils.getResByID(activity, "zplayDialogFull",
								"style"));
				dialog.setCancelable(false);
				ClickThroughAbsoluteLayout contentView = new ClickThroughAbsoluteLayout(
						activity);
				dialog.setContentView(contentView);
				dialog.getWindow().setWindowAnimations(
						Utils.getResByID(activity, "zplayDialogAnimScale",
								"style"));
				ImageView bgView = new ImageView(activity);
				bgView.setBackgroundResource(Utils.getResByID(activity,
						"stage_10_bg", "drawable"));

				AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
						(int) SizeHelper.xOGUnitToPixel(387), (int) SizeHelper
								.yOGUnitToPixel(360), (int) SizeHelper
								.xOGUnitToPixel(126), (int) SizeHelper
								.yOGUnitToPixel(300));
				contentView.addView(bgView, params);

				Button closeBtn = new Button(activity);
				closeBtn.setBackgroundResource(Utils.getResByID(activity,
						"quit_btn", "drawable"));
				params = new AbsoluteLayout.LayoutParams((int) SizeHelper
						.xOGUnitToPixel(44), (int) SizeHelper
						.yOGUnitToPixel(44), (int) SizeHelper
						.xOGUnitToPixel(459), (int) SizeHelper
						.yOGUnitToPixel(314));
				contentView.addView(closeBtn, params);
				closeBtn.setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {
						closeSpringFestivalActDialogAndGive5Stars(dialog, "10");
					}
				});

				final EditText textBtn = new EditText(activity);
				textBtn.setBackgroundResource(Utils.getResByID(activity,
						"stage_15_edit", "drawable"));
				textBtn.setTextColor(android.graphics.Color.BLACK);
				textBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						SizeHelper.xOGUnitToPixel(30));
				params = new AbsoluteLayout.LayoutParams((int) SizeHelper
						.xOGUnitToPixel(325),
						AbsoluteLayout.LayoutParams.WRAP_CONTENT,
						(int) SizeHelper.xOGUnitToPixel(158), (int) SizeHelper
								.yOGUnitToPixel(494));
				contentView.addView(textBtn, params);

				Button joinBtn = new Button(activity);
				joinBtn.setBackgroundResource(Utils.getResByID(activity,
						"spring_festival_ok", "drawable"));
				params = new AbsoluteLayout.LayoutParams((int) SizeHelper
						.xOGUnitToPixel(154), (int) SizeHelper
						.yOGUnitToPixel(56), (int) SizeHelper
						.xOGUnitToPixel(243), (int) SizeHelper
						.yOGUnitToPixel(560));
				contentView.addView(joinBtn, params);

				joinBtn.setOnClickListener(new View.OnClickListener() {
					@SuppressWarnings("unchecked")
					public void onClick(View arg0) {
						TCAgent.onEvent(activity, "点击参与2015年新年抽奖活动");
						if (textBtn.getText().toString().trim().equals("")) {
							Toast.makeText(activity, "请输入手机号码来参与活动",
									Toast.LENGTH_SHORT).show();
						} else {
							String tel = textBtn.getText().toString().trim();
							String gameID = ConfigValueHandler
									.getGameID(activity);
							String channelID = ConfigValueHandler
									.getChannel(activity);
							String gameVersion = InstalledAppInfoHandler
									.getAppVersionName(activity,
											activity.getPackageName());
							String device = PhoneInfoGetter.getIMEI(activity);
							String ts = String.valueOf(System
									.currentTimeMillis());
							String deviceType = "android";
							String level = "10";
							String sign = Encrypter
									.doMD5EncodeWithLowercase(gameID
											+ channelID + device + tel
											+ "zplay888");
							new WebTaskHandler(activity, new WebTask() {
								public void doTask(String arg0, String arg1) {
									if (arg0 == null) {
										Toast.makeText(activity,
												"网络原因，参与活动失败，请点击参与按钮重试",
												Toast.LENGTH_SHORT).show();
									} else {
										if (arg0.trim().equals("1")) {
											Toast.makeText(activity, "感谢您的参与",
													Toast.LENGTH_SHORT).show();
											closeSpringFestivalActDialogAndGive5Stars(
													dialog, "10");
										} else {
											Toast.makeText(activity,
													"参与活动失败，请重试",
													Toast.LENGTH_SHORT).show();
										}
									}
								}
							}, true, true, null, Utils.getResByID(activity,
									"cd_key_handling", "string"), false, true).execute(WebParamsMapBuilder
									.buildParams(
											"http://popstar.zplay.cn/ten/tenin.php",
											JSONBuilder
													.buildJSON(
															MapBuilder
																	.buildMap(
																			new String[] {
																					"data",
																					"sign" },
																			new Object[] {
																					MapBuilder
																							.buildMap(
																									new String[] {
																											"tel",
																											"channelID",
																											"device",
																											"devicetype",
																											"gameID",
																											"gameVersion",
																											"ts",
																											"level" },
																									new String[] {
																											tel,
																											channelID,
																											device,
																											deviceType,
																											gameID,
																											gameVersion,
																											ts,
																											level }),
																					sign }))
													.toString()));
						}
					}
				});
				dialog.show();
			}
		});
	}

	// 显示到达第15关的奖励弹窗
	private void showStage15Awards() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				final android.app.Dialog dialog = new android.app.Dialog(
						activity, Utils.getResByID(activity, "zplayDialogFull",
								"style"));
				dialog.setCancelable(false);
				ClickThroughAbsoluteLayout contentView = new ClickThroughAbsoluteLayout(
						activity);
				dialog.setContentView(contentView);
				dialog.getWindow().setWindowAnimations(
						Utils.getResByID(activity, "zplayDialogAnimScale",
								"style"));
				ImageView bgView = new ImageView(activity);
				bgView.setBackgroundResource(Utils.getResByID(activity,
						"stage_15_bg", "drawable"));

				AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
						(int) SizeHelper.xOGUnitToPixel(387), (int) SizeHelper
								.yOGUnitToPixel(360), (int) SizeHelper
								.xOGUnitToPixel(126), (int) SizeHelper
								.yOGUnitToPixel(300));
				contentView.addView(bgView, params);

				Button closeBtn = new Button(activity);
				closeBtn.setBackgroundResource(Utils.getResByID(activity,
						"quit_btn", "drawable"));
				params = new AbsoluteLayout.LayoutParams((int) SizeHelper
						.xOGUnitToPixel(44), (int) SizeHelper
						.yOGUnitToPixel(44), (int) SizeHelper
						.xOGUnitToPixel(459), (int) SizeHelper
						.yOGUnitToPixel(314));
				contentView.addView(closeBtn, params);
				closeBtn.setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {
						closeSpringFestivalActDialogAndGive5Stars(dialog, "15");
					}
				});

				final EditText textBtn = new EditText(activity);
				textBtn.setBackgroundResource(Utils.getResByID(activity,
						"stage_15_edit", "drawable"));
				textBtn.setTextColor(android.graphics.Color.BLACK);
				textBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						SizeHelper.xOGUnitToPixel(30));
				params = new AbsoluteLayout.LayoutParams((int) SizeHelper
						.xOGUnitToPixel(325),
						AbsoluteLayout.LayoutParams.WRAP_CONTENT,
						(int) SizeHelper.xOGUnitToPixel(158), (int) SizeHelper
								.yOGUnitToPixel(494));
				contentView.addView(textBtn, params);

				Button joinBtn = new Button(activity);
				joinBtn.setBackgroundResource(Utils.getResByID(activity,
						"spring_festival_ok", "drawable"));
				params = new AbsoluteLayout.LayoutParams((int) SizeHelper
						.xOGUnitToPixel(154), (int) SizeHelper
						.yOGUnitToPixel(56), (int) SizeHelper
						.xOGUnitToPixel(243), (int) SizeHelper
						.yOGUnitToPixel(560));
				contentView.addView(joinBtn, params);

				joinBtn.setOnClickListener(new View.OnClickListener() {
					@SuppressWarnings("unchecked")
					public void onClick(View arg0) {
						TCAgent.onEvent(activity, "点击参与第15关完成之后的活动");
						if (textBtn.getText().toString().trim().equals("")) {
							Toast.makeText(activity, "请输入手机号码来参与活动",
									Toast.LENGTH_SHORT).show();
						} else {
							String tel = textBtn.getText().toString().trim();
							String gameID = ConfigValueHandler
									.getGameID(activity);
							String channelID = ConfigValueHandler
									.getChannel(activity);
							String gameVersion = InstalledAppInfoHandler
									.getAppVersionName(activity,
											activity.getPackageName());
							String device = PhoneInfoGetter.getIMEI(activity);
							String ts = String.valueOf(System
									.currentTimeMillis());
							String deviceType = "android";
							String level = "15";
							String sign = Encrypter
									.doMD5EncodeWithLowercase(gameID
											+ channelID + device + tel
											+ "zplay888");
							new WebTaskHandler(activity, new WebTask() {
								public void doTask(String arg0, String arg1) {
									if (arg0 == null) {
										Toast.makeText(activity,
												"网络原因，参与活动失败，请点击参与按钮重试",
												Toast.LENGTH_SHORT).show();
									} else {
										if (arg0.trim().equals("1")) {
											Toast.makeText(activity, "感谢您的参与",
													Toast.LENGTH_SHORT).show();
											closeSpringFestivalActDialogAndGive5Stars(
													dialog, "15");
										} else {
											Toast.makeText(activity,
													"参与活动失败，请重试",
													Toast.LENGTH_SHORT).show();
										}
									}
								}
							}, true, true, null, Utils.getResByID(activity,
									"cd_key_handling", "string"), false, true).execute(WebParamsMapBuilder
									.buildParams(
											"http://popstar.zplay.cn/ten/tenin.php",
											JSONBuilder
													.buildJSON(
															MapBuilder
																	.buildMap(
																			new String[] {
																					"data",
																					"sign" },
																			new Object[] {
																					MapBuilder
																							.buildMap(
																									new String[] {
																											"tel",
																											"channelID",
																											"device",
																											"devicetype",
																											"gameID",
																											"gameVersion",
																											"ts",
																											"level" },
																									new String[] {
																											tel,
																											channelID,
																											device,
																											deviceType,
																											gameID,
																											gameVersion,
																											ts,
																											level }),
																					sign }))
													.toString()));
						}
					}
				});
				dialog.show();
			}
		});
	}

	public void autoClickToggle() {
		isAutoClick = !isAutoClick;
		autoClick();
	}

	// TODO 测试用，自动点击
	private void autoClick() {
		if (isAutoClick) {
			LogUtils.v(TAG, "进行自动点击...");
			List<StarSprite> starSpriteList = getMaxLinkedStarSpriteList();
			if (starSpriteList.size() >= 2) {
				StarSprite starSprite = starSpriteList.get(0);
				handleStarTouch(starSprite.getType(), starSprite.getIndexX(),
						starSprite.getIndexY(), false);
			}
		}
	}

	// 使能所有星星的touch
	private void enableAllStarSpritesTouch() {
		isStarsStatic = true;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				starSprites[i][j].setIgnoreTouch(false);
			}
		}
	}

	// 所有的星星都不能接受点击事件
	public void disableAllStarSpritesTouch() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				starSprites[i][j].setIgnoreTouch(true);
			}
		}
	}

	private void showMoveAndLoadStars() {
		MoveModifier stageMoveIn = new MoveModifier(0.5f, 35, 35, -107
				+ stageLabel.getHeight() / 2, 49 - stageLabel.getHeight() / 2);
		MoveModifier clearScoreMoveIn = new MoveModifier(0.5f,
				clearScoreLabel.getX(), clearScoreLabel.getX(), -151,
				49 - clearScoreLabel.getHeight() / 2);

		MoveModifier currentScoreMoveIn = new MoveModifier(0.5f,
				320 - currentScoreLabel.getWidth() / 2,
				320 - currentScoreLabel.getWidth() / 2, 0,
				156 - currentScoreLabel.getHeight() / 2);
		// duration=0.5s

		stageLabel.registerEntityModifier(stageMoveIn);
		stageLabel.setVisible(true);

		clearScoreLabel.registerEntityModifier(clearScoreMoveIn);
		clearScoreLabel.setVisible(true);

		currentScoreLabel.registerEntityModifier(currentScoreMoveIn);
		currentScoreLabel.setVisible(true);

		// duration=0.5s
		MoveModifier stageLabelInAnimation = new MoveModifier(0.5f, 640,
				320 - moveStageLabel.getWidth() / 2, moveStageLabel.getY(),
				moveStageLabel.getY(), EaseExponentialInOut.getInstance());
		// delay 0.5s->moveto 0.5s

		SequenceEntityModifier sequence = new SequenceEntityModifier(
				new DelayModifier(0.5f), stageLabelInAnimation);
		sequence.addModifierListener(new IModifierListener<IEntity>() {
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
			}

			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				showScoreMoveInAnimation();
			}
		});

		hammerBtn.setAlpha(0);
		hammerBtn.setVisible(true);

		optionsBtn.setAlpha(0);
		optionsBtn.setVisible(true);

		// autoBtn.setAlpha(0);
		// autoBtn.setVisible(true);

		switchBtn.setAlpha(0);
		switchBtn.setVisible(true);

		backSpaceBtn.setAlpha(0);
		backSpaceBtn.setVisible(true);

		luckyStarBuyBtn.setAlpha(0);
		luckyStarBuyBtn.setVisible(true);

		starNumLabel.setAlpha(0);
		starNumLabel.setVisible(true);

		// fadein 0.5s
		hammerBtn.registerEntityModifier(new FadeInModifier(0.5f));
		optionsBtn.registerEntityModifier(new FadeInModifier(0.5f));
		// autoBtn.registerEntityModifier(new FadeInModifier(0.5f));
		switchBtn.registerEntityModifier(new FadeInModifier(0.5f));
		backSpaceBtn.registerEntityModifier(new FadeInModifier(0.5f));
		luckyStarBuyBtn.registerEntityModifier(new FadeInModifier(0.5f));
		starNumLabel.registerEntityModifier(new FadeInModifier(0.5f));

		moveStageLabel.setPosition(640, moveStageLabel.getY());
		moveStageLabel.registerEntityModifier(sequence);
		moveStageLabel.setVisible(true);
	}

	// 目标分数label飞入
	public void showScoreMoveInAnimation() {
		// delay 1s
		DelayModifier delayTime = new DelayModifier(1.0f);
		MoveModifier scoreLabelInAnimation = new MoveModifier(0.5f, 640,
				320 - moveScoreLabel.getWidth() / 2, moveScoreLabel.getY(),
				moveScoreLabel.getY(), EaseExponentialInOut.getInstance());

		MoveModifier stageLabelOutAnimaiton = new MoveModifier(0.2f,
				320 - moveStageLabel.getWidth() / 2,
				0 - moveStageLabel.getWidth() - 5, moveStageLabel.getY(),
				moveStageLabel.getY(), EaseExponentialInOut.getInstance());

		MoveModifier scoreLabelOutAnimation = new MoveModifier(0.2f,
				320 - moveScoreLabel.getWidth() / 2,
				0 - moveScoreLabel.getWidth() - 5, moveScoreLabel.getY(),
				moveScoreLabel.getY(), EaseExponentialInOut.getInstance());
		LoopEntityModifier blink = BlinkModifierMaker.make(1f, 6);

		SequenceEntityModifier sequence = new SequenceEntityModifier(delayTime,
				scoreLabelInAnimation, new DelayModifier(1.0f),
				scoreLabelOutAnimation);

		sequence.addModifierListener(new IModifierListener<IEntity>() {
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
				moveScoreLabel.setVisible(true);
			}

			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				afterMoveStageAndScoreGone();
			}
		});
		moveScoreLabel.registerEntityModifier(sequence);
		clearScoreLabel.setAlpha(1);
		clearScoreLabel.registerEntityModifier(new SequenceEntityModifier(
				new DelayModifier(1.0f), blink));
		moveStageLabel.registerEntityModifier(new SequenceEntityModifier(
				new DelayModifier(2.5f), stageLabelOutAnimaiton));
	}

	// 分数以及关卡飞出之后
	public void afterMoveStageAndScoreGone() {
		generateStarSignArraysAndInitSpriteState();
		loadStars();
	}

	public void showScoreMoveOutAnimation() {
		DelayModifier delaTime = new DelayModifier(1.0f);
		MoveModifier moveTo = new MoveModifier(0.5f, 640,
				320 - moveScoreLabel.getWidth() / 2, moveScoreLabel.getY(),
				moveScoreLabel.getY());
		SequenceEntityModifier sequence = new SequenceEntityModifier(delaTime,
				moveTo);
		sequence.addModifierListener(new IModifierListener<IEntity>() {
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
				moveScoreLabel.setVisible(true);
			}

			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
			}
		});
		moveScoreLabel.registerEntityModifier(sequence);
	}

	private void doCheers(final int num) {
		if (num >= 6) {
			SoundUtils.playCheers();
			SoundUtils.playApplause();
			String uniqueTextureName = null;
			if (num >= 6) {
				uniqueTextureName = "combo_good";
			}
			if (num >= 8) {
				uniqueTextureName = "combo_cool";
			}
			if (num >= 11) {
				uniqueTextureName = "combo_awesome";
			}
			if (num >= 14) {
				uniqueTextureName = "combo_fantastic";
			}

			final Sprite cheerSprite = (Sprite) SpriteMaker
					.makeSpriteWithSingleImageFile(uniqueTextureName,
							vertextBufferObjectManager);
			cheerSprite.setAlpha(1);
			cheerSprite.setScale(10);
			cheerSprite.setCentrePosition(320, 400);

			ScaleModifier scaleTo = new ScaleModifier(0.15f, 10f, 1f);
			LoopEntityModifier blink = BlinkModifierMaker.make(0.6f, 6);
			DelayModifier delay = new DelayModifier(1.0f);
			FadeOutModifier fadeOut = new FadeOutModifier(1.0f);

			SequenceEntityModifier sequence = new SequenceEntityModifier(
					scaleTo, blink, delay, fadeOut);
			sequence.addModifierListener(new IModifierListener<IEntity>() {
				public void onModifierStarted(IModifier<IEntity> pModifier,
						IEntity pItem) {
				}

				public void onModifierFinished(IModifier<IEntity> pModifier,
						IEntity pItem) {
					removeNode(cheerSprite);
				}
			});
			cheerSprite.registerEntityModifier(sequence);
			attachChild(cheerSprite);
			flash();
			popScene.showFireworks(0, num / 2 + 8);
		}
	}

	// 停止所有动作并隐藏
	private void pauseAllActionsAndHideStars() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				starSprites[i][j].clearEntityModifiers();
				starSprites[i][j].setVisible(false);

				starSpriteBorders[i][j].clearEntityModifiers();
				starSpriteBorders[i][j].setVisible(false);
			}
		}

		starsLayer.setVisible(false);
		scoreTipsLabel.clearEntityModifiers();
		scoreTipsLabel.setVisible(false);
		clearScoreLabel.clearEntityModifiers();
		stageLabel.clearEntityModifiers();
		luckyStarBuyBtn.clearEntityModifiers();
		starNumLabel.clearEntityModifiers();
		optionsBtn.clearEntityModifiers();
		// autoBtn.clearEntityModifiers();
		switchBtn.clearEntityModifiers();
		backSpaceBtn.clearEntityModifiers();
		hammerBtn.clearEntityModifiers();

		moveStageLabel.clearEntityModifiers();
		moveScoreLabel.clearEntityModifiers();

		moveScoreLabel.setVisible(false);
		moveStageLabel.setVisible(false);

		bonusScoreLabel.clearEntityModifiers();
		bonusScoreLabel.setVisible(false);

		remainNumLabel.clearEntityModifiers();
		remainNumLabel.setVisible(false);
	}

	// 隐藏关卡、目标、当前分数的label并拉起计分板
	private void hideAllLabelsAndPushOutScoreBoard() {
		MoveModifier stageLabelOut = new MoveModifier(0.5f, 35f, 35f,
				stageLabel.getY(), -151);
		MoveModifier clearScoreOut = new MoveModifier(0.5f,
				clearScoreLabel.getX(), clearScoreLabel.getX(),
				clearScoreLabel.getY(), -151);
		MoveModifier currentScoreOut = new MoveModifier(0.5f,
				currentScoreLabel.getX(), currentScoreLabel.getX(),
				currentScoreLabel.getY(), -currentScoreLabel.getHeight());

		stageLabel.registerEntityModifier(stageLabelOut);
		clearScoreLabel.registerEntityModifier(clearScoreOut);
		currentScoreLabel.registerEntityModifier(currentScoreOut);

		optionsBtn.registerEntityModifier(new FadeOutModifier(0.5f));
		// autoBtn.registerEntityModifier(new FadeOutModifier(0.5f));
		switchBtn.registerEntityModifier(new FadeOutModifier(0.5f));
		backSpaceBtn.registerEntityModifier(new FadeOutModifier(0.5f));
		luckyStarBuyBtn.registerEntityModifier(new FadeOutModifier(0.5f));
		hammerBtn.registerEntityModifier(new FadeOutModifier(0.5f));
		starNumLabel.registerEntityModifier(new FadeOutModifier(0.5f));

		bonusScoreLabel.setVisible(false);
		remainNumLabel.setVisible(false);
		stageClearSprite.setVisible(false);
	}

	// 刷新显示的星星数量
	public void refreshStarNum() {
		starNumLabel.setText(String.valueOf(SPUtils.getLuckStarNum(activity)));
		starNumLabel.setRightPositionX(580);
	}

	private void disableStarTouch() {
		isStarsStatic = false;
	}

	private void quitHammerState() {
		isInHammerState = false;
		hammerBtn.clearEntityModifiers();
		hammerBtn.setScale(0.75f);
		removeNode(hammerTips);
		removeNode(hammerSprite);
		Position position = selectedStarList.get(0);
		int x = position.getX();
		int y = position.getY();

		starSprites[x][y].setScale(1);
		starSprites[x][y].setZIndex(0);
		starSpriteBorders[x][y].setScale(1);
		optionsBtn.setEnabled(true);
		// autoBtn.setEnabled(true);
		switchBtn.setEnabled(true);
		backSpaceBtn.setEnabled(true);
		luckyStarBuyBtn.setEnabled(true);
	}

	// 在选项界面点击了“主菜单”按钮
	public void showMainMenu() {
		if (isInHammerState) {
			LogUtils.v(TAG, "当前处于锤子状态中，退出锤子状态...");
			isInHammerState = false;
			quitHammerState();
			clearCurrentSelectedStarsState();
			checkIfNoBlockPopableAndDoStuff();
		} else {
			isAutoClick = false;
			disableStarTouch();
			disableAllBtns();
			pauseAllActionsAndHideStars();
			hideAllLabelsAndPushOutScoreBoard();
			saveGameData();

			((PopStar) activity).hideBanner();
		}
	}

	// 点击新游戏之后，清楚保存的关卡以及分数
	public void clearStageAndScoreData() {
		stage = 1;
		savedStage = stage;
		savedScore = intToDesString(0);
		visualScore = savedScore;
		desScore = savedScore;
		isStageClearShown = false;
		stageClearScore = longToDesString(GameConstants.getClearScore(stage));
		rebornNum = 1;

		SPUtils.saveStage(activity, savedStage);
		SPUtils.saveCurrentScore(activity, desStringToInt(savedScore));
		SPUtils.saveHammerUsedNum(activity, 0);
		SPUtils.saveSwitchUsedNum(activity, 0);
	}

	// 点击新游戏/继续游戏之后需要执行该方法来设置关卡、目标分数、当前分数展示的内容
	private void initTotalLabelStringsShow() {
		stageLabel.setText("关卡：" + String.valueOf(stage));
		clearScoreLabel.setText("目标:"
				+ String.valueOf(desStringToLong(stageClearScore)));
		clearScoreLabel.setCentrePositionX(320);
		// 长度过长，将会被右边的星星图标盖住，所以，这时候就右对齐星星图标
		if (String.valueOf(desStringToLong(stageClearScore)).length() > 7) {
			clearScoreLabel.setRightPositionX(luckyStarBuyBtn.getLeftX() - 5);
		}
		currentScoreLabel.setText(String
				.valueOf((int) desStringToFloat(visualScore)));
		LogUtils.v(TAG, "当前分数宽度：" + currentScoreLabel.getWidth());
		currentScoreLabel.setPositionX(320 - currentScoreLabel.getWidthHalf());
		moveStageLabel.setText("关卡" + stage);
		moveScoreLabel.setText("目标分数" + desStringToLong(stageClearScore));
		starNumLabel.setText(String.valueOf(SPUtils.getLuckStarNum(activity)));
		starNumLabel.setRightPositionX(580);

		lastCurrentScoreLength = String.valueOf(
				(int) desStringToFloat(visualScore)).length();
	}

	private void initTotalLabelStringsShowWithoutCurrentScoreLabel() {
		stageLabel.setText("关卡：" + String.valueOf(stage));
		clearScoreLabel.setText("目标:"
				+ String.valueOf(desStringToLong(stageClearScore)));
		clearScoreLabel.setCentrePositionX(320);
		if (String.valueOf(desStringToLong(stageClearScore)).length() > 7) {
			clearScoreLabel.setRightPositionX(luckyStarBuyBtn.getLeftX() - 5);
		}
		moveStageLabel.setText("关卡" + stage);
		moveScoreLabel.setText("目标分数" + desStringToLong(stageClearScore));
		// 防止显现在屏幕内
		moveScoreLabel.setRightPositionX(0);
		moveStageLabel.setRightPositionX(0);

		starNumLabel.setText(String.valueOf(SPUtils.getLuckStarNum(activity)));
		starNumLabel.setRightPositionX(580);
	}

	// 点击新游戏
	public void doNewGameStuff() {
		setInResultState(false);
		LogUtils.v(TAG, "doNewGameStuff");
		clearUpdateVariables();
		isQuickBuyDialogShowed = false;
		isStarsStatic = false;
		isInHammerState = false;
		isRollBackEnabled = false;
		isItemUsedTriggerShowed = false;
		stageNum = 1;
		trackmoveStack.clear();
		clearStageAndScoreData();
		initTotalLabelStringsShow();
		generateStarSigns();
		showMoveAndLoadStars();
		enableAllBtns();
		((PopStar) getActivity()).showBanner();
	}

	private void enableAllBtns() {
		hammerBtn.setEnabled(true);
		hammerBtn.setIgnoreTouch(false);

		optionsBtn.setEnabled(true);
		optionsBtn.setIgnoreTouch(false);

		// autoBtn.setEnabled(true);
		// autoBtn.setIgnoreTouch(false);

		switchBtn.setEnabled(true);
		switchBtn.setIgnoreTouch(false);

		backSpaceBtn.setEnabled(true);
		backSpaceBtn.setIgnoreTouch(false);

		backSpaceBtn.setColor(0.5f, 0.5f, 0.5f);

		luckyStarBuyBtn.setEnabled(true);
		luckyStarBuyBtn.setIgnoreTouch(false);
	}

	private void enableAllBtnsWhitouSetColor() {
		hammerBtn.setEnabled(true);
		hammerBtn.setIgnoreTouch(false);

		optionsBtn.setEnabled(true);
		optionsBtn.setIgnoreTouch(false);

		// autoBtn.setEnabled(true);
		// autoBtn.setIgnoreTouch(false);

		switchBtn.setEnabled(true);
		switchBtn.setIgnoreTouch(false);

		backSpaceBtn.setEnabled(true);
		backSpaceBtn.setIgnoreTouch(false);

		luckyStarBuyBtn.setEnabled(true);
		luckyStarBuyBtn.setIgnoreTouch(false);
	}

	private void disableAllBtns() {
		hammerBtn.setEnabled(false);
		hammerBtn.setIgnoreTouch(true);

		optionsBtn.setEnabled(false);
		optionsBtn.setIgnoreTouch(true);

		// autoBtn.setEnabled(false);
		// autoBtn.setIgnoreTouch(true);

		switchBtn.setEnabled(false);
		switchBtn.setIgnoreTouch(true);

		backSpaceBtn.setEnabled(false);
		backSpaceBtn.setIgnoreTouch(true);

		luckyStarBuyBtn.setEnabled(false);
		luckyStarBuyBtn.setIgnoreTouch(true);
	}

	// 聚集starSigns
	private void gatherStarSigns() {
		Map<Integer, List<Map<Integer, Position>>> moveDownPositionList = new HashMap<Integer, List<Map<Integer, Position>>>();
		for (int i = 0; i < MAX_LINE; i++) {
			int moveStep = 0;
			List<Map<Integer, Position>> positionList = new ArrayList<Map<Integer, Position>>();
			for (int j = 0; j < MAX_LINE; j++) {
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
			gatherHorizontalStarSigns();
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
			gatherHorizontalStarSigns();
		}
	}

	private void gatherHorizontalStarSigns() {
		int[] xs = new int[MAX_LINE];
		for (int i = 0; i < xs.length; i++) {
			xs[i] = i;
		}
		List<Integer> nullList = new ArrayList<Integer>();
		for (int i = 0; i < xs.length; i++) {
			boolean isNullColumn = true;
			for (int j = 0; j < MAX_LINE; j++) {
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
			for (int i = nullX + 1; i < MAX_LINE; i++) {
				if (nullList.contains(i)) {
					tempStep++;
				} else {
					for (int j = 0; j < MAX_LINE; j++) {
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

	// 保存游戏数据(星星位置、分数、当前关)
	public void saveGameData() {
		if (starSigns != null) {
			StringBuffer sb = new StringBuffer();
			gatherStarSigns();
			for (int i = 0; i < MAX_LINE; i++) {
				for (int j = 0; j < MAX_LINE; j++) {
					sb.append(starSigns[i][j]);
					sb.append(",");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append(";");
			}
			sb.deleteCharAt(sb.length() - 1);
			SPUtils.saveStars(activity, sb.toString());
			SPUtils.saveCurrentScore(activity, desStringToInt(savedScore));
			SPUtils.saveStage(activity, savedStage);
			SPUtils.saveRebornNum(activity, rebornNum);
			LogUtils.v(TAG, "保存星星状态：" + sb.toString());
			LogUtils.v(TAG, "保存当前分数：" + savedScore + ",当前关卡：" + savedStage);
//			SaveGameData.saveJsonData(activity);
		}
	}

	// 加载星星数据、重置状态数据，比如：是否成功的标示
	private void loadStarsDataFromFileAndResetStateValues() {
		starSigns = new int[10][10];
		String starData = SPUtils.getStars(activity);
		String[] columnsData = starData.split(";");
		for (int i = 0; i < columnsData.length; i++) {
			String[] rowsData = columnsData[i].split(",");
			for (int j = 0; j < rowsData.length; j++) {
				starSigns[i][j] = Integer.parseInt(rowsData[j]);
			}
		}
		stage = (int) SPUtils.getStage(activity);
		savedStage = stage;
		visualScore = intToDesString((int) SPUtils.getCurrentScore(activity));
		savedScore = visualScore;
		desScore = savedScore;
		stageClearScore = longToDesString(GameConstants
				.getClearScore(savedStage));
		isStageClearShown = false;
	}

	// 点击继续游戏
	public void doContinueGameStuff() {
		setInResultState(false);
		rebornNum = SPUtils.getRebornNum(activity);
		isQuickBuyDialogShowed = false;
		isInHammerState = false;
		isStageClearShowEnabled = true;
		isRollBackEnabled = false;
		isItemUsedTriggerShowed = false;
		stageNum = 1;
		trackmoveStack.clear();
		clearUpdateVariables();
		loadStarsDataFromFileAndResetStateValues();
		initTotalLabelStringsShow();
		initContinueStars();
		showScoreBoard();
		enableAllBtns();
		if (isDead()) {
			nextStageOrGameOver();
		} else {
			// 通过第一关的奖励
			if (stage == 2 && !SPUtils.isStageOneAward(activity)) {
				doStage1AwardStuff();
			}
			// 通过第二关的奖励
			if (stage == 3 && !SPUtils.isStageTwoAward(activity)) {
				doStage2AwardStuff();
			}

			if (stage == 4 || stage == 10) {
				showQuickBuyDialog(BUY_TYPE.NONE);
			}
		}
		((PopStar) getActivity()).showBanner();
	}

	// 展示分数板
	private void showScoreBoard() {
		MoveModifier stageMoveIn = new MoveModifier(0.5f, 35, 35, -107
				+ stageLabel.getHeight() / 2, 49 - stageLabel.getHeight() / 2);
		MoveModifier clearScoreMoveIn = new MoveModifier(0.5f,
				320 - clearScoreLabel.getWidth() / 2,
				320 - clearScoreLabel.getWidth() / 2, -151,
				49 - clearScoreLabel.getHeight() / 2);
		MoveModifier currentScoreMoveIn = new MoveModifier(0.5f,
				320 - currentScoreLabel.getWidthHalf(),
				320 - currentScoreLabel.getWidthHalf(), 0,
				156 - currentScoreLabel.getHeight() / 2);

		stageLabel.registerEntityModifier(stageMoveIn);
		stageLabel.setVisible(true);

		clearScoreLabel.setAlpha(1);
		clearScoreLabel.registerEntityModifier(clearScoreMoveIn);
		clearScoreLabel.setVisible(true);

		currentScoreLabel.registerEntityModifier(currentScoreMoveIn);
		currentScoreLabel.setVisible(true);

		hammerBtn.setAlpha(0);
		hammerBtn.setVisible(true);

		optionsBtn.setAlpha(0);
		optionsBtn.setVisible(true);

		// autoBtn.setAlpha(0);
		// autoBtn.setVisible(true);

		switchBtn.setAlpha(0);
		switchBtn.setVisible(true);

		backSpaceBtn.setAlpha(0);
		backSpaceBtn.setVisible(true);

		luckyStarBuyBtn.setAlpha(0);
		luckyStarBuyBtn.setVisible(true);

		starNumLabel.setAlpha(0);
		starNumLabel.setVisible(true);

		hammerBtn.registerEntityModifier(new FadeInModifier(0.5f));
		optionsBtn.registerEntityModifier(new FadeInModifier(0.5f));
		// autoBtn.registerEntityModifier(new FadeInModifier(0.5f));
		switchBtn.registerEntityModifier(new FadeInModifier(0.5f));
		backSpaceBtn.registerEntityModifier(new FadeInModifier(0.5f));
		luckyStarBuyBtn.registerEntityModifier(new FadeInModifier(0.5f));
		starNumLabel.registerEntityModifier(new FadeInModifier(0.5f));
	}

	// 初始化“继续”的星星
	private void initContinueStars() {
		starsLayer.setVisible(true);
		for (int j = 0; j < MAX_LINE; j++) {
			for (int i = 0; i < MAX_LINE; i++) {
				// 因为有可能在进行移动过程中被暂停，如果不进行设置，位置有可能错乱
				starSprites[i][j].setPosition(borderWidth / 2 + i * borderWidth
						- starWidth / 2, 960 - borderHeight * (j + 1)
						+ (borderHeight - starHeight) / 2
						- GameConstants.AD_BANNER_HEIGHT);
				starSprites[i][j].setCustomAttributes(starSigns[i][j], i, j,
						false);
				starSprites[i][j].setIgnoreTouch(false);
				starSprites[i][j].setScale(1);
				if (starSigns[i][j] != GameConstants.STAR_NONE) {
					// TODO 设置展示的星星类型
					starSprites[i][j].setCurrentTileIndex(starSigns[i][j]);
					starSprites[i][j].setVisible(true);
				}
			}
		}
		doAfterLoadStarsStuff();
		enableAllStarSpritesTouch();
	}

	/**
	 * 商城购买后返回
	 */
	public void buyback() {

		buyDialog.setDialogDismissListener(new DialogDismissListener() {
			public void onDialogDismiss() {
				ResourceManager.unloadBuyDialogTextures();
				if (buyType == BUY_TYPE.REBORN) {
					// SPUtils.saveLuckStarNum(
					// activity,
					// SPUtils.getLuckStarNum(activity)
					// + QUICK_BUY_NUM
					// - GameConstants
					// .getRebornCost(
					// stage,
					// rebornNum));
					// refreshStarNum();
					showStage(stage, true);
				}
				if (buyType == BUY_TYPE.HAMMER) {
					// SPUtils.saveLuckStarNum(
					// activity,
					// SPUtils.getLuckStarNum(activity)
					// + QUICK_BUY_NUM);
					// refreshStarNum();
					doAddHammer();
				}
				if (buyType == BUY_TYPE.SWITCH
						|| buyType == BUY_TYPE.END_SWITCH) {
					// SPUtils.saveLuckStarNum(
					// activity,
					// SPUtils.getLuckStarNum(activity)
					// + QUICK_BUY_NUM);
					doSwitch();
					if (buyType == BUY_TYPE.END_SWITCH) {
						isQuickBuyDialogShowed = true;
					}
				}
				if (buyType == BUY_TYPE.ROLL_BACK) {
					// SPUtils.saveLuckStarNum(
					// activity,
					// SPUtils.getLuckStarNum(activity)
					// + QUICK_BUY_NUM);
					doRollBack();
				}

				if (buyType == BUY_TYPE.NONE) {
					popScene.onSceneResume();
					// SPUtils.saveLuckStarNum(
					// activity,
					// SPUtils.getLuckStarNum(activity)
					// + QUICK_BUY_NUM);
					refreshStarNum();
				}

				if (buyType == BUY_TYPE.STAGE_6) {
					// SPUtils.saveLuckStarNum(
					// activity,
					// SPUtils.getLuckStarNum(activity)
					// + QUICK_BUY_NUM);
					// refreshStarNum();
					int costNum = GameConstants.getRebornCost(stage, rebornNum);
					buildGoonDialog(costNum);
					goonDialog.show();
				}
			}
		});
		buyDialog.dismissWithAnimamtion();
	}
}
