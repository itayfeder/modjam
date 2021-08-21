package net.aritsu.registry;

import net.aritsu.entity.SitDummyEntity;
import net.aritsu.mod.AritsuMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AritsuEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES,  AritsuMod.MODID);

    public static final RegistryObject<EntityType<SitDummyEntity>> SIT_DUMMY = ENTITIES.register("sit_dummy",
            () -> EntityType.Builder.<SitDummyEntity>of(SitDummyEntity::new, MobCategory.MISC)
            .setTrackingRange(256).setUpdateInterval(20).sized(0.001F, 0.001F).build(AritsuMod.MODID + ":sit_dummy"));

}
