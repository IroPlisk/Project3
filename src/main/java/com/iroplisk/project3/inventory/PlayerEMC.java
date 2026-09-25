package com.iroplisk.project3.inventory;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerEMC extends PersistentState {
    private final Map<UUID, Long> balances = new HashMap<>();

    public static PlayerEMC get(ServerWorld world) {
        ServerWorld overworld = world.getServer().getOverworld();
        PersistentStateManager manager = overworld.getPersistentStateManager();
        return manager.getOrCreate(
                PlayerEMC::fromNbt,
                PlayerEMC::new,
                "project3_player_emc"
        );
    }


    public long getEmc(UUID playerId) {
        return balances.getOrDefault(playerId, 0L);
    }

    public void addEmc(UUID playerId, long amount) {
        if (amount <= 0) return;
        balances.merge(playerId, amount, Long::sum);
        markDirty();
    }

    public boolean trySpendEmc(UUID playerId, long amount) {
        if (amount <= 0) return true;
        long current = getEmc(playerId);
        if (current < amount) return false;
        balances.put(playerId, current - amount);
        markDirty();
        return true;
    }

    public void setEmc(UUID playerId, long amount) {
        balances.put(playerId, Math.max(0, amount));
        markDirty();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound data = new NbtCompound();
        for (Map.Entry<UUID, Long> entry : balances.entrySet()) {
            data.putLong(entry.getKey().toString(), entry.getValue());
        }
        nbt.put("Balances", data);
        return nbt;
    }

    public static PlayerEMC fromNbt(NbtCompound nbt) {
        PlayerEMC storage = new PlayerEMC();
        NbtCompound data = nbt.getCompound("Balances");
        for (String key : data.getKeys()) {
            storage.balances.put(UUID.fromString(key), data.getLong(key));
        }
        return storage;
    }
}
