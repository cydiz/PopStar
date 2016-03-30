package com.zplay.game.popstarog.particle;

import com.orange.entity.particle.emitter.CircleParticleEmitter;
import com.orange.entity.particle.initializer.AccelerationParticleInitializer;
import com.orange.entity.particle.initializer.RotationParticleInitializer;
import com.orange.entity.particle.initializer.SizeParticleInitializer;
import com.orange.entity.particle.initializer.VelocityParticleInitializer;
import com.orange.entity.particle.modifier.ScaleParticleModifier;
import com.orange.entity.sprite.Sprite;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.res.RegionRes;
import com.zplay.game.popstarog.others.GameConstants;

public class FireworkParticleMaker {

	public static FireworkParticle make(
			VertexBufferObjectManager vertexBufferObjectManager, float x,
			float y, int type) {
		String textureRegionName = null;

		switch (type) {
		case GameConstants.STAR_BLUE:
			textureRegionName = "blue_star";
			break;
		case GameConstants.STAR_RED:
			textureRegionName = "red_star";
			break;
		case GameConstants.STAR_GREEEN:
			textureRegionName = "green_star";
			break;
		case GameConstants.STAR_YELLOW:
			textureRegionName = "yellow_star";
			break;
		case GameConstants.STAR_PURPLE:
			textureRegionName = "pink_star";
			break;

		}
		final FireworkParticle particle = new FireworkParticle(
				new CircleParticleEmitter(x, y, 0), 30, 30, 30,
				RegionRes.getTextureRegion(textureRegionName),
				vertexBufferObjectManager);
		particle.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(
				0f, 10));
		particle.addParticleInitializer(new RotationParticleInitializer<Sprite>(
				0, 360));
		particle.addParticleInitializer(new VelocityParticleInitializer<Sprite>(
				-40, 40, false, -40, 30f, false));
		particle.addParticleInitializer(new SizeParticleInitializer<Sprite>(
				12.2f, 20.8f));
		particle.addParticleModifier(new ScaleParticleModifier<Sprite>(0, 2.2f,
				1.0f, 0f));
		particle.setCentrePosition(x, y);
		particle.setRateSecond(0.001f);
		particle.reset();
		return particle;
	}
}
