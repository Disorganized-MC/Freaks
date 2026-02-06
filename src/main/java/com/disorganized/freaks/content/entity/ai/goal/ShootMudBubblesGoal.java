package com.disorganized.freaks.content.entity.ai.goal;

import com.disorganized.freaks.content.entity.MudBubbleEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;

public class ShootMudBubblesGoal<T extends MobEntity> extends Goal {

	private static final int WINDUP_DURATION = 20;
	private static final int DURATION = WINDUP_DURATION + 15;
	private static final float KICK_SPEED = 0.4f;

	public final T entity;
	public int progress = 0;

	public ShootMudBubblesGoal(T entity) {
		this.entity = entity;
		this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
	}

	@Override
	public boolean shouldRunEveryTick() {
		return true;
	}

	@Override
	public boolean shouldContinue() {
		return this.canStart() || !this.entity.getNavigation().isIdle();
	}

	@Override
	public boolean canStart() {
		if (this.entity != null && this.entity.getTarget() != null) {
			return this.entity.distanceTo(this.entity.getTarget()) > 3;
		}
		return false;
	}

	@Override
	public void start() {
		super.start();
		this.progress = DURATION;
	}

	@Override
	public void tick() {
		this.progress--;
		if (this.progress > DURATION - WINDUP_DURATION + 1) return;

		this.entity.getMoveControl().strafeTo(-1, 0);

		if (this.progress < 0) {
			this.stop();
			return;
		}

		if (this.progress % 5 == 0) {
			this.shoot();
		}
	}

	private boolean shoot() {
		if (this.entity.getTarget() == null) return false;
		if (!this.entity.getTarget().isAlive()) return false;

		World world = this.entity.getWorld();
		if (world.isClient()) return true;

		System.out.println(this.progress);

		MudBubbleEntity bubble = new MudBubbleEntity(world, this.entity);
		bubble.setPos(this.entity.getX(), this.entity.getEyeY(), this.entity.getZ());
		bubble.setTarget(this.entity.getTarget());

		Vec3d forward = this.entity.getRotationVec(1).normalize();
		Vec3d kick = forward.multiply(KICK_SPEED).add(
			(this.entity.getRandom().nextFloat() - 0.5f) * KICK_SPEED,
			(this.entity.getRandom().nextFloat() - 0.5f) * KICK_SPEED * 0.5f,
			(this.entity.getRandom().nextFloat() - 0.5f) * KICK_SPEED
		);
		bubble.setVelocity(kick);

		world.spawnEntity(bubble);
		this.entity.playSound(SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 1, 0.8f + this.entity.getRandom().nextFloat() * 0.4f);
		return true;
	}

}
