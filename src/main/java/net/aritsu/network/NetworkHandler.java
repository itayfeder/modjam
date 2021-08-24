package net.aritsu.network;

import net.aritsu.mod.AritsuMod;
import net.aritsu.network.client.ClientPacketSetBackPack;
import net.aritsu.network.client.ClientReceiveOtherBackPack;
import net.aritsu.network.server.ServerPacketSpawnBackPack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class NetworkHandler {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(AritsuMod.MODID, "aritsu_network"), () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);


    int id = 0;

    public NetworkHandler() {
        //register packets here
        //NETWORK.registerMessage(id++, null);
        new ServerPacketSpawnBackPack().register(id++);
        new ClientPacketSetBackPack().register(id++);
        new ClientReceiveOtherBackPack().register(id++);
    }
}


