package com.disorganized.freaks.registry;

import com.disorganized.freaks.Freaks;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModBlockTags {

	public static void init() {}

	public static final TagKey<Block> SHEEPER_PASTURES = of("sheeper_pastures");

	private static TagKey<Block> of(String path) {
		return TagKey.of(RegistryKeys.BLOCK, Freaks.of(path));
	}

}
