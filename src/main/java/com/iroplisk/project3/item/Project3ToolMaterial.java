package com.iroplisk.project3.item;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;



public enum Project3ToolMaterial implements ToolMaterial{
    DARK_MATTER(5, 0, 14.0f, 6.0f, 25);

    private final int material;
    private final int durability;
    private final float efficiency;
    private final float damage;
    private final int enchant;

    Project3ToolMaterial(int material, int durability, float efficiency, float damage, int enchant){
        this.material = material;
        this.durability = durability;
        this.efficiency = efficiency;
        this.damage = damage;
        this.enchant = enchant;
    }

    @Override
    public int getDurability() {
        return this.durability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.efficiency;
    }

    @Override
    public float getAttackDamage() {
        return this.damage;
    }

    @Override
    public int getMiningLevel() {
        return this.material;
    }

    @Override
    public int getEnchantability() {
        return this.enchant;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return null;
    }
}
