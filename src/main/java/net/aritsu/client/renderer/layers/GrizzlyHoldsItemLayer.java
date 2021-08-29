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
        if (grizzlyBear.isInSittingPose() ) {
            float f = -0.6F;
            float f1 = 1.4F;

            poseStack.pushPose();
            getParentModel().translateToFrontRightLeg(poseStack);
            poseStack.translate(0.2f,0.55f,-0.4f);
            poseStack.mulPose(new Quaternion(90,10,0,true));

            //poseStack.translate((double)0.1F, (double)f1, (double)f);
            Minecraft.getInstance().getItemInHandRenderer().renderItem(grizzlyBear, itemstack, ItemTransforms.TransformType.GROUND, false, poseStack, multiBufferSource, p_117282_);
            poseStack.popPose();
        }
    }
}