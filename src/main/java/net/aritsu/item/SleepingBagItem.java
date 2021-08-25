package net.aritsu.item;

import net.aritsu.block.TentBlock;
import net.aritsu.blockentity.TentBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SleepingBagItem extends BlockItem {
<<<<<<< HEAD
    public SleepingBagItem(Block block, Properties properties) {
        super(block, properties);
    }

    protected boolean placeBlock(BlockPlaceContext placeContext, BlockState state) {
        return placeContext.getLevel().setBlock(placeContext.getClickedPos(), state, 26);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
//        BlockPos pos = context.getClickedPos();
//        Level level = context.getLevel();
//        if (!level.isClientSide())
//            if (level.getBlockEntity(pos) instanceof TentBlockEntity tentBlockEntity)
//                if (TentBlock.tentIsEmpty(tentBlockEntity, level.getBlockState(pos), level, pos)) {
//                    tentBlockEntity.setSleepingBag(stack);
//                    stack.shrink(1);
//                    return InteractionResult.SUCCESS;
//                } else return InteractionResult.CONSUME;
        return super.onItemUseFirst(stack, context);
=======
    public SleepingBagItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    protected boolean placeBlock(BlockPlaceContext blockPlaceContext, BlockState blockState) {
        return blockPlaceContext.getLevel().setBlock(blockPlaceContext.getClickedPos(), blockState, 26);
>>>>>>> origin/master
    }
}