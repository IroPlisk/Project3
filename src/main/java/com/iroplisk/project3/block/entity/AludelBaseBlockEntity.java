package com.iroplisk.project3.block.entity;

import com.iroplisk.project3.block.BlockEntities;
import com.iroplisk.project3.recipes.AludelRecipes;
import com.iroplisk.project3.screen.AludelInterfaceHandler;
import com.iroplisk.project3.screen.CalcinatorInterfaceHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static com.iroplisk.project3.item.Items.MINIUM_STONE;

public class AludelBaseBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

    protected final PropertyDelegate propertyDelegate;
    public static final int TOP_INPUT = 0;
    public static final int BOTTOM_INPUT = 1;
    public static final int FUEL = 2;
    public static final int OUTPUT = 3;

    public int progress = 0;
    public int maxProgress = 10;
    public int burnTime = 0;
    public int maxBurnTime = 60;
    private boolean isFormed = false;


    public AludelBaseBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.ALUDEL_BASE_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> AludelBaseBlockEntity.this.progress;
                    case 1 -> AludelBaseBlockEntity.this.maxProgress;
                    case 2 -> AludelBaseBlockEntity.this.burnTime;
                    case 3 -> AludelBaseBlockEntity.this.maxBurnTime;
                    default -> 0;
                };
            }
            @Override
            public void set(int index, int value) {
                switch(index){
                    case 0 -> AludelBaseBlockEntity.this.progress = value;
                    case 1 -> AludelBaseBlockEntity.this.maxProgress = value;
                    case 2 -> AludelBaseBlockEntity.this.burnTime = value;
                    case 3 -> AludelBaseBlockEntity.this.maxBurnTime = value;
                }
            }

            @Override
            public int size() {
                return 4;
            }
            };
    }

    public boolean isFormed(){
        return this.isFormed;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    public boolean checkMultiblock() {
        if (world == null) return false;
        BlockState aboveState = world.getBlockState(pos.up());
        return aboveState.isOf(com.example.project3.block.Blocks.GLASS_BELL);
    }

    private boolean hasFuel() {
        ItemStack fuelStack = getStack(FUEL);
        if (fuelStack.getItem() == MINIUM_STONE) {
            return fuelStack.getDamage() < fuelStack.getMaxDamage() - 1;
        }
        return AbstractFurnaceBlockEntity.canUseAsFuel(fuelStack);
    }

    private void consumeFuel() {
        ItemStack fuelStack = getStack(FUEL);
        if (fuelStack.getItem() == MINIUM_STONE) {
            fuelStack.setDamage(fuelStack.getDamage() + 1);
            if (fuelStack.getDamage() >= fuelStack.getMaxDamage()) {
                setStack(FUEL, ItemStack.EMPTY);
            }
        } else {
            removeStack(FUEL, 1);
        }
    }

    private int getFuelBurnTime() {
        ItemStack fuelStack = getStack(FUEL);
        if (fuelStack.getItem() == MINIUM_STONE) {
            return currentRecipe != null ? currentRecipe.getCookTime() : maxProgress;
        }
        return AbstractFurnaceBlockEntity.createFuelTimeMap().getOrDefault(fuelStack.getItem(), 0);
    }

    public static void tick(World world, BlockPos pos, BlockState state, AludelBaseBlockEntity entity) {
        if (world.isClient) return;
        boolean currentlyFormed = entity.checkMultiblock();
        if (currentlyFormed != entity.isFormed) {
            entity.isFormed = currentlyFormed;
            entity.markDirty();
        }

        if (!entity.isFormed) {
            if (entity.progress > 0) {
                entity.progress = 0;
                entity.markDirty();
            }
            return;
        }
        if (entity.burnTime > 0) {
            entity.burnTime--;
            entity.markDirty();
        }
        ItemStack input = entity.getStack(TOP_INPUT);
        ItemStack reagent = entity.getStack(BOTTOM_INPUT);

        if (!input.isEmpty() && !reagent.isEmpty() && entity.canCraft()) {
            if (entity.burnTime <= 0 && entity.hasFuel()) {
                entity.burnTime = entity.getFuelBurnTime();
                entity.consumeFuel();
                entity.markDirty();
            }

            if (entity.burnTime > 0) {
                entity.progress++;
                if (entity.progress >= entity.maxProgress) {
                    entity.craftItem();
                    entity.progress = 0;
                }
                entity.markDirty();
            }
        } else {
            if (entity.progress > 0) {
                entity.progress = 0;
                entity.markDirty();
            }
        }
    }

    private AludelRecipes currentRecipe;

    private boolean canCraft() {
        currentRecipe = world.getRecipeManager()
                .listAllOfType(AludelRecipes.Type.INSTANCE)
                .stream()
                .filter(r -> r.matches(this, world))
                .findFirst()
                .orElse(null);

        if (currentRecipe != null) {
            maxProgress = currentRecipe.getCookTime();
            return true;
        }
        return false;
    }

    private void craftItem() {
        if (currentRecipe == null) return;

        removeStack(TOP_INPUT, currentRecipe.getTopCount());
        removeStack(BOTTOM_INPUT, currentRecipe.getBottomCount());
        ItemStack result = getStack(OUTPUT);
        if (result.isEmpty()) {
            setStack(OUTPUT, currentRecipe.getOutput(world.getRegistryManager()).copy());
        } else {
            result.increment(1);
        }
        currentRecipe = null;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
        this.progress = nbt.getInt("aludel.progress");
        this.burnTime = nbt.getInt("aludel.burntime");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putInt("aludel.progress", this.progress);
        nbt.putInt("aludel.burntime", this.burnTime);
    }

    @Override
    public void markDirty() {
        super.markDirty();
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

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Aludel");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new AludelInterfaceHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}
