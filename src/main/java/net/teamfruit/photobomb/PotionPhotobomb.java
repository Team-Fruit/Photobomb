package net.teamfruit.photobomb;

import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;

public class PotionPhotobomb extends Potion {
	public PotionPhotobomb(EffectInstance... effectsIn) {
		super(effectsIn);
		setRegistryName("potion_photobomb");
	}
}