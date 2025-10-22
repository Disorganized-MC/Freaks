package com.disorganized.freaks.content.entity;

import com.disorganized.freaks.registry.ModSoundEvents;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class SheeperEntity extends AbstractSheeperEntity {

	public SheeperEntity(EntityType<? extends CreeperEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return ModSoundEvents.ENTITY_SHEEPER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ModSoundEvents.ENTITY_SHEEPER_DEATH;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return ModSoundEvents.ENTITY_SHEEPER_AMBIENT;
	}

	@Override
	public SoundEvent getPrimedSound() {
		return ModSoundEvents.ENTITY_SHEEPER_PRIMED;
	}

	public static boolean canSpawn(EntityType<SheeperEntity> type, WorldAccess world, SpawnReason reason, BlockPos pos, Random random) {
		return world.getBlockState(pos.down()).isIn(BlockTags.ANIMALS_SPAWNABLE_ON) && world.getBaseLightLevel(pos, 0) > 8;
	}

}
