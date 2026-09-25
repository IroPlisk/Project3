package com.iroplisk.project3.recipes;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class AludelRecipes implements Recipe<Inventory> {
    private final Identifier id;
    private final Ingredient topInput;
    private final int topCount;
    private final Ingredient bottomInput;
    private final int bottomCount;
    private final ItemStack output;
    private final int cookTime;

    public AludelRecipes(Identifier id, Ingredient topInput, int topCount, Ingredient bottomInput, int bottomCount, ItemStack output, int cookTime) {
        this.id = id;
        this.topInput = topInput;
        this.topCount = topCount;
        this.bottomInput = bottomInput;
        this.bottomCount = bottomCount;
        this.output = output;
        this.cookTime = cookTime;
    }

    public int getTopCount() { return topCount; }
    public int getBottomCount() { return bottomCount; }
    public int getCookTime() { return cookTime; }

    @Override
    public boolean matches(Inventory inv, World world) {
        ItemStack top = inv.getStack(0);
        ItemStack bottom = inv.getStack(1);
        return topInput.test(top) && top.getCount() >= topCount
                && bottomInput.test(bottom) && bottom.getCount() >= bottomCount;
    }

    @Override
    public ItemStack craft(Inventory inv, DynamicRegistryManager registryManager) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return output;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(topInput);
        list.add(bottomInput);
        return list;
    }

    public static class Type implements RecipeType<AludelRecipes> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "aludel";
    }

    public static class Serializer implements RecipeSerializer<AludelRecipes> {
        public static final Serializer INSTANCE = new Serializer();
        public static final Identifier ID = new Identifier("project3", "aludel");

        @Override
        public AludelRecipes read(Identifier id, JsonObject json) {
            JsonObject topJson = JsonHelper.getObject(json, "top_input");
            Ingredient top = Ingredient.fromJson(topJson);
            int topCount = JsonHelper.getInt(topJson, "count", 1);

            JsonObject bottomJson = JsonHelper.getObject(json, "bottom_input");
            Ingredient bottom = Ingredient.fromJson(bottomJson);
            int bottomCount = JsonHelper.getInt(bottomJson, "count", 1);

            JsonObject resultJson = JsonHelper.getObject(json, "result");
            Identifier itemId = new Identifier(JsonHelper.getString(resultJson, "item"));
            int count = JsonHelper.getInt(resultJson, "count", 1);
            ItemStack output = new ItemStack(Registries.ITEM.get(itemId), count);

            int cookTime = JsonHelper.getInt(json, "cook_time", 100);
            return new AludelRecipes(id, top, topCount, bottom, bottomCount, output, cookTime);
        }

        @Override
        public AludelRecipes read(Identifier id, PacketByteBuf buf) {
            Ingredient top = Ingredient.fromPacket(buf);
            int topCount = buf.readInt();
            Ingredient bottom = Ingredient.fromPacket(buf);
            int bottomCount = buf.readInt();
            ItemStack output = buf.readItemStack();
            int cookTime = buf.readInt();
            return new AludelRecipes(id, top, topCount, bottom, bottomCount, output, cookTime);
        }

        @Override
        public void write(PacketByteBuf buf, AludelRecipes recipe) {
            recipe.topInput.write(buf);
            buf.writeInt(recipe.topCount);
            recipe.bottomInput.write(buf);
            buf.writeInt(recipe.bottomCount);
            buf.writeItemStack(recipe.output);
            buf.writeInt(recipe.cookTime);
        }
    }
}