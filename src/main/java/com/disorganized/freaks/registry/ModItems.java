package com.disorganized.freaks.registry;

import com.disorganized.freaks.Freaks;
import com.disorganized.freaks.content.block.SteelWoolBlock;
import com.disorganized.freaks.mixin.FireBlockInvoker;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

import java.awt.*;

public class ModItems {

	public static void init() {}

	public static final Item SHEEPER_SPAWN_EGG = register("sheeper_spawn_egg", new SpawnEggItem(
		ModEntityTypes.SHEEPER,
		Color.decode("#6899c1").getRGB(),
		Color.decode("#3d3f4e").getRGB(),
		new Item.Settings()
	));

	private static Item register(String path, Item item) {
		return Registry.register(Registries.ITEM, Freaks.of(path), item);
	}

}
