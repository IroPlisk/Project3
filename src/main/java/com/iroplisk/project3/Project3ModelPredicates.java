package com.iroplisk.project3;

import com.iroplisk.project3.item.Items;
import com.iroplisk.project3.item.custom.PhilosophersStoneItem;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class Project3ModelPredicates {
    public static void register() {
        ModelPredicateProviderRegistry.register(Items.PHILOSOPHERS_STONE,
                new Identifier(Project3.MOD_ID, "flash"),
                (stack, world, entity, seed) -> {
                    long start = PhilosophersStoneItem.flashStartMs;
                    if (start == Long.MIN_VALUE) return 0f;
                    long elapsed = Util.getMeasuringTimeMs() - start;
                    if (elapsed >= PhilosophersStoneItem.FLASH_MS) return 0f;
                    return 1f - elapsed / (float) PhilosophersStoneItem.FLASH_MS;
                });
    }
}
