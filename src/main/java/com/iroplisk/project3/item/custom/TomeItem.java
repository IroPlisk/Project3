package com.iroplisk.project3.item.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TomeItem extends Item {
    public TomeItem(Settings settings) {
        super(settings.maxCount(1).maxDamage(2048));
    }

    public static boolean hasLearned(ItemStack tomeStack, Identifier itemId) {
        return getLearnedIds(tomeStack).contains(itemId.toString());
    }

    public static boolean learnItem(ItemStack tomeStack, Identifier itemId) {
        if (hasLearned(tomeStack, itemId)) return false;
        NbtCompound nbt = tomeStack.getOrCreateNbt();
        NbtList list = nbt.getList("Learned", 8);
        list.add(NbtString.of(itemId.toString()));
        nbt.put("Learned", list);
        return true;
    }

    public static List<Item> getLearnedItems(ItemStack tomeStack) {
        List<Item> items = new ArrayList<>();
        for (String idStr : getLearnedIds(tomeStack)) {
            Identifier id = Identifier.tryParse(idStr);
            if (id != null) {
                Item item = Registries.ITEM.get(id);
                items.add(item);
            }
        }
        return items;
    }

    private static List<String> getLearnedIds(ItemStack tomeStack) {
        List<String> ids = new ArrayList<>();
        if (!tomeStack.hasNbt()) return ids;
        NbtCompound nbt = tomeStack.getNbt();
        NbtList list = nbt.getList("Learned", 8);
        for (int i = 0; i < list.size(); i++) {
            ids.add(list.getString(i));
        }
        return ids;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, net.minecraft.entity.player.PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) {
            MinecraftClient.getInstance().setScreen(new com.iroplisk.project3.screen.TomeScreen(stack));
        }
        return TypedActionResult.success(stack, world.isClient);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal("An alchemist's best friend -2nd edition"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
