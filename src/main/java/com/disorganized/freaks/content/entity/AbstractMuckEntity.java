package com.disorganized.freaks.content.entity;

import com.disorganized.freaks.content.entity.ai.goal.ShootMudBubblesGoal;
import net.minecraft.block.Block;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class AbstractMuckEntity extends SlimeEntity {

	public AbstractMuckEntity(EntityType<? extends AbstractMuckEntity> type, World world) {
		super(type, world);
	}

	@Override
	public void setComponents(ComponentMapImpl components) {

	}

	@Override
	public ComponentMapImpl getMutableComponents() {
		return new ComponentMapImpl(ComponentMap.EMPTY);
	}

	protected void initGoals() {
		this.goalSelector.add(1, new SwimmingGoal(this));
		this.goalSelector.add(2, new FaceTowardTargetGoal(this));
		this.goalSelector.add(3, new RandomLookGoal(this));
		this.goalSelector.add(5, new MoveGoal(this));

		this.targetSelector.add(1, new ShootMudBubblesGoal<>(this, PlayerEntity.class, 10, true, false, player -> Math.abs(player.getY() - this.getY()) <= 4.0));
		this.targetSelector.add(3, new ShootMudBubblesGoal<>(this, IronGolemEntity.class, true));
	}

	@Override
	public void onDeath(DamageSource damageSource) {
		super.onDeath(damageSource);

		if (this.getWorld().isClient) return;
		if (!this.isDead()) return;

		FallingBlockEntity fallingBlock = FallingBlockEntity.spawnFromBlock(this.getWorld(), this.getBlockPos(), this.getLivingBlock().getDefaultState());
		((SturdyFallingBlockEntity)fallingBlock).setSturdy(true);
		fallingBlock.setVelocity(this.getVelocity().multiply(1.5));
		fallingBlock.velocityModified = true;
	}

	@Override
	public void remove(Entity.RemovalReason reason) {
		this.setRemoved(reason);
		this.brain.forgetAll();
	}

	public abstract float getKnockbackMultiplier();

	public abstract Block getLivingBlock();

	@Override
	public void takeKnockback(double strength, double x, double z) {
		super.takeKnockback(strength * getKnockbackMultiplier(), x, z);
	}

	@Override
	public EntityDimensions getBaseDimensions(EntityPose pose) {
		return EntityDimensions.fixed(this.getSize(), this.getSize()).scaled(this.getSize());
	}

	@Override
	protected int getTicksUntilNextJump() {
		return this.random.nextInt(10) + 10;
	}

	@Override
	protected boolean canAttack() {
		return this.canMoveVoluntarily();
	}

	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return HostileEntity.createHostileAttributes();
	}

}
