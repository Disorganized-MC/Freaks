package com.disorganized.freaks.content.entity;

import com.disorganized.freaks.registry.ModDamageTypes;
import com.disorganized.freaks.registry.ModEntityTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MudBubbleEntity extends ProjectileEntity {

	private final int damage = 3;

	public MudBubbleEntity(EntityType<MudBubbleEntity> type, World world) {
		super(type, world);
	}

	public MudBubbleEntity(World world, Entity owner) {
		super(ModEntityTypes.MUD_BUBBLE, world);
		this.setOwner(owner);
	}

	public static MudBubbleEntity spawnAt(World world, Entity owner) {
		return new MudBubbleEntity(world, owner);
	}

	@Override
	protected void initDataTracker() {

	}

	@Override
	protected void onEntityHit(EntityHitResult result) {
		super.onEntityHit(result);
		Entity entity = result.getEntity();
		Entity owner = this.getOwner();
		DamageSource source = createDamageSource(entity, owner);
		if (!entity.damage(source, this.damage)) return;

		if (entity instanceof LivingEntity target) {
			this.onHit(target);
		}

		this.pop();
	}

	protected void onHit(LivingEntity target) {}

	public DamageSource createDamageSource(Entity entity, Entity owner) {
		RegistryEntry<DamageType> type = this.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(ModDamageTypes.STEEL_WOOL);
		if (owner == null) {
			return new DamageSource(type);
		}
		else {
			if (owner instanceof LivingEntity livingOwner) livingOwner.onAttacking(entity);
			return new DamageSource(type, owner);
		}
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		this.pop();
	}

	public void pop() {
		this.playSound(SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 1, 1);
		this.discard();
	}

	@Override
	public void tick() {
		super.tick();
	}

}
