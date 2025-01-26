package com.disorganized.freaks.client.registry;

import com.disorganized.freaks.Freaks;
import com.disorganized.freaks.client.content.entity.model.MuckModel;
import com.disorganized.freaks.client.content.entity.model.SheeperModel;
import com.disorganized.freaks.client.content.entity.model.SheeperWoolModel;
import com.disorganized.freaks.client.content.entity.renderer.MuckRenderer;
import com.disorganized.freaks.client.content.entity.renderer.MudBubbleRenderer;
import com.disorganized.freaks.client.content.entity.renderer.SheeperRenderer;
import com.disorganized.freaks.registry.ModEntityTypes;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.entity.FallingBlockEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class ModEntityRenderers {

	public static final EntityModelLayer SHEEPER_LAYER = new EntityModelLayer(Freaks.of("sheeper"), "main");
	public static final EntityModelLayer SHEEPER_WOOL_LAYER = new EntityModelLayer(Freaks.of("sheeper"), "wool");
	public static final EntityModelLayer MUCK_LAYER = new EntityModelLayer(Freaks.of("muck"), "main");

	public static void init() {
		EntityRendererRegistry.register(ModEntityTypes.SHEEPER, SheeperRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(SHEEPER_LAYER, () -> SheeperModel.getTexturedModelData(Dilation.NONE));
		EntityModelLayerRegistry.registerModelLayer(SHEEPER_WOOL_LAYER, SheeperWoolModel::getTexturedModelData);

		EntityRendererRegistry.register(ModEntityTypes.MUCK, MuckRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(MUCK_LAYER, MuckModel::getTexturedModelData);
		EntityRendererRegistry.register(ModEntityTypes.MUD_BUBBLE, MudBubbleRenderer::new);
	}

}
