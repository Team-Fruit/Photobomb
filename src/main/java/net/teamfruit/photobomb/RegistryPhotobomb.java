package net.teamfruit.photobomb;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
// Event bus for receiving Registry Events)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryPhotobomb {
    public static final EffectPhotobomb effect = new EffectPhotobomb();
    public static final PotionPhotobomb potion = new PotionPhotobomb(new EffectInstance(effect, 3600));

    @SubscribeEvent
    public static void onPotionRegistry(final RegistryEvent.Register<Potion> potionRegistryEvent) {
        // register a new potion here
        Log.log.info("Adding PotionPhotobomb");
        potionRegistryEvent.getRegistry().register(potion);
    }

    @SubscribeEvent
    public static void onEffectRegistry(final RegistryEvent.Register<Effect> effectRegistryEvent) {
        // register a new effect here
        Log.log.info("Adding EffectPhotobomb");
        effectRegistryEvent.getRegistry().register(effect);
    }
}