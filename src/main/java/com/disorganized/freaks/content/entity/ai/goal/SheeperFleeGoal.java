package com.disorganized.freaks.content.entity.ai.goal;

import com.disorganized.freaks.content.entity.AbstractSheeperEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class SheeperFleeGoal<T extends LivingEntity> extends Goal {

	protected final AbstractSheeperEntity entity;
	protected final Class<T> targetType;
	protected final EntityNavigation navigation;
	protected final float fleeDistance;

	@Nullable protected T target;
	@Nullable protected Path fleePath;

	public SheeperFleeGoal(AbstractSheeperEntity entity, Class<T> targetType, float distance) {
		this.entity = entity;
		this.targetType = targetType;
		this.navigation = entity.getNavigation();
		this.fleeDistance = distance;
		this.setControls(EnumSet.of(Control.MOVE));
	}

	@Override
	public boolean canStart() {
		if (this.entity.getFuseSpeed() > 0) return false;

		Box box = this.entity.getBoundingBox().expand(this.fleeDistance, 3.0, this.fleeDistance);
		List<? extends T> possibleTargets = this.entity.getWorld().getEntitiesByClass(this.targetType, box, (livingEntity) -> true);
		Vec3d pos = this.entity.getPos();
		this.target = this.entity.getWorld().getClosestEntity(possibleTargets, TargetPredicate.DEFAULT, this.entity, pos.x, pos.y, pos.z);
		if (this.target == null) return false;

		Vec3d fleePosition = NoPenaltyTargeting.findFrom(this.entity, 8, 5, this.target.getPos());
		if (fleePosition == null) return false;

		this.fleePath = this.navigation.findPathTo(fleePosition.x, fleePosition.y, fleePosition.z, 0);
		return this.fleePath != null;

//		Vec3d fleePosition = NoPenaltyTargeting.findFrom(this.entity, 16, 7, this.target.getPos());
//		if (fleePosition == null) {
//			return false;
//		} else if (this.target.squaredDistanceTo(fleePosition.x, fleePosition.y, fleePosition.z) < this.target.squaredDistanceTo(this.entity)) {
//			return false;
//		} else {
//			this.fleePath = this.navigation.findPathTo(fleePosition.x, fleePosition.y, fleePosition.z, 0);
//			return this.fleePath != null;
//		}
	}

	@Override
	public boolean shouldContinue() {
		return !this.navigation.isIdle();
	}

	@Override
	public void start() {
		this.navigation.startMovingAlong(this.fleePath, 1);
	}

	@Override
	public void stop() {
		this.target = null;
	}

	@Override
	public void tick() {
		if (this.target == null) {
			this.entity.setFuseSpeed(-1);
		} else if (this.entity.squaredDistanceTo(this.target) > 16) {
			this.entity.setFuseSpeed(-1);
		} else if (!this.entity.getVisibilityCache().canSee(this.target)) {
			this.entity.setFuseSpeed(-1);
		} else {
			this.entity.setFuseSpeed(1);
		}
	}

	public boolean shouldRunEveryTick() {
		return true;
	}

}
