package net.aritsu.events.modbus;

import net.aritsu.mod.AritsuMod;
import net.aritsu.network.NetworkHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetupEvent {

    @SubscribeEvent
    public static void setupClient(FMLCommonSetupEvent event) {
        new NetworkHandler();
    }
}
