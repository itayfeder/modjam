package net.aritsu.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.aritsu.entity.ReinforcedFishingHookEntity;
import net.aritsu.mod.AritsuMod;
import net.aritsu.registry.AritsuItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ReinforcedFishingHookRenderer extends EntityRenderer<ReinforcedFishingHookEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(AritsuMod.MODID, "textures/entity/reinforced_fishing_hook.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutout(TEXTURE_LOCATION);
    private static final double VIEW_BOBBING_SCALE = 960.0D;

    public ReinforcedFishingHookRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    private static float fraction(int divided, int divider) {
        return (float) divided / (float) divider;
    }

    private static void vertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, int p_114715_, float x, int z, int u, int v) {
        vertexConsumer.vertex(matrix4f, x - 0.5F, (float) z - 0.5F, 0.0F).color(255, 255, 255, 255).uv((float) u, (float) v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_114715_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void stringVertex(float p_174119_, float p_174120_, float p_174121_, VertexConsumer p_174122_, PoseStack.Pose p_174123_, float p_174124_, float p_174125_) {
        float f = p_174119_ * p_174124_;
        float f1 = p_174120_ * (p_174124_ * p_174124_ + p_174124_) * 0.5F + 0.25F;
        float f2 = p_174121_ * p_174124_;
        float f3 = p_174119_ * p_174125_ - f;
        float f4 = p_174120_ * (p_174125_ * p_174125_ + p_174125_) * 0.5F + 0.25F - f1;
        float f5 = p_174121_ * p_174125_ - f2;
        float f6 = Mth.sqrt(f3 * f3 + f4 * f4 + f5 * f5);
        f3 = f3 / f6;
        f4 = f4 / f6;
        f5 = f5 / f6;
        p_174122_.vertex(p_174123_.pose(), f, f1, f2).color(0, 0, 0, 255).normal(p_174123_.normal(), f3, f4, f5).endVertex();
    }

    public void render(ReinforcedFishingHookEntity fishingHookEntity, float p_114706_, float p_114707_, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_114710_) {
        Player player = fishingHookEntity.getPlayerOwner();
        if (player != null) {
            poseStack.pushPose();
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            PoseStack.Pose posestack$pose = poseStack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();
            VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RENDER_TYPE);
            vertex(vertexconsumer, matrix4f, matrix3f, p_114710_, 0.0F, 0, 0, 1);
            vertex(vertexconsumer, matrix4f, matrix3f, p_114710_, 1.0F, 0, 1, 1);
            vertex(vertexconsumer, matrix4f, matrix3f, p_114710_, 1.0F, 1, 1, 0);
            vertex(vertexconsumer, matrix4f, matrix3f, p_114710_, 0.0F, 1, 0, 0);
            poseStack.popPose();
            int i = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
            ItemStack itemstack = player.getMainHandItem();
            if (!itemstack.is(AritsuItems.REINFORCED_FISHING_ROD.get())) {
                i = -i;
            }

            float f = player.getAttackAnim(p_114707_);
            float f1 = Mth.sin(Mth.sqrt(f) * (float) Math.PI);
            float f2 = Mth.lerp(p_114707_, player.yBodyRotO, player.yBodyRot) * ((float) Math.PI / 180F);
            double d0 = Mth.sin(f2);
            double d1 = Mth.cos(f2);
            double d2 = (double) i * 0.35D;
            double d3 = 0.8D;
            double d4;
            double d5;
            double d6;
            float f3;
            if ((this.entityRenderDispatcher.options == null || this.entityRenderDispatcher.options.getCameraType().isFirstPerson()) && player == Minecraft.getInstance().player) {
                double d7 = 960.0D / this.entityRenderDispatcher.options.fov;
                Vec3 vec3 = this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane((float) i * 0.525F, -0.1F);
                vec3 = vec3.scale(d7);
                vec3 = vec3.yRot(f1 * 0.5F);
                vec3 = vec3.xRot(-f1 * 0.7F);
                d4 = Mth.lerp(p_114707_, player.xo, player.getX()) + vec3.x;
                d5 = Mth.lerp(p_114707_, player.yo, player.getY()) + vec3.y;
                d6 = Mth.lerp(p_114707_, player.zo, player.getZ()) + vec3.z;
                f3 = player.getEyeHeight();
            } else {
                d4 = Mth.lerp(p_114707_, player.xo, player.getX()) - d1 * d2 - d0 * 0.8D;
                d5 = player.yo + (double) player.getEyeHeight() + (player.getY() - player.yo) * (double) p_114707_ - 0.45D;
                d6 = Mth.lerp(p_114707_, player.zo, player.getZ()) - d0 * d2 + d1 * 0.8D;
                f3 = player.isCrouching() ? -0.1875F : 0.0F;
            }

            double d9 = Mth.lerp(p_114707_, fishingHookEntity.xo, fishingHookEntity.getX());
            double d10 = Mth.lerp(p_114707_, fishingHookEntity.yo, fishingHookEntity.getY()) + 0.25D;
            double d8 = Mth.lerp(p_114707_, fishingHookEntity.zo, fishingHookEntity.getZ());
            float f4 = (float) (d4 - d9);
            float f5 = (float) (d5 - d10) + f3;
            float f6 = (float) (d6 - d8);
            VertexConsumer vertexconsumer1 = multiBufferSource.getBuffer(RenderType.lineStrip());
            PoseStack.Pose posestack$pose1 = poseStack.last();
            int j = 16;

            for (int k = 0; k <= 16; ++k) {
                stringVertex(f4, f5, f6, vertexconsumer1, posestack$pose1, fraction(k, 16), fraction(k + 1, 16));
            }

            poseStack.popPose();
            super.render(fishingHookEntity, p_114706_, p_114707_, poseStack, multiBufferSource, p_114710_);
        }
    }

    public ResourceLocation getTextureLocation(ReinforcedFishingHookEntity hookEntity) {
        return TEXTURE_LOCATION;
    }
}