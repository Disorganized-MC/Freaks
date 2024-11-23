package com.disorganized.freaks.content.entity;

import com.disorganized.freaks.content.entity.ai.goal.EatIronGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class SheeperEntity extends CreeperEntity {

	private static final TrackedData<Boolean> SHEARED = DataTracker.registerData(SheeperEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> FLAMING = DataTracker.registerData(SheeperEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	private EatIronGoal eatIronGoal;

	public SheeperEntity(EntityType<? extends CreeperEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(SHEARED, false);
		this.dataTracker.startTracking(FLAMING, false);
	}

	@Override
	protected void initGoals() {
		this.eatIronGoal = new EatIronGoal(this);
		this.goalSelector.add(1, new SwimGoal(this));
		this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
		this.goalSelector.add(2, new CreeperIgniteGoal(this));
		this.goalSelector.add(3, new FleeEntityGoal<>(this, PassiveEntity.class, 6.0F, 1.0, 1.2));
		this.goalSelector.add(3, new FleeEntityGoal<>(this, PlayerEntity.class, 6.0F, 1.0, 1.2));
		this.goalSelector.add(5, this.eatIronGoal);
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(6, new LookAroundGoal(this));
	}


	public boolean isSheared() {
		return this.dataTracker.get(SHEARED);
	}

	public void setSheared(boolean sheared) {
		this.dataTracker.set(SHEARED, sheared);
	}

	public boolean isShearable() {
		return this.isAlive() && !this.isSheared() && !this.isFlaming();
	}

	public boolean isFlaming() {
		return this.dataTracker.get(FLAMING);
	}

	public void setFlaming(boolean flaming) {
		this.dataTracker.set(FLAMING, flaming);
	}


	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("Sheared", this.isSheared());
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.setSheared(nbt.getBoolean("Sheared"));
	}


	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return CreeperEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25);
	}

}
