package net.aritsu.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SleepingBagItem extends BlockItem {
    public SleepingBagItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext placeContext, BlockState state) {
        return placeContext.getLevel().setBlock(placeContext.getClickedPos(), state, 26);
    }
}