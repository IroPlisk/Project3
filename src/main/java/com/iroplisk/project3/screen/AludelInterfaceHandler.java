package com.iroplisk.project3.screen;

import com.iroplisk.project3.block.entity.AludelBaseBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class AludelInterfaceHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final AludelBaseBlockEntity aludelBlockEntity;

    public AludelInterfaceHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf){
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(buf.readBlockPos()),
                new ArrayPropertyDelegate(4));
    }

    public AludelInterfaceHandler(int syncId, PlayerInventory playerInventory,
                                  BlockEntity blockEntity, PropertyDelegate arrayPropertyDelegate) {
        super(Project3ScreenHandlers.ALUDEL_SCREEN_HANDLER, syncId);
        checkSize(((Inventory) blockEntity), 4);
        this.inventory = ((Inventory) blockEntity);
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = arrayPropertyDelegate;
        this.aludelBlockEntity = ((AludelBaseBlockEntity) blockEntity);

        this.addSlot(new Slot(inventory, 0, 44, 18));  // Input
        this.addSlot(new Slot(inventory, 1, 44, 39)); // Comburent
        this.addSlot(new Slot(inventory, 2, 44, 74));  // Fuel
        this.addSlot(new Slot(inventory, 3, 118, 37){ // output
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addProperties(arrayPropertyDelegate);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0 && isBurning();
    }

    public int getScaledProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        int progressArrowSize = 23;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public boolean isBurning(){
        return propertyDelegate.get(2) > 0;
    }

    public int getScaledBurnTIme(){
        int burnTime = this.propertyDelegate.get(2);
        int maxBurnTime = this.propertyDelegate.get(3);
        int burnFlameSize = 13;

        return maxBurnTime != 0 && burnTime != 0 ? burnTime * burnFlameSize / maxBurnTime : 0;
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 106 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 164));
        }
    }
}
