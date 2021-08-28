package net.aritsu.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.aritsu.capability.PlayerData;
import net.aritsu.item.BackPackItem;
import net.aritsu.registry.AritsuBlocks;
import net.aritsu.util.BagTag;
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
import net.minecraftforge.items.ItemStackHandler;

public class BackPackLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final ItemStackHandler fakeInventory = new ItemStackHandler(10);

    public BackPackLayer(PlayerRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, AbstractClientPlayer player, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {

        if (!(player.getInventory().getArmor(2).isEmpty()) && player.getInventory().getArmor(2).getItem() instanceof BackPackItem) {
            renderPack(poseStack, multiBufferSource, packedLight, OverlayTexture.NO_OVERLAY, -0.5f, 0.435f, 0.25f);

        } else {
            PlayerData.get(player).ifPresent(data -> {
                if (!data.getBackPack().isEmpty()) {
                    renderPack(poseStack, multiBufferSource, packedLight, OverlayTexture.NO_OVERLAY, -0.5f, 0.435f, 0.25f);

                    if (data.getBackPack().hasTag() && data.getBackPack().getTag().contains(BagTag.allItems)) {
                        fakeInventory.deserializeNBT(data.getBackPack().getTag().getCompound(BagTag.allItems));
                        if (!fakeInventory.getStackInSlot(2).isEmpty()) {
                            //TODO render sleepingbag
                        }
                    }
                }
            });
        }
    }

    private void renderPack(PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay, double offsetX, double offsetY, double offsetZ) {

        poseStack.pushPose();
        this.getParentModel().body.translateAndRotate(poseStack);
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockState state = AritsuBlocks.BACKPACK.get().defaultBlockState();
        float scale = 1f;
        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.mulPose(new Quaternion(0, 180, 180, true));
        poseStack.translate(-0.5f, -0.5f, -0.5f);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(offsetX, offsetY, offsetZ);
        BakedModel model = blockRenderDispatcher.getBlockModel(state);
        blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(),
                multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(InventoryMenu.BLOCK_ATLAS)),
                state, model, 0, 0, 0, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
        poseStack.popPose();
    }
}
