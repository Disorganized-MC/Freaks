package com.disorganized.freaks.client.content.entity.renderer;

import com.disorganized.freaks.content.entity.MultiFallingBlockEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class MultiFallingBlockRenderer extends EntityRenderer<MultiFallingBlockEntity> {

	private final BlockRenderManager manager;

	public MultiFallingBlockRenderer(EntityRendererFactory.Context context) {
		super(context);
		this.manager = context.getBlockRenderManager();
	}

	@Override
	public void render(MultiFallingBlockEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();

		int size = entity.getSize();
		float offset = (size - 1) / -2F;
		if (size > 1) matrices.translate(offset, offset, offset);

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				for (int z = 0; z < size; z++) {
					this.renderBlockPart(entity, matrices, vertexConsumers, light, x, y, z);
				}
			}
		}

		matrices.pop();
	}

	public void renderBlockPart(MultiFallingBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int x, int y, int z) {
		matrices.push();
		matrices.translate(x, y, z);

		this.manager.renderBlockAsEntity(
			entity.getBlockState(),
			matrices,
			vertexConsumers,
			light,
			OverlayTexture.DEFAULT_UV
		);

		matrices.pop();
	}

	@Override
	public Identifier getTexture(MultiFallingBlockEntity entity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}

}
