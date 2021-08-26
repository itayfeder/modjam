package net.aritsu.events.forgebus;

import net.aritsu.block.SleepingBagBlock;
import net.aritsu.mod.AritsuMod;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerSpawnEvent {
    @SubscribeEvent
    public static void onSetSpawn(PlayerSetSpawnEvent event) {
        if (event.getEntity().getCommandSenderWorld().getBlockState(event.getNewSpawn()).getBlock() instanceof SleepingBagBlock) {
            event.setCanceled(true);
        }
    }
}
