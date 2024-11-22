package com.disorganized.freaks.registry;

import com.disorganized.freaks.Freaks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSoundEvents {

    public static void init() {}

    public static final SoundEvent BLOCK_STEEL_WOOL_SCRUB = register("block.freaks.steel_wool.scrub");

    private static SoundEvent register(String path) {
        Identifier id = Freaks.of(path);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

}
