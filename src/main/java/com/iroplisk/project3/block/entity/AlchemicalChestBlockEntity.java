package com.iroplisk.project3.block.entity;

import com.iroplisk.project3.block.BlockEntities;
import com.iroplisk.project3.screen.AlchemicalChestHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class AlchemicalChestBlockEntity extends BlockEntity implements ImplementedInventory, ExtendedScreenHandlerFactory {
    private final int tier;
    private final DefaultedList<ItemStack> inventory;

    public AlchemicalChestBlockEntity(BlockPos pos, BlockState state, int tier) {
        super(typeForTier(tier), pos, state);
        this.tier = tier;
        this.inventory = DefaultedList.ofSize(slotsForTier(tier), ItemStack.EMPTY);
    }

    private static BlockEntityType<?> typeForTier(int tier) {
        return switch (tier) {
            case 2 -> BlockEntities.ALCHEMICAL_CHEST_AZURE_BLOCK_ENTITY;
            case 3 -> BlockEntities.ALCHEMICAL_CHEST_MINIUM_BLOCK_ENTITY;
            default -> BlockEntities.ALCHEMICAL_CHEST_VERDANT_BLOCK_ENTITY;
        };
    }

    private static int slotsForTier(int tier) {
        return switch (tier) {
            case 2 -> 84;
            case 3 -> 117;
            default -> 48;
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new AlchemicalChestHandler(syncId, inv, this, tier);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeInt(tier);
        buf.writeInt(inventory.size());
    }
}
