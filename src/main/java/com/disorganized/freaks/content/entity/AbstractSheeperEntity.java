package com.disorganized.freaks.content.entity;

import com.disorganized.freaks.access.FreaksExplosion;
import com.disorganized.freaks.content.entity.ai.goal.EatIronGoal;
import com.disorganized.freaks.content.entity.ai.goal.SheeperFleeGoal;
import com.disorganized.freaks.registry.ModLootTables;
import com.disorganized.freaks.registry.ModSoundEvents;
import com.disorganized.freaks.registry.ModTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Shearable;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class AbstractSheeperEntity extends CreeperEntity implements HissingEntity, Shearable, GeoEntity {

	public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("walking");
	public static final RawAnimation IDLING = RawAnimation.begin().thenLoop("idling");
	public static final RawAnimation GRAZING_START = RawAnimation.begin().thenPlay("grazing_start");
	public static final RawAnimation GRAZING_LOOP = RawAnimation.begin().thenLoop("grazing_loop");
	public static final RawAnimation GRAZING_STOP = RawAnimation.begin().thenPlay("grazing_stop");

	public static final int MAX_WOOL_LAYERS = 4;

	protected static final TrackedData<Integer> WOOL_LAYERS = DataTracker.registerData(AbstractSheeperEntity.class, TrackedDataHandlerRegistry.INTEGER);
	protected static final TrackedData<Boolean> GRAZING = DataTracker.registerData(AbstractSheeperEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
	private boolean wasGrazing = false;

	public AbstractSheeperEntity(EntityType<? extends CreeperEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(WOOL_LAYERS, MAX_WOOL_LAYERS);
		builder.add(GRAZING, false);
		this.explosionRadius = 9;
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(1, new SwimGoal(this));
		this.goalSelector.add(3, new SheeperFleeGoal<>(this, CatEntity.class, 6));
		this.goalSelector.add(3, new SheeperFleeGoal<>(this, OcelotEntity.class, 6));
		this.goalSelector.add(3, new SheeperFleeGoal<>(this, PlayerEntity.class, 6));
		this.goalSelector.add(4, new EatIronGoal(this));
		this.goalSelector.add(6, new WanderAroundFarGoal(this, 1));
		this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8));
		this.goalSelector.add(8, new LookAroundGoal(this));
	}

	public int getWoolLayers() {
		return Math.clamp(this.dataTracker.get(WOOL_LAYERS), 0, AbstractSheeperEntity.MAX_WOOL_LAYERS);
	}

	private void setWoolLayers(int layers) {
		this.dataTracker.set(WOOL_LAYERS, layers);
	}

	public void addWoolLayer() {
		this.setWoolLayers(this.dataTracker.get(WOOL_LAYERS) + 1);
	}

	public void removeWoolLayer() {
		this.setWoolLayers(this.dataTracker.get(WOOL_LAYERS) - 1);
	}

	public boolean canGrowWool() {
		return getWoolLayers() < MAX_WOOL_LAYERS;
	}

	@Override
	public boolean isShearable() {
		return this.isAlive() && this.getWoolLayers() > 0 && !this.isOnFire();
	}

	public boolean isGrazing() {
		return this.dataTracker.get(GRAZING);
	}

	public void setGrazing(boolean grazing) {
		this.dataTracker.set(GRAZING, grazing);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("wool_layers", this.getWoolLayers());
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.setWoolLayers(nbt.getInt("wool_layers"));
	}

	@Override
	public void onTrackedDataSet(TrackedData<?> data) {
		if (PassiveEntity.CHILD.equals(data)) {
			this.calculateDimensions();
		}

		super.onTrackedDataSet(data);
	}

	@Override
	public void explode() {
		if (this.getWorld().isClient) return;

		float multiplier = this.shouldRenderOverlay() ? 2.0F : 1.0F;
		this.dead = true;
		Explosion explosion = this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * multiplier, true, World.ExplosionSourceType.NONE);
		((FreaksExplosion)explosion).getAffectedEntities().forEach(entity -> entity.setFireTicks(60));
		this.discard();
		this.spawnEffectsCloud();
	}

	@Override
	public void onEatingGrass() {
		super.onEatingGrass();
		this.addWoolLayer();
	}

	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (stack.isIn(ModTags.SHEEPER_FOOD)) {
//			return this.tryFeed(stack, player, hand);
		} else if (this.isShearable() || stack.isOf(Items.SHEARS)) {
			return this.tryShear(stack, player, hand);
		}
		return super.interactMob(player, hand);
	}

//	public ActionResult tryFeed(ItemStack stack, PlayerEntity player, Hand hand) {
//		int age = this.getBreedingAge();
//		if (!this.getWorld().isClient && age == 0 && this.canEat()) {
//			stack.decrementUnlessCreative(1, player);
//			this.loveTicks = 600;
//			if (player != null) this.lovingPlayer = player.getUuid();
//			this.getWorld().sendEntityStatus(this, (byte)18);
//			this.ignite();
//
//			return ActionResult.SUCCESS;
//		}
//
//		if (this.isBaby()) {
//			stack.decrementUnlessCreative(1, player);
//			int toGrowUp = (int)((float)(-age / 20) * 0.1F);
//			this.growUp(toGrowUp, true);
//			return ActionResult.success(this.getWorld().isClient);
//		}
//
//		if (this.getWorld().isClient) return ActionResult.CONSUME;
//		return ActionResult.PASS;
//	}

	public ActionResult tryShear(ItemStack stack, PlayerEntity player, Hand hand) {
		this.sheared(SoundCategory.PLAYERS);
		this.emitGameEvent(GameEvent.SHEAR, player);
		if (!this.getWorld().isClient) stack.damage(1, player, getSlotForHand(hand));
		this.ignite();

		return ActionResult.success(this.getWorld().isClient);
	}

	@Override
	public void sheared(SoundCategory category) {
		this.getWorld().playSoundFromEntity(null, this, ModSoundEvents.ENTITY_SHEEPER_SHEAR, category, 1.0F, 1.0F);
		this.dropShearedItems();
		this.removeWoolLayer();
	}

	public void dropShearedItems() {
		if (this.getWorld().isClient) return;
		ServerWorld world = (ServerWorld) this.getWorld();

		LootTable table = world.getServer().getReloadableRegistries().getLootTable(ModLootTables.SHEEPER_SHEARING);
		LootContextParameterSet parameters = new LootContextParameterSet.Builder(world)
			.add(LootContextParameters.ORIGIN, this.getPos())
			.add(LootContextParameters.THIS_ENTITY, this)
			.build(LootContextTypes.SHEARING);
		table.generateLoot(parameters).forEach(stack -> this.dropStack(stack, this.getHeight()));
	}

	@Override
	public int getFuseTime() {
		return (int)(this.fuseTime * (1 + this.getWoolLayers() * 0.5));
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "movement", 0, state -> {
			boolean isGrazing = this.isGrazing();

			if (isGrazing && !this.wasGrazing) {
				this.wasGrazing = true;
				return state.setAndContinue(GRAZING_START);
			}
			if (!isGrazing && this.wasGrazing) {
				this.wasGrazing = false;
				return state.setAndContinue(GRAZING_STOP);
			}
			if (isGrazing && this.wasGrazing) {
				if (state.getController().hasAnimationFinished()) {
					return state.setAndContinue(GRAZING_LOOP);
				}
				return PlayState.CONTINUE;
			}

			// Not grazing - normal movement animations
			if (state.isMoving()) {
				return state.setAndContinue(WALKING);
			}

			return state.setAndContinue(IDLING);
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.animationCache;
	}

	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return CreeperEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15);
	}

}
