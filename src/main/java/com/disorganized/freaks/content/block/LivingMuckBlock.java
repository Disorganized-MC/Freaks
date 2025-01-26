package com.disorganized.freaks.content.block;

import com.disorganized.freaks.content.entity.MuckEntity;
import com.disorganized.freaks.registry.ModEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class LivingMuckBlock extends Block {

	public static final BooleanProperty HYDRATED = BooleanProperty.of("hydrated");

	public LivingMuckBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(HYDRATED, true));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HYDRATED);
	}

	public static final int MUCK_FORMING_CHANCE = 1;

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return true;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (FarmlandBlock.isWaterNearby(world, pos) || world.hasRain(pos.up())) {
			if (!state.get(HYDRATED)) world.setBlockState(pos, state.with(HYDRATED, true));
			if (random.nextInt(MUCK_FORMING_CHANCE) == 0) tryFormingMuck(world, pos);
		} else {
			if (state.get(HYDRATED)) world.setBlockState(pos, state.with(HYDRATED, false));
		}
	}

	public void tryFormingMuck(ServerWorld world, BlockPos pos) {
		System.out.println("test");
		world.setBlockState(pos, Blocks.AIR.getDefaultState());
		MuckEntity entity = ModEntityTypes.MUCK.create(world);
		if (entity == null) return;

		entity.setPosition(Vec3d.of(pos));
		world.spawnEntity(entity);
	}

}
