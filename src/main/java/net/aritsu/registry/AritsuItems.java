package net.aritsu.registry;

import net.aritsu.item.HikerArmorItem;
import net.aritsu.item.ReinforcedFishingRodItem;
import net.aritsu.item.SleepingBagItem;
import net.aritsu.mod.AritsuMod;
import net.aritsu.util.ModTab;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AritsuItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AritsuMod.MODID);

    public static final RegistryObject<Item> OAK_LOG_SEAT = ITEMS.register("oak_log_seat",
            () -> new BlockItem(AritsuBlocks.OAK_LOG_SEAT.get(), (new Item.Properties()).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> SPRUCE_LOG_SEAT = ITEMS.register("spruce_log_seat",
            () -> new BlockItem(AritsuBlocks.SPRUCE_LOG_SEAT.get(), (new Item.Properties()).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> BIRCH_LOG_SEAT = ITEMS.register("birch_log_seat",
            () -> new BlockItem(AritsuBlocks.BIRCH_LOG_SEAT.get(), (new Item.Properties()).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> JUNGLE_LOG_SEAT = ITEMS.register("jungle_log_seat",
            () -> new BlockItem(AritsuBlocks.JUNGLE_LOG_SEAT.get(), (new Item.Properties()).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> ACACIA_LOG_SEAT = ITEMS.register("acacia_log_seat",
            () -> new BlockItem(AritsuBlocks.ACACIA_LOG_SEAT.get(), (new Item.Properties()).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> DARK_OAK_LOG_SEAT = ITEMS.register("dark_oak_log_seat",
            () -> new BlockItem(AritsuBlocks.DARK_OAK_LOG_SEAT.get(), (new Item.Properties()).tab(ModTab.INSTANCE)));

    public static final RegistryObject<Item> WHITE_SLEEPING_BAG = ITEMS.register("white_sleeping_bag",
            () -> new SleepingBagItem(AritsuBlocks.WHITE_SLEEPING_BAG.get(), (new Item.Properties()).stacksTo(1).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> ORANGE_SLEEPING_BAG = ITEMS.register("orange_sleeping_bag",
            () -> new SleepingBagItem(AritsuBlocks.ORANGE_SLEEPING_BAG.get(), (new Item.Properties()).stacksTo(1).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> MAGENTA_SLEEPING_BAG = ITEMS.register("magenta_sleeping_bag",
            () -> new SleepingBagItem(AritsuBlocks.MAGENTA_SLEEPING_BAG.get(), (new Item.Properties()).stacksTo(1).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> LIGHT_BLUE_SLEEPING_BAG = ITEMS.register("light_blue_sleeping_bag",
            () -> new SleepingBagItem(AritsuBlocks.LIGHT_BLUE_SLEEPING_BAG.get(), (new Item.Properties()).stacksTo(1).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> YELLOW_SLEEPING_BAG = ITEMS.register("yellow_sleeping_bag",
            () -> new SleepingBagItem(AritsuBlocks.YELLOW_SLEEPING_BAG.get(), (new Item.Properties()).stacksTo(1).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> LIME_SLEEPING_BAG = ITEMS.register("lime_sleeping_bag",
            () -> new SleepingBagItem(AritsuBlocks.LIME_SLEEPING_BAG.get(), (new Item.Properties()).stacksTo(1).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> PINK_SLEEPING_BAG = ITEMS.register("pink_sleeping_bag",
            () -> new SleepingBagItem(AritsuBlocks.PINK_SLEEPING_BAG.get(), (new Item.Properties()).stacksTo(1).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> GRAY_SLEEPING_BAG = ITEMS.register("gray_sleeping_bag",
            () -> new SleepingBagItem(AritsuBlocks.GRAY_SLEEPING_BAG.get(), (new Item.Properties()).stacksTo(1).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> LIGHT_GRAY_SLEEPING_BAG = ITEMS.register("light_gray_sleeping_bag",
            () -> new SleepingBagItem(AritsuBlocks.LIGHT_GRAY_SLEEPING_BAG.get(), (new Item.Properties()).stacksTo(1).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> CYAN_SLEEPING_BAG = ITEMS.register("cyan_sleeping_bag",
            () -> new SleepingBagItem(AritsuBlocks.CYAN_SLEEPING_BAG.get(), (new Item.Properties()).stacksTo(1).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> PURPLE_SLEEPING_BAG = ITEMS.register("purple_sleeping_bag",
            () -> new SleepingBagItem(AritsuBlocks.PURPLE_SLEEPING_BAG.get(), (new Item.Properties()).stacksTo(1).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> BLUE_SLEEPING_BAG = ITEMS.register("blue_sleeping_bag",
            () -> new SleepingBagItem(AritsuBlocks.BLUE_SLEEPING_BAG.get(), (new Item.Properties()).stacksTo(1).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> BROWN_SLEEPING_BAG = ITEMS.register("brown_sleeping_bag",
            () -> new SleepingBagItem(AritsuBlocks.BROWN_SLEEPING_BAG.get(), (new Item.Properties()).stacksTo(1).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> GREEN_SLEEPING_BAG = ITEMS.register("green_sleeping_bag",
            () -> new SleepingBagItem(AritsuBlocks.GREEN_SLEEPING_BAG.get(), (new Item.Properties()).stacksTo(1).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> RED_SLEEPING_BAG = ITEMS.register("red_sleeping_bag",
            () -> new SleepingBagItem(AritsuBlocks.RED_SLEEPING_BAG.get(), (new Item.Properties()).stacksTo(1).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> BLACK_SLEEPING_BAG = ITEMS.register("black_sleeping_bag",
            () -> new SleepingBagItem(AritsuBlocks.BLACK_SLEEPING_BAG.get(), (new Item.Properties()).stacksTo(1).tab(ModTab.INSTANCE)));

    public static final RegistryObject<Item> HIKER_ARMOR_BOOTS = ITEMS.register("hiker_boots", () -> new HikerArmorItem(EquipmentSlot.FEET));

    public static final RegistryObject<Item> BAIT = ITEMS.register("bait",
            () -> new Item((new Item.Properties()).tab(ModTab.INSTANCE)));
    public static final RegistryObject<Item> REINFORCED_FISHING_ROD = ITEMS.register("reinforced_fishing_rod",
            () -> new ReinforcedFishingRodItem((new Item.Properties()).durability(256).tab(ModTab.INSTANCE)));
}
