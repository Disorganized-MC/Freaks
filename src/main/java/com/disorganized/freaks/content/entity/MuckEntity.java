package com.disorganized.freaks.content.entity;

import com.disorganized.freaks.content.entity.ai.goal.MudBubbleAttackGoal;
import com.disorganized.freaks.content.entity.projectile.MudBubbleEntity;
import com.disorganized.freaks.registry.ModBlocks;
import com.disorganized.freaks.registry.ModEntityTypes;
import com.disorganized.freaks.registry.ModParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.control.JumpControl;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

public class MuckEntity extends SlimeEntity implements RangedAttackMob {

	private static final float KICK_SPEED = 0.4f;

	public MuckEntity(EntityType<? extends MuckEntity> type, World world) {
		super(type, world);
		this.jumpControl = new MuckJumpControl(this);
	}

	public MuckEntity(World world) {
		super(ModEntityTypes.MUCK, world);
	}

	@Nullable
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
		EntityData data = super.initialize(world, difficulty, spawnReason, entityData);
		this.setSize(this.getRandomSize(), false);
		return data;
	}

	public int getRandomSize() {
		float value = this.getRandom().nextFloat();

		if (value < 0.8F) return 1;
		if (value < 0.95F) return 2;
		return 3;
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.targetSelector.add(1,
			new MudBubbleAttackGoal(this, 0.75F, 60, 4, 16, 3, 10)
		);
	}

	@Override
	public void shootAt(LivingEntity target, float power) {
		MudBubbleEntity bubble = new MudBubbleEntity(this.getWorld(), this);
		bubble.setPos(this.getX(), this.getEyeY(), this.getZ());
		bubble.setTarget(target);

		Vec3d forward = this.getRotationVec(power).normalize();
		Vec3d kick = forward.multiply(KICK_SPEED).add(
			(this.getRandom().nextFloat() - 0.5f) * KICK_SPEED,
			(this.getRandom().nextFloat() - 0.5f) * KICK_SPEED * 0.5f,
			(this.getRandom().nextFloat() - 0.5f) * KICK_SPEED
		);
		bubble.setVelocity(kick);
		this.playSound(SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 1, power);
		this.getWorld().spawnEntity(bubble);
	}

	@Override
	public void onDeath(DamageSource damageSource) {
		super.onDeath(damageSource);

		if (this.getWorld().isClient) return;
		if (!this.isDead()) return;

		double count = Math.pow(this.getSize(), 3);
		for (int i = 0; i < count; i++) {
			FallingBlockEntity fallingBlock = FallingBlockEntity.spawnFromBlock(this.getWorld(), this.getBlockPos(), this.getLivingBlock().getDefaultState());
			((SturdyFallingBlockEntity)fallingBlock).setSturdy(true);
			Vec3d velocity = this.getVelocity().add(
				this.getRandom().nextFloat() * 0.1F,
				this.getRandom().nextFloat() * 0.1F,
				this.getRandom().nextFloat() * 0.1F
			);
			fallingBlock.setVelocity(velocity);
			fallingBlock.velocityModified = true;
		}
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
	public void baseTick() {
		int air = this.getAir();
		super.baseTick();
		this.tickWaterBreathingAir(air);
	}

	protected void tickWaterBreathingAir(int air) {
		if (this.isAlive() && !this.isTouchingWaterOrRain()) {
			this.setAir(air - 1);
			if (this.getAir() == -20) {
				this.setAir(0);
				this.damage(this.getDamageSources().drown(), 1);
			}
		} else {
			this.setAir(300);
		}

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

	public static class MuckJumpControl extends JumpControl {

		public MuckJumpControl(MobEntity entity) {
			super(entity);
		}

		public void stopJumping() {
			this.active = false;
		}

	}

}
