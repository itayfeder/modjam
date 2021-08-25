package net.aritsu.client.renderer.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.aritsu.block.SleepingBagBlock;
import net.aritsu.block.TentBlock;
import net.aritsu.blockentity.TentBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraftforge.client.model.data.EmptyModelData;

public class TentRenderer implements BlockEntityRenderer<TentBlockEntity> {

    public TentRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(TentBlockEntity tentBlockEntity, float someFloat, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockState tentState = tentBlockEntity.getBlockState();

        BlockPos pos = switch (tentState.getValue(TentBlock.PART)) {
            case HEAD -> switch (tentState.getValue(TentBlock.FACING)) {
                case NORTH -> tentBlockEntity.getBlockPos().south();
                case SOUTH -> tentBlockEntity.getBlockPos().north();
                case WEST -> tentBlockEntity.getBlockPos().east();
                case EAST -> tentBlockEntity.getBlockPos().west();
                default -> null;
            };
            case FOOT -> switch (tentState.getValue(TentBlock.FACING)) {
                case NORTH -> tentBlockEntity.getBlockPos().north();
                case SOUTH -> tentBlockEntity.getBlockPos().south();
                case WEST -> tentBlockEntity.getBlockPos().west();
                case EAST -> tentBlockEntity.getBlockPos().east();
                default -> null;
            };
        };

        if (pos != null) {

            if (tentBlockEntity.getLevel().getBlockEntity(pos) instanceof TentBlockEntity otherTentPart) {
                ItemStack thisTentIsEmpty = tentBlockEntity.getSleepingBag();
                ItemStack otherTentIsEmpty = otherTentPart.getSleepingBag();
                ItemStack sleepingBag = thisTentIsEmpty.isEmpty() ? otherTentIsEmpty : thisTentIsEmpty;

                if (sleepingBag.getItem() instanceof BlockItem item) {
                    BlockState sleepingBagState = item.getBlock().defaultBlockState().setValue(SleepingBagBlock.FACING, tentState.getValue(TentBlock.FACING)).setValue(SleepingBagBlock.PART, tentState.getValue(TentBlock.PART));
                    BakedModel bagModel = blockRenderDispatcher.getBlockModel(sleepingBagState);
                    stack.pushPose();
                    Direction facing = tentState.getValue(TentBlock.FACING);
                    BedPart part = tentState.getValue(TentBlock.PART);
                    float translateX = 0.0f;
                    float scaleX = 1.0f;
                    float translateZ = 0.0f;
                    float scaleZ = 0.0F;
                    boolean opposite = (facing == Direction.WEST || facing == Direction.NORTH);
                    if ((facing == Direction.EAST || facing == Direction.WEST)) {
                        scaleX = 0.875f;
                        if (part == (opposite ? BedPart.HEAD : BedPart.FOOT))
                            translateX = 0.125f;
                        scaleZ = 0.8f;
                        translateZ = 0.1f;
                    }

                    if ((facing == Direction.NORTH || facing == Direction.SOUTH)) {
                        scaleZ = 0.875f;
                        if (part == (opposite ? BedPart.HEAD : BedPart.FOOT))
                            translateZ = 0.125f;
                        scaleX = 0.8f;
                        translateX = 0.1f;
                    }
                    stack.translate(translateX, 0.0f, translateZ);
                    stack.scale(scaleX, 1.0f, scaleZ);
                    blockRenderDispatcher.getModelRenderer().renderModel(stack.last(), buffer.getBuffer(RenderType.entityCutoutNoCull(InventoryMenu.BLOCK_ATLAS)), sleepingBagState, bagModel, 0, 0, 0, light, overlay, EmptyModelData.INSTANCE);
                    stack.popPose();
                }
            }
        }


        BakedModel tentModel = blockRenderDispatcher.getBlockModel(tentState);
        stack.pushPose();
        blockRenderDispatcher.getModelRenderer().renderModel(stack.last(), buffer.getBuffer(RenderType.entityCutoutNoCull(InventoryMenu.BLOCK_ATLAS)), tentBlockEntity.getBlockState(), tentModel, 0, 0, 0, light, overlay, EmptyModelData.INSTANCE);
        stack.popPose();

    }
}
