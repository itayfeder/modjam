package net.aritsu.registry;

import net.aritsu.block.*;
import net.aritsu.mod.AritsuMod;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AritsuBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AritsuMod.MODID);

    public static final RegistryObject<Block> OAK_LOG_SEAT = BLOCKS.register("oak_log_seat",
            () -> new LogSeatBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD), WoodType.OAK));
    public static final RegistryObject<Block> SPRUCE_LOG_SEAT = BLOCKS.register("spruce_log_seat",
            () -> new LogSeatBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD), WoodType.SPRUCE));
    public static final RegistryObject<Block> BIRCH_LOG_SEAT = BLOCKS.register("birch_log_seat",
            () -> new LogSeatBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.QUARTZ).strength(2.0F).sound(SoundType.WOOD), WoodType.BIRCH));
    public static final RegistryObject<Block> JUNGLE_LOG_SEAT = BLOCKS.register("jungle_log_seat",
            () -> new LogSeatBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD), WoodType.JUNGLE));
    public static final RegistryObject<Block> ACACIA_LOG_SEAT = BLOCKS.register("acacia_log_seat",
            () -> new LogSeatBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.STONE).strength(2.0F).sound(SoundType.WOOD), WoodType.ACACIA));
    public static final RegistryObject<Block> DARK_OAK_LOG_SEAT = BLOCKS.register("dark_oak_log_seat",
            () -> new LogSeatBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD), WoodType.DARK_OAK));

    public static final RegistryObject<Block> WHITE_SLEEPING_BAG = BLOCKS.register("white_sleeping_bag",
            () -> createBed(DyeColor.WHITE));
    public static final RegistryObject<Block> ORANGE_SLEEPING_BAG = BLOCKS.register("orange_sleeping_bag",
            () -> createBed(DyeColor.ORANGE));
    public static final RegistryObject<Block> MAGENTA_SLEEPING_BAG = BLOCKS.register("magenta_sleeping_bag",
            () -> createBed(DyeColor.MAGENTA));
    public static final RegistryObject<Block> LIGHT_BLUE_SLEEPING_BAG = BLOCKS.register("light_blue_sleeping_bag",
            () -> createBed(DyeColor.LIGHT_BLUE));
    public static final RegistryObject<Block> YELLOW_SLEEPING_BAG = BLOCKS.register("yellow_sleeping_bag",
            () -> createBed(DyeColor.YELLOW));
    public static final RegistryObject<Block> LIME_SLEEPING_BAG = BLOCKS.register("lime_sleeping_bag",
            () -> createBed(DyeColor.LIME));
    public static final RegistryObject<Block> PINK_SLEEPING_BAG = BLOCKS.register("pink_sleeping_bag",
            () -> createBed(DyeColor.PINK));
    public static final RegistryObject<Block> GRAY_SLEEPING_BAG = BLOCKS.register("gray_sleeping_bag",
            () -> createBed(DyeColor.GRAY));
    public static final RegistryObject<Block> LIGHT_GRAY_SLEEPING_BAG = BLOCKS.register("light_gray_sleeping_bag",
            () -> createBed(DyeColor.LIGHT_GRAY));
    public static final RegistryObject<Block> CYAN_SLEEPING_BAG = BLOCKS.register("cyan_sleeping_bag",
            () -> createBed(DyeColor.CYAN));
    public static final RegistryObject<Block> PURPLE_SLEEPING_BAG = BLOCKS.register("purple_sleeping_bag",
            () -> createBed(DyeColor.PURPLE));
    public static final RegistryObject<Block> BLUE_SLEEPING_BAG = BLOCKS.register("blue_sleeping_bag",
            () -> createBed(DyeColor.BLUE));
    public static final RegistryObject<Block> BROWN_SLEEPING_BAG = BLOCKS.register("brown_sleeping_bag",
            () -> createBed(DyeColor.BROWN));
    public static final RegistryObject<Block> GREEN_SLEEPING_BAG = BLOCKS.register("green_sleeping_bag",
            () -> createBed(DyeColor.GREEN));
    public static final RegistryObject<Block> RED_SLEEPING_BAG = BLOCKS.register("red_sleeping_bag",
            () -> createBed(DyeColor.RED));
    public static final RegistryObject<Block> BLACK_SLEEPING_BAG = BLOCKS.register("black_sleeping_bag",
            () -> createBed(DyeColor.BLACK));

    public static final RegistryObject<Block> WHITE_TENT = BLOCKS.register("white_tent",
            () -> createTent(DyeColor.WHITE));
    public static final RegistryObject<Block> ORANGE_TENT = BLOCKS.register("orange_tent",
            () -> createTent(DyeColor.ORANGE));
    public static final RegistryObject<Block> MAGENTA_TENT = BLOCKS.register("magenta_tent",
            () -> createTent(DyeColor.MAGENTA));
    public static final RegistryObject<Block> LIGHT_BLUE_TENT = BLOCKS.register("light_blue_tent",
            () -> createTent(DyeColor.LIGHT_BLUE));
    public static final RegistryObject<Block> YELLOW_TENT = BLOCKS.register("yellow_tent",
            () -> createTent(DyeColor.YELLOW));
    public static final RegistryObject<Block> LIME_TENT = BLOCKS.register("lime_tent",
            () -> createTent(DyeColor.LIME));
    public static final RegistryObject<Block> PINK_TENT = BLOCKS.register("pink_tent",
            () -> createTent(DyeColor.PINK));
    public static final RegistryObject<Block> GRAY_TENT = BLOCKS.register("gray_tent",
            () -> createTent(DyeColor.GRAY));
    public static final RegistryObject<Block> LIGHT_GRAY_TENT = BLOCKS.register("light_gray_tent",
            () -> createTent(DyeColor.LIGHT_GRAY));
    public static final RegistryObject<Block> CYAN_TENT = BLOCKS.register("cyan_tent",
            () -> createTent(DyeColor.CYAN));
    public static final RegistryObject<Block> PURPLE_TENT = BLOCKS.register("purple_tent",
            () -> createTent(DyeColor.PURPLE));
    public static final RegistryObject<Block> BLUE_TENT = BLOCKS.register("blue_tent",
            () -> createTent(DyeColor.BLUE));
    public static final RegistryObject<Block> BROWN_TENT = BLOCKS.register("brown_tent",
            () -> createTent(DyeColor.BROWN));
    public static final RegistryObject<Block> GREEN_TENT = BLOCKS.register("green_tent",
            () -> createTent(DyeColor.GREEN));
    public static final RegistryObject<Block> RED_TENT = BLOCKS.register("red_tent",
            () -> createTent(DyeColor.RED));
    public static final RegistryObject<Block> BLACK_TENT = BLOCKS.register("black_tent",
            () -> createTent(DyeColor.BLACK));

    public static final RegistryObject<Block> CAMPFIRE_GRILL = BLOCKS.register("campfire_grill",
            () -> new CampfireGrillBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 600).sound(SoundType.METAL)));

    public static final RegistryObject<Block> BACKPACK = BLOCKS.register("backpack",
            () -> new BackPackBlock(BlockBehaviour.Properties.of(Material.WOOL).strength(1.0f, 100).noOcclusion()));

    public static final RegistryObject<Block> BEAR_TRAP = BLOCKS.register("bear_trap",
            () -> new BearTrapBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(3.5F, 20).sound(SoundType.METAL)));

    public static final RegistryObject<Block> BLUEBERRY_BUSH = BLOCKS.register("blueberry_bush",
            () -> new BlueberryBushBlock(BlockBehaviour.Properties.of(Material.PLANT).randomTicks().noCollission().sound(SoundType.SWEET_BERRY_BUSH)));
    public static final RegistryObject<Block> COFFEE_BUSH = BLOCKS.register("coffee_bush",
            () -> new CoffeeBushBlock(BlockBehaviour.Properties.of(Material.PLANT).randomTicks().noCollission().sound(SoundType.SWEET_BERRY_BUSH)));


    private static Block createBed(DyeColor color) {
        return new SleepingBagBlock(color, BlockBehaviour.Properties.of(Material.WOOL, (p_152613_) -> {
            return p_152613_.getValue(BedBlock.PART) == BedPart.FOOT ? color.getMaterialColor() : MaterialColor.WOOL;
        }).sound(SoundType.WOOL).strength(0.2F).noOcclusion());
    }
    public static final RegistryObject<Block> LIGHT_AIR = BLOCKS.register("light_air",
            ()-> new LightAirBlock(BlockBehaviour.Properties.of(Material.AIR).noCollission().noDrops().air()));

    private static Block createTent(DyeColor color) {
        return new TentBlock(color, BlockBehaviour.Properties.of(Material.WOOL, (blockState) -> {
            return blockState.getValue(BedBlock.PART) == BedPart.FOOT ? color.getMaterialColor() : MaterialColor.WOOL;
        }).sound(SoundType.WOOL).strength(0.5F).lightLevel(state -> state.getValue(TentBlock.LANTERN) ? 12 : 0).noOcclusion());
    }
}
