
package com.disorganized.freaks.client.content.entity.renderer;

import com.disorganized.freaks.Freaks;
import com.disorganized.freaks.client.content.entity.model.MuckModel;
import com.disorganized.freaks.client.registry.ModEntityRenderers;
import com.disorganized.freaks.content.entity.MuckEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class MuckRenderer extends MobEntityRenderer<MuckEntity, MuckModel<MuckEntity>> {

	private static final Identifier TEXTURE = Freaks.of("textures/entity/muck.png");

	public MuckRenderer(EntityRendererFactory.Context context) {
		super(context, new MuckModel<>(context.getPart(ModEntityRenderers.MUCK_LAYER)), 0.5F);
	}

	@Override
	public void render(MuckEntity entity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertices, int i) {
		this.shadowRadius = entity.getSize();
		super.render(entity, f, g, matrices, vertices, i);
	}

	@Override
	protected void scale(MuckEntity slimeEntity, MatrixStack matrixStack, float f) {
		matrixStack.scale(0.999F, 0.999F, 0.999F);
		matrixStack.translate(0.0F, 0.001F, 0.0F);
		float size = (float)slimeEntity.getSize();
		float squishSize = MathHelper.lerp(f, slimeEntity.lastStretch, slimeEntity.stretch) / (size * 0.5F + 1.0F);
		float width = 1.0F / (squishSize + 1.0F);
		matrixStack.scale(width * size, 1.0F / width * size, width * size);
	}

	@Override
	public Identifier getTexture(MuckEntity entity) {
		return TEXTURE;
	}

}
