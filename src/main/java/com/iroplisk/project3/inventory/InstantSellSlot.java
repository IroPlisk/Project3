package com.iroplisk.project3.inventory;

import com.iroplisk.project3.VanillaModifiers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;

public class InstantSellSlot extends Slot {
    private final PlayerEntity player;

    public InstantSellSlot(Inventory inventory, int index, int x, int y, PlayerEntity player) {
        super(inventory, index, x, y);
        this.player = player;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return VanillaModifiers.hasEmc(stack);
    }

    @Override
    public void setStack(ItemStack stack) {
        if (!stack.isEmpty() && !player.getWorld().isClient) {
            long emc = VanillaModifiers.getEmc(stack) * stack.getCount();
            if (emc > 0) {
                PlayerEMC.get((ServerWorld) player.getWorld()).addEmc(player.getUuid(), emc);
                super.setStack(ItemStack.EMPTY);
                return;
            }
        }
        super.setStack(stack);
    }
}
