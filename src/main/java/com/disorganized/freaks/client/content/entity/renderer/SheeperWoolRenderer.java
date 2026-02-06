package com.disorganized.freaks.client.content.entity.renderer;

import com.disorganized.freaks.Freaks;
import com.disorganized.freaks.client.content.entity.model.SheeperModel;
import com.disorganized.freaks.client.content.entity.model.SheeperWoolModel;
import com.disorganized.freaks.content.entity.SheeperEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SheeperWoolRenderer extends FeatureRenderer<SheeperEntity, SheeperModel<SheeperEntity>> {

	public static final Identifier TEXTURE = Freaks.of("textures/entity/sheeper/wool/");

	private final SheeperWoolModel[] woolModels = new SheeperWoolModel[5];

	public SheeperWoolRenderer(FeatureRendererContext<SheeperEntity, SheeperModel<SheeperEntity>> context,
		 ModelPart layer1Part,
		 ModelPart layer2Part,
		 ModelPart layer3Part,
		 ModelPart layer4Part
	) {
		super(context);

		this.woolModels[1] = new SheeperWoolModel<>(layer1Part);
		this.woolModels[2] = new SheeperWoolModel<>(layer2Part);
		this.woolModels[3] = new SheeperWoolModel<>(layer3Part);
		this.woolModels[4] = new SheeperWoolModel<>(layer4Part);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, SheeperEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (entity.getWoolLayers() <= 0) return;

		int layer = entity.getWoolLayers();
		String modifier = entity.isOnFire() ? "lit" : "default";
		Identifier texture = TEXTURE.withPath(path -> path + modifier + "/" + layer + ".png");
		render(this.getContextModel(), this.woolModels[layer], texture, matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch, -1);
	}

}
