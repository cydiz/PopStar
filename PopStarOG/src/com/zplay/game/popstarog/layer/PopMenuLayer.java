package com.zplay.game.popstarog.layer;

import java.util.Random;

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
import com.e7studio.android.e7appsdk.utils.PhoneInfoHandler;
import com.e7studio.android.e7appsdk.utils.WebParamsMapBuilder;
import com.e7studio.android.e7appsdk.utils.WebTask;
import com.e7studio.android.e7appsdk.utils.WebTaskHandler;
import com.orange.engine.handler.timer.TimerHandler;
import com.orange.entity.IEntity;
import com.orange.entity.layer.Layer;
import com.orange.entity.modifier.DelayModifier;
import com.orange.entity.modifier.FadeInModifier;
import com.orange.entity.modifier.FadeOutModifier;
import com.orange.entity.modifier.IEntityModifier.IEntityModifierListener;
import com.orange.entity.modifier.LoopEntityModifier;
import com.orange.entity.modifier.MoveModifier;
import com.orange.entity.modifier.ParallelEntityModifier;
import com.orange.entity.modifier.RotationByModifier;
import com.orange.entity.modifier.ScaleModifier;
import com.orange.entity.modifier.SequenceEntityModifier;
import com.orange.entity.primitive.Line;
import com.orange.entity.sprite.ButtonSprite;
import com.orange.entity.sprite.ButtonSprite.OnClickListener;
import com.orange.entity.sprite.Sprite;
import com.orange.entity.text.Text;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.res.FontRes;
import com.orange.res.RegionRes;
import com.orange.util.HorizontalAlign;
import com.orange.util.color.Color;
import com.orange.util.modifier.IModifier;
import com.orange.util.modifier.IModifier.IModifierListener;
import com.tendcloud.tenddata.TCAgent;
import com.zplay.game.popstarog.PopStar;
import com.zplay.game.popstarog.custom.ClickThroughAbsoluteLayout;
import com.zplay.game.popstarog.custom.Dialog;
import com.zplay.game.popstarog.custom.DialogDismissListener;
import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.pay.PayCallback;
import com.zplay.game.popstarog.scene.AboutScene;
import com.zplay.game.popstarog.scene.OZScene;
import com.zplay.game.popstarog.scene.PopScene;
import com.zplay.game.popstarog.scene.ShopScene;
import com.zplay.game.popstarog.utils.AnnouncementShower;
import com.zplay.game.popstarog.utils.BlinkModifierMaker;
import com.zplay.game.popstarog.utils.ButtonMaker;
import com.zplay.game.popstarog.utils.ConfigValueHandler;
import com.zplay.game.popstarog.utils.Encrypter;
import com.zplay.game.popstarog.utils.OperatingActivitiesCallback;
import com.zplay.game.popstarog.utils.ResourceManager;
import com.zplay.game.popstarog.utils.SPUtils;
import com.zplay.game.popstarog.utils.SaveGameData;
import com.zplay.game.popstarog.utils.SizeHelper;
import com.zplay.game.popstarog.utils.SoundUtils;
import com.zplay.game.popstarog.utils.SpriteMaker;
import com.zplay.game.popstarog.utils.TextMaker;
import com.zplay.game.popstarog.utils.Utils;
import com.zplay.game.popstarog.utils.sp.ConstantsHolder;
import com.zplay.game.popstarog.utils.sp.PhoneInfoGetter;
import com.zplay.huodongsdk.UpdateClass;
import com.zplay.huodongsdk.ZplayHDsdk;

@SuppressWarnings("deprecation")
@SuppressLint({ "SetJavaScriptEnabled", "InflateParams" })
public class PopMenuLayer extends Layer {
	private final static String TAG = "PopMenuLayer";
	private PopScene popScene;

	private Sprite officalSprite;
	private ButtonSprite newGameBtn;
	private ButtonSprite ozModeBtn;
	private ButtonSprite shopBtn;
	private ButtonSprite newbieGiftBtn;
	private ButtonSprite redPacketBtn; // 红包按钮 

	private Sprite[] lightSprites = new Sprite[5];
	private Text highScoreLabel;

	private long savedHighScore;
	private Sprite highScoreSprite;

	private ButtonSprite announceBtn;
	private ButtonSprite cdKeyBtn;
	private ButtonSprite aboutBtn;

	private boolean isCDKeyinProgress = false;
	private VertexBufferObjectManager vertextBufferObjectManager = null;

	private long lastCDKeyTime = 0;

	private Activity activity;
	private TimerHandler timerHandler;
	private static ButtonSprite redDot;

	public PopMenuLayer(PopScene scene) {
		super(GameConstants.BASE_WIDTH, GameConstants.BASE_HEIGHT, scene);
		
		this.popScene = scene;
		setSize(GameConstants.BASE_WIDTH, GameConstants.BASE_HEIGHT);
		vertextBufferObjectManager = getVertexBufferObjectManager();
		activity = getActivity();
		addLights();
		startLightTwinkleAction();
		addHighScoreBg();
		addHighScoreLable();
		addAnouncement();
		addCDKey();
		addAbout();
		addOfficialSign();
		addBtns();
		showBtnsAnimation();
//		dorain();
		final OperatingActivitiesCallback callback = new OperatingActivitiesCallback() {
			
			@Override
			public void operatingActivitiesCallback() {
				showTodayReward();
			}

			@Override
			public void showNewVersionDownLoadCallback() {
				showNewVersionDownLoad();
			}
		};
		((PopStar) activity).initHDSDK(callback);
	}

	/**
	 * 显示今日奖励领取界面 add by lvjibin
	 */
	private void showTodayReward() {
		final String todayRewards = SPUtils.getTodayRewards(activity);
		if (todayRewards.trim().equals("zero_reawards")) {
			// 无奖励或已经领取了奖励zero_reawards
		} else {
			showRewardsToday(todayRewards.trim());
		}
	}

