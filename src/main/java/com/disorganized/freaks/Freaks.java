package com.disorganized.freaks;

import com.disorganized.freaks.registry.ModBlocks;
import com.disorganized.freaks.registry.ModDamageTypes;
import com.disorganized.freaks.registry.ModSoundEvents;
import net.fabricmc.api.ModInitializer;

import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Freaks implements ModInitializer {

	public static final String MOD_ID = "freaks";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier of(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModBlocks.init();
		ModDamageTypes.init();
		ModSoundEvents.init();
		LOGGER.info("Initialized!");
	}

}