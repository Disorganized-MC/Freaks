package com.disorganized.freaks.registry;

import com.disorganized.freaks.Freaks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags {

	public static void init() {}

	public static final TagKey<Item> SHEEPER_FOOD = of(RegistryKeys.ITEM, "sheeper_food");

	public static final TagKey<Block> SHEEPER_PASTURES = of(RegistryKeys.BLOCK, "sheeper_pastures");

	private static <T> TagKey<T> of(RegistryKey<Registry<T>> key, String path) {
		return TagKey.of(key, Freaks.of(path));
	}

}
