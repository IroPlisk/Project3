package com.iroplisk.project3.screen;

import com.iroplisk.project3.Project3;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class TabletInterface extends HandledScreen<TabletInterfaceHandler> {
    private static final Identifier TEXTURE = new Identifier(Project3.MOD_ID, "textures/gui/transmutation_tablet.png");

    private static final int BUY_X = 173;
    private static final int BUY_Y = 36;
    private static final int BUY_COLS = 3;
    private static final int BUY_VISIBLE_ROWS = 11;
    private static final int SLOT_SIZE = 18;

    private static final int EMC_TEXT_X = 9;
    private static final int EMC_TEXT_Y = 142;

    private static final int SCROLLBAR_X = BUY_X + (BUY_COLS * SLOT_SIZE) + 4;
    private static final int SCROLLBAR_Y = BUY_Y;
    private static final int SCROLLBAR_HEIGHT = BUY_VISIBLE_ROWS * SLOT_SIZE;
    private static final int SCROLLBAR_WIDTH = 6;

    private int scrollOffset = 0;

    public TabletInterface(TabletInterfaceHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, Text.literal(""));
        this.backgroundWidth = 255;
        this.backgroundHeight = 245;
        this.playerInventoryTitleY = -1000;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        long emc = handler.getPlayerEmc();
        context.drawText(this.textRenderer, "EMC: " + emc, x + EMC_TEXT_X, y + EMC_TEXT_Y, 0x55FF55, false);

        List<Item> buyable = handler.getBuyableItems();
        int visibleCount = BUY_COLS * BUY_VISIBLE_ROWS;
        for (int i = 0; i < visibleCount; i++) {
            int itemIndex = i + scrollOffset;
            if (itemIndex >= buyable.size()) break;
            int col = i % BUY_COLS;
            int row = i / BUY_COLS;
            int slotX = x + BUY_X + col * SLOT_SIZE;
            int slotY = y + BUY_Y + row * SLOT_SIZE;
            context.drawItem(new ItemStack(buyable.get(itemIndex)), slotX, slotY);
        }

        int maxScroll = Math.max(0, buyable.size() - visibleCount);

        int trackX = x + SCROLLBAR_X;
        int trackY = y + SCROLLBAR_Y;
        context.fill(trackX, trackY, trackX + SCROLLBAR_WIDTH, trackY + SCROLLBAR_HEIGHT, 0xFF404040);

        if (maxScroll > 0) {
            int handleHeight = Math.max(10, SCROLLBAR_HEIGHT * visibleCount / buyable.size());
            int handleY = trackY + (SCROLLBAR_HEIGHT - handleHeight) * scrollOffset / maxScroll;
            context.fill(trackX, handleY, trackX + SCROLLBAR_WIDTH, handleY + handleHeight, 0xFFAAAAAA);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        List<Item> buyable = handler.getBuyableItems();
        int visibleCount = BUY_COLS * BUY_VISIBLE_ROWS;

        for (int i = 0; i < visibleCount; i++) {
            int itemIndex = i + scrollOffset;
            if (itemIndex >= buyable.size()) break;
            int col = i % BUY_COLS;
            int row = i / BUY_COLS;
            int slotX = x + BUY_X + col * SLOT_SIZE;
            int slotY = y + BUY_Y + row * SLOT_SIZE;
            if (mouseX >= slotX && mouseX < slotX + 16 && mouseY >= slotY && mouseY < slotY + 16) {
                context.drawItemTooltip(this.textRenderer, new ItemStack(buyable.get(itemIndex)), mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        List<Item> buyable = handler.getBuyableItems();
        int visibleCount = BUY_COLS * BUY_VISIBLE_ROWS;

        for (int i = 0; i < visibleCount; i++) {
            int itemIndex = i + scrollOffset;
            if (itemIndex >= buyable.size()) break;
            int col = i % BUY_COLS;
            int row = i / BUY_COLS;
            int slotX = x + BUY_X + col * SLOT_SIZE;
            int slotY = y + BUY_Y + row * SLOT_SIZE;
            if (mouseX >= slotX && mouseX < slotX + 16 && mouseY >= slotY && mouseY < slotY + 16) {
                MinecraftClient.getInstance().interactionManager.clickButton(handler.syncId, itemIndex);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int maxScroll = Math.max(0, handler.getBuyableItems().size() - (BUY_COLS * BUY_VISIBLE_ROWS));
        scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset - (int) amount));
        return true;
    }
}
