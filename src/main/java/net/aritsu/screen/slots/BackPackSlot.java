package net.aritsu.screen.slots;

import net.aritsu.registry.AritsuItems;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class BackPackSlot extends SlotItemHandler {
    public BackPackSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return !(stack.getItem().equals(AritsuItems.BACKPACK.get()));
    }
}
