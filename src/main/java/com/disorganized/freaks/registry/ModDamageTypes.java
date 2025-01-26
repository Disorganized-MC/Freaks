package com.disorganized.freaks.registry;

import com.disorganized.freaks.Freaks;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class ModDamageTypes {

	public static void init() {}

	public static final RegistryKey<DamageType> STEEL_WOOL = of("steel_wool");
	public static final RegistryKey<DamageType> MUD_BUBBLE = of("mud_bubble");

	private static RegistryKey<DamageType> of(String path) {
		return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Freaks.of(path));
	}

}
