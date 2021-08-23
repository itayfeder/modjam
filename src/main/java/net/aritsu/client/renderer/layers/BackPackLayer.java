package net.aritsu.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.aritsu.registry.AritsuBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;

public class BackPackLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public BackPackLayer(PlayerRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, AbstractClientPlayer player, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {

        poseStack.pushPose();
        this.getParentModel().body.translateAndRotate(poseStack);
        renderWool(player, poseStack, multiBufferSource, packedLight, OverlayTexture.NO_OVERLAY, -0.5f, -0.75f, 0.4f);
        poseStack.popPose();
    }

    private void renderWool(AbstractClientPlayer player, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay, double offsetX, double offsetY, double offsetZ) {

        poseStack.pushPose();
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockState state = AritsuBlocks.BACKPACKBLOCK.get().defaultBlockState();
        float scale = 1f;
        poseStack.scale(scale,scale,scale);

        poseStack.translate(offsetX, offsetY, offsetZ);
        BakedModel model = blockRenderDispatcher.getBlockModel(state);
        blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(),
                multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(InventoryMenu.BLOCK_ATLAS)),
                state, model, 0, 0, 0, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
        poseStack.popPose();
    }
}
