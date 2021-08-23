package net.aritsu.screen.common;

import net.aritsu.blockentity.BackPackBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class BackPackInventory extends ItemStackHandler {

    private final BackPackBlockEntity be;
    public BackPackInventory(BackPackBlockEntity be) {
        //9craft slots + 1output slot,<no yes> 1 ender chest, 1sleeping bag, 1 tent, 6 extra slots, 1 flask
        super(10);
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
        be.setChanged();
    }
}
