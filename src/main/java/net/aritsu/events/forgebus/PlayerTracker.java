package net.aritsu.events.forgebus;

import net.aritsu.capability.PlayerData;
import net.aritsu.mod.AritsuMod;
import net.aritsu.network.NetworkHandler;
import net.aritsu.network.client.ClientPacketSetBackPack;
import net.aritsu.network.client.ClientReceiveOtherBackPack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerTracker {

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof ServerPlayer player) {
            PlayerData.get(player).ifPresent(data -> {
                if (!data.getBackPack().isEmpty() && !player.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                    BlockPos pos = player.getOnPos();
                    Containers.dropItemStack(player.level, pos.getX(), pos.getY(), pos.getZ(), data.getBackPack());
                }
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player newPlayer = event.getPlayer();
        Player original = event.getOriginal();

        //isWasDeath is true if this is a clone because the player died
        //if isWasDeath is false, the player is cloned to another dimension and the contents need to be passed over too
        if (newPlayer.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) && event.isWasDeath() || !event.isWasDeath()) {
            PlayerData.get(original).ifPresent(dataOriginal -> {
                PlayerData.get(newPlayer).ifPresent(dataNew -> {
                    dataNew.addBackpack(dataOriginal.getBackPack());
                });
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
            PlayerData.get(serverPlayer).ifPresent(data -> {
                NetworkHandler.NETWORK.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ClientPacketSetBackPack(data.getBackPack()));
            });
        }
    }

    @SubscribeEvent
    public static void startTracking(PlayerEvent.StartTracking event) {
        if (!event.getPlayer().level.isClientSide())
            if (event.getTarget() instanceof ServerPlayer playerToSendTo && event.getPlayer() instanceof ServerPlayer you) {
                PlayerData.get(you).ifPresent(data -> {
                    NetworkHandler.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> you), new ClientReceiveOtherBackPack(you.getUUID(), data.getBackPack()));
                });
            }
    }
}
