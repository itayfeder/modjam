package net.aritsu.item;

import net.aritsu.registry.AritsuItems;
import net.aritsu.util.ConfigData;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
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
    private static final Random rnd = new Random();
    private final Stage stage;

    public MarshmallowOnAStickItem(Properties properties, Stage stage) {
        super(properties);
        this.stage = stage;
    }

    private static boolean isActuallyLookingAt(Player p, BlockPos pos, double limit) {
        Vec3 playerVec = p.getPosition(1.0F).normalize();
        Vec3 distanceVec = new Vec3(pos.getX() - p.getX(), pos.getY() - p.getY(), pos.getZ() - p.getZ()).normalize();
        double dist = playerVec.distanceTo(distanceVec);
        return dist < limit;
    }

    /**
     * @param flag here is set to true when the held item is in the hotbar slot. this excludes offhand items
     */
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int anInt, boolean flag) {
        super.inventoryTick(stack, level, entity, anInt, flag);
        if (stack.getItem() instanceof MarshmallowOnAStickItem) {
            if (entity instanceof ServerPlayer player) {
                InteractionHand hand = null;
                if (player.getOffhandItem().equals(stack))
                    hand = InteractionHand.OFF_HAND;
                if (player.getMainHandItem().equals(stack))
                    hand = InteractionHand.MAIN_HAND;

                BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
                if (level.getBlockState(result.getBlockPos()).getBlock() instanceof CampfireBlock
                        && isActuallyLookingAt(player, result.getBlockPos(), 4)
                        && rnd.nextInt(ConfigData.cookMarshmallows/*seconds*/ * 4/*one second aka 20ticks*/) == 0) {
                    ItemStack newStack = stage == Stage.NORMAL ? AritsuItems.ROASTED_MARSHMALLOW_ON_A_STICK.get().getDefaultInstance() :
                            AritsuItems.BURNT_MARSHMALLOW_ON_A_STICK.get().getDefaultInstance();
                    if (hand != null)
                        player.setItemInHand(hand, newStack);
                }
            }
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        super.finishUsingItem(itemStack, level, livingEntity);
        if (livingEntity instanceof ServerPlayer) {
            ServerPlayer serverplayer = (ServerPlayer) livingEntity;
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, itemStack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
        }

        if (livingEntity instanceof Player && !((Player) livingEntity).getAbilities().instabuild) {
            itemStack.shrink(1);
        }

        return itemStack.isEmpty() ? new ItemStack(Items.STICK) : itemStack;
    }

    public enum Stage {
        NORMAL,
        ROASTED,
        BURNT
    }
}
