package net.aritsu.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class BearTrapBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    protected static final VoxelShape SHAPE_NS = Block.box(2.0D, 0.0D, 3.0D, 14.0D, 3.0D, 13.0D);
    protected static final VoxelShape SHAPE_NS_ON = Block.box(6.0D, 0.0D, 3.0D, 10.0D, 6.0D, 13.0D);
    protected static final VoxelShape SHAPE_EW = Block.box(3.0D, 0.0D, 2.0D, 13.0D, 3.0D, 14.0D);
    protected static final VoxelShape SHAPE_EW_ON = Block.box(3.0D, 0.0D, 6.0D, 13.0D, 6.0D, 10.0D);

    public BearTrapBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TRIGGERED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_57070_) {
        return this.defaultBlockState().setValue(FACING, p_57070_.getHorizontalDirection().getOpposite());
    }

    public VoxelShape getShape(BlockState state, BlockGetter p_57101_, BlockPos p_57102_, CollisionContext p_57103_) {
        switch ((Direction)state.getValue(FACING)) {
            case NORTH:
            case SOUTH:
            default:
                return state.getValue(TRIGGERED) ? SHAPE_NS_ON : SHAPE_NS;
            case EAST:
            case WEST:
                return state.getValue(TRIGGERED) ? SHAPE_EW_ON : SHAPE_EW;
        }
    }

    public boolean useShapeForLightOcclusion(BlockState p_57109_) {
        return true;
    }

    public RenderShape getRenderShape(BlockState p_57098_) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState p_57093_, Rotation p_57094_) {
        return p_57093_.setValue(FACING, p_57094_.rotate(p_57093_.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState p_57090_, Mirror p_57091_) {
        return p_57090_.rotate(p_57091_.getRotation(p_57090_.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(FACING, TRIGGERED);
    }

    public boolean isPathfindable(BlockState p_57078_, BlockGetter p_57079_, BlockPos p_57080_, PathComputationType p_57081_) {
        return true;
    }

    public void entityInside(BlockState p_57270_, Level p_57271_, BlockPos p_57272_, Entity p_57273_) {
        if (p_57273_ instanceof LivingEntity && p_57273_.yOld <= p_57272_.getY() + 0.1875) {
            p_57273_.makeStuckInBlock(p_57270_, new Vec3((double)0.05F, 0.00D, (double)0.05F));
            if (!p_57270_.getValue(TRIGGERED)) {
                if (!p_57271_.isClientSide) {
                    p_57273_.hurt(DamageSource.GENERIC, 8.0F);
                    p_57271_.setBlock(p_57272_, p_57270_.setValue(TRIGGERED, true), 3);
                } else {
                    Random rnd = new Random();
                    p_57271_.playLocalSound(p_57272_.getX(), p_57272_.getY(), p_57272_.getZ(), SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                    for (int i = 0; i < 3; i++) {
                        double randX = 0.5 + (0.3125 - 0) * rnd.nextDouble() * (rnd.nextBoolean() ? 1 : -1);
                        double randZ = 0.5 + (0.3125 - 0) * rnd.nextDouble() * (rnd.nextBoolean() ? 1 : -1);
                        p_57271_.addParticle(ParticleTypes.CRIT,p_57272_.getX() + randX, p_57272_.getY() + 0.1875, p_57272_.getZ() + randZ, 0, 0, 0);
                    }
                }
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_60572_, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) {
        switch ((Direction)p_60572_.getValue(FACING)) {
            case NORTH:
            case SOUTH:
            default:
                return p_60572_.getValue(TRIGGERED) ? SHAPE_NS_ON : SHAPE_NS;
            case EAST:
            case WEST:
                return p_60572_.getValue(TRIGGERED) ? SHAPE_EW_ON : SHAPE_EW;
        }
    }
}
