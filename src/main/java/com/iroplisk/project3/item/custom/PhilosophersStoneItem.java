package com.iroplisk.project3.item.custom;

import com.iroplisk.project3.Project3;
import com.iroplisk.project3.inventory.ChargeableTool;
import com.iroplisk.project3.inventory.Project3Sounds;
import com.iroplisk.project3.item.Items;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PhilosophersStoneItem extends Item  implements ChargeableTool {
    public static final long FLASH_MS = 600;
    public static long flashStartMs = Long.MIN_VALUE;

    public PhilosophersStoneItem(Settings settings) {
        super(settings.maxCount(1).recipeRemainder(Items.PHILOSOPHERS_STONE));
    }

    private static final Map<Block, Block> TRANSMUTATIONS = Map.of(
            Blocks.DIRT, Blocks.COBBLESTONE,
            Blocks.COBBLESTONE, Blocks.DIRT,
            Blocks.GRASS_BLOCK, Blocks.STONE,
            Blocks.STONE, Blocks.GRASS_BLOCK,
            Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN,
            Blocks.CRYING_OBSIDIAN, Blocks.LAVA,
            Blocks.LAVA, Blocks.OBSIDIAN
    );

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        Block from = world.getBlockState(pos).getBlock();
        Block to = TRANSMUTATIONS.get(from);
        if (to == null) {
            return ActionResult.PASS;
        }

        if (world.isClient) {
            flashStartMs = Util.getMeasuringTimeMs();
        } else {
            PlayerEntity player = context.getPlayer();
            int r = ChargeableTool.getCharge(context.getStack());

            for (int dx = -r; dx <= r; dx++) {
                for (int dy = -r; dy <= r; dy++) {
                    for (int dz = -r; dz <= r; dz++) {
                        BlockPos p = pos.add(dx, dy, dz);
                        if (!world.getBlockState(p).isOf(from)) continue;
                        if (player != null && !world.canPlayerModifyAt(player, p)) continue;
                        world.setBlockState(p, to.getDefaultState());
                    }
                }
            }
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
                    Project3Sounds.PHILOSOPHERS_STONE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }
        return ActionResult.success(world.isClient);
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
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
