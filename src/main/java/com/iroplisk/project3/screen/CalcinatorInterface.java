package com.iroplisk.project3.screen;

import com.iroplisk.project3.Project3;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CalcinatorInterface extends HandledScreen<CalcinatorInterfaceHandler> {
    public static final Identifier TEXTURE = new Identifier(Project3.MOD_ID, "textures/gui/calcinator.png");

    public CalcinatorInterface(CalcinatorInterfaceHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 184;
        this.backgroundWidth = 176;
        this.titleY = 5;
        this.playerInventoryTitleY = 82;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        renderProgressArrow(context, x, y);
        renderFuelGauge(context, x, y);
    }
    private void renderProgressArrow(DrawContext context, int x, int y) {
        if(handler.isCrafting()){
            int correctV = 23;

            context.drawTexture(TEXTURE, x + 83, y + 35, 176, 14, handler.getScaledProgress(), 14);
        }
    }

    private void renderFuelGauge(DrawContext context, int x, int y) {
        if(handler.isBurning()) {
            int burnHeight = handler.getScaledBurnTIme();
            context.drawTexture(TEXTURE, x + 56, y + 47 + (14 - burnHeight), 176, 2 + (14 - burnHeight), 14, burnHeight);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

}
