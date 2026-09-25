package com.iroplisk.project3.item.custom;

import com.iroplisk.project3.inventory.AlchemicalBagInventory;
import com.iroplisk.project3.inventory.AlchemicalBagStorage;
import com.iroplisk.project3.screen.AlchemicalBagHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class AlchemicalBagItem extends Item {
    private final int tier;
    private final int slotCount;

    public AlchemicalBagItem(Settings settings, int tier, int slotCount) {
        super(settings.maxCount(1).rarity(switch(tier){
            case 2 ->Rarity.RARE;
            case 3 ->Rarity.EPIC;
            default -> Rarity.UNCOMMON;
        }));
        this.tier = tier;
        this.slotCount = slotCount;
    }

    public int getTier() { return tier; }
    public int getSlotCount() { return slotCount; }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient && user instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.openHandledScreen(new ExtendedScreenHandlerFactory() {
                @Override
                public Text getDisplayName() {
                    return Text.translatable(getTranslationKey());
                }

                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    var storage = AlchemicalBagStorage.get((ServerWorld) world);
                    var sharedInv = storage.getInventory(player.getUuid(), tier, slotCount);
                    return new AlchemicalBagHandler(syncId, inv, sharedInv, tier);
                }

                @Override
                public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                    buf.writeInt(tier);
                    buf.writeInt(slotCount);
                }
            });
        }
        return TypedActionResult.success(stack, world.isClient);
    }


}
