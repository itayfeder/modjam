package net.aritsu.registry;


import net.aritsu.effcts.EnergizedEffect;
import net.aritsu.effcts.SugarRushEffect;
import net.aritsu.mod.AritsuMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AritsuEffects {
    public static DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS,
            AritsuMod.MODID);
    public static final RegistryObject<MobEffect> SUGAR_RUSH = EFFECTS.register("sugar_rush",
            ()-> new SugarRushEffect());
    public static final RegistryObject<MobEffect> ENERGIZED = EFFECTS.register("energized",
            ()-> new EnergizedEffect());
}
