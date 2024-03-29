package net.aritsu.item;

import net.aritsu.mod.AritsuMod;
import net.aritsu.registry.AritsuEffects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.stream.StreamSupport;

public class BlueberryJamItem extends Item {
    private static final int DRINK_DURATION = 40;
    public BlueberryJamItem(Item.Properties p_41346_) {
        super(p_41346_);
    }
    public static float getDisplay(ItemStack itemStack) {
        return itemStack.getDisplayName().getString().equals("[Secret Stuff]") ? 1 : 0;
    }


    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        super.finishUsingItem(itemStack, level, livingEntity);
        if (livingEntity instanceof ServerPlayer) {
            ServerPlayer serverplayer = (ServerPlayer)livingEntity;
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, itemStack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!level.isClientSide) {
            if (!itemStack.getDisplayName().getString().equals("[Secret Stuff]"))
            livingEntity.addEffect(new MobEffectInstance(AritsuEffects.SUGAR_RUSH.get(), 2400, 0));
            else {
                livingEntity.addEffect(new MobEffectInstance(AritsuEffects.SUGAR_RUSH.get(), 400, 1));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 1));
                if(livingEntity instanceof ServerPlayer player)
                    player.getAdvancements().award(player.getServer().getAdvancements().getAdvancement(new ResourceLocation(AritsuMod.MODID, "camping/secret")), "secret");
            }
        }

        if (itemStack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
            if (livingEntity instanceof Player && !((Player)livingEntity).getAbilities().instabuild) {
                ItemStack itemstack = new ItemStack(Items.GLASS_BOTTLE);
                Player player = (Player)livingEntity;
                if (!player.getInventory().add(itemstack)) {
                    player.drop(itemstack, false);
                }
            }

            return itemStack;
        }
    }

    public int getUseDuration(ItemStack p_41360_) {
        return 40;
    }

    public UseAnim getUseAnimation(ItemStack p_41358_) {
        return UseAnim.DRINK;
    }

    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    public SoundEvent getEatingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    public InteractionResultHolder<ItemStack> use(Level p_41352_, Player p_41353_, InteractionHand p_41354_) {
        return ItemUtils.startUsingInstantly(p_41352_, p_41353_, p_41354_);
    }
}