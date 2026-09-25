package com.iroplisk.project3.block.custom;

import com.iroplisk.project3.block.BlockEntities;
import com.iroplisk.project3.block.entity.AludelBaseBlockEntity;
import com.iroplisk.project3.block.entity.CalcinatorBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AludelBaseBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final VoxelShape SHAPE = Block.createCuboidShape(0,0,0,16,16,16);

    public AludelBaseBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AludelBaseBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof AludelBaseBlockEntity aludel) {
                if (!aludel.isFormed()) {
                    player.sendMessage(Text.literal("The Aludel needs a Glass Bell on top!"), true); // action bar message, optional
                    return ActionResult.SUCCESS;
                }
                player.openHandledScreen(aludel);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, BlockEntities.ALUDEL_BASE_BLOCK_ENTITY,
                (world1, pos, state1, blockEntity) -> AludelBaseBlockEntity.tick(world1, pos, state1, (AludelBaseBlockEntity) blockEntity)
        );
    }
}
