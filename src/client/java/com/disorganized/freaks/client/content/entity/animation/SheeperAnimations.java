package com.disorganized.freaks.client.content.entity.animation;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class SheeperAnimations {

	public static final Animation GRAZE_START = Animation.Builder.create(0.4F)
		.addBoneAnimation("neck", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.build();

	public static final Animation GRAZE_LOOP = Animation.Builder.create(0.4F).looping()
		.addBoneAnimation("head", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.2F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.build();

	public static final Animation GRAZE_END = Animation.Builder.create(0.4F)
		.addBoneAnimation("neck", new Transformation(Transformation.Targets.ROTATE,
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
		))
		.build();

}
