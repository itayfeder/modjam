package net.aritsu.events.modbus;

import net.aritsu.entity.grizzly_bear.GrizzlyBear;
import net.aritsu.mod.AritsuMod;
import net.aritsu.registry.AritsuEntities;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityAttributeEvent {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(AritsuEntities.GRIZZLY_BEAR.get(), GrizzlyBear.createAttributes().build());
    }
}
