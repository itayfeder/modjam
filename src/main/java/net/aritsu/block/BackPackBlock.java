package net.aritsu.block;

import net.aritsu.blockentity.BackPackBlockEntity;
import net.aritsu.capability.PlayerData;
import net.aritsu.network.NetworkHandler;
import net.aritsu.network.client.ClientPacketSetBackPack;
import net.aritsu.network.client.ClientReceiveOtherBackPack;
import net.aritsu.registry.AritsuItems;
import net.aritsu.screen.common.BackPackContainer;
import net.aritsu.util.BagTag;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import javax.annotation.Nullable;

public class BackPackBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    protected static final VoxelShape LAYER = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 10.0D, 12.0D);

    public BackPackBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));

    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player instanceof ServerPlayer serverPlayer && level.getBlockEntity(blockPos) instanceof BackPackBlockEntity backpack) {
            if (player.isCrouching()) {
                PlayerData.get(player).ifPresent(data -> {
                    ItemStack pack = createBackPackStack(backpack);
                    data.addBackpack(pack);
                    level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                    level.removeBlockEntity(blockPos);
                    NetworkHandler.NETWORK.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ClientPacketSetBackPack(pack));
                    NetworkHandler.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> serverPlayer), new ClientReceiveOtherBackPack(serverPlayer.getUUID(), data.getBackPack()));
                });
            } else {
                MenuConstructor provider = BackPackContainer.getServerContainerProvider(backpack.getBackpackinventory());
                MenuProvider namedProvider = new SimpleMenuProvider(provider, new TranslatableComponent("container.aritsumods.backpack"));
                NetworkHooks.openGui(serverPlayer, namedProvider);
                return InteractionResult.SUCCESS;
            }

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
    public boolean removedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!level.isClientSide()) {
            if (level.getBlockEntity(pos) instanceof BackPackBlockEntity pack) {
                ItemStack backPack = createBackPackStack(pack);
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), backPack);
            }
        }
        return super.removedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    private ItemStack createBackPackStack(BackPackBlockEntity pack) {
        ItemStack backPack = new ItemStack(AritsuItems.BACKPACK.get());
        backPack.getOrCreateTag().put(BagTag.allItems, pack.getBackpackinventory().serializeNBT());
        return backPack;
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

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {
        return LAYER;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDef) {
        stateDef.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        return this.defaultBlockState().setValue(FACING, placeContext.getHorizontalDirection().getOpposite());
    }
}
