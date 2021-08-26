package net.aritsu.events.forgebus;

import net.aritsu.mod.AritsuMod;
import net.aritsu.registry.AritsuEffects;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModPlayerTickEvent {
    static int updateDuration;
    public ModPlayerTickEvent(){}

    @SubscribeEvent
    public static void onLivingUpdateEvent(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level world = player.level;
        updateDuration++;
        if (player.getEffect
                (AritsuEffects.ENERGIZED.get()) != null)
            if (updateDuration % 50 == 0)
                player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
    }

    @SubscribeEvent
    public static void jumpEvent(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof Player player) {
            if (player.getEffect(AritsuEffects.SUGAR_RUSH.get()) != null) {
                double motX = player.getDeltaMovement().x, motY = player.getDeltaMovement().y, motZ = player.getDeltaMovement().z;
                player.setDeltaMovement(motX, motY + 0.3D, motZ);
            }
        }
    }
}
