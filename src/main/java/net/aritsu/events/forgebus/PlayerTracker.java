package net.aritsu.events.forgebus;

import net.aritsu.capability.PlayerData;
import net.aritsu.mod.AritsuMod;
import net.aritsu.network.NetworkHandler;
import net.aritsu.network.client.ClientPacketSetBackPack;
import net.aritsu.network.client.ClientReceiveOtherBackPack;
import net.aritsu.registry.AritsuItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import java.util.Random;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerTracker {

    private static final Random random = new Random();

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
                if (data.loggedInForTheFirstTime) {
                    ItemStack[] hiker = new ItemStack[]{new ItemStack(AritsuItems.HIKER_ARMOR_HELMET.get()), new ItemStack(AritsuItems.HIKER_ARMOR_CHEST.get()), new ItemStack(AritsuItems.HIKER_ARMOR_LEGS.get()), new ItemStack(AritsuItems.HIKER_ARMOR_BOOTS.get())};
                    for (ItemStack armorStack : hiker) {
                        if (armorStack.getItem() instanceof ArmorItem armorItem) {
                            int index = armorItem.getSlot().getIndex();
                            if (serverPlayer.getInventory().armor.get(index).isEmpty())
                                serverPlayer.getInventory().armor.set(index, armorStack);
                            else
                                serverPlayer.getInventory().add(armorStack);
                        }
                        data.loggedInForTheFirstTime = false;
                    }
                }
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

    @SubscribeEvent
    public static void playerUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            PlayerData.get(player).ifPresent(data -> {
                if (data.isHiker) {
                    float exhaustionLevel = player.getFoodData().getExhaustionLevel();
                    if (data.prevSaturation == -1.0f) {
                        data.prevSaturation = exhaustionLevel;
                    } else {
                        if (data.prevSaturation > exhaustionLevel) {
                            if (random.nextInt(5) == 0) //one chance in 5 or 20% more efficient
                                player.getFoodData().setExhaustion(data.prevSaturation);
                        }
                    }
                } else data.prevSaturation = -1.0f;
            });
        }
    }
}
