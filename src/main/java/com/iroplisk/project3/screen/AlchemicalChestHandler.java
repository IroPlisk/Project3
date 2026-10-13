package com.iroplisk.project3.screen;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;

public class AlchemicalChestHandler extends AlchemicalBagHandler{
    public AlchemicalChestHandler(int syncId, PlayerInventory inv, Inventory chestInv, int tier) {
        super(syncId, inv, chestInv, tier, chestType(tier));
    }

    public AlchemicalChestHandler(int syncId, PlayerInventory inv, PacketByteBuf buf) {
        this(syncId, inv, buf.readInt(), buf.readInt());
    }

    private AlchemicalChestHandler(int syncId, PlayerInventory inv, int tier, int slotCount) {
        this(syncId, inv, new SimpleInventory(slotCount), tier);
    }

    private static ScreenHandlerType<?> chestType(int tier) {
        return switch (tier) {
            case 2 -> Project3ScreenHandlers.ALCHEMICAL_CHEST_AZURE_SCREEN_HANDLER;
            case 3 -> Project3ScreenHandlers.ALCHEMICAL_CHEST_MINIUM_SCREEN_HANDLER;
            default -> Project3ScreenHandlers.ALCHEMICAL_CHEST_VERDANT_SCREEN_HANDLER;
        };
    }
}
