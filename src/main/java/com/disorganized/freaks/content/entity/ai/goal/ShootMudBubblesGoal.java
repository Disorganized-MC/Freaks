package com.disorganized.freaks.content.entity.ai.goal;

import com.disorganized.freaks.content.entity.MudBubbleEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class ShootMudBubblesGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {

	public final MobEntity entity;
	public int cooldown = 40;

	public ShootMudBubblesGoal(MobEntity entity, Class<T> targetClass, boolean checkVisibility) {
		this(entity, targetClass, 10, checkVisibility, false, null);
	}

	public ShootMudBubblesGoal(MobEntity entity, Class<T> targetClass, int chance, boolean checkVisibility, boolean checkCanNavigate, @Nullable Predicate<LivingEntity> targetPredicate) {
		super(entity, targetClass, chance, checkVisibility, checkCanNavigate, targetPredicate);
		this.entity = entity;
	}

	@Override
	public boolean canStart() {
		return false;
//		if (this.entity == null) return false;
//		return this.entity.distanceTo(this.targetEntity) > 3;
	}

	@Override
	public void tick() {
		cooldown--;
		if (cooldown > 0) return;
		cooldown = 40;

		if (this.targetEntity == null) return;
		if (!this.targetEntity.isAlive()) return;

		MudBubbleEntity bubble = MudBubbleEntity.spawnAt(this.entity.getWorld(), this.entity);
		this.entity.getWorld().spawnEntity(bubble);
	}

}
