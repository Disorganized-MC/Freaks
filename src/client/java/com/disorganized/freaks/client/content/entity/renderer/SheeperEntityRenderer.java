package com.disorganized.freaks.client.content.entity.renderer;

import com.disorganized.freaks.Freaks;
import com.disorganized.freaks.client.content.entity.feature.SheeperEntityWoolRenderer;
import com.disorganized.freaks.client.content.entity.model.SheeperEntityModel;
import com.disorganized.freaks.client.registry.ModEntityRenderers;
import com.disorganized.freaks.content.entity.SheeperEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class SheeperEntityRenderer extends MobEntityRenderer<SheeperEntity, SheeperEntityModel<SheeperEntity>> {

	private static final Identifier TEXTURE = Freaks.of("textures/entity/sheeper/default.png");

	public SheeperEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new SheeperEntityModel<>(context.getPart(ModEntityRenderers.SHEEPER_LAYER)), 0.5F);
		this.addFeature(new SheeperEntityWoolRenderer(this, context.getModelLoader()));
	}

	protected void scale(SheeperEntity entity, MatrixStack matrixStack, float f) {
		float g = entity.getClientFuseTime(f);
		float h = 1.0F + MathHelper.sin(g * 100.0F) * g * 0.01F;
		g = MathHelper.clamp(g, 0.0F, 1.0F);
		g *= g;
		g *= g;
		float i = (1.0F + g * 0.4F) * h;
		float j = (1.0F + g * 0.1F) / h;
		matrixStack.scale(i, j, i);
	}

	protected float getAnimationCounter(SheeperEntity entity, float f) {
		float g = entity.getClientFuseTime(f);
		return (int)(g * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(g, 0.5F, 1.0F);
	}

	public Identifier getTexture(SheeperEntity entity) {
		return TEXTURE;
	}

}
