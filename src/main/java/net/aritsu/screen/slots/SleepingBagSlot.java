package net.aritsu.screen.slots;

import net.aritsu.block.SleepingBagBlock;
import net.aritsu.item.SleepingBagItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SleepingBagSlot extends SlotItemHandler {
    public SleepingBagSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return stack.getItem() instanceof SleepingBagItem;
    }
}
