package com.zplay.game.popstarog.scene;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.Toast;

import com.e7studio.android.e7appsdk.utils.LogUtils;
import com.orange.content.SceneBundle;
import com.orange.entity.modifier.LoopEntityModifier;
import com.orange.entity.modifier.ScaleModifier;
import com.orange.entity.modifier.SequenceEntityModifier;
import com.orange.entity.sprite.ButtonSprite;
import com.orange.entity.sprite.ButtonSprite.OnClickListener;
import com.orange.entity.sprite.Sprite;
import com.orange.entity.text.Text;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.util.HorizontalAlign;
import com.tendcloud.tenddata.TCAgent;
import com.zplay.game.popstarog.PopStar;
import com.zplay.game.popstarog.custom.CustomBaseScene;
import com.zplay.game.popstarog.custom.Dialog;
import com.zplay.game.popstarog.custom.DialogDismissListener;
import com.zplay.game.popstarog.layer.PopGameLayer;
import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.pay.PayCallback;
import com.zplay.game.popstarog.utils.ButtonMaker;
import com.zplay.game.popstarog.utils.ResourceManager;
import com.zplay.game.popstarog.utils.SPUtils;
import com.zplay.game.popstarog.utils.SoundUtils;
import com.zplay.game.popstarog.utils.SpriteMaker;
import com.zplay.game.popstarog.utils.TextMaker;
import com.zplay.game.popstarog.utils.sp.ConstantsHolder;
import com.zplay.game.popstarog.utils.sp.PhoneInfoGetter;

public class ShopScene extends CustomBaseScene {

	private final static String TAG = "ShopScene";
	private Context context;
	private Text starNumLabel;

	// 1分钱的按钮
	private ButtonSprite centBtn;

	private VertexBufferObjectManager vertexBufferObjectManager;

	private boolean isFromPop = false;
	private boolean isFromMenu = false;
	private boolean isFrompop_big = false;
	private boolean isFromMenu_big =false;
	private Dialog dialog;

	@Override
	public void onSceneCreate(SceneBundle bundle) {
		super.onSceneCreate(bundle);

		if (bundle != null) {
			isFromPop = bundle.getBooleanExtra("fromPop", false);
			isFrompop_big = bundle.getBooleanExtra("Frompop_big", false);
			isFromMenu_big=bundle.getBooleanExtra("isFromMenu_big", false);
		} else {
			isFromMenu = true;
		}

		ResourceManager.loadShopTextures();

		LogUtils.v(TAG, "ShopScene, onSceneCreate...");
		context = getActivity();
		vertexBufferObjectManager = getVertexBufferObjectManager();
		setSize(GameConstants.BASE_WIDTH, GameConstants.BASE_HEIGHT);
		addBg();
		addTel();
		andStarNumLabel();
		addBackBtn();
		addChargePoints();
	}

	private void addTel() {
		Text telLabel = TextMaker.make("如果充值遇到问题，请咨询:400-066-4568", "30white",
				320, 0, HorizontalAlign.CENTER, vertexBufferObjectManager);
		telLabel.setTopPositionY(0);
		attachChild(telLabel);
	}

	private void addBg() {
		Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile("bg",
				getVertexBufferObjectManager());
		bgSprite.setCentrePosition(GameConstants.BASE_WIDTH / 2,
				GameConstants.BASE_HEIGHT / 2);
		attachChild(bgSprite);
	}

	private void andStarNumLabel() {
		long starNum = SPUtils.getLuckStarNum(context);
		LogUtils.v(TAG, "幸运星：" + starNum);
		starNumLabel = TextMaker.make("你有 0123456789个幸运星", "50white", 275, 70,
				HorizontalAlign.CENTER, vertexBufferObjectManager);
		starNumLabel.setText("你有" + starNum + "个幸运星");
		starNumLabel.setCentrePosition(275, 70);
		attachChild(starNumLabel);
	}

