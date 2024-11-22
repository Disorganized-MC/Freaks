package com.disorganized.freaks.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FireBlock.class)
public interface FireBlockInvoker {

	@Invoker
	static int invokeGetFireTickDelay(Random random) {
		return 0;
	}

	@Invoker
	int invokeGetSpreadChance(BlockState state);

	@Invoker
	void invokeRegisterFlammableBlock(Block block, int burnChance, int spreadChance);

}
