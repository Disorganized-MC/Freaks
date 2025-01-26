package com.disorganized.freaks.registry;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModParticleTypes {

	public static void init() {}

	public static final DefaultParticleType ITEM_MUCK = of("item_muck", false);

	private static DefaultParticleType of(String path, boolean alwaysShow) {
		return Registry.register(Registries.PARTICLE_TYPE, path, FabricParticleTypes.simple(alwaysShow));
	}

}
