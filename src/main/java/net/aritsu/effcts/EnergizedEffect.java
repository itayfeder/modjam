package net.aritsu.effcts;

import net.aritsu.mod.AritsuMod;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class EnergizedEffect extends MobEffect {
    private final ResourceLocation potionIcon;
    public EnergizedEffect() {
        super(MobEffectCategory.BENEFICIAL, 13893703);
        potionIcon = new ResourceLocation(AritsuMod.MODID,"textures/mob_effect/sturdy.png");
    }
    public String getName() {
        return "effect.energized";
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }

    public boolean isInstant() {
        return false;
    }

    public boolean shouldRenderInvText(EffectInstance effect) {
        return true;
    }

    public boolean shouldRender(EffectInstance effect) {
        return true;
    }

    public boolean shouldRenderHUD(EffectInstance effect) {
        return true;
    }

    public boolean isReady(int duration, int amplifier) {
        return true;
    }
}
