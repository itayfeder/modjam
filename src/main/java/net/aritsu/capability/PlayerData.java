package net.aritsu.capability;

import net.aritsu.util.BagTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

public class PlayerData {

    private Player player;
    private ItemStackHandler inventoryForbackPack = new ItemStackHandler(1);

    public PlayerData() {

    }

    public static LazyOptional<PlayerData> get(Player player) {

        return player.getCapability(PlayerCapability.CAPABILITY, null);
    }

    public Player getPlayer() {

        return player;
    }

    public void setPlayer(Player newPlayer) {

        this.player = newPlayer;
    }

    public Tag writeData() {
        CompoundTag tag = new CompoundTag();
        tag.put(BagTag.allItems, inventoryForbackPack.serializeNBT());
        return tag;
    }

    public void readData(Tag tag) {
        if (tag instanceof CompoundTag saved) {
            if (saved.contains(BagTag.allItems) && saved.get(BagTag.allItems) instanceof CompoundTag items)
                inventoryForbackPack.deserializeNBT(items);
        }
    }


    public void addBackpack(ItemStack itemStack) {
        inventoryForbackPack.setStackInSlot(0, itemStack);
    }

    public ItemStack getBackPack(){
        return inventoryForbackPack.getStackInSlot(0);
    }
}