package net.aritsu.blockentity;

import net.aritsu.registry.AritsuBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BackPackBlockEntity extends BlockEntity {

    public BackPackBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(AritsuBlockEntities.BACKPACK_BE.get(), blockPos, blockState);
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
    public CompoundTag save(CompoundTag p_58888_) {
        return super.save(p_58888_);
    }

    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
    }
}
