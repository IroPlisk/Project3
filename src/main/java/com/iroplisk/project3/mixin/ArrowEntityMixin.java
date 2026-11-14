package com.iroplisk.project3.mixin;

import com.iroplisk.project3.item.Items;
import com.iroplisk.project3.item.custom.DarkMatterArrow;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArrowEntity.class)
public abstract class ArrowEntityMixin {
    @Shadow
    public abstract int getColor();

    @Inject(method = "asItemStack", at = @At("HEAD"), cancellable = true)
    private void project3$darkPickup(CallbackInfoReturnable<ItemStack> cir) {
        if (this.getColor() == DarkMatterArrow.DARK_COLOR) {
            cir.setReturnValue(new ItemStack(Items.DM_ARROW));
        }
    }
}