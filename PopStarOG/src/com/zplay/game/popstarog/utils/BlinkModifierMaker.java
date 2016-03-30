package com.zplay.game.popstarog.utils;

import com.orange.entity.modifier.FadeInModifier;
import com.orange.entity.modifier.FadeOutModifier;
import com.orange.entity.modifier.LoopEntityModifier;
import com.orange.entity.modifier.SequenceEntityModifier;

public class BlinkModifierMaker {
	public static LoopEntityModifier make(float duration, int count) {
		float stepDuration = duration / count / 2;
		FadeOutModifier fadeOut = new FadeOutModifier(stepDuration);
		FadeInModifier fadeIn = new FadeInModifier(stepDuration);
		SequenceEntityModifier sequence = new SequenceEntityModifier(fadeOut,
				fadeIn);
		return new LoopEntityModifier(sequence, count);
	}

}
