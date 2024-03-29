package net.aritsu.effects;

import net.aritsu.mod.AritsuMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EnergizedEffect extends MobEffect {
    private final ResourceLocation potionIcon;

    public EnergizedEffect() {
        super(MobEffectCategory.BENEFICIAL, 16776973);
        potionIcon = new ResourceLocation(AritsuMod.MODID, "textures/mob_effect/energized.png");
    }

    public String getName() {
        return "effect.energized";
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }
}
