package net.aritsu.registry;

import net.aritsu.entity.ReinforcedFishingHookEntity;
import net.aritsu.entity.SitDummyEntity;
import net.aritsu.entity.grizzly_bear.GrizzlyBear;
import net.aritsu.mod.AritsuMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AritsuEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, AritsuMod.MODID);

    public static final RegistryObject<EntityType<SitDummyEntity>> SIT_DUMMY = ENTITIES.register("sit_dummy",
            () -> EntityType.Builder.<SitDummyEntity>of(SitDummyEntity::new, MobCategory.MISC)
                    .setTrackingRange(256).setUpdateInterval(20).noSave().noSummon().sized(0.001F, 0.001F).build(AritsuMod.MODID + ":sit_dummy"));

    public static final RegistryObject<EntityType<ReinforcedFishingHookEntity>> REINFORCED_FISHING_BOBBER = ENTITIES.register("reinforced_fishing_bobber",
            () -> EntityType.Builder.<ReinforcedFishingHookEntity>of(ReinforcedFishingHookEntity::new, MobCategory.MISC)
                    .noSave().noSummon().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(5).build(AritsuMod.MODID + ":reinforced_fishing_bobber"));

    public static final RegistryObject<EntityType<GrizzlyBear>> GRIZZLY_BEAR = ENTITIES.register("grizzly_bear",
            () -> EntityType.Builder.of(GrizzlyBear::new, MobCategory.CREATURE)
                    .sized(1.4F, 1.4F).clientTrackingRange(10).build(AritsuMod.MODID + ":grizzly_bear"));

}
