package com.disorganized.freaks.registry;

import com.disorganized.freaks.Freaks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSoundEvents {

    public static void init() {}

    public static final SoundEvent BLOCK_STEEL_WOOL_SCRUB = register("block.freaks.steel_wool.scrub");

	public static final SoundEvent ENTITY_SHEEPER_AMBIENT = register("entity.freaks.sheeper.ambient");
	public static final SoundEvent ENTITY_SHEEPER_DEATH = register("entity.freaks.sheeper.death");
	public static final SoundEvent ENTITY_SHEEPER_HURT = register("entity.freaks.sheeper.hurt");
	public static final SoundEvent ENTITY_SHEEPER_PRIMED = register("entity.freaks.sheeper.primed");
	public static final SoundEvent ENTITY_SHEEPER_SHEAR = register("entity.freaks.sheeper.shear");

	private static SoundEvent register(String path) {
        Identifier id = Freaks.of(path);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

}
