package com.iroplisk.project3.item.custom;

import com.iroplisk.project3.item.Items;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class PhilosophersStoneItem extends Item {
    public PhilosophersStoneItem(Settings settings) {
        super(settings.maxCount(1).recipeRemainder(Items.PHILOSOPHERS_STONE));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var block = context.getWorld().getBlockState(context.getBlockPos());
        if (block.isOf(Blocks.DIRT)){
            context.getWorld().setBlockState(context.getBlockPos(), Blocks.COBBLESTONE.getDefaultState());
        } else if (block.isOf(Blocks.COBBLESTONE)) {
            context.getWorld().setBlockState(context.getBlockPos(), Blocks.DIRT.getDefaultState());
        } else if (block.isOf(Blocks.GRASS_BLOCK)) {
            context.getWorld().setBlockState(context.getBlockPos(), Blocks.STONE.getDefaultState());
        } else if (block.isOf(Blocks.OBSIDIAN)) {
            context.getWorld().setBlockState(context.getBlockPos(), Blocks.CRYING_OBSIDIAN.getDefaultState());
        } else if (block.isOf(Blocks.CRYING_OBSIDIAN)) {
            context.getWorld().setBlockState(context.getBlockPos(), Blocks.LAVA.getDefaultState());
        } else if (block.isOf(Blocks.LAVA)) {
            context.getWorld().setBlockState(context.getBlockPos(), Blocks.OBSIDIAN.getDefaultState());
        }

        return super.useOnBlock(context);
    }

    @Override
    public boolean hasRecipeRemainder() {
        return true;
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return stack.copy();
    }
}
