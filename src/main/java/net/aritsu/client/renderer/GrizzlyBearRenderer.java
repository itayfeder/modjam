package net.aritsu.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.aritsu.client.renderer.models.GrizzlyBearModel;
import net.aritsu.entity.grizzly_bear.GrizzlyBear;
import net.aritsu.mod.AritsuMod;
import net.aritsu.registry.AritsuModels;
import net.minecraft.client.model.PolarBearModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.PolarBear;

public class GrizzlyBearRenderer extends MobRenderer<GrizzlyBear, GrizzlyBearModel<GrizzlyBear>> {
    private static final ResourceLocation BEAR_LOCATION = new ResourceLocation(AritsuMod.MODID,"textures/entity/bear/grizzlybear.png");

    public GrizzlyBearRenderer(EntityRendererProvider.Context p_174356_) {
        super(p_174356_, new GrizzlyBearModel<>(p_174356_.bakeLayer(AritsuModels.GRIZZLY_BEAR_LOCATION)), 0.9F);
    }

    public ResourceLocation getTextureLocation(GrizzlyBear p_115732_) {
        return BEAR_LOCATION;
    }

    protected void scale(GrizzlyBear p_115734_, PoseStack p_115735_, float p_115736_) {
        p_115735_.scale(1.2F, 1.2F, 1.2F);
        super.scale(p_115734_, p_115735_, p_115736_);
    }
}