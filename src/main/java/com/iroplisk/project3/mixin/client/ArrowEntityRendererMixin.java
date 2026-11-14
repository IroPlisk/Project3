package com.iroplisk.project3.mixin.client;

import com.iroplisk.project3.item.custom.DarkMatterArrow;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArrowEntityRenderer.class)
public class ArrowEntityRendererMixin {
    private static final Identifier DM_ARROW_TEXTURE =
            new Identifier("project3", "textures/entity/projectiles/dm_arrow.png");

    @Inject(method = "getTexture(Lnet/minecraft/entity/projectile/ArrowEntity;)Lnet/minecraft/util/Identifier;",
            at = @At("HEAD"), cancellable = true)
    private void project3$darkTexture(ArrowEntity arrow, CallbackInfoReturnable<Identifier> cir) {
        if (arrow.getColor() == DarkMatterArrow.DARK_COLOR) {
            cir.setReturnValue(DM_ARROW_TEXTURE);
        }
    }
}