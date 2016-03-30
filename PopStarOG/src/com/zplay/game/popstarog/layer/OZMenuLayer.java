package com.zplay.game.popstarog.layer;

import javax.microedition.khronos.opengles.GL10;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;

import com.orange.entity.IEntity;
import com.orange.entity.layer.Layer;
import com.orange.entity.modifier.FadeInModifier;
import com.orange.entity.modifier.FadeOutModifier;
import com.orange.entity.modifier.LoopEntityModifier;
import com.orange.entity.modifier.ScaleModifier;
import com.orange.entity.modifier.SequenceEntityModifier;
import com.orange.entity.sprite.ButtonSprite;
import com.orange.entity.sprite.ButtonSprite.OnClickListener;
import com.orange.entity.sprite.Sprite;
import com.orange.entity.text.Text;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.res.FontRes;
import com.orange.res.RegionRes;
import com.orange.util.HorizontalAlign;
import com.orange.util.modifier.IModifier;
import com.orange.util.modifier.IModifier.IModifierListener;
import com.tendcloud.tenddata.TCAgent;
import com.zplay.game.popstarog.custom.Dialog;
import com.zplay.game.popstarog.custom.DialogDismissListener;
import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.scene.OZScene;
import com.zplay.game.popstarog.scene.ShopScene;
import com.zplay.game.popstarog.utils.ButtonMaker;
import com.zplay.game.popstarog.utils.ResourceManager;
import com.zplay.game.popstarog.utils.SPUtils;
import com.zplay.game.popstarog.utils.SoundUtils;
import com.zplay.game.popstarog.utils.SpriteMaker;
import com.zplay.game.popstarog.utils.TextMaker;

@SuppressLint({ "SetJavaScriptEnabled", "InflateParams" })
public class OZMenuLayer extends Layer {

	private ButtonSprite backBtn;
	private Sprite medalSprite;
	private Text highScoreLabel;
	private ButtonSprite shopBtn;
	private Text starNumLabel;

	private Sprite tipsSprite;
	private Sprite iconSprite;

	private ButtonSprite newGameBtn;
	private ButtonSprite continueGameBtn;

	private VertexBufferObjectManager vertextBufferObjectManager = null;

	private Activity activity;

	private OZScene ozScene;

	public OZMenuLayer(OZScene scene) {
		super(GameConstants.BASE_WIDTH, GameConstants.BASE_HEIGHT, scene);
		this.ozScene = scene;
		setSize(GameConstants.BASE_WIDTH, GameConstants.BASE_HEIGHT);
		vertextBufferObjectManager = getVertexBufferObjectManager();
		activity = getActivity();
		buildUI();
	}

	private void buildUI() {
		backBtn = new ButtonSprite(0, 0,
				RegionRes.getTextureRegion("1010_back_btn"),
				vertextBufferObjectManager);
		backBtn.setPosition(81, 79);
		backBtn.setScale(0.8f);
		backBtn.setIgnoreTouch(false);
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				TCAgent.onEvent(activity, "退出连萌界面");
				SoundUtils.play1010Drop();
				ozScene.onBackKeyDown();
			}
		});
		attachChild(backBtn);

		medalSprite = SpriteMaker.makeSpriteWithSingleImageFile("medal",
				vertextBufferObjectManager);
		medalSprite.setPosition(217, 91);
		attachChild(medalSprite);

		highScoreLabel = new Text(0, 0, FontRes.getFont("1010num"),
				"0123456789100123456789", vertextBufferObjectManager);
		highScoreLabel.setText(String.valueOf(SPUtils
				.get1010HighScore(activity)));
		highScoreLabel.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		highScoreLabel.setColor(69.0f / 255, 179.0f / 255, 204.0f / 255);
		highScoreLabel.setLeftPositionX(290);
		highScoreLabel.setCentrePositionY(114);
		attachChild(highScoreLabel);

		setHighScoreAndMedalPosition();

		shopBtn = new ButtonSprite(0, 0,
				RegionRes.getTextureRegion("lucky_star_bg"),
				vertextBufferObjectManager);
		shopBtn.setPosition(425, 89);
		shopBtn.setIgnoreTouch(false);
		shopBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				TCAgent.onEvent(activity, "点击连萌界面商城");
				SoundUtils.play1010Drop();
				ozScene.startScene(ShopScene.class);
			}
		});
		attachChild(shopBtn);

		starNumLabel = new Text(0, 0, FontRes.getFont("1010num"),
				"0123456789100123456789", vertextBufferObjectManager);
		starNumLabel.setText(String.valueOf(SPUtils.getLuckStarNum(activity)));
		starNumLabel.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		starNumLabel.setCentrePositionX(shopBtn.getCentreX()+10);
		starNumLabel.setCentrePositionY(110);
		starNumLabel.setScale(0.8f);
		attachChild(starNumLabel);
		
		tipsSprite = SpriteMaker.makeSpriteWithSingleImageFile("1010_tips",
				vertextBufferObjectManager);
		tipsSprite.setCentrePosition(320, 507);
		attachChild(tipsSprite);
		
		iconSprite = SpriteMaker.makeSpriteWithSingleImageFile("1010_over_icon",
				vertextBufferObjectManager);
		iconSprite.setCentrePositionX(320);
		iconSprite.setBottomPositionY(tipsSprite.getTopY() - 30);
		attachChild(iconSprite);
		iconSprite.registerEntityModifier(new LoopEntityModifier(
				new SequenceEntityModifier(new ScaleModifier(0.5f, 1f, 0.95f),
						new ScaleModifier(0.5f, 0.95f, 1f))));

		newGameBtn = ButtonMaker.makeFromSingleImgFile(0, 0, "1010_new_game",
				vertextBufferObjectManager);
		newGameBtn.setPosition(83, 757);
		newGameBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				SoundUtils.play1010Drop();
				TCAgent.onEvent(activity, "点击连萌新游戏");
				doNewGame();
			}
		});
		attachChild(newGameBtn);

		continueGameBtn = new ButtonSprite(0, 0,
				RegionRes.getRegion("1010_continue_game_enable"),
				RegionRes.getRegion("1010_continue_game_enable"),
				RegionRes.getRegion("1010_continue_game_disable"),
				vertextBufferObjectManager);
		continueGameBtn.setPosition(352, 757);
		continueGameBtn.setIgnoreTouch(false);
		if (SPUtils.get1010CurrentScore(activity) != 0) {
			continueGameBtn.setEnabled(true);
		} else {
			continueGameBtn.setEnabled(false);
		}

		continueGameBtn.setOnClickListener(new OnClickListener() {
			public void onClick(ButtonSprite pButtonSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				TCAgent.onEvent(activity, "点击连萌继续游戏");
				SoundUtils.play1010Drop();
				doGameStuff(true);
			}
		});

		attachChild(continueGameBtn);

	}

	// 让最高分跟江北的位置处于中间
	private void setHighScoreAndMedalPosition() {
		float width = medalSprite.getWidth() + highScoreLabel.getWidth()
				* (highScoreLabel.getText().toString().length() / 22.0f) + 20;

		float leftX = GameConstants.BASE_WIDTH / 2 - width / 2;

		medalSprite.setLeftPositionX(leftX);
		highScoreLabel.setLeftPositionX(leftX + medalSprite.getWidth() + 20);
	}

	private void doNewGame() {
		if (!ozScene.isGameOn()) {
			if (SPUtils.get1010CurrentScore(activity) != 0) {
				ResourceManager.loadIphoneDialogTextures();
				final Dialog dialog = new Dialog(ozScene);
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
						dialog.dismiss();
						SPUtils.reset1010GameData(activity);
						doGameStuff(false);
					}
				});
				Text okLabel = TextMaker.make("确定", "systemFont30", 187, 547,
						HorizontalAlign.CENTER, vertextBufferObjectManager);
				dialog.attachChild(okBtn);
				dialog.attachChild(okLabel);

				ButtonSprite cancelBtn = ButtonMaker.makeFromSingleImgFile(453,
						547, "alert_cancel", vertextBufferObjectManager);
				cancelBtn.setOnClickListener(new OnClickListener() {
					public void onClick(ButtonSprite pButtonSprite,
							float pTouchAreaLocalX, float pTouchAreaLocalY) {
						dialog.dismiss();
					}
				});
				Text cancelLabel = TextMaker
						.make("取消", "systemFont30", 453, 547,
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
				SPUtils.reset1010GameData(activity);
				doGameStuff(false);
			}
		}
	}

	private void disableAllBtns() {
		newGameBtn.setEnabled(false);
		newGameBtn.setIgnoreTouch(true);

		continueGameBtn.setEnabled(false);
		continueGameBtn.setIgnoreTouch(true);

		shopBtn.setEnabled(false);
		shopBtn.setIgnoreTouch(true);

		backBtn.setEnabled(false);
		backBtn.setIgnoreTouch(true);
	}

	private void enableAllBtns() {
		newGameBtn.setEnabled(true);
		newGameBtn.setIgnoreTouch(false);

		continueGameBtn.setEnabled(true);
		continueGameBtn.setIgnoreTouch(false);

		if (SPUtils.get1010CurrentScore(activity) == 0) {
			continueGameBtn.setEnabled(false);
		}

		shopBtn.setEnabled(true);
		shopBtn.setIgnoreTouch(false);

		backBtn.setEnabled(true);
		backBtn.setIgnoreTouch(false);
	}

	private void doGameStuff(final boolean flag) {
		ozScene.setGameOn(true);
		disableAllBtns();

		FadeOutModifier fadeOut = new FadeOutModifier(1f);
		fadeOut.addModifierListener(new IModifierListener<IEntity>() {
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
			}

			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				ozScene.doGameStuff(flag);
			}
		});

		backBtn.registerEntityModifier(fadeOut);
		medalSprite.registerEntityModifier(fadeOut.deepCopy());
		highScoreLabel.registerEntityModifier(fadeOut.deepCopy());
		shopBtn.registerEntityModifier(fadeOut.deepCopy());
		starNumLabel.registerEntityModifier(fadeOut.deepCopy());
		tipsSprite.registerEntityModifier(fadeOut.deepCopy());
		iconSprite.registerEntityModifier(fadeOut.deepCopy());
		newGameBtn.registerEntityModifier(fadeOut.deepCopy());
		continueGameBtn.registerEntityModifier(fadeOut.deepCopy());
	}

	private void allComponentsStopActions() {
		backBtn.clearEntityModifiers();
		medalSprite.clearEntityModifiers();
		highScoreLabel.clearEntityModifiers();
		starNumLabel.clearEntityModifiers();
		shopBtn.clearEntityModifiers();
		tipsSprite.clearEntityModifiers();
		newGameBtn.clearEntityModifiers();
		continueGameBtn.clearEntityModifiers();
	}

	private void fadeInAllFadedOutComponents() {
		FadeInModifier fadeIn = new FadeInModifier(0.5f);
		backBtn.registerEntityModifier(fadeIn);
		medalSprite.registerEntityModifier(fadeIn.deepCopy());
		highScoreLabel.registerEntityModifier(fadeIn.deepCopy());
		starNumLabel.registerEntityModifier(fadeIn.deepCopy());
		shopBtn.registerEntityModifier(fadeIn.deepCopy());
		tipsSprite.registerEntityModifier(fadeIn.deepCopy());
		iconSprite.registerEntityModifier(fadeIn.deepCopy());
		newGameBtn.registerEntityModifier(fadeIn.deepCopy());
		continueGameBtn.registerEntityModifier(fadeIn.deepCopy());
	}

	public void showMainMenu() {
		allComponentsStopActions();
		highScoreLabel.setText(String.valueOf(SPUtils
				.get1010HighScore(activity)));
		highScoreLabel.setLeftPositionX(290);

		setHighScoreAndMedalPosition();
		fadeInAllFadedOutComponents();
		enableAllBtns();

		refreshStarNum();
	}

	public void onPause() {

	}

	private void refreshStarNum() {
		starNumLabel.setText(String.valueOf(SPUtils.getLuckStarNum(activity)));
		starNumLabel.setCentrePositionX(shopBtn.getCentreX()+10);
	}

	public void onResume() {
		refreshStarNum();
	}

}
