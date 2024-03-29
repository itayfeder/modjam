package net.aritsu.screen.common;

import net.aritsu.item.FlaskItem;
import net.aritsu.item.SleepingBagItem;
import net.aritsu.item.TentItem;
import net.aritsu.registry.AritsuContainers;
import net.aritsu.screen.slots.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;

public class BackPackContainer extends AbstractContainerMenu {

    /*******************************************************/

    final ItemStackHandler backpackInventory;

    public BackPackContainer(int id, Inventory playerInventory, ItemStackHandler inventory) {
        super(AritsuContainers.BACKPACK_CONTAINER_TYPE.get(), id);
        backpackInventory = inventory;

        this.addSlot(new FlaskSlot(inventory, 0, 44, 20));
        this.addSlot(new EnderSlot(inventory, 1, 62, 20));

        this.addSlot(new SleepingBagSlot(inventory, 2, 44, 38));
        this.addSlot(new TentSlot(inventory, 3, 62, 38));

        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 3; ++x) {
                this.addSlot(new BackPackSlot(inventory, 1 + 3 * (1 + y) + x, 80 + x * 18, 20 + y * 18));
            }
        }

        ///////////////////////ADD PLAYER INVENTORY///////////////////////////////
        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 127));
        }

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + (y + 1) * 9, 8 + x * 18, 69 + y * 18));
            }
        }
    }

    /********************** FACTORIES **********************/
    public static BackPackContainer registerClientContainer(int id, Inventory playerinventory) {
        return new BackPackContainer(id, playerinventory, new ItemStackHandler(10));
    }

    /**
     * Get the server container provider for NetworkHooks.openGui
     */
    public static MenuConstructor getServerContainerProvider(ItemStackHandler stackHandler) {

        return (id, playerInventory, serverPlayer) -> new BackPackContainer(id, playerInventory, stackHandler);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotnumber) {
        ItemStack copy = ItemStack.EMPTY;
        Slot clicked = this.getSlot(slotnumber);
        if (clicked != null && clicked.hasItem()) {
            ItemStack slotItem = clicked.getItem();
            copy = slotItem.copy();

            //shift clicked inside backpack
            if (slotnumber < backpackInventory.getSlots()) {
                //move to player inventory
                if (!this.moveItemStackTo(slotItem, this.slots.size() - 9, this.slots.size(), false)) {
                    if (!this.moveItemStackTo(slotItem, backpackInventory.getSlots(), this.slots.size() - 9, false)) {
                        return ItemStack.EMPTY;
                    }
                    return ItemStack.EMPTY;
                }
            } else {
                //check for specific item
                if (slotItem.getItem() instanceof FlaskItem) {
                    //move to specific item slot
                    if (!this.moveItemStackTo(slotItem, 0, 1, false)) {
                        return switchBetweenPlayerInv(slotnumber, slotItem);
                    }
                } else if (slotItem.getItem().equals(Items.ENDER_CHEST)) {
                    if (!this.moveItemStackTo(slotItem, 1, 2, false)) {
                        return switchBetweenPlayerInv(slotnumber, slotItem);
                    }
                } else if (slotItem.getItem() instanceof SleepingBagItem) {
                    if (!this.moveItemStackTo(slotItem, 2, 3, false)) {
                        return switchBetweenPlayerInv(slotnumber, slotItem);
                    }
                } else if (slotItem.getItem() instanceof TentItem) {
                    if (!this.moveItemStackTo(slotItem, 3, 4, false)) {
                        return switchBetweenPlayerInv(slotnumber, slotItem);
                    }
                }

                //move to extra slots
                else if (!this.moveItemStackTo(slotItem, 4, backpackInventory.getSlots(), false)) {
                    return switchBetweenPlayerInv(slotnumber, slotItem);
                }
            }
            if (slotItem.isEmpty())
                clicked.set(ItemStack.EMPTY);
            else clicked.setChanged();
        }
        return copy;
    }

    private ItemStack switchBetweenPlayerInv(int slotnumber, ItemStack slotItem) {
        if (slotnumber < this.slots.size() - 9) {
            if (!this.moveItemStackTo(slotItem, this.slots.size() - 9, this.slots.size(), false)) {
                return ItemStack.EMPTY;
            }
        } else if (slotnumber < this.slots.size()) {
            if (!this.moveItemStackTo(slotItem, backpackInventory.getSlots(), this.slots.size() - 9, false)) {
                return ItemStack.EMPTY;
            }
        }
        return ItemStack.EMPTY;
    }
}