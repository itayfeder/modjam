package net.aritsu.registry;

import net.aritsu.blockentity.BackPackBlockEntity;
import net.aritsu.blockentity.CampfireGrillBlockEntity;
import net.aritsu.blockentity.SleepingBagBlockEntity;
import net.aritsu.blockentity.TentBlockEntity;
import net.aritsu.mod.AritsuMod;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AritsuBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> TILEENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, AritsuMod.MODID);

    public static final RegistryObject<BlockEntityType<SleepingBagBlockEntity>> SLEEPING_BAG = TILEENTITIES.register("sleeping_bag", () ->
            BlockEntityType.Builder.of(SleepingBagBlockEntity::new,
                    AritsuBlocks.WHITE_SLEEPING_BAG.get(),
                    AritsuBlocks.ORANGE_SLEEPING_BAG.get(),
                    AritsuBlocks.MAGENTA_SLEEPING_BAG.get(),
                    AritsuBlocks.LIGHT_BLUE_SLEEPING_BAG.get(),
                    AritsuBlocks.YELLOW_SLEEPING_BAG.get(),
                    AritsuBlocks.LIME_SLEEPING_BAG.get(),
                    AritsuBlocks.PINK_SLEEPING_BAG.get(),
                    AritsuBlocks.GRAY_SLEEPING_BAG.get(),
                    AritsuBlocks.LIGHT_GRAY_SLEEPING_BAG.get(),
                    AritsuBlocks.CYAN_SLEEPING_BAG.get(),
                    AritsuBlocks.PURPLE_SLEEPING_BAG.get(),
                    AritsuBlocks.BLUE_SLEEPING_BAG.get(),
                    AritsuBlocks.BROWN_SLEEPING_BAG.get(),
                    AritsuBlocks.GREEN_SLEEPING_BAG.get(),
                    AritsuBlocks.RED_SLEEPING_BAG.get(),
                    AritsuBlocks.BLACK_SLEEPING_BAG.get()).build(null));

    public static final RegistryObject<BlockEntityType<TentBlockEntity>> TENT_BE = TILEENTITIES.register("tent_be", () ->
            BlockEntityType.Builder.of(TentBlockEntity::new,
                    AritsuBlocks.WHITE_TENT.get(),
                    AritsuBlocks.ORANGE_TENT.get(),
                    AritsuBlocks.MAGENTA_TENT.get(),
                    AritsuBlocks.LIGHT_BLUE_TENT.get(),
                    AritsuBlocks.YELLOW_TENT.get(),
                    AritsuBlocks.LIME_TENT.get(),
                    AritsuBlocks.PINK_TENT.get(),
                    AritsuBlocks.GRAY_TENT.get(),
                    AritsuBlocks.LIGHT_GRAY_TENT.get(),
                    AritsuBlocks.CYAN_TENT.get(),
                    AritsuBlocks.PURPLE_TENT.get(),
                    AritsuBlocks.BLUE_TENT.get(),
                    AritsuBlocks.BROWN_TENT.get(),
                    AritsuBlocks.GREEN_TENT.get(),
                    AritsuBlocks.RED_TENT.get(),
                    AritsuBlocks.BLACK_TENT.get()).build(null));

    public static final RegistryObject<BlockEntityType<CampfireGrillBlockEntity>> CAMPFIRE_GRILL = TILEENTITIES.register("campfire_grill", () ->
            BlockEntityType.Builder.of(CampfireGrillBlockEntity::new,
                    AritsuBlocks.CAMPFIRE_GRILL.get()).build(null));

    public static final RegistryObject<BlockEntityType<BackPackBlockEntity>> BACKPACK_BE = TILEENTITIES.register("backpack_be", () ->
            BlockEntityType.Builder.of(BackPackBlockEntity::new,
                    AritsuBlocks.BACKPACK.get()).build(null));
}
