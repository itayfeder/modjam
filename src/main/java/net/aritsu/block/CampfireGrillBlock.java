package net.aritsu.block;

import net.aritsu.blockentity.CampfireGrillBlockEntity;
import net.aritsu.mod.AritsuMod;
import net.aritsu.registry.AritsuBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Optional;

public class CampfireGrillBlock extends BaseEntityBlock {
    protected static final VoxelShape LAYER = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public CampfireGrillBlock(Properties p_49795_) {
        super(p_49795_);
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return LAYER;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level p_51275_, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        BlockEntity blockentity = p_51275_.getBlockEntity(blockPos);
        if (blockentity instanceof CampfireGrillBlockEntity) {
            CampfireGrillBlockEntity campfireblockentity = (CampfireGrillBlockEntity) blockentity;
            ItemStack itemstack = player.getItemInHand(hand);
            Optional<CampfireCookingRecipe> optional = campfireblockentity.getCookableRecipe(itemstack);
            if (optional.isPresent()) {
                if (!p_51275_.isClientSide && campfireblockentity.placeFood(player.getAbilities().instabuild ? itemstack.copy() : itemstack, optional.get().getCookingTime())) {
                    player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                    return InteractionResult.SUCCESS;
                }

                return InteractionResult.CONSUME;
            }
            if (!p_51275_.isClientSide && campfireblockentity.placeKettle(player.getAbilities().instabuild ? itemstack.copy() : itemstack)) {
                player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                ServerPlayer serverPlayer = (ServerPlayer) player;
                serverPlayer.getAdvancements().award(serverPlayer.getServer().getAdvancements().getAdvancement(new ResourceLocation(AritsuMod.MODID, "camping/boil_kettle")), "boil_kettle");
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockPos blockpos = blockPos.below();
        return levelReader.getBlockState(blockpos).getBlock() instanceof CampfireBlock;
    }

    @Override
    public BlockState updateShape(BlockState fromState, Direction direction, BlockState toState, LevelAccessor levelAccessor, BlockPos fromPos, BlockPos toPos) {
        if (!fromState.canSurvive(levelAccessor, fromPos)) {
            return Blocks.AIR.defaultBlockState();
        } else {

            return super.updateShape(fromState, direction, toState, levelAccessor, fromPos, toPos);
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState state, boolean flagf) {
        if (!blockState.is(state.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(blockPos);
            if (blockentity instanceof CampfireGrillBlockEntity) {
                Containers.dropContents(level, blockPos, ((CampfireGrillBlockEntity) blockentity).getItems());
            }

            super.onRemove(blockState, level, blockPos, state, flagf);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CampfireGrillBlockEntity(blockPos, blockState);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return createTickerHelper(blockEntityType, AritsuBlockEntities.CAMPFIRE_GRILL.get(), CampfireGrillBlockEntity::particleTick);
        } else {
            return createTickerHelper(blockEntityType, AritsuBlockEntities.CAMPFIRE_GRILL.get(), CampfireGrillBlockEntity::cookTick);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

}
