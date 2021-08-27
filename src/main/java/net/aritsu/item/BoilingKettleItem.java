package net.aritsu.item;

import net.aritsu.registry.AritsuItems;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BoilingKettleItem extends Item {
    private static final int BAR_COLOR = Mth.hsvToRgb(0.625F, 0.65F, 1.0F);

    public BoilingKettleItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return BAR_COLOR;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack retval = new ItemStack(this);
        retval.setDamageValue(itemStack.getDamageValue() + 1);
        if (retval.getDamageValue() >= retval.getMaxDamage()) {
            return AritsuItems.EMPTY_KETTLE.get().getDefaultInstance();
        }
        return retval;
    }
}
