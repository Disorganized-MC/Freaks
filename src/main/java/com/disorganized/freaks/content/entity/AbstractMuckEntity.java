package com.disorganized.freaks.content.entity;

import com.disorganized.freaks.content.entity.ai.goal.ShootMudBubblesGoal;
import net.minecraft.block.Block;
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

	protected void initGoals() {
		this.targetSelector.add(1, new ShootMudBubblesGoal<>(this, PlayerEntity.class, 10, true, false, player -> Math.abs(player.getY() - this.getY()) <= 4.0));
		this.targetSelector.add(3, new ShootMudBubblesGoal<>(this, IronGolemEntity.class, true));
		super.initGoals();
	}

	@Override
	public void onDeath(DamageSource damageSource) {
		super.onDeath(damageSource);

		if (this.getWorld().isClient) return;
		if (!this.isDead()) return;

		Vec3d pos = this.getPos();
		System.out.println(pos);
		FallingBlockEntity fallingBlock = FallingBlockEntity.spawnFromBlock(this.getWorld(), this.getBlockPos(), this.getLivingBlock().getDefaultState());
//		FallingBlockEntity fallingBlock = new FallingBlockEntity(this.getWorld(), pos.x, pos.y + 3, pos.z, ModBlocks.LIVING_MUCK.getDefaultState());
		System.out.println(fallingBlock);
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
	public EntityDimensions getDimensions(EntityPose pose) {
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
