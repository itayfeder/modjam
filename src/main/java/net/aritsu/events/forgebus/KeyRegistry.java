package net.aritsu.events.forgebus;

import net.aritsu.mod.AritsuMod;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = AritsuMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyRegistry {

    public static KeyMapping keybackpack;

    @SubscribeEvent
    public static void launchClientEvent(FMLClientSetupEvent event) {
        new KeyRegistry().registerKey();
    }

    public void registerKey() {

        keybackpack = new KeyMapping("Unequip BackPack", GLFW.GLFW_KEY_B, "unequip backpack");
        ClientRegistry.registerKeyBinding(keybackpack);
    }

}
