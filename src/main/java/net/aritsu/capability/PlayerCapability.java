package net.aritsu.capability;

import net.aritsu.mod.AritsuMod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class PlayerCapability implements ICapabilitySerializable<CompoundTag> {

    /**
     * Unique key to identify the attached provider from others
     */
    public static final ResourceLocation KEY = new ResourceLocation(AritsuMod.MODID, "backpack_cap");
    /*
     * This field will contain the forge-allocated Capability class. This instance
     * will be initialized internally by Forge, upon calling register in FMLCommonSetupEvent.
     */
    @CapabilityInject(PlayerData.class)
    public static net.minecraftforge.common.capabilities.Capability<PlayerData> CAPABILITY;

    final PlayerData playerData = new PlayerData();

    /**
     * Gets called before world is initiated. player.worldObj will return null here !
     */
    public PlayerCapability(Player player) {

        playerData.setPlayer(player);
    }

    @SubscribeEvent
    public static void startCommonSetup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(PlayerData.class);
    }

    @Override
    public CompoundTag serializeNBT() {

        return (CompoundTag) playerData.writeData();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

        playerData.readData(nbt);
    }

    @Override
    public <T> LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> cap, Direction side) {

        if (cap == PlayerCapability.CAPABILITY)
            return (LazyOptional<T>) LazyOptional.of(this::getImpl);

        return LazyOptional.empty();
    }

    private PlayerData getImpl() {

        return playerData;
    }
}
