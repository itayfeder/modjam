package net.aritsu.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import net.aritsu.mod.AritsuMod;
import net.aritsu.registry.AritsuItems;
import net.aritsu.registry.AritsuModels;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;

public class BackPackLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final ResourceLocation BACKPACK_TEXTURE_LOCATION = new ResourceLocation(AritsuMod.MODID, "textures/models/backpack.png");

    private final ModelPart backpack;

    public BackPackLayer(PlayerRenderer renderer) {
        super(renderer);

        ModelPart modelpart = Minecraft.getInstance().getEntityModels().bakeLayer(AritsuModels.BACKPACK_CHEST_MODEL_LOCATION);
        this.backpack = modelpart.getChild("backpack");
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, AbstractClientPlayer player, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
        if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() == AritsuItems.BACKPACK.get()) {
            poseStack.pushPose();
            this.getParentModel().body.translateAndRotate(poseStack);
            renderChest(poseStack, multiBufferSource.getBuffer(RenderType.entityCutout(BACKPACK_TEXTURE_LOCATION)), packedLight, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
    }

    private void renderChest(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int overlayLight) {
        backpack.render(poseStack, vertexConsumer, packedLight, overlayLight);
    }
}
