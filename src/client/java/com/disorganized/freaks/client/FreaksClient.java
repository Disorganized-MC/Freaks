package com.disorganized.freaks.client;

import com.disorganized.freaks.client.registry.ModEntityRenderers;
import com.disorganized.freaks.registry.ModBlocks;
import com.disorganized.freaks.registry.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemGroups;

public class FreaksClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModEntityRenderers.init();
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
			ModBlocks.STEEL_WOOL
		);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content ->
			content.addAfter(Blocks.MAGMA_BLOCK, ModBlocks.STEEL_WOOL)
		);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(content ->
			content.add(ModItems.SHEEPER_SPAWN_EGG)
		);
	}

}
