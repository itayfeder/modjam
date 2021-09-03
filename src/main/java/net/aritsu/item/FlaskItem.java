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

    public static final String MILKTAG = "storesMilk";
    public static final String CHARGESTAG = "Charges";

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
            return compoundtag.getInt(CHARGESTAG);
        }
    }

    private boolean canTakeLiquid(ItemStack flask, ItemStack hovered) {
        boolean isEmpty = flask.getTag() == null || flask.getTag().isEmpty();
        if (!isEmpty) {
            boolean hasCharges = flask.getTag().contains(CHARGESTAG) && flask.getTag().getInt(CHARGESTAG) > 0;
            if (hasCharges) {
                boolean hasMilk = flask.getTag().contains(MILKTAG) && getLiquidCandidateFromHovered(hovered) == FlaskLiquid.MILK;
                boolean hasPotion = flask.getTag().contains("Potion") && getLiquidCandidateFromHovered(hovered) == FlaskLiquid.POTION;
                return hasMilk || hasPotion;
            }
        }
        return true; //can take if empty, can take if it has no charges
    }

    private FlaskLiquid getContainingLiquid(ItemStack flask) {
        boolean isEmpty = flask.getTag() == null || flask.getTag().isEmpty();
        if (!isEmpty) {
            boolean hasCharges = flask.getTag().contains(CHARGESTAG) && flask.getTag().getInt(CHARGESTAG) > 0;
            if (hasCharges) {
                boolean hasMilk = flask.getTag().contains(MILKTAG);
                boolean hasPotion = flask.getTag().contains("Potion");
                return hasMilk ? FlaskLiquid.MILK : hasPotion ? FlaskLiquid.POTION : FlaskLiquid.NONE;
            }
        }
        return FlaskLiquid.NONE; //flask is empty
    }

    private FlaskLiquid getLiquidCandidateFromHovered(ItemStack hovered) {
        if (hovered.getItem() instanceof MilkBucketItem)
            return FlaskLiquid.MILK;
        if (hovered.getItem() instanceof PotionItem)
            return FlaskLiquid.POTION;
        return FlaskLiquid.NONE;
    }

    private Potion getPotionFromStackNBT(ItemStack itemStack) {

        CompoundTag compoundtag = itemStack.getTag();
        if (compoundtag == null) {
            return Potions.EMPTY;
        } else {
            if (compoundtag.contains("Potion"))
                return PotionUtils.getPotion(itemStack);
            else return Potions.EMPTY;
        }
    }

    private void removeOneFromFlask(ItemStack stack) {
        stack.getOrCreateTag().putInt(CHARGESTAG, getChargeCount(stack) - 1);
        if (getChargeCount(stack) == 0) {
            PotionUtils.setPotion(stack, Potions.EMPTY);
            stack.removeTagKey(MILKTAG);
        }
    }

    private boolean flaskIsEmpty(ItemStack self) {
        return getContainingLiquid(self) == FlaskLiquid.NONE;
    }

    private void addOneToFlask(ItemStack stack, Potion potion) {
        if (stack.getTag() == null)
            stack.getOrCreateTag();
        if (potion == null) {
            if ((getContainingLiquid(stack) == FlaskLiquid.MILK) || flaskIsEmpty(stack)) {
                stack.getOrCreateTag().putBoolean(MILKTAG, true);
            }
        } else if ((getContainingLiquid(stack) == FlaskLiquid.POTION) || flaskIsEmpty(stack)) {
            if (getPotionFromStackNBT(stack) == Potions.EMPTY) {
                PotionUtils.setPotion(stack, potion); // set nbt to have a potion tag
            }
        }

        if (stack.getTag().contains(CHARGESTAG)) {
            stack.getTag().putInt(CHARGESTAG, getChargeCount(stack) + 1);
        } else stack.getTag().putInt(CHARGESTAG, 1);

    }

    public ItemStack getDefaultInstance() {
        return PotionUtils.setPotion(super.getDefaultInstance(), Potions.EMPTY);
    }

    public boolean overrideOtherStackedOnMe(ItemStack thisStack, ItemStack hoveredStack, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction == ClickAction.SECONDARY && slot.allowModification(player)) {
            if (canTakeLiquid(thisStack, hoveredStack)) //check if clicked item can be poured in
            {
                if (getChargeCount(thisStack) < 9) {
                    switch (getLiquidCandidateFromHovered(hoveredStack)) {
                        case POTION -> {
                            Potion potion = PotionUtils.getPotion(hoveredStack);
                            if (getPotionFromStackNBT(thisStack) == Potions.EMPTY || getPotionFromStackNBT(thisStack) == potion) {
                                addOneToFlask(thisStack, potion);
                                if (!player.getAbilities().instabuild) {
                                    hoveredStack.shrink(1);
                                    player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE, 1));
                                }
                                return true;
                            }
                        }
                        case MILK -> {
                            addOneToFlask(thisStack, null);
                            if (!player.getAbilities().instabuild) {
                                hoveredStack.shrink(1);
                                player.getInventory().add(new ItemStack(Items.BUCKET, 1));
                            }
                            return true;
                        }
                    }
                }
            } else if (getChargeCount(thisStack) > 0) { //if the item cant be poored in, and the flask has charges
                if (hoveredStack.getItem() instanceof BottleItem && getContainingLiquid(thisStack) == FlaskLiquid.POTION) {
                    takeLiquidOutofStack(player, thisStack, hoveredStack, PotionUtils.setPotion(Items.POTION.getDefaultInstance(), getPotionFromStackNBT(thisStack)));
                } else if (hoveredStack.getItem() instanceof BucketItem && getContainingLiquid(thisStack) == FlaskLiquid.MILK) {
                    takeLiquidOutofStack(player, thisStack, hoveredStack, new ItemStack(Items.MILK_BUCKET));
                }
            }
        }
        return false;
    }

    private void takeLiquidOutofStack(Player player, ItemStack flask, ItemStack emptyContainerStack, ItemStack fullContainerStack) {
        emptyContainerStack.shrink(1);
        player.getInventory().add(fullContainerStack);
        removeOneFromFlask(flask);
    }

    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {

        if (livingEntity instanceof Player player) {

            if (player instanceof ServerPlayer serverPlayer) { //doubles as !isclient check

                CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemStack);
                if (getContainingLiquid(itemStack) == FlaskLiquid.POTION) {
                    for (MobEffectInstance mobeffectinstance : PotionUtils.getMobEffects(itemStack)) {
                        if (mobeffectinstance.getEffect().isInstantenous()) {
                            mobeffectinstance.getEffect().applyInstantenousEffect(serverPlayer, serverPlayer, livingEntity, mobeffectinstance.getAmplifier(), 1.0D);
                        } else {
                            livingEntity.addEffect(new MobEffectInstance(mobeffectinstance));
                        }
                    }
                } else if (getContainingLiquid(itemStack) == FlaskLiquid.MILK) {
                    serverPlayer.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
                }

            }
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                removeOneFromFlask(itemStack);
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
        if (getChargeCount(itemStack) > 0 && itemStack.getTag().contains(MILKTAG))
            components.add(new TranslatableComponent("milk.flask").withStyle(ChatFormatting.GRAY));
        else
            PotionUtils.addPotionTooltip(itemStack, components, 1.0F);
    }

    public enum FlaskLiquid {
        POTION,
        MILK,
        NONE // edge case.
    }
}
