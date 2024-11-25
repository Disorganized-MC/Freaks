package com.disorganized.freaks.client.content.entity.feature;

import com.disorganized.freaks.Freaks;
import com.disorganized.freaks.client.content.entity.model.SheeperModel;
import com.disorganized.freaks.client.content.entity.model.SheeperWoolModel;
import com.disorganized.freaks.client.registry.ModEntityRenderers;
import com.disorganized.freaks.content.entity.SheeperEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SheeperWoolRenderer extends FeatureRenderer<SheeperEntity, SheeperModel<SheeperEntity>> {

	public static final Identifier DEFAULT = Freaks.of("textures/entity/sheeper/wool/default.png");
	public static final Identifier FLAMING = Freaks.of("textures/entity/sheeper/wool/flaming.png");

	private final SheeperWoolModel<SheeperEntity> model;

	public SheeperWoolRenderer(FeatureRendererContext<SheeperEntity, SheeperModel<SheeperEntity>> context, EntityModelLoader loader) {
		super(context);
		this.model = new SheeperWoolModel<>(loader.getModelPart(ModEntityRenderers.SHEEPER_WOOL_LAYER));
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, SheeperEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (entity.getWoolLayers() == 0) return;
		Identifier texture = entity.isOnFire() ? FLAMING : DEFAULT;
		render(this.getContextModel(), this.model, texture, matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch, 1, 1, 1);
	}

}
