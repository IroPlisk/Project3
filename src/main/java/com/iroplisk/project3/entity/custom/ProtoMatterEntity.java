package com.iroplisk.project3.entity.custom;

import com.iroplisk.project3.Project3;
import com.iroplisk.project3.entity.Project3Entities;
import com.iroplisk.project3.item.Items;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class ProtoMatterEntity extends ThrownItemEntity {

    public ProtoMatterEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public ProtoMatterEntity(World world, LivingEntity owner) {
        super(Project3Entities.PROTO_MATTER_ENTITY, owner, world);
    }

    public ProtoMatterEntity(World world, double x, double y, double z) {
        super(Project3Entities.PROTO_MATTER_ENTITY, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.PROTO_MATTER;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.getWorld().createExplosion(
                    this,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    6.5F,
                    World.ExplosionSourceType.MOB
            );
            this.discard();
        }
    }
}

