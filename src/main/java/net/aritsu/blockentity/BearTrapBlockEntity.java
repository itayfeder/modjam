package net.aritsu.blockentity;

import net.aritsu.block.BearTrapBlock;
import net.aritsu.registry.AritsuBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.Random;

public class BearTrapBlockEntity extends BlockEntity {
    public BearTrapBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(AritsuBlockEntities.BEAR_TRAP.get(), p_155229_, p_155230_);
    }

    public static void trapTick(Level level, BlockPos pos, BlockState state, BearTrapBlockEntity be) {
        AABB aabb = getShape(state, pos);

        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb);
        for (LivingEntity entity : entities) {
            entity.makeStuckInBlock(state, new Vec3((double)0.05F, 0.00D, (double)0.05F));
            if (!state.getValue(BearTrapBlock.TRIGGERED)) {
                if (!level.isClientSide) {
                    entity.hurt(DamageSource.GENERIC, 8.0F);
                    level.setBlock(pos, state.setValue(BearTrapBlock.TRIGGERED, true), 3);
                } else {
                    Random rnd = new Random();
                    level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                    for (int i = 0; i < 3; i++) {
                        double randX = 0.5 + (0.3125 - 0) * rnd.nextDouble() * (rnd.nextBoolean() ? 1 : -1);
                        double randZ = 0.5 + (0.3125 - 0) * rnd.nextDouble() * (rnd.nextBoolean() ? 1 : -1);
                        level.addParticle(ParticleTypes.CRIT,pos.getX() + randX, pos.getY() + 0.1875, pos.getZ() + randZ, 0, 0, 0);
                    }
                }
            }
        }
    }

    public static AABB getShape(BlockState state, BlockPos pos) {
        switch ((Direction) state.getValue(BearTrapBlock.FACING)) {
            case NORTH:
            case SOUTH:
            default:
                if (!state.getValue(BearTrapBlock.TRIGGERED))
                    return new AABB(pos.getX() + 0.125, pos.getY(), pos.getZ() + 0.1875,
                            pos.getX() + 0.875, pos.getY() + 0.3125, pos.getZ() + 0.8125);
                else return new AABB(pos.getX() + 0.375, pos.getY(), pos.getZ() + 0.1875,
                        pos.getX() + 0.625, pos.getY() + 0.375, pos.getZ() + 0.8125);
            case EAST:
            case WEST:
                if (!state.getValue(BearTrapBlock.TRIGGERED)) return new AABB(pos.getX() + 0.1875, pos.getY(), pos.getZ() + 0.125,
                            pos.getX() + 0.8125, pos.getY() + 0.3125, pos.getZ() + 0.875);
                else return new AABB(pos.getX() + 0.1875, pos.getY(), pos.getZ() + 0.375,
                        pos.getX() + 0.8125, pos.getY() + 0.375, pos.getZ() + 0.625);
        }
    }
}
