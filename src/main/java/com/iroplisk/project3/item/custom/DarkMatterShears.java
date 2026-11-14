package com.iroplisk.project3.item.custom;

import com.iroplisk.project3.inventory.ChargeableTool;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Shearable;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class DarkMatterShears extends ShearsItem implements ChargeableTool {

    public DarkMatterShears(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        World world = user.getWorld();

        if (entity instanceof SheepEntity sheep && sheep.isShearable()) {
            if (!world.isClient) {
                float chaos = (float) ChargeableTool.getCharge(stack) / getMaxCharge();
                Random random = sheep.getRandom();

                sheep.setSheared(true);
                world.playSound(null, sheep.getBlockPos(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 1f, 1f);

                int count = 1 + random.nextInt(3);
                for (int i = 0; i < count; i++) {
                    DyeColor color = random.nextFloat() < chaos
                            ? DyeColor.byId(random.nextInt(16))
                            : sheep.getColor();
                    Item wool = Registries.ITEM.get(new Identifier("minecraft", color.getName() + "_wool"));

                    ItemEntity drop = sheep.dropItem(wool, 1);
                    if (drop != null) {
                        drop.setVelocity(drop.getVelocity().add(
                                (random.nextFloat() - random.nextFloat()) * 0.1f,
                                random.nextFloat() * 0.05f,
                                (random.nextFloat() - random.nextFloat()) * 0.1f));
                    }
                }

                sheep.emitGameEvent(GameEvent.SHEAR, user);
                stack.damage(1, user, p -> p.sendToolBreakStatus(hand));
            }
            return ActionResult.success(world.isClient);
        }

        if (entity instanceof Shearable other && other.isShearable()) {
            if (!world.isClient) {
                other.sheared(SoundCategory.PLAYERS);
                entity.emitGameEvent(GameEvent.SHEAR, user);
                stack.damage(1, user, p -> p.sendToolBreakStatus(hand));
            }
            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }
}
