package com.disorganized.freaks.client;

import com.disorganized.freaks.registry.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroups;

public class FreaksClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content ->
			content.addAfter(Blocks.MAGMA_BLOCK, ModBlocks.STEEL_WOOL)
		);
	}

}
