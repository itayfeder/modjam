package net.aritsu.events.forgebus;

import net.aritsu.mod.AritsuMod;
import net.aritsu.registry.AritsuEntities;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BiomeEvents {
    @SubscribeEvent
    public static void onBiomesLoad(final BiomeLoadingEvent event) {
        System.out.println("BIOME LOAD");

        Biome biome = ForgeRegistries.BIOMES.getValue(event.getName());
        if (biome == ForgeRegistries.BIOMES.getValue(Biomes.TAIGA.location()) ||
                biome == ForgeRegistries.BIOMES.getValue(Biomes.TAIGA_HILLS.location()) ||
                biome == ForgeRegistries.BIOMES.getValue(Biomes.GIANT_TREE_TAIGA.location()) ||
                biome == ForgeRegistries.BIOMES.getValue(Biomes.GIANT_TREE_TAIGA_HILLS.location()) ||
                biome == ForgeRegistries.BIOMES.getValue(Biomes.GIANT_SPRUCE_TAIGA.location()) ||
                biome == ForgeRegistries.BIOMES.getValue(Biomes.GIANT_SPRUCE_TAIGA_HILLS.location())) {
            System.out.println("BIOME: " + biome.getRegistryName().toString());
            event.getSpawns().getSpawner(MobCategory.CREATURE).add(new MobSpawnSettings.SpawnerData(AritsuEntities.GRIZZLY_BEAR.get(), 100, 2, 4));
        }
    }
}
