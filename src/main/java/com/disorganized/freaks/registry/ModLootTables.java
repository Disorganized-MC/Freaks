package com.disorganized.freaks.registry;

import com.disorganized.freaks.Freaks;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class ModLootTables {

	public static final RegistryKey<LootTable> SHEEPER_SHEARING = register("shearing/sheeper");

	public static RegistryKey<LootTable> register(String path) {
		return RegistryKey.of(RegistryKeys.LOOT_TABLE, Freaks.of(path));
	}

}
