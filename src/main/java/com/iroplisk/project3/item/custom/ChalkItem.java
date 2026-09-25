package com.iroplisk.project3.item.custom;

import com.example.project3.block.Blocks;
import com.iroplisk.project3.block.custom.TabletPartBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChalkItem extends Item {
    public ChalkItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos centerPos = context.getBlockPos();

        if (world.isClient) return ActionResult.SUCCESS;
        if (!world.getBlockState(centerPos).isOf(Blocks.ASH_STONE)) {
            return ActionResult.FAIL;
        }
        if (!checkRing(world, centerPos)) {
            PlayerEntity player = context.getPlayer();
            if (player != null) {
                player.sendMessage(Text.literal("This needs a full ring of Ash Stones around it!"), true);
            }
            return ActionResult.FAIL;
        }

        setPart(world, centerPos, -1, -1, TabletPartBlock.Part.NW);
        setPart(world, centerPos, 0, -1, TabletPartBlock.Part.N);
        setPart(world, centerPos, 1, -1, TabletPartBlock.Part.NE);
        setPart(world, centerPos, -1, 0, TabletPartBlock.Part.W);
        setPart(world, centerPos, 1, 0, TabletPartBlock.Part.E);
        setPart(world, centerPos, -1, 1, TabletPartBlock.Part.SW);
        setPart(world, centerPos, 0, 1, TabletPartBlock.Part.S);
        setPart(world, centerPos, 1, 1, TabletPartBlock.Part.SE);
        world.setBlockState(centerPos, Blocks.TABLET_BLOCK.getDefaultState(), 3);

        if (context.getPlayer() == null || !context.getPlayer().getAbilities().creativeMode) {
            context.getStack().decrement(1);
        }
        return ActionResult.SUCCESS;
    }

    private void setPart(World world, BlockPos center, int dx, int dz, TabletPartBlock.Part part) {
        BlockPos pos = center.add(dx, 0, dz);
        world.setBlockState(pos, Blocks.TABLET_PART_BLOCK.getDefaultState().with(TabletPartBlock.PART, part), 3);
    }

    private boolean checkRing(World world, BlockPos center) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) continue;
                if (!world.getBlockState(center.add(dx, 0, dz)).isOf(Blocks.ASH_STONE)) {
                    return false;
                }
            }
        }
        return true;
    }
}