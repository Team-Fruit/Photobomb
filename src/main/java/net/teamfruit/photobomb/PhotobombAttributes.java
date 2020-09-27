package net.teamfruit.photobomb;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PhotobombAttributes {
    public static final IAttribute PHOTOBOMB_TYPE = new RangedAttribute((IAttribute) null, "photobomb.type",
            0.0F, 0.0F, Float.MAX_VALUE).setDescription("Photobomb Type").setShouldWatch(true);

    @SubscribeEvent
    public static void attachAttributes(EntityEvent.EntityConstructing event) {
        if (event.getEntity() instanceof LivingEntity) {
            final LivingEntity entity = (LivingEntity) event.getEntity();
            final AbstractAttributeMap map = entity.getAttributes();
            if (map.getAttributeInstance(PHOTOBOMB_TYPE) == null)
                map.registerAttribute(PHOTOBOMB_TYPE);
        }
    }
}
