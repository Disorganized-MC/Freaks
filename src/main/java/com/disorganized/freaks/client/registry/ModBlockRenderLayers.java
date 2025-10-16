package com.disorganized.freaks.client.registry;

import com.disorganized.freaks.registry.ModBlocks;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class ModBlockRenderLayers {

	public static void init() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
			ModBlocks.STEEL_WOOL
		);
	}

}
