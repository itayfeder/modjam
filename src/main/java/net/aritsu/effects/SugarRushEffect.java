package net.aritsu.effects;

import net.aritsu.mod.AritsuMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SugarRushEffect extends MobEffect {
    private final ResourceLocation potionIcon;

    public SugarRushEffect() {
        super(MobEffectCategory.BENEFICIAL, 16423656);
        potionIcon = new ResourceLocation(AritsuMod.MODID, "textures/mob_effect/sugar_rush.png");
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "91AEAA56-123F-4498-935B-2F7F68070636", 0.4D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public String getName() {
        return "effect.sugar_rush";
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }

}