	private void addBackBtn() {
		ButtonSprite backBtn = ButtonMaker.makeFromSingleImgFile(584, 69,
				"option_back", vertexBufferObjectManager);
		attachChild(backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				TCAgent.onEvent(context, "点击商城返回按钮");
				finish();
			}
		});
	}

	// 添加计费内容
	private void addChargePoints() {
		// 联通或者电信的设备没有1分钱
		if ((PhoneInfoGetter.getMobileSP(context).equals(
				ConstantsHolder.CHINA_UNICOME) || PhoneInfoGetter.getMobileSP(
				context).equals(ConstantsHolder.CHINA_TELECOM))
				&& GameConstants.productList.size() == 7) {
			GameConstants.productList.remove(0);
		} else {
			if (SPUtils.isCentAlreadyBuy(context)
					&& GameConstants.productList.size() == 7) {
				GameConstants.productList.remove(0);
			}
		}
		// 如果已经购买了一分钱的计费点，从列表中去除掉
		for (int i = 0; i < GameConstants.productList.size(); i++) {
			final Map<String, String> productItem = GameConstants.productList
					.get(i);
			Sprite productBg = SpriteMaker.makeSpriteWithSingleImageFile(
					"bg_shopitem", vertexBufferObjectManager);
			productBg.setPosition(0, 130 + 120 * i);
			productBg.setSize(640, 105);
			attachChild(productBg);

			String discount = productItem.get("discount");

			// 没有折扣
			if (discount == null || discount.equals("")) {

			} else {
				if (discount.equals("7")) {
					Sprite discountBgSprite = SpriteMaker
							.makeSpriteWithSingleImageFile("5_discount_bg",
									vertexBufferObjectManager);
					discountBgSprite.setPositionX(23);
					discountBgSprite.setPositionY(130 + 120 * i);
					attachChild(discountBgSprite);
				}
				Sprite discountSprite = SpriteMaker
						.makeSpriteWithSingleImageFile(discount + "zhe",
								vertexBufferObjectManager);
				discountSprite.setPositionX(297);
				discountSprite.setCentrePositionY(179 + 120 * i);
				attachChild(discountSprite);
			}

			Sprite starSprite = SpriteMaker.makeSpriteWithSingleImageFile(
					"star", vertexBufferObjectManager);
			starSprite.setPosition(31, 0);
			starSprite.setCentrePositionY(179 + 120 * i);
			attachChild(starSprite);

			Text starNumLabel = TextMaker.make(
					"× " + productItem.get("display"), "40white", 0, 0,
					HorizontalAlign.CENTER, vertexBufferObjectManager);
			starNumLabel.setLeftPositionX(119);
			starNumLabel.setCentrePositionY(179 + 120 * i);
			attachChild(starNumLabel);

			// 小数,认定为是1分的计费点
			if (productItem.get("money").contains(".")) {
				centBtn = ButtonMaker.makeFromSingleImgFile(538, 179 + 120 * i,
						"1cent", vertexBufferObjectManager);
				centBtn.setCentrePosition(538, 179 + 120 * i);
				centBtn.setScale(0.9f);
				attachChild(centBtn);
				centBtn.setOnClickListener(new OnClickListener() {
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						if (SPUtils.isCentAlreadyBuy(context)) {
							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(context, "该计费点只能购买一次",
											Toast.LENGTH_SHORT).show();
								}
							});
						} else {
							if (SPUtils.isShowYingXiaoDialog(context)) {
								showRewardsToday(productItem.get("id"));   
								//如果是电信则弹出营销页否则直接支付   by lvjibin
							}else {
								doBuy(productItem.get("id"));
							}
						}
					}
				});

			} else {
				ButtonSprite buyBtn = ButtonMaker.makeFromSingleImgFile(538,
						179 + 120 * i, "yellow_btn", vertexBufferObjectManager);
				buyBtn.setSize(154, 59);
				buyBtn.setCentrePosition(538, 179 + 120 * i);
				attachChild(buyBtn);

				buyBtn.setOnClickListener(new OnClickListener() {
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						if (SPUtils.isShowYingXiaoDialog(context)) {
							showRewardsToday(productItem.get("id"));   
							//如果是电信手机，则弹出营销页否则直接支付   by lvjibin
						}else {
							doBuy(productItem.get("id"));
						}
					}
				});

				Text moneyLabel = TextMaker.make(
						productItem.get("money") + "元", "40white", 0,
						179 + 120 * i, HorizontalAlign.CENTER,
						vertexBufferObjectManager);
				moneyLabel.setCentrePositionX(540);
				attachChild(moneyLabel);
			}

			String type = productItem.get("type");
			// 超值
			if (type.equals("ob")) {
				Sprite typeSprite = SpriteMaker.makeSpriteWithSingleImageFile(
						"icon_buy_cz", vertexBufferObjectManager);
				typeSprite.setScale(0.8f);
				typeSprite.setLeftPositionX(580);
				typeSprite.setTopPositionY(120 + 120 * i);
				attachChild(typeSprite);
			}
			// 热销
			if (type.equals("hot")) {
				Sprite typeSprite = SpriteMaker.makeSpriteWithSingleImageFile(
						"icon_hot", vertexBufferObjectManager);
				typeSprite.setLeftPositionX(563);
				typeSprite.setTopPositionY(140 + 120 * i);
				attachChild(typeSprite);
			}
			// 超爽
			if (type.equals("feel")) {
				Sprite typeSprite = SpriteMaker.makeSpriteWithSingleImageFile(
						"icon_feel", vertexBufferObjectManager);
				typeSprite.setScale(0.8f);
				typeSprite.setLeftPositionX(580);
				typeSprite.setTopPositionY(120 + 120 * i);
				attachChild(typeSprite);
			}
		}
	}

	public void doBuy(final String id) { 
		LogUtils.v(TAG, "购买商品：" + id);
		List<Map<String, String>> productList = GameConstants.productList;
		int num = 0;
		String money = null;
		for (int i = 0; i < productList.size(); i++) {
			Map<String, String> item = productList.get(i);
			String itemID = item.get("id");
			if (itemID.equals(id)) {
				num = Integer.parseInt(item.get("num"));
				money = item.get("money");
				break;
			}
		}
		final int count = num;
		// 为什么叫tdMoney，因为是要上报到talkingdata的money字段，不过，很少有人会看吧
		final String tdMoney = money;
		((PopStar) getActivity()).pay(id, new PayCallback() {
			public void callback(int code, String msg) {
				LogUtils.v(TAG, "订购结束");
				if (code == PayCallback.OK) {
					if(10==count){
						//如果购买了1分钱礼包就不再显示，也可以单独写一个一份购买的方法
						SPUtils.setCentPointAlreadyBuy(context);
					}
					// 认为1分钱的计费点都在最顶部，如果产品设计上有修改的话，这里也需要做相应修改
					if (id.equals(GameConstants.CENT_POINT)) {
						centBtn.setEnabled(false);
						ScaleModifier scale = new ScaleModifier(0.4f, 1f, 0f);
						centBtn.registerEntityModifier(scale);
					}
					SPUtils.saveLuckStarNum(context,
							SPUtils.getLuckStarNum(context) + count);
					starNumLabel.setText("你有" + SPUtils.getLuckStarNum(context)
							+ "个幸运星");
					TCAgent.onEvent(context, (isFromMenu ? "在主菜单"
							: (isFromPop ? "消灭星星模式" : "星星连萌模式"))
							+ ("购买" + tdMoney + "元幸运星"));
					if(null!=dialog){
						dialog.dismissWithAnimamtion();
					}
					if(isFrompop_big){
						//finish掉商城并返回到之前的游戏页面
						setResult(20);
						finish();
					}else if(isFromMenu_big){
						setResult(30);
						finish();
					}
				}
				Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
			}
		});
	}
	/**
	 * 显示购买页面
	 * 添加加载释放资源方法，避免因内存回收带来的显示缺失
	 * @param starNum 
	 * add by lvjibin
	 */
	private void showRewardsToday(final String id) {
		List<Map<String, String>> productList = GameConstants.productList;
		int starNum = 0;
		String money = null;
		//取出id和mon
		for (int i = 0; i < productList.size(); i++) {
			Map<String, String> item = productList.get(i);
			String itemID = item.get("id");
			if (itemID.equals(id)) {
				starNum = Integer.parseInt(item.get("num"));
				money = item.get("money");
				break;
			}
		}
		ResourceManager.loadCTBuyDialog(getActivity());
		dialog = new Dialog(ShopScene.this);
		dialog.setSize(GameConstants.BASE_WIDTH, GameConstants.BASE_HEIGHT);
		Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"common_bg", vertexBufferObjectManager);
		bgSprite.setPosition(31, 165);
		bgSprite.setScale(0.8f);
		dialog.attachChild(bgSprite);   
		//标题
		Text title1 = TextMaker.make(
				"即将购买","50white",bgSprite.getCentreX(),bgSprite.getCentreY() - 200f * bgSprite.getScaleY(), HorizontalAlign.CENTER,getVertexBufferObjectManager());
		dialog.attachChild(title1);
		
		// 购买金额
		Text tipsLabe2 = TextMaker.make(
				"￥" + (money.contains(".")?money:money+".00"), "rewards",
				bgSprite.getCentreX()-5, bgSprite.getCentreY()+bgSprite.getHeight()/6, HorizontalAlign.CENTER,
				vertexBufferObjectManager);
		tipsLabe2.setScale(0.25f);
		dialog.attachChild(tipsLabe2);
		
		// 购买幸运星数量
		Text tipsLabel = TextMaker.make(
				"" + starNum, "rewards",
				200, bgSprite.getCentreY()+20, HorizontalAlign.LEFT,
				vertexBufferObjectManager);
		tipsLabel.setScale(0.9f);
		dialog.attachChild(tipsLabel);
		// 购买幸运星数量前的乘号
				Text tipsLabelx = TextMaker.make(
						"×", "rewards",
						200, bgSprite.getCentreY()+10, HorizontalAlign.LEFT,
						vertexBufferObjectManager);
				tipsLabelx.setScale(0.5f);
				dialog.attachChild(tipsLabelx);
			
		// 幸运星图片
		Sprite starSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"fly_star", vertexBufferObjectManager);
		starSprite.setScale(1.1f);
		starSprite.setCentrePositionY(tipsLabel.getCentreY()-10);
		dialog.attachChild(starSprite);   
		
		// 校正幸运星数量和图片位置
		float gapH = 15.0f;
		float tempW = tipsLabel.getWidth() + starSprite.getWidth() + gapH;
		float baseX = (GameConstants.BASE_WIDTH - tempW) / 2;
		if(starNum>100){
			starSprite.setPositionX(baseX-23);
			tipsLabelx.setPositionX(starSprite.getRightX() + gapH/2);
			tipsLabel.setPositionX(tipsLabelx.getRightX() + gapH/2);
		}else {
			starSprite.setPositionX(baseX-30);
			tipsLabelx.setPositionX(starSprite.getRightX() + gapH/2);
			tipsLabel.setPositionX(tipsLabelx.getRightX() + gapH/2);
		}
		
		
		float gapV = 8.0f;
		// 购买按钮
		ButtonSprite okBtn = ButtonMaker.makeFromSingleImgFile(320,
				730, "yellow_btn_long", vertexBufferObjectManager);
		okBtn.setScale(0.85f);
		okBtn.setCentrePositionX(GameConstants.BASE_WIDTH / 2);
		okBtn.setBottomPositionY(bgSprite.getCentreY() + bgSprite.getHeightHalf() * bgSprite.getScaleY()- gapV);
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				doBuy(id);//点击购买
			}
		});
		dialog.attachChild(okBtn);

		
		Text oKText = TextMaker.make(
				"购  买", "50white",
				320, 705, HorizontalAlign.CENTER,
				vertexBufferObjectManager);
		oKText.setCentrePosition(okBtn.getCentreX(), okBtn.getCentreY());
		dialog.attachChild(oKText);
		
		dialog.setDialogDismissListener(new DialogDismissListener() {
					public void onDialogDismiss() {
						ResourceManager.unloadCTBuysDialog();
					}
				});
//		商城营销页购买按钮添加动画效果
		LoopEntityModifier loop = new LoopEntityModifier(
				new SequenceEntityModifier(new ScaleModifier(0.6f,
						0.8f, 0.7f),
						new ScaleModifier(0.6f, 0.7f, 0.8f)));
		okBtn.registerEntityModifier(loop);
		oKText.registerEntityModifier(loop.deepCopy());
		// 退出按钮
		ButtonSprite quitBtn = ButtonMaker.makeFromSingleImgFile(563, 266,
				"quit", vertexBufferObjectManager);
			quitBtn.setCentrePositionX(bgSprite.getCentreX() + bgSprite.getWidthHalf() * bgSprite.getScaleX() - 20);
		quitBtn.setCentrePositionY(bgSprite.getCentreY() - bgSprite.getHeightHalf() * bgSprite.getScaleY() + 10);
			quitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				dialog.dismissWithAnimamtion();
			}
		});
		quitBtn.setScale(0.8f);
		dialog.attachChild(quitBtn);
		dialog.showWithAnimation();
	}
	@Override
	public void onSceneDestroy() {
		super.onSceneDestroy();
		SoundUtils.playButtonClick();
		ResourceManager.unloadShopTextures();
	}

	
}
