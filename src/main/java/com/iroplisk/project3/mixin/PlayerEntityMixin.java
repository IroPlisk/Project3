package com.iroplisk.project3.mixin;

import com.iroplisk.project3.inventory.ChargeableTool;
import com.iroplisk.project3.item.custom.DarkMatterSword;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    private double project3$sweepExtra() {
        ItemStack stack = ((PlayerEntity) (Object) this).getMainHandStack();
        return stack.getItem() instanceof DarkMatterSword ? ChargeableTool.getCharge(stack) : 0;
    }

    @ModifyArgs(method = "attack(Lnet/minecraft/entity/Entity;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"),
            require = 0)
    private void project3$widerSweepBox(Args args) {
        double extra = project3$sweepExtra();
        if (extra > 0) {
            double x = args.get(0);
            double y = args.get(1);
            double z = args.get(2);
            args.set(0, x + extra);
            args.set(1, y + extra * 0.25);
            args.set(2, z + extra);
        }
    }

    @ModifyConstant(method = "attack(Lnet/minecraft/entity/Entity;)V",
            constant = @Constant(doubleValue = 9.0),
            require = 0)
    private double project3$widerSweepRange(double original) {
        double extra = project3$sweepExtra();
        if (extra <= 0) return original;
        double radius = 3.0 + extra;
        return radius * radius;
    }
}
