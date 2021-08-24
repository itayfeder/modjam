package net.aritsu.network.client;

import net.aritsu.capability.PlayerData;
import net.aritsu.network.IPacketBase;
import net.aritsu.network.NetworkHandler;
import net.aritsu.util.ClientReferences;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketSetBackPack implements IPacketBase {

    private ItemStack stack;

    //empty needed for registration
    public ClientPacketSetBackPack() {
    }

    public ClientPacketSetBackPack(ItemStack stack) {
        this.stack = stack;
    }

    public ClientPacketSetBackPack(FriendlyByteBuf buf) {
        decode(buf);
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeItemStack(stack, false);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        stack = buf.readItem();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        Player player = ClientReferences.getClientPlayer();
        PlayerData.get(player).ifPresent(data -> {
            data.addBackpack(stack);
        });
        context.get().setPacketHandled(true);
    }

    @Override
    public void register(int id) {
        NetworkHandler.NETWORK.registerMessage(id, ClientPacketSetBackPack.class, ClientPacketSetBackPack::encode, ClientPacketSetBackPack::new, ClientPacketSetBackPack::handle);
    }
}
