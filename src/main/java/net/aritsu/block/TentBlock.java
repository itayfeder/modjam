package net.aritsu.block;

import net.aritsu.blockentity.TentBlockEntity;
import net.aritsu.item.SleepingBagItem;
import net.aritsu.screen.common.TentContainer;
import net.aritsu.util.TentUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class TentBlock extends BedBlock {
    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;
    public static final BooleanProperty LANTERN = BooleanProperty.create("lantern_in_tent");
    private static final VoxelShape common = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D);
    private static final VoxelShape base = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.5D, 16.0D);
    private static final VoxelShape shapeNS2 = Block.box(2.0D, 4.0D, 0.0D, 14.0D, 8.0D, 16.0D);
    private static final VoxelShape shapeNS3 = Block.box(4.0D, 8.0D, 0.0D, 12.0D, 12.0D, 16.0D);
    private static final VoxelShape shapeNS4 = Block.box(6.0D, 12.0D, 0.0D, 10.0D, 15.0D, 16.0D);

    private static final VoxelShape shapeEW2 = Block.box(0.0D, 4.0D, 2.0D, 16.0D, 8.0D, 14.0D);
    private static final VoxelShape shapeEW3 = Block.box(0.0D, 8.0D, 4.0D, 16.0D, 12.0D, 12.0D);
    private static final VoxelShape shapeEW4 = Block.box(0.0D, 12.0D, 6.0D, 16.0D, 15.0D, 10.0D);

    private static final VoxelShape NS = Shapes.or(common, shapeNS2, shapeNS3, shapeNS4);
    private static final VoxelShape EW = Shapes.or(common, shapeEW2, shapeEW3, shapeEW4);

    private final DyeColor color;

    public TentBlock(DyeColor color, Properties properties) {
        super(color, properties);
        this.color = color;
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, BedPart.FOOT).setValue(OCCUPIED, false).setValue(LANTERN, false));
    }

    public static ServerLevel getLevel(Player player) {
        return (ServerLevel) player.level;
    }

    private static boolean bedInRange(BlockPos pos, Direction direction, Player player) {
        if (direction == null) return false;
        return isReachableBedBlock(pos, player) || isReachableBedBlock(pos.relative(direction.getOpposite()), player);
    }

    private static boolean isReachableBedBlock(BlockPos pos, Player player) {
        Vec3 vec3 = Vec3.atBottomCenterOf(pos);
        return Math.abs(player.getX() - vec3.x()) <= 3.0D && Math.abs(player.getY() - vec3.y()) <= 2.0D && Math.abs(player.getZ() - vec3.z()) <= 3.0D;
    }

    private static boolean bedBlocked(BlockPos pos, Direction dir, Player player) {
        BlockPos blockpos = pos.above();
        return !freeAt(blockpos, player) || !freeAt(blockpos.relative(dir.getOpposite()), player);
    }

    protected static boolean freeAt(BlockPos pos, Player player) {
        return !player.level.getBlockState(pos).isSuffocating(player.level, pos);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockpos, BlockState state) {
        return new TentBlockEntity(blockpos, state, this.color);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, OCCUPIED, LANTERN);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        //super.fallOn(level, state, pos, entity, fallDistance * 0.25F);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        BlockPos otherpos = blockpos.relative(context.getHorizontalDirection());
        return context.getLevel().getBlockState(otherpos).canBeReplaced(context) ? this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()) : null;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter getter, BlockPos pos) {
        return common;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return base;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {

        switch (state.getValue(FACING)) {
            case NORTH, SOUTH -> {
                return NS;
            }
            case EAST, WEST -> {
                return EW;
            }
            default -> {
                return common;
            }
        }
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public DyeColor getColor() {
        return this.color;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!level.isClientSide()) {
            TentBlockEntity tentBlockEntity = TentUtils.getTentBlockEntityForInventory(pos, level);
            if (tentBlockEntity == null)
                return InteractionResult.FAIL;

            if (player.isCrouching()) {
                MenuConstructor provider = TentContainer.getServerContainerProvider(tentBlockEntity.getInventory(), ContainerLevelAccess.create(level, pos));
                MenuProvider namedProvider = new SimpleMenuProvider(provider, new TranslatableComponent("container.aritsumods.tent"));
                NetworkHooks.openGui((ServerPlayer) player, namedProvider);
                return InteractionResult.SUCCESS;
            } else if (!TentUtils.tentHasSleepingBag(pos, level)) {
                ItemStack heldStack = player.getItemInHand(hand);
                if (heldStack.getItem() instanceof SleepingBagItem) {
                    tentBlockEntity.setSleepingBag(heldStack);
                    if (!player.getAbilities().instabuild) {
//                        heldStack.shrink(1);
                        player.setItemInHand(hand, ItemStack.EMPTY);
                    }
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.FAIL;
            } else {
                if (state.getValue(PART) != BedPart.HEAD) {
                    pos = pos.relative(state.getValue(FACING));
                    state = level.getBlockState(pos);
                    if (!state.is(this)) {
                        return InteractionResult.CONSUME;
                    }
                }

                if (!canSetSpawn(level)) {
                    level.removeBlock(pos, false);
                    BlockPos blockpos = pos.relative(state.getValue(FACING).getOpposite());
                    if (level.getBlockState(blockpos).is(this)) {
                        level.removeBlock(blockpos, false);
                    }

                    level.explode(null, DamageSource.badRespawnPointExplosion(), null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 5.0F, true, Explosion.BlockInteraction.DESTROY);
                    return InteractionResult.SUCCESS;
                } else if (state.getValue(OCCUPIED)) {
                    if (!this.kickVillagerOutOfBed(level, pos)) {
                        player.displayClientMessage(new TranslatableComponent("block.minecraft.bed.occupied"), true);
                    }

                    return InteractionResult.SUCCESS;
                } else {
                    player.startSleepInBed(pos).ifLeft((sleepingPlayer) -> {
                        if (sleepingPlayer != null) {
                            player.displayClientMessage(sleepingPlayer.getMessage(), true);
                        }

                    });
                    return InteractionResult.SUCCESS;
                }

            }
        }


        return InteractionResult.CONSUME;
    }

    private boolean kickVillagerOutOfBed(Level level, BlockPos blockPos) {
        List<Villager> list = level.getEntitiesOfClass(Villager.class, new AABB(blockPos), LivingEntity::isSleeping);
        if (list.isEmpty()) {
            return false;
        } else {
            list.get(0).stopSleeping();
            return true;
        }
    }

    @Override
    public Optional<Vec3> getRespawnPosition(BlockState state, EntityType<?> type, LevelReader world, BlockPos pos, float orientation, @Nullable LivingEntity entity) {
        return Optional.empty();
    }

    @Override
    public boolean isPossibleToRespawnInThis() {
        return false;
    }

    @Override
    public boolean removedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!level.isClientSide()) {
            TentBlockEntity tent = TentUtils.getTentBlockEntityForInventory(pos, level);
            if (tent != null) {
                for (ItemStack drop : tent.getInventory().getAllItems()) {
                    if (!drop.isEmpty())
                        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), drop);
                }
            }
        }
        return super.removedByPlayer(state, level, pos, player, willHarvest, fluid);
    }
}
