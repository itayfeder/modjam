package net.aritsu.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SleepingBagItem extends BlockItem {
    public SleepingBagItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    protected boolean placeBlock(BlockPlaceContext blockPlaceContext, BlockState blockState) {
        return blockPlaceContext.getLevel().setBlock(blockPlaceContext.getClickedPos(), blockState, 26);
    }
}