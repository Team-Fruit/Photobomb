package net.teamfruit.photobomb;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class PhotobombRenderer {
    private static final ResourceLocation resHidden = new ResourceLocation("photobomb", "textures/hidden.png");
    private static final ResourceLocation resNoValue = new ResourceLocation("photobomb", "textures/novalue.png");
    private static final RenderType.State stateHidden = RenderType.State.getBuilder()
            .texture(new RenderState.TextureState(
                    resHidden, // resource
                    false, // blur
                    false // mipmap
            ))
            .alpha(new RenderState.AlphaState(.2f))
            .build(true);
    private static final RenderType.State stateNoValue = RenderType.State.getBuilder()
            .texture(new RenderState.TextureState(
                    resNoValue, // resource
                    false, // blur
                    false // mipmap
            ))
            .alpha(new RenderState.AlphaState(.2f))
            .build(true);
    private static final RenderType renderTypeHidden = RenderType.makeType(
            "render_photobomb", // name
            DefaultVertexFormats.POSITION_TEX, // vertexFormat
            GL11.GL_QUADS, // drawMode
            256, // bufferSize
            true, // useDelegate
            false, // needsSorting
            stateHidden
    );
    private static final RenderType renderTypeNoValue = RenderType.makeType(
            "render_photobomb", // name
            DefaultVertexFormats.POSITION_TEX, // vertexFormat
            GL11.GL_QUADS, // drawMode
            256, // bufferSize
            true, // useDelegate
            false, // needsSorting
            stateNoValue
    );

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        IAttributeInstance attribute = event.getPlayer().getAttribute(PhotobombAttributes.PHOTOBOMB_TYPE);
        double value = attribute.getValue();
        if (value > 1) {
            event.setCanceled(true);
            renderHolo(event, renderTypeNoValue);
            renderName(event);
        } else if (value > 0) {
            renderHolo(event, renderTypeHidden);
        }
    }

    private static void renderName(RenderPlayerEvent event) {
        Entity entityIn = event.getEntity();
        if (!(entityIn instanceof AbstractClientPlayerEntity))
            return;
        AbstractClientPlayerEntity playerIn = (AbstractClientPlayerEntity) entityIn;
        PlayerRenderer renderIn = event.getRenderer();
        MatrixStack matrixStackIn = event.getMatrixStack();
        IRenderTypeBuffer bufferIn = event.getBuffers();
        int packedLightIn = event.getLight();
        RenderNameplateEvent renderNameplateEvent = new RenderNameplateEvent(playerIn, playerIn.getDisplayName().getFormattedText(), renderIn, matrixStackIn, bufferIn, packedLightIn);
        MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
        if (renderNameplateEvent.getResult() != Event.Result.DENY) {
            renderIn.renderName(playerIn, renderNameplateEvent.getContent(), matrixStackIn, bufferIn, packedLightIn);
        }
    }

    private static void renderHolo(RenderPlayerEvent event, RenderType type) {
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

        IVertexBuilder buffer = event.getBuffers().getBuffer(type);
        buffer.pos(matrix, -.5f, +.5f, 0f).tex(1f, 0f).endVertex();
        buffer.pos(matrix, +.5f, +.5f, 0f).tex(0f, 0f).endVertex();
        buffer.pos(matrix, +.5f, -.5f, 0f).tex(0f, 1f).endVertex();
        buffer.pos(matrix, -.5f, -.5f, 0f).tex(1f, 1f).endVertex();

        matrixStack.pop(); // pop matrix
    }
}
