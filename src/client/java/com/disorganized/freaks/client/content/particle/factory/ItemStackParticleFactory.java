package com.disorganized.freaks.client.content.particle.factory;

import net.minecraft.client.particle.CrackParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;

public class ItemStackParticleFactory implements ParticleFactory<DefaultParticleType> {

	private final ItemStack stack;

	public ItemStackParticleFactory(Item item) {
		this.stack = item.getDefaultStack();
	}

	@Override
	public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
		return new CrackParticle(clientWorld, d, e, f, this.stack);
	}

}
