package net.aritsu.network.server;

import net.aritsu.capability.PlayerData;
import net.aritsu.network.IPacketBase;
import net.aritsu.network.NetworkHandler;
import net.aritsu.network.client.ClientPacketSetBackPack;
import net.aritsu.network.client.ClientReceiveOtherBackPack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import java.util.function.Supplier;

public class ServerPacketSpawnBackPack implements IPacketBase {

    //empty constructor needed
    public ServerPacketSpawnBackPack() {
    }

    //bytebuffer constructor for decoding
    public ServerPacketSpawnBackPack(FriendlyByteBuf buf) {
        decode(buf);
    }

    private static BlockHitResult getPlayerPOVHitResult(Level level, Player player, ClipContext.Fluid clipContext) {
        float f = player.getXRot();
        float f1 = player.getYRot();
        Vec3 vec3 = player.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = Mth.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -Mth.cos(-f * ((float) Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float) Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = player.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue();
        Vec3 vec31 = vec3.add((double) f6 * d0, (double) f5 * d0, (double) f7 * d0);
        return level.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, clipContext, player));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {

    }

    @Override
    public void decode(FriendlyByteBuf buf) {

    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        Level level = player.level;
        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockPos oneUp = result.getBlockPos().above();
        if (level.getBlockState(oneUp).isAir() && result.getType() == HitResult.Type.BLOCK) {
            PlayerData.get(player).ifPresent(data -> {
                if (data.getBackPack().getItem() instanceof BlockItem blockItem) {
                    blockItem.place(new BlockPlaceContext(level, player, InteractionHand.MAIN_HAND, data.getBackPack(), result));
                    NetworkHandler.NETWORK.send(PacketDistributor.PLAYER.with(() -> player), new ClientPacketSetBackPack(ItemStack.EMPTY));
                    NetworkHandler.NETWORK.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), new ClientReceiveOtherBackPack(player.getUUID(), ItemStack.EMPTY));
                }
            });
        }

        context.get().setPacketHandled(true);
    }

    @Override
    public void register(int id) {
        NetworkHandler.NETWORK.registerMessage(id, ServerPacketSpawnBackPack.class, ServerPacketSpawnBackPack::encode, ServerPacketSpawnBackPack::new, ServerPacketSpawnBackPack::handle);
    }
}
