package net.aritsu.screen.common;

import net.aritsu.blockentity.TentBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class TentInventory extends ItemStackHandler {

    private final TentBlockEntity be;

    public TentInventory(TentBlockEntity be) {
        super(6);
        this.be = be;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }

    public NonNullList<ItemStack> getAllItems() {
        return stacks;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        be.markUpdated();
    }
}
