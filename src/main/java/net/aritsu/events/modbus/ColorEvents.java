package net.aritsu.events.modbus;

import net.aritsu.mod.AritsuMod;
import net.aritsu.registry.AritsuItems;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ColorEvents {
    @SubscribeEvent
    public static void registerColorHandlers(ColorHandlerEvent.Item event) {
        final ItemColor flaskColorHandler = (anInt, itemStack) -> {
            return itemStack > 0 ? -1 : PotionUtils.getColor(anInt);
        };

        event.getItemColors().register(flaskColorHandler, AritsuItems.FLASK.get());
    }
}
