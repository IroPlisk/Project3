package com.iroplisk.project3.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class ResearchStationInterfaceHandler extends ScreenHandler {
    private final Inventory tableInventory;
    private final PropertyDelegate propertyDelegate;

    public ResearchStationInterfaceHandler(int syncId, PlayerInventory playerInventory, Inventory tableInventory, PropertyDelegate propertyDelegate) {
        super(Project3ScreenHandlers.RESEARCH_STATION_SCREEN_HANDLER, syncId);
        this.tableInventory = tableInventory;
        this.propertyDelegate = propertyDelegate;
        tableInventory.onOpen(playerInventory.player);
        this.addProperties(propertyDelegate);

        this.addSlot(new Slot(tableInventory, 0, 79, 84));  // item to learn
        this.addSlot(new Slot(tableInventory, 1, 161, 84)); // tome

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 50 + col * 18, 152 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 50 + col * 18, 210));
        }
    }

    public ResearchStationInterfaceHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, resolveInventory(playerInventory, buf), new ArrayPropertyDelegate(2));
    }

    private static Inventory resolveInventory(PlayerInventory playerInventory, PacketByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        var be = playerInventory.player.getWorld().getBlockEntity(pos);
        if (be instanceof Inventory inv) return inv;
        return new net.minecraft.inventory.SimpleInventory(2);
    }

    public int getProgressScaled(int pixels) {
        int maxProgress = propertyDelegate.get(1);
        int progress = propertyDelegate.get(0);
        return maxProgress == 0 ? 0 : progress * pixels / maxProgress;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return tableInventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (index < 2) {
                if (!this.insertItem(originalStack, 2, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.insertItem(originalStack, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (originalStack.isEmpty()) slot.setStack(ItemStack.EMPTY);
            else slot.markDirty();
        }
        return newStack;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        tableInventory.onClose(player);
    }
}
