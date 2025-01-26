package com.disorganized.freaks.client.content.entity.renderer;

import com.disorganized.freaks.Freaks;
import com.disorganized.freaks.content.entity.MudBubbleEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.*;

public class MudBubbleRenderer extends EntityRenderer<MudBubbleEntity> {

	private static final Identifier TEXTURE = Freaks.of("textures/particle/mud_bubble/still.png");

	public MudBubbleRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Override
	public void render(MudBubbleEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();
		matrices.scale((float)9/16, (float)9/16, (float)9/16);
		matrices.translate(0, 0.2F, 0);
		matrices.multiply(this.dispatcher.getRotation());
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));

		MatrixStack.Entry entry = matrices.peek();
		Matrix4f positionMatrix = entry.getPositionMatrix();
		Matrix3f normalMatrix = entry.getNormalMatrix();
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.getLayer(entity));

		produceVertex(vertexConsumer, positionMatrix, normalMatrix, light, 0, 0, 0, 1);
		produceVertex(vertexConsumer, positionMatrix, normalMatrix, light, 1, 0, 1, 1);
		produceVertex(vertexConsumer, positionMatrix, normalMatrix, light, 1, 1, 1, 0);
		produceVertex(vertexConsumer, positionMatrix, normalMatrix, light, 0, 1, 0, 0);

		matrices.pop();
	}

	public static void produceVertex(
		VertexConsumer vertexConsumer,
		Matrix4f modelMatrix,
		Matrix3f normalMatrix,
		int light,
		float x,
		int y,
		int textureU,
		int textureV
	) {
		vertexConsumer.vertex(modelMatrix, x - 0.5f, (float) y - 0.25f, 0.0f)
			.color(255, 255, 255, 255)
			.texture(textureU, textureV)
			.overlay(OverlayTexture.DEFAULT_UV)
			.light(light)
			.normal(normalMatrix, 0.0f, 1.0f, 0.0f)
			.next();
	}

	private RenderLayer getLayer(MudBubbleEntity entity) {
		return RenderLayer.getEntityCutoutNoCull(this.getTexture(entity));
	}

	@Override
	public Identifier getTexture(MudBubbleEntity entity) {
		return TEXTURE;
	}

}
