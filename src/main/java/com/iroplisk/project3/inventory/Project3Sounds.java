package com.iroplisk.project3.inventory;

import com.iroplisk.project3.Project3;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class Project3Sounds {
    public static final SoundEvent PHILOSOPHERS_STONE_USE = register("item.philosophersstone.use");
    public static final SoundEvent CHARGE_UP = register("tool.charge_up");
    public static final SoundEvent CHARGE_DOWN = register("tool.charge_down");

    private static SoundEvent register(String name) {
        Identifier id = new Identifier(Project3.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        Project3.LOGGER.info("Registering Project3 sounds");
    }
}
