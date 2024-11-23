package com.disorganized.freaks.client.content.entity.feature;

import com.disorganized.freaks.client.content.entity.model.SheeperEntityModel;
import com.disorganized.freaks.content.entity.SheeperEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.client.render.entity.model.SheepWoolEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.SheepEntity;

public class SheeperWoolFeatureRenderer extends FeatureRenderer<SheeperEntity, SheeperEntityModel<SheeperEntity>> {

	private final SheepWoolEntityModel<SheepEntity> model;

	public SheeperWoolFeatureRenderer(FeatureRendererContext<SheeperEntity, SheeperEntityModel<SheeperEntity>> context, EntityModelLoader loader) {
		super(context);
		this.model = new SheepWoolEntityModel(loader.getModelPart(EntityModelLayers.SHEEP_FUR));
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, SheeperEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

	}

}
