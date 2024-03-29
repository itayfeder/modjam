package net.aritsu.item;

import net.aritsu.mod.AritsuMod;
import net.aritsu.registry.AritsuBlocks;
import net.aritsu.registry.AritsuEffects;
import net.aritsu.util.ClientReferences;
import net.aritsu.util.ModTab;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.IItemRenderProperties;

import javax.annotation.Nullable;
import java.util.Random;

public class TravelerArmorItem extends ArmorItem implements IItemRenderProperties {

    public static final Item.Properties armorProps = new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).durability(200).defaultDurability(200).tab(ModTab.INSTANCE);
    Random rnd = new Random();
    int soundChance = 0;
    double pitch = 0;
    boolean addClimber1, addClimber2, addSwimmer, addHeadlight;
    private double tempClimbCache = 0.0D;

    public TravelerArmorItem(EquipmentSlot slot, Item.Properties props) {
        super(new TravelerArmorItem.TravelerMaterial(), slot, props);
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

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        super.onArmorTick(stack, world, player);
        if (stack.getItem() instanceof TravelerArmorItem armorItem) {
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.getAdvancements().award(player.getServer().getAdvancements().getAdvancement(new ResourceLocation(AritsuMod.MODID, "camping/traveler")), "traveler");
                if (addClimber1)
                    serverPlayer.getAdvancements().award(player.getServer().getAdvancements().getAdvancement(new ResourceLocation(AritsuMod.MODID, "camping/climbing_boots_1")), "climbing_boots_1");
                if (addClimber2) {
                    serverPlayer.getAdvancements().award(player.getServer().getAdvancements().getAdvancement(new ResourceLocation(AritsuMod.MODID, "camping/climbing_boots_2")), "climbing_boots_2");
                }
                if (addSwimmer) {
                    serverPlayer.getAdvancements().award(player.getServer().getAdvancements().getAdvancement(new ResourceLocation(AritsuMod.MODID, "camping/swimmer_trunk")), "swimmer_trunk");
                }
                if (addHeadlight) {
                    serverPlayer.getAdvancements().award(player.getServer().getAdvancements().getAdvancement(new ResourceLocation(AritsuMod.MODID, "camping/headlight")), "headlight");
                }
            }

            switch (armorItem.getSlot()) {
                case HEAD:
                    if (world.getBlockState(player.blockPosition()).isAir())
                        world.setBlock(player.blockPosition(), AritsuBlocks.LIGHT_AIR.get().defaultBlockState(), 512);
                    else if (world.getBlockState(player.blockPosition().above()).isAir())
                        world.setBlock(player.blockPosition().above(), AritsuBlocks.LIGHT_AIR.get().defaultBlockState(), 512);
                    else if (world.getBlockState(player.blockPosition().above(2)).isAir())
                        world.setBlock(player.blockPosition().above(2), AritsuBlocks.LIGHT_AIR.get().defaultBlockState(), 512);
                    addHeadlight = true;
                    break;
                case CHEST:
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 2, false, false, false));
                    break;
                case LEGS:
                    if (player.isInWater() && player.isAffectedByFluids() && !player.canStandOnFluid(player.level.getFluidState(player.blockPosition()).getType())) {
                        player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 20, 0, false, false, false));
                        player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 20, 0, false, false, false));
                    }
                    if (player.isSwimming()) {
                        addSwimmer = true;
                    }
                    break;
                case FEET:
                    if (world.isClientSide)
                        if (player.horizontalCollision) {
                            player.fallDistance = 0.0F;
                            tempClimbCache += 0.01d;
                            boolean sugarFlag = player.hasEffect(AritsuEffects.SUGAR_RUSH.get());
                            double d2 = sugarFlag ? Math.min(tempClimbCache, 2.0d) : 0.19d;
                            if (player.isCrouching())
                                d2 = 0.0D;
                            player.setDeltaMovement(new Vec3(player.getDeltaMovement().x, d2, player.getDeltaMovement().z));
                            addClimber1 = !sugarFlag;
                            addClimber2 = sugarFlag;

                            if (player.getRandom().nextInt(7) == 0)
                                player.playSound(SoundEvents.STONE_STEP, player.getRandom().nextFloat(), 1);
                        } else {
                            tempClimbCache = 0.0d;
                        }
                    break;
            }
        }
    }

    @Override
    public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack
            itemStack, EquipmentSlot armorSlot, A _default) {
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
                case FEET, HEAD -> {
                    return 10;
                }
            }
            return 0;
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot slot) {
            switch (slot) {
                case CHEST, LEGS, FEET, HEAD -> {
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
            return Ingredient.of(ItemStack.EMPTY);
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
