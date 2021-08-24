package net.aritsu.network.client;

import net.aritsu.capability.PlayerData;
import net.aritsu.network.IPacketBase;
import net.aritsu.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSetBackPack implements IPacketBase {

    private ItemStack stack;

    //empty needed for registration
    public PacketSetBackPack() {
    }

    public PacketSetBackPack(ItemStack stack) {
        this.stack = stack;
    }

    public PacketSetBackPack(FriendlyByteBuf buf) {
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
        Player player = Minecraft.getInstance().player;
        PlayerData.get(player).ifPresent(data -> {
            data.addBackpack(stack);
        });
        context.get().setPacketHandled(true);
    }

    @Override
    public void register(int id) {
        NetworkHandler.NETWORK.registerMessage(id, PacketSetBackPack.class, PacketSetBackPack::encode, PacketSetBackPack::new, PacketSetBackPack::handle);
    }
}
