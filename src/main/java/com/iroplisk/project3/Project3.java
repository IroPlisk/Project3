package com.iroplisk.project3;

import com.iroplisk.project3.block.BlockEntities;
import com.iroplisk.project3.item.Items;
import com.iroplisk.project3.recipes.AludelRecipes;
import com.iroplisk.project3.screen.Project3ScreenHandlers;
import net.fabricmc.api.ModInitializer;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Project3 implements ModInitializer {
	public static final String MOD_ID = "project3";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Starting Project3 Initialization");
		Items.itemReg();
		CustomLootTables.CustomLootProject3();
		CreativeTabProject3.CreativeTabReg();
		VanillaModifiers.AddTooltips();
		com.example.project3.block.Blocks.registerModBlocks();
		BlockEntities.registerBlockEntities();
		Project3ScreenHandlers.registerScreenHandlers();
		Registry.register(Registries.RECIPE_TYPE, new Identifier("project3", "aludel"), AludelRecipes.Type.INSTANCE);
		Registry.register(Registries.RECIPE_SERIALIZER, AludelRecipes.Serializer.ID, AludelRecipes.Serializer.INSTANCE);
	}
}