package net.aritsu.mod;

import net.aritsu.registry.*;
import net.aritsu.util.ConfigData;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(AritsuMod.MODID)
public class AritsuMod {

    public static final String MODID = "aritsumods";
    public static final Logger LOGGER = LogManager.getLogger();

    public AritsuMod() {

        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, ConfigData.SERVER_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ConfigData.CLIENT_SPEC);

        AritsuBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        AritsuItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        AritsuEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        AritsuBlockEntities.TILEENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        AritsuContainers.CONTAINER.register(FMLJavaModLoadingContext.get().getModEventBus());
        AritsuEffects.EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());

    }
}
