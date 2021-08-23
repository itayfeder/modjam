package net.aritsu.block;

import net.aritsu.blockentity.BackPackBlockEntity;
import net.aritsu.registry.AritsuItems;
import net.aritsu.screen.common.BackPackContainer;
import net.aritsu.util.BagTag;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;

public class BackPackBlock extends BaseEntityBlock {

    public BackPackBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player instanceof ServerPlayer serverPlayer && level.getBlockEntity(blockPos) instanceof BackPackBlockEntity backpack) {
            MenuConstructor provider = BackPackContainer.getServerContainerProvider(backpack.getBackpackinventory());
            MenuProvider namedProvider = new SimpleMenuProvider(provider, new TranslatableComponent("container.aritsumods.backpack"));
            NetworkHooks.openGui(serverPlayer, namedProvider);
            return InteractionResult.SUCCESS;
        }
        //client reaches here.
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BackPackBlockEntity(pos, state);
    }

    @Override
    @Deprecated
    public void onRemove(BlockState toState, Level level, BlockPos pos, BlockState fromState, boolean flag) {

        if (!toState.is(fromState.getBlock()) && !level.isClientSide()) {
            if (level.getBlockEntity(pos) instanceof BackPackBlockEntity pack) {
                ItemStack backPack = new ItemStack(AritsuItems.BACKPACK.get());
                backPack.getOrCreateTag().put(BagTag.allItems, pack.getBackpackinventory().serializeNBT());
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), backPack);
            }
            super.onRemove(toState, level, pos, fromState, flag);
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(level, pos, state, entity, stack);
        if (stack.hasTag() && stack.getTag() != null && stack.getTag().contains(BagTag.allItems)) {
            if (stack.getTag().get(BagTag.allItems) instanceof CompoundTag itemsTag)
                if (level.getBlockEntity(pos) instanceof BackPackBlockEntity be) {
                    be.getBackpackinventory().deserializeNBT(itemsTag);
                }
        }
    }
}
