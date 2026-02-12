package com.disorganized.freaks.content.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class MultiFallingBlockEntity extends FallingBlockEntity implements SturdyFallingBlockEntity {

	protected int size;

	public MultiFallingBlockEntity(EntityType<? extends FallingBlockEntity> entityType, World world) {
		super(entityType, world);
	}

	public MultiFallingBlockEntity(World world, double x, double y, double z, BlockState block, int size) {
		super(world, x, y, z, block);
		this.size = size;
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("Size", this.size);
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.size = nbt.getInt("Size");
	}

	@Override
	protected Box calculateBoundingBox() {
		return super.calculateBoundingBox().expand(this.getSize());
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return this.size;
	}

}
