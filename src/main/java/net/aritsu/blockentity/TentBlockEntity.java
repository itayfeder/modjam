package net.aritsu.blockentity;

import net.aritsu.registry.AritsuBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class TentBlockEntity extends BlockEntity {

    public static final int SLEEPINGBAG = 0;
    public static final int LANTERN = 1;
    //TentInventory inventory = new TentInventory(this);
    protected NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    ;

    private DyeColor color;

    public TentBlockEntity(BlockPos pos, BlockState state, DyeColor color) {
        this(pos, state);
        this.color = color;
    }

    public TentBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(AritsuBlockEntities.TENT_BE.get(), blockPos, blockState);
    }

    ///////////////// 4 METHODS ABSOLUTELY NEEDED FOR CLIENT/SERVER
    ///////////////// SYNCING/////////////////////

    @Override
    public CompoundTag getUpdateTag() {
        return save(new CompoundTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(getBlockPos(), 0, this.save(new CompoundTag()));
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }
    //*************************************************************


    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }

    public ItemStack getSleepingBag() {
        return inventory.get(SLEEPINGBAG);
    }

    public void setSleepingBag(ItemStack stack) {
        inventory.set(SLEEPINGBAG, stack);
        this.markUpdated();
    }

    public ItemStack getLantern() {
        return inventory.get(1);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {

        ContainerHelper.saveAllItems(tag, inventory);
        return super.save(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        ContainerHelper.loadAllItems(tag, inventory);
        super.load(tag);
    }

    public void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }
}
