package com.disorganized.freaks.content.entity.ai.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class ShootMudBubblesGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {

	public ShootMudBubblesGoal(MobEntity mob, Class<T> targetClass, boolean checkVisibility) {
		this(mob, targetClass, 10, checkVisibility, false, (Predicate)null);
	}

	public ShootMudBubblesGoal(MobEntity mob, Class<T> targetClass, int reciprocalChance, boolean checkVisibility, boolean checkCanNavigate, @Nullable Predicate<LivingEntity> targetPredicate) {
		super(mob, targetClass, reciprocalChance, checkVisibility, checkCanNavigate, targetPredicate);
	}

	@Override
	public boolean canStart() {
		return false;
	}

}
