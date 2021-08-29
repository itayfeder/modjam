package net.aritsu.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.aritsu.events.forgebus.ModelLoadingEvent;
import net.aritsu.item.TravelerArmorItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.IForgeBakedModel;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Random;

public class HeadLightLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final ItemStackHandler fakeInventory = new ItemStackHandler(10);

    public HeadLightLayer(PlayerRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        if (!(player.getInventory().getArmor(3).isEmpty()) && player.getInventory().getArmor(3).getItem() instanceof TravelerArmorItem item) {
            if (item.getSlot()==EquipmentSlot.HEAD) {
                poseStack.pushPose();
                getParentModel().getHead().translateAndRotate(poseStack);

                // pixel scale * pixel offset = 16 pixels is one block
                float scale = 0.0625f * (16 + 1);
                poseStack.scale(scale, scale, scale);

                poseStack.mulPose(Vector3f.XP.rotationDegrees(180));
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180));

                poseStack.translate(-(0.0625f * 8), (0.0625f * 3), -(0.0625f * 7));
                IForgeBakedModel model = Minecraft.getInstance().getModelManager().getModel(ModelLoadingEvent.HEADLIGHT);
                render(model, multiBufferSource, RenderType.entityCutoutNoCull(InventoryMenu.BLOCK_ATLAS), poseStack, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
                poseStack.popPose();
            }
        }
    }

    private void render(IForgeBakedModel model, MultiBufferSource multiBufferSource, RenderType renderType, PoseStack poseStack, int packedLight, int overlay, int color) {
        Random rand = new Random(42);

        float a = ((color >> 24) & 0xFF) / 255.0f;
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = ((color >> 0) & 0xFF) / 255.0f;

        VertexConsumer bb = multiBufferSource.getBuffer(renderType);
        for (BakedQuad quad : model.getQuads(null, null, rand, EmptyModelData.INSTANCE))
        {
            bb.putBulkData(poseStack.last(), quad, r, g, b, a, packedLight, overlay, true);
        }
    }
}
