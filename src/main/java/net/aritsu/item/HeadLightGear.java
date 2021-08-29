package net.aritsu.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Wearable;

import javax.annotation.Nullable;

public class HeadLightGear extends Item implements Wearable {
    public HeadLightGear(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        if (armorType == EquipmentSlot.HEAD && entity instanceof Player)
            return true;
        return super.canEquip(stack, armorType, entity);
    }

    @Nullable
    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_LEATHER;
    }
}
