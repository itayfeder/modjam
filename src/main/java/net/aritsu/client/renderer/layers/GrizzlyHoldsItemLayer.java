package net.aritsu.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.aritsu.client.renderer.models.GrizzlyBearModel;
import net.aritsu.entity.grizzly_bear.GrizzlyBear;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class GrizzlyHoldsItemLayer extends RenderLayer<GrizzlyBear, GrizzlyBearModel<GrizzlyBear>> {
    public GrizzlyHoldsItemLayer(RenderLayerParent<GrizzlyBear, GrizzlyBearModel<GrizzlyBear>> renderer) {
        super(renderer);
    }

    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int p_117282_, GrizzlyBear grizzlyBear, float p_117284_, float p_117285_, float p_117286_, float p_117287_, float p_117288_, float p_117289_) {
        ItemStack itemstack = grizzlyBear.getItemBySlot(EquipmentSlot.MAINHAND);
        if (grizzlyBear.isInSittingPose()) {

            poseStack.pushPose();
            if (this.getParentModel().young) {
                poseStack.translate(0.0D, 0.75D, 0.0D);
                poseStack.scale(0.5F, 0.5F, 0.5F);
            }

            getParentModel().translateToFrontRightLeg(poseStack);
            poseStack.mulPose(new Quaternion(90, 0, 0, true));
            poseStack.translate(0.25, -0.3, -0.6f);

            Minecraft.getInstance().getItemInHandRenderer().renderItem(grizzlyBear, itemstack, ItemTransforms.TransformType.GROUND, false, poseStack, multiBufferSource, p_117282_);
            poseStack.popPose();
        }
    }
}