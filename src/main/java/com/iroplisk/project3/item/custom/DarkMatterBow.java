package com.iroplisk.project3.item.custom;

import com.iroplisk.project3.inventory.ChargeableTool;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class DarkMatterBow extends BowItem implements ChargeableTool {
    public DarkMatterBow(Settings settings) {
        super(settings);
    }
    @Override
    public Predicate<ItemStack> getProjectiles() {
        return stack -> stack.getItem() instanceof DarkMatterArrow;
    }

    public static float getTieredDrawSpeed(ItemStack stack){
        return 1f +0.5f*ChargeableTool.getCharge(stack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player)) return;

        boolean creative = player.getAbilities().creativeMode;
        ItemStack ammo = player.getProjectileType(stack);
        if (!(ammo.getItem() instanceof DarkMatterArrow)) {
            if (!creative) return;
            ammo = new ItemStack(com.iroplisk.project3.item.Items.DM_ARROW);
        }

        float pull = BowItem.getPullProgress((int) ((this.getMaxUseTime(stack) - remainingUseTicks) * getTieredDrawSpeed(stack)));
        if (pull < 0.1f) return;

        if (!world.isClient) {
            DarkMatterArrow arrowItem = (DarkMatterArrow) ammo.getItem();
            PersistentProjectileEntity arrow = arrowItem.createArrow(world, ammo, player);
            arrow.setVelocity(player, player.getPitch(), player.getYaw(), 0f, pull * 3f, 1f);
            if (pull == 1f) arrow.setCritical(true);

            int power = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
            if (power > 0) arrow.setDamage(arrow.getDamage() + power * 0.5 + 0.5);
            int punch = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
            if (punch > 0) arrow.setPunch(punch);
            if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) arrow.setOnFireFor(100);
            if (creative) arrow.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;

            stack.damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
            world.spawnEntity(arrow);
        }

        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1f,
                1f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + pull * 0.5f);

        if (!creative) {
            ammo.decrement(1);
            if (ammo.isEmpty()) player.getInventory().removeOne(ammo);
        }
        player.incrementStat(Stats.USED.getOrCreateStat(this));
    }
}

