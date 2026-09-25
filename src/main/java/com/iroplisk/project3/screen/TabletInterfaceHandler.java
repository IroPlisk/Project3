package com.iroplisk.project3.screen;

import com.iroplisk.project3.VanillaModifiers;
import com.iroplisk.project3.inventory.InstantSellSlot;
import com.iroplisk.project3.inventory.PlayerEMC;
import com.iroplisk.project3.inventory.SellSlot;
import com.iroplisk.project3.inventory.TomeSlot;
import com.iroplisk.project3.item.custom.TomeItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TabletInterfaceHandler extends ScreenHandler {
    public static final int SELL_SLOT_COUNT = 9;
    public static final int TOME_SLOT = 9;

    private final Inventory tabletInventory;
    private final PlayerEntity player;
    private final PropertyDelegate propertyDelegate;

    public TabletInterfaceHandler(int syncId, PlayerInventory playerInventory, Inventory tabletInventory, PlayerEntity player) {
        super(Project3ScreenHandlers.TABLET_INTERFACE_SCREEN_HANDLER, syncId);
        this.tabletInventory = tabletInventory;
        this.player = player;
        tabletInventory.onOpen(playerInventory.player);

        if (!player.getWorld().isClient) {
            this.propertyDelegate = new PropertyDelegate() {
                @Override
                public int get(int index) {
                    if (!(player.getWorld() instanceof ServerWorld sw)) return 0;
                    long emc = PlayerEMC.get(sw).getEmc(player.getUuid());
                    return index == 0 ? (int) emc : (int) (emc >> 32);
                }

                @Override
                public void set(int index, int value) { }

                @Override
                public int size() { return 2; }
            };
        } else {
            this.propertyDelegate = new net.minecraft.screen.ArrayPropertyDelegate(2);
        }
        this.addProperties(propertyDelegate);

        this.addSlot(new SellSlot(tabletInventory, 0, 62, 24));
        this.addSlot(new SellSlot(tabletInventory, 1, 89, 35));
        this.addSlot(new SellSlot(tabletInventory, 2, 98, 61));
        this.addSlot(new SellSlot(tabletInventory, 3, 89, 87));
        this.addSlot(new SellSlot(tabletInventory, 4, 62, 99));
        this.addSlot(new SellSlot(tabletInventory, 5, 35, 87));
        this.addSlot(new SellSlot(tabletInventory, 6, 26, 61));
        this.addSlot(new SellSlot(tabletInventory, 7, 35, 35));
        this.addSlot(new InstantSellSlot(tabletInventory, 8, 62, 61, player));

        this.addSlot(new TomeSlot(tabletInventory, TOME_SLOT, 152, 15));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 164 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 222));
        }
    }

    public TabletInterfaceHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, resolveInventory(playerInventory, buf), playerInventory.player);
    }

    private static Inventory resolveInventory(PlayerInventory playerInventory, PacketByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        var be = playerInventory.player.getWorld().getBlockEntity(pos);
        if (be instanceof Inventory inv) return inv;
        return new SimpleInventory(10);
    }

    public long getPlayerEmc() {
        int low = propertyDelegate.get(0);
        int high = propertyDelegate.get(1);
        return ((long) high << 32) | (low & 0xFFFFFFFFL);
    }

    private long getSellSlotEmc() {
        long total = 0;
        for (int i = 0; i < SELL_SLOT_COUNT; i++) {
            ItemStack stack = tabletInventory.getStack(i);
            if (!stack.isEmpty()) {
                total += VanillaModifiers.getEmc(stack) * stack.getCount();
            }
        }
        return total;
    }

    public List<Item> getBuyableItems() {
        ItemStack tome = tabletInventory.getStack(TOME_SLOT);
        if (tome.isEmpty() || !(tome.getItem() instanceof TomeItem)) return List.of();

        long available = getPlayerEmc() + getSellSlotEmc();

        return TomeItem.getLearnedItems(tome).stream()
                .filter(item -> {
                    long cost = VanillaModifiers.getEmc(new ItemStack(item));
                    return cost > 0 && cost <= available;
                })
                .sorted(Comparator.comparingLong(i -> VanillaModifiers.getEmc(new ItemStack(i))))
                .collect(Collectors.toList());
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (player.getWorld().isClient) return false;
        List<Item> buyable = getBuyableItems();
        if (id < 0 || id >= buyable.size()) return false;

        Item item = buyable.get(id);
        long cost = VanillaModifiers.getEmc(new ItemStack(item));
        if (cost <= 0) return false;

        ServerWorld sw = (ServerWorld) player.getWorld();
        PlayerEMC storage = PlayerEMC.get(sw);
        long balance = storage.getEmc(player.getUuid());

        if (balance < cost) {
            for (int i = 0; i < SELL_SLOT_COUNT && balance < cost; i++) {
                ItemStack sellStack = tabletInventory.getStack(i);
                while (balance < cost && !sellStack.isEmpty()) {
                    long itemEmc = VanillaModifiers.getEmc(sellStack);
                    if (itemEmc <= 0) break;

                    sellStack.decrement(1);
                    tabletInventory.markDirty();
                    storage.addEmc(player.getUuid(), itemEmc);
                    balance += itemEmc;

                    sellStack = tabletInventory.getStack(i);
                }
            }
        }

        if (balance < cost) return false;
        if (!storage.trySpendEmc(player.getUuid(), cost)) return false;

        ItemStack result = new ItemStack(item);
        ItemStack cursor = player.currentScreenHandler.getCursorStack();
        if (cursor.isEmpty()) {
            player.currentScreenHandler.setCursorStack(result);
        } else if (ItemStack.canCombine(cursor, result) && cursor.getCount() + result.getCount() <= cursor.getMaxCount()) {
            cursor.increment(result.getCount());
        } else {
            if (!player.getInventory().insertStack(result)) {
                player.dropItem(result, false);
            }
        }
        return true;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return tabletInventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (index < 10) {
                if (!this.insertItem(originalStack, 10, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.insertItem(originalStack, 0, 10, false)) {
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
        tabletInventory.onClose(player);
    }
}