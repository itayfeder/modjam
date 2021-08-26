package net.aritsu.client.renderer.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.aritsu.blockentity.CampfireGrillBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CampfireBlock;

public class CampfireGrillRenderer implements BlockEntityRenderer<CampfireGrillBlockEntity> {
    private static final float SIZE = 0.375F;

    public CampfireGrillRenderer(BlockEntityRendererProvider.Context p_173602_) {
    }

    public void render(CampfireGrillBlockEntity p_112344_, float p_112345_, PoseStack p_112346_, MultiBufferSource p_112347_, int p_112348_, int p_112349_) {
        if (p_112344_.getLevel().getBlockState(p_112344_.getBlockPos().below()).getBlock() instanceof CampfireBlock) {
            Direction direction = p_112344_.getLevel().getBlockState(p_112344_.getBlockPos().below()).getValue(CampfireBlock.FACING);
            NonNullList<ItemStack> nonnulllist = p_112344_.getItems();
            int i = (int) p_112344_.getBlockPos().asLong();

            for (int j = 0; j < nonnulllist.size() - 1; ++j) {
                ItemStack itemstack = nonnulllist.get(j);
                if (itemstack != ItemStack.EMPTY) {
                    p_112346_.pushPose();
                    p_112346_.translate(0.5D, 0.0234375D, 0.5D);
                    Direction direction1 = Direction.from2DDataValue((j + direction.get2DDataValue()) % 4);
                    float f = -direction1.toYRot();
                    p_112346_.mulPose(Vector3f.YP.rotationDegrees(f));
                    p_112346_.mulPose(Vector3f.XP.rotationDegrees(90.0F));
                    p_112346_.translate(-0.3125D, -0.3125D, 0.0D);
                    p_112346_.scale(0.375F, 0.375F, 0.375F);
                    Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemTransforms.TransformType.FIXED, p_112348_, p_112349_, p_112346_, p_112347_, i + j);
                    p_112346_.popPose();
                }
            }

            ItemStack itemstack = nonnulllist.get(4);
            if (itemstack != ItemStack.EMPTY) {
                p_112346_.pushPose();
                p_112346_.translate(0.45D, 0.30D, 0.5D);
                p_112346_.translate(0.0D, 0.0D, 0.0D);
                p_112346_.scale(0.625F, 0.625F, 0.625F);
                Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemTransforms.TransformType.FIXED, p_112348_, p_112349_, p_112346_, p_112347_, i + 4);
                p_112346_.popPose();
            }

        }


    }
}