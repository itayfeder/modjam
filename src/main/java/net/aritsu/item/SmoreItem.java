package net.aritsu.item;

import net.aritsu.registry.AritsuEffects;
import net.aritsu.registry.AritsuItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SmoreItem extends Item {

    public SmoreItem(Properties properties) {
        super(properties);
    }

    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        super.finishUsingItem(itemStack, level, livingEntity);
        if (!level.isClientSide) {
            if (livingEntity instanceof Player player)
                player.addEffect(new MobEffectInstance(AritsuEffects.SUGAR_RUSH.get(), 600));
        }
        return itemStack;
    }
}

