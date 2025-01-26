package com.disorganized.freaks.client.registry;

import com.disorganized.freaks.registry.ModBlocks;
import com.disorganized.freaks.registry.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroups;

public class ModItemGroups {

	public static void init() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content ->
			content.addAfter(Blocks.MAGMA_BLOCK, ModBlocks.STEEL_WOOL, ModBlocks.LIVING_MUCK)
		);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(content -> {
			content.add(ModItems.SHEEPER_SPAWN_EGG);
			content.add(ModItems.MUCK_SPAWN_EGG);
		});
	}

}
