package com.disorganized.freaks.content.entity;

import com.disorganized.freaks.registry.ModBlocks;
import com.disorganized.freaks.registry.ModParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;

public class MuckEntity extends AbstractMuckEntity {

	public MuckEntity(EntityType<? extends MuckEntity> type, World world) {
		super(type, world);
	}

	@Override
	protected ParticleEffect getParticles() {
		return ModParticleTypes.ITEM_MUCK;
	}

	@Override
	public float getKnockbackMultiplier() {
		return 2;
	}

	@Override
	public Block getLivingBlock() {
		return ModBlocks.LIVING_MUCK;
	}

	public static boolean canSpawn(EntityType<MuckEntity> type, ServerWorldAccess world, SpawnReason reason, BlockPos pos, Random random) {
		if (world.getDifficulty() == Difficulty.PEACEFUL) return false;
		try (ServerWorld serverWorld = world.toServerWorld()) {
			if (serverWorld.hasRain(pos)) return false;
		} catch (Exception ignored) {}
		double spawnMultiplier = world.getBiome(pos).isIn(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS) ? 2 : 0.5;
		int lightLevel = world.getLightingProvider().get(LightType.BLOCK).getLightLevel(pos);
		if (pos.getY() > 63 && pos.getY() < 99 && random.nextFloat() < (0.5F * spawnMultiplier) && random.nextFloat() < world.getMoonSize() && lightLevel <= random.nextInt(8)) {
			return canMobSpawn(type, world, reason, pos, random);
		}
		return false;
	}

}
