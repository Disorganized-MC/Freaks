package com.disorganized.freaks.client.content.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class SheeperEntityWoolModel<T extends Entity> extends EntityModel<T> {

	private final ModelPart head;
	private final ModelPart body;

	public SheeperEntityWoolModel(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("head", ModelPartBuilder.create().uv(49, 54).cuboid(-6.0F, -6.0F, -4.0F, 12.0F, 12.0F, 3.0F, new Dilation(0.0F))
			.uv(49, 38).cuboid(-6.0F, -6.0F, -4.0F, 12.0F, 12.0F, 3.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, 3.0F, -5.0F));

		modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 36).cuboid(-11.0F, -22.0F, 10.0F, 12.0F, 23.0F, 12.0F, new Dilation(0.5F))
			.uv(0, 0).cuboid(-11.0F, -22.0F, 10.0F, 12.0F, 23.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 19.0F, -16.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}

	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		head.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

}
