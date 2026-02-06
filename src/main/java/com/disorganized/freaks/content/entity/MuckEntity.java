package com.disorganized.freaks.content.entity;

import com.disorganized.freaks.content.entity.ai.goal.ShootMudBubblesGoal;
import com.disorganized.freaks.registry.ModBlocks;
import com.disorganized.freaks.registry.ModParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;

public class MuckEntity extends SlimeEntity {

	public MuckEntity(EntityType<? extends MuckEntity> type, World world) {
		super(type, world);
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.targetSelector.add(1, new ShootMudBubblesGoal<>(this));
	}

	@Override
	public void setMovementSpeed(float speed) {
		super.setMovementSpeed(-speed);
	}

	@Override
	public void onDeath(DamageSource damageSource) {
		super.onDeath(damageSource);

		if (this.getWorld().isClient) return;
		if (!this.isDead()) return;

		FallingBlockEntity fallingBlock = FallingBlockEntity.spawnFromBlock(this.getWorld(), this.getBlockPos(), this.getLivingBlock().getDefaultState());
		((SturdyFallingBlockEntity)fallingBlock).setSturdy(true);
		fallingBlock.setVelocity(this.getVelocity().multiply(1.5));
		fallingBlock.velocityModified = true;
	}

	@Override
	public void remove(Entity.RemovalReason reason) {
		this.setRemoved(reason);
		this.brain.forgetAll();
	}

	@Override
	public void takeKnockback(double strength, double x, double z) {
		super.takeKnockback(strength * getKnockbackMultiplier(), x, z);
	}

	@Override
	public EntityDimensions getBaseDimensions(EntityPose pose) {
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

	@Override
	protected ParticleEffect getParticles() {
		return ModParticleTypes.ITEM_MUCK;
	}

	public float getKnockbackMultiplier() {
		return 2;
	}

	public Block getLivingBlock() {
		return ModBlocks.LIVING_MUCK;
	}

	public static boolean canSpawn(EntityType<MuckEntity> type, ServerWorldAccess world, SpawnReason reason, BlockPos pos, Random random) {
		if (world.getDifficulty() == Difficulty.PEACEFUL) return false;
		try (ServerWorld serverWorld = world.toServerWorld()) {
			if (serverWorld.hasRain(pos)) return false;
		} catch (Exception ignored) {}
		double spawnMultiplier = world.getBiome(pos).isIn(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS) ? 2 : 0.5;
		int lightLevel = world.getLightingProvider().get(LightType.BLOCK).getLightLevel(pos);
		if (pos.getY() > 63 && pos.getY() < 99 && random.nextFloat() < (0.5F * spawnMultiplier) && random.nextFloat() < world.getMoonSize() && lightLevel <= random.nextInt(8)) {
			return canMobSpawn(type, world, reason, pos, random);
		}
		return false;
	}

}
