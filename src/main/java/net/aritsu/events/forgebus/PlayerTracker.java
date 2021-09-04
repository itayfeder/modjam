package net.aritsu.events.forgebus;

import net.aritsu.capability.PlayerData;
import net.aritsu.item.TravelerArmorItem;
import net.aritsu.mod.AritsuMod;
import net.aritsu.network.NetworkHandler;
import net.aritsu.network.client.ClientPacketSetBackPack;
import net.aritsu.network.client.ClientReceiveOtherBackPack;
import net.aritsu.registry.AritsuEffects;
import net.aritsu.registry.AritsuItems;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerTracker {

    private static final Random random = new Random();
    private static final UUID SPEED_BOOTS_MODIFIER_UUID = UUID.fromString("5bc8125c-597f-4084-948d-b9b73b245cee");
    private static final AttributeModifier SPEED_BOOTS_MODIFIER = new AttributeModifier(SPEED_BOOTS_MODIFIER_UUID, "Boots speed boost", 0.3D, AttributeModifier.Operation.MULTIPLY_TOTAL);

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
    public static void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
            PlayerData.get(serverPlayer).ifPresent(data -> {
                NetworkHandler.NETWORK.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ClientPacketSetBackPack(data.getBackPack()));
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        //isWasDeath is true if this is a clone because the player died
        //if isWasDeath is false, the player is cloned to another dimension and the contents need to be passed over too

        Player newPlayer = event.getPlayer();
        Player original = event.getOriginal();

        original.reviveCaps();
        PlayerData.get(original).ifPresent(originalData -> {
            boolean loggedIn = originalData.hasLoggedInBefore;
            ItemStack stack = originalData.getBackPack();

            PlayerData.get(newPlayer).ifPresent(newData -> {
                newData.hasLoggedInBefore = loggedIn;
                if (newPlayer.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) && event.isWasDeath() || !event.isWasDeath()) {
                    newData.addBackpack(stack);
                } else {
                    ItemEntity drop = new ItemEntity(original.level, original.getX(), original.getY() + 1, original.getZ(), stack);
                    original.level.addFreshEntity(drop);
                }
            });
        });
        original.invalidateCaps();
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
            PlayerData.get(serverPlayer).ifPresent(data -> {
                NetworkHandler.NETWORK.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ClientPacketSetBackPack(data.getBackPack()));
                if (!data.hasLoggedInBefore) {
                    ItemStack[] hiker = new ItemStack[]{new ItemStack(AritsuItems.HIKER_ARMOR_HELMET.get()), new ItemStack(AritsuItems.HIKER_ARMOR_CHEST.get()), new ItemStack(AritsuItems.HIKER_ARMOR_LEGS.get()), new ItemStack(AritsuItems.HIKER_ARMOR_BOOTS.get())};
                    for (ItemStack armorStack : hiker) {
                        if (armorStack.getItem() instanceof ArmorItem armorItem) {
                            int index = armorItem.getSlot().getIndex();
                            if (serverPlayer.getInventory().armor.get(index).isEmpty())
                                serverPlayer.getInventory().armor.set(index, armorStack);
                            else
                                serverPlayer.getInventory().add(armorStack);
                        }
                    }
                    data.hasLoggedInBefore = true;
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
    public static void onLivingUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            if (!player.level.isClientSide) {
                if (player.getInventory().getArmor(0).getItem() instanceof TravelerArmorItem armor && armor.getSlot() == EquipmentSlot.FEET) {
                    if (!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(SPEED_BOOTS_MODIFIER))
                        player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(SPEED_BOOTS_MODIFIER);
                } else if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(SPEED_BOOTS_MODIFIER)) {
                    player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_BOOTS_MODIFIER);

                }
            }
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

    @SubscribeEvent
    public static void playerUpdate(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        //moved update tick to player data so it's player specific
        PlayerData.get(player).ifPresent(data -> {
            if (player.getEffect(AritsuEffects.ENERGIZED.get()) != null) {
                //update timer only if the player has the effect.
                data.customEffectTick++;
                if (data.customEffectTick % 50 == 0)
                    player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
            } else//reset the tick to 0 if the player doesnt have the effect to prevent the number ramping up throughout play time
                data.customEffectTick = 0;
        });
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

    @SubscribeEvent
    public static void playerAttackEntityEvent(AttackEntityEvent event) {
        if (event.getTarget() instanceof LivingEntity) {
            Player player = event.getPlayer();
            if (player.getItemBySlot(EquipmentSlot.CHEST).is(AritsuItems.BEAR_CHESTPLATE.get()))
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.getAdvancements().award(player.getServer().getAdvancements().getAdvancement(new ResourceLocation(AritsuMod.MODID, "camping/bear_chestplate")), "bear_chestplate");
                }
        }
    }
}
