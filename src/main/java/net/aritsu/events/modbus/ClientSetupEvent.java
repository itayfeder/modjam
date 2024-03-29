package net.aritsu.events.modbus;

import net.aritsu.client.renderer.EmptyEntityRenderer;
import net.aritsu.client.renderer.GrizzlyBearRenderer;
import net.aritsu.client.renderer.ReinforcedFishingHookRenderer;
import net.aritsu.client.renderer.tile.CampfireGrillRenderer;
import net.aritsu.client.renderer.tile.TentRenderer;
import net.aritsu.item.BlueberryJamItem;
import net.aritsu.item.FlaskItem;
import net.aritsu.item.ReinforcedFishingRodItem;
import net.aritsu.mod.AritsuMod;
import net.aritsu.registry.*;
import net.aritsu.screen.client.BackPackScreen;
import net.aritsu.screen.client.TentScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetupEvent {

    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {

        EntityRenderers.register(AritsuEntities.SIT_DUMMY.get(), EmptyEntityRenderer::new);
        EntityRenderers.register(AritsuEntities.REINFORCED_FISHING_BOBBER.get(), ReinforcedFishingHookRenderer::new);
        EntityRenderers.register(AritsuEntities.GRIZZLY_BEAR.get(), GrizzlyBearRenderer::new);

        MenuScreens.register(AritsuContainers.BACKPACK_CONTAINER_TYPE.get(), BackPackScreen::new);
        MenuScreens.register(AritsuContainers.TENT_CONTAINER_TYPE.get(), TentScreen::new);

        ItemProperties.register(AritsuItems.REINFORCED_FISHING_ROD.get(), new ResourceLocation("cast"), (p_174595_, p_174596_, p_174597_, p_174598_) -> {
            if (p_174597_ == null) {
                return 0.0F;
            } else {
                boolean flag = p_174597_.getMainHandItem() == p_174595_;
                boolean flag1 = p_174597_.getOffhandItem() == p_174595_;
                if (p_174597_.getMainHandItem().getItem() instanceof FishingRodItem) {
                    flag1 = false;
                }

                return (flag || flag1) && p_174597_ instanceof Player && ((Player) p_174597_).fishing != null ? 1.0F : 0.0F;
            }
        });
        ItemProperties.register(AritsuItems.REINFORCED_FISHING_ROD.get(), new ResourceLocation("baited"), (p_174625_, p_174626_, p_174627_, p_174628_) -> {
            return ReinforcedFishingRodItem.getFullnessDisplay(p_174625_);
        });

        ItemProperties.register(AritsuItems.FLASK.get(), new ResourceLocation("filled"), (p_174625_, p_174626_, p_174627_, p_174628_) -> {
            return FlaskItem.getFullnessDisplay(p_174625_);
        });

        ItemProperties.register(AritsuItems.BLUEBERRY_JAM.get(), new ResourceLocation("named"), (p_174625_, p_174626_, p_174627_, p_174628_) -> {
            return BlueberryJamItem.getDisplay(p_174625_);
        });

        ItemBlockRenderTypes.setRenderLayer(AritsuBlocks.CAMPFIRE_GRILL.get(), RenderType.cutout());
        BlockEntityRenderers.register(AritsuBlockEntities.CAMPFIRE_GRILL.get(), CampfireGrillRenderer::new);
        BlockEntityRenderers.register(AritsuBlockEntities.TENT_BE.get(), TentRenderer::new);

        ItemBlockRenderTypes.setRenderLayer(AritsuBlocks.BEAR_TRAP.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(AritsuBlocks.BLUEBERRY_BUSH.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(AritsuBlocks.COFFEE_BUSH.get(), RenderType.cutout());

    }
}
