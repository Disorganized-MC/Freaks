package com.disorganized.freaks.client.content.entity.model;

import com.disorganized.freaks.Freaks;
import com.disorganized.freaks.content.entity.SheeperEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtil;

public class SheeperWoolLayer extends GeoRenderLayer<SheeperEntity> {

	private static final Identifier TEXTURE_BASE = Freaks.of("textures/entity/sheeper/wool/");
	private final SheeperWoolModel model;

	public SheeperWoolLayer(GeoRenderer<SheeperEntity> entityRendererIn) {
		super(entityRendererIn);
		this.model = new SheeperWoolModel();
	}

	@Override
	public void render(
		MatrixStack poseStack, SheeperEntity animatable, BakedGeoModel bakedModel,
		RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer,
		float partialTick, int packedLight, int packedOverlay
	) {
		if (animatable.getWoolLayers() <= 0) {
			return;
		}

		poseStack.push();
//		poseStack.translate(0, -0.333f, 0);
		RenderUtil.rotateMatrixAroundBone(poseStack, bakedModel.getBone("body").orElseThrow());

		int layer = animatable.getWoolLayers();
		String modifier = animatable.isOnFire() ? "lit" : "default";
		Identifier texture = TEXTURE_BASE.withPath(path -> path + modifier + "/" + layer + ".png");

		RenderLayer type = RenderLayer.getEntityCutoutNoCull(texture);

		BakedGeoModel model = this.model.getBakedModel(this.model.getModelResource(animatable));
		this.getRenderer().reRender(model, poseStack, bufferSource, animatable, type, bufferSource.getBuffer(type), partialTick, packedLight, OverlayTexture.DEFAULT_UV, -1);
		poseStack.pop();
	}
}
