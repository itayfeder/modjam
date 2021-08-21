package net.aritsu.entity;

import net.aritsu.registry.AritsuEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

public class SitDummyEntity extends Entity {

    public SitDummyEntity(Level p_19871_) {
        super(AritsuEntities.SIT_DUMMY.get(), p_19871_);
    }

    public SitDummyEntity(Level level, BlockPos pos) {
        super(AritsuEntities.SIT_DUMMY.get(), level);
        this.setPos(pos.getX()+0.5,pos.getY()+0.35, pos.getZ()+0.5);
        this.noPhysics = true;
    }

    public SitDummyEntity(EntityType<? extends SitDummyEntity> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Override
    protected void defineSynchedData() { }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) { }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) { }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void rideTick() {
        super.rideTick();
        if (this.getPassengers() == null) {
            this.kill();
        }
    }
}
