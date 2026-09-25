package com.iroplisk.project3.inventory;

import com.iroplisk.project3.VanillaModifiers;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class SellSlot extends Slot {
    public SellSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return VanillaModifiers.hasEmc(stack);
    }
}
