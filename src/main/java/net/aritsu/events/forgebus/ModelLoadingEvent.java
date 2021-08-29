package net.aritsu.events.forgebus;

import net.aritsu.mod.AritsuMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModelLoadingEvent {

    public static final ResourceLocation HEADLIGHT = new ResourceLocation(AritsuMod.MODID, "headlight_model");

    @SubscribeEvent
    public static void registerItemModel(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(HEADLIGHT);
    }
}