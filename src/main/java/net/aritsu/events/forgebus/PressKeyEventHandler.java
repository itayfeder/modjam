package net.aritsu.events.forgebus;

import net.aritsu.capability.PlayerData;
import net.aritsu.mod.AritsuMod;
import net.aritsu.network.NetworkHandler;
import net.aritsu.network.server.ServerPacketSpawnBackPack;
import net.aritsu.util.ClientReferences;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PressKeyEventHandler {

    @SubscribeEvent
    public static void keyPressed(KeyInputEvent event) {

        if (KeyRegistry.keybackpack.consumeClick()) {
            PlayerData.get(ClientReferences.getClientPlayer()).ifPresent(data -> {
                NetworkHandler.NETWORK.sendToServer(new ServerPacketSpawnBackPack());
                if (!data.getBackPack().isEmpty()) {
                    ClientReferences.getClientPlayer().swing(InteractionHand.MAIN_HAND);
                    ClientReferences.getClientLevel().playSound(ClientReferences.getClientPlayer(), ClientReferences.getClientPlayer().getOnPos(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1, 1);
                }
            });
        }
    }
}
