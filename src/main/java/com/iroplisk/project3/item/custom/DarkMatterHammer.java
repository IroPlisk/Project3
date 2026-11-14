package com.iroplisk.project3.item.custom;

import com.iroplisk.project3.inventory.ChargeableTool;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DarkMatterHammer extends PickaxeItem implements ChargeableTool {
    private static boolean mining = false;

    public DarkMatterHammer(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            ItemStack stack = player.getMainHandStack();
            if (mining || world.isClient || player.isSneaking()
                    || !(stack.getItem() instanceof DarkMatterHammer)
                    || !(player instanceof ServerPlayerEntity sp)) {
                return true;
            }

            int r = 1 + ChargeableTool.getCharge(stack);
            Direction side = hitSide(world, player, pos);

            mining = true;
            try {
                for (int a = -r; a <= r; a++) {
                    for (int b = -r; b <= r; b++) {
                        if (a == 0 && b == 0) continue;
                        if (stack.isEmpty()) return true;

                        BlockPos p = switch (side.getAxis()) {
                            case Y -> pos.add(a, 0, b);
                            case X -> pos.add(0, a, b);
                            case Z -> pos.add(a, b, 0);
                        };
                        BlockState s = world.getBlockState(p);
                        if (s.isAir() || s.getHardness(world, p) < 0 || !stack.isSuitableFor(s)) continue;
                        sp.interactionManager.tryBreakBlock(p);
                    }
                }
            } finally {
                mining = false;
            }
            return true;
        });
    }

    private static Direction hitSide(World world, PlayerEntity player, BlockPos pos) {
        Vec3d eye = player.getEyePos();
        Vec3d end = eye.add(player.getRotationVec(1f).multiply(8));
        BlockHitResult hit = world.raycast(new RaycastContext(eye, end,
                RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
        if (hit.getType() == HitResult.Type.BLOCK && hit.getBlockPos().equals(pos)) {
            return hit.getSide();
        }
        return Direction.getEntityFacingOrder(player)[0].getOpposite();
    }

    @Override
    public int getMaxCharge() {
        return 3;
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }
}
