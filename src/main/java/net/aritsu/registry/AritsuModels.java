package net.aritsu.registry;

import net.aritsu.client.renderer.models.GrizzlyBearModel;
import net.aritsu.mod.AritsuMod;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AritsuModels {

    public static ModelLayerLocation BACKPACK_CHEST_MODEL_LOCATION = new ModelLayerLocation(new ResourceLocation("minecraft:player"), "hiker_backpack_chest");
    public static ModelLayerLocation GRIZZLY_BEAR_LOCATION = new ModelLayerLocation(new ResourceLocation("grizzly_bear"), "grizzly_bear");

    public static ModelLayerLocation HEAD_MODEL_LOCATION = new ModelLayerLocation(new ResourceLocation("minecraft:player"), "hiker_head");
    public static ModelLayerLocation CHESTNBOOTS_MODEL_LOCATION = new ModelLayerLocation(new ResourceLocation("minecraft:player"), "hiker_chest");
    public static ModelLayerLocation LEGS_MODEL_LOCATION = new ModelLayerLocation(new ResourceLocation("minecraft:player"), "hiker_legs");

    @SubscribeEvent
    public static void clientSetup(EntityRenderersEvent.RegisterLayerDefinitions event) {

        //hiker armor models
        LayerDefinition head = LayerDefinition.create(PlayerModel.createMesh(new CubeDeformation(0.51f), false), 64, 64);
        LayerDefinition chestNboots = LayerDefinition.create(PlayerModel.createMesh(new CubeDeformation(0.27f), false), 64, 64);
        LayerDefinition legs = LayerDefinition.create(PlayerModel.createMesh(new CubeDeformation(0.26f), false), 64, 64);

        event.registerLayerDefinition(HEAD_MODEL_LOCATION, () -> head);
        event.registerLayerDefinition(CHESTNBOOTS_MODEL_LOCATION, () -> chestNboots);
        event.registerLayerDefinition(LEGS_MODEL_LOCATION, () -> legs);

        event.registerLayerDefinition(GRIZZLY_BEAR_LOCATION, GrizzlyBearModel::createBodyLayer);
    }
}


