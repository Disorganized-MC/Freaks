package com.disorganized.freaks.content.entity.ai.goal;

import com.disorganized.freaks.registry.ModBlockTags;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;

public class EatIronGoal extends Goal {

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
		if (this.mob.getRandom().nextInt(this.mob.isBaby() ? 50 : 1000) != 0) return false;

		BlockPos pos = this.mob.getBlockPos().down();
		return this.world.getBlockState(pos).isIn(ModBlockTags.SHEEPER_PASTURES);
	}

	public void start() {
		this.timer = this.getTickCount(MAX_TIMER);
		this.world.sendEntityStatus(this.mob, (byte)10);
		this.mob.getNavigation().stop();
	}

	public void stop() {
		this.timer = 0;
	}

	public boolean shouldContinue() {
		return this.timer > 0;
	}

	public int getTimer() {
		return this.timer;
	}

	public void tick() {
		this.timer = Math.max(0, this.timer - 1);
		if (this.timer != this.getTickCount(4)) return;

		BlockPos pos = this.mob.getBlockPos().down();
		if (!this.world.getBlockState(pos).isIn(ModBlockTags.SHEEPER_PASTURES)) return;
		this.mob.onEatingGrass();
	}

}
