package com.iroplisk.project3.screen;

import com.iroplisk.project3.inventory.AlchemicalBagInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class AlchemicalBagHandler extends ScreenHandler {
    private final Inventory bagInventory;
    private final int tier;

    public AlchemicalBagHandler(int syncId, PlayerInventory playerInventory, Inventory bagInventory, int tier) {
        super(getHandlerTypeForTier(tier), syncId);
        this.bagInventory = bagInventory;
        this.tier = tier;
        bagInventory.onOpen(playerInventory.player);

        int slotCount = bagInventory.size();
        int cols = colsForTier(tier);

        int bagTopOffset = bagTopOffsetForTier(tier);
        for (int i = 0; i < slotCount; i++) {
            int row = i / cols;
            int col = i % cols;
            this.addSlot(new Slot(bagInventory, i, 8 + col * 18, bagTopOffset + row * 18));
        }

        int playerInvX = playerInvXForTier(tier);
        int playerInvY = playerInvYForTier(tier);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, playerInvX + col * 18, playerInvY + row * 18));
            }
        }
        int hotbarY = playerInvY + (3 * 18) + 4;
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, playerInvX + col * 18, hotbarY));
        }
    }

    private static int colsForTier(int tier) {
        if(tier == 3) {
            return 13;
        }
        return 12;
    }

    private static int bagTopOffsetForTier(int tier) {
        if(tier == 3) {
            return 8;
        }
        return 18;
    }

    private static int playerInvYForTier(int tier) {
        return switch (tier) {
            case 2 -> 158;
            case 3 -> 174;
            default -> 104;
        };
    }

    private static int playerInvXForTier(int tier) {
        if(tier == 3) {
            return 44;
        }
        return 35;
    }

    public AlchemicalBagHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, buf.readInt(), buf.readInt());
    }

    private AlchemicalBagHandler(int syncId, PlayerInventory playerInventory, int tier, int slotCount) {
        this(syncId, playerInventory, new net.minecraft.inventory.SimpleInventory(slotCount), tier);
    }

    public int getTier() {
        return tier;
    }

    public ItemStack getBagStack() {
        if (bagInventory instanceof AlchemicalBagInventory bagInv) {
            return bagInv.getBagStack();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return bagInventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            int bagSize = bagInventory.size();
            if (index < bagSize) {
                if (!this.insertItem(originalStack, bagSize, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.insertItem(originalStack, 0, bagSize, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (originalStack.isEmpty()) slot.setStack(ItemStack.EMPTY);
            else slot.markDirty();
        }
        return newStack;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        bagInventory.onClose(player);
    }

    private static ScreenHandlerType<?> getHandlerTypeForTier(int tier) {
        return switch (tier) {
            case 2 -> Project3ScreenHandlers.ALCHEMICAL_BAG_MEDIUM_SCREEN_HANDLER;
            case 3 -> Project3ScreenHandlers.ALCHEMICAL_BAG_LARGE_SCREEN_HANDLER;
            default -> Project3ScreenHandlers.ALCHEMICAL_BAG_SMALL_SCREEN_HANDLER;
        };
    }

}
