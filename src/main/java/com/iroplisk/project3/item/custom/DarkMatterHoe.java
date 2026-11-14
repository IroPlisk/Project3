package com.iroplisk.project3.item.custom;

import com.iroplisk.project3.inventory.ChargeableTool;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class DarkMatterHoe extends HoeItem implements ChargeableTool {
    public DarkMatterHoe(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        ActionResult result = super.useOnBlock(ctx);
        if (!result.isAccepted() || ctx.getWorld().isClient) return result;

        int r = ChargeableTool.getCharge(ctx.getStack());
        BlockPos center = ctx.getBlockPos();
        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {
                if (dx == 0 && dz == 0) continue;
                if (ctx.getStack().isEmpty()) return result;
                BlockPos p = center.add(dx, 0, dz);
                BlockHitResult hit = new BlockHitResult(Vec3d.ofCenter(p), Direction.UP, p, false);
                super.useOnBlock(new ItemUsageContext(ctx.getWorld(), ctx.getPlayer(), ctx.getHand(), ctx.getStack(), hit));
            }
        }
        return result;
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

}
