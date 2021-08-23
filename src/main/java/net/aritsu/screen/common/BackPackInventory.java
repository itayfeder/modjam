package net.aritsu.screen.common;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class BackPackInventory extends ItemStackHandler {

    public BackPackInventory() {
        //9craft slots + 1output slot,<no yes> 1 ender chest, 1sleeping bag, 1 tent, 6 extra slots, 1 flask
        super(10);
    }


    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        return true;
    }
}
