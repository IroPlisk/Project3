package com.iroplisk.project3.screen;

import com.iroplisk.project3.Project3;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ResearchStationInterface extends HandledScreen<ResearchStationInterfaceHandler> {
    private static final Identifier TEXTURE = new Identifier(Project3.MOD_ID, "textures/gui/research_station_bg.png");
    private static final Identifier PROGRESS_TEXTURE = new Identifier(Project3.MOD_ID, "textures/gui/research_station_glyph1.png");


    public ResearchStationInterface(ResearchStationInterfaceHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 255;
        this.backgroundHeight = 233;
        this.titleY = 13;
        this.titleX = 23;
        this.playerInventoryTitleY = 127;
        this.playerInventoryTitleX = 23;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        int filled = handler.getProgressScaled(backgroundHeight);
        context.drawTexture(PROGRESS_TEXTURE, x, y, 0, 0, backgroundWidth, filled);

        int arrowWidth = 40;
        int arrowHeight = 15;
        int arrowFilled = handler.getProgressScaled(arrowWidth);
        context.drawTexture(TEXTURE, x + 107, y + 85, 0, 234, arrowFilled, arrowHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
