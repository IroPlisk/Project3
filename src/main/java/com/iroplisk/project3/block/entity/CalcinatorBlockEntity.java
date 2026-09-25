package com.iroplisk.project3.block.entity;

import com.iroplisk.project3.VanillaModifiers;
import com.iroplisk.project3.block.BlockEntities;
import com.iroplisk.project3.item.Items;
import com.iroplisk.project3.screen.CalcinatorInterfaceHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CalcinatorBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    public final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    public static final int CALCINATE = 0;
    public static final int FUEL = 1;
    public static final int ASH_OUTPUT = 2;

    protected final PropertyDelegate propertyDelegate;
    public int progress = 0;
    public int maxProgress = 10;
    public int burnTime = 0;
    public int maxBurnTime = 60;

    public CalcinatorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.CALCINATOR_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> CalcinatorBlockEntity.this.progress;
                    case 1 -> CalcinatorBlockEntity.this.maxProgress;
                    case 2 -> CalcinatorBlockEntity.this.burnTime;
                    case 3 -> CalcinatorBlockEntity.this.maxBurnTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> CalcinatorBlockEntity.this.progress = value;
                    case 1 -> CalcinatorBlockEntity.this.maxProgress = value;
                    case 2 -> CalcinatorBlockEntity.this.burnTime = value;
                    case 3 -> CalcinatorBlockEntity.this.maxBurnTime = value;
                }
            }

            @Override
            public int size() {
                return 4;
            }
        };
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("calcinator.progress");
        burnTime = nbt.getInt("calcinator.burn_time");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("calcinator.progress", progress);
        nbt.putInt("calcinator.burn_time", burnTime);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Calcinator");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new CalcinatorInterfaceHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient()) {
            return;
        }

        boolean wasBurning = isBurning();
        boolean dirty = false;

        if (isBurning()) {
            this.burnTime--;
            dirty = true;
        }

        ItemStack inputStack = getStack(CALCINATE);
        ItemStack fuelStack = getStack(FUEL);

        if (this.hasRecipe()) {
            if (!isBurning() && getFuelTime(fuelStack) > 0) {
                this.maxBurnTime = getFuelTime(fuelStack);
                this.burnTime = this.maxBurnTime;
                fuelStack.decrement(1);
                dirty = true;
            }

            if (isBurning()) {
                this.progress++;

                if (this.progress >= this.maxProgress) {
                    this.craftItem();
                    this.resetProgress();
                }
                dirty = true;
            } else {
                this.resetProgress();
            }
        } else {
            this.resetProgress();
        }

        if (wasBurning != isBurning() || dirty) {
            markDirty(world, pos, state);
        }
    }

    private int getFuelTime(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        net.minecraft.item.Item item = stack.getItem();
        Integer fuelTime = net.minecraft.block.entity.AbstractFurnaceBlockEntity.createFuelTimeMap().get(item);
        return fuelTime != null ? fuelTime : 0;
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    public void resetProgress() {
        this.progress = 0;
    }

    private boolean hasRecipe() {
        ItemStack input = getStack(CALCINATE);
        if (input.isEmpty()) {
            return false;
        }

        ItemStack expectedResult = getDustOutputForInput(input);
        if (expectedResult.isEmpty()) return false;

        return canInsertAmountIntoOutputSlot(expectedResult) && canInsertItemIntoOutputSlot(expectedResult.getItem());
    }

    private ItemStack getDustOutputForInput(ItemStack inputStack) {
        if (inputStack.isEmpty()) return ItemStack.EMPTY;
        int emc = getEmcFromTags(inputStack);

        if (emc < 64) {
            return new ItemStack(Items.DUST_ASH, 1);
        }
        if (emc <= 2047) {
            return new ItemStack(Items.DUST_VERDANT, 1);
        }

        if (emc >= 8192) {
            return new ItemStack(Items.DUST_MINIUM, 1);
        }

        return ItemStack.EMPTY;
    }

    private int getEmcFromTags(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        if (stack.isIn(VanillaModifiers.EMC_ONE_TAG)) return 1;
        if (stack.isIn(VanillaModifiers.EMC_32_TAG)) return 32;
        if (stack.isIn(VanillaModifiers.EMC_64_TAG)) return 64;
        if (stack.isIn(VanillaModifiers.EMC_128_TAG)) return 128;
        if (stack.isIn(VanillaModifiers.EMC_8192_TAG)) return 8192;

        return 0;
    }

    private void craftItem() {
        ItemStack inputStack = getStack(CALCINATE);
        ItemStack resultDust = getDustOutputForInput(inputStack);
        if (resultDust.isEmpty()) {
            return;
        }
        this.removeStack(CALCINATE, 1);
        ItemStack currentOutput = getStack(ASH_OUTPUT);
        if (currentOutput.isEmpty()) {
            this.setStack(ASH_OUTPUT, resultDust.copy());
        } else {
            currentOutput.increment(1);
        }
    }


    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.getStack(ASH_OUTPUT).getItem() == item || this.getStack(ASH_OUTPUT).isEmpty();
    }

    private boolean canInsertAmountIntoOutputSlot(ItemStack result) {
        return this.getStack(ASH_OUTPUT).getCount() + result.getCount() <= getStack(ASH_OUTPUT).getMaxCount();
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.getStack(ASH_OUTPUT).isEmpty() || this.getStack(ASH_OUTPUT).getCount() < this.getStack(ASH_OUTPUT).getMaxCount();
    }
}
