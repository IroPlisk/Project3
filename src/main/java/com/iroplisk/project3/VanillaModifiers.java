package com.iroplisk.project3;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class VanillaModifiers {

    private static volatile Map<Item, Long> ITEM_EMC = new HashMap<>();
    private static volatile Map<TagKey<Item>, Long> TAG_EMC = new LinkedHashMap<>();

    public static void register() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA)
                .registerReloadListener(new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return new Identifier("project3", "emc_values");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        Map<Item, Long> items = new HashMap<>();
                        Map<TagKey<Item>, Long> tags = new LinkedHashMap<>();

                        Identifier file = new Identifier("project3", "emc/emc.json");
                        manager.getResource(file).ifPresentOrElse(resource -> {
                            try (Reader reader = resource.getReader()) {
                                JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
                                for (Map.Entry<String, JsonElement> e : obj.entrySet()) {
                                    String key = e.getKey();
                                    long value = e.getValue().getAsLong();
                                    if (key.startsWith("#")) {
                                        Identifier tagId = Identifier.tryParse(key.substring(1));
                                        if (tagId != null) tags.put(TagKey.of(RegistryKeys.ITEM, tagId), value);
                                    } else {
                                        Identifier itemId = Identifier.tryParse(key);
                                        if (itemId == null) continue;
                                        Registries.ITEM.getOrEmpty(itemId).ifPresentOrElse(
                                                item -> items.put(item, value),
                                                () -> Project3.LOGGER.warn("EMC: unknown item {}", key));
                                    }
                                }
                            } catch (Exception ex) {
                                Project3.LOGGER.error("Failed to load {}", file, ex);
                            }
                        }, () -> Project3.LOGGER.error("Missing EMC file {}", file));

                        ITEM_EMC = items;
                        TAG_EMC = tags;
                    }
                });
    }

    public static long getEmc(ItemStack stack) {
        Long direct = ITEM_EMC.get(stack.getItem());
        if (direct != null) return direct;
        for (Map.Entry<TagKey<Item>, Long> e : TAG_EMC.entrySet()) {
            if (stack.isIn(e.getKey())) return e.getValue();
        }
        return 0;
    }

    public static boolean hasEmc(ItemStack stack) {
        return getEmc(stack) > 0;
    }

    public static void AddTooltips() {
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            long emc = getEmc(stack);
            if (emc > 0) {
                lines.add(Text.literal("EMC: ").formatted(Formatting.AQUA)
                        .append(Text.literal(String.valueOf(emc)).formatted(Formatting.WHITE)));
            }
        });
    }
}