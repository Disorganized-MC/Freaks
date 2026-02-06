package com.disorganized.freaks.mixin;

import com.disorganized.freaks.content.block.Ignitable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireBlock.class)
public abstract class FireBlockMixin extends Block {

	public FireBlockMixin(Settings settings) {
		super(settings);
	}

	@Inject(method = "trySpreadingFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z", shift = At.Shift.AFTER), cancellable = true)
	private void ignoreDefaultTntInteraction1(World world, BlockPos pos, int spreadFactor, Random random, int currentAge, CallbackInfo ci) {
		ci.cancel();
	}

	@Inject(method = "trySpreadingFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", shift = At.Shift.AFTER), cancellable = true)
	private void ignoreDefaultTntInteraction2(World world, BlockPos pos, int spreadFactor, Random random, int currentAge, CallbackInfo ci) {
		ci.cancel();
	}

	@Inject(method = "trySpreadingFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", ordinal = 1, shift = At.Shift.AFTER), cancellable = true)
	private void customIgnitionHandling(World world, BlockPos pos, int spreadFactor, Random random, int currentAge, CallbackInfo ci) {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof Ignitable ignitable)
			if (!ignitable.onIgnited(world, pos)) ci.cancel();
	}

}
