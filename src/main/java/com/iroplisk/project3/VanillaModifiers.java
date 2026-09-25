package com.iroplisk.project3;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class VanillaModifiers {

    public static final TagKey<Item> EMC_ONE_TAG = TagKey.of(RegistryKeys.ITEM, new Identifier("project3", "emc_one"));
    public static final TagKey<Item> EMC_8_TAG = TagKey.of(RegistryKeys.ITEM, new Identifier("project3", "emc_8"));
    public static final TagKey<Item> EMC_32_TAG = TagKey.of(RegistryKeys.ITEM, new Identifier("project3", "emc_32"));
    public static final TagKey<Item> EMC_64_TAG = TagKey.of(RegistryKeys.ITEM, new Identifier("project3", "emc_64"));
    public static final TagKey<Item> EMC_128_TAG = TagKey.of(RegistryKeys.ITEM, new Identifier("project3", "emc_128"));
    public static final TagKey<Item> EMC_8192_TAG = TagKey.of(RegistryKeys.ITEM, new Identifier("project3", "emc_8192"));


    public static void AddTooltips() {
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if (stack.isIn(EMC_ONE_TAG)) {
                lines.add(Text.literal("EMC: 1").formatted(Formatting.AQUA));
            }
            if (stack.isIn(EMC_8_TAG)){
                lines.add(Text.literal("EMC:8").formatted(Formatting.AQUA));
            }
            if(stack.isIn(EMC_32_TAG)){
                lines.add(Text.literal("EMC:32").formatted(Formatting.AQUA));
            }
            if(stack.isIn(EMC_64_TAG)){
                lines.add(Text.literal("EMC:64").formatted(Formatting.AQUA));
            }
            if(stack.isIn(EMC_128_TAG)){
                lines.add(Text.literal("EMC:128").formatted(Formatting.AQUA));
            }
            if (stack.isIn(EMC_8192_TAG)) {
                lines.add(Text.literal("EMC: 8192").formatted(Formatting.AQUA));
            }
        });
    }

    public static long getEmc(ItemStack stack) {
        if (stack.isIn(EMC_ONE_TAG)) return 1;
        if (stack.isIn(EMC_8_TAG)) return 8;
        if (stack.isIn(EMC_32_TAG)) return 32;
        if (stack.isIn(EMC_64_TAG)) return 64;
        if (stack.isIn(EMC_128_TAG)) return 128;
        if (stack.isIn(EMC_8192_TAG)) return 8192;
        return 0;
    }

    public static boolean hasEmc(ItemStack stack) {
        return getEmc(stack) > 0;
    }
}