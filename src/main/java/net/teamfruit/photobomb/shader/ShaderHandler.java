package net.teamfruit.photobomb.shader;

import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.teamfruit.photobomb.Log;
import net.teamfruit.photobomb.RegistryPhotobomb;

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
            if (!ShaderRenderEventHandler.shaderGroups.containsKey(SHADER_MOSAIC)) {
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
        } else if (ShaderRenderEventHandler.shaderGroups.containsKey(SHADER_MOSAIC)) {
            this.deactivateShader(SHADER_MOSAIC);
        }
    }

    void setShader(int shaderId, ShaderGroup target) {
        if (ShaderRenderEventHandler.shaderGroups.containsKey(shaderId)) {
            ShaderRenderEventHandler.shaderGroups.get(shaderId).close();
            ShaderRenderEventHandler.shaderGroups.remove(shaderId);
        }

        try {
            if (target == null) {
                this.deactivateShader(shaderId);
            } else {
                ShaderRenderEventHandler.resetShaders = true;
                ShaderRenderEventHandler.shaderGroups.put(shaderId, target);
            }
        } catch (Exception e) {
            ShaderRenderEventHandler.shaderGroups.remove(shaderId);
        }
    }

    public void deactivateShader(int shaderId) {
        maskFramebuffer = null;

        if (ShaderRenderEventHandler.shaderGroups.containsKey(shaderId)) {
            ShaderRenderEventHandler.shaderGroups.get(shaderId).close();
        }

        ShaderRenderEventHandler.shaderGroups.remove(shaderId);
    }
}
