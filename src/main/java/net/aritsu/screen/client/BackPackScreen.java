package net.aritsu.screen.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.aritsu.client.renderer.ItemRendererTransparent;
import net.aritsu.screen.common.BackPackContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BackPackScreen extends AbstractContainerScreen<BackPackContainer> {

    //enables rendering itemstacks transpart as a flavor for slot rendering. nice to use for specific slots so people know what to put in them
    private final ItemRendererTransparent renderer;

    public BackPackScreen(BackPackContainer backPackContainer, Inventory inventory, Component component) {
        super(backPackContainer, inventory, component);

        BlockEntityWithoutLevelRenderer blockentitywithoutlevelrenderer = new BlockEntityWithoutLevelRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        renderer = new ItemRendererTransparent(Minecraft.getInstance().textureManager, Minecraft.getInstance().getModelManager(),
                Minecraft.getInstance().getItemColors(), blockentitywithoutlevelrenderer);

    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack p_97787_, float p_97788_, int p_97789_, int p_97790_) {

    }
}
