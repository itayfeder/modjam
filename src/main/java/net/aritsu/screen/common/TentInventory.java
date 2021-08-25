package net.aritsu.screen.common;

import net.aritsu.blockentity.TentBlockEntity;
import net.minecraftforge.items.ItemStackHandler;

public class TentInventory extends ItemStackHandler {

    private final TentBlockEntity be;

    public TentInventory(TentBlockEntity be) {
        super(2);
        this.be = be;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        be.setChanged();
    }
}
