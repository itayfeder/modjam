package net.aritsu.block;

import net.aritsu.blockentity.CampfireGrillBlockEntity;
import net.aritsu.registry.AritsuBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
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

    public VoxelShape getShape(BlockState p_56620_, BlockGetter p_56621_, BlockPos p_56622_, CollisionContext p_56623_) {
        return LAYER;
    }

    public InteractionResult use(BlockState p_51274_, Level p_51275_, BlockPos p_51276_, Player p_51277_, InteractionHand p_51278_, BlockHitResult p_51279_) {
        BlockEntity blockentity = p_51275_.getBlockEntity(p_51276_);
        if (blockentity instanceof CampfireGrillBlockEntity) {
            CampfireGrillBlockEntity campfireblockentity = (CampfireGrillBlockEntity)blockentity;
            ItemStack itemstack = p_51277_.getItemInHand(p_51278_);
            Optional<CampfireCookingRecipe> optional = campfireblockentity.getCookableRecipe(itemstack);
            if (optional.isPresent()) {
                if (!p_51275_.isClientSide && campfireblockentity.placeFood(p_51277_.getAbilities().instabuild ? itemstack.copy() : itemstack, optional.get().getCookingTime())) {
                    p_51277_.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                    return InteractionResult.SUCCESS;
                }

                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    public boolean canSurvive(BlockState p_56109_, LevelReader p_56110_, BlockPos p_56111_) {
        BlockPos blockpos = p_56111_.below();
        return p_56110_.getBlockState(blockpos).getBlock() instanceof CampfireBlock;
    }

    public BlockState updateShape(BlockState p_56113_, Direction p_56114_, BlockState p_56115_, LevelAccessor p_56116_, BlockPos p_56117_, BlockPos p_56118_) {
        if (!p_56113_.canSurvive(p_56116_, p_56117_)) {
            return Blocks.AIR.defaultBlockState();
        } else {

            return super.updateShape(p_56113_, p_56114_, p_56115_, p_56116_, p_56117_, p_56118_);
        }
    }

    public void onRemove(BlockState p_51281_, Level p_51282_, BlockPos p_51283_, BlockState p_51284_, boolean p_51285_) {
        if (!p_51281_.is(p_51284_.getBlock())) {
            BlockEntity blockentity = p_51282_.getBlockEntity(p_51283_);
            if (blockentity instanceof CampfireGrillBlockEntity) {
                Containers.dropContents(p_51282_, p_51283_, ((CampfireGrillBlockEntity)blockentity).getItems());
            }

            super.onRemove(p_51281_, p_51282_, p_51283_, p_51284_, p_51285_);
        }
    }

    public BlockEntity newBlockEntity(BlockPos p_152759_, BlockState p_152760_) {
        return new CampfireGrillBlockEntity(p_152759_, p_152760_);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152755_, BlockState p_152756_, BlockEntityType<T> p_152757_) {
        if (p_152755_.isClientSide) {
            return createTickerHelper(p_152757_, AritsuBlockEntities.CAMPFIRE_GRILL.get(), CampfireGrillBlockEntity::particleTick);
        } else {
            return createTickerHelper(p_152757_, AritsuBlockEntities.CAMPFIRE_GRILL.get(), CampfireGrillBlockEntity::cookTick);
        }
    }

    public RenderShape getRenderShape(BlockState p_49545_) {
        return RenderShape.MODEL;
    }

}
