package com.disorganized.freaks.client.content.entity.renderer;

import com.disorganized.freaks.Freaks;
import com.disorganized.freaks.content.entity.MudBubbleEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MudBubbleRenderer extends EntityRenderer<MudBubbleEntity> {

	private static final Identifier TEXTURE = Freaks.of("textures/entity/projectile/mud_bubble/still.png");
	private static final RenderLayer LAYER = RenderLayer.getEntityCutoutNoCull(TEXTURE);

	@Override
	public Identifier getTexture(MudBubbleEntity dragonFireballEntity) {
		return TEXTURE;
	}

	public MudBubbleRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	protected int getBlockLight(MudBubbleEntity dragonFireballEntity, BlockPos blockPos) {
		return 15;
	}

	@Override
	public void render(MudBubbleEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();
		matrices.scale((float)9/16, (float)9/16, (float)9/16);
		matrices.translate(0, 0.2F, 0);
		matrices.multiply(this.dispatcher.getRotation());

		MatrixStack.Entry entry = matrices.peek();
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(LAYER);
		produceVertex(vertexConsumer, entry, light, 0.0F, 0, 0, 1);
		produceVertex(vertexConsumer, entry, light, 1.0F, 0, 1, 1);
		produceVertex(vertexConsumer, entry, light, 1.0F, 1, 1, 0);
		produceVertex(vertexConsumer, entry, light, 0.0F, 1, 0, 0);
		matrices.pop();

		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
	}

	private static void produceVertex(VertexConsumer vertexConsumer, MatrixStack.Entry matrix, int light, float x, int z, int textureU, int textureV) {
		vertexConsumer.vertex(matrix, x - 0.5F, (float)z - 0.25F, 0.0F).color(-1)
			.texture((float)textureU, (float)textureV)
			.overlay(OverlayTexture.DEFAULT_UV)
			.light(light)
			.normal(matrix, 0.0F, 1.0F, 0.0F);
	}

}
