package net.aritsu.events.modbus;

import net.aritsu.mod.AritsuMod;
import net.aritsu.registry.AritsuItems;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ColorEvents {
    @SubscribeEvent
    public static void registerColorHandlers(ColorHandlerEvent.Item event) {

        registerItemColorHandlers(event.getItemColors());
    }

    private static void registerItemColorHandlers(final ItemColors itemColors) {
        final ItemColor FlaskColorHandler = (p_92699_, p_92700_) -> {
            return p_92700_ > 0 ? -1 : PotionUtils.getColor(p_92699_);
        };

        itemColors.register(FlaskColorHandler, AritsuItems.FLASK.get());
    }
}
