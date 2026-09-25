package com.example.project3.block;

import com.iroplisk.project3.Project3;
import com.iroplisk.project3.block.custom.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Blocks {

    public static final Block CALCINATOR = registerBlock("calcinator",
            new CalcinatorBlock(FabricBlockSettings.copyOf(net.minecraft.block.Blocks.STONE).nonOpaque()));
    public static final Block GLASS_BELL = registerBlock("glass_bell",
            new GlassBellBlock(FabricBlockSettings.copyOf(net.minecraft.block.Blocks.GLASS).nonOpaque()));
    public static final Block ALUDEL_BASE = registerBlock("aludel_base",
            new AludelBaseBlock(FabricBlockSettings.copyOf(net.minecraft.block.Blocks.IRON_BLOCK).nonOpaque()));
    public static final Block RESEARCH_STATION = registerBlock("research_station",
            new ResearchStationBlock(FabricBlockSettings.copyOf(net.minecraft.block.Blocks.OAK_WOOD).nonOpaque()));
    public static final Block TABLET_BLOCK = registerBlock("tablet_block",
            new TabletBlock(FabricBlockSettings.copyOf(net.minecraft.block.Blocks.OBSIDIAN)));
    public static final Block TABLET_PART_BLOCK = registerBlock("tablet_part_block",
            new TabletPartBlock(FabricBlockSettings.copyOf(net.minecraft.block.Blocks.OBSIDIAN)));

    public static final Block ASH_STONE = registerBlock("ash_stone",
            new Block(FabricBlockSettings.copyOf(net.minecraft.block.Blocks.POLISHED_ANDESITE)));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(Project3.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(Project3.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        Project3.LOGGER.info("Registering ModBlocks for " + Project3.MOD_ID);
    }
}
