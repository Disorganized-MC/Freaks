package com.disorganized.freaks.content.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface Ignitable {

	boolean onIgnited(World world, BlockPos pos);

}
