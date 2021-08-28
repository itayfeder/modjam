package net.aritsu.block;

import net.aritsu.blockentity.BearTrapBlockEntity;
import net.aritsu.registry.AritsuBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BearTrapBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    public static final VoxelShape SHAPE_NS = Block.box(2.0D, 0.0D, 3.0D, 14.0D, 3.0D, 13.0D);
    public static final VoxelShape SHAPE_NS_ON = Block.box(6.0D, 0.0D, 3.0D, 10.0D, 6.0D, 13.0D);
    public static final VoxelShape SHAPE_EW = Block.box(3.0D, 0.0D, 2.0D, 13.0D, 3.0D, 14.0D);
    public static final VoxelShape SHAPE_EW_ON = Block.box(3.0D, 0.0D, 6.0D, 13.0D, 6.0D, 10.0D);

    public BearTrapBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TRIGGERED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_57070_) {
        return this.defaultBlockState().setValue(FACING, p_57070_.getHorizontalDirection().getOpposite());
    }

    public VoxelShape getShape(BlockState state, BlockGetter p_57101_, BlockPos p_57102_, CollisionContext p_57103_) {
        return switch (state.getValue(FACING)) {
            default -> state.getValue(TRIGGERED) ? SHAPE_NS_ON : SHAPE_NS;
            case EAST, WEST -> state.getValue(TRIGGERED) ? SHAPE_EW_ON : SHAPE_EW;
        };
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

    @Override
    public VoxelShape getCollisionShape(BlockState p_60572_, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) {
        return switch (p_60572_.getValue(FACING)) {
            default -> p_60572_.getValue(TRIGGERED) ? SHAPE_NS_ON : SHAPE_NS;
            case EAST, WEST -> p_60572_.getValue(TRIGGERED) ? SHAPE_EW_ON : SHAPE_EW;
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_152759_, BlockState p_152760_) {
        return new BearTrapBlockEntity(p_152759_, p_152760_);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152755_, BlockState p_152756_, BlockEntityType<T> p_152757_) {
        return createTickerHelper(p_152757_, AritsuBlockEntities.BEAR_TRAP.get(), BearTrapBlockEntity::trapTick);

    }

}
