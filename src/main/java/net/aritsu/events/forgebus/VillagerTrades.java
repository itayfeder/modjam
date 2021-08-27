package net.aritsu.events.forgebus;

import net.aritsu.mod.AritsuMod;
import net.aritsu.registry.AritsuItems;
import net.aritsu.registry.AritsuVillagers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VillagerTrades {
    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() == AritsuVillagers.CAMPER.get()) {
            event.getTrades().get(1).add(new BasicTrade(5, Items.CAMPFIRE.getDefaultInstance(), 8, 3, 0.15F));
            event.getTrades().get(1).add(new BasicTrade(12, AritsuItems.BACKPACK.get().getDefaultInstance(), 2, 5, 0.15F));
            event.getTrades().get(1).add(new BasicTrade(new ItemStack(Items.COAL, 13), ItemStack.EMPTY, Items.EMERALD.getDefaultInstance(), 13, 1, 0.15F));
            event.getTrades().get(1).add(new BasicTrade(new ItemStack(Items.CHARCOAL, 13), ItemStack.EMPTY, Items.EMERALD.getDefaultInstance(), 13, 1, 0.15F));

            event.getTrades().get(2).add(new BasicTrade(8, AritsuItems.WHITE_TENT.get().getDefaultInstance(), 4, 7, 0.225F));
            event.getTrades().get(2).add(new BasicTrade(8, AritsuItems.ORANGE_TENT.get().getDefaultInstance(), 4, 7, 0.225F));
            event.getTrades().get(2).add(new BasicTrade(8, AritsuItems.MAGENTA_TENT.get().getDefaultInstance(), 4, 7, 0.225F));
            event.getTrades().get(2).add(new BasicTrade(8, AritsuItems.LIGHT_BLUE_TENT.get().getDefaultInstance(), 4, 7, 0.225F));
            event.getTrades().get(2).add(new BasicTrade(8, AritsuItems.YELLOW_TENT.get().getDefaultInstance(), 4, 7, 0.225F));
            event.getTrades().get(2).add(new BasicTrade(8, AritsuItems.LIME_TENT.get().getDefaultInstance(), 4, 7, 0.225F));
            event.getTrades().get(2).add(new BasicTrade(8, AritsuItems.PINK_TENT.get().getDefaultInstance(), 4, 7, 0.225F));
            event.getTrades().get(2).add(new BasicTrade(8, AritsuItems.GRAY_TENT.get().getDefaultInstance(), 4, 7, 0.225F));
            event.getTrades().get(2).add(new BasicTrade(8, AritsuItems.LIGHT_GRAY_TENT.get().getDefaultInstance(), 4, 7, 0.225F));
            event.getTrades().get(2).add(new BasicTrade(8, AritsuItems.CYAN_TENT.get().getDefaultInstance(), 4, 7, 0.225F));
            event.getTrades().get(2).add(new BasicTrade(8, AritsuItems.PURPLE_TENT.get().getDefaultInstance(), 4, 7, 0.225F));
            event.getTrades().get(2).add(new BasicTrade(8, AritsuItems.BLUE_TENT.get().getDefaultInstance(), 4, 7, 0.225F));
            event.getTrades().get(2).add(new BasicTrade(8, AritsuItems.BROWN_TENT.get().getDefaultInstance(), 4, 7, 0.225F));
            event.getTrades().get(2).add(new BasicTrade(8, AritsuItems.GREEN_TENT.get().getDefaultInstance(), 4, 7, 0.225F));
            event.getTrades().get(2).add(new BasicTrade(8, AritsuItems.RED_TENT.get().getDefaultInstance(), 4, 7, 0.225F));
            event.getTrades().get(2).add(new BasicTrade(8, AritsuItems.BLACK_TENT.get().getDefaultInstance(), 4, 7, 0.225F));

            event.getTrades().get(3).add(new BasicTrade(13, new ItemStack(AritsuItems.BAIT.get(), (14 + new Random().nextInt(7))), 8, 9, 0.225F));
            event.getTrades().get(3).add(new BasicTrade(13, new ItemStack(AritsuItems.MARSHMALLOW.get(), (7 + new Random().nextInt(4))), 8, 9, 0.225F));
            event.getTrades().get(3).add(new BasicTrade(13, new ItemStack(AritsuItems.BLUEBERRY.get(), 3), 8, 9, 0.225F));
            event.getTrades().get(3).add(new BasicTrade(13, new ItemStack(AritsuItems.COFFEE_BERRY.get(), 3), 8, 9, 0.225F));

            event.getTrades().get(4).add(new BasicTrade(17, AritsuItems.FLASK.get().getDefaultInstance(), 2, 11, 0.3F));
            event.getTrades().get(4).add(new BasicTrade(17, AritsuItems.BEAR_TRAP.get().getDefaultInstance(), 2, 11, 0.3F));
            event.getTrades().get(4).add(new BasicTrade(17, AritsuItems.CAMPFIRE_GRILL.get().getDefaultInstance(), 2, 11, 0.3F));
        }
    }
}
