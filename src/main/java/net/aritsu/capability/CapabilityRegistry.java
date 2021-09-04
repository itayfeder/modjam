package net.aritsu.capability;

import net.aritsu.mod.AritsuMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityRegistry {

    @SubscribeEvent
    public static void onAttach(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player)
            event.addCapability(PlayerCapability.KEY, new PlayerCapability(player));
    }

    @SubscribeEvent
    public static void registerCapabilityEvent(RegisterCapabilitiesEvent event) {
        event.register(PlayerData.class);
    }
}