package com.iroplisk.project3.item;

import com.iroplisk.project3.Project3;
import com.iroplisk.project3.item.custom.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
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
    public static final Item DUST_VERDANT = registerItem("dust_verdant", new Item(new FabricItemSettings())); // EMC 64
    public static final Item DUST_AZURE = registerItem("dust_azure", new Item(new FabricItemSettings())); // EMC 2048
    public static final Item DUST_MINIUM = registerItem("dust_minium", new Item(new FabricItemSettings())); // EMC 8192
    public static final Item DUST_AMARANTHINE = registerItem("dust_amaranthine", new Item(new FabricItemSettings())); // EMC 16384
    public static final Item DUST_IRIDESCENT = registerItem("dust_iridescent", new Item(new FabricItemSettings().rarity(Rarity.RARE)){
        @Override
        public boolean hasGlint(ItemStack stack) {
            return true;
        } // EMC 32768
    });
    public static final Item PROTO_MATTER = registerItem("proto_matter", new ProtoMatterItem(new FabricItemSettings()));
    public static final Item DARK_MATTER = registerItem("dark_matter", new Item(new FabricItemSettings().fireproof().rarity(Rarity.EPIC).fireproof()));

    public static final Item DM_SWORD = registerItem("dm_sword", new DarkMatterSword(Project3ToolMaterial.DARK_MATTER, 3, -2.4f, new FabricItemSettings().fireproof()));
    public static final Item DM_PICKAXE = registerItem("dm_pickaxe", new PickaxeItem(Project3ToolMaterial.DARK_MATTER, 1, -2.8f, new FabricItemSettings().fireproof()));
    public static final Item DM_AXE = registerItem("dm_axe", new DarkMatterAxe(Project3ToolMaterial.DARK_MATTER, 5, -3.0f, new FabricItemSettings().fireproof()));
    public static final Item DM_SHOVEL = registerItem("dm_shovel", new DarkMatterShovel(Project3ToolMaterial.DARK_MATTER, 1.5f, -3.0f, new FabricItemSettings().fireproof()));
    public static final Item DM_HOE = registerItem("dm_hoe", new DarkMatterHoe(Project3ToolMaterial.DARK_MATTER, -4, 0.0f, new FabricItemSettings().fireproof()));
    public static final Item DM_ARROW = registerItem("dm_arrow", new DarkMatterArrow(new FabricItemSettings().fireproof()));
    public static final Item DM_BOW = registerItem("dm_bow", new DarkMatterBow(new FabricItemSettings().fireproof()));
    public static final Item DM_SHEARS = registerItem("dm_shears", new DarkMatterShears(new FabricItemSettings().fireproof()));
    public static final Item DM_FISHING_ROD = registerItem("dm_fishing_rod", new FishingRodItem(new FabricItemSettings().fireproof().maxCount(1)));
    public static final Item DM_HAMMER = registerItem("dm_hammer", new DarkMatterHammer(Project3ToolMaterial.DARK_MATTER,2, -3.7f, new FabricItemSettings().fireproof()));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(Project3.MOD_ID, name), item);
    }

    public static void itemReg(){
        Project3.LOGGER.info("Registering Project3 items");
    }


}
