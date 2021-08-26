package net.aritsu.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TentItem extends BlockItem {
    public TentItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    protected boolean placeBlock(BlockPlaceContext placeContext, BlockState state) {
        return placeContext.getLevel().setBlock(placeContext.getClickedPos(), state, 26);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        //TODO use tent on sleepingbag and replace sleepingbag with tent with sleepingbag in
        return super.onItemUseFirst(stack, context);
    }
}