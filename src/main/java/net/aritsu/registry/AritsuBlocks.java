package net.aritsu.registry;

import net.aritsu.block.LogSeatBlock;
import net.aritsu.mod.AritsuMod;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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

}
