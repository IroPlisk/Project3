package com.iroplisk.project3.item;

import com.iroplisk.project3.Project3;
import com.iroplisk.project3.item.custom.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class Items {
    public static final Item INERT_STONE = registerItem("inert_stone", new Item(new FabricItemSettings()));
    public static final Item MINIUM_SHARD = registerItem("minium_shard", new Item(new FabricItemSettings()));

    public static final Item MINIUM_STONE = registerItem("minium_stone", new MiniumStoneItem(new FabricItemSettings()));
    public static final Item PHILOSOPHERS_STONE = registerItem("philosophers_stone", new PhilosophersStoneItem(new FabricItemSettings()));
    public static final Item ALCHEMICAL_BAG_SMALL = registerItem("alchemical_bag_small", new AlchemicalBagItem(new FabricItemSettings(), 1, 48));
    public static final Item ALCHEMICAL_BAG_MEDIUM = registerItem("alchemical_bag_medium", new AlchemicalBagItem(new FabricItemSettings(), 2, 84));
    public static final Item ALCHEMICAL_BAG_LARGE = registerItem("alchemical_bag_large", new AlchemicalBagItem(new FabricItemSettings(), 3, 117));
    public static final Item KNOWLEDGE_TOME = registerItem("knowledge_tome", new TomeItem(new FabricItemSettings()));
    public static final Item CHALK = registerItem("chalk", new ChalkItem(new FabricItemSettings()));
    public static final Item CRYSTAL_BALL = registerItem("crystal_ball", new CrystalBallItem(new FabricItemSettings()));

    public static final Item DUST_ASH = registerItem("dust_ash", new Item(new FabricItemSettings())); // EMC 1
    public static final Item DUST_VERDANT = registerItem("dust_verdant", new Item(new FabricItemSettings()));
    public static final Item DUST_AZURE = registerItem("dust_azure", new Item(new FabricItemSettings()));
    public static final Item DUST_MINIUM = registerItem("dust_minium", new Item(new FabricItemSettings())); // EMC 8192
    public static final Item DUST_AMARANTHINE = registerItem("dust_amaranthine", new Item(new FabricItemSettings()));
    public static final Item DUST_IRIDESCENT = registerItem("dust_iridescent", new Item(new FabricItemSettings().rarity(Rarity.EPIC)){
        @Override
        public boolean hasGlint(ItemStack stack) {
            return true;
        }
    });

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(Project3.MOD_ID, name), item);
    }

    public static void itemReg(){
        Project3.LOGGER.info("Registering Project3 items");

    }


}
