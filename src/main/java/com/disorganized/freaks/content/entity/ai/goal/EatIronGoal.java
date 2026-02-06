package com.disorganized.freaks.content.entity.ai.goal;

import com.disorganized.freaks.content.entity.AbstractSheeperEntity;
import com.disorganized.freaks.registry.ModTags;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicInteger;

public class EatIronGoal extends Goal {

	private static final int MAX_TIMER = 100;
	private static final int GRAZING_CHANCE = 2500;

	private final AbstractSheeperEntity entity;
	private final World world;
	private final AtomicInteger timer;

	public EatIronGoal(AbstractSheeperEntity entity) {
		this.entity = entity;
		this.world = entity.getWorld();
		this.timer = new AtomicInteger(0);
		this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
	}

	@Override
	public boolean canStart() {
		if (!this.entity.canGrowWool()) return false;
		if (this.entity.getRandom().nextInt(GRAZING_CHANCE) != 0) return false;
		BlockPos pos = this.entity.getSteppingPos();
		return this.world.getBlockState(pos).isIn(ModTags.SHEEPER_PASTURES);
	}

	@Override
	public void start() {
		this.timer.setPlain(MAX_TIMER);
		this.entity.setGrazing(true);
		this.entity.getNavigation().stop();
	}

	@Override
	public void stop() {
		if (this.timer.get() != 0) return;

		BlockPos pos = this.entity.getBlockPos().down();
		if (!this.world.getBlockState(pos).isIn(ModTags.SHEEPER_PASTURES)) return;

		this.entity.setGrazing(false);
		this.entity.onEatingGrass();
	}

	@Override
	public boolean shouldContinue() {
		return this.timer.get() > 0;
	}

	@Override
	public void tick() {
		this.timer.decrementAndGet();
	}

//	@Override
//	public boolean shouldRunEveryTick() {
//		return true;
//	}

}
