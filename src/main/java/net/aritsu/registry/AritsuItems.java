package net.aritsu.registry;

import net.aritsu.mod.AritsuMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AritsuItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AritsuMod.MODID);

    public static final RegistryObject<Item> OAK_LOG_SEAT = ITEMS.register("oak_log_seat",
            () -> new BlockItem(AritsuBlocks.OAK_LOG_SEAT.get(), (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> SPRUCE_LOG_SEAT = ITEMS.register("spruce_log_seat",
            () -> new BlockItem(AritsuBlocks.SPRUCE_LOG_SEAT.get(), (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> BIRCH_LOG_SEAT = ITEMS.register("birch_log_seat",
            () -> new BlockItem(AritsuBlocks.BIRCH_LOG_SEAT.get(), (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> JUNGLE_LOG_SEAT = ITEMS.register("jungle_log_seat",
            () -> new BlockItem(AritsuBlocks.JUNGLE_LOG_SEAT.get(), (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> ACACIA_LOG_SEAT = ITEMS.register("acacia_log_seat",
            () -> new BlockItem(AritsuBlocks.ACACIA_LOG_SEAT.get(), (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> DARK_OAK_LOG_SEAT = ITEMS.register("dark_oak_log_seat",
            () -> new BlockItem(AritsuBlocks.DARK_OAK_LOG_SEAT.get(), (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS)));



}
