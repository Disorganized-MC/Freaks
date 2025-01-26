package com.disorganized.freaks.client.registry;

import com.disorganized.freaks.client.content.particle.factory.ItemStackParticleFactory;
import com.disorganized.freaks.registry.ModItems;
import com.disorganized.freaks.registry.ModParticleTypes;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class ModParticleEffects {

	public static void init() {
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.ITEM_MUCK, new ItemStackParticleFactory(ModItems.MUCK_BALL));
	}

}
