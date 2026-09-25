package com.iroplisk.project3.inventory;

import com.iroplisk.project3.item.custom.TomeItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class TomeSlot extends Slot {
    public TomeSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof TomeItem;
    }
}