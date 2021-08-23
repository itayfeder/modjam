package net.aritsu.item;

import net.aritsu.registry.AritsuBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Wearable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class BackPackItem extends BlockItem implements Wearable {
    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        protected ItemStack execute(BlockSource p_40408_, ItemStack p_40409_) {
            return BackPackItem.dispenseBackpack(p_40408_, p_40409_) ? p_40409_ : super.execute(p_40408_, p_40409_);
        }
    };

    public static boolean dispenseBackpack(BlockSource p_40399_, ItemStack p_40400_) {
        BlockPos blockpos = p_40399_.getPos().relative(p_40399_.getBlockState().getValue(DispenserBlock.FACING));
        List<LivingEntity> list = p_40399_.getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(blockpos), EntitySelector.NO_SPECTATORS.and(new EntitySelector.MobCanWearArmorEntitySelector(p_40400_)));
        if (list.isEmpty()) {
            return false;
        } else {
            LivingEntity livingentity = list.get(0);
            EquipmentSlot equipmentslot = EquipmentSlot.CHEST;
            ItemStack itemstack = p_40400_.split(1);
            livingentity.setItemSlot(equipmentslot, itemstack);
            if (livingentity instanceof Mob) {
                ((Mob)livingentity).setDropChance(equipmentslot, 2.0F);
                ((Mob)livingentity).setPersistenceRequired();
            }

            return true;
        }
    }

    public InteractionResultHolder<ItemStack> use(Level p_40395_, Player p_40396_, InteractionHand p_40397_) {
        ItemStack itemstack = p_40396_.getItemInHand(p_40397_);
        EquipmentSlot equipmentslot = EquipmentSlot.CHEST;
        ItemStack itemstack1 = p_40396_.getItemBySlot(equipmentslot);
        if (itemstack1.isEmpty()) {
            p_40396_.setItemSlot(equipmentslot, itemstack.copy());
            if (!p_40395_.isClientSide()) {
                p_40396_.awardStat(Stats.ITEM_USED.get(this));
            }

            itemstack.setCount(0);
            return InteractionResultHolder.sidedSuccess(itemstack, p_40395_.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    public BackPackItem(Properties prop) {
        super(AritsuBlocks.BACKPACKBLOCK.get(), prop);
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return armorType == EquipmentSlot.CHEST;
    }
}
