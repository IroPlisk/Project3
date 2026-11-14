package com.iroplisk.project3.item.custom;

import com.iroplisk.project3.mixin.ArrowEntityAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DarkMatterArrow extends ArrowItem {
    public static final int DARK_COLOR = 0x7B2FBE;

    public DarkMatterArrow(Settings settings) { super(settings); }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        ArrowEntity arrow = new ArrowEntity(world, shooter);
        ((ArrowEntityAccessor) arrow).project3$setColor(DARK_COLOR);
        arrow.setDamage(arrow.getDamage() + 2.0);
        return arrow;
    }
}
