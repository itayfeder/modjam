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

    public ReinforcedFishingRodItem(Properties p_41285_) {
        super(p_41285_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41290_, Player p_41291_, InteractionHand p_41292_) {
        ItemStack itemstack = p_41291_.getItemInHand(p_41292_);
        if (p_41291_.fishing != null) {
            if (!p_41290_.isClientSide) {
                int i = p_41291_.fishing.retrieve(itemstack);
                itemstack.hurtAndBreak(i, p_41291_, (p_41288_) -> {
                    p_41288_.broadcastBreakEvent(p_41292_);
                });
            }

            p_41290_.playSound((Player)null, p_41291_.getX(), p_41291_.getY(), p_41291_.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (p_41290_.getRandom().nextFloat() * 0.4F + 0.8F));
            p_41290_.gameEvent(p_41291_, GameEvent.FISHING_ROD_REEL_IN, p_41291_);
        } else {
            p_41290_.playSound((Player)null, p_41291_.getX(), p_41291_.getY(), p_41291_.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (p_41290_.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!p_41290_.isClientSide) {
                int j;
                if (getContents(itemstack).toArray().length > 0) {
                    j = 1 + EnchantmentHelper.getFishingSpeedBonus(itemstack) * 2;
                } else {
                    j = EnchantmentHelper.getFishingSpeedBonus(itemstack);
                }
                int k = EnchantmentHelper.getFishingLuckBonus(itemstack);

                p_41290_.addFreshEntity(new ReinforcedFishingHookEntity(p_41291_, p_41290_, k, j));
                removeSingleItem(itemstack);
            }

            p_41291_.awardStat(Stats.ITEM_USED.get(this));
            p_41290_.gameEvent(p_41291_, GameEvent.FISHING_ROD_CAST, p_41291_);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, p_41290_.isClientSide());
    }

    public int getEnchantmentValue() {
        return 1;
    }

    public static float getFullnessDisplay(ItemStack p_150767_) {
        return (float)getContentWeight(p_150767_) / 64.0F;
    }

    private static int getContentWeight(ItemStack p_150779_) {
        return getContents(p_150779_).mapToInt((p_150785_) -> {
            return getWeight(p_150785_) * p_150785_.getCount();
        }).sum();
    }

    private static Stream<ItemStack> getContents(ItemStack p_150783_) {
        CompoundTag compoundtag = p_150783_.getTag();
        if (compoundtag == null) {
            return Stream.empty();
        } else {
            ListTag listtag = compoundtag.getList("Bait", 10);
            return listtag.stream().map(CompoundTag.class::cast).map(ItemStack::of);
        }
    }

    private static int getWeight(ItemStack p_150777_) {
        if (p_150777_.is(AritsuItems.REINFORCED_FISHING_ROD.get())) {
            return 4 + getContentWeight(p_150777_);
        } else {
            if ((p_150777_.is(Items.BEEHIVE) || p_150777_.is(Items.BEE_NEST)) && p_150777_.hasTag()) {
                CompoundTag compoundtag = p_150777_.getTagElement("BlockEntityTag");
                if (compoundtag != null && !compoundtag.getList("Bees", 10).isEmpty()) {
                    return 64;
                }
            }

            return 64 / p_150777_.getMaxStackSize();
        }
    }

    public boolean overrideStackedOnOther(ItemStack p_150733_, Slot p_150734_, ClickAction p_150735_, Player p_150736_) {
        if (p_150735_ != ClickAction.SECONDARY) {
            return false;
        } else {
            ItemStack itemstack = p_150734_.getItem();
            if (itemstack.getItem() == AritsuItems.BAIT.get()) {
                if (itemstack.isEmpty()) {
                    removeOne(p_150733_).ifPresent((p_150740_) -> {
                        add(p_150733_, p_150734_.safeInsert(p_150740_));
                    });
                } else if (itemstack.getItem().canFitInsideContainerItems()) {
                    int i = (64 - getContentWeight(p_150733_)) / getWeight(itemstack);
                    add(p_150733_, p_150734_.safeTake(itemstack.getCount(), i, p_150736_));
                }

                return true;
            }
            return false;

        }
    }

    public boolean overrideOtherStackedOnMe(ItemStack p_150742_, ItemStack p_150743_, Slot p_150744_, ClickAction p_150745_, Player p_150746_, SlotAccess p_150747_) {
        if (p_150745_ == ClickAction.SECONDARY && p_150744_.allowModification(p_150746_)) {
            if (p_150743_.getItem() == AritsuItems.BAIT.get() || p_150743_.isEmpty()) {
                if (p_150743_.isEmpty()) {
                    removeOne(p_150742_).ifPresent(p_150747_::set);
                } else {
                    p_150743_.shrink(add(p_150742_, p_150743_));
                }
                return true;
            }
            return false;

        } else {
            return false;
        }
    }

    private static int add(ItemStack p_150764_, ItemStack p_150765_) {
        if (!p_150765_.isEmpty() && p_150765_.getItem().canFitInsideContainerItems()) {
            CompoundTag compoundtag = p_150764_.getOrCreateTag();
            if (!compoundtag.contains("Bait")) {
                compoundtag.put("Bait", new ListTag());
            }

            int i = getContentWeight(p_150764_);
            int j = getWeight(p_150765_);
            int k = Math.min(p_150765_.getCount(), (64 - i) / j);
            if (k == 0) {
                return 0;
            } else {
                ListTag listtag = compoundtag.getList("Bait", 10);
                Optional<CompoundTag> optional = getMatchingItem(p_150765_, listtag);
                if (optional.isPresent()) {
                    CompoundTag compoundtag1 = optional.get();
                    ItemStack itemstack = ItemStack.of(compoundtag1);
                    itemstack.grow(k);
                    itemstack.save(compoundtag1);
                    listtag.remove(compoundtag1);
                    listtag.add(0, (Tag)compoundtag1);
                } else {
                    ItemStack itemstack1 = p_150765_.copy();
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

    private static Optional<CompoundTag> getMatchingItem(ItemStack p_150757_, ListTag p_150758_) {
        return p_150757_.is(AritsuItems.REINFORCED_FISHING_ROD.get()) ? Optional.empty() : p_150758_.stream().filter(CompoundTag.class::isInstance).map(CompoundTag.class::cast).filter((p_150755_) -> {
            return ItemStack.isSameItemSameTags(ItemStack.of(p_150755_), p_150757_);
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

    public Optional<TooltipComponent> getTooltipImage(ItemStack p_150775_) {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();
        getContents(p_150775_).forEach(nonnulllist::add);
        return Optional.of(new BundleTooltip(nonnulllist, getContentWeight(p_150775_)));
    }

    public void appendHoverText(ItemStack p_150749_, Level p_150750_, List<Component> p_150751_, TooltipFlag p_150752_) {
        p_150751_.add((new TranslatableComponent("item.minecraft.bundle.fullness", getContentWeight(p_150749_), 64)).withStyle(ChatFormatting.GRAY));
    }

    public void onDestroyed(ItemEntity p_150728_) {
        ItemUtils.onContainerDestroyed(p_150728_, getContents(p_150728_.getItem()));
    }
}
