package com.disorganized.freaks.registry;

import com.disorganized.freaks.Freaks;
import com.disorganized.freaks.content.entity.MuckEntity;
import com.disorganized.freaks.content.entity.MudBubbleEntity;
import com.disorganized.freaks.content.entity.SheeperEntity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.Heightmap;

public class ModEntityTypes {

	public static void init() {
		BiomeModifications.addSpawn(BiomeSelectors.tag(ConventionalBiomeTags.CLIMATE_TEMPERATE), SpawnGroup.CREATURE, MUCK, 20, 1, 3);

		SpawnRestriction.register(SHEEPER, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SheeperEntity::canSpawn);
		SpawnRestriction.register(MUCK, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MuckEntity::canSpawn);

		FabricDefaultAttributeRegistry.register(SHEEPER, SheeperEntity.createMobAttributes());
		FabricDefaultAttributeRegistry.register(MUCK, MuckEntity.createMobAttributes());
	}

	public static final EntityType<SheeperEntity> SHEEPER = register("sheeper",
		EntityType.Builder.create(SheeperEntity::new, SpawnGroup.MONSTER)
			.dimensions(1, 1.625F)
	);
	public static final EntityType<MuckEntity> MUCK = register("muck",
		EntityType.Builder.create(MuckEntity::new, SpawnGroup.CREATURE)
	);
	public static final EntityType<MudBubbleEntity> MUD_BUBBLE = register("mud_bubble",
		EntityType.Builder.<MudBubbleEntity>create(MudBubbleEntity::new, SpawnGroup.MISC)
			.dimensions(0.5F, 0.5F)
	);

	private static <T extends Entity> EntityType<T> register(String path, EntityType.Builder<T> builder) {
		return Registry.register(Registries.ENTITY_TYPE, Freaks.of(path), builder.build());
	}

}
