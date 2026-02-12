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
	public static final float FORMING_CHANCE = 0.5F;
	public static final float DECAYING_CHANCE = 0.5F;

	public LivingMuckBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(HYDRATED, true));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HYDRATED);
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return true;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (FarmlandBlock.isWaterNearby(world, pos) || world.hasRain(pos.up())) {
			if (!state.get(HYDRATED)) world.setBlockState(pos, state.with(HYDRATED, true));
			if (random.nextFloat() < FORMING_CHANCE) tryFormingMuck(world, pos);
		} else {
			if (state.get(HYDRATED)) world.setBlockState(pos, state.with(HYDRATED, false));
			if (random.nextFloat() < DECAYING_CHANCE) world.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
	}

	public void tryFormingMuck(ServerWorld world, BlockPos pos) {
		world.setBlockState(pos, Blocks.AIR.getDefaultState());
		MuckEntity entity = new MuckEntity(world);
		entity.setPosition(Vec3d.of(pos));
		entity.setSize(1, false);
		world.spawnEntity(entity);
	}

}
