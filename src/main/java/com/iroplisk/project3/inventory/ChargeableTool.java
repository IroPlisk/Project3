package com.iroplisk.project3.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public interface ChargeableTool {
    int MAX_CHARGE = 3;
    int BAR_COLOR = 0xB04BFF;

    default int getMaxCharge() {
        return MAX_CHARGE;
    }

    default Text describeCharge(ItemStack stack) {
        return Text.literal("Charge: " + getCharge(stack) + "/" + getMaxCharge());
    }

    static int getCharge(ItemStack stack) {
        return stack.hasNbt() ? stack.getNbt().getInt("Charge") : 0;
    }

    static void setCharge(ItemStack stack, int charge) {
        if (charge <= 0) {
            stack.removeSubNbt("Charge");
        } else {
            stack.getOrCreateNbt().putInt("Charge", charge);
        }
    }

    static boolean barVisible(ItemStack stack) {
        return getCharge(stack) > 0;
    }

    static int barStep(ItemStack stack, int maxCharge) {
        return Math.round(13f * getCharge(stack) / maxCharge);
    }
}
