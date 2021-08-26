package net.aritsu.events.modbus;

import net.aritsu.entity.grizzly_bear.GrizzlyBear;
import net.aritsu.mod.AritsuMod;
import net.aritsu.network.NetworkHandler;
import net.aritsu.registry.AritsuEntities;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetupEvent {

    @SubscribeEvent
    public static void setupClient(FMLCommonSetupEvent event) {
        new NetworkHandler();

        event.enqueueWork(() -> {
            SpawnPlacements.register(AritsuEntities.GRIZZLY_BEAR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GrizzlyBear::checkGrizzlyBearSpawnRules);
        });
    }
}
