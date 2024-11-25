package com.disorganized.freaks.registry;

import com.disorganized.freaks.Freaks;
import com.disorganized.freaks.content.entity.SheeperEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntityTypes {

	public static void init() {
		FabricDefaultAttributeRegistry.register(SHEEPER, SheeperEntity.createMobAttributes());
	}

	public static final EntityType<SheeperEntity> SHEEPER = register("sheeper",
		FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SheeperEntity::new)
			.dimensions(new EntityDimensions(1, 1.625F, true))
	);

	private static <T extends Entity> EntityType<T> register(String path, FabricEntityTypeBuilder<T> builder) {
		return Registry.register(Registries.ENTITY_TYPE, Freaks.of(path), builder.build());
	}

}
