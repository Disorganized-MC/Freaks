package com.disorganized.freaks.content.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.world.World;

public class MutltiFallingBlockEntity extends FallingBlockEntity {

	private int size;

	public MutltiFallingBlockEntity(EntityType<? extends FallingBlockEntity> entityType, World world) {
		super(entityType, world);
	}

	public MutltiFallingBlockEntity(World world, double x, double y, double z, BlockState block) {
		super(world, x, y, z, block);
	}

}
