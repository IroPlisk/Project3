package com.iroplisk.project3;

import com.example.project3.block.Blocks;
import com.iroplisk.project3.item.Items;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CreativeTabProject3 {

    public static final ItemGroup CREATIVE_TAB_PROJECT3 = Registry.register(Registries.ITEM_GROUP, new Identifier(Project3.MOD_ID, "project3"), FabricItemGroup.builder()
            .displayName(Text.literal("Project3"))
            .icon(() -> new ItemStack(Items.MINIUM_SHARD)).entries(((displayContext, entries) -> {
                entries.add(Items.MINIUM_SHARD);
                entries.add(Items.INERT_STONE);

                entries.add(Items.MINIUM_STONE);
                entries.add(Items.PHILOSOPHERS_STONE);
                entries.add(Items.ALCHEMICAL_BAG_SMALL);
                entries.add(Items.ALCHEMICAL_BAG_MEDIUM);
                entries.add(Items.ALCHEMICAL_BAG_LARGE);
                entries.add(Items.KNOWLEDGE_TOME);
                entries.add(Items.CHALK);
                entries.add(Items.CRYSTAL_BALL);

                entries.add(Items.DUST_ASH);
                entries.add(Items.DUST_VERDANT);
                entries.add(Items.DUST_AZURE);
                entries.add(Items.DUST_MINIUM);
                entries.add(Items.DUST_AMARANTHINE);
                entries.add(Items.DUST_IRIDESCENT);

                entries.add(Blocks.ASH_STONE);

                entries.add(Blocks.CALCINATOR);
                entries.add((Blocks.GLASS_BELL));
                entries.add(Blocks.ALUDEL_BASE);
                entries.add(Blocks.RESEARCH_STATION);
            })).build());

    public static void CreativeTabReg(){
        Project3.LOGGER.info("Loading Project3's Creative Tab");
    }
}
