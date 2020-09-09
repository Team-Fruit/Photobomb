package net.teamfruit.photobomb;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

public class PhotobombAttributes {
    public static final IAttribute PHOTOBOMB_TYPE = new RangedAttribute((IAttribute) null, "photobomb.type",
            0.0F, 0.0F, Float.MAX_VALUE).setDescription("Photobomb Type").setShouldWatch(true);
}
