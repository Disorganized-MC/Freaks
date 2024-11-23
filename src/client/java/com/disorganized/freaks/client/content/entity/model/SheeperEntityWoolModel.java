package com.disorganized.freaks.client.content.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class SheeperEntityWoolModel<T extends Entity> extends QuadrupedEntityModel<T> {

	private float headAngle;

	public SheeperEntityWoolModel(ModelPart root) {
		super(root, false, 8.0F, 4.0F, 2.0F, 2.0F, 24);
	}

	public static TexturedModelData getTexturedModelData(Dilation dilation) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 72).cuboid(-3.0F, -11.0F, -2.9167F, 6.0F, 4.0F, 6.0F, new Dilation(0.0F))
			.uv(49, 70).cuboid(-3.0F, -15.0F, -4.9167F, 6.0F, 4.0F, 8.0F, new Dilation(0.0F))
			.uv(49, 0).cuboid(-5.0F, -7.0F, -4.9167F, 10.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 16.0F, -0.0833F));

		ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(49, 21).cuboid(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -13.0F, -4.9167F));

		ModelPartData leg1 = modelPartData.addChild("right_front_leg", ModelPartBuilder.create().uv(25, 72).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, 18.0F, -5.0F));
		ModelPartData leg2 = modelPartData.addChild("left_front_leg", ModelPartBuilder.create().uv(78, 70).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 18.0F, -5.0F));
		ModelPartData leg3 = modelPartData.addChild("right_hind_leg", ModelPartBuilder.create().uv(80, 38).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, 18.0F, 5.0F));
		ModelPartData leg4 = modelPartData.addChild("left_hind_leg", ModelPartBuilder.create().uv(80, 49).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 18.0F, 5.0F));

		return TexturedModelData.of(modelData, 128, 128);
	}

	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.head.yaw = headYaw * 0.017453292F;
		this.head.pitch = headPitch * 0.017453292F;
		this.leftHindLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
		this.rightHindLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
		this.leftFrontLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
		this.rightFrontLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
	}

}
