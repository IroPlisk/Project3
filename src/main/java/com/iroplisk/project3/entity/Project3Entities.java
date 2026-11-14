package com.iroplisk.project3.entity;

import com.iroplisk.project3.Project3;
import com.iroplisk.project3.entity.custom.ProtoMatterEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Project3Entities {
    public static final EntityType<ProtoMatterEntity> PROTO_MATTER_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Project3.MOD_ID, "proto_matter"),
            FabricEntityTypeBuilder.<ProtoMatterEntity>create(SpawnGroup.MISC, ProtoMatterEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(4)
                    .trackedUpdateRate(10)
                    .build()
    );

    public static void registerEntities() {
        Project3.LOGGER.info("Registering Project3 Entities");
    }
}
