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


    @Override
    public void tick() {
        super.tick();
        if (this.getPassengers().isEmpty()) {
            this.kill();
            if (this.level.getBlockState(logPos).getBlock() instanceof LogSeatBlock) {
                level.setBlock(logPos, level.getBlockState(logPos).setValue(LogSeatBlock.OCCUPIED, false), 3);
            }
        }
    }

    @Override
    public void rideTick() {
        super.rideTick();
        if (this.getPassengers().isEmpty()) {
            this.kill();
            if (this.level.getBlockState(logPos).getBlock() instanceof LogSeatBlock) {
                level.setBlock(logPos, level.getBlockState(logPos).setValue(LogSeatBlock.OCCUPIED, false), 3);
            }
        }
    }
}
