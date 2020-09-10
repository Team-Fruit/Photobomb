package net.teamfruit.photobomb;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class PhotobombRenderer {
    private final ResourceLocation res = new ResourceLocation("photobomb", "textures/hidden.png");
    private final RenderType.State state = RenderType.State.getBuilder()
            .texture(new RenderState.TextureState(
                    res, // resource
                    false, // blur
                    false // mipmap
            ))
            .alpha(new RenderState.AlphaState(.2f))
            .build(true);
    private final RenderType renderType = RenderType.makeType(
            "render_photobomb", // name
            DefaultVertexFormats.POSITION_TEX, // vertexFormat
            GL11.GL_QUADS, // drawMode
            256, // bufferSize
            true, // useDelegate
            false, // needsSorting
            state
    );

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Post event) {
        GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
        ActiveRenderInfo info = gameRenderer.getActiveRenderInfo();

        float partialTicks = event.getPartialRenderTick();

        MatrixStack matrixStack = event.getMatrixStack();
        matrixStack.push(); // push matrix

        Quaternion rot = info.getRotation();
        Vec3d camPosition = info.getProjectedView();
        Vec3d playerPosition = event.getPlayer().getEyePosition(partialTicks);
        Vec3d sub = camPosition.subtract(playerPosition).normalize().scale(.8);

        Matrix4f matrix = matrixStack.getLast().getMatrix();
        matrix.mul(Matrix4f.makeTranslate(0f, 1.2f, 0f));
        matrix.mul(Matrix4f.makeTranslate((float) sub.x, (float) sub.y, (float) sub.z));
        matrix.mul(rot);
        matrix.mul(Matrix4f.makeScale(1.6f, 1.6f, 1.6f));

        if (event.getPlayer().getAttribute(PhotobombAttributes.PHOTOBOMB_TYPE).getValue() > 0) {
            IVertexBuilder buffer = event.getBuffers().getBuffer(renderType);
            buffer.pos(matrix, -.5f, +.5f, 0f).tex(1f, 0f).endVertex();
            buffer.pos(matrix, +.5f, +.5f, 0f).tex(0f, 0f).endVertex();
            buffer.pos(matrix, +.5f, -.5f, 0f).tex(0f, 1f).endVertex();
            buffer.pos(matrix, -.5f, -.5f, 0f).tex(1f, 1f).endVertex();
        }

        matrixStack.pop(); // pop matrix
    }
}
