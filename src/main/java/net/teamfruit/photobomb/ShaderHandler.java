package net.teamfruit.photobomb;

import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.TickEvent;

import java.io.IOException;

public class ShaderHandler {
    public static final int SHADER_MOSAIC = 0;
    public static ResourceLocation[] shader_resources = new ResourceLocation[]{
            new ResourceLocation("photobomb", "shaders/post/mosaic.json"),
    };
    public static Framebuffer maskFramebuffer;

    protected void checkShaders(TickEvent.PlayerTickEvent event, Minecraft mc) {
        // Mosaic
        if (event.player.isPotionActive(RegistryPhotobomb.effect)) {
            if (!RenderEventHandler.shaderGroups.containsKey(SHADER_MOSAIC)) {
                try {
                    ShaderGroup sg = new ShaderGroup(
                            mc.getTextureManager(),
                            mc.getResourceManager(),
                            mc.getFramebuffer(),
                            shader_resources[SHADER_MOSAIC]
                    );
                    int width = mc.getMainWindow().getFramebufferWidth();
                    int height = mc.getMainWindow().getFramebufferHeight();
                    sg.addFramebuffer("mask", width, height);
                    maskFramebuffer = sg.getFramebufferRaw("mask");
                    this.setShader(SHADER_MOSAIC, sg);
                } catch (JsonSyntaxException | IOException e) {
                    Log.log.error("Shader load error: ", e);
                }
            }
        } else if (RenderEventHandler.shaderGroups.containsKey(SHADER_MOSAIC)) {
            this.deactivateShader(SHADER_MOSAIC);
        }
    }

    void setShader(int shaderId, ShaderGroup target) {
        if (RenderEventHandler.shaderGroups.containsKey(shaderId)) {
            RenderEventHandler.shaderGroups.get(shaderId).close();
            RenderEventHandler.shaderGroups.remove(shaderId);
        }

        try {
            if (target == null) {
                this.deactivateShader(shaderId);
            } else {
                RenderEventHandler.resetShaders = true;
                RenderEventHandler.shaderGroups.put(shaderId, target);
            }
        } catch (Exception e) {
            RenderEventHandler.shaderGroups.remove(shaderId);
        }
    }

    public void deactivateShader(int shaderId) {
        maskFramebuffer = null;

        if (RenderEventHandler.shaderGroups.containsKey(shaderId)) {
            RenderEventHandler.shaderGroups.get(shaderId).close();
        }

        RenderEventHandler.shaderGroups.remove(shaderId);
    }
}
