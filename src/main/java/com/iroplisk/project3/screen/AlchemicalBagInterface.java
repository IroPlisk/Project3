package com.iroplisk.project3.screen;

import com.iroplisk.project3.Project3;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AlchemicalBagInterface extends HandledScreen<AlchemicalBagHandler> {
    private final Identifier texture;

    public AlchemicalBagInterface(AlchemicalBagHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, Text.literal(""));

        int tier = handler.getTier();
        this.texture = new Identifier(Project3.MOD_ID, "textures/gui/alchemical_bag_" + tierName(tier) + ".png");

        int slotCount = handler.getBagStack() == null ? 27 : slotCountForTier(tier);
        int rows = (int) Math.ceil(slotCount / 9.0);
        this.backgroundWidth = guiWidth(handler.getTier());
        this.backgroundHeight = guiHeight(handler.getTier());
        this.playerInventoryTitleY = -1000;
    }

    private static String tierName(int tier) {
        return switch (tier) {
            case 2 -> "medium";
            case 3 -> "large";
            default -> "small";
        };
    }

    private static int guiWidth(int tier){
        if(tier == 3){
            return 247;
        }
        return 229;
    }

    private static int guiHeight(int tier) {
        return switch (tier) {
            case 2 -> 239;
            case 3 -> 255;
            default -> 185;
        };
    }

    private static int slotCountForTier(int tier) {
        return switch (tier) {
            case 2 -> 84;
            case 3 -> 117;
            default -> 48;
        };
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        this.drawMouseoverTooltip(context, mouseX, mouseY);
        int x = (width - guiWidth(handler.getTier())) / 2;
        int y = (height - guiHeight(handler.getTier())) / 2;
        context.drawTexture(texture, x, y, 0, 0, guiWidth(handler.getTier()), guiHeight(handler.getTier()));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
