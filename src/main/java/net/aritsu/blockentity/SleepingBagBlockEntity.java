package net.aritsu.blockentity;

import net.aritsu.registry.AritsuBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SleepingBagBlockEntity extends BlockEntity {
    private DyeColor color;

    public SleepingBagBlockEntity(BlockPos pos, BlockState state, DyeColor color) {
        this(pos, state);
        this.color = color;
    }

    public SleepingBagBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(AritsuBlockEntities.SLEEPING_BAG.get(), blockPos, blockState);
    }
}
