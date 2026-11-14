package com.iroplisk.project3;

import com.iroplisk.project3.inventory.ChargeableTool;
import com.iroplisk.project3.inventory.Project3Sounds;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class Project3Networking {
    public static final Identifier CHARGE_PACKET = new Identifier(Project3.MOD_ID, "change_charge");

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(CHARGE_PACKET, (server, player, handler, buf, sender) -> {
            boolean lower = buf.readBoolean();
            server.execute(() -> {
                ItemStack stack = player.getMainHandStack();
                if (!(stack.getItem() instanceof ChargeableTool tool)) return;

                int oldCharge = ChargeableTool.getCharge(stack);
                int newCharge = lower ? Math.max(0, oldCharge - 1) : Math.min(tool.getMaxCharge(), oldCharge + 1);
                if (newCharge == oldCharge) return;

                ChargeableTool.setCharge(stack, newCharge);

                float pitch = 0.8f + 0.2f * newCharge;
                SoundEvent sound = lower ? Project3Sounds.CHARGE_DOWN : Project3Sounds.CHARGE_UP;

                player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
                        sound, SoundCategory.PLAYERS, 1.0f, pitch);
            });
        });
    }
}
