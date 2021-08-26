package net.aritsu.effcts;

import net.aritsu.mod.AritsuMod;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SugarRushEffect extends MobEffect {
    private final ResourceLocation potionIcon;
    public SugarRushEffect() {
        super(MobEffectCategory.BENEFICIAL, 16423656);
        potionIcon = new ResourceLocation(AritsuMod.MODID,"textures/mob_effect/sugar_rush.png");
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "91AEAA56-123F-4498-935B-2F7F68070636", 0.2D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.JUMP_STRENGTH, "AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3", 3D, AttributeModifier.Operation.ADDITION);
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
