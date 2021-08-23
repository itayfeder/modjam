package net.aritsu.item;

import net.aritsu.registry.AritsuItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class MarshmallowOnAStickItem extends Item {
    private final Stage stage;

    public MarshmallowOnAStickItem(Properties p_41383_, Stage stag) {
        super(p_41383_);
        this.stage = stag;
    }


    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);
        if (p_41406_ instanceof Player) {
            Player p = (Player)p_41406_;
            BlockHitResult result = getPlayerPOVHitResult(p_41405_, p, ClipContext.Fluid.SOURCE_ONLY);
            Random rnd = new Random();
            if (p_41405_.getBlockState(result.getBlockPos()).getBlock() instanceof CampfireBlock
                    && isActuallyLookingAt(p, result.getBlockPos(), 4)
                    && rnd.nextInt(50) == 10 && p_41408_) {

                if (p.getMainHandItem().getItem() instanceof MarshmallowOnAStickItem) {
                    switch (this.stage) {
                        case NORMAL:
                            p.setItemInHand(InteractionHand.MAIN_HAND, AritsuItems.ROASTED_MARSHMALLOW_ON_A_STICK.get().getDefaultInstance());
                            break;
                        case ROASTED:
                            p.setItemInHand(InteractionHand.MAIN_HAND, AritsuItems.BURNT_MARSHMALLOW_ON_A_STICK.get().getDefaultInstance());
                            break;
                        case BURNT:
                            break;
                    }
                }
                else if (p.getOffhandItem().getItem() instanceof MarshmallowOnAStickItem) {
                    switch (this.stage) {
                        case NORMAL:
                            p.setItemInHand(InteractionHand.OFF_HAND, AritsuItems.ROASTED_MARSHMALLOW_ON_A_STICK.get().getDefaultInstance());
                            break;
                        case ROASTED:
                            p.setItemInHand(InteractionHand.OFF_HAND, AritsuItems.BURNT_MARSHMALLOW_ON_A_STICK.get().getDefaultInstance());
                            break;
                        case BURNT:
                            break;
                    }
                }
            }
        }
    }

    private static boolean isActuallyLookingAt(Player p, BlockPos pos, double limit) {
        Vec3 playerVec = p.getPosition(1.0F).normalize();
        Vec3 distanceVec = new Vec3(pos.getX()-p.getX(), pos.getY()-p.getY(), pos.getZ()-p.getZ()).normalize();
        double dist = playerVec.distanceTo(distanceVec);
        return dist < limit;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack p_41409_, Level p_41410_, LivingEntity p_41411_) {
        if (p_41411_ instanceof ServerPlayer) {
            ServerPlayer serverplayer = (ServerPlayer)p_41411_;
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, p_41409_);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (p_41411_ instanceof Player && !((Player)p_41411_).getAbilities().instabuild) {
            p_41409_.shrink(1);
        }

        return p_41409_.isEmpty() ? new ItemStack(Items.STICK) : p_41409_;
    }

    public enum Stage {
        NORMAL,
        ROASTED,
        BURNT
    }
}
