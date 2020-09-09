package net.teamfruit.photobomb;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class EffectPhotobomb extends Effect {
    public EffectPhotobomb() {
        super(EffectType.NEUTRAL, 0x000000);
        setRegistryName("effect_photobomb");
        addAttributesModifier(PhotobombAttributes.PHOTOBOMB_TYPE, "B1B267C4-F259-11EA-ADC1-0242AC120002", 1, AttributeModifier.Operation.ADDITION);
    }
}
