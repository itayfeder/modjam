package net.aritsu.registry;

import com.google.common.collect.ImmutableSet;
import net.aritsu.mod.AritsuMod;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AritsuVillagers {
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, AritsuMod.MODID);
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, AritsuMod.MODID);

    public static final RegistryObject<PoiType> CAMPFIRE = POI_TYPES.register("campfire", () -> new PoiType("camper", PoiType.getBlockStates(Blocks.CAMPFIRE), 1, 1));
    public static final RegistryObject<VillagerProfession> CAMPER = PROFESSIONS.register("camper", () -> new VillagerProfession("camper", CAMPFIRE.get(), ImmutableSet.of(AritsuItems.MARSHMALLOW.get(), AritsuItems.BLUEBERRY.get()), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_LEATHERWORKER));


}
