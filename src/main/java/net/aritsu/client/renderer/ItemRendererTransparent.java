package net.aritsu.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;

import java.util.List;

public class ItemRendererTransparent extends ItemRenderer {

    public ItemRendererTransparent(TextureManager textureManager, ModelManager modelManager, ItemColors itemColors, BlockEntityWithoutLevelRenderer bewlr) {
        super(textureManager, modelManager, itemColors, bewlr);
    }

    @Override
    public void renderQuadList(PoseStack poseStack, VertexConsumer consumer, List<BakedQuad> quads, ItemStack stack, int light, int overlay) {
        for (BakedQuad quad : quads) {
            consumer.putBulkData(poseStack.last(), quad, 0.5f, 0.5f, 0.5f, 0.3f, light, overlay, false);
        }
    }

    @Override
    public void render(ItemStack stack, ItemTransforms.TransformType transformType, boolean b, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay, BakedModel model) {

        if (!stack.isEmpty()) {
            poseStack.pushPose();
            boolean flag = transformType == ItemTransforms.TransformType.GUI || transformType == ItemTransforms.TransformType.GROUND
                    || transformType == ItemTransforms.TransformType.FIXED;

            model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(poseStack, model, transformType, b);
            poseStack.translate(-0.5D, -0.5D, -0.5D);

            if (!model.isCustomRenderer() && (stack.getItem() != Items.TRIDENT || flag)) {
                boolean flag1 = true;
                if (transformType != ItemTransforms.TransformType.GUI && !transformType.firstPerson() && stack.getItem() instanceof BlockItem) {
                    Block block = ((BlockItem) stack.getItem()).getBlock();
                    flag1 = !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
                }
                RenderType rendertype = ItemBlockRenderTypes.getRenderType(stack, flag1);
                VertexConsumer vertexconsumer;

                if (flag1) {
                    vertexconsumer = getFoilBufferDirect(multiBufferSource, rendertype, true, stack.hasFoil());
                } else {
                    vertexconsumer = getFoilBuffer(multiBufferSource, rendertype, true, stack.hasFoil());
                }

                this.renderModelLists(model, stack, light, overlay, poseStack, vertexconsumer);
            }

            poseStack.popPose();
        }
    }
}
