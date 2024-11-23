package com.disorganized.freaks.client.content.entity.model;

import com.disorganized.freaks.content.entity.SheeperEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class SheeperEntityModel<T extends SheeperEntity> extends EntityModel<T> {

	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart rightFrontLeg;
	private final ModelPart leftFrontLeg;
	private final ModelPart rightHindLeg;
	private final ModelPart leftHindLeg;

	private float headPitchModifier;

	public SheeperEntityModel(ModelPart root) {
		this.body = root.getChild("body");
		this.head = root.getChild("head");
		this.rightFrontLeg = root.getChild("right_front_leg");
		this.leftFrontLeg = root.getChild("left_front_leg");
		this.rightHindLeg = root.getChild("right_hind_leg");
		this.leftHindLeg = root.getChild("left_hind_leg");
	}

	public static TexturedModelData getTexturedModelData(Dilation dilation) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 72).cuboid(-3.0F, -11.0F, -2.9167F, 6.0F, 4.0F, 6.0F, new Dilation(0.0F))
			.uv(49, 70).cuboid(-3.0F, -15.0F, -4.9167F, 6.0F, 4.0F, 8.0F, new Dilation(0.0F))
			.uv(49, 0).cuboid(-5.0F, -7.0F, -4.9167F, 10.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 16.0F, -0.0833F));

		modelPartData.addChild("head", ModelPartBuilder.create().uv(49, 21).cuboid(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.0F, -5.0F));

		modelPartData.addChild("right_front_leg", ModelPartBuilder.create().uv(25, 72).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, 18.0F, -5.0F));
		modelPartData.addChild("left_front_leg", ModelPartBuilder.create().uv(78, 70).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 18.0F, -5.0F));
		modelPartData.addChild("right_hind_leg", ModelPartBuilder.create().uv(80, 38).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, 18.0F, 5.0F));
		modelPartData.addChild("left_hind_leg", ModelPartBuilder.create().uv(80, 49).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 18.0F, 5.0F));

		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void animateModel(T sheepEntity, float f, float g, float h) {
		super.animateModel(sheepEntity, f, g, h);
		this.head.pivotY = 6.0F + sheepEntity.getNeckAngle(h) * 9.0F;
		this.headPitchModifier = sheepEntity.getHeadAngle(h);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.head.yaw = headYaw * 0.017453292F;
//		this.head.pitch = headPitch * 0.017453292F;
		this.head.pitch = this.headPitchModifier;
		this.leftHindLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
		this.rightHindLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
		this.leftFrontLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
		this.rightFrontLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		head.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rightFrontLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		leftFrontLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		rightHindLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		leftHindLeg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

}
