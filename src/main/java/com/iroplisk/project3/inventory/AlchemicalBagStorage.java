package com.iroplisk.project3.inventory;

import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AlchemicalBagStorage extends PersistentState {
    private final Map<String, SimpleInventory> inventories = new HashMap<>();

    public static AlchemicalBagStorage get(ServerWorld world) {
        PersistentStateManager manager = world.getPersistentStateManager();
        return manager.getOrCreate(
                AlchemicalBagStorage::fromNbt,
                AlchemicalBagStorage::new,
                "project3_alchemical_bags"
        );
    }

    private static String key(UUID playerId, int tier) {
        return playerId + "_" + tier;
    }

    public SimpleInventory getInventory(UUID playerId, int tier, int slotCount) {
        return inventories.computeIfAbsent(key(playerId, tier), k -> {
            SimpleInventory inv = new SimpleInventory(slotCount) {
                @Override
                public void markDirty() {
                    super.markDirty();
                    AlchemicalBagStorage.this.markDirty();
                }
            };
            return inv;
        });
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        for (Map.Entry<String, SimpleInventory> entry : inventories.entrySet()) {
            NbtCompound invNbt = new NbtCompound();
            DefaultedList<ItemStack> items = DefaultedList.ofSize(entry.getValue().size(), ItemStack.EMPTY);
            for (int i = 0; i < entry.getValue().size(); i++) {
                items.set(i, entry.getValue().getStack(i));
            }
            Inventories.writeNbt(invNbt, items);
            invNbt.putInt("Size", entry.getValue().size());
            nbt.put(entry.getKey(), invNbt);
        }
        return nbt;
    }

    public static AlchemicalBagStorage fromNbt(NbtCompound nbt) {
        AlchemicalBagStorage storage = new AlchemicalBagStorage();
        for (String key : nbt.getKeys()) {
            NbtCompound invNbt = nbt.getCompound(key);
            int size = invNbt.getInt("Size");
            DefaultedList<ItemStack> items = DefaultedList.ofSize(size, ItemStack.EMPTY);
            Inventories.readNbt(invNbt, items);
            SimpleInventory inv = new SimpleInventory(size) {
                @Override
                public void markDirty() {
                    super.markDirty();
                    storage.markDirty();
                }
            };
            for (int i = 0; i < size; i++) {
                inv.setStack(i, items.get(i));
            }
            storage.inventories.put(key, inv);
        }
        return storage;
    }
}
