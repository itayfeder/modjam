package net.aritsu.registry;

import net.aritsu.mod.AritsuMod;
import net.aritsu.screen.common.BackPackContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AritsuContainers {

    public static final DeferredRegister<MenuType<?>> CONTAINER = DeferredRegister.create(ForgeRegistries.CONTAINERS, AritsuMod.MODID);

    public static final RegistryObject<MenuType<BackPackContainer>> BACKPACK_CONTAINER_TYPE = CONTAINER.register("artisu_backpack_container",
            () -> new MenuType<>(BackPackContainer::registerClientContainer));
}
