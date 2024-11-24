package com.disorganized.freaks.content.entity.ai.goal;

import com.disorganized.freaks.registry.ModBlockTags;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.EnumSet;

public class EatIronGoal extends Goal {

	public final AnimationState animationState = new AnimationState();

	private static final int MAX_TIMER = 40;

	private final MobEntity mob;
	private final World world;
	private int timer;

	public EatIronGoal(MobEntity mob) {
		this.mob = mob;
		this.world = mob.getWorld();
		this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
	}

	public boolean canStart() {
		if (this.mob.getRandom().nextInt(1000) != 0) return false;
		BlockPos pos = this.mob.getSteppingPos();
		return this.world.getBlockState(pos).isIn(ModBlockTags.SHEEPER_PASTURES);
	}

	public void start() {
		System.out.println("test");
		this.timer = this.getTickCount(MAX_TIMER);
		this.reset();
		this.mob.getNavigation().stop();
	}

	public void stop() {
		this.timer = 0;
	}

	public boolean shouldContinue() {
		return this.timer > 0;
	}

	@Override
	public void tick() {
		this.timer = Math.max(0, this.timer - 1);
		if (this.timer != this.getTickCount(4)) return;

		BlockPos pos = this.mob.getBlockPos().down();
		if (!this.world.getBlockState(pos).isIn(ModBlockTags.SHEEPER_PASTURES)) return;
		this.mob.onEatingGrass();
	}

	public void reset() {
		this.timer = MAX_TIMER;
	}

	public float getHeadAngle(float delta, float pitch) {
		int end = MAX_TIMER - 4;
		if (timer > 4 && timer <= end) {
			float f = ((float)(timer - 4) - delta) / 32.0F;
			return 0.62831855F + 0.21991149F * MathHelper.sin(f * 28.7F);
		} else {
			return timer > 0 ? 0.62831855F : pitch * 0.017453292F;
		}
	}

	public float getNeckAngle(float delta) {
		int end = MAX_TIMER - 4;
		if (timer <= 0) {
			return 0.0F;
		} else if (timer >= 4 && timer <= end) {
			return 1.0F;
		} else {
			return timer < 4 ? ((float)timer - delta) / 4 : -((float)(timer - MAX_TIMER) - delta) / 4;
		}
	}

}
