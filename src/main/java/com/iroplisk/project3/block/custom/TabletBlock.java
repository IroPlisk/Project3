package com.iroplisk.project3.block.custom;

import com.example.project3.block.Blocks;
import com.iroplisk.project3.block.entity.TabletBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TabletBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final VoxelShape SLAB_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 8, 16);

    public TabletBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TabletBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SLAB_SHAPE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof TabletBlockEntity tablet) {
                player.openHandledScreen(tablet);
            }
        }
        return ActionResult.SUCCESS;
    }


    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
        if (!world.isClient && !ringIntact(world, pos)) {
            revertAll(world, pos);
        }
    }

    private boolean ringIntact(World world, BlockPos center) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) continue;
                if (!world.getBlockState(center.add(dx, 0, dz)).isOf(Blocks.TABLET_PART_BLOCK)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void revertAll(World world, BlockPos center) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos p = center.add(dx, 0, dz);
                if (dx == 0 && dz == 0) {
                    world.setBlockState(p, Blocks.ASH_STONE.getDefaultState(), 3);
                } else if (world.getBlockState(p).isOf(Blocks.TABLET_PART_BLOCK)) {
                    world.setBlockState(p, Blocks.ASH_STONE.getDefaultState(), 3);
                }
            }
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            if (!world.isClient) {
                revertRingOnly(world, pos);
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    private void revertRingOnly(World world, BlockPos center) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) continue;
                world.setBlockState(center.add(dx, 0, dz), Blocks.ASH_STONE.getDefaultState(), 3);
            }
        }
    }
}