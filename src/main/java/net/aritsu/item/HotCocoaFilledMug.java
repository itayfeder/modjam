package net.aritsu.item;

import net.aritsu.registry.AritsuItems;
import net.minecraft.advancements.CriteriaTriggers;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class HotCocoaFilledMug extends Item {
    public HotCocoaFilledMug(Item.Properties p_41346_) {
        super(p_41346_);
    }

    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        super.finishUsingItem(itemStack, level, livingEntity);
        if (livingEntity instanceof ServerPlayer) {
            ServerPlayer serverplayer = (ServerPlayer)livingEntity;
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, itemStack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!level.isClientSide) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,1800,0));
        }
        if (livingEntity instanceof Player && !((Player)livingEntity).getAbilities().instabuild) {
            itemStack.shrink(1);
        }
        if (itemStack.isEmpty()) {
            return new ItemStack(AritsuItems.GLASS_MUG.get());
        } else {
            if (livingEntity instanceof Player && !((Player)livingEntity).getAbilities().instabuild) {
                ItemStack itemstack = new ItemStack(AritsuItems.GLASS_MUG.get());
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
        return SoundEvents.GENERIC_DRINK;
    }

    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        return ItemUtils.startUsingInstantly(level, player, interactionHand);
    }
}
