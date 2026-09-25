package com.iroplisk.project3.block;

import com.example.project3.block.Blocks;
import com.iroplisk.project3.Project3;
import com.iroplisk.project3.block.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockEntities {
    public static final BlockEntityType<CalcinatorBlockEntity> CALCINATOR_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Project3.MOD_ID, "calcinator_be"),
                    FabricBlockEntityTypeBuilder.create(CalcinatorBlockEntity::new,
                            Blocks.CALCINATOR).build());
    public static final BlockEntityType<GlassBellBlockEntity> GLASS_BELL_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Project3.MOD_ID, "glass_bell_be"),
                    FabricBlockEntityTypeBuilder.create(GlassBellBlockEntity::new,
                            Blocks.GLASS_BELL).build());
    public static final BlockEntityType<AludelBaseBlockEntity> ALUDEL_BASE_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Project3.MOD_ID, "aludel_base_be"),
                    FabricBlockEntityTypeBuilder.create(AludelBaseBlockEntity::new,
                            Blocks.ALUDEL_BASE).build());
    public static final BlockEntityType<ResearchStationBlockEntity> RESEARCH_STATION_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Project3.MOD_ID, "research_station_be"),
                    FabricBlockEntityTypeBuilder.create(ResearchStationBlockEntity::new,
                            Blocks.RESEARCH_STATION).build());
    public static final BlockEntityType<TabletBlockEntity> TRANSMUTATION_TABLET_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Project3.MOD_ID, "transmutation_tablet_be"),
                    FabricBlockEntityTypeBuilder.create(TabletBlockEntity::new,
                            Blocks.TABLET_BLOCK).build());

    public static void registerBlockEntities() {
        Project3.LOGGER.info("Registering Block Entities for " + Project3.MOD_ID);
    }
}
