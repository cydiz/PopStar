package com.zplay.game.popstarog;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.e7studio.android.e7appsdk.utils.InstalledAppInfoHandler;
import com.e7studio.android.e7appsdk.utils.WebParamsMapBuilder;
import com.e7studio.android.e7appsdk.utils.WebTask;
import com.e7studio.android.e7appsdk.utils.WebTaskHandler;
import com.example.zplay.AnnouncentConfig;
import com.example.zplay.AnnouncentConfig.InitCallBack;
import com.orange.engine.Engine;
import com.orange.engine.LimitedFPSEngine;
import com.orange.engine.camera.Camera;
import com.orange.engine.camera.ZoomCamera;
import com.orange.engine.options.EngineOptions;
import com.orange.engine.options.PixelPerfectEngineOptions;
import com.orange.engine.options.ScreenOrientation;
import com.orange.engine.options.resolutionpolicy.FillResolutionPolicy;
import com.orange.res.FontRes;
import com.orange.ui.activity.GameActivity;
import com.tendcloud.tenddata.TCAgent;
import com.zplay.android.sdk.notify.ZplayNotifier;
import com.zplay.android.sdk.pay.ZplayPay;
import com.zplay.android.sdk.pay.ZplayPayCallback;
import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.pay.PayCallback;
import com.zplay.game.popstarog.scene.SplashScene;
import com.zplay.game.popstarog.utils.BannerConfig;
import com.zplay.game.popstarog.utils.ConfigValueHandler;
import com.zplay.game.popstarog.utils.HttpUtils;
import com.zplay.game.popstarog.utils.JsonParser;
import com.zplay.game.popstarog.utils.LogUtils;
import com.zplay.game.popstarog.utils.OperatingActivitiesCallback;
import com.zplay.game.popstarog.utils.ResourceManager;
import com.zplay.game.popstarog.utils.SPUtils;
import com.zplay.game.popstarog.utils.SaveGameData;
import com.zplay.game.popstarog.utils.SoundUtils;
import com.zplay.game.popstarog.utils.sp.PhoneInfoGetter;
import com.zplay.huodongsdk.ZplayHDsdk;
import com.zplay.huodongsdk.ZplayHDsdk.ActivityCallBack;

public class PopStar extends GameActivity {

