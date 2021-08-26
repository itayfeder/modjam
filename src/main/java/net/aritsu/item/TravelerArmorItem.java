package net.aritsu.item;

import net.aritsu.mod.AritsuMod;
import net.aritsu.util.ClientReferences;
import net.aritsu.util.ModTab;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.Random;

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
        return AritsuMod.MODID + ":" + (slot == EquipmentSlot.LEGS ? "textures/models/armor/traveler_layer_2.png" : "textures/models/armor/traveler_layer_1.png");
    }
    Random rnd =new Random();
    int soundChance=0;
    double pitch=0;
    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        super.onArmorTick(stack, world, player);
        if (stack.getItem() instanceof TravelerArmorItem armorItem) {
            switch (armorItem.getSlot()) {
                case CHEST:
                    if (player.getEffect(MobEffects.DAMAGE_BOOST) == null) {
                        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 0));
                    } else {
                        if (player.getEffect(MobEffects.DAMAGE_BOOST).getDuration() <= 200)
                            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 0));
                    }
                    break;
                case LEGS:
                    if (player.getEffect(MobEffects.DOLPHINS_GRACE) == null) {
                        player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 300, 0));
                    } else {
                        if (player.getEffect(MobEffects.DOLPHINS_GRACE).getDuration() <= 200)
                            player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 300, 0));
                    }
                    if (player.getEffect(MobEffects.CONDUIT_POWER) == null) {
                        player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 300, 0));
                    } else {
                        if (player.getEffect(MobEffects.CONDUIT_POWER).getDuration() <= 200)
                            player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 300, 0));
                    }
                    break;
                case FEET:
                    if (player.getEffect(MobEffects.MOVEMENT_SPEED) == null) {
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 0));
                    } else {
                        if (player.getEffect(MobEffects.MOVEMENT_SPEED).getDuration() <= 200)
                            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 0));
                    }
                    if (player.horizontalCollision) {
                        double motX = player.getDeltaMovement().x;
                        player.getDeltaMovement();
                        motX = player.getDeltaMovement().z;
                        double motY=player.getDeltaMovement().y;
                        player.fallDistance = 0.0F;
                        if (player.isCrouching()) {
                            player.setDeltaMovement(motX, 0.0D, motX);
                        } else {
                            if (motY>=2) player.setDeltaMovement(motX, 2D, motX); else player.setDeltaMovement(motX, motY+ 0.1D, motX);
                            soundChance++;
                            pitch=rnd.nextDouble();
                            if(soundChance%7==0)player.playSound(SoundEvents.STONE_STEP,(float) pitch,1);
                        }

                    }
                    break;
            }
        }
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
                    return 5;
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
