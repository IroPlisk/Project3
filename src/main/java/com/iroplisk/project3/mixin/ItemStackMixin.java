package com.iroplisk.project3.mixin;

import com.iroplisk.project3.inventory.ChargeableTool;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Inject(method = "isItemBarVisible", at = @At("HEAD"), cancellable = true)
    private void project3$barVisible(CallbackInfoReturnable<Boolean> cir) {
        ItemStack self = (ItemStack) (Object) this;
        if (getItem() instanceof ChargeableTool && ChargeableTool.getCharge(self) > 0) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getItemBarStep", at = @At("HEAD"), cancellable = true)
    private void project3$barStep(CallbackInfoReturnable<Integer> cir) {
        ItemStack self = (ItemStack) (Object) this;
        if (getItem() instanceof ChargeableTool tool && ChargeableTool.getCharge(self) > 0) {
            cir.setReturnValue(ChargeableTool.barStep(self, tool.getMaxCharge()));
        }
    }

    @Inject(method = "getItemBarColor", at = @At("HEAD"), cancellable = true)
    private void project3$barColor(CallbackInfoReturnable<Integer> cir) {
        ItemStack self = (ItemStack) (Object) this;
        if (getItem() instanceof ChargeableTool && ChargeableTool.getCharge(self) > 0) {
            cir.setReturnValue(ChargeableTool.BAR_COLOR);
        }
    }
}
