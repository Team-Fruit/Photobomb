package net.teamfruit.photobomb;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("photobomb")
public class Photobomb {

    public Photobomb() {
        // Register ourselves for modloading
        FMLJavaModLoadingContext.get().getModEventBus().register(this);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        // some preinit code
    }

    @SubscribeEvent
    public void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        Framebuffer framebuffer = event.getMinecraftSupplier().get().getFramebuffer();
        Log.log.info("Enabling stencil buffer");
        framebuffer.enableStencil();
        Log.log.info("StencilBuffer: {}", framebuffer.isStencilEnabled());
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        Log.log.info("HELLO from server starting");
    }
}
