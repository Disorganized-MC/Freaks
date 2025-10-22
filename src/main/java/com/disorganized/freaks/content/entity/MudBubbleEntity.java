package com.disorganized.freaks.content.entity;

import com.disorganized.freaks.registry.ModDamageTypes;
import com.disorganized.freaks.registry.ModEntityTypes;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Uuids;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MudBubbleEntity extends Entity implements Ownable {

//	public static final Codec<Entity> OWNER_CODEC = Uuids.CODEC.xmap(
//		e ->
//	);

	private @Nullable Entity owner;
	private @Nullable LivingEntity target;
	private int targetTimeout = 0;
	private int lifespan = 200;

	public MudBubbleEntity(EntityType<MudBubbleEntity> type, World world) {
		super(type, world);
	}

	public MudBubbleEntity(World world, Entity owner) {
		super(ModEntityTypes.MUD_BUBBLE, world);
		this.setOwner(owner);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {

	}

	@Override
	public @Nullable Entity getOwner() {
		return this.owner;
	}

	public void setOwner(@Nullable Entity entity) {
		this.owner = entity;
	}

	public static MudBubbleEntity spawnAt(World world, Entity owner) {
		return new MudBubbleEntity(world, owner);
	}

//	@Override
//	protected void onEntityHit(EntityHitResult result) {
//		super.onEntityHit(result);
//		Entity entity = result.getEntity();
//		Entity owner = this.getOwner();
//		DamageSource source = createDamageSource(entity, owner);
//		if (!entity.damage(source, 3)) return;
//
//		if (entity instanceof LivingEntity target) this.onHit(target);
//		this.kill();
//	}

//	protected void onHit(LivingEntity target) {
//		System.out.println("aa");
//	}

	public DamageSource createDamageSource(Entity entity, Entity owner) {
		RegistryEntry<DamageType> type = this.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(ModDamageTypes.STEEL_WOOL);
		if (owner == null) {
			return new DamageSource(type);
		}
		if (owner instanceof LivingEntity living) living.onAttacking(entity);
		return new DamageSource(type, owner);
	}

//	@Override
//	protected void onBlockHit(BlockHitResult blockHitResult) {
//		super.onBlockHit(blockHitResult);
//		this.kill();
//	}

	@Override
	public void kill() {
		super.kill();
		this.playSound(SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 1, 1);
	}

	@Override
	public void tick() {
		super.tick();
		--this.lifespan;
		if (this.lifespan <= 0) {
			this.kill();
			return;
		}

		if (this.target == null) {
			if (this.targetTimeout <= 0) {
				this.target = findNewTarget();
				this.targetTimeout = 60;
			} else {
				this.targetTimeout--;
				return;
			}
		}

		if (this.target != null && this.target.isAlive()) {
			if (this.distanceTo(this.target) > 5) {
				this.target = null;
				this.targetTimeout = 60;
				return;
			}

			Vec3d targetPos = this.target.getPos().add(0, this.target.getHeight() / 2, 0);
			Vec3d currentPos = this.getPos();
			Vec3d direction = targetPos.subtract(currentPos).normalize();

			this.setVelocity(direction.multiply(0.5));
			this.velocityModified = true;
			this.velocityDirty = true;
		}
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		if (nbt.containsUuid("owner")) {
			this.owner = this.getWorld().getPlayerByUuid(nbt.getUuid("owner"));
		}
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		if (this.owner != null) {
			nbt.putUuid("owner", this.owner.getUuid());
		}
	}

	public @Nullable LivingEntity findNewTarget() {
		World world = this.getWorld();
		Vec3d pos = this.getPos();
		return world.getClosestPlayer(pos.x, pos.y, pos.z, 15, true);
	}

	@Override
	public void setComponents(ComponentMapImpl components) {

	}

	@Override
	public ComponentMapImpl getMutableComponents() {
		return null;
	}

}
