package com.disorganized.freaks.mixin;

import com.disorganized.freaks.content.entity.SturdyFallingBlockEntity;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity implements SturdyFallingBlockEntity {

	@Unique private boolean sturdy = false;

	public FallingBlockEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	public boolean isSturdy() {
		return this.sturdy;
	}

	@Override
	public void setSturdy(boolean sturdy) {
		this.sturdy = sturdy;
	}

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;canReplace(Lnet/minecraft/item/ItemPlacementContext;)Z"))
	private boolean sturdyCanReplace(boolean canReplace, @Local BlockState state) {
		if (this.isSturdy()) return true;
		return canReplace;
	}

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FallingBlock;canFallThrough(Lnet/minecraft/block/BlockState;)Z"))
	private boolean sturdyCanFallThrough(boolean canFallThrough, @Local BlockState state) {
		if (this.isSturdy()) return false;
		return canFallThrough;
	}

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;canPlaceAt(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z"))
	private boolean sturdyCanPlaceAt(boolean canPlaceAt, @Local BlockState state) {
		if (!this.isSturdy()) return canPlaceAt;

		if (state.isReplaceable()) return true;
		return switch (state.getPistonBehavior()) {
			case DESTROY, IGNORE -> true;
			default -> false;
		};
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private void breakBlockBeforeSetting(CallbackInfo ci, @Local BlockPos pos) {
		this.getWorld().breakBlock(pos, true);
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void afterWriteNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putBoolean("sturdy", this.isSturdy());
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void afterReadNbt(NbtCompound nbt, CallbackInfo ci) {
		this.setSturdy(nbt.getBoolean("sturdy"));
	}

}
