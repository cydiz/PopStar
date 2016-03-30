package com.zplay.game.popstarog.custom;

import com.orange.entity.IEntity;
import com.orange.entity.layer.Layer;
import com.orange.entity.modifier.IEntityModifier;
import com.orange.entity.modifier.ScaleModifier;
import com.orange.entity.primitive.Rectangle;
import com.orange.input.touch.TouchEvent;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.util.modifier.IModifier;
import com.orange.util.modifier.IModifier.IModifierListener;
import com.orange.util.modifier.ease.EaseBackIn;
import com.orange.util.modifier.ease.EaseBackOut;

public class Dialog extends Layer {

	// 是否相应back键
	private boolean isBackKeyResponsed = true;
	private CustomBaseScene baseScene;
	private DialogDismissListener dialogDismissListener;
	private DialogBG dialogBG;

	private IEntityModifier dismissModifier;

	public Dialog(float pX, float pY, float pWidth, float pHeight,
			CustomBaseScene pScene) {
		super(pX, pY, pWidth, pHeight, pScene);
		this.baseScene = pScene;
		setScaleCenter(getWidthHalf(), getHeightHalf());

	}

	public Dialog(CustomBaseScene scene) {
		this(0, 0, scene.getWidth(), scene.getHeight(), scene);
		setScaleCenter(getWidthHalf(), getHeightHalf());
	}

	@Override
	public void setWidth(float pWidth) {
		super.setWidth(pWidth);
		setScaleCenterX(pWidth / 2);
	}

	@Override
	public void setHeight(float pHeight) {
		super.setHeight(pHeight);
		setScaleCenterY(pHeight / 2);
	}

	@Override
	public void setSize(float pWidth, float pHeight) {
		super.setSize(pWidth, pHeight);
		setScaleCenter(pWidth / 2, pHeight / 2);
	}

	public DialogDismissListener getDialogDismissListener() {
		return dialogDismissListener;
	}

	public void setDialogDismissListener(
			DialogDismissListener dialogDismissListener) {
		this.dialogDismissListener = dialogDismissListener;
	}

	public boolean isBackKeyResponsed() {
		return isBackKeyResponsed;
	}

	public void setBackKeyResponsed(boolean isKeyEnabled) {
		this.isBackKeyResponsed = isKeyEnabled;
	}

	public void show() {
		dialogBG = new DialogBG(0, 0, baseScene.getWidth(),
				baseScene.getHeight(), getVertexBufferObjectManager());
		baseScene.attachChild(dialogBG, Integer.MAX_VALUE);
		baseScene.attachChild(this, Integer.MAX_VALUE);
		baseScene.getDialogStack().add(this);
	}

	public void showWithAnimation() {
		dialogBG = new DialogBG(0, 0, baseScene.getWidth(),
				baseScene.getHeight(), getVertexBufferObjectManager());
		baseScene.attachChild(dialogBG, Integer.MAX_VALUE);
		ScaleModifier scale = new ScaleModifier(0.5f, 0, 1,
				EaseBackOut.getInstance());
		dismissModifier = new ScaleModifier(0.5f, 1.0f, 0f,
				EaseBackIn.getInstance());
		registerEntityModifier(scale);
		setScale(0f);
		baseScene.attachChild(this, Integer.MAX_VALUE);
		baseScene.getDialogStack().add(this);
	}

	public void dismiss() {
		if (dismissModifier != null) {
			registerEntityModifier(dismissModifier);
			dismissModifier
					.addModifierListener(new IModifierListener<IEntity>() {
						public void onModifierStarted(
								IModifier<IEntity> pModifier, IEntity pItem) {
						}

						public void onModifierFinished(
								IModifier<IEntity> pModifier, IEntity pItem) {
							if (Dialog.this.hasParent()) {
								dialogBG.detachSelf();
								dialogBG.dispose();
								detachChildren();
								detachSelf();
								dispose();
								baseScene.getDialogStack().pop();
								if (dialogDismissListener != null) {
									dialogDismissListener.onDialogDismiss();
								}
							}
						}
					});
		} else {
			if (this.hasParent()) {
				dialogBG.detachSelf();
				dialogBG.dispose();
				detachChildren();
				detachSelf();
				dispose();
				baseScene.getDialogStack().pop();
				if (dialogDismissListener != null) {
					dialogDismissListener.onDialogDismiss();
				}
			}
		}

	}

	public void dismissWithoutAnimation() {
		if (this.hasParent()) {
			dialogBG.detachSelf();
			dialogBG.dispose();
			detachChildren();
			detachSelf();
			dispose();
			baseScene.getDialogStack().pop();
			if (dialogDismissListener != null) {
				dialogDismissListener.onDialogDismiss();
			}
		}
	}

	public void dismissWithAnimamtion() {
		ScaleModifier scaleTo = new ScaleModifier(0.5f, 1.0f, 0f,
				EaseBackIn.getInstance());
		registerEntityModifier(scaleTo);

		scaleTo.addModifierListener(new IModifierListener<IEntity>() {
			public void onModifierStarted(IModifier<IEntity> pModifier,
					IEntity pItem) {
			}

			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				if (Dialog.this.hasParent()) {
					dialogBG.detachSelf();
					dialogBG.dispose();
					detachChildren();
					detachSelf();
					dispose();
					baseScene.getDialogStack().pop();
					if (dialogDismissListener != null) {
						dialogDismissListener.onDialogDismiss();
					}
				}
			}
		});
	}
}

class DialogBG extends Rectangle {
	public DialogBG(float pX, float pY, float pWidth, float pHeight,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		setColor(0, 0, 0);
		setAlpha(127 * 1.0f / 255);
		setIgnoreTouch(false);
	}

	@Override
	public boolean onTouch(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		return true;
	}

}
