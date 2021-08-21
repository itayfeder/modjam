package net.aritsu.blockentity;

import net.aritsu.registry.ArtisuBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SleepingBagBlockEntity extends BlockEntity {
    private DyeColor color;

    public SleepingBagBlockEntity(BlockPos p_155229_, BlockState p_155230_, DyeColor color) {
        this(p_155229_, p_155230_);
        this.color = color;
    }

    public SleepingBagBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ArtisuBlockEntities.SLEEPING_BAG.get(), blockPos, blockState);
    }
}
