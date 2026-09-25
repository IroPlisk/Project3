package com.iroplisk.project3.block.custom;

import com.iroplisk.project3.block.entity.GlassBellBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GlassBellBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 12, 16);

    public GlassBellBlock(Settings settings) {
        super(settings.luminance(state -> 1));
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GlassBellBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof GlassBellBlockEntity bell) {
            ItemStack itemInHand = player.getStackInHand(hand);
            ItemStack itemInBell = bell.getStack(0);

            if (itemInBell.isEmpty() && !itemInHand.isEmpty()) {
                if (!world.isClient) {
                    bell.setStack(0, itemInHand.copyWithCount(1));
                    if (!player.getAbilities().creativeMode) {
                        itemInHand.decrement(1);
                    }
                    bell.markDirtyAndSync();
                }
                return ActionResult.success(world.isClient);
            }
            else if (!itemInBell.isEmpty() && itemInHand.isEmpty()) {
                if (!world.isClient) {
                    player.setStackInHand(hand, itemInBell.copy());
                    bell.setStack(0, ItemStack.EMPTY);
                    bell.markDirtyAndSync();
                }
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.PASS;
    }

}
