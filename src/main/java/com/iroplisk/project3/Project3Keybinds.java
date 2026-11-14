package com.iroplisk.project3;

import com.iroplisk.project3.inventory.ChargeableTool;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import org.lwjgl.glfw.GLFW;

public class Project3Keybinds {
    public static KeyBinding CHARGE_KEY;

    public static void register() {
        CHARGE_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.project3.charge", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "category.project3"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (CHARGE_KEY.wasPressed()) {
                if (client.player == null) return;
                if (!(client.player.getMainHandStack().getItem() instanceof ChargeableTool)) continue;

                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBoolean(Screen.hasShiftDown());
                ClientPlayNetworking.send(Project3Networking.CHARGE_PACKET, buf);
            }
        });
    }
}
