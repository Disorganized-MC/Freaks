package com.disorganized.freaks.content.entity;

import com.disorganized.freaks.registry.ModEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class MudBubbleEntity extends ProjectileEntity {

	protected @Nullable LivingEntity target;
	private @Nullable Vec3d overshotTarget = null;
	protected int lifespan = 200;

	private static final float ACCELERATION = 0.1F;
	private static final float MAX_SPEED = 2;
	private static final float DRAG = 0.8F;

	public MudBubbleEntity(EntityType<MudBubbleEntity> type, World world) {
		super(type, world);
	}

	public MudBubbleEntity(World world, Entity owner) {
		super(ModEntityTypes.MUD_BUBBLE, world);
		this.setOwner(owner);
		this.noClip = true;
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
	}

	public @Nullable LivingEntity getTarget() {
		return this.target;
	}

	public void setTarget(@Nullable LivingEntity target) {
		this.target = target;
	}

	@Override
	public void tick() {
		super.tick();

		this.lifespan--;
		if (this.lifespan <= 0) {
			this.kill();
			return;
		}

		if (this.age % 20 == 0) {
			this.target = this.findNewTarget();
		}

		if (this.target != null && (!this.target.isAlive() || this.target.isRemoved())) {
			this.target = null;
			this.overshotTarget = null;
		}

		this.moveToTarget();
		this.move(MovementType.SELF, this.getVelocity());

		this.checkEntityCollisions();
	}

	@Nullable
	private LivingEntity findNewTarget() {
		List<LivingEntity> nearbyEntities = this.getWorld().getEntitiesByClass(
			LivingEntity.class,
			this.getBoundingBox().expand(16),
			entity -> (entity instanceof PlayerEntity || entity instanceof IronGolemEntity)
				&& entity.isAlive()
				&& !entity.isRemoved()
				&& entity != this.getOwner()
		);

		return nearbyEntities.stream()
			.min(Comparator.comparingDouble(entity -> entity.squaredDistanceTo(this)))
			.orElse(null);
	}

	private void moveToTarget() {
		Vec3d currentVelocity = this.getVelocity();

		if (this.target == null && this.overshotTarget == null) {
			this.setVelocity(currentVelocity.multiply(DRAG));
			this.velocityDirty = true;
			this.velocityModified = true;
			return;
		}

		Vec3d steerToward;
		if (this.overshotTarget != null) {
			steerToward = this.overshotTarget;
		} else {
			steerToward = this.target.getPos();
		}

		Vec3d toTarget = steerToward.subtract(this.getPos());
		double distToTarget = toTarget.length();

		if (this.overshotTarget == null && distToTarget < 1.5 && currentVelocity.lengthSquared() > 0.005F) {
			this.overshotTarget = steerToward;
		}

		if (this.overshotTarget != null && currentVelocity.lengthSquared() < 0.002F) {
			this.overshotTarget = null;
		}

		Vec3d direction = toTarget.normalize();
		Vec3d acceleration = direction.multiply(ACCELERATION);
		Vec3d newVelocity = currentVelocity.add(acceleration);

		double speed = newVelocity.length();
		if (speed > MAX_SPEED) {
			newVelocity = newVelocity.multiply(MAX_SPEED / speed);
		}
		newVelocity = newVelocity.multiply(DRAG);

		this.setVelocity(newVelocity);
		this.velocityDirty = true;
		this.velocityModified = true;
	}

	public static final Predicate<Entity> COLLISION_PREDICATE = EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.and(entity -> entity instanceof LivingEntity);

	private void checkEntityCollisions() {
		List<Entity> collidingEntities = this.getWorld().getOtherEntities(this, this.getBoundingBox(), COLLISION_PREDICATE);
		for (Entity entity : collidingEntities) {
			if (entity == this.getOwner()) continue;

			entity.damage(this.getDamageSources().mobAttack((LivingEntity) this.getOwner()), 1.0f);
			this.kill();
			break;
		}
	}

	@Override
	protected void onBlockCollision(BlockState state) {
		super.onBlockCollision(state);
		if (state.isAir()) return;

		this.kill();
	}

	@Override
	public void kill() {
		super.kill();
		this.playSound(SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 1, 1);
	}

}
