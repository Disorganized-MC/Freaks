package com.disorganized.freaks.client.content.entity.model;

import com.disorganized.freaks.content.entity.MuckEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

public class MuckModel<T extends MuckEntity> extends SinglePartEntityModel<T> {

	private final ModelPart root;
	private final ModelPart body;

	public MuckModel(ModelPart root) {
		this.root = root;
		this.body = root.getChild("body");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F))
			.uv(0, 50).cuboid(-22.0F, -22.0F, 0.0F, 14.0F, 14.0F, 0.0F, new Dilation(0.0F))
			.uv(0, 50).mirrored().cuboid(8.0F, -18.0F, 0.0F, 14.0F, 14.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(MuckEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public ModelPart getPart() {
		return this.root;
	}

}
