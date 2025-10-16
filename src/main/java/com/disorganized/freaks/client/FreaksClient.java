package com.disorganized.freaks.client;

import com.disorganized.freaks.client.registry.ModBlockRenderLayers;
import com.disorganized.freaks.client.registry.ModEntityRenderers;
import com.disorganized.freaks.client.registry.ModItemGroups;
import com.disorganized.freaks.client.registry.ModParticleEffects;
import net.fabricmc.api.ClientModInitializer;

public class FreaksClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModEntityRenderers.init();
		ModParticleEffects.init();
		ModBlockRenderLayers.init();
		ModItemGroups.init();
	}

}
