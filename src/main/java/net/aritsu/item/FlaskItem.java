package net.aritsu.item;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FlaskItem extends Item {

    public ItemStack getDefaultInstance() {
        return PotionUtils.setPotion(super.getDefaultInstance(), Potions.EMPTY);
    }

    public FlaskItem(Properties p_41383_) {
        super(p_41383_);
    }

    public static float getFullnessDisplay(ItemStack p_150767_) {
        return (float)getChargeCount(p_150767_) / 9.0F;
    }

    private static int getChargeCount(ItemStack p_150779_) {
        CompoundTag compoundtag = p_150779_.getTag();
        if (compoundtag == null) {
            return 0;
        } else {
            return compoundtag.getInt("Charges");
        }
    }

    private static Potion getPotion(ItemStack p_150783_) {
        CompoundTag compoundtag = p_150783_.getTag();
        if (compoundtag == null) {
            return Potions.EMPTY;
        } else {
            return PotionUtils.getPotion(p_150783_);
        }
    }

    public boolean overrideOtherStackedOnMe(ItemStack p_150742_, ItemStack p_150743_, Slot p_150744_, ClickAction p_150745_, Player p_150746_, SlotAccess p_150747_) {
        if (p_150745_ == ClickAction.SECONDARY && p_150744_.allowModification(p_150746_)) {
            if (p_150743_.getItem() instanceof PotionItem
                    && (PotionUtils.getPotion(p_150743_) == getPotion(p_150742_) || getPotion(p_150742_) == Potions.EMPTY)
                    && getChargeCount(p_150742_) < 9) {
                add(p_150742_, PotionUtils.getPotion(p_150743_));
                if (!p_150746_.getAbilities().instabuild) {
                    p_150743_.shrink(1);
                    p_150746_.getInventory().add(new ItemStack(Items.GLASS_BOTTLE,1));
                }
            }
            else if (p_150743_.getItem() instanceof BottleItem && getPotion(p_150742_) != Potions.EMPTY) {
                p_150743_.shrink(1);
                p_150746_.getInventory().add(PotionUtils.setPotion(Items.POTION.getDefaultInstance(), getPotion(p_150742_)));
                removeOne(p_150742_);
            }

            return true;
        } else {
            return false;
        }
    }

    public ItemStack finishUsingItem(ItemStack p_42984_, Level p_42985_, LivingEntity p_42986_) {
        Player player = p_42986_ instanceof Player ? (Player)p_42986_ : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, p_42984_);
        }

        if (!p_42985_.isClientSide) {
            for(MobEffectInstance mobeffectinstance : PotionUtils.getMobEffects(p_42984_)) {
                if (mobeffectinstance.getEffect().isInstantenous()) {
                    mobeffectinstance.getEffect().applyInstantenousEffect(player, player, p_42986_, mobeffectinstance.getAmplifier(), 1.0D);
                } else {
                    p_42986_.addEffect(new MobEffectInstance(mobeffectinstance));
                }
            }
        }

        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                removeOne(p_42984_);
            }
        }

        p_42985_.gameEvent(p_42986_, GameEvent.DRINKING_FINISH, p_42986_.eyeBlockPosition());
        return p_42984_;
    }

    private static int add(ItemStack stack, Potion pot) {
        if (pot == getPotion(stack) || getPotion(stack) == Potions.EMPTY) {
            CompoundTag compoundtag = stack.getTag();
            if (getPotion(stack) == Potions.EMPTY) {
                PotionUtils.setPotion(stack, pot);
            }
            if (compoundtag != null) {
                compoundtag.putInt("Charges", getChargeCount(stack)+1);
            }
            else {
                stack.getOrCreateTag().putInt("Charges", 1);
            }

            return getChargeCount(stack);
        } else {
            return 0;
        }
    }

    private static void removeOne(ItemStack stack) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        stack.getOrCreateTag().putInt("Charges", getChargeCount(stack)-1);
        if (getChargeCount(stack) == 0) {
            PotionUtils.setPotion(stack, Potions.EMPTY);
        }

    }

    public int getUseDuration(ItemStack p_43001_) {
        return 32;
    }

    public UseAnim getUseAnimation(ItemStack p_42997_) {
        return UseAnim.DRINK;
    }

    public InteractionResultHolder<ItemStack> use(Level p_42993_, Player p_42994_, InteractionHand p_42995_) {
        ItemStack stack = p_42994_.getItemInHand(p_42995_);
        return getChargeCount(stack) > 0 ? ItemUtils.startUsingInstantly(p_42993_, p_42994_, p_42995_) : InteractionResultHolder.pass(stack);
    }

    public void appendHoverText(ItemStack p_42988_, @Nullable Level p_42989_, List<Component> p_42990_, TooltipFlag p_42991_) {
        p_42990_.add((new TranslatableComponent("item.minecraft.bundle.fullness", getChargeCount(p_42988_), 9)).withStyle(ChatFormatting.GRAY));
        PotionUtils.addPotionTooltip(p_42988_, p_42990_, 1.0F);
    }
}
