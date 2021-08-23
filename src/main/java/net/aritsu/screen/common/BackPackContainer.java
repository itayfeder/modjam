package net.aritsu.screen.common;

import net.aritsu.registry.AritsuContainers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;

public class BackPackContainer extends AbstractContainerMenu {

    /********************** FACTORIES **********************/
    public static BackPackContainer registerClientContainer(int id, Inventory playerinventory){
            return new BackPackContainer(id, playerinventory);
    }

    /** Get the server container provider for NetworkHooks.openGui */
    public static MenuConstructor getServerContainerProvider()
    {

        return (id, playerInventory, serverPlayer) -> new BackPackContainer(id, playerInventory);
    }

    /*******************************************************/

    public BackPackContainer(int id, Inventory playerInventory) {
        super(AritsuContainers.PETBUDDY_CONTAINER_TYPE.get(), id);
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return true;
    }
}