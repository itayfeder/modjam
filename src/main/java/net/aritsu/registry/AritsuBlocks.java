package net.aritsu.registry;

import net.aritsu.block.BackPackBlock;
import net.aritsu.block.CampfireGrillBlock;
import net.aritsu.block.LogSeatBlock;
import net.aritsu.block.SleepingBagBlock;
import net.aritsu.mod.AritsuMod;
import net.minecraft.world.item.DyeColor;
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
    public static final RegistryObject<Block> BACKPACKBLOCK = BLOCKS.register("backpack_block",
            () -> new BackPackBlock(BlockBehaviour.Properties.of(Material.WOOL)));

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

    public static final RegistryObject<Block> CAMPFIRE_GRILL = BLOCKS.register("campfire_grill",
            () -> new CampfireGrillBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 600).sound(SoundType.ANVIL)));



    private static Block createBed(DyeColor color) {
        return new SleepingBagBlock(color, BlockBehaviour.Properties.of(Material.WOOL, (p_152613_) -> {
            return p_152613_.getValue(BedBlock.PART) == BedPart.FOOT ? color.getMaterialColor() : MaterialColor.WOOL;
        }).sound(SoundType.WOOL).strength(0.2F).noOcclusion());
    }
}
