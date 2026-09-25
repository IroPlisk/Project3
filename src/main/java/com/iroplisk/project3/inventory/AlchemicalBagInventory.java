package com.iroplisk.project3.inventory;

import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

public class AlchemicalBagInventory implements Inventory {
    private final ItemStack bagStack;
    private final DefaultedList<ItemStack> items;
    private final int size;

    public AlchemicalBagInventory(ItemStack bagStack, int size) {
        this.bagStack = bagStack;
        this.size = size;
        this.items = DefaultedList.ofSize(size, ItemStack.EMPTY);
        NbtCompound nbt = bagStack.getOrCreateNbt();
        if (!nbt.containsUuid("BagId")) {
            nbt.putUuid("BagId", java.util.UUID.randomUUID());
        }
        if (nbt.contains("Items")) {
            Inventories.readNbt(nbt, items);
        }
    }

    @Override
    public int size() { return size; }

    public ItemStack getBagStack() {
        return bagStack;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = net.minecraft.inventory.Inventories.splitStack(items, slot, amount);
        if (!result.isEmpty()) markDirty();
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return net.minecraft.inventory.Inventories.removeStack(items, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
        markDirty();
    }

    @Override
    public void markDirty() {
        NbtCompound nbt = bagStack.getOrCreateNbt();
        Inventories.writeNbt(nbt, items);
    }

    @Override
    public boolean canPlayerUse(net.minecraft.entity.player.PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        items.clear();
        markDirty();
    }

}
