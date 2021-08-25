package net.aritsu.item;

import net.aritsu.entity.ReinforcedFishingHookEntity;
import net.aritsu.registry.AritsuItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.datafix.fixes.ItemIdFix;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ReinforcedFishingRodItem extends FishingRodItem {

    public ReinforcedFishingRodItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        if (player.fishing != null) {
            if (!level.isClientSide) {
                int i = player.fishing.retrieve(itemstack);
                itemstack.hurtAndBreak(i, player, (p_41288_) -> {
                    p_41288_.broadcastBreakEvent(interactionHand);
                });
            }

            level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            level.gameEvent(player, GameEvent.FISHING_ROD_REEL_IN, player);
        } else {
            level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!level.isClientSide) {
                int j;
                if (getContents(itemstack).toArray().length > 0) {
                    j = 1 + EnchantmentHelper.getFishingSpeedBonus(itemstack) * 2;
                } else {
                    j = EnchantmentHelper.getFishingSpeedBonus(itemstack);
                }
                int k = EnchantmentHelper.getFishingLuckBonus(itemstack);

                level.addFreshEntity(new ReinforcedFishingHookEntity(player, level, k, j));
                removeSingleItem(itemstack);
            }

            player.awardStat(Stats.ITEM_USED.get(this));
            level.gameEvent(player, GameEvent.FISHING_ROD_CAST, player);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    public int getEnchantmentValue() {
        return 1;
    }

    public static float getFullnessDisplay(ItemStack itemStack) {
        return (float)getContentWeight(itemStack) / 64.0F;
    }

    private static int getContentWeight(ItemStack itemStack) {
        return getContents(itemStack).mapToInt((itemStack1) -> {
            return getWeight(itemStack1) * itemStack1.getCount();
        }).sum();
    }

    private static Stream<ItemStack> getContents(ItemStack itemStack) {
        CompoundTag compoundtag = itemStack.getTag();
        if (compoundtag == null) {
            return Stream.empty();
        } else {
            ListTag listtag = compoundtag.getList("Bait", 10);
            return listtag.stream().map(CompoundTag.class::cast).map(ItemStack::of);
        }
    }

    private static int getWeight(ItemStack itemStack) {
        if (itemStack.is(AritsuItems.REINFORCED_FISHING_ROD.get())) {
            return 4 + getContentWeight(itemStack);
        } else {
            if ((itemStack.is(Items.BEEHIVE) || itemStack.is(Items.BEE_NEST)) && itemStack.hasTag()) {
                CompoundTag compoundtag = itemStack.getTagElement("BlockEntityTag");
                if (compoundtag != null && !compoundtag.getList("Bees", 10).isEmpty()) {
                    return 64;
                }
            }

            return 64 / itemStack.getMaxStackSize();
        }
    }

    public boolean overrideStackedOnOther(ItemStack itemStack, Slot slot, ClickAction clickAction, Player player) {
        if (clickAction != ClickAction.SECONDARY) {
            return false;
        } else {
            ItemStack itemstack = slot.getItem();
            if (itemstack.getItem() == AritsuItems.BAIT.get()) {
                if (itemstack.isEmpty()) {
                    removeOne(itemStack).ifPresent((p_150740_) -> {
                        add(itemStack, slot.safeInsert(p_150740_));
                    });
                } else if (itemstack.getItem().canFitInsideContainerItems()) {
                    int i = (64 - getContentWeight(itemStack)) / getWeight(itemstack);
                    add(itemStack, slot.safeTake(itemstack.getCount(), i, player));
                }

                return true;
            }
            return false;

        }
    }

    public boolean overrideOtherStackedOnMe(ItemStack itemStack, ItemStack itemStack1, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction == ClickAction.SECONDARY && slot.allowModification(player)) {
            if (itemStack1.getItem() == AritsuItems.BAIT.get() || itemStack1.isEmpty()) {
                if (itemStack1.isEmpty()) {
                    removeOne(itemStack).ifPresent(slotAccess::set);
                } else {
                    itemStack1.shrink(add(itemStack, itemStack1));
                }
                return true;
            }
            return false;

        } else {
            return false;
        }
    }

    private static int add(ItemStack stack, ItemStack itemStack) {
        if (!itemStack.isEmpty() && itemStack.getItem().canFitInsideContainerItems()) {
            CompoundTag compoundtag = stack.getOrCreateTag();
            if (!compoundtag.contains("Bait")) {
                compoundtag.put("Bait", new ListTag());
            }

            int i = getContentWeight(stack);
            int j = getWeight(itemStack);
            int k = Math.min(itemStack.getCount(), (64 - i) / j);
            if (k == 0) {
                return 0;
            } else {
                ListTag listtag = compoundtag.getList("Bait", 10);
                Optional<CompoundTag> optional = getMatchingItem(itemStack, listtag);
                if (optional.isPresent()) {
                    CompoundTag compoundtag1 = optional.get();
                    ItemStack itemstack = ItemStack.of(compoundtag1);
                    itemstack.grow(k);
                    itemstack.save(compoundtag1);
                    listtag.remove(compoundtag1);
                    listtag.add(0, (Tag)compoundtag1);
                } else {
                    ItemStack itemstack1 = itemStack.copy();
                    itemstack1.setCount(k);
                    CompoundTag compoundtag2 = new CompoundTag();
                    itemstack1.save(compoundtag2);
                    listtag.add(0, (Tag)compoundtag2);
                }

                return k;
            }
        } else {
            return 0;
        }
    }

    private static Optional<CompoundTag> getMatchingItem(ItemStack itemStack, ListTag listTag) {
        return itemStack.is(AritsuItems.REINFORCED_FISHING_ROD.get()) ? Optional.empty() : listTag.stream().filter(CompoundTag.class::isInstance).map(CompoundTag.class::cast).filter((p_150755_) -> {
            return ItemStack.isSameItemSameTags(ItemStack.of(p_150755_), itemStack);
        }).findFirst();
    }

    private static Optional<ItemStack> removeOne(ItemStack p_150781_) {
        CompoundTag compoundtag = p_150781_.getOrCreateTag();
        if (!compoundtag.contains("Bait")) {
            return Optional.empty();
        } else {
            ListTag listtag = compoundtag.getList("Bait", 10);
            if (listtag.isEmpty()) {
                return Optional.empty();
            } else {
                int i = 0;
                CompoundTag compoundtag1 = listtag.getCompound(0);
                ItemStack itemstack = ItemStack.of(compoundtag1);
                listtag.remove(0);
                if (listtag.isEmpty()) {
                    p_150781_.removeTagKey("Bait");
                }
                p_150781_.getOrCreateTag().put("Bait", listtag);

                return Optional.of(itemstack);
            }
        }
    }

    private static Optional<ItemStack> removeSingleItem(ItemStack p_150781_) {
        CompoundTag compoundtag = p_150781_.getOrCreateTag();
        if (!compoundtag.contains("Bait")) {
            return Optional.empty();
        } else {
            ListTag listtag = compoundtag.getList("Bait", 10);
            if (listtag.isEmpty()) {
                return Optional.empty();
            } else {
                int i = 0;
                CompoundTag compoundtag1 = listtag.getCompound(0);
                ItemStack itemstack = ItemStack.of(compoundtag1);
                itemstack.shrink(1);
                CompoundTag newTag = itemstack.save(compoundtag1);
                listtag.set(0, newTag);
                if (listtag.isEmpty()) {
                    p_150781_.removeTagKey("Bait");
                }

                return Optional.of(itemstack);
            }
        }
    }

    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();
        getContents(itemStack).forEach(nonnulllist::add);
        return Optional.of(new BundleTooltip(nonnulllist, getContentWeight(itemStack)));
    }

    public void appendHoverText(ItemStack itemStack, Level level, List<Component> components, TooltipFlag tooltipFlag) {
        components.add((new TranslatableComponent("item.minecraft.bundle.fullness", getContentWeight(itemStack), 64)).withStyle(ChatFormatting.GRAY));
    }

    public void onDestroyed(ItemEntity itemEntity) {
        ItemUtils.onContainerDestroyed(itemEntity, getContents(itemEntity.getItem()));
    }
}
