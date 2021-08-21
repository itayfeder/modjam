package net.aritsu.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import net.aritsu.events.modbus.AritsuModelRegistrationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;

public class BackPackLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final ModelPart lid;
    private final ModelPart bottom;
    private final ModelPart lock;

    public BackPackLayer(PlayerRenderer renderer) {
        super(renderer);

        ModelPart modelpart = Minecraft.getInstance().getEntityModels().bakeLayer(AritsuModelRegistrationEvent.BACKPACK_CHEST_MODEL_LOCATION);
        this.bottom = modelpart.getChild("bottom");
        this.lid = modelpart.getChild("lid");
        this.lock = modelpart.getChild("lock");
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, AbstractClientPlayer player, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {

        poseStack.pushPose();
        this.getParentModel().body.translateAndRotate(poseStack);
        renderWool(player, poseStack, multiBufferSource, packedLight, OverlayTexture.NO_OVERLAY, -0.5f, -0.75f, 0.4f);
        poseStack.popPose();

        poseStack.pushPose();
        this.getParentModel().body.translateAndRotate(poseStack);
        poseStack.mulPose(new Quaternion(0,0,180,true));
        float scale = 0.6f;
        poseStack.scale(scale, scale, scale/2f);
        poseStack.translate(-0.5f,-1f,0.5f);
        renderChest(poseStack, Sheets.CHEST_LOCATION.buffer(multiBufferSource, RenderType::entityCutout), packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    private void renderWool(AbstractClientPlayer player, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay, double offsetX, double offsetY, double offsetZ) {

        poseStack.pushPose();
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockState state = Blocks.RED_WOOL.defaultBlockState();
        float scale = 2.5f;
        poseStack.scale(2 / scale, 0.75f / scale, 0.75f / scale);

        poseStack.translate(offsetX, offsetY, offsetZ);
        BakedModel model = blockRenderDispatcher.getBlockModel(state);
        blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(),
                multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(InventoryMenu.BLOCK_ATLAS)),
                state, model, 0, 0, 0, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
        poseStack.popPose();
    }

    private void renderChest(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int overlayLight) {
        lid.render(poseStack, vertexConsumer, packedLight, overlayLight);
        lock.render(poseStack, vertexConsumer, packedLight, overlayLight);
        bottom.render(poseStack, vertexConsumer, packedLight, overlayLight);
    }
}
