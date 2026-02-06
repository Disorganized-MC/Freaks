package com.disorganized.freaks.client.content.entity.renderer;

import com.disorganized.freaks.client.content.entity.model.SheeperModel;
import com.disorganized.freaks.client.content.entity.model.SheeperWoolLayer;
import com.disorganized.freaks.content.entity.SheeperEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SheeperRenderer extends GeoEntityRenderer<SheeperEntity> {

	public SheeperRenderer(EntityRendererFactory.Context context) {
		super(context, new SheeperModel());
		this.shadowRadius = 0.5F;
		addRenderLayer(new SheeperWoolLayer(this));
	}

	@Override
	public void render(SheeperEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
		float fuseTime = entity.getClientFuseTime(partialTick);
		float pulseFactor = 1.0F + MathHelper.sin(fuseTime * 100.0F) * fuseTime * 0.01F;
		fuseTime = MathHelper.clamp(fuseTime, 0.0F, 1.0F);
		fuseTime *= fuseTime;
		fuseTime *= fuseTime;
		float scaleX = (1.0F + fuseTime * 0.4F) * pulseFactor;
		float scaleY = (1.0F + fuseTime * 0.1F) / pulseFactor;

		poseStack.push();
		poseStack.scale(scaleX, scaleY, scaleX);

		super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

		poseStack.pop();
	}

	@Override
	protected float getDeathMaxRotation(SheeperEntity animatable) {
		float fuseTime = animatable.getClientFuseTime(1.0F);
		return (int)(fuseTime * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(fuseTime, 0.5F, 1.0F);
	}

}
