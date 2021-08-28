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
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class CampfireGrillBlockEntity extends BlockEntity implements Clearable {
    public static final BlockEntityTicker<CampfireGrillBlockEntity> CLIENTTICKER = (level, pos, state, be) -> be.clientTick(level, pos, state, be);
    public static final BlockEntityTicker<CampfireGrillBlockEntity> SERVERTICKER = (level, pos, state, be) -> be.serverTick(level, pos, state, be);
    private final NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
    private final int[] cookingProgress = new int[5];
    private final int[] cookingTime = new int[4];

    public CampfireGrillBlockEntity(BlockPos pos, BlockState state) {
        super(AritsuBlockEntities.CAMPFIRE_GRILL.get(), pos, state);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state, CampfireGrillBlockEntity grillBlockEntity) {
        if (level.getBlockState(pos.below()).getBlock() instanceof CampfireBlock) {
            if (level.getBlockState(pos.below()).getValue(CampfireBlock.LIT)) {
                cook(level, pos, state, grillBlockEntity);
            } else {
                cooldown(level, pos, state, grillBlockEntity);
            }
        }

    }

    public void cook(Level level, BlockPos pos, BlockState state, CampfireGrillBlockEntity grillBlockEntity) {
        boolean flag = false;

        for (int i = 0; i < grillBlockEntity.items.size() - 1; ++i) {
            ItemStack itemstack = grillBlockEntity.items.get(i);
            if (!itemstack.isEmpty()) {
                flag = true;
                int cookTimer = grillBlockEntity.cookingProgress[i]++;
                if (cookTimer >= grillBlockEntity.cookingTime[i]) {
                    Container container = new SimpleContainer(itemstack);
                    ItemStack cookedItem = level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, container, level).map((p_155305_) -> p_155305_.assemble(container)).orElse(itemstack);
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), cookedItem);
                    grillBlockEntity.items.set(i, ItemStack.EMPTY);
                    level.sendBlockUpdated(pos, state, state, 3);
                }
            }
        }

        ItemStack kettleSlotItem = grillBlockEntity.items.get(4);
        if (!kettleSlotItem.isEmpty()) {
            flag = true;
            int timer = grillBlockEntity.cookingProgress[4]++;
            if (timer >= 600) {
                ItemStack hotKettle = AritsuItems.BOILING_KETTLE.get().getDefaultInstance();
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), hotKettle);
                grillBlockEntity.items.set(4, ItemStack.EMPTY);
                level.sendBlockUpdated(pos, state, state, 3);
            }
        }

        if (flag) {
            setChanged(level, pos, state);
        }
    }


    public void cooldown(Level level, BlockPos pos, BlockState state, CampfireGrillBlockEntity grillBlockEntity) {
        boolean flag = false;

        for (int i = 0; i < grillBlockEntity.items.size() - 1; ++i) {
            if (grillBlockEntity.cookingProgress[i] > 0) {
                flag = true;
                grillBlockEntity.cookingProgress[i] = Mth.clamp(grillBlockEntity.cookingProgress[i] - 2, 0, grillBlockEntity.cookingTime[i]);
            }
        }

        if (grillBlockEntity.cookingProgress[4] > 0) {
            flag = true;
            grillBlockEntity.cookingProgress[4] = Mth.clamp(grillBlockEntity.cookingProgress[4] - 2, 0, 600);
        }

        if (flag) {
            setChanged(level, pos, state);
        }

    }

    public void clientTick(Level level, BlockPos pos, BlockState state, CampfireGrillBlockEntity grillBlockEntity) {
        if (level.getBlockState(pos.below()).getBlock() instanceof CampfireBlock) {
            if (level.getBlockState(pos.below()).getValue(CampfireBlock.LIT)) {
                Random random = level.random;
                if (random.nextFloat() < 0.11F) {
                    for (int i = 0; i < random.nextInt(2) + 2; ++i) {
                        CampfireBlock.makeParticles(level, pos, level.getBlockState(pos.below()).getValue(CampfireBlock.SIGNAL_FIRE), false);
                    }
                }

                int l = level.getBlockState(pos.below()).getValue(CampfireBlock.FACING).get2DDataValue();

                for (int j = 0; j < grillBlockEntity.items.size() - 1; ++j) {
                    if (!grillBlockEntity.items.get(j).isEmpty() && random.nextFloat() < 0.2F) {
                        Direction direction = Direction.from2DDataValue(Math.floorMod(j + l, 4));
                        float f = 0.3125F;
                        double d0 = (double) pos.getX() + 0.5D - (double) ((float) direction.getStepX() * 0.3125F) + (double) ((float) direction.getClockWise().getStepX() * 0.3125F);
                        double d1 = (double) pos.getY() + 0.0234375D;
                        double d2 = (double) pos.getZ() + 0.5D - (double) ((float) direction.getStepZ() * 0.3125F) + (double) ((float) direction.getClockWise().getStepZ() * 0.3125F);

                        for (int k = 0; k < 4; ++k) {
                            level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 5.0E-4D, 0.0D);
                        }
                    }
                }

                if (!grillBlockEntity.items.get(4).isEmpty() && random.nextFloat() < 0.2F) {

                    for (int k = 0; k < 4; ++k) {
                        level.addParticle(ParticleTypes.CLOUD, 0.45D, 0.30D, 0.5D, 0.0D, 5.0E-4D, 0.0D);
                    }
                }
            }
        }

    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items.clear();
        ContainerHelper.loadAllItems(compoundTag, this.items);
        if (compoundTag.contains("CookingTimes", 11)) {
            int[] allItems = compoundTag.getIntArray("CookingTimes");
            System.arraycopy(allItems, 0, this.cookingProgress, 0, Math.min(this.cookingTime.length, allItems.length));
        }

        if (compoundTag.contains("CookingTotalTimes", 11)) {
            int[] allItems = compoundTag.getIntArray("CookingTotalTimes");
            System.arraycopy(allItems, 0, this.cookingTime, 0, Math.min(this.cookingTime.length, allItems.length));
        }

    }

    public CompoundTag save(CompoundTag compoundTag) {
        this.saveMetadataAndItems(compoundTag);
        compoundTag.putIntArray("CookingTimes", this.cookingProgress);
        compoundTag.putIntArray("CookingTotalTimes", this.cookingTime);
        return compoundTag;
    }


    private CompoundTag saveMetadataAndItems(CompoundTag compoundTag) {
        super.save(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, this.items, true);
        return compoundTag;
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

    public boolean placeFood(ItemStack stack, int cookingTime) {
        for (int i = 0; i < this.items.size() - 1; ++i) {
            ItemStack itemstack = this.items.get(i);
            if (itemstack.isEmpty()) {
                this.cookingTime[i] = cookingTime;
                this.cookingProgress[i] = 0;
                this.items.set(i, stack.split(1));
                this.markUpdated();
                return true;
            }
        }

        return false;
    }

    public boolean placeKettle(ItemStack stack) {
        if (this.items.get(4).isEmpty() && stack.is(AritsuItems.WATER_KETTLE.get())) {
            this.cookingProgress[4] = 0;
            this.items.set(4, stack.split(1));
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
}
