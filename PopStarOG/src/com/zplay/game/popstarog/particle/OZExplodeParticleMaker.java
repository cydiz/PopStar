package com.zplay.game.popstarog.particle;

import com.orange.entity.particle.emitter.CircleParticleEmitter;
import com.orange.entity.particle.initializer.AccelerationParticleInitializer;
import com.orange.entity.particle.initializer.ColorParticleInitializer;
import com.orange.entity.particle.initializer.RotationParticleInitializer;
import com.orange.entity.particle.initializer.SizeParticleInitializer;
import com.orange.entity.particle.initializer.VelocityParticleInitializer;
import com.orange.entity.particle.modifier.AlphaParticleModifier;
import com.orange.entity.particle.modifier.ColorParticleModifier;
import com.orange.entity.particle.modifier.ScaleParticleModifier;
import com.orange.entity.sprite.Sprite;
import com.orange.opengl.vbo.VertexBufferObjectManager;
import com.orange.res.RegionRes;
import com.zplay.game.popstarog.others.GameConstants;

public class OZExplodeParticleMaker {
	public static PopParticle make(
			VertexBufferObjectManager vertexBufferObjectManager, float x,
			float y, int type) {
		final PopParticle particle = new PopParticle(new CircleParticleEmitter(
				x, y, 0), 10, 10, 10, RegionRes.getTextureRegion("particle"),
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

		particle.addParticleModifier(new ColorParticleModifier<Sprite>(0, 2,
				getR(type), 0, getG(type), 0, getB(type), 0));

		particle.addParticleModifier(new AlphaParticleModifier<Sprite>(0, 2, 1,
				0));
		particle.addParticleModifier(new ScaleParticleModifier<Sprite>(0, 2,
				1.0f, 0.5f));
		particle.setCentrePosition(x, y);
		particle.setRateSecond(0.02f);
		particle.reset();
		return particle;
	}

	private static float getR(int type) {
		float r = 0f;
		switch (type) {
		case GameConstants.BLOCK_0: 	//1 点 紫色
			r = 0.6f;
			break;
		case GameConstants.BLOCK_1: 	//9 田 绿色
			r = 0.6f;
			break;
		case GameConstants.BLOCK_2: 	//4 田 深青色：119,187,46
			r = 0.47f;
			break;
		case GameConstants.BLOCK_3: 	//3 L 红色：226,88,76
			r = 0.89f;
			break;
		case GameConstants.BLOCK_4: 	//蓝色
			r = 0.36f;
			break;
		case GameConstants.BLOCK_5: 	//2 横条 橙色
			r = 0.97f;
			break;
		case GameConstants.BLOCK_6: 	//3 横条 青色：76,214,174 
			r = 0.3f;
			break;
		case GameConstants.BLOCK_7: 	//4 横条 黄色
			r = 0.88f;
			break;
		case GameConstants.BLOCK_8: 	//5 横条 粉红色
			r = 0.9f;
			break;
		case GameConstants.BLOCK_9: //小点点 226,232,242
			r = 0.89f;
			break;
		}
		return r;
	}

	private static float getG(int type) {
		float g = 0f;
		switch (type) {
		case GameConstants.BLOCK_0:
			g = 0.39f;
			break;
		case GameConstants.BLOCK_1:
			g = 0.87f;
			break;
		case GameConstants.BLOCK_2:
			g = 0.73f;
			break;
		case GameConstants.BLOCK_3:
			g = 0.35f;
			break;
		case GameConstants.BLOCK_4:
			g = 0.74f;
			break;
		case GameConstants.BLOCK_5:
			g = 0.52f;
			break;
		case GameConstants.BLOCK_6:
			g = 0.84f;
			break;
		case GameConstants.BLOCK_7:
			g = 0.76f;
			break;
		case GameConstants.BLOCK_8:
			g = 0.42f;
			break;
		case GameConstants.BLOCK_9:
			g = 0.91f;
			break;
		}
		return g;
	}

	private static float getB(int type) {
		float b = 0f;
		switch (type) {
		case GameConstants.BLOCK_0:
			b = 0.88f;
			break;
		case GameConstants.BLOCK_1:
			b = 0.33f;
			break;
		case GameConstants.BLOCK_2:
			b = 0.18f;
			break;
		case GameConstants.BLOCK_3:
			b = 0.3f;
			break;
		case GameConstants.BLOCK_4:
			b = 0.89f;
			break;
		case GameConstants.BLOCK_5:
			b = 0.25f;
			break;
		case GameConstants.BLOCK_6:
			b = 0.68f;
			break;
		case GameConstants.BLOCK_7:
			b = 0.23f;
			break;
		case GameConstants.BLOCK_8:
			b = 0.51f;
			break;
		case GameConstants.BLOCK_9:
			b = 0.95f;
			break;
		}
		return b;
	}
}
