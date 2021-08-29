package net.aritsu.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.aritsu.client.renderer.layers.GrizzlyHoldsItemLayer;
import net.aritsu.client.renderer.models.GrizzlyBearModel;
import net.aritsu.entity.grizzly_bear.GrizzlyBear;
import net.aritsu.mod.AritsuMod;
import net.aritsu.registry.AritsuModels;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GrizzlyBearRenderer extends MobRenderer<GrizzlyBear, GrizzlyBearModel<GrizzlyBear>> {
    private static final ResourceLocation BEAR_LOCATION = new ResourceLocation(AritsuMod.MODID, "textures/entity/bear/grizzlybear.png");
    private static final ResourceLocation HONEY_LOCATION = new ResourceLocation(AritsuMod.MODID, "textures/entity/bear/grizzlybear_honey.png");

    public GrizzlyBearRenderer(EntityRendererProvider.Context context) {
        super(context, new GrizzlyBearModel<>(context.bakeLayer(AritsuModels.GRIZZLY_BEAR_LOCATION)), 0.9F);
        this.addLayer(new GrizzlyHoldsItemLayer(this));
    }

    public ResourceLocation getTextureLocation(GrizzlyBear grizzlyBear) {
        return grizzlyBear.isInSittingPose() ? HONEY_LOCATION : BEAR_LOCATION;
    }

    protected void scale(GrizzlyBear grizzlyBear, PoseStack poseStack, float aFloat) {
        poseStack.scale(1.2F, 1.2F, 1.2F);
        super.scale(grizzlyBear, poseStack, aFloat);
    }
}