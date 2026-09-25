package com.iroplisk.project3.item.custom;

import com.iroplisk.project3.item.Items;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class MiniumStoneItem extends Item {
    public MiniumStoneItem(Settings settings) {
        super(settings.maxCount(1).maxDamage(2048));
    }

    @Override
    public boolean hasRecipeRemainder() {
        return true;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var block = context.getWorld().getBlockState(context.getBlockPos());
        var player = context.getPlayer();
        if (block.isOf(Blocks.DIRT)){
            context.getWorld().setBlockState(context.getBlockPos(), Blocks.COBBLESTONE.getDefaultState());
        } else if (block.isOf(Blocks.COBBLESTONE)) {
            context.getWorld().setBlockState(context.getBlockPos(), Blocks.DIRT.getDefaultState());
        } else if (block.isOf(Blocks.GRASS_BLOCK)) {
            context.getWorld().setBlockState(context.getBlockPos(), Blocks.STONE.getDefaultState());
        } else if (block.isOf(Blocks.STONE)) {
            context.getWorld().setBlockState(context.getBlockPos(), Blocks.GRASS_BLOCK.getDefaultState());
        }

        context.getStack().damage(1, player, (p) -> {
            p.sendToolBreakStatus(context.getHand());
        });
        return super.useOnBlock(context);
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        ItemStack after_item = stack.copy();
        after_item.setDamage(stack.getDamage()+1);

        if(after_item.getDamage() >= after_item.getMaxDamage()){
            return ItemStack.EMPTY;
        }

        return after_item;
    }

}
