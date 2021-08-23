package net.aritsu.block;

import net.aritsu.blockentity.BackPackBlockEntity;
import net.aritsu.screen.common.BackPackContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuConstructor;
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
        if (player instanceof ServerPlayer serverPlayer) {
            MenuConstructor provider = BackPackContainer.getServerContainerProvider();
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
}
