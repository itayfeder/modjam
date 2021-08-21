package net.aritsu.events.modbus;

import net.aritsu.client.renderer.layers.BackPackLayer;
import net.aritsu.mod.AritsuMod;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AddLayersToPlayerEvent {

    @SubscribeEvent
    public static void layers(EntityRenderersEvent.AddLayers event) {

        event.getSkins().forEach(skinTypeName -> { //default , slim
            if (event.getSkin(skinTypeName) instanceof PlayerRenderer renderer)
                renderer.addLayer(new BackPackLayer(renderer));
        });
    }
}
