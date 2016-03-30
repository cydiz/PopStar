package com.zplay.game.popstarog.particle;

import com.orange.engine.handler.IUpdateHandler;
import com.orange.entity.particle.emitter.CircleParticleEmitter;
import com.orange.entity.particle.initializer.AccelerationParticleInitializer;
import com.orange.entity.particle.initializer.ColorParticleInitializer;
import com.orange.entity.particle.initializer.RotationParticleInitializer;
import com.orange.entity.particle.initializer.SizeParticleInitializer;
import com.orange.entity.particle.initializer.VelocityParticleInitializer;
import com.orange.entity.sprite.Sprite;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.res.RegionRes;
import com.zplay.game.popstarog.others.GameConstants;

public class OZParticleMaker {
	public static OZParticle make(
			VertexBufferObjectManager vertexBufferObjectManager, float x,
			float y, int type) {
		final OZParticle particle = new OZParticle(new CircleParticleEmitter(x,
				y, 0), 5, 5, 5, RegionRes.getTextureRegion("particle"),
				vertexBufferObjectManager);
		particle.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(
				0.1f, 800));
		particle.addParticleInitializer(new RotationParticleInitializer<Sprite>(
				0, 180));
		particle.addParticleInitializer(new VelocityParticleInitializer<Sprite>(
				-880, 880, false, -880, -240f, false));
		particle.addParticleInitializer(new SizeParticleInitializer<Sprite>(32,
				40));
		particle.addParticleInitializer(new ColorParticleInitializer<Sprite>(
				getR(type), getG(type), getB(type)));
		particle.setCentrePosition(x, y);
		particle.setRateSecond(0.02f);
		particle.reset();

		particle.registerUpdateHandler(new IUpdateHandler() {
			float delta = 0;
			public void reset() {
			}
			public void onUpdate(float pSecondsElapsed) {
				delta += pSecondsElapsed;
				if (delta >= 2) {
					particle.detachSelf();
					particle.dispose();
				}
			}
		});
		return particle;
	}

	private static float getR(int type) {
		float r = 0f;
		switch (type) {
		case GameConstants.STAR_BLUE:
			r = 0.23f;
			break;
		case GameConstants.STAR_RED:
			r = 0.94f;
			break;
		case GameConstants.STAR_GREEEN:
			r = 0.23f;
			break;
		case GameConstants.STAR_YELLOW:
			r = 1.0f;
			break;
		case GameConstants.STAR_PURPLE:
			r = 1.0f;
			break;
		}
		return r;
	}

	private static float getG(int type) {
		float g = 0f;
		switch (type) {
		case GameConstants.STAR_BLUE:
			g = 0.63f;
			break;
		case GameConstants.STAR_RED:
			g = 0.2f;
			break;
		case GameConstants.STAR_GREEEN:
			g = 1.0f;
			break;
		case GameConstants.STAR_YELLOW:
			g = 0.99f;
			break;
		case GameConstants.STAR_PURPLE:
			g = 0.25f;
			break;
		}
		return g;
	}

	private static float getB(int type) {
		float b = 0f;
		switch (type) {
		case GameConstants.STAR_BLUE:
			b = 1.0f;
			break;
		case GameConstants.STAR_RED:
			b = 0.2f;
			break;
		case GameConstants.STAR_GREEEN:
			b = 0.33f;
			break;
		case GameConstants.STAR_YELLOW:
			b = 0.22f;
			break;
		case GameConstants.STAR_PURPLE:
			b = 0.94f;
			break;
		}
		return b;
	}

}
