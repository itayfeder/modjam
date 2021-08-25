package net.aritsu.blockentity;

import net.aritsu.registry.AritsuBlockEntities;
import net.aritsu.screen.common.TentInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class TentBlockEntity extends BlockEntity {

    public static final int SLEEPINGBAG = 0;
    public static final int LANTERN = 1;
    private final TentInventory tentInventory = new TentInventory(this);

    public TentBlockEntity(BlockPos pos, BlockState state, DyeColor color) {
        this(pos, state);
    }

    public TentBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(AritsuBlockEntities.TENT_BE.get(), blockPos, blockState);
    }

    public TentInventory getInventory() {
        return tentInventory;
    }

    public ItemStack getSleepingBag() {
        return getInventory().getStackInSlot(SLEEPINGBAG);
    }

    public void setSleepingBag(ItemStack stack) {
        getInventory().setStackInSlot(SLEEPINGBAG, stack);

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

    @Override
    public CompoundTag save(CompoundTag tag) {

        tag.put("inventory_tent", getInventory().serializeNBT());
        return super.save(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        CompoundTag items = tag.getCompound("inventory_tent");
        getInventory().deserializeNBT(items);
        super.load(tag);
    }


    //called in TentInventory onSlotChanged
    public void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }
}
