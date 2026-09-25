package com.iroplisk.project3;

import com.iroplisk.project3.block.BlockEntities;
import com.iroplisk.project3.block.entity.GlassBellEntityRenderer;
import com.iroplisk.project3.item.Items;
import com.iroplisk.project3.screen.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class Project3Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Project3ScreenHandlers.registerScreenHandlers();

        HandledScreens.register(Project3ScreenHandlers.CALCINATOR_SCREEN_HANDLER, CalcinatorInterface::new);
        HandledScreens.register(Project3ScreenHandlers.ALUDEL_SCREEN_HANDLER, AludelInterface::new);
        HandledScreens.register(Project3ScreenHandlers.ALCHEMICAL_BAG_SMALL_SCREEN_HANDLER, AlchemicalBagInterface::new);
        HandledScreens.register(Project3ScreenHandlers.ALCHEMICAL_BAG_MEDIUM_SCREEN_HANDLER, AlchemicalBagInterface::new);
        HandledScreens.register(Project3ScreenHandlers.ALCHEMICAL_BAG_LARGE_SCREEN_HANDLER, AlchemicalBagInterface::new);
        HandledScreens.register(Project3ScreenHandlers.RESEARCH_STATION_SCREEN_HANDLER, ResearchStationInterface::new);
        HandledScreens.register(Project3ScreenHandlers.TABLET_INTERFACE_SCREEN_HANDLER, TabletInterface::new);

        BlockRenderLayerMap.INSTANCE.putBlock(com.example.project3.block.Blocks.GLASS_BELL, RenderLayer.getTranslucent());
        BlockEntityRendererFactories.register(BlockEntities.GLASS_BELL_BLOCK_ENTITY, GlassBellEntityRenderer::new);
        BlockEntityRendererFactories.register(BlockEntities.TRANSMUTATION_TABLET_BLOCK_ENTITY, TabletBlockEntityRenderer::new);


        FabricModelPredicateProviderRegistry.register(
                Items.ALCHEMICAL_BAG_SMALL, new Identifier("open"),
                (stack, world, entity, seed) -> entity instanceof PlayerEntity player && isBagOpenFor(player, stack, 1) ? 1.0f : 0.0f
        );
        FabricModelPredicateProviderRegistry.register(
                Items.ALCHEMICAL_BAG_MEDIUM, new Identifier("open"),
                (stack, world, entity, seed) -> entity instanceof PlayerEntity player && isBagOpenFor(player, stack, 2) ? 1.0f : 0.0f
        );
        FabricModelPredicateProviderRegistry.register(
                Items.ALCHEMICAL_BAG_LARGE, new Identifier("open"),
                (stack, world, entity, seed) -> entity instanceof PlayerEntity player && isBagOpenFor(player, stack, 3) ? 1.0f : 0.0f
        );    }

    private static boolean isBagOpenFor(PlayerEntity player, ItemStack stack, int tier) {
        return player.currentScreenHandler instanceof AlchemicalBagHandler bagHandler && bagHandler.getTier() == tier;
    }
}
