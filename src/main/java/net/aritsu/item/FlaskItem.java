package net.aritsu.item;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
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

public class FlaskItem extends Item {

    public FlaskItem(Properties properties) {
        super(properties);
    }

    public static float getFullnessDisplay(ItemStack itemStack) {
        return (float) getChargeCount(itemStack) / 9.0F;
    }

    private static int getChargeCount(ItemStack itemStack) {
        CompoundTag compoundtag = itemStack.getTag();
        if (compoundtag == null) {
            return 0;
        } else {
            return compoundtag.getInt("Charges");
        }
    }

    private static Potion getPotion(ItemStack itemStack) {
        CompoundTag compoundtag = itemStack.getTag();
        if (compoundtag == null) {
            return Potions.EMPTY;
        } else {
            return PotionUtils.getPotion(itemStack);
        }
    }

    private static int add(ItemStack stack, Potion pot) {
        if (pot == getPotion(stack) || getPotion(stack) == Potions.EMPTY) {
            CompoundTag compoundtag = stack.getTag();
            if (getPotion(stack) == Potions.EMPTY) {
                PotionUtils.setPotion(stack, pot);
            }
            if (compoundtag != null) {
                compoundtag.putInt("Charges", getChargeCount(stack) + 1);
            } else {
                stack.getOrCreateTag().putInt("Charges", 1);
            }

            return getChargeCount(stack);
        } else {
            return 0;
        }
    }

    private static void removeOne(ItemStack stack) {
        CompoundTag compoundtag = stack.getOrCreateTag();
        stack.getOrCreateTag().putInt("Charges", getChargeCount(stack) - 1);
        if (getChargeCount(stack) == 0) {
            PotionUtils.setPotion(stack, Potions.EMPTY);
        }

    }

    public ItemStack getDefaultInstance() {
        return PotionUtils.setPotion(super.getDefaultInstance(), Potions.EMPTY);
    }

    public boolean overrideOtherStackedOnMe(ItemStack itemStack, ItemStack itemStack1, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction == ClickAction.SECONDARY && slot.allowModification(player)) {
            if (itemStack1.getItem() instanceof PotionItem
                    && (PotionUtils.getPotion(itemStack1) == getPotion(itemStack) || getPotion(itemStack) == Potions.EMPTY)
                    && getChargeCount(itemStack) < 9) {
                add(itemStack, PotionUtils.getPotion(itemStack1));
                if (!player.getAbilities().instabuild) {
                    itemStack1.shrink(1);
                    player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE, 1));
                }
            } else if (itemStack1.getItem() instanceof BottleItem && getPotion(itemStack) != Potions.EMPTY) {
                itemStack1.shrink(1);
                player.getInventory().add(PotionUtils.setPotion(Items.POTION.getDefaultInstance(), getPotion(itemStack)));
                removeOne(itemStack);
            }

            return true;
        } else {
            return false;
        }
    }

    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        Player player = livingEntity instanceof Player ? (Player) livingEntity : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, itemStack);
        }

        if (!level.isClientSide) {
            for (MobEffectInstance mobeffectinstance : PotionUtils.getMobEffects(itemStack)) {
                if (mobeffectinstance.getEffect().isInstantenous()) {
                    mobeffectinstance.getEffect().applyInstantenousEffect(player, player, livingEntity, mobeffectinstance.getAmplifier(), 1.0D);
                } else {
                    livingEntity.addEffect(new MobEffectInstance(mobeffectinstance));
                }
            }
        }

        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                removeOne(itemStack);
            }
        }

        level.gameEvent(livingEntity, GameEvent.DRINKING_FINISH, livingEntity.eyeBlockPosition());
        return itemStack;
    }

    public int getUseDuration(ItemStack itemStack) {
        return 32;
    }

    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.DRINK;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand inHand) {
        ItemStack stack = player.getItemInHand(inHand);
        return getChargeCount(stack) > 0 ? ItemUtils.startUsingInstantly(level, player, inHand) : InteractionResultHolder.pass(stack);
    }

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        components.add((new TranslatableComponent("item.minecraft.bundle.fullness", getChargeCount(itemStack), 9)).withStyle(ChatFormatting.GRAY));
        PotionUtils.addPotionTooltip(itemStack, components, 1.0F);
    }
}
