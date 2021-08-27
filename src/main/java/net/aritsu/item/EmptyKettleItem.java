package net.aritsu.item;

import net.aritsu.registry.AritsuItems;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;

public class EmptyKettleItem extends Item {
    public EmptyKettleItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResult useOn(UseOnContext p_40581_) {
        BlockPlaceContext context = new BlockPlaceContext(p_40581_);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(p_40581_.getLevel(), p_40581_.getPlayer(), ClipContext.Fluid.SOURCE_ONLY);
        if (p_40581_.getLevel().getBlockState(blockhitresult.getBlockPos()).getBlock() == Blocks.WATER) {
            p_40581_.getPlayer().setItemInHand(p_40581_.getHand(), AritsuItems.WATER_KETTLE.get().getDefaultInstance());
            p_40581_.getLevel().setBlock(context.getClickedPos(), Blocks.AIR.defaultBlockState(), 3);
            return InteractionResult.SUCCESS;
        } else {
            return super.useOn(p_40581_);
        }
    }
}
