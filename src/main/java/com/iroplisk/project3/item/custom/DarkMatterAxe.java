package com.iroplisk.project3.item.custom;

import com.iroplisk.project3.inventory.ChargeableTool;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DarkMatterAxe extends AxeItem implements ChargeableTool {
    public DarkMatterAxe(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    private static final int MAX_LOGS = 128;
    private static boolean felling = false;

    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            ItemStack stack = player.getMainHandStack();
            if (felling || world.isClient || player.isSneaking()
                    || !(stack.getItem() instanceof DarkMatterAxe axe)
                    || !state.isIn(BlockTags.LOGS)
                    || !(player instanceof ServerPlayerEntity sp)) {
                return true;
            }

            float cookChance = (float) ChargeableTool.getCharge(stack) / axe.getMaxCharge();
            Random random = world.getRandom();

            felling = true;
            try {
                for (BlockPos p : findTree(world, pos)) {
                    if (stack.isEmpty()) break;

                    if (random.nextFloat() < cookChance) {
                        if (!world.canPlayerModifyAt(player, p)) continue;
                        world.breakBlock(p, false, player);
                        Block.dropStack(world, p, new ItemStack(net.minecraft.item.Items.CHARCOAL));
                        stack.damage(1, player, e -> e.sendToolBreakStatus(Hand.MAIN_HAND));
                    } else {
                        sp.interactionManager.tryBreakBlock(p);
                    }
                }
            } finally {
                felling = false;
            }
            return false;
        });
    }

    private static List<BlockPos> findTree(World world, BlockPos start) {
        List<BlockPos> found = new ArrayList<>();
        Set<BlockPos> seen = new HashSet<>();
        Deque<BlockPos> queue = new ArrayDeque<>();
        queue.add(start);
        seen.add(start);

        while (!queue.isEmpty() && found.size() < MAX_LOGS) {
            BlockPos current = queue.poll();
            found.add(current);
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) continue;
                        BlockPos next = current.add(dx, dy, dz);
                        if (seen.add(next) && world.getBlockState(next).isIn(BlockTags.LOGS)) {
                            queue.add(next);
                        }
                    }
                }
            }
        }
        return found;
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }
}
