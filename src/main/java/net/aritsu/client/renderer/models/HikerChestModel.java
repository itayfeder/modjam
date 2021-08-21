package net.aritsu.client.renderer.models;

import net.aritsu.mod.AritsuMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HikerChestModel {

    public static ModelLayerLocation CHEST_MODEL_LOCATION = new ModelLayerLocation(new ResourceLocation("minecraft:player"), "fashion_head");

    @SubscribeEvent
    public static void clientSetup(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CHEST_MODEL_LOCATION, () -> ChestRenderer.createSingleBodyLayer());
    }
}


