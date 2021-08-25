package net.aritsu.effcts;

import net.aritsu.mod.AritsuMod;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SugarRushEffect extends MobEffect {
    private final ResourceLocation potionIcon;
    public SugarRushEffect() {
        super(MobEffectCategory.BENEFICIAL, 13893703);
        potionIcon = new ResourceLocation(AritsuMod.MODID,"textures/mob_effect/sturdy.png");
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "91AEAA56-123F-4498-935B-2F7F68070636", 5D, AttributeModifier.Operation.ADDITION);
        addAttributeModifier(Attributes.ATTACK_SPEED, "AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3", (double)0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
    public String getName() {
        return "effect.sugar_rush";
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
