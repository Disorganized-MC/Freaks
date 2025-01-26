package com.disorganized.freaks.registry;

import com.disorganized.freaks.Freaks;
import com.disorganized.freaks.content.block.LivingMuckBlock;
import com.disorganized.freaks.content.block.SteelWoolBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

public class ModBlocks {

    public static void init() {
		((FireBlock)Blocks.FIRE).registerFlammableBlock(STEEL_WOOL, 15, 400);
	}

    public static final Block STEEL_WOOL = register("steel_wool", new SteelWoolBlock(
		FabricBlockSettings.create()
			.mapColor(MapColor.GRAY)
			.instrument(Instrument.GUITAR)
			.strength(1.6F)
			.sounds(BlockSoundGroup.WOOL)
			.luminance(SteelWoolBlock::getLuminance)
			.burnable()
    ));
	public static final Block LIVING_MUCK = register("living_muck", new LivingMuckBlock(
		FabricBlockSettings.create()
			.mapColor(MapColor.BROWN)
			.sounds(BlockSoundGroup.MUD)
	));

    private static Block register(String path, Block block) {
        Registry.register(Registries.BLOCK, Freaks.of(path), block);
        Registry.register(Registries.ITEM, Freaks.of(path), new BlockItem(block, new Item.Settings()));
        return block;
    }

}
