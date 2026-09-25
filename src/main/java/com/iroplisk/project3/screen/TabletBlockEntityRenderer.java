package com.iroplisk.project3.screen;

import com.iroplisk.project3.Project3;
import com.iroplisk.project3.block.entity.TabletBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class TabletBlockEntityRenderer implements BlockEntityRenderer<TabletBlockEntity> {
    private static final Identifier OVERLAY_TEXTURE = new Identifier(Project3.MOD_ID, "textures/block/transmutation_tablet.png");

    public TabletBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(TabletBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        matrices.translate(-0.5, 0.51, -0.5);
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(OVERLAY_TEXTURE));
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        float size = 2.0f;
        float y = 0f;

        buffer.vertex(matrix, 0, y, 0).color(255, 255, 255, 255).texture(0, 0).overlay(overlay).light(light).normal(0, 1, 0).next();
        buffer.vertex(matrix, 0, y, size).color(255, 255, 255, 255).texture(0, 1).overlay(overlay).light(light).normal(0, 1, 0).next();
        buffer.vertex(matrix, size, y, size).color(255, 255, 255, 255).texture(1, 1).overlay(overlay).light(light).normal(0, 1, 0).next();
        buffer.vertex(matrix, size, y, 0).color(255, 255, 255, 255).texture(1, 0).overlay(overlay).light(light).normal(0, 1, 0).next();

        matrices.pop();
    }
}
