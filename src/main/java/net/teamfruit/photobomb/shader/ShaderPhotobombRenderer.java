package net.teamfruit.photobomb.shader;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

//@Mod.EventBusSubscriber({Dist.CLIENT})
public class ShaderPhotobombRenderer {
    @SubscribeEvent
    public static void onRenderGuiMask(RenderGameOverlayEvent.Post event) {
        MainWindow window = event.getWindow();
        int width = window.getScaledWidth();
        int height = window.getScaledHeight();

        if (ShaderHandler.maskFramebuffer != null) {
            ShaderHandler.maskFramebuffer.bindFramebuffer(false);

            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glPushMatrix(); // push matrix

            GL11.glColor4f(1, 1, 1, 1);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            Matrix4f matrix = Matrix4f.makeScale(width, height, 1);
            matrix.mul(Matrix4f.makeScale(.9f, .9f, 1f));

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(matrix, 0f, 1f, 0f).tex(1f, 0f).endVertex();
            buffer.pos(matrix, 1f, 1f, 0f).tex(0f, 0f).endVertex();
            buffer.pos(matrix, 1f, 0f, 0f).tex(0f, 1f).endVertex();
            buffer.pos(matrix, 0f, 0f, 0f).tex(1f, 1f).endVertex();
            tessellator.draw();

            GL11.glPopMatrix(); // pop matrix
            GL11.glPopAttrib();

            //ShaderHandler.maskFramebuffer.unbindFramebuffer();
            Minecraft.getInstance().getFramebuffer().bindFramebuffer(true);
        }
    }

    /*
    @SubscribeEvent
    public static void onRenderPlayerMask(RenderPlayerEvent.Post event) {
        GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
        ActiveRenderInfo info = gameRenderer.getActiveRenderInfo();

        float partialTicks = event.getPartialRenderTick();

        MatrixStack matrixStack = event.getMatrixStack();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix(); // push matrix

        if (ShaderHandler.maskFramebuffer != null) {
            ShaderHandler.maskFramebuffer.bindFramebuffer(true);

            GL11.glColor4f(1, 1, 1, 1);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            Quaternion rot = info.getRotation();
            Vec3d camPosition = info.getProjectedView();
            Vec3d playerPosition = event.getPlayer().getEyePosition(partialTicks);
            Vec3d sub = camPosition.subtract(playerPosition).normalize().scale(.8);

            Matrix4f matrix = matrixStack.getLast().getMatrix();
            GL11.glTranslatef(0f, 1.2f, 0f);
            GL11.glTranslatef((float) sub.x, (float) sub.y, (float) sub.z);
            float qw = rot.getW();
            GL11.glRotated(2 * Math.acos(qw),
                    rot.getX() / Math.sqrt(1 - qw * qw),
                    rot.getY() / Math.sqrt(1 - qw * qw),
                    rot.getZ() / Math.sqrt(1 - qw * qw));
            GL11.glScalef(1.6f, 1.6f, 1.6f);

            IAttributeInstance attribute = event.getPlayer().getAttribute(PhotobombAttributes.PHOTOBOMB_TYPE);
            if (attribute != null && attribute.getValue() > 0) {
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                buffer.pos(matrix, -.5f, +.5f, 0f).tex(1f, 0f).endVertex();
                buffer.pos(matrix, +.5f, +.5f, 0f).tex(0f, 0f).endVertex();
                buffer.pos(matrix, +.5f, -.5f, 0f).tex(0f, 1f).endVertex();
                buffer.pos(matrix, -.5f, -.5f, 0f).tex(1f, 1f).endVertex();
                tessellator.draw();
            }

            Minecraft.getInstance().getFramebuffer().bindFramebuffer(true);
        }

        GL11.glPopMatrix(); // pop matrix
        GL11.glPopAttrib();
    }
     */
}
