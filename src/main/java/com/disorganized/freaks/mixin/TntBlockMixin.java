package com.disorganized.freaks.mixin;

import com.disorganized.freaks.content.block.Ignitable;
import net.minecraft.block.Block;
import net.minecraft.block.TntBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TntBlock.class)
public abstract class TntBlockMixin extends Block implements Ignitable {

	public TntBlockMixin(Settings settings) {
		super(settings);
	}

	@Override
	public boolean onIgnited(World world, BlockPos pos) {
		TntBlock.primeTnt(world, pos);
		return true;
	}

}
