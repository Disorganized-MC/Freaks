package com.disorganized.freaks.client.content.entity.model;

import com.disorganized.freaks.Freaks;
import com.disorganized.freaks.content.entity.SheeperEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class SheeperWoolModel extends GeoModel<SheeperEntity> {

	@Override
	public Identifier getModelResource(SheeperEntity animatable) {
		if (animatable.getWoolLayers() <= 0) {
			return null;
		}

		int layer = animatable.getWoolLayers();
		return Freaks.of("geo/entity/sheeper/wool/" + layer + ".geo.json");
	}

	@Override
	public Identifier getTextureResource(SheeperEntity animatable) {
		if (animatable.getWoolLayers() <= 0) {
			return null;
		}

		int layer = animatable.getWoolLayers();
		String modifier = animatable.isOnFire() ? "lit" : "default";
		return Freaks.of("textures/entity/sheeper/wool/" + modifier + "/" + layer + ".png");
	}

	@Override
	public Identifier getAnimationResource(SheeperEntity animatable) {
		return Freaks.of("animations/entity/sheeper.json");
	}

	@Override
	public void setCustomAnimations(SheeperEntity animatable, long instanceId, AnimationState<SheeperEntity> animationState) {
		super.setCustomAnimations(animatable, instanceId, animationState);

		GeoBone head = getAnimationProcessor().getBone("head");
		if (head == null) return;

		EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
		head.setRotX(entityData.headPitch() * ((float) Math.PI / 180F));
		head.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F));
	}

}
