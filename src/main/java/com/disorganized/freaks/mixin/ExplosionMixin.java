package com.disorganized.freaks.mixin;

import com.disorganized.freaks.content.misc.ExplosionCollection;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Explosion.class)
public class ExplosionMixin implements ExplosionCollection {

	private final List<Entity> affectedEntities = new ArrayList<>();

	@Inject(method = "collectBlocksAndDamageEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
	private void injected(CallbackInfo ci, @Local Entity entity, @Local double w ) {
		this.affectedEntities.add(entity);
	}

	@Override
	public List<Entity> getAffectedEntities() {
		return this.affectedEntities;
	}

}
