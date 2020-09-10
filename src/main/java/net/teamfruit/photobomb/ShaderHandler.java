package net.teamfruit.photobomb;

import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.TickEvent;

import java.io.IOException;

public class ShaderHandler {
    public static final int SHADER_MOSAIC = 0;
    public static ResourceLocation[] shader_resources = new ResourceLocation[]{
            new ResourceLocation("photobomb", "shaders/post/mosaic.json"),
    };

    protected void checkShaders(TickEvent.PlayerTickEvent event, Minecraft mc) {
        // Mosaic
        if (event.player.isPotionActive(RegistryPhotobomb.effect)) {
            if (!RenderEventHandler.shaderGroups.containsKey(SHADER_MOSAIC)) {
                try {
                    this.setShader(SHADER_MOSAIC, new ShaderGroup(
                            mc.getTextureManager(),
                            mc.getResourceManager(),
                            mc.getFramebuffer(),
                            shader_resources[SHADER_MOSAIC]
                    ));
                } catch (JsonSyntaxException | IOException ignored) {
                    ;
                }
            }
        } else if (RenderEventHandler.shaderGroups.containsKey(0)) {
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
        } catch (Exception var4) {
            RenderEventHandler.shaderGroups.remove(shaderId);
        }
    }

    public void deactivateShader(int shaderId) {
        if (RenderEventHandler.shaderGroups.containsKey(shaderId)) {
            RenderEventHandler.shaderGroups.get(shaderId).close();
        }

        RenderEventHandler.shaderGroups.remove(shaderId);
    }
}
