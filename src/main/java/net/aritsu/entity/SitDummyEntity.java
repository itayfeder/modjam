package net.aritsu.entity;

import net.aritsu.block.LogSeatBlock;
import net.aritsu.registry.AritsuEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class SitDummyEntity extends Entity {

    private final BlockPos logPos;

    public SitDummyEntity(EntityType type, Level level) {
        super(type, level);
        logPos = BlockPos.ZERO;
    }

    public SitDummyEntity(Level level, BlockPos pos) {
        super(AritsuEntities.SIT_DUMMY.get(), level);
        this.logPos = pos;
        this.setPos(pos.getX() + 0.5, pos.getY() + 0.35, pos.getZ() + 0.5);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    /*ridetick is only called when the entity is a rider, and not a vehicle. we use the general tick update method instead.*/
    @Override
    public void tick() {
        super.tick();
        //passenger (player) gets set imediatly on spawn.
        //so when this is empty, the player has unmounted.
        if (this.getPassengers().isEmpty()) {
            this.kill(); //remvoe this entity
            //set log block to unoccupied so we can spawn a new entity and sit back down
            if (this.level.getBlockState(logPos).getBlock() instanceof LogSeatBlock) {
                level.setBlock(logPos, level.getBlockState(logPos).setValue(LogSeatBlock.OCCUPIED, false), 3);
            }
        }
    }
}
