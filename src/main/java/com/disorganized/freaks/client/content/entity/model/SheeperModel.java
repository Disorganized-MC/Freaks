package com.disorganized.freaks.client.content.entity.model;

import com.disorganized.freaks.client.content.entity.animation.SheeperAnimations;
import com.disorganized.freaks.content.entity.SheeperEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class SheeperModel extends SinglePartEntityModel<SheeperEntity> {

	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart leg1;
	private final ModelPart leg2;
	private final ModelPart leg3;
	private final ModelPart leg4;

	public SheeperModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.head = this.body.getChild("head");
		this.leg1 = this.root.getChild("leg1");
		this.leg2 = this.root.getChild("leg2");
		this.leg3 = this.root.getChild("leg3");
		this.leg4 = this.root.getChild("leg4");
	}

	public static TexturedModelData getTexturedModelData(Dilation d) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 13.0F, -2.5F));

		ModelPartData body = root.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -8.0F, -6.0F, 12.0F, 9.0F, 12.0F, new Dilation(0.0F))
			.uv(0, 21).cuboid(-4.0F, -18.0F, -4.0F, 8.0F, 10.0F, 8.0F, new Dilation(0.0F))
			.uv(32, 37).cuboid(-4.0F, -18.0F, -6.0F, 8.0F, 5.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 5.0F, 2.5F));

		ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(32, 21).cuboid(-4.0F, -5.0F, -6.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -16.0F, -8.0F));

		ModelPartData leg1 = root.addChild("leg1", ModelPartBuilder.create().uv(48, 0).cuboid(-2.0F, -1.0F, -3.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-6.0F, 6.0F, -3.5F));

		ModelPartData leg2 = root.addChild("leg2", ModelPartBuilder.create().uv(48, 0).mirrored().cuboid(-2.0F, -1.0F, -3.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(6.0F, 6.0F, -3.5F));

		ModelPartData leg3 = root.addChild("leg3", ModelPartBuilder.create().uv(48, 0).cuboid(-2.0F, -1.0F, -1.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-6.0F, 6.0F, 8.5F));

		ModelPartData leg4 = root.addChild("leg4", ModelPartBuilder.create().uv(48, 0).mirrored().cuboid(-2.0F, -1.0F, -1.0F, 4.0F, 6.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(6.0F, 6.0F, 8.5F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(SheeperEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.head.yaw = headYaw * 0.017453292F;
		this.head.pitch = headPitch * 0.017453292F;
		this.leg4.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
		this.leg3.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
		this.leg2.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;
		this.leg1.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;

		this.updateAnimation(entity.idlingAnimationState, SheeperAnimations.IDLING, animationProgress, 1.0F);
//		if (entity.startGrazingState.isRunning()) {
//			this.updateAnimation(entity.startGrazingState, SheeperAnimations.GRAZING_LOOP, animationProgress, 1.0F);
//		}
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		this.root.render(matrices, vertices, light, overlay, color);
	}

	@Override
	public ModelPart getPart() {
		return this.root;
	}

}
