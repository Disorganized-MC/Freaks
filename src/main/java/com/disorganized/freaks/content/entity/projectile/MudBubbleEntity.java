package com.disorganized.freaks.content.entity.projectile;

import com.disorganized.freaks.registry.ModEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class MudBubbleEntity extends AbstractBubbleEntity {

	public MudBubbleEntity(EntityType<MudBubbleEntity> type, World world) {
		super(type, world);
	}

	public MudBubbleEntity(World world, Entity owner) {
		super(ModEntityTypes.MUD_BUBBLE, world, owner);
	}

	@Override
	protected void onEntityHit(EntityHitResult result) {
		super.onEntityHit(result);
		result.getEntity().damage(this.getDamageSources().mobAttack((LivingEntity) this.getOwner()), 1);
	}

}
