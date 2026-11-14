package com.iroplisk.project3.mixin;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FishingBobberEntity.class)
public class DarkFishingBobberEntityMixin {
    @Redirect(method = "removeIfInvalid",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean project3$anyRod(ItemStack stack, Item item) {
        return stack.getItem() instanceof FishingRodItem;
    }
}