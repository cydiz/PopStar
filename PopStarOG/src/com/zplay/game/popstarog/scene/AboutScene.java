package com.zplay.game.popstarog.scene;

import android.widget.Toast;

import com.e7studio.android.e7appsdk.utils.InstalledAppInfoHandler;
import com.orange.content.SceneBundle;
import com.orange.entity.modifier.LoopEntityModifier;
import com.orange.entity.modifier.ScaleModifier;
import com.orange.entity.modifier.SequenceEntityModifier;
import com.orange.entity.sprite.ButtonSprite;
import com.orange.entity.sprite.ButtonSprite.OnClickListener;
import com.orange.entity.sprite.Sprite;
import com.orange.entity.text.Text;
import com.orange.entity.text.TickerText;
import com.orange.entity.text.TickerText.TickerTextOptions;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.res.FontRes;
import com.orange.util.HorizontalAlign;
import com.zplay.game.popstarog.custom.CustomBaseScene;
import com.zplay.game.popstarog.custom.Dialog;
import com.zplay.game.popstarog.custom.DialogDismissListener;
import com.zplay.game.popstarog.layer.PopMenuLayer;
import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.utils.ButtonMaker;
import com.zplay.game.popstarog.utils.ResourceManager;
import com.zplay.game.popstarog.utils.SPUtils;
import com.zplay.game.popstarog.utils.SpriteMaker;
import com.zplay.game.popstarog.utils.TextMaker;
import com.zplay.huodongsdk.UpdateClass;

public class AboutScene extends CustomBaseScene {
	private VertexBufferObjectManager vertexBufferObjectManager;

	@Override
	public void onSceneCreate(SceneBundle bundle) {
		super.onSceneCreate(bundle);
		vertexBufferObjectManager = getVertexBufferObjectManager();
		Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile("bg",
				vertexBufferObjectManager);
		bgSprite.setPosition(0, 0);
		attachChild(bgSprite);
		// Text text = TextMaker
		// .make("本游戏的版权归\n北京宏润基业商贸有限公司所有\n游戏中的文字、图片等内容\n均为游戏版权所有者的\n个人态度或立场\n客服电话：400-066-4568\n客服邮箱：kefu@zplay.cn",
		// "aboutFont", 320, 0, HorizontalAlign.CENTER,
		// vertexBufferObjectManager);
		// text.setTopPositionY(80);
		TickerText text = new TickerText(
				0,
				0,
				FontRes.getFont("aboutFont"),
				"本游戏的版权归\n"
						+ (GameConstants.IS_MM_ZIZHI_ZPLAY ? "掌游天下(北京)信息技术有限公司所有"
								: "北京宏润基业商贸有限公司所有")
						+ "\n游戏中的文字、图片等内容\n均为游戏版权所有者的\n个人态度或立场\n客服电话：400-066-4568\n客服邮箱：kefu@zplay.cn\n版本:"
						+ InstalledAppInfoHandler.getAppVersionName(
								getActivity(), getActivity().getPackageName()),
				new TickerTextOptions(HorizontalAlign.CENTER, 35),
				vertexBufferObjectManager);
		text.setCentrePositionX(320);
		text.setCentrePositionY(480);
		attachChild(text);

		ButtonSprite backBtn = ButtonMaker.makeFromSingleImgFile(0, 0,
				"options_back", vertexBufferObjectManager);
		backBtn.setRightPositionX(630);
		backBtn.setTopPositionY(10);
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				finish();
			}
		});
		attachChild(backBtn);
		ButtonSprite backBtn2 = ButtonMaker.makeFromSingleImgFile(0, 0,
				"cent_yellow_btn", vertexBufferObjectManager);
		backBtn2.setRightPositionX(bgSprite.getWidthHalf());
		backBtn2.setTopPositionY(730);
		backBtn2.setCentrePosition(bgSprite.getWidthHalf(), 730);
		
		backBtn2.setScale(0.8f);
		attachChild(backBtn2);
		LoopEntityModifier loop = new LoopEntityModifier(
				new SequenceEntityModifier(new ScaleModifier(0.5f,
						0.7f, 0.8f),
						new ScaleModifier(0.5f, 0.8f, 0.7f)));
		backBtn2.registerEntityModifier(loop);
		
		Text gengxintext = TextMaker
				 .make("检查更新",
				 "rewards_new", 320, 0, HorizontalAlign.CENTER,
				 vertexBufferObjectManager);
		gengxintext.setCentrePosition(bgSprite.getWidthHalf(), 730);
		attachChild(gengxintext);
		gengxintext.registerEntityModifier(loop.deepCopy());
		backBtn2.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				boolean getisnewversion = SPUtils.getisnewversion(getActivity());
				if (getisnewversion) {
					getActivity().
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getActivity(), "已经是最新版本", 0).show();
						}
					});
				} else {
					showNewVersionDownLoad();
				}
			}
		});
	}

	public void onSceneDestroy() {
		super.onSceneDestroy();
		ResourceManager.unloadAboutSceneResources();
	}
	
	private void showNewVersionDownLoad() {
		ResourceManager.loadNewversionDownLoadTextures(getActivity());
		final Dialog dialog = new Dialog(this);
		dialog.setSize(GameConstants.BASE_WIDTH, GameConstants.BASE_HEIGHT);
		Sprite bgSprite = SpriteMaker.makeSpriteWithSingleImageFile(
				"common_bg_new", vertexBufferObjectManager);
		bgSprite.setPosition(31, 165);
		bgSprite.setScale(0.8f);
		dialog.attachChild(bgSprite);

		Text title = TextMaker.make("版本更新", "rewards_new",
				bgSprite.getCentreX(),
				bgSprite.getCentreY() - 200f * bgSprite.getScaleY(),
				HorizontalAlign.CENTER, vertexBufferObjectManager);
		dialog.attachChild(title);

		Text tipsLabel = TextMaker
				.make("全新的中文版上线了\n\40\40\40\40新体验强势登场\n\40\40\40\40\40\40更新即送30 \40\40\40\40,抢\b",
						"rewards_new", 320, bgSprite.getCentreY() - 20,
						HorizontalAlign.LEFT, vertexBufferObjectManager);
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
				"yellow_btn_new", vertexBufferObjectManager);
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
						UpdateClass.download(getActivity());
						// 下载最新版本

					}
				}).start();

				dialog.dismiss();
			}
		});
		dialog.attachChild(okBtn);

		Text oKText = TextMaker.make("更新版本", "50white", 320, 705,
				HorizontalAlign.CENTER, vertexBufferObjectManager);
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
				"quit_new", vertexBufferObjectManager);
		quitBtn.setCentrePositionX(bgSprite.getCentreX()
				+ bgSprite.getWidthHalf() * bgSprite.getScaleX() - 20);
		quitBtn.setCentrePositionY(bgSprite.getCentreY()
				- bgSprite.getHeightHalf() * bgSprite.getScaleY() + 10);
		quitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				dialog.dismissWithAnimamtion();
			}
		});
		dialog.attachChild(quitBtn);
		quitBtn.setScale(0.8f);
		dialog.showWithAnimation();
	}
}
