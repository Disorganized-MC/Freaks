
package com.disorganized.freaks.client.content.entity.renderer;

import com.disorganized.freaks.Freaks;
import com.disorganized.freaks.client.content.entity.model.MuckModel;
import com.disorganized.freaks.client.registry.ModEntityRenderers;
import com.disorganized.freaks.content.entity.MuckEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

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
	protected void scale(MuckEntity entity, MatrixStack matrices, float f) {
		float size = entity.getSize();
		matrices.scale(size, size, size);
	}

	@Override
	public Identifier getTexture(MuckEntity entity) {
		return TEXTURE;
	}

}
