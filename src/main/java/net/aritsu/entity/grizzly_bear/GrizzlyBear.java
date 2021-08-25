package net.aritsu.entity.grizzly_bear;

import net.aritsu.registry.AritsuEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

public class GrizzlyBear extends Animal implements NeutralMob {
    private static final EntityDataAccessor<Boolean> DATA_STANDING_ID = SynchedEntityData.defineId(GrizzlyBear.class, EntityDataSerializers.BOOLEAN);
    private static final float STAND_ANIMATION_TICKS = 6.0F;
    private float clientSideStandAnimationO;
    private float clientSideStandAnimation;
    private int warningSoundTicks;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private int remainingPersistentAngerTime;
    private UUID persistentAngerTarget;

    public GrizzlyBear(EntityType<? extends GrizzlyBear> p_29519_, Level p_29520_) {
        super(p_29519_, p_29520_);
    }

    public AgeableMob getBreedOffspring(ServerLevel p_149005_, AgeableMob p_149006_) {
        return AritsuEntities.GRIZZLY_BEAR.get().create(p_149005_);
    }

    public boolean isFood(ItemStack p_29565_) {
        return false;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new GrizzlyBear.GrizzlyBearMeleeAttackGoal());
        this.goalSelector.addGoal(1, new GrizzlyBear.GrizzlyBearPanicGoal());
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new GrizzlyBear.GrizzlyBearHurtByTargetGoal());
        this.targetSelector.addGoal(2, new GrizzlyBear.GrizzlyBearAttackPlayersGoal());
        this.targetSelector.addGoal(2, new GrizzlyBear.GrizzlyBearAttackFoodHolderGoal());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Fox.class, 10, true, true, (Predicate<LivingEntity>)null));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.FOLLOW_RANGE, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    public static boolean checkGrizzlyBearSpawnRules(EntityType<GrizzlyBear> p_29550_, LevelAccessor p_29551_, MobSpawnType p_29552_, BlockPos p_29553_, Random p_29554_) {
        Optional<ResourceKey<Biome>> optional = p_29551_.getBiomeName(p_29553_);
        if (!Objects.equals(optional, Optional.of(Biomes.FROZEN_OCEAN)) && !Objects.equals(optional, Optional.of(Biomes.DEEP_FROZEN_OCEAN))) {
            return checkAnimalSpawnRules(p_29550_, p_29551_, p_29552_, p_29553_, p_29554_);
        } else {
            return p_29551_.getRawBrightness(p_29553_, 0) > 8 && p_29551_.getBlockState(p_29553_.below()).is(Blocks.ICE);
        }
    }

    public void readAdditionalSaveData(CompoundTag p_29541_) {
        super.readAdditionalSaveData(p_29541_);
        this.readPersistentAngerSaveData(this.level, p_29541_);
    }

    public void addAdditionalSaveData(CompoundTag p_29548_) {
        super.addAdditionalSaveData(p_29548_);
        this.addPersistentAngerSaveData(p_29548_);
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    public void setRemainingPersistentAngerTime(int p_29543_) {
        this.remainingPersistentAngerTime = p_29543_;
    }

    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    public void setPersistentAngerTarget(@Nullable UUID p_29539_) {
        this.persistentAngerTarget = p_29539_;
    }

    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? SoundEvents.POLAR_BEAR_AMBIENT_BABY : SoundEvents.POLAR_BEAR_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_29559_) {
        return SoundEvents.POLAR_BEAR_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.POLAR_BEAR_DEATH;
    }

    protected void playStepSound(BlockPos p_29545_, BlockState p_29546_) {
        this.playSound(SoundEvents.POLAR_BEAR_STEP, 0.15F, 1.0F);
    }

    protected void playWarningSound() {
        if (this.warningSoundTicks <= 0) {
            this.playSound(SoundEvents.POLAR_BEAR_WARNING, 1.0F, this.getVoicePitch());
            this.warningSoundTicks = 40;
        }

    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_STANDING_ID, false);
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.clientSideStandAnimation != this.clientSideStandAnimationO) {
                this.refreshDimensions();
            }

            this.clientSideStandAnimationO = this.clientSideStandAnimation;
            if (this.isStanding()) {
                this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation + 1.0F, 0.0F, 6.0F);
            } else {
                this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation - 1.0F, 0.0F, 6.0F);
            }
        }

        if (this.warningSoundTicks > 0) {
            --this.warningSoundTicks;
        }

        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }

    }

    public EntityDimensions getDimensions(Pose p_29531_) {
        if (this.clientSideStandAnimation > 0.0F) {
            float f = this.clientSideStandAnimation / 6.0F;
            float f1 = 1.0F + f;
            return super.getDimensions(p_29531_).scale(1.0F, f1);
        } else {
            return super.getDimensions(p_29531_);
        }
    }

    public boolean doHurtTarget(Entity p_29522_) {
        boolean flag = p_29522_.hurt(DamageSource.mobAttack(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (flag) {
            this.doEnchantDamageEffects(this, p_29522_);
        }

        return flag;
    }

    public boolean isStanding() {
        return this.entityData.get(DATA_STANDING_ID);
    }

    public void setStanding(boolean p_29568_) {
        this.entityData.set(DATA_STANDING_ID, p_29568_);
    }

    public float getStandingAnimationScale(float p_29570_) {
        return Mth.lerp(p_29570_, this.clientSideStandAnimationO, this.clientSideStandAnimation) / 6.0F;
    }

    protected float getWaterSlowDown() {
        return 0.98F;
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_29533_, DifficultyInstance p_29534_, MobSpawnType p_29535_, @Nullable SpawnGroupData p_29536_, @Nullable CompoundTag p_29537_) {
        if (p_29536_ == null) {
            p_29536_ = new AgeableMob.AgeableMobGroupData(1.0F);
        }

        return super.finalizeSpawn(p_29533_, p_29534_, p_29535_, p_29536_, p_29537_);
    }

    class GrizzlyBearAttackPlayersGoal extends NearestAttackableTargetGoal<Player> {
        public GrizzlyBearAttackPlayersGoal() {
            super(GrizzlyBear.this, Player.class, 20, true, true, (Predicate<LivingEntity>)null);
        }

        public boolean canUse() {
            if (GrizzlyBear.this.isBaby()) {
                return false;
            } else {
                if (super.canUse()) {
                    for(GrizzlyBear GrizzlyBear : GrizzlyBear.this.level.getEntitiesOfClass(GrizzlyBear.class, GrizzlyBear.this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D))) {
                        if (GrizzlyBear.isBaby()) {
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        protected double getFollowDistance() {
            return super.getFollowDistance() * 0.5D;
        }
    }

    class GrizzlyBearAttackFoodHolderGoal extends NearestAttackableTargetGoal<Player> {
        public GrizzlyBearAttackFoodHolderGoal() {
            super(GrizzlyBear.this, Player.class, 20, true, true, (Predicate<LivingEntity>)null);
        }

        public boolean canUse() {
            if (GrizzlyBear.this.isBaby()) {
                return false;
            } else {
                if (super.canUse()) {
                    for(Player player : GrizzlyBear.this.level.getEntitiesOfClass(Player.class, GrizzlyBear.this.getBoundingBox().inflate(16.0D, 8.0D, 16.0D))) {
                        if (player.getMainHandItem().getItem().getFoodProperties() != null || player.getOffhandItem().getItem().getFoodProperties() != null) {
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        protected double getFollowDistance() {
            return super.getFollowDistance() * 2D;
        }
    }

    class GrizzlyBearHurtByTargetGoal extends HurtByTargetGoal {
        public GrizzlyBearHurtByTargetGoal() {
            super(GrizzlyBear.this);
        }

        public void start() {
            super.start();
            if (GrizzlyBear.this.isBaby()) {
                this.alertOthers();
                this.stop();
            }

        }

        protected void alertOther(Mob p_29580_, LivingEntity p_29581_) {
            if (p_29580_ instanceof GrizzlyBear && !p_29580_.isBaby()) {
                super.alertOther(p_29580_, p_29581_);
            }

        }
    }

    class GrizzlyBearMeleeAttackGoal extends MeleeAttackGoal {
        public GrizzlyBearMeleeAttackGoal() {
            super(GrizzlyBear.this, 1.25D, true);
        }

        protected void checkAndPerformAttack(LivingEntity p_29589_, double p_29590_) {
            double d0 = this.getAttackReachSqr(p_29589_);
            if (p_29590_ <= d0 && this.isTimeToAttack()) {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(p_29589_);
                GrizzlyBear.this.setStanding(false);
            } else if (p_29590_ <= d0 * 2.0D) {
                if (this.isTimeToAttack()) {
                    GrizzlyBear.this.setStanding(false);
                    this.resetAttackCooldown();
                }

                if (this.getTicksUntilNextAttack() <= 10) {
                    GrizzlyBear.this.setStanding(true);
                    GrizzlyBear.this.playWarningSound();
                }
            } else {
                this.resetAttackCooldown();
                GrizzlyBear.this.setStanding(false);
            }

        }

        public void stop() {
            GrizzlyBear.this.setStanding(false);
            super.stop();
        }

        protected double getAttackReachSqr(LivingEntity p_29587_) {
            return (double)(4.0F + p_29587_.getBbWidth());
        }
    }

    class GrizzlyBearPanicGoal extends PanicGoal {
        public GrizzlyBearPanicGoal() {
            super(GrizzlyBear.this, 2.0D);
        }

        public boolean canUse() {
            return !GrizzlyBear.this.isBaby() && !GrizzlyBear.this.isOnFire() ? false : super.canUse();
        }
    }
}
