package net.aritsu.block;

import com.mojang.datafixers.util.Either;
import net.aritsu.blockentity.SleepingBagBlockEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.core.jmx.Server;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class SleepingBagBlock extends BedBlock {
    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;
    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D);

    private final DyeColor color;

    public SleepingBagBlock(DyeColor p_49454_, Properties p_49455_) {
        super(p_49454_, p_49455_);
        this.color = p_49454_;
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, BedPart.FOOT).setValue(OCCUPIED, false));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos p_152175_, BlockState p_152176_) {
        return new SleepingBagBlockEntity(p_152175_, p_152176_, this.color);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, OCCUPIED);
    }

    @Override
    public void fallOn(Level p_152169_, BlockState p_152170_, BlockPos p_152171_, Entity p_152172_, float p_152173_) {
        super.fallOn(p_152169_, p_152170_, p_152171_, p_152172_, p_152173_ * 0.25F);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_49479_) {
        BlockPos blockpos = p_49479_.getClickedPos();
        BlockPos otherpos = blockpos.relative(p_49479_.getHorizontalDirection());
        return p_49479_.getLevel().getBlockState(otherpos).canBeReplaced(p_49479_) ? this.defaultBlockState().setValue(FACING, p_49479_.getHorizontalDirection()) : null;
    }

    @Override
    public VoxelShape getShape(BlockState p_49547_, BlockGetter p_49548_, BlockPos p_49549_, CollisionContext p_49550_) {
        return SHAPE;
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49545_) {
        return RenderShape.MODEL;
    }

    public DyeColor getColor() {
        return this.color;
    }

    @Override
    public InteractionResult use(BlockState p_49515_, Level p_49516_, BlockPos p_49517_, Player p_49518_, InteractionHand p_49519_, BlockHitResult p_49520_) {
        if (p_49516_.isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            if (p_49515_.getValue(PART) != BedPart.HEAD) {
                p_49517_ = p_49517_.relative(p_49515_.getValue(FACING));
                p_49515_ = p_49516_.getBlockState(p_49517_);
                if (!p_49515_.is(this)) {
                    return InteractionResult.CONSUME;
                }
            }

            if (!canSetSpawn(p_49516_)) {
                p_49516_.removeBlock(p_49517_, false);
                BlockPos blockpos = p_49517_.relative(p_49515_.getValue(FACING).getOpposite());
                if (p_49516_.getBlockState(blockpos).is(this)) {
                    p_49516_.removeBlock(blockpos, false);
                }

                p_49516_.explode((Entity)null, DamageSource.badRespawnPointExplosion(), (ExplosionDamageCalculator)null, (double)p_49517_.getX() + 0.5D, (double)p_49517_.getY() + 0.5D, (double)p_49517_.getZ() + 0.5D, 5.0F, true, Explosion.BlockInteraction.DESTROY);
                return InteractionResult.SUCCESS;
            } else if (p_49515_.getValue(OCCUPIED)) {
                if (!this.kickVillagerOutOfBed(p_49516_, p_49517_)) {
                    p_49518_.displayClientMessage(new TranslatableComponent("block.minecraft.bed.occupied"), true);
                }

                return InteractionResult.SUCCESS;
            } else {
                startSleepInBed(p_49517_, p_49518_).ifLeft((p_49477_) -> {
                    if (p_49477_ != null) {
                        p_49518_.displayClientMessage(p_49477_.getMessage(), true);
                    }

                });
                return InteractionResult.SUCCESS;
            }
        }
    }

    private boolean kickVillagerOutOfBed(Level p_49491_, BlockPos p_49492_) {
        List<Villager> list = p_49491_.getEntitiesOfClass(Villager.class, new AABB(p_49492_), LivingEntity::isSleeping);
        if (list.isEmpty()) {
            return false;
        } else {
            list.get(0).stopSleeping();
            return true;
        }
    }

    public Either<Player.BedSleepingProblem, Unit> startSleepInBed(BlockPos p_9115_, Player player) {
        java.util.Optional<BlockPos> optAt = java.util.Optional.of(p_9115_);
        Player.BedSleepingProblem ret = net.minecraftforge.event.ForgeEventFactory.onPlayerSleepInBed(player, optAt);
        if (ret != null) return Either.left(ret);
        Direction direction = player.level.getBlockState(p_9115_).getValue(HorizontalDirectionalBlock.FACING);
        if (!player.isSleeping() && player.isAlive()) {
            if (!player.level.dimensionType().natural()) {
                return Either.left(Player.BedSleepingProblem.NOT_POSSIBLE_HERE);
            } else if (!bedInRange(p_9115_, direction, player)) {
                return Either.left(Player.BedSleepingProblem.TOO_FAR_AWAY);
            } else if (bedBlocked(p_9115_, direction, player)) {
                return Either.left(Player.BedSleepingProblem.OBSTRUCTED);
            } else {
                if (!net.minecraftforge.event.ForgeEventFactory.fireSleepingTimeCheck(player, optAt)) {
                    return Either.left(Player.BedSleepingProblem.NOT_POSSIBLE_NOW);
                } else {
                    if (!player.isCreative()) {
                        double d0 = 8.0D;
                        double d1 = 5.0D;
                        Vec3 vec3 = Vec3.atBottomCenterOf(p_9115_);
                        List<Monster> list = player.level.getEntitiesOfClass(Monster.class, new AABB(vec3.x() - 8.0D, vec3.y() - 5.0D, vec3.z() - 8.0D, vec3.x() + 8.0D, vec3.y() + 5.0D, vec3.z() + 8.0D), (p_9062_) -> {
                            return p_9062_.isPreventingPlayerRest(player);
                        });
                        if (!list.isEmpty()) {
                            return Either.left(Player.BedSleepingProblem.NOT_SAFE);
                        }
                    }

                    Either<Player.BedSleepingProblem, Unit> either = playerStartSleepInBed(p_9115_, player).ifRight((p_9029_) -> {
                        player.awardStat(Stats.SLEEP_IN_BED);
                        CriteriaTriggers.SLEPT_IN_BED.trigger((ServerPlayer)player);
                    });
                    if (!getLevel(player).canSleepThroughNights()) {
                        player.displayClientMessage(new TranslatableComponent("sleep.not_possible"), true);
                    }

                    ((ServerLevel)player.level).updateSleepingPlayerList();
                    return either;
                }
            }
        } else {
            return Either.left(Player.BedSleepingProblem.OTHER_PROBLEM);
        }
    }

    public Either<Player.BedSleepingProblem, Unit> playerStartSleepInBed(BlockPos p_36203_, Player player) {
        ((LivingEntity)player).startSleeping(p_36203_);
        ObfuscationReflectionHelper.setPrivateValue(Player.class, player, 0, "sleepCounter");
        return Either.right(Unit.INSTANCE);
    }

    @Override
    public Optional<Vec3> getRespawnPosition(BlockState state, EntityType<?> type, LevelReader world, BlockPos pos, float orientation, @Nullable LivingEntity entity) {
        return Optional.empty();
    }

    @Override
    public boolean isPossibleToRespawnInThis() {
        return false;
    }

    public static ServerLevel getLevel(Player player) {
        return (ServerLevel)player.level;
    }

    private static boolean bedInRange(BlockPos p_9117_, Direction p_9118_, Player player) {
        if (p_9118_ == null) return false;
        return isReachableBedBlock(p_9117_, player) || isReachableBedBlock(p_9117_.relative(p_9118_.getOpposite()), player);
    }

    private static boolean isReachableBedBlock(BlockPos p_9223_, Player player) {
        Vec3 vec3 = Vec3.atBottomCenterOf(p_9223_);
        return Math.abs(player.getX() - vec3.x()) <= 3.0D && Math.abs(player.getY() - vec3.y()) <= 2.0D && Math.abs(player.getZ() - vec3.z()) <= 3.0D;
    }

    private static boolean bedBlocked(BlockPos p_9192_, Direction p_9193_, Player player) {
        BlockPos blockpos = p_9192_.above();
        return !freeAt(blockpos, player) || !freeAt(blockpos.relative(p_9193_.getOpposite()), player);
    }

    protected static boolean freeAt(BlockPos p_36351_, Player player) {
        return !player.level.getBlockState(p_36351_).isSuffocating(player.level, p_36351_);
    }
}
