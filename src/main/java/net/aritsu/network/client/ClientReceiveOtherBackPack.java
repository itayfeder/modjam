package net.aritsu.network.client;

import net.aritsu.capability.PlayerData;
import net.aritsu.network.IPacketBase;
import net.aritsu.network.NetworkHandler;
import net.aritsu.util.ClientReferences;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ClientReceiveOtherBackPack implements IPacketBase {

    private UUID other;
    private ItemStack stack;

    public ClientReceiveOtherBackPack() {
    }

    public ClientReceiveOtherBackPack(UUID uuid, ItemStack stack) {
        this.other = uuid;
        this.stack = stack;
    }

    public ClientReceiveOtherBackPack(FriendlyByteBuf buf) {
        decode(buf);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(other);
        buf.writeItemStack(stack, true);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        other = buf.readUUID();
        stack = buf.readItem();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {

        Level level = ClientReferences.getClientLevel();
        Player otherPlayer = level.getPlayerByUUID(other);
        if (otherPlayer != null)
            PlayerData.get(otherPlayer).ifPresent(dataOther -> {
                dataOther.addBackpack(stack);
            });
        context.get().setPacketHandled(true);
    }

    @Override
    public void register(int id) {
        NetworkHandler.NETWORK.registerMessage(id, ClientReceiveOtherBackPack.class, ClientReceiveOtherBackPack::encode, ClientReceiveOtherBackPack::new, ClientReceiveOtherBackPack::handle);
    }
}
