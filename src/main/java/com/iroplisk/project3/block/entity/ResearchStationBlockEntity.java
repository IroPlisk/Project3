package com.iroplisk.project3.block.entity;

import com.iroplisk.project3.VanillaModifiers;
import com.iroplisk.project3.block.BlockEntities;
import com.iroplisk.project3.item.custom.TomeItem;
import com.iroplisk.project3.screen.ResearchStationInterfaceHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ResearchStationBlockEntity extends BlockEntity implements ImplementedInventory, ExtendedScreenHandlerFactory {
    public static final int ITEM_SLOT = 0;
    public static final int TOME_SLOT = 1;
    public static final int LEARN_TIME = 100;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private int progress = 0;

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> LEARN_TIME;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) progress = value;
        }

        @Override
        public int size() { return 2; }
    };

    public ResearchStationBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.RESEARCH_STATION_BLOCK_ENTITY, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ImplementedInventory.super.setStack(slot, stack);
        if (world != null && !world.isClient && !canLearn()) {
            progress = 0;
        }
    }

    private boolean canLearn() {
        ItemStack itemStack = getStack(ITEM_SLOT);
        ItemStack tomeStack = getStack(TOME_SLOT);
        if (itemStack.isEmpty() || tomeStack.isEmpty()) return false;
        if (!(tomeStack.getItem() instanceof TomeItem)) return false;
        if (!VanillaModifiers.hasEmc(itemStack)) return false;
        var itemId = Registries.ITEM.getId(itemStack.getItem());
        return !TomeItem.hasLearned(tomeStack, itemId);
    }

    public static void tick(World world, BlockPos pos, BlockState state, ResearchStationBlockEntity entity) {
        if (world.isClient) return;

        if (entity.canLearn()) {
            entity.progress++;
            entity.markDirty();
            if (entity.progress >= LEARN_TIME) {
                entity.finishLearning();
                entity.progress = 0;
            }
        } else if (entity.progress > 0) {
            entity.progress = 0;
            entity.markDirty();
        }
    }

    private void finishLearning() {
        ItemStack itemStack = getStack(ITEM_SLOT);
        ItemStack tomeStack = getStack(TOME_SLOT);
        var itemId = Registries.ITEM.getId(itemStack.getItem());
        boolean learned = TomeItem.learnItem(tomeStack, itemId);
        if (learned) {
            itemStack.decrement(1);
            if (world != null) {
                world.updateListeners(pos, getCachedState(), getCachedState(), 3);
            }
        }
    }

    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
        this.progress = nbt.getInt("Progress");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putInt("Progress", this.progress);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ResearchStationInterfaceHandler(syncId, playerInventory, this, propertyDelegate);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}