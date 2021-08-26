package net.aritsu.events.forgebus;

import net.aritsu.mod.AritsuMod;
import net.aritsu.registry.AritsuEffects;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModPlayerTickEvent  {
    int updateDuration;
    @SubscribeEvent
    public void onLivingUpdateEvent(TickEvent.PlayerTickEvent event){
       Player player = event.player;
       Level world = player.level;
        updateDuration++;
       if (player.getEffect(AritsuEffects.ENERGIZED.get())!=null)
           if(updateDuration%50==0)
                player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
    }
}
