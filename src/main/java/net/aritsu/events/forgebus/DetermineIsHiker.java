package net.aritsu.events.forgebus;

import net.aritsu.capability.PlayerData;
import net.aritsu.item.HikerArmorItem;
import net.aritsu.mod.AritsuMod;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DetermineIsHiker {

    @SubscribeEvent
    public static void equipmentChangedEvent(LivingEquipmentChangeEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            if (!player.level.isClientSide()) {
                PlayerData.get(player).ifPresent(data -> {
                    int armorCount = 0;
                    for (ItemStack stack : player.getArmorSlots()) {
                        if (stack.isEmpty()) {
                            return;
                        }
                        if (stack.getItem() instanceof HikerArmorItem)
                            armorCount++;
                    }
                    data.isHiker = armorCount == 4;
                });

            }
        }
    }
}
