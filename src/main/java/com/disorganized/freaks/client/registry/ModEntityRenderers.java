package com.disorganized.freaks.client.registry;

import com.disorganized.freaks.Freaks;
import com.disorganized.freaks.client.content.entity.model.MuckModel;
import com.disorganized.freaks.client.content.entity.renderer.MuckRenderer;
import com.disorganized.freaks.client.content.entity.renderer.MudBubbleRenderer;
import com.disorganized.freaks.client.content.entity.renderer.SheeperRenderer;
import com.disorganized.freaks.registry.ModEntityTypes;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class ModEntityRenderers {

	public static final EntityModelLayer SHEEPER_LAYER = new EntityModelLayer(Freaks.of("sheeper"), "main");
	public static final EntityModelLayer SHEEPER_WOOL_1_LAYER = new EntityModelLayer(Freaks.of("sheeper_wool_1"), "main");
	public static final EntityModelLayer SHEEPER_WOOL_2_LAYER = new EntityModelLayer(Freaks.of("sheeper_wool_2"), "main");
	public static final EntityModelLayer SHEEPER_WOOL_3_LAYER = new EntityModelLayer(Freaks.of("sheeper_wool_3"), "main");
	public static final EntityModelLayer SHEEPER_WOOL_4_LAYER = new EntityModelLayer(Freaks.of("sheeper_wool_4"), "main");

	public static final EntityModelLayer MUCK_LAYER = new EntityModelLayer(Freaks.of("muck"), "main");

	public static void init() {
		EntityRendererRegistry.register(ModEntityTypes.SHEEPER, SheeperRenderer::new);

//		EntityRendererRegistry.register(ModEntityTypes.SHEEPER, SheeperRenderer::new);
//		EntityModelLayerRegistry.registerModelLayer(SHEEPER_LAYER, () -> SheeperModel.getTexturedModelData(Dilation.NONE));
//		EntityModelLayerRegistry.registerModelLayer(SHEEPER_WOOL_1_LAYER, SheeperWoolModel::getFirstLayerModelData);
//		EntityModelLayerRegistry.registerModelLayer(SHEEPER_WOOL_2_LAYER, SheeperWoolModel::getSecondLayerModelData);
//		EntityModelLayerRegistry.registerModelLayer(SHEEPER_WOOL_3_LAYER, SheeperWoolModel::getThirdLayerModelData);
//		EntityModelLayerRegistry.registerModelLayer(SHEEPER_WOOL_4_LAYER, SheeperWoolModel::getFourthLayerModelData);

		EntityRendererRegistry.register(ModEntityTypes.MUCK, MuckRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(MUCK_LAYER, MuckModel::getTexturedModelData);
		EntityRendererRegistry.register(ModEntityTypes.MUD_BUBBLE, MudBubbleRenderer::new);
	}

}
