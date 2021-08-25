package net.aritsu.blockentity;

import net.aritsu.registry.AritsuBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class TentBlockEntity extends BlockEntity {
    private DyeColor color;

    private ItemStackHandler inventory = new ItemStackHandler(2);

    public TentBlockEntity(BlockPos pos, BlockState state, DyeColor color) {
        this(pos, state);
        this.color = color;
    }

    public TentBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(AritsuBlockEntities.SLEEPING_BAG.get(), blockPos, blockState);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ItemStack getSleepingBag() {
        return inventory.getStackInSlot(0);
    }

    public ItemStack getLantern() {
        return inventory.getStackInSlot(1);
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.get("inventory") instanceof CompoundTag invTag)
            inventory.deserializeNBT(invTag);

        super.load(tag);

    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.put("inventory", inventory.serializeNBT());
        return super.save(tag);
    }
}
