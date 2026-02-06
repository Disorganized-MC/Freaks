package com.disorganized.freaks.client.content.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.Entity;

import java.util.List;
import java.util.function.Function;

public class SheeperWoolModel<T extends Entity> extends SinglePartEntityModel<T> {

	private final ModelPart root;

	public SheeperWoolModel(ModelPart root) {
		this.root = root;
	}

	public static TexturedModelData getFirstLayerModelData() {
		ModelData data = new ModelData();
		data.getRoot().addChild("wool1", ModelPartBuilder.create().uv(32, 37).cuboid(-4.0F, -24.0F, -6.0F, 8.0F, 5.0F, 2.0F, new Dilation(0.3F))
			.uv(0, 21).cuboid(-4.0F, -24.0F, -4.0F, 8.0F, 10.0F, 8.0F, new Dilation(0.3F))
			.uv(0, 0).cuboid(-6.0F, -14.0F, -6.0F, 12.0F, 9.0F, 12.0F, new Dilation(0.3F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(data, 128, 128);
	}

	public static TexturedModelData getSecondLayerModelData() {
		ModelData data = new ModelData();
		ModelPartData part = data.getRoot().addChild("wool2", ModelPartBuilder.create().uv(0, 0).cuboid(-7.0F, -16.0F, -7.0F, 14.0F, 12.0F, 14.0F, new Dilation(0.0F))
			.uv(0, 26).cuboid(-6.0F, -26.0F, -6.0F, 12.0F, 10.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		part.addChild("fluff2_r1", ModelPartBuilder.create().uv(56, 0).cuboid(-10.8995F, -12.0F, 0.0F, 21.799F, 23.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -16.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		part.addChild("fluff1_r1", ModelPartBuilder.create().uv(56, 0).cuboid(-10.8995F, 5.0F, 0.0F, 21.799F, 24.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -33.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		return TexturedModelData.of(data, 128, 128);
	}

	public static TexturedModelData getThirdLayerModelData() {
		ModelData data = new ModelData();
		ModelPartData part = data.getRoot().addChild("wool3", ModelPartBuilder.create().uv(0, 2).cuboid(-7.0F, -29.0F, -7.0F, 14.0F, 25.0F, 14.0F, new Dilation(-0.01F))
			.uv(42, 0).cuboid(-7.0F, -29.0F, -11.0F, 14.0F, 12.0F, 4.0F, new Dilation(-0.01F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		part.addChild("fluff2_r2", ModelPartBuilder.create().uv(56, -5).cuboid(-0.01F, -13.5F, -10.8954F, 0.02F, 27.0F, 21.7907F, new Dilation(-0.01F)), ModelTransform.of(0.0F, -17.5F, 0.0F, 0.0F, -2.3562F, 0.0F));

		part.addChild("fluff1_r2", ModelPartBuilder.create().uv(56, -5).cuboid(0.01F, -30.0F, -13.3096F, 0.02F, 27.0F, 21.7907F, new Dilation(-0.01F)), ModelTransform.of(1.7212F, -1.0F, -1.693F, 0.0F, 2.3562F, 0.0F));
		return TexturedModelData.of(data, 128, 128);
	}

	public static TexturedModelData getFourthLayerModelData() {
		ModelData data = new ModelData();
		ModelPartData part = data.getRoot().addChild("wool4", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -31.0F, -8.0F, 16.0F, 27.0F, 16.0F, new Dilation(-0.01F))
			.uv(48, 0).cuboid(-8.0F, -31.0F, -12.0F, 16.0F, 14.0F, 4.0F, new Dilation(-0.01F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		part.addChild("fluff2_r3", ModelPartBuilder.create().uv(64, -6).cuboid(0.0F, -28.0F, -12.3096F, 0.03F, 27.0F, 24.6191F, new Dilation(-0.01F)), ModelTransform.of(-0.0141F, -5.0F, 0.0141F, 0.0F, 0.7854F, 0.0F));

		part.addChild("fluff1_r3", ModelPartBuilder.create().uv(64, -6).cuboid(0.0F, -28.0F, -12.3096F, 0.03F, 27.0F, 24.6191F, new Dilation(-0.01F)), ModelTransform.of(-0.0141F, -5.0F, 0.0141F, 0.0F, 2.3562F, 0.0F));

		return TexturedModelData.of(data, 128, 128);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {}

	@Override
	public ModelPart getPart() {
		return this.root;
	}

}