	private void addAbout() {
		aboutBtn = ButtonMaker.makeFromSingleImgFile(558, 694, "about",
				vertextBufferObjectManager);
		aboutBtn.setPosition(479, 847);
		attachChild(aboutBtn);
		aboutBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				ResourceManager.loadAboutSceneResources(getActivity());
				popScene.startScene(AboutScene.class);
			}
		});
		redDot = ButtonMaker.makeFromSingleImgFile(558, 694, "reddot",
				vertextBufferObjectManager);
		redDot.setPosition(489+aboutBtn.getWidthHalf(), 847);
		attachChild(redDot);
		redDot.setVisible(false);
		//设置不可见 红点点
		aboutBtn.setScale(0);
	}

	//
	// // 公告
	private void addAnouncement() {
		announceBtn = new ButtonSprite(0, 0, RegionRes.getTextureRegion("box"),
				vertextBufferObjectManager);
		announceBtn.setPosition(271, 850);
		announceBtn.setIgnoreTouch(false);
		announceBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				showAnnouncement();
			}
		});
		announceBtn.setScale(0);
		attachChild(announceBtn);
		if (!SPUtils.isAnnouncementShow(activity)) {
			announceBtn.setVisible(false);
		}
	}

	//
	public void showAnnouncement() {
		TCAgent.onEvent(activity, "点击公告按钮");
		AnnouncementShower.showAnnouncement(activity, popScene,
				AnnouncementShower.CLICK);
	}

	//
	// // 兑换码
	private void addCDKey() {
		cdKeyBtn = new ButtonSprite(0, 0, RegionRes.getTextureRegion("cdkey"),
				vertextBufferObjectManager);
		cdKeyBtn.setIgnoreTouch(false);
		cdKeyBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				TCAgent.onEvent(activity, "点击兑换码");
				showCDKey();
			}
		});
		cdKeyBtn.setPosition(76, 850);
		attachChild(cdKeyBtn);
		cdKeyBtn.setScale(0);
	}

	public void showCDKey() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastCDKeyTime >= 1000 || currentTime < lastCDKeyTime) {
			lastCDKeyTime = currentTime;
			activity.runOnUiThread(new Runnable() {
				public void run() {
					final android.app.Dialog dialog = new android.app.Dialog(
							activity, Utils.getResByID(activity, "zplayDialogFull", "style"));
					ClickThroughAbsoluteLayout contentView = new ClickThroughAbsoluteLayout(
							activity);
					dialog.setContentView(contentView);
					dialog.getWindow().setWindowAnimations(
							Utils.getResByID(activity, "zplayDialogAnimScale", "style"));
					ImageView bgView = new ImageView(activity);
					bgView.setBackgroundResource(Utils.getResByID(activity, "alert_tips_bg", "drawable"));

					AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
							(int) SizeHelper.xOGUnitToPixel(579),
							(int) SizeHelper.yOGUnitToPixel(631),
							(int) SizeHelper.xOGUnitToPixel(30),
							(int) SizeHelper.yOGUnitToPixel(265));
					contentView.addView(bgView, params);

					Button closeBtn = new Button(activity);
					closeBtn.setBackgroundResource(Utils.getResByID(activity, "options_quit", "drawable"));
					params = new AbsoluteLayout.LayoutParams((int) SizeHelper
							.xOGUnitToPixel(48), (int) SizeHelper
							.yOGUnitToPixel(48), (int) SizeHelper
							.xOGUnitToPixel(536), (int) SizeHelper
							.yOGUnitToPixel(273));
					contentView.addView(closeBtn, params);

					ImageView tipsView = new ImageView(activity);
					tipsView.setBackgroundResource(Utils.getResByID(activity, "qhxyx", "drawable"));

					params = new AbsoluteLayout.LayoutParams((int) SizeHelper
							.xOGUnitToPixel(389), (int) SizeHelper
							.yOGUnitToPixel(61), (int) SizeHelper
							.xOGUnitToPixel(127), (int) SizeHelper
							.yOGUnitToPixel(344));
					contentView.addView(tipsView, params);

					ImageView tipsView2 = new ImageView(activity);
					tipsView2.setBackgroundResource(Utils.getResByID(activity, "qhxyx_tips", "drawable"));

					params = new AbsoluteLayout.LayoutParams((int) SizeHelper
							.xOGUnitToPixel(399), (int) SizeHelper
							.yOGUnitToPixel(135), (int) SizeHelper
							.xOGUnitToPixel(120), (int) SizeHelper
							.yOGUnitToPixel(433));
					contentView.addView(tipsView2, params);

					final EditText textBtn = new EditText(activity);
					textBtn.setBackgroundColor(android.graphics.Color.WHITE);

					textBtn.setTextColor(android.graphics.Color.RED);
					textBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
							SizeHelper.xOGUnitToPixel(25));
					params = new AbsoluteLayout.LayoutParams((int) SizeHelper
							.xOGUnitToPixel(482),
							AbsoluteLayout.LayoutParams.WRAP_CONTENT,
							(int) SizeHelper.xOGUnitToPixel(79),
							(int) SizeHelper.yOGUnitToPixel(606));
					contentView.addView(textBtn, params);
					closeBtn.setOnClickListener(new View.OnClickListener() {
						public void onClick(View arg0) {
							dialog.dismiss();
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
							(int) SizeHelper.xOGUnitToPixel(185),
							(int) SizeHelper.yOGUnitToPixel(730));
					
					Button lqBtn = new Button(activity);
					lqBtn.setBackgroundResource(Utils.getResByID(activity, "lq", "drawable"));
					LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					layout.addView(lqBtn, params2);
					contentView.addView(layout, params);
					
					lqBtn.setOnClickListener(new View.OnClickListener() {
						public void onClick(View arg0) {
							TCAgent.onEvent(activity, "点击领取兑换码");
							getStarsByCDKey(dialog, textBtn.getText()
									.toString().trim());
						}
					});
					dialog.show();
				}
			});
		}
	}

	public void getStarsByCDKey(final android.app.Dialog dialog,
			final String cdKey) {
		if (isCDKeyinProgress) {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(activity, "请等待本次兑换完成之后再执行下次兑换操作...",
							Toast.LENGTH_SHORT).show();
				}
			});
		} else {
			isCDKeyinProgress = true;
			if (cdKey.equals("")) {
				isCDKeyinProgress = false;
				activity.runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(activity, "输入的兑换码为空，请重新输入之后再点击领取",
								Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				activity.runOnUiThread(new Runnable() {
					@SuppressWarnings("unchecked")
					public void run() {
						String gameID = ConfigValueHandler.getGameID(activity);
						String channelID = ConfigValueHandler
								.getChannel(activity);
						String deviceID = PhoneInfoHandler.getIMEI(activity);
						String gameVersion = InstalledAppInfoHandler
								.getAppVersionName(activity,
										activity.getPackageName());
						String sign = Encrypter.doMD5EncodeWithLowercase(gameID
								+ channelID + deviceID + cdKey + "zplay888");
						String jsonString = JSONBuilder
								.buildJSON(
										MapBuilder.buildMap(
												new String[] { "data", "sign" },
												new Object[] {
														MapBuilder
																.buildMap(
																		new String[] {
																				"gameID",
																				"channelID",
																				"device",
																				"devicetype",
																				"gameVersion",
																				"cdkey",
																				"ts" },
																		new String[] {
																				gameID,
																				channelID,
																				deviceID,
																				"android",
																				gameVersion,
																				cdKey,
																				String.valueOf(System
																						.currentTimeMillis()) }),
														sign })).toString();
						new WebTaskHandler(
								activity,
								new WebTask() {
									public void doTask(String data, String msg) {
										isCDKeyinProgress = false;
										if (data == null) {
											Toast.makeText(
													activity,
													"与服务器交互失败，兑换失败，请检查网络后重试...",
													Toast.LENGTH_SHORT).show();
										} else {
											TCAgent.onEvent(activity, "领取兑换码成功");
											if (dialog != null
													&& dialog.isShowing()) {
												dialog.dismiss();
											}
											int num = 0;
											try {
												num = Integer.parseInt(data
														.trim());
											} catch (Exception ex) {
											}
											if (num > 0) {
												Toast.makeText(activity,
														"兑换成功，兑换数量：" + num,
														Toast.LENGTH_SHORT)
														.show();
												SPUtils.saveLuckStarNum(
														activity,
														SPUtils.getLuckStarNum(activity)
																+ num);
											} else {

												TCAgent.onEvent(activity,
														"领取兑换码失败");

												Toast.makeText(activity,
														"兑换失败，无效的兑换码",
														Toast.LENGTH_SHORT)
														.show();
											}
										}
									}
								}, true, true, null, Utils.getResByID(activity, "cd_key_handling", "string"),
								false,
								false).execute(WebParamsMapBuilder
								.buildParams(
										"http://popstar.zplay.cn/cdkey/cdkey/cdkeyin.php",
										jsonString));
					}
				});
			}
		}
	}

	public void onResume() {
		// 联通和电信是直接显示红包的
		if (SPUtils.isShowRedBtnExpectOneCent(activity)) {
			// 移动的先显示1cent再显示红包购买
			if (SPUtils.isCentAlreadyBuy(activity)) {
				newbieGiftBtn.setVisible(false);
			}
			redPacketBtn.setVisible(true);
		} else {
			// 移动的先显示1cent再显示红包购买
			if (SPUtils.isCentAlreadyBuy(activity)) {
				newbieGiftBtn.setVisible(false);
			}
			if (SPUtils.isShowRedPacket(activity)) {
				redPacketBtn.setVisible(true);
			} else {
				redPacketBtn.setVisible(false);
			}
		}
	}

	public void onPause() {
		if (popScene.isGameOn()) {
			saveHighScore();
		}
	}

	public void receiveCurrentScore(int score) {
		if (savedHighScore < score) {
			savedHighScore = score;
		}
		highScoreLabel.setText(String.valueOf(savedHighScore));
	}

	// // 展示新游戏按钮的显现动画
	public void showBtnsAnimation() {
		newGameBtn.registerEntityModifier(new SequenceEntityModifier(
				new DelayModifier(0.4f), new ScaleModifier(0.2f, 0f, 1f)));

		shopBtn.registerEntityModifier(new SequenceEntityModifier(
				new DelayModifier(0.6f), new ScaleModifier(0.2f, 0f, 1f)));
		ozModeBtn.registerEntityModifier(new SequenceEntityModifier(
				new DelayModifier(0.5f), new ScaleModifier(0.2f, 0f, 1f)));
		newbieGiftBtn.registerEntityModifier(new SequenceEntityModifier(
				new DelayModifier(0.7f), new ScaleModifier(0.2f, 0f, 1f)));
		redPacketBtn.registerEntityModifier(new SequenceEntityModifier(
				new DelayModifier(0.7f), new ScaleModifier(0.2f, 0f, 1f), getRedPacketBtnModifier()));

		aboutBtn.registerEntityModifier(new SequenceEntityModifier(
				new DelayModifier(1.0f), new ParallelEntityModifier(
						new ScaleModifier(0.2f, 0f, 1f),
						new RotationByModifier(0.2f, 360))));
		
		redDot.registerEntityModifier(new SequenceEntityModifier(
				new DelayModifier(1.0f), new ParallelEntityModifier(
						new ScaleModifier(0.2f, 0f, 1f),
						new RotationByModifier(0.2f, 360))));
		
		announceBtn.registerEntityModifier(new SequenceEntityModifier(
				new DelayModifier(1.2f), new ParallelEntityModifier(
						new ScaleModifier(0.2f, 0f, 1f),
						new RotationByModifier(0.2f, 360))));
		cdKeyBtn.registerEntityModifier(new SequenceEntityModifier(
				new DelayModifier(1.4f), new ParallelEntityModifier(
						new ScaleModifier(0.2f, 0f, 1f),
						new RotationByModifier(0.2f, 360))));
	}

	//
	// // 闪烁的灯光
	private void addLights() {
		lightSprites = new Sprite[5];
		// 红
		lightSprites[0] = SpriteMaker.makeSpriteWithSingleImageFile(
				"light_red", vertextBufferObjectManager);
		lightSprites[0].setCentrePosition(80, 320);
		lightSprites[0].setAlpha(100);
		attachChild(lightSprites[0]);

		// 绿
		lightSprites[1] = SpriteMaker.makeSpriteWithSingleImageFile(
				"light_green", vertextBufferObjectManager);
		lightSprites[1].setCentrePosition(330, 320);
		lightSprites[1].setAlpha(255);
		attachChild(lightSprites[1]);

		// 紫色
		lightSprites[2] = SpriteMaker.makeSpriteWithSingleImageFile(
				"light_pink", vertextBufferObjectManager);
		lightSprites[2].setCentrePosition(160, 540);
		lightSprites[2].setAlpha(205);
		attachChild(lightSprites[2]);

		// 黄
		lightSprites[3] = SpriteMaker.makeSpriteWithSingleImageFile(
				"light_yellow", vertextBufferObjectManager);
		lightSprites[3].setCentrePosition(400, 620);
		lightSprites[3].setAlpha(50);
		attachChild(lightSprites[3]);

		// 蓝
		lightSprites[4] = SpriteMaker.makeSpriteWithSingleImageFile(
				"light_blue", vertextBufferObjectManager);
		lightSprites[4].setCentrePosition(540, 460);
		lightSprites[4].setAlpha(0);
		attachChild(lightSprites[4]);
	}

	//
	private void startLightTwinkleAction() {
		LoopEntityModifier blueLoopModifier = new LoopEntityModifier(
				new SequenceEntityModifier(new FadeInModifier(1.6f),
						new DelayModifier(0.5f), new FadeOutModifier(1.6f)));
		// 蓝色
		lightSprites[4].registerEntityModifier(blueLoopModifier);

		// 红色
		LoopEntityModifier redLoopModifier = new LoopEntityModifier(
				new SequenceEntityModifier(new FadeInModifier(2.2f),
						new DelayModifier(0.5f), new FadeOutModifier(2.2f)));
		lightSprites[0].registerEntityModifier(redLoopModifier);

		// 绿色
		LoopEntityModifier greenModifier = new LoopEntityModifier(
				new SequenceEntityModifier(new FadeInModifier(1.8f),
						new DelayModifier(0.5f), new FadeOutModifier(1.8f)));
		lightSprites[1].registerEntityModifier(greenModifier);

		// 粉色
		LoopEntityModifier purpleModifier = new LoopEntityModifier(
				new SequenceEntityModifier(new FadeInModifier(2f),
						new DelayModifier(0.5f), new FadeOutModifier(2f)));
		lightSprites[2].registerEntityModifier(purpleModifier);

		// 黄色
		LoopEntityModifier yellowModifier = new LoopEntityModifier(
				new SequenceEntityModifier(new FadeInModifier(2.4f),
						new DelayModifier(0.5f), new FadeOutModifier(2.4f)));
		lightSprites[3].registerEntityModifier(yellowModifier);
	}

	// // 添加最高分图片
	private void addHighScoreBg() {
		highScoreSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"highscore", vertextBufferObjectManager);

		highScoreSprite.setTopPositionY(31);
		highScoreSprite.setCentrePositionX(320);

		attachChild(highScoreSprite);
	}

	// // 添加最高分的label
	private void addHighScoreLable() {
		savedHighScore = SPUtils.getHighScore(activity);
		LogUtils.v(TAG, "最高分：" + savedHighScore);

		highScoreLabel = new Text(0, 0, FontRes.getFont("35white"),
				"0123456789100123456789", vertextBufferObjectManager);
		highScoreLabel.setText(String.valueOf(savedHighScore));
		highScoreLabel.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		highScoreLabel.setCentrePosition(436, 58);
		attachChild(highScoreLabel);
	}

	//
	// // 添加官方标志
	private void addOfficialSign() {
		officalSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"offical_sign", vertextBufferObjectManager);
		officalSprite.setScale(0.9f);
		officalSprite.setCentrePosition(320, 270);
		attachChild(officalSprite);
	}
	
	/**
	 * 获取红包动画效果
	 */
	private final LoopEntityModifier getRedPacketBtnModifier() {
		// 特效，每隔两秒播放一次抖动动画
		float angle = 30.0f;
		RotationByModifier rotateByL = new RotationByModifier(0.07f, -angle);
		RotationByModifier rotateByR = new RotationByModifier(0.07f, angle);
		DelayModifier delayTime = new DelayModifier(2.0f);
		// SequenceEntityModifier sequence = new SequenceEntityModifier(
		// new ParallelEntityModifier(rotateByL, rotateByR,
		// rotateByR.deepCopy(), rotateByL.deepCopy()), delayTime);
		SequenceEntityModifier sequence = new SequenceEntityModifier(rotateByL,
				rotateByR, rotateByR.deepCopy(), rotateByL.deepCopy(),
				delayTime);
		final LoopEntityModifier loop = new LoopEntityModifier(sequence);
		return loop;
	}

	//
	// // 添加按钮
	private void addBtns() {
		newGameBtn = new ButtonSprite(0, 0,
				RegionRes.getTextureRegion("btn_new"),
				vertextBufferObjectManager);
		newGameBtn.setPosition(166, 467);
		newGameBtn.setCentrePositionX(320);
		newGameBtn.setScale(0);
		newGameBtn.setIgnoreTouch(false);

		newGameBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				doNewGame();
			}
		});
		// 1分钱按钮
		newbieGiftBtn = new ButtonSprite(0, 0,
				RegionRes.getTextureRegion("newbie_gift"),
				vertextBufferObjectManager);
		newbieGiftBtn.setPosition(517, 494);
		newbieGiftBtn.setScale(0);
		newbieGiftBtn.setIgnoreTouch(false);
		newbieGiftBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				ResourceManager.loadCentDialogTextures();
				final Dialog centBuyDialog = new Dialog(popScene);
				centBuyDialog.setSize(640, 960);
				Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile(
						"common_bg", vertextBufferObjectManager);
				bgSprite.setPosition(31, 165);
				centBuyDialog.attachChild(bgSprite);

				ButtonSprite quitBtn = ButtonMaker.makeFromSingleImgFile(563,
						266, "cent_options_quit", vertextBufferObjectManager);
				quitBtn.setPosition(529, 173);
				quitBtn.setScale(0.9f);
				quitBtn.setOnClickListener(new OnClickListener() {
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						centBuyDialog.dismiss();
					}
				});
				centBuyDialog.attachChild(quitBtn);

				// 购买按钮
				final ButtonSprite okBtn = ButtonMaker.makeFromSingleImgFile(320,
						730, "cent_yellow_btn", vertextBufferObjectManager);
				okBtn.setScale(0.9f);
				okBtn.setPosition(173, 659);
				okBtn.setOnClickListener(new OnClickListener() {
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						doBuy1Cent(centBuyDialog,okBtn);
					}
				});
				centBuyDialog.attachChild(okBtn);

				Sprite okTextSprite = SpriteMaker
						.makeSpriteWithSingleImageFile("text_lingqu",
								vertextBufferObjectManager);
				okTextSprite.setScale(0.9f);
				okTextSprite.setPosition(239, 665);
				centBuyDialog.attachChild(okTextSprite);

				LoopEntityModifier loop = new LoopEntityModifier(
						new SequenceEntityModifier(new ScaleModifier(0.5f,
								0.9f, 1.0f),
								new ScaleModifier(0.5f, 1.0f, 0.9f)));
				okBtn.registerEntityModifier(loop);
				okTextSprite.registerEntityModifier(loop.deepCopy());

				Text tipsLabel = TextMaker.make(
						"新手福利来喽！仅需0.01元，获得价\n值一元的10枚幸运星，仅限一次哦！", "30white",
						320, 820, HorizontalAlign.CENTER,
						vertextBufferObjectManager);
				tipsLabel.setCentrePosition(320, 600);
				centBuyDialog.attachChild(tipsLabel);

				Sprite textTen = SpriteMaker.makeSpriteWithSingleImageFile(
						"test_zeng_cdkey", vertextBufferObjectManager);
				textTen.setPosition(218, 349);
				centBuyDialog.attachChild(textTen);

				Sprite title = SpriteMaker.makeSpriteWithSingleImageFile(
						"text_libao_cdkey", vertextBufferObjectManager);
				title.setPosition(162, 249);
				centBuyDialog.attachChild(title);
				centBuyDialog
						.setDialogDismissListener(new DialogDismissListener() {
							public void onDialogDismiss() {
								ResourceManager.unloadCentDialogTextures();
							}
						});
				centBuyDialog.showWithAnimation();
			}
		});
		// 联通或者电信设备没有一分钱
		if (!PhoneInfoGetter.isSimAvaliable(activity)
				|| ((PhoneInfoGetter.getMobileSP(activity).equals(
						ConstantsHolder.CHINA_UNICOME) || PhoneInfoGetter
						.getMobileSP(activity).equals(
								ConstantsHolder.CHINA_TELECOM)))
				|| SPUtils.isCentAlreadyBuy(activity)) {
			newbieGiftBtn.setVisible(false);
		}

		redPacketBtn = new ButtonSprite(0, 0,
				RegionRes.getTextureRegion("redPacket"),
				vertextBufferObjectManager);
		redPacketBtn.registerEntityModifier(getRedPacketBtnModifier());
		

		redPacketBtn.setPosition(517, 494);
		redPacketBtn.setScale(0);
		redPacketBtn.setIgnoreTouch(false);
		redPacketBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				buildBuyDialog(GameConstants.QUICK_BUY_CHARGEPOINT_ID);
				// showRewardsToday("255");
			}
		});
		if (SPUtils.isShowRedPacket(activity)) {
			redPacketBtn.setVisible(true);
		} else {
			redPacketBtn.setVisible(false);
		}

		shopBtn = new ButtonSprite(0, 0,
				RegionRes.getTextureRegion("btn_shop"),
				vertextBufferObjectManager);
		shopBtn.setPosition(175, 744);
		shopBtn.setScale(0);
		shopBtn.setIgnoreTouch(false);
		shopBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				goShopping();
			}
		});

		ozModeBtn = new ButtonSprite(0, 0,
				RegionRes.getTextureRegion("btn_1010"),
				vertextBufferObjectManager);
		ozModeBtn.setPosition(175, 601);
		ozModeBtn.setScale(0);
		ozModeBtn.setIgnoreTouch(false);
		ozModeBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SoundUtils.playButtonClick();
				TCAgent.onEvent(activity, "点击星星连萌按钮");
				goTo1010Scene();
			}
		});

		attachChild(newGameBtn);
		attachChild(shopBtn);
		attachChild(ozModeBtn);
		attachChild(newbieGiftBtn);
		attachChild(redPacketBtn);
	}

	/**
	 * 显示今日奖励
	 * 
	 * @param starNum
	 */
	private void showRewardsToday(final String starNum) {
		ResourceManager.loadRewardsDialog(activity);
		final Dialog dialog = new Dialog(popScene);
		dialog.setSize(GameConstants.BASE_WIDTH, GameConstants.BASE_HEIGHT);
		Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"common_bg", vertextBufferObjectManager);
		bgSprite.setPosition(31, 165);
		bgSprite.setScale(0.8f);
		dialog.attachChild(bgSprite);

		Text title = TextMaker.make("今日奖励", "40white", bgSprite.getCentreX(),
				bgSprite.getCentreY() - 200f * bgSprite.getScaleY(),
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		dialog.attachChild(title);

		// 领取幸运星数量
		Text tipsLabel = TextMaker.make("" + starNum, "rewards", 200,
				bgSprite.getCentreY() + 15, HorizontalAlign.LEFT,
				vertextBufferObjectManager);
		dialog.attachChild(tipsLabel);

		// 幸运星图片
		Sprite starSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"fly_star", vertextBufferObjectManager);
		starSprite.setScale(0.8f);
		starSprite.setCentrePositionY(tipsLabel.getCentreY());
		dialog.attachChild(starSprite);

		// 校正幸运星数量和图片位置
		float gapH = 5.0f;
		float tempW = tipsLabel.getWidth() + starSprite.getWidth() + gapH;
		float baseX = (GameConstants.BASE_WIDTH - tempW) / 2;
		tipsLabel.setPositionX(baseX);
		starSprite.setPositionX(tipsLabel.getRightX() + gapH);

		float gapV = 8.0f;
		// 领取按钮
		ButtonSprite okBtn = ButtonMaker.makeFromSingleImgFile(320, 730,
				"yellow_btn_long", vertextBufferObjectManager);
		okBtn.setScale(0.85f);
		okBtn.setCentrePositionX(GameConstants.BASE_WIDTH / 2);
		okBtn.setBottomPositionY(bgSprite.getCentreY()
				+ bgSprite.getHeightHalf() * bgSprite.getScaleY() - gapV);
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				pButtonSprite.setEnabled(false);
				// doBuy1Cent(centBuyDialog);
				// 保存领取的幸运星数量 add by lvjibin
				SPUtils.saveLuckStarNum(
						activity,
						SPUtils.getLuckStarNum(activity)
								+ Long.parseLong(starNum));
				// 领取之后本地置为不再显示状态
				SPUtils.setTodayRewards(activity, "zero_reawards");

				ZplayHDsdk.writeLogin(getActivity());// 领取之后告诉服务器
				dialog.dismiss();
			}
		});
		dialog.attachChild(okBtn);

		Text oKText = TextMaker.make("领  取", "50white", 320, 705,
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		oKText.setCentrePosition(okBtn.getCentreX(), okBtn.getCentreY());
		dialog.attachChild(oKText);

		dialog.setDialogDismissListener(new DialogDismissListener() {
			public void onDialogDismiss() {
				ResourceManager.unloadCentDialogTextures();
				unregisterUpdateHandler(timerHandler);
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

	private void doBuy1Cent(final Dialog dialog,final ButtonSprite bton) {
		((PopStar) activity).pay(GameConstants.CENT_POINT, new PayCallback() {
			public void callback(int code, String msg) {
				if (code == PayCallback.OK) {
					bton.setEnabled(false);
					dialog.dismiss();
					TCAgent.onEvent(activity, "购买新手礼包");
					SPUtils.saveLuckStarNum(activity,
							SPUtils.getLuckStarNum(activity) + 10);
					newbieGiftBtn.setVisible(false);
					Toast.makeText(activity, "购买成功", Toast.LENGTH_SHORT).show();
					if (SPUtils.isShowRedPacket(activity)) {
						redPacketBtn.setVisible(true);
					}
				}
			}
		});

	}

	private void goTo1010Scene() {
		ResourceManager.load1010SceneTextures();
		popScene.startScene(OZScene.class);
	}

	// 点击菜单按钮
	public void doShowOptions() {
		LogUtils.v(TAG, "展示选项界面");
		SoundUtils.playButtonClick();
		popScene.showOptions();
	}

	public void goShopping() {
		LogUtils.v(TAG, "展示商城界面");
		SoundUtils.playButtonClick();
		popScene.startScene(ShopScene.class);
	}

	// 继续游戏
	public void doResumeGame() {
		SoundUtils.playButtonClick();
		LogUtils.v(TAG, "继续游戏");
		popScene.setGameOn(true);
		disableAllBtns();
		fadeMenuLayerAndLoadGameLayer(false);
	}

	// // 新游戏
	public void doNewGame() {
		TCAgent.onEvent(activity, "新游戏按钮");
		if (!popScene.isGameOn()) {
			SoundUtils.playButtonClick();
			LogUtils.v(TAG, "新游戏");
			if (SPUtils.getCurrentScore(activity) != 0) {
				ResourceManager.loadIphoneDialogTextures();
				final Dialog dialog = new Dialog(popScene);
				Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile(
						"alert_bg", vertextBufferObjectManager);
				bgSprite.setCentrePosition(320, 483);
				dialog.attachChild(bgSprite);

				Text titleLabel = TextMaker.make("存在游戏存档！", "systemFont40",
						320, 397, HorizontalAlign.CENTER,
						vertextBufferObjectManager);
				Text contentLabel = TextMaker.make("想要重新开始新游戏吗？",
						"systemFont30", 320, 454, HorizontalAlign.CENTER,
						vertextBufferObjectManager);
				dialog.attachChild(titleLabel);
				dialog.attachChild(contentLabel);

				ButtonSprite okBtn = ButtonMaker.makeFromSingleImgFile(187,
						547, "alert_ok", vertextBufferObjectManager);
				okBtn.setOnClickListener(new OnClickListener() {
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						onNewGameDialogOK(dialog);
					}
				});
				Text okLabel = TextMaker.make("新游戏", "systemFont30", 187, 547,
						HorizontalAlign.CENTER, vertextBufferObjectManager);
				dialog.attachChild(okBtn);
				dialog.attachChild(okLabel);

				ButtonSprite cancelBtn = ButtonMaker.makeFromSingleImgFile(453,
						547, "alert_cancel", vertextBufferObjectManager);
				cancelBtn.setOnClickListener(new OnClickListener() {
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						dialog.dismiss();
						doResumeGame();
					}
				});
				Text cancelLabel = TextMaker
						.make("继续游戏", "systemFont30", 453, 547,
								HorizontalAlign.CENTER,
								vertextBufferObjectManager);
				dialog.attachChild(cancelBtn);
				dialog.attachChild(cancelLabel);
				dialog.show();

				dialog.setDialogDismissListener(new DialogDismissListener() {
					public void onDialogDismiss() {
						ResourceManager.unloadIphoneDialogTextures();
					}
				});
			} else {
				doNewGameStuff();
			}
		}
	}

	// 点击对话框的确定按钮
	public void onNewGameDialogOK(Dialog dialog) {
		dialog.dismiss();
		doNewGameStuff();
	}

	private void disableAllBtns() {
		newGameBtn.setEnabled(false);
		newGameBtn.setIgnoreTouch(true);
		shopBtn.setEnabled(false);
		shopBtn.setIgnoreTouch(true);
		ozModeBtn.setEnabled(false);
		ozModeBtn.setIgnoreTouch(true);
		newbieGiftBtn.setEnabled(false);
		newbieGiftBtn.setIgnoreTouch(true);
		redPacketBtn.setEnabled(false);
		redPacketBtn.setIgnoreTouch(true);
		announceBtn.setEnabled(false);
		announceBtn.setIgnoreTouch(true);
		redDot.setEnabled(false);
		redDot.setIgnoreTouch(true);
		aboutBtn.setEnabled(false);
		aboutBtn.setIgnoreTouch(true);
		cdKeyBtn.setEnabled(false);
		cdKeyBtn.setIgnoreTouch(true);
	}

	private void doNewGameStuff() {
		popScene.clearSavedGameData();
		popScene.setGameOn(true);
		SPUtils.setStageThreePassed(activity, false);
		disableAllBtns();
		LoopEntityModifier blink = BlinkModifierMaker.make(0.8f, 10);
		blink.addModifierListener(new IModifierListener<IEntity>() {
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
			}

			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				fadeMenuLayerAndLoadGameLayer(true);
			}
		});
		newGameBtn.registerEntityModifier(blink);
	}

	private void stopLightsAnimation() {
		for (int i = 0; i < lightSprites.length; i++) {
			lightSprites[i].clearEntityModifiers();
		}
	}

	private void stopBtnsAnimation() {
		newGameBtn.clearEntityModifiers();
		shopBtn.clearEntityModifiers();
		ozModeBtn.clearEntityModifiers();
		newbieGiftBtn.clearEntityModifiers();
		redPacketBtn.clearEntityModifiers();
		announceBtn.clearEntityModifiers();
		aboutBtn.clearEntityModifiers();
		cdKeyBtn.clearEntityModifiers();
	}
