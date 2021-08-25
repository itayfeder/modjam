package net.aritsu.item;

import net.aritsu.mod.AritsuMod;
import net.aritsu.util.ClientReferences;
import net.aritsu.util.ModTab;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;

public class TravelerArmorItem extends ArmorItem implements IItemRenderProperties {

    private static final Item.Properties armorProps = new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).durability(200).defaultDurability(200).tab(ModTab.INSTANCE);

    public TravelerArmorItem(EquipmentSlot slot) {
        super(new TravelerArmorItem.TravelerMaterial(), slot, armorProps);
    }

    @Override
    public Object getRenderPropertiesInternal() {
        return this;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return AritsuMod.MODID+":"+ (slot == EquipmentSlot.LEGS ? "textures/models/armor/hiker_layer_2.png" : "textures/models/armor/hiker_layer_1.png");
    }

    @Override
    public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
        return (A) ClientReferences.getArmorModel(armorSlot);
    }

    private static class TravelerMaterial implements ArmorMaterial {

        @Override
        public int getDurabilityForSlot(EquipmentSlot slot) {
            switch (slot) {
                case CHEST -> {
                    return 30;
                }
                case LEGS -> {
                    return 15;
                }
                case FEET -> {
                    return 10;
                }
            }
            return 0;
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot slot) {
            switch (slot) {
                case CHEST -> {
                    return 5;
                }
                case LEGS -> {
                    return 5;
                }
                case FEET -> {
                    return 3;
                }
            }
            return 0;
        }

        @Override
        public int getEnchantmentValue() {
            return 0;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_GENERIC;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(Tags.Items.LEATHER);
        }

        @Override
        public String getName() {
            return "traveler";
        }

        @Override
        public float getToughness() {
            return 1.0F;
        }

        @Override
        public float getKnockbackResistance() {
            return 0.0F;
        }
    }
}
