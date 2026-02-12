package com.disorganized.freaks.content.entity.ai.goal;

import com.disorganized.freaks.content.entity.MuckEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class MudBubbleAttackGoal extends Goal {

	protected final MobEntity mob;
	@Nullable protected LivingEntity target;
	protected final RangedAttackMob owner;

	protected int updateCountdownTicks;
	protected final double mobSpeed;
	protected int seenTargetTicks;
	protected final int intervalTicks;
	protected final float minShootRange;
	protected final float maxShootRange;
	protected final float squaredMaxShootRange;
	protected int currentAttackCount;
	protected final int attackCount;
	protected final int attackCooldown;

	public MudBubbleAttackGoal(RangedAttackMob mob, double mobSpeed, int intervalTicks, float minShootRange, float maxShootRange, int attackCount, int attackCooldown) {
		this.updateCountdownTicks = -1;
		this.owner = mob;
		this.mob = (MobEntity)mob;
		this.mobSpeed = mobSpeed;
		this.intervalTicks = intervalTicks;
		this.minShootRange = minShootRange;
		this.maxShootRange = maxShootRange;
		this.squaredMaxShootRange = maxShootRange * maxShootRange;
		this.attackCount = attackCount;
		this.attackCooldown = attackCooldown;
		this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
	}

	@Override
	public boolean shouldRunEveryTick() {
		return true;
	}

	@Override
	public boolean canStart() {
		LivingEntity target = this.mob.getTarget();
		if (target == null || !target.isAlive()) return false;

		this.target = target;
		return true;
	}

	@Override
	public boolean shouldContinue() {
		return this.canStart() || this.target != null && this.target.isAlive() && !this.mob.getNavigation().isIdle();
	}

	@Override
	public void stop() {
		this.target = null;
		this.seenTargetTicks = 0;
		this.updateCountdownTicks = -1;
	}

	@Override
	public void tick() {
		if (this.target == null) return;

		double distance = this.mob.squaredDistanceTo(this.target.getX(), this.target.getY(), this.target.getZ());
		if (distance < this.minShootRange) {
			this.target = null;
			return;
		}

		boolean canSeeTarget = this.mob.getVisibilityCache().canSee(this.target);
		if (canSeeTarget) {
			++this.seenTargetTicks;
		} else {
			this.seenTargetTicks = 0;
		}

		this.mob.getMoveControl().strafeTo(0, 0);
//		if (this.mob.getJumpControl() instanceof MuckEntity.MuckJumpControl control) {
//			control.stopJumping();
//		}
		this.mob.getNavigation().stop();

		this.mob.getLookControl().lookAt(this.target, 15, 15);
		if (--this.updateCountdownTicks == 0) {
			if (!canSeeTarget) return;

			this.currentAttackCount++;
			this.owner.shootAt(this.target, (float) 1 / this.currentAttackCount);
			if (this.currentAttackCount < this.attackCount) {
				this.updateCountdownTicks = this.attackCooldown;
			} else {
				this.currentAttackCount = 0;
				this.updateCountdownTicks = this.intervalTicks;
			}
		} else if (this.updateCountdownTicks < 0) {
			this.updateCountdownTicks = MathHelper.floor(MathHelper.lerp(Math.sqrt(distance) / (double)this.maxShootRange, this.intervalTicks, this.intervalTicks));
		}
	}

}
