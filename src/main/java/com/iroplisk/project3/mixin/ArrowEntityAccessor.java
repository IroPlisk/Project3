package com.iroplisk.project3.mixin;

import net.minecraft.entity.projectile.ArrowEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ArrowEntity.class)
public interface ArrowEntityAccessor {
    @Invoker("setColor")
    void project3$setColor(int color);
}