package com.disorganized.freaks.client.content.entity.model;

import com.disorganized.freaks.client.content.entity.animation.SheeperAnimations;
import com.disorganized.freaks.content.entity.SheeperEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class SheeperModel<T extends SheeperEntity> extends SinglePartEntityModel<T> {

	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart rightFrontLeg;
	private final ModelPart leftFrontLeg;
	private final ModelPart rightHindLeg;
	private final ModelPart leftHindLeg;

	private boolean previousGrazingState = false;

	public SheeperModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("neck").getChild("head");
		this.rightFrontLeg = root.getChild("right_front_leg");
		this.leftFrontLeg = root.getChild("left_front_leg");
		this.rightHindLeg = root.getChild("right_hind_leg");
		this.leftHindLeg = root.getChild("left_hind_leg");
	}

	public static TexturedModelData getTexturedModelData(Dilation dilation) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData neck = modelPartData.addChild("neck", ModelPartBuilder.create().uv(49, 70).cuboid(-3.0F, -8.0F, -5.0F, 6.0F, 4.0F, 8.0F, new Dilation(0.0F))
			.uv(0, 72).cuboid(-3.0F, -4.0F, -3.0F, 6.0F, 4.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 9.0F, 0.0F));
		neck.addChild("head", ModelPartBuilder.create().uv(49, 21).cuboid(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, -5.0F));
		modelPartData.addChild("body", ModelPartBuilder.create().uv(49, 0).cuboid(-5.0F, -7.0F, -4.9167F, 10.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 16.0F, -0.0833F));
		modelPartData.addChild("right_front_leg", ModelPartBuilder.create().uv(25, 72).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, 18.0F, -5.0F));
		modelPartData.addChild("left_front_leg", ModelPartBuilder.create().uv(78, 70).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 18.0F, -5.0F));
		modelPartData.addChild("right_hind_leg", ModelPartBuilder.create().uv(80, 38).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, 18.0F, 5.0F));
		modelPartData.addChild("left_hind_leg", ModelPartBuilder.create().uv(80, 49).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 18.0F, 5.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void animateModel(T sheepEntity, float f, float g, float h) {
		super.animateModel(sheepEntity, f, g, h);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.head.yaw = headYaw * 0.017453292F;
		this.head.pitch = headPitch * 0.017453292F;
		this.leftHindLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
		this.rightHindLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
		this.leftFrontLeg.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
		this.rightFrontLeg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;


//		this.updateAnimation(entity.startGrazingState, SheeperAnimations.START_GRAZING, animationProgress, 1.0F);
//		this.updateAnimation(entity.stopGrazingState, SheeperAnimations.STOP_GRAZING, animationProgress, 1.0F);
		if (entity.startGrazingState.isRunning()) {
			this.updateAnimation(entity.startGrazingState, SheeperAnimations.LOOP_GRAZING, animationProgress, 1.0F);
//			this.animate(SheeperAnimations.LOOP_GRAZING);
		}
		this.updateAnimation(entity.startGrazingState, SheeperAnimations.LOOP_GRAZING, animationProgress, 1.0F);


//		if (!this.previousGrazingState && entity.grazingAnimation.isRunning()) {
//			System.out.println("start");
////			this.animate(SheeperAnimations.GRAZE_START);
////			this.updateAnimation(entity.grazingAnimation, SheeperAnimations.GRAZE_START, animationProgress);
//		}
//		if (this.previousGrazingState && !entity.grazingAnimation.isRunning()) {
//			System.out.println("end");
//			this.animate(SheeperAnimations.STOP_GRAZING);
////			this.updateAnimation(entity.grazingAnimation, SheeperAnimations.GRAZE_END, animationProgress);
//		}
//		this.updateAnimation(entity.grazingAnimation, SheeperAnimations.LOOP_GRAZING, animationProgress);
//		this.previousGrazingState = entity.grazingAnimation.isRunning();
	}

	@Override
	public ModelPart getPart() {
		return this.root;
	}

}
