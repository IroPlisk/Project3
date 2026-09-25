package com.iroplisk.project3.screen;

import com.iroplisk.project3.Project3;
import com.iroplisk.project3.VanillaModifiers;
import com.iroplisk.project3.item.custom.TomeItem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TomeScreen extends Screen {
    private static final Identifier TEXTURE = new Identifier(Project3.MOD_ID, "textures/gui/tome.png");

    private static final int BOOK_WIDTH = 256;
    private static final int BOOK_HEIGHT = 200;

    private static final int COLS_PER_PAGE = 4;
    private static final int ROWS_PER_PAGE = 10;
    private static final int SLOT_SIZE = 18;
    private static final int LEFT_PAGE_X = 30;
    private static final int RIGHT_PAGE_X = 140;
    private static final int PAGE_TOP_Y = 24;

    private final List<Item> learnedItems;
    private int pageIndex = 0;

    private ButtonWidget prevButton;
    private ButtonWidget nextButton;

    public TomeScreen(ItemStack tomeStack) {
        super(Text.literal("Tome of Knowledge"));
        this.learnedItems = TomeItem.getLearnedItems(tomeStack).stream()
                .sorted(Comparator.comparingLong(item -> VanillaModifiers.getEmc(new ItemStack(item))))
                .collect(Collectors.toList());
    }

    @Override
    protected void init() {
        int x = (this.width - BOOK_WIDTH) / 2;
        int y = (this.height - BOOK_HEIGHT) / 2;

        prevButton = ButtonWidget.builder(Text.literal("<"), btn -> changePage(-1))
                .dimensions(x + BOOK_WIDTH / 2 - 40, y + BOOK_HEIGHT - 20, 20, 20)
                .build();
        nextButton = ButtonWidget.builder(Text.literal(">"), btn -> changePage(1))
                .dimensions(x + BOOK_WIDTH / 2 + 20, y + BOOK_HEIGHT - 20, 20, 20)
                .build();

        this.addDrawableChild(prevButton);
        this.addDrawableChild(nextButton);
        updateButtonState();
    }

    private int itemsPerSpread() {
        return COLS_PER_PAGE * ROWS_PER_PAGE * 2;
    }

    private int maxPageIndex() {
        return Math.max(0, (learnedItems.size() - 1) / itemsPerSpread());
    }

    private void changePage(int delta) {
        pageIndex = Math.max(0, Math.min(maxPageIndex(), pageIndex + delta));
        updateButtonState();
    }

    private void updateButtonState() {
        prevButton.active = pageIndex > 0;
        nextButton.active = pageIndex < maxPageIndex();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        int x = (this.width - BOOK_WIDTH) / 2;
        int y = (this.height - BOOK_HEIGHT) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, BOOK_WIDTH, BOOK_HEIGHT);

        int startIndex = pageIndex * itemsPerSpread();
        ItemStack hoveredStack = null;

        for (int slot = 0; slot < itemsPerSpread(); slot++) {
            int itemIndex = startIndex + slot;
            if (itemIndex >= learnedItems.size()) break;

            boolean rightPage = slot >= COLS_PER_PAGE * ROWS_PER_PAGE;
            int localSlot = rightPage ? slot - COLS_PER_PAGE * ROWS_PER_PAGE : slot;
            int col = localSlot % COLS_PER_PAGE;
            int row = localSlot / COLS_PER_PAGE;

            int pageBaseX = rightPage ? RIGHT_PAGE_X : LEFT_PAGE_X;
            int slotX = x + pageBaseX + col * SLOT_SIZE;
            int slotY = y + PAGE_TOP_Y + row * SLOT_SIZE;

            ItemStack displayStack = new ItemStack(learnedItems.get(itemIndex));
            context.drawItem(displayStack, slotX, slotY);

            if (mouseX >= slotX && mouseX < slotX + 16 && mouseY >= slotY && mouseY < slotY + 16) {
                hoveredStack = displayStack;
            }
        }

        super.render(context, mouseX, mouseY, delta);

        if (hoveredStack != null) {
            context.drawItemTooltip(this.textRenderer, hoveredStack, mouseX, mouseY);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}