package net.aritsu.blockentity;

import net.aritsu.registry.AritsuBlockEntities;
import net.aritsu.registry.AritsuItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class CampfireGrillBlockEntity extends BlockEntity implements Clearable {
    private final NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
    private final int[] cookingProgress = new int[5];
    private final int[] cookingTime = new int[4];

    public CampfireGrillBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(AritsuBlockEntities.CAMPFIRE_GRILL.get(), p_155229_, p_155230_);
    }

    public static void cookTick(Level p_155307_, BlockPos p_155308_, BlockState p_155309_, CampfireGrillBlockEntity p_155310_) {
        if (p_155307_.getBlockState(p_155308_.below()).getBlock() instanceof CampfireBlock) {
            if (p_155307_.getBlockState(p_155308_.below()).getValue(CampfireBlock.LIT)) {
                cook(p_155307_, p_155308_, p_155309_, p_155310_);
            } else {
                cooldown(p_155307_, p_155308_, p_155309_, p_155310_);
            }
        }

    }

    public static void cook(Level p_155307_, BlockPos p_155308_, BlockState p_155309_, CampfireGrillBlockEntity p_155310_) {
        boolean flag = false;

        for (int i = 0; i < p_155310_.items.size() - 1; ++i) {
            ItemStack itemstack = p_155310_.items.get(i);
            if (!itemstack.isEmpty()) {
                flag = true;
                int j = p_155310_.cookingProgress[i]++;
                if (p_155310_.cookingProgress[i] >= p_155310_.cookingTime[i]) {
                    Container container = new SimpleContainer(itemstack);
                    ItemStack itemstack1 = p_155307_.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, container, p_155307_).map((p_155305_) -> {
                        return p_155305_.assemble(container);
                    }).orElse(itemstack);
                    Containers.dropItemStack(p_155307_, p_155308_.getX(), p_155308_.getY(), p_155308_.getZ(), itemstack1);
                    p_155310_.items.set(i, ItemStack.EMPTY);
                    p_155307_.sendBlockUpdated(p_155308_, p_155309_, p_155309_, 3);
                }
            }
        }

        ItemStack itemstack = p_155310_.items.get(4);
        if (!itemstack.isEmpty()) {
            flag = true;
            int j = p_155310_.cookingProgress[4]++;
            if (p_155310_.cookingProgress[4] >= 600) {
                Container container = new SimpleContainer(itemstack);
                ItemStack itemstack1 = AritsuItems.BOILING_KETTLE.get().getDefaultInstance();
                Containers.dropItemStack(p_155307_, p_155308_.getX(), p_155308_.getY(), p_155308_.getZ(), itemstack1);
                p_155310_.items.set(4, ItemStack.EMPTY);
                p_155307_.sendBlockUpdated(p_155308_, p_155309_, p_155309_, 3);
            }
        }

        if (flag) {
            setChanged(p_155307_, p_155308_, p_155309_);
        }
    }


    public static void cooldown(Level p_155314_, BlockPos p_155315_, BlockState p_155316_, CampfireGrillBlockEntity p_155317_) {
        boolean flag = false;

        for (int i = 0; i < p_155317_.items.size() - 1; ++i) {
            if (p_155317_.cookingProgress[i] > 0) {
                flag = true;
                p_155317_.cookingProgress[i] = Mth.clamp(p_155317_.cookingProgress[i] - 2, 0, p_155317_.cookingTime[i]);
            }
        }

        if (p_155317_.cookingProgress[4] > 0) {
            flag = true;
            p_155317_.cookingProgress[4] = Mth.clamp(p_155317_.cookingProgress[4] - 2, 0, 600);
        }

        if (flag) {
            setChanged(p_155314_, p_155315_, p_155316_);
        }

    }

    public static void particleTick(Level p_155319_, BlockPos p_155320_, BlockState p_155321_, CampfireGrillBlockEntity p_155322_) {
        if (p_155319_.getBlockState(p_155320_.below()).getBlock() instanceof CampfireBlock) {
            if (p_155319_.getBlockState(p_155320_.below()).getValue(CampfireBlock.LIT)) {
                Random random = p_155319_.random;
                if (random.nextFloat() < 0.11F) {
                    for (int i = 0; i < random.nextInt(2) + 2; ++i) {
                        CampfireBlock.makeParticles(p_155319_, p_155320_, p_155319_.getBlockState(p_155320_.below()).getValue(CampfireBlock.SIGNAL_FIRE), false);
                    }
                }

                int l = p_155319_.getBlockState(p_155320_.below()).getValue(CampfireBlock.FACING).get2DDataValue();

                for (int j = 0; j < p_155322_.items.size() - 1; ++j) {
                    if (!p_155322_.items.get(j).isEmpty() && random.nextFloat() < 0.2F) {
                        Direction direction = Direction.from2DDataValue(Math.floorMod(j + l, 4));
                        float f = 0.3125F;
                        double d0 = (double) p_155320_.getX() + 0.5D - (double) ((float) direction.getStepX() * 0.3125F) + (double) ((float) direction.getClockWise().getStepX() * 0.3125F);
                        double d1 = (double) p_155320_.getY() + 0.0234375D;
                        double d2 = (double) p_155320_.getZ() + 0.5D - (double) ((float) direction.getStepZ() * 0.3125F) + (double) ((float) direction.getClockWise().getStepZ() * 0.3125F);

                        for (int k = 0; k < 4; ++k) {
                            p_155319_.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 5.0E-4D, 0.0D);
                        }
                    }
                }

                if (!p_155322_.items.get(4).isEmpty() && random.nextFloat() < 0.2F) {

                    for (int k = 0; k < 4; ++k) {
                        p_155319_.addParticle(ParticleTypes.CLOUD, 0.45D, 0.30D, 0.5D, 0.0D, 5.0E-4D, 0.0D);
                    }
                }
            }
        }

    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public void load(CompoundTag p_155312_) {
        super.load(p_155312_);
        this.items.clear();
        ContainerHelper.loadAllItems(p_155312_, this.items);
        if (p_155312_.contains("CookingTimes", 11)) {
            int[] aint = p_155312_.getIntArray("CookingTimes");
            System.arraycopy(aint, 0, this.cookingProgress, 0, Math.min(this.cookingTime.length, aint.length));
        }

        if (p_155312_.contains("CookingTotalTimes", 11)) {
            int[] aint1 = p_155312_.getIntArray("CookingTotalTimes");
            System.arraycopy(aint1, 0, this.cookingTime, 0, Math.min(this.cookingTime.length, aint1.length));
        }

    }

    public CompoundTag save(CompoundTag p_59060_) {
        this.saveMetadataAndItems(p_59060_);
        p_59060_.putIntArray("CookingTimes", this.cookingProgress);
        p_59060_.putIntArray("CookingTotalTimes", this.cookingTime);
        return p_59060_;
    }


    private CompoundTag saveMetadataAndItems(CompoundTag p_59064_) {
        super.save(p_59064_);
        ContainerHelper.saveAllItems(p_59064_, this.items, true);
        return p_59064_;
    }

    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {

        this.load(pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {

        return save(new CompoundTag());
    }

    // calls readFromNbt by default. no need to add anything in here
    @Override
    public void handleUpdateTag(CompoundTag tag) {

        super.handleUpdateTag(tag);
    }

    public Optional<CampfireCookingRecipe> getCookableRecipe(ItemStack p_59052_) {
        return this.items.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SimpleContainer(p_59052_), this.level);
    }

    public boolean placeFood(ItemStack p_59054_, int p_59055_) {
        for (int i = 0; i < this.items.size() - 1; ++i) {
            ItemStack itemstack = this.items.get(i);
            if (itemstack.isEmpty()) {
                this.cookingTime[i] = p_59055_;
                this.cookingProgress[i] = 0;
                this.items.set(i, p_59054_.split(1));
                this.markUpdated();
                return true;
            }
        }

        return false;
    }

    public boolean placeKettle(ItemStack p_59054_) {
        if (this.items.get(4).isEmpty() && p_59054_.is(AritsuItems.WATER_KETTLE.get())) {
            this.cookingProgress[4] = 0;
            this.items.set(4, p_59054_.split(1));
            this.markUpdated();
            return true;
        }

        return false;
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public void clearContent() {
        this.items.clear();
    }

    public void dowse() {
        if (this.level != null) {
            this.markUpdated();
        }

    }
}
