package com.zplay.game.popstarog.scene;

import com.e7studio.android.e7appsdk.utils.LogUtils;
import com.orange.content.SceneBundle;
import com.orange.entity.IEntity;
import com.orange.entity.modifier.DelayModifier;
import com.orange.entity.modifier.IEntityModifier.IEntityModifierListener;
import com.orange.entity.sprite.Sprite;
import com.orange.util.modifier.IModifier;
import com.zplay.game.popstarog.custom.CustomBaseScene;
import com.zplay.game.popstarog.others.GameConstants;
import com.zplay.game.popstarog.utils.AnnouncementShower;
import com.zplay.game.popstarog.utils.ResourceManager;
import com.zplay.game.popstarog.utils.SpriteMaker;

public class SplashScene extends CustomBaseScene {
	private final static String TAG = "SplashScene";

	@Override
	public void onSceneCreate(SceneBundle bundle) {
		super.onSceneCreate(bundle);
		LogUtils.v(TAG, "onSceneCreate...");
		AnnouncementShower.showAnnouncement(getActivity(), this,
				AnnouncementShower.OPEN);
		setSize(GameConstants.BASE_WIDTH, GameConstants.BASE_HEIGHT);

		Sprite sprite = SpriteMaker.makeSpriteWithSingleImageFile("splash",
				getVertexBufferObjectManager());
		sprite.setPosition(0, 0);
		attachChild(sprite);

		DelayModifier fadeOut = new DelayModifier(3f,
				new IEntityModifierListener() {
					public void onModifierStarted(IModifier<IEntity> pModifier,
							IEntity pItem) {
					}

					public void onModifierFinished(
							IModifier<IEntity> pModifier, IEntity pItem) {
						startScene(PopScene.class);
						finish();
						dispose();
						ResourceManager.unloadSplashTextures();
					}
				});
		sprite.registerEntityModifier(fadeOut);
	}

	@Override
	public void onSceneDestroy() {
		super.onSceneDestroy();
		LogUtils.v(TAG, "onSceneDestroy...");
	}

}
