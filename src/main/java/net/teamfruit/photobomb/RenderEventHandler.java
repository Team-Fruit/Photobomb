package net.teamfruit.photobomb;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

@EventBusSubscriber({Dist.CLIENT})
public class RenderEventHandler {
    private static ShaderHandler shaderhandler = new ShaderHandler();
    public static HashMap<Integer, ShaderGroup> shaderGroups = new HashMap<>();

    public static boolean resetShaders = false;
    private static int oldDisplayWidth = 0;
    private static int oldDisplayHeight = 0;

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.side != LogicalSide.SERVER && event.player.getEntityId() == mc.player.getEntityId()) {
            if (event.phase == TickEvent.Phase.START) {
                try {
                    shaderhandler.checkShaders(event, mc);
                } catch (Exception ignored) {
                    ;
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderShaders(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.ALL) {
            Minecraft mc = Minecraft.getInstance();
            if (shaderGroups.size() > 0) {
                updateShaderFrameBuffers(mc);

                //GL11.glLoadIdentity();

                for (ShaderGroup sg : shaderGroups.values()) {
                    //GL11.glPushMatrix();

                    try {
                        sg.render(event.getPartialTicks());
                    } catch (Exception ignored) {
                        ;
                    }

                    //GL11.glPopMatrix();
                }

                mc.getFramebuffer().bindFramebuffer(true);
            }
        }

    }

    private static void updateShaderFrameBuffers(Minecraft mc) {
        int width = mc.getMainWindow().getFramebufferWidth();
        int height = mc.getMainWindow().getFramebufferHeight();
        if (resetShaders
                || width != oldDisplayWidth
                || height != oldDisplayHeight) {
            for (ShaderGroup sg : shaderGroups.values()) {
                sg.createBindFramebuffers(width, height);
            }

            oldDisplayWidth = width;
            oldDisplayHeight = height;
            resetShaders = false;
        }
    }
}