	public final static String TAG = "PopStar";
	private Activity activity;
	int selfCode = PayCallback.FAILED;
	String result = "购买失败";
	
//	private YumiBanner banner;
//	private YumiInterstitial interstitial;
//	private static final String YUMI_AD_KEY = "6aa7c37aa33572ad166ae7a7e3f7b007";	//测试key
//	private static final String YUMI_AD_KEY = "cb183c7e4f58bf0c828ec3d1aaec61c6";	//testin key
	private static final String YUMI_AD_KEY = "9b5c7c34107e6a2eeb902791416b2e1f";	//正式key

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.activity = this;
		initScreen();
		initPay();
		initTD();
		initPush();
		initAnnouncement();
		initAD();
		getSpringFestivalActConfig();
		initSHOP();
		getOnOffXxyl();
	}

	private void initScreen() {
		getRenderLayout().getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			public void onGlobalLayout() {
				Display d = getWindowManager().getDefaultDisplay();
				GameConstants.screen_width = d.getWidth();
				GameConstants.screen_height = d.getHeight();
				LogUtils.v(TAG, "屏幕尺寸：" + GameConstants.screen_width + "," + GameConstants.screen_height);
			}
		});
	}

	//获取服务器星星有礼弹窗开关
	private void getOnOffXxyl() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = HttpUtils.getServiceData("http://popstar.zplay.cn/cloud/in/onoff.php");
				LogUtils.v(result);
				if ("1".equals(result)) {
					LogUtils.v("result=" + result + ",星星有礼服务开启...");
					SPUtils.setServerOnOff(getApplicationContext(), true);
				} else {
					LogUtils.v("result=" + result + ",星星有礼服务关闭...");
				}
			}
		}).start();
	}
	
	/**
	 * 是否显示商城按钮
	 */
	private void initSHOP() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean httpGets = HttpUtils.HttpGets();
				if(httpGets){
					SPUtils.setIsShowBtShop(getApplicationContext(), true);
				}else {
					SPUtils.setIsShowBtShop(getApplicationContext(), false);
				}
			}
		}).start();
	}

	/**
	 * 活动sdk初始化（包括登陆送stars等）
	 */
	public void initHDSDK(final OperatingActivitiesCallback callback) {
		SPUtils.setisnewversion(getApplicationContext(), true);
		ZplayHDsdk.initHDSDK(this, new ActivityCallBack() {
			@Override
			public void UrlState(String url) {
				//检查此版本是否为最新版本的
				if (null != url) {
					SPUtils.setisnewversion(getApplicationContext(), false);
					callback.showNewVersionDownLoadCallback();
				}
			}
			
			@Override
			public void PhonenumState(List<Map<String, Object>> arg0) {
			}
			
			@Override
			public void MissionsState(List<Map<String, Object>> arg0) {
			}
			
			@Override
			public void LoginState(List<Map<String, Object>> logins) {
				// 获取登陆奖励活动
				if (logins != null) {
					String str = logins.get(0).get("reward").toString();
					String num = JsonParser.getVerifyCode(str).trim();
					SPUtils.setTodayRewards(getApplicationContext(), num);
					callback.operatingActivitiesCallback();
				} else {// 不显示登陆活动
					SPUtils.setTodayRewards(getApplicationContext(), "zero_reawards");
					callback.operatingActivitiesCallback();
				}
			}
			
			@Override
			public void LevelState(List<Map<String, Object>> arg0) {
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void getSpringFestivalActConfig() {
		new WebTaskHandler(getApplicationContext(), new WebTask() {
			public void doTask(String arg0, String arg1) {
				if (arg0 == null) {
					SPUtils.setSpringFestivalActOn(getApplicationContext(),
							"10", false);
				} else {
					// 关
					if (arg0.trim().equals("0")) {
						SPUtils.setSpringFestivalActOn(getApplicationContext(),
								"10", false);
					}
					// 开
					if (arg0.trim().equals("1")) {
						SPUtils.setSpringFestivalActOn(getApplicationContext(),
								"10", true);
					}
				}
			}
		}, false, false, "", -1, false, false)
				.execute(WebParamsMapBuilder
						.buildParams(
								"http://popstar.zplay.cn/ten/open_close_zwb0122.php",
								new String[] { "gameid", "channelid", "ver",
										"device" },
								new String[] {
										ConfigValueHandler
												.getGameID(PopStar.this),
										ConfigValueHandler
												.getChannel(getApplicationContext()),
										InstalledAppInfoHandler
												.getAppVersionName(
														getApplicationContext(),
														getApplicationContext()
																.getPackageName()),
										PhoneInfoGetter
												.getIMEI(getApplicationContext()) }));

		// 第15关的奖励是否开启
		new WebTaskHandler(PopStar.this, new WebTask() {
			public void doTask(String arg0, String arg1) {
				if (arg0 == null) {
					SPUtils.setSpringFestivalActOn(getApplicationContext(),
							"15", false);
				} else {

					LogUtils.v(TAG, "第15关的开关配置为：" + arg0);

					// 关
					if (arg0.trim().equals("0")) {
						SPUtils.setSpringFestivalActOn(getApplicationContext(),
								"15", false);
					}
					// 开
					if (arg0.trim().equals("1")) {
						SPUtils.setSpringFestivalActOn(getApplicationContext(),
								"15", true);
					}
				}
			}
		}, true, false, null, -1, false, false).execute(WebParamsMapBuilder
				.buildParams(
						"http://popstar.zplay.cn/ten/open_close_zwb320.php",
						new String[] { "gameid", "channelid", "ver", "deice" },
						new String[] {
								ConfigValueHandler.getGameID(PopStar.this),
								ConfigValueHandler
										.getChannel(getApplicationContext()),
								InstalledAppInfoHandler.getAppVersionName(
										getApplicationContext(),
										getApplicationContext()
												.getPackageName()),
								PhoneInfoGetter
										.getIMEI(getApplicationContext()) }));

	}

	//初始化广告
	private void initAD() {
		//初始化插屏
//		interstitial = new YumiInterstitial(this, YUMI_AD_KEY);
//		interstitial.requestYumiInterstitial();
//		interstitial.setInterstitialEventListener(mInterstititalListener);
//		interstitial.setChannelID(ConfigValueHandler.getChannel(getApplicationContext()));
//		interstitial.setVersionName(AppUtils.getAppVersion(activity));
		//初始化banner容器
		RelativeLayout layout = (RelativeLayout) getLayoutInflater().inflate(
				getResources().getIdentifier("banner_layout", "layout", getPackageName()), null);
		FrameLayout.LayoutParams parmas = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		parmas.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		addContentView(layout, parmas);
		layout.setGravity(Gravity.CENTER);
		//初始化banner
//		banner = new YumiBanner(this, YUMI_AD_KEY);
//		banner.setBannerContainer(layout, AdSize.BANNER_SIZE_AUTO);
//		banner.requestYumiBanner();
//		banner.dismissBanner();
//		banner.setBannerEventListener(mBannerListener);
//		banner.setChannelID(ConfigValueHandler.getChannel(getApplicationContext()));
//		banner.setVersionName(AppUtils.getAppVersion(activity));
	}
	
	//banner回调
//	private IYumiBannerListener mBannerListener = new IYumiBannerListener() {
//		@Override
//		public void onBannerPreparedFailed(LayerErrorCode arg0) {
//			LogUtils.d("banner-onBannerPreparedFailed");
//		}
//		
//		@Override
//		public void onBannerPrepared() {
//			LogUtils.d("banner-onBannerPrepared");
//		}
//		
//		@Override
//		public void onBannerExposure() {
//			LogUtils.d("banner-onBannerExposure");
//		}
//		
//		@Override
//		public void onBannerClosed() {
//			LogUtils.d("banner-onBannerClosed");
//		}
//		
//		@Override
//		public void onBannerClicked() {
//			LogUtils.d("banner-onBannerClicked");
//		}
//	};
	
	//插屏回调
//	private IYumiInterstititalListener mInterstititalListener = new IYumiInterstititalListener() {
//		@Override
//		public void onInterstitialPreparedFailed(LayerErrorCode arg0) {
//			LogUtils.d("interstitital-onInterstitialPreparedFailed");
//		}
//		
//		@Override
//		public void onInterstitialPrepared() {
//			LogUtils.d("interstitital-onInterstitialPrepared");
//		}
//		
//		@Override
//		public void onInterstitialExposure() {
//			LogUtils.d("interstitital-onInterstitialExposure");
//		}
//		
//		@Override
//		public void onInterstitialClosed() {
//			LogUtils.d("interstitital-onInterstitialClosed");
//		}
//		
//		@Override
//		public void onInterstitialClicked() {
//			LogUtils.d("interstitital-onInterstitialClicked");
//		}
//	};

	public void showPop() {
		runOnUiThread(new Runnable() {
			public void run() {
//				if (interstitial != null) {
//					LogUtils.d("显示插屏");
//					interstitial.showInterstitial(false);
//				}
			}
		});
	}

	public void showBanner() {
		if (BannerConfig.isBannerShow()) {
			runOnUiThread(new Runnable() {
				public void run() {
//					banner.resumeBanner();
				}
			});
		}
	}

	public void hideBanner() {
		runOnUiThread(new Runnable() {
			public void run() {
//				banner.dismissBanner();
			}
		});
	}

	private void initAnnouncement() {
		AnnouncentConfig.initSDK(PopStar.this, new InitCallBack() {
			public void State(int isshow) {
				SPUtils.setAnnouncementShowPlace(PopStar.this, isshow);
				if (isshow == 0) {
					SPUtils.setAnnouncementShow(PopStar.this, false);
				} else {
					SPUtils.setAnnouncementShow(PopStar.this, true);
				}
			}

			public void ShowState(int showstate) {
				if (showstate == 1) {
					SPUtils.setAnnouncementShow(PopStar.this, true);
				}
				if (showstate == 3) {
					SPUtils.setAnnouncementShow(PopStar.this, false);
				}
			}
		});
	}

	private void initPush() {
		ZplayNotifier.startWork(this);
	}

	private void initTD() {
		TCAgent.init(getApplicationContext(),
				GameConstants.IS_MM_ZIZHI_ZPLAY ? "8FE68AC6B1FD3C89817358F0AB250ECC" : "512152B5584220EBB238004E71208EDB", 
				ConfigValueHandler.getChannel(getApplicationContext()));
	}

	private void initPay() {
		ZplayPay.init(activity);
	}
	
	public void pay(final String id, final PayCallback callback) {
		TCAgent.onEvent(this, "支付sdk_请求支付");
		runOnUiThread(new Runnable() {
			public void run() {
				LogUtils.i("点击计费道具");
				ZplayPay.pay(PopStar.this, id, new ZplayPayCallback() {
					public void callback(int code, String arg1, String arg2) {
						int selfCode = PayCallback.FAILED;
						String result = "购买失败";
						if (code == ZplayPay.SUCCESS) {
							result = "购买成功," + arg2;
							selfCode = PayCallback.OK;
							if (id.equals(GameConstants.CENT_POINT)) {
								SPUtils.setCentPointAlreadyBuy(PopStar.this);
							}
						}
						if (code == ZplayPay.CANCEL) {
							result = "购买取消：" + arg2;
							selfCode = PayCallback.CANCEL;
						}
						if (code == ZplayPay.FAILED) {
							result = "购买失败," + arg2;
							selfCode = PayCallback.CANCEL;
						}
						if (callback != null) {
							LogUtils.v(TAG, "callback");
							callback.callback(selfCode, result);
						}
					}
				});
			}
		});
	}

	

	@Override
	protected synchronized void onResume() {
		super.onResume();
		TCAgent.onResume(this);
//		interstitial.onResume();
//		banner.onResume();
		try {
//			MobileAgent.onResume(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onStop() {
		LogUtils.d("调用onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		interstitial.onDestory();
//		banner.onDestroy();
		SaveGameData.saveJsonData(this);
		LogUtils.d("调用onDestroy");
	}

	protected void onPause() {
		super.onPause();
		TCAgent.onPause(this);
//		interstitial.onPause();
//		banner.onPause();
		try {
//			MobileAgent.onPause(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtils.d("调用onDestroy");
	}
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();		
	}

	@Override
	protected PixelPerfectEngineOptions onCreatePixelPerfectEngineOptions() {
		LogUtils.v(TAG, "onCreatePixelPerfectEngineOptions...");
		PixelPerfectEngineOptions pixelPerfectEngineOptions = new PixelPerfectEngineOptions(
				this, ZoomCamera.class);
		return pixelPerfectEngineOptions;
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		return new LimitedFPSEngine(pEngineOptions, 60);
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		EngineOptions options = new EngineOptions(true,
				ScreenOrientation.PORTRAIT_FIXED, new FillResolutionPolicy(),
				new Camera(0, 0, GameConstants.BASE_WIDTH,
						GameConstants.BASE_HEIGHT));
		options.getTouchOptions().setNeedsMultiTouch(false);
		options.getAudioOptions().setNeedsSound(true);
		options.getAudioOptions().setNeedsMusic(true);
		options.getRenderOptions().setDithering(true);
		return options;
	}

	@Override
	protected void onLoadResources() {
		LogUtils.v(TAG, "onLoadResources...");
		// 这里加载资源，是全部都加载完毕，还是只加载splash，算了，先只加载splash，摸索中……
		SoundUtils.preLoad();
		SoundUtils.preload1010Sounds();
		SoundUtils.setVolumn(SPUtils.isAudioOpen(getApplicationContext()) ? 1
				: 0);
		ResourceManager.loadSplashTextures();
		ResourceManager.loadMainSceneResources();
		ResourceManager.loadHammerTextures();
		
		FontRes.loadFont(512, 256,
				Typeface.createFromAsset(getAssets(), "fonts/font.ttf"), 50f,
				true, Color.WHITE, "50white");
		FontRes.loadFont(512, 256,
				Typeface.createFromAsset(getAssets(), "fonts/font.ttf"), 40f,
				true, Color.WHITE, "40white");
		FontRes.loadFont(512, 256,
				Typeface.createFromAsset(getAssets(), "fonts/font.ttf"), 30f,
				true, Color.WHITE, "30white");
		FontRes.loadFont(128, 128,
				Typeface.createFromAsset(getAssets(), "fonts/font.ttf"), 20f,
				true, Color.WHITE, "20white");

		FontRes.loadFont(256, 256,
				Typeface.createFromAsset(getAssets(), "fonts/font.ttf"), 25f,
				true, Color.WHITE, "25white");
		FontRes.loadFont(128, 128,
				Typeface.createFromAsset(getAssets(), "fonts/font.ttf"), 25f,
				true, Color.WHITE, "cdkey");
		FontRes.loadFont(128, 128,
				Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 40, true,
				Color.WHITE, "systemFont40");
		FontRes.loadFont(256, 256,
				Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 30, true,
				Color.WHITE, "systemFont30");
		FontRes.loadFont(256, 256,
				Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), 28, true,
				Color.WHITE, "systemFont28");
		FontRes.loadFont(256, 256,
				Typeface.createFromAsset(getAssets(), "fonts/font.ttf"), 35,
				true, Color.WHITE, "35white");
		FontRes.loadFont(512, 512,
				Typeface.createFromAsset(getAssets(), "fonts/font.ttf"), 50,
				true, Color.WHITE, "moveStage");
		FontRes.loadFont(128, 128,
				Typeface.createFromAsset(getAssets(), "fonts/font.ttf"), 45,
				true, Color.rgb(254, 239, 0), "currentScoreFont");
		FontRes.loadFont(256, 256,
				Typeface.createFromAsset(getAssets(), "fonts/font.ttf"), 100,
				true, Color.WHITE, "100white");
		FontRes.loadFont(256, 256,
				Typeface.createFromAsset(getAssets(), "fonts/font.ttf"), 50,
				true, Color.WHITE, "50white");
		/*FontRes.loadFont(128, 128,
				Typeface.createFromAsset(getAssets(), "fonts/font.ttf"), 40,
				true, Color.WHITE, "40white");*/
		FontRes.loadFont(128, 128,
				Typeface.createFromAsset(getAssets(), "fonts/1010.ttf"), 50,
				true, Color.WHITE, "1010num");
		FontRes.loadFont(128, 128,
				Typeface.createFromAsset(getAssets(), "fonts/1010.ttf"), 65,
				true, Color.WHITE, "1010numAdd5");

		FontRes.loadFont(256, 128,
				Typeface.createFromAsset(getAssets(), "fonts/1010.ttf"), 30,
				true, Color.WHITE, "1010costTips");
		FontRes.loadFont(128, 128,
				Typeface.createFromAsset(getAssets(), "fonts/1010.ttf"), 60,
				true, Color.WHITE, "1010gameOver");
		FontRes.loadFont(256, 256,
				Typeface.createFromAsset(getAssets(), "fonts/1010.ttf"), 120,
				true, Color.rgb(255,223,64), "1010ScoreAnim");
	}

	protected void onLoadComplete() {
		LogUtils.v(TAG, "onLoadComplete...");
		startScene(SplashScene.class);
	}

}
