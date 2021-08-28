package net.aritsu.block;

import net.aritsu.entity.SitDummyEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class LogSeatBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;
    protected static final VoxelShape SHAPE_NS = Block.box(0.0D, 0.0D, 3.0D, 16.0D, 10.0D, 13.0D);
    protected static final VoxelShape SHAPE_EW = Block.box(3.0D, 0.0D, 0.0D, 13.0D, 10.0D, 16.0D);

    private final WoodType woodType;

    public LogSeatBlock(Properties props, WoodType woodType) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OCCUPIED, false));
        this.woodType = woodType;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        return this.defaultBlockState().setValue(FACING, placeContext.getHorizontalDirection().getOpposite());
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        switch (state.getValue(FACING)) {
            case NORTH:
            case SOUTH:
            default:
                return SHAPE_NS;
            case EAST:
            case WEST:
                return SHAPE_EW;
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockState(pos.above()).isAir()) {
            if (!state.getValue(OCCUPIED)) {
                SitDummyEntity seat = new SitDummyEntity(level, pos);
                level.addFreshEntity(seat);
                player.startRiding(seat);
                level.setBlock(pos, state.setValue(OCCUPIED, true), 3);
                return InteractionResult.SUCCESS;
            } else {
                List<SitDummyEntity> entities = level.getEntitiesOfClass(SitDummyEntity.class, new AABB
                        (pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1));
                if (entities.size() == 1) {
                    player.startRiding(entities.get(0));
                    return InteractionResult.SUCCESS;
                }

                if (entities.size() == 0) {
                    SitDummyEntity seat = new SitDummyEntity(level, pos);
                    level.addFreshEntity(seat);
                    player.startRiding(seat);
                    return InteractionResult.SUCCESS;

                }
            }
        }

        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public void destroy(LevelAccessor accessor, BlockPos pos, BlockState state) {
        List<SitDummyEntity> entities = accessor.getEntitiesOfClass(SitDummyEntity.class, new AABB
                (pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1));
        for (SitDummyEntity entity : entities) {
            entity.kill();
        }
        super.destroy(accessor, pos, state);
    }

    public boolean useShapeForLightOcclusion(BlockState blockState) {
        return true;
    }

    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING, OCCUPIED);
    }

    public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type) {
        return false;
    }

}
