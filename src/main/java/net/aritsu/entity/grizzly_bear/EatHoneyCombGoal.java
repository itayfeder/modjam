package net.aritsu.entity.grizzly_bear;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;

public class EatHoneyCombGoal extends Goal {

    private int eatTick = 0;
    private final GrizzlyBear bear;

    public EatHoneyCombGoal(GrizzlyBear bear) {
        this.bear = bear;
    }


    @Override
    public void start() {
        eatTick = 0;
        bear.setInSittingPose(true);
        bear.getNavigation().stop();
        bear.startHoneyClattered();
    }

    @Override
    public void stop() {
        bear.setInSittingPose(false);
        bear.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        if (eatTick > 10 * 20)
            stop();
        eatTick++;
        bear.getNavigation().stop();
    }

    @Override
    public boolean canContinueToUse() {
        return bear.isInSittingPose() && !bear.getItemInHand(InteractionHand.MAIN_HAND).isEmpty();
    }

    @Override
    public boolean canUse() {
        return !bear.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && bear.getRandom().nextInt(200) == 0;
    }
}