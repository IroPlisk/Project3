package com.iroplisk.project3;

import com.iroplisk.project3.item.Items;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

public class CustomLootTables {
    private static final Identifier ZOMBIE_ID =
            new Identifier("minecraft", "entities/zombie");


    public static void CustomLootProject3(){
        Project3.LOGGER.info("Loading Project3 custom loot tables");
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (ZOMBIE_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .with(ItemEntry.builder(Items.MINIUM_SHARD))
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.05f));

                tableBuilder.pool(poolBuilder);
            }
        });
    }
}