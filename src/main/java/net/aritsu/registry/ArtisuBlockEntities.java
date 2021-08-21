package net.aritsu.registry;

import net.aritsu.blockentity.SleepingBagBlockEntity;
import net.aritsu.mod.AritsuMod;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ArtisuBlockEntities {
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
}