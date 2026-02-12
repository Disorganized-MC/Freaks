package com.disorganized.freaks.content.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class AbstractBubbleEntity extends ProjectileEntity {

	public static final Predicate<Entity> TARGET_PREDICATE = EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.and(entity ->
		(entity instanceof PlayerEntity || entity instanceof IronGolemEntity)
			&& entity.isAlive()
			&& !entity.isRemoved()
	);

	protected static final int LIFESPAN = 200;
	protected static final float ACCELERATION = 0.1F;
	protected static final float MAX_SPEED = 0.333F;
	protected static final float DRAG = 0.8F;

	@Nullable
	protected LivingEntity target;

	public AbstractBubbleEntity(EntityType<? extends AbstractBubbleEntity> type, World world) {
		super(type, world);
		this.noClip = true;
	}

	public AbstractBubbleEntity(EntityType<? extends AbstractBubbleEntity> type, World world, Entity owner) {
		super(type, world);
		this.setOwner(owner);
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
	protected double getGravity() {
		return 0.04F;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		this.pop();
		return true;
	}

	@Override
	public void tick() {
		super.tick();

		if (this.age >= LIFESPAN) {
			this.pop();
			return;
		}

		if (this.age % 20 == 0) {
			this.target = this.findNewTarget();
		}

		if (!this.getWorld().isClient) {
			HitResult result = ProjectileUtil.getCollision(this, this::canHit);
			if (result.getType() != HitResult.Type.MISS) this.hitOrDeflect(result);

			if (this.target != null && (!this.target.isAlive() || this.target.isRemoved())) {
				this.applyGravity();
			} else {
				this.modifyVelocity();
			}
		}

		this.checkBlockCollision();
		this.move(MovementType.SELF, this.getVelocity());
	}

	@Nullable
	protected LivingEntity findNewTarget() {
		Box box = this.getBoundingBox().expand(16);
		List<LivingEntity> nearbyTargets = this.getWorld().getEntitiesByClass(LivingEntity.class, box, TARGET_PREDICATE);

		return nearbyTargets.stream()
			.filter(entity -> entity != this.getOwner())
			.min(Comparator.comparingDouble(entity -> entity.squaredDistanceTo(this)))
			.orElse(null);
	}

	protected void modifyVelocity() {
		Vec3d currentVelocity = this.getVelocity();

		if (this.target == null) {
			this.setVelocity(currentVelocity.multiply(DRAG));
			this.velocityDirty = true;
			this.velocityModified = true;
			return;
		}

		Vec3d toTarget = this.target.getPos().subtract(this.getPos());

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

	@Override
	public boolean canHit() {
		return true;
	}

	@Override
	protected boolean canHit(Entity entity) {
		return super.canHit(entity) && !entity.noClip;
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		this.pop();
	}

	protected void onCollision(HitResult result) {
		super.onCollision(result);
		this.pop();
	}

	@Override
	protected void onBlockHit(BlockHitResult result) {
		super.onBlockHit(result);
		this.pop();
	}

	public void pop() {
		this.playSound(SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 1, 1);
		this.getWorld().emitGameEvent(GameEvent.ENTITY_DAMAGE, this.getPos(), GameEvent.Emitter.of(this));
		this.discard();
	}

}
