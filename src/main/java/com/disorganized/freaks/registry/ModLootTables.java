package com.disorganized.freaks.registry;

import com.disorganized.freaks.Freaks;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModLootTables {

	public static final RegistryKey<LootTable> SHEEPER_SHEARED = register("gameplay/sheeper_sheared");

	public static RegistryKey<LootTable> register(String path) {
		return RegistryKey.of(RegistryKeys.LOOT_TABLE, Freaks.of(path));
	}

}