//清除所有组件
	private void fadeOutAllComponents(final boolean isNewGame) {
		for (int i = 0; i < lightSprites.length; i++) {
			FadeOutModifier fadeOut = new FadeOutModifier(0.5f);
			lightSprites[i].registerEntityModifier(fadeOut);
		}
		FadeOutModifier fadeOut = new FadeOutModifier(1f);
		fadeOut.addModifierListener(new IModifierListener<IEntity>() {
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				showGameLayer(isNewGame);
			}
		});
		officalSprite.registerEntityModifier(fadeOut);
		newGameBtn.registerEntityModifier(fadeOut.deepCopy());
		shopBtn.registerEntityModifier(fadeOut.deepCopy());
		ozModeBtn.registerEntityModifier(fadeOut.deepCopy());
		newbieGiftBtn.registerEntityModifier(fadeOut.deepCopy());
		redPacketBtn.registerEntityModifier(fadeOut.deepCopy());
		highScoreLabel.registerEntityModifier(fadeOut.deepCopy());
		highScoreSprite.registerEntityModifier(fadeOut.deepCopy());

		announceBtn.registerEntityModifier(new ParallelEntityModifier(
				new ScaleModifier(1.0f, 1.0f, 0f), new RotationByModifier(1.0f,
						360)));
		redDot.registerEntityModifier(new ParallelEntityModifier(
				new ScaleModifier(1.0f, 1.0f, 0f), new RotationByModifier(1.0f,
						360)));
		aboutBtn.registerEntityModifier(new ParallelEntityModifier(
				new ScaleModifier(1.0f, 1.0f, 0f), new RotationByModifier(1.0f,
						360)));
		cdKeyBtn.registerEntityModifier(new ParallelEntityModifier(
				new ScaleModifier(1.0f, 1.0f, 0f), new RotationByModifier(1.0f,
						360)));

	}

	//
	// // 展示游戏界面
	public void showGameLayer(boolean isNewGame) {
		if (isNewGame) {
			popScene.doNewGameStuff();
		} else {
			popScene.doContinueGameStuff();
		}
	}

	// // 点击新游戏/继续游戏
	public void fadeMenuLayerAndLoadGameLayer(boolean isNewGame) {
		stopBtnsAnimation();
		stopLightsAnimation();
		fadeOutAllComponents(isNewGame);
	}

	//
	// // 从游戏场景返回到菜单界面
	public void showMainMenu() {
		allComponentsStopActions();
		saveHighScore();
		SoundUtils.playStart();
		startLightTwinkleAction();
		fadeInAllFadedOutComponents();
		showBtnsAnimation();
		enableAllBtns();
		SaveGameData.saveJsonData(activity);
	}

	private void buildBuyDialog(final String chargePointID) {
		ResourceManager.loadBuyDialogTextures(activity);
		final Dialog buyDialog = new Dialog(popScene);

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
			}
		});
		quitBtn.setScale(0.8f);
		buyDialog.attachChild(quitBtn);

		// 328/186
		ButtonSprite okBtn = ButtonMaker.makeFromSingleImgFile(320, 774,
				"quick_buy_btn_ok", vertextBufferObjectManager);
		okBtn.setPosition(340, 717);
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				quickBuy(chargePointID, buyDialog);
			}
		});
		buyDialog.attachChild(okBtn);
		//去商城按钮
				ButtonSprite goshopBtn = ButtonMaker.makeFromSingleImgFile(320, 774,
						"btn_go_shop", vertextBufferObjectManager);
				goshopBtn.setPosition(65, 717); 
				
				if(SPUtils.getIsShowBtShop(activity)){
					buyDialog.attachChild(goshopBtn);
				}else {
					okBtn.setPosition(205, 717);
				}
				
				goshopBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
							float pTouchAreaLocalY) {
						goShopping();
					}
				});
		
		buyDialog.setDialogDismissListener(new DialogDismissListener() {
			public void onDialogDismiss() {
				ResourceManager.unloadBuyDialogTextures();

				popScene.onSceneResume();

			}
		});
		buyDialog.show();
	}

	/**
	 * 通过chargingID购买
	 * 
	 * @param chargePointID
	 * @param dialog
	 */
	public void quickBuy(String chargePointID, final Dialog dialog) {
		// 10元计费
		((PopStar) getActivity()).pay(
				chargePointID == null ? GameConstants.QUICK_BUY_CHARGEPOINT_ID : chargePointID, new PayCallback() {
					public void callback(int code, String msg) {
						if (code == PayCallback.OK) {
							dialog.setDialogDismissListener(new DialogDismissListener() {
								public void onDialogDismiss() {
									ResourceManager.unloadBuyDialogTextures();
									SPUtils.saveLuckStarNum(activity, SPUtils.getLuckStarNum(activity)+118);
								}
							});
							dialog.dismissWithAnimamtion();
						}
						Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
					}
				});
	}

	//
	private void allComponentsStopActions() {
		newGameBtn.clearEntityModifiers();
		shopBtn.clearEntityModifiers();
		ozModeBtn.clearEntityModifiers();
		newbieGiftBtn.clearEntityModifiers();
		redDot.clearEntityModifiers();
		redPacketBtn.clearEntityModifiers();
		announceBtn.clearEntityModifiers();
		aboutBtn.clearEntityModifiers();
		cdKeyBtn.clearEntityModifiers();
		officalSprite.clearEntityModifiers();
		highScoreLabel.clearEntityModifiers();
		highScoreSprite.clearEntityModifiers();
	}

	private void enableAllBtns() {
		newGameBtn.setEnabled(true);
		newGameBtn.setIgnoreTouch(false);
		shopBtn.setEnabled(true);
		shopBtn.setIgnoreTouch(false);
		ozModeBtn.setEnabled(true);
		ozModeBtn.setIgnoreTouch(false);
		newbieGiftBtn.setEnabled(true);
		newbieGiftBtn.setIgnoreTouch(false);
		redPacketBtn.setEnabled(true);
		redPacketBtn.setIgnoreTouch(false);
		cdKeyBtn.setEnabled(true);
		cdKeyBtn.setIgnoreTouch(false);
		announceBtn.setEnabled(true);
		announceBtn.setIgnoreTouch(false);
		redDot.setEnabled(true);
		redDot.setIgnoreTouch(true);
		aboutBtn.setEnabled(true);
		aboutBtn.setIgnoreTouch(false);
	}

	// 展示之前fadedOut的组件
	private void fadeInAllFadedOutComponents() {
		FadeInModifier fadeIn = new FadeInModifier(0.5f);
		officalSprite.registerEntityModifier(fadeIn.deepCopy());
		highScoreLabel.registerEntityModifier(fadeIn.deepCopy());
		highScoreSprite.registerEntityModifier(fadeIn.deepCopy());

		newGameBtn.setScale(0);
		newGameBtn.setAlpha(1);

		shopBtn.setScale(0);
		shopBtn.setAlpha(1);

		ozModeBtn.setScale(0);
		ozModeBtn.setAlpha(1);

		newbieGiftBtn.setScale(0);
		newbieGiftBtn.setAlpha(1);

		redPacketBtn.setScale(0);
		redPacketBtn.setAlpha(1);

		announceBtn.setRotation(0);
		announceBtn.setScale(0);

		redDot.setRotation(0);
		redDot.setScale(0);
		
		aboutBtn.setRotation(0);
		aboutBtn.setScale(0);

		cdKeyBtn.setRotation(0);
		cdKeyBtn.setScale(0);
	}

	// // 保存最高分
	public void saveHighScore() {
		LogUtils.v(TAG, "保存最高分");
		SPUtils.saveHighScore(activity, savedHighScore);
		highScoreLabel.setText(String.valueOf(savedHighScore));
	}

	private void showNewVersionDownLoad() {
		ResourceManager.loadNewversionDownLoadTextures(activity);
		final Dialog dialog = new Dialog(popScene);
		dialog.setSize(GameConstants.BASE_WIDTH, GameConstants.BASE_HEIGHT);
		Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"common_bg_new", vertextBufferObjectManager);
		bgSprite.setPosition(31, 165);
		bgSprite.setScale(0.8f);
		dialog.attachChild(bgSprite);

		Text title = TextMaker.make("版本更新", "rewards_new",
				bgSprite.getCentreX(),
				bgSprite.getCentreY() - 200f * bgSprite.getScaleY(),
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		dialog.attachChild(title);

		Text tipsLabel = TextMaker
				.make("全新的中文版上线了\n\40\40\40\40新体验强势登场\n\40\40\40\40\40\40更新即送30 \40\40\40\40,抢\b",
						"rewards_new", 320, bgSprite.getCentreY() - 20,
						HorizontalAlign.LEFT, vertextBufferObjectManager);
		dialog.attachChild(tipsLabel);

		Text tips2Label = TextMaker.make("游戏存档已保存到服务器上", "systemFont28", 320,
				bgSprite.getCentreY() + 90, HorizontalAlign.CENTER,
				getVertexBufferObjectManager());
		dialog.attachChild(tips2Label);

		Text tips3Label = TextMaker.make("请卸载后安装最新版", "systemFont28", 320,
				bgSprite.getCentreY() + 130, HorizontalAlign.CENTER,
				getVertexBufferObjectManager());
		dialog.attachChild(tips3Label);

		Sprite starSprite = SpriteMaker.makeSpriteWithSingleImageFile("star",
				getVertexBufferObjectManager());
		starSprite.setPosition(dialog.getCentreX() + 70,
				dialog.getCentreY() - 15);
		starSprite.setScale(0.5f);
		dialog.attachChild(starSprite);
		float gapV = 8.0f;
		// 领取按钮
		ButtonSprite okBtn = ButtonMaker.makeFromSingleImgFile(320, 730,
				"yellow_btn_new", vertextBufferObjectManager);
		okBtn.setScale(0.85f);
		okBtn.setCentrePositionX(GameConstants.BASE_WIDTH / 2);
		okBtn.setBottomPositionY(bgSprite.getCentreY()
				+ bgSprite.getHeightHalf() * bgSprite.getScaleY() - gapV);
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				pButtonSprite.setEnabled(false);
				new Thread(new Runnable() {

					@Override
					public void run() {
						UpdateClass.download(activity);
						// 下载最新版本

					}
				}).start();

				dialog.dismiss();
			}
		});
		dialog.attachChild(okBtn);

		Text oKText = TextMaker.make("更新版本", "50white", 320, 705,
				HorizontalAlign.CENTER, vertextBufferObjectManager);
		oKText.setCentrePosition(okBtn.getCentreX(), okBtn.getCentreY());
		dialog.attachChild(oKText);

		dialog.setDialogDismissListener(new DialogDismissListener() {
			public void onDialogDismiss() {
				ResourceManager.unloadNewversionDownLoadTextures();
			}
		});
		LoopEntityModifier loop = new LoopEntityModifier(
				new SequenceEntityModifier(new ScaleModifier(0.5f, 0.7f, 0.8f),
						new ScaleModifier(0.5f, 0.8f, 0.7f)));
		okBtn.registerEntityModifier(loop);
		oKText.registerEntityModifier(loop.deepCopy());

		// 退出按钮
		ButtonSprite quitBtn = ButtonMaker.makeFromSingleImgFile(563, 266,
				"quit_new", vertextBufferObjectManager);
		quitBtn.setCentrePositionX(bgSprite.getCentreX()
				+ bgSprite.getWidthHalf() * bgSprite.getScaleX() - 20);
		quitBtn.setCentrePositionY(bgSprite.getCentreY()
				- bgSprite.getHeightHalf() * bgSprite.getScaleY() + 10);
		quitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				redDot.setVisible(true);
				dialog.dismissWithAnimamtion();
			}
		});
		dialog.attachChild(quitBtn);
		quitBtn.setScale(0.8f);
		dialog.showWithAnimation();
	}
	
	 public static void setreddot(){
		 redDot.setVisible(true);
	 }
	 private void dorain(){
		  final long RANDOM_SEED = 1234567890;//随机数种子
		  final Random random = new Random(RANDOM_SEED);
		  for (int i = 0; i < 100; i++) {
		   final float x1 = random.nextFloat() * 300;//线x起点 随机0-300
//		   final float x2 = random.nextFloat() * 300;//线x终点 随机0-300
		   final float y1 = random.nextFloat() * 480;//线y起点 随机0-480
//		   final float y2 = random.nextFloat() * 480;//线y终点 随机0-480
		   final float lineWidth = random.nextFloat() * 2;//线的宽度 随机0-5
		   final Line line = new Line(x1, y1, x1+10, y1-40, lineWidth,
		     getVertexBufferObjectManager());//画线
//		   line.setColor(255, 255,255);//设置颜色值 范围0-1
		   line.setColor(Color.WHITE);
		   
		   this.attachChild(line);//加入本场景 实体只有加入场景后才会被绘制和更新状态
			final IEntityModifierListener listener = new IEntityModifierListener() {
				
				@Override
				public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				}
				
				@Override
				public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
					
				}
			};
		   MoveModifier move = new MoveModifier(0.5f, line.getX(), line.getX(),
				   line.getY(), -line.getHeight());
			FadeInModifier fadeIn = new FadeInModifier(0.1f);
			FadeOutModifier fadeOut = new FadeOutModifier(0.1f);
		   LoopEntityModifier loop = new LoopEntityModifier(
					new SequenceEntityModifier(fadeOut, fadeIn), 6);
		   line.registerEntityModifier(new SequenceEntityModifier(listener,
				   loop, new DelayModifier(0.8f) ,move));
	 }
	 }
}
