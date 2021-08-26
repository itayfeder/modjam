package net.aritsu.util;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModTab extends CreativeModeTab {
    public static final ModTab INSTANCE = new ModTab();

    public ModTab() {
        super("camping_mod_tab");
    }

    @Override
    public ItemStack makeIcon() {
        return Items.CAMPFIRE.getDefaultInstance();
    }
}
