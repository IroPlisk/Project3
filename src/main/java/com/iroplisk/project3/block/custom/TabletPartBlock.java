package com.iroplisk.project3.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class TabletPartBlock extends Block {
    public static final VoxelShape SLAB_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 8, 16);
    public static final EnumProperty<Part> PART = EnumProperty.of("part", Part.class);

    public TabletPartBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(PART, Part.N));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PART);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SLAB_SHAPE;
    }

    public enum Part implements StringIdentifiable {
        N, S, E, W, NE, NW, SE, SW;

        @Override
        public String asString() {
            return name().toLowerCase();
        }
    }
}