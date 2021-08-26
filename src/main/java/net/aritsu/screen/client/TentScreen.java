package net.aritsu.screen.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.aritsu.mod.AritsuMod;
import net.aritsu.screen.common.TentContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TentScreen extends AbstractContainerScreen<TentContainer> {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(AritsuMod.MODID, "textures/gui/tent_gui.png");
    private static final ResourceLocation BAG = new ResourceLocation(AritsuMod.MODID, "textures/gui/bag.png");
    private static final ResourceLocation LANTERN = new ResourceLocation(AritsuMod.MODID, "textures/gui/lantern.png");

    public TentScreen(TentContainer tentContainer, Inventory inventory, Component component) {
        super(tentContainer, inventory, component);
        this.imageHeight = 192;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        int centerX = (this.width - this.imageWidth) / 2;
        int centerY = (this.height - this.imageHeight) / 2;
        this.blit(poseStack, centerX, centerY, 0, 0, this.imageWidth, this.imageHeight);

        super.render(poseStack, mouseX, mouseY, partialTicks);

        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float f1, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        for (int slotId = 0; slotId < 2; slotId++)
            if (this.menu.getSlot(slotId).getItem().isEmpty()) {
                RenderSystem.setShaderTexture(0, getSlotTexture(slotId));
                int centerX = (this.width - this.imageWidth) / 2;
                int centerY = (this.height - this.imageHeight) / 2;
                blit(poseStack, menu.getSlot(slotId).x + centerX, menu.getSlot(slotId).y + centerY, 0, 0, 16, 16, 16, 16);
            }
    }

    private ResourceLocation getSlotTexture(int slotID) {
        return slotID == 0 ? BAG : LANTERN;
    }
}
