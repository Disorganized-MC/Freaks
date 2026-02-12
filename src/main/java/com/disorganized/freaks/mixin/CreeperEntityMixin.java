package com.disorganized.freaks.mixin;

import com.disorganized.freaks.content.entity.HissingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreeperEntity.class)
public class CreeperEntityMixin implements HissingEntity {

	@Shadow public int fuseTime;

	@Redirect(
		method = {"handleFallDamage", "tick", "getClientFuseTime"},
		at = @At(value = "FIELD", target = "Lnet/minecraft/entity/mob/CreeperEntity;fuseTime:I", opcode = Opcodes.GETFIELD)
	)
	private int getProperFuseTime(CreeperEntity instance) {
		return ((HissingEntity)instance).getFuseTime();
	}

	@ModifyArg(
		method = "tick",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/CreeperEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"),
		index = 0
	)
	private SoundEvent useCustomPrimedSound(SoundEvent par1) {
		return this.getPrimedSound();
	}

	@Override
	public int getFuseTime() {
		return this.fuseTime;
	}

	@Override
	public SoundEvent getPrimedSound() {
		return SoundEvents.ENTITY_CREEPER_PRIMED;
	}

}
