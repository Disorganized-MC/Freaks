package com.disorganized.freaks.content.entity;

import com.disorganized.freaks.content.entity.ai.goal.EatIronGoal;
import com.disorganized.freaks.content.entity.ai.goal.SheeperFleeGoal;
import com.disorganized.freaks.content.misc.ExplosionCollection;
import com.disorganized.freaks.registry.ModLootTables;
import com.disorganized.freaks.registry.ModSoundEvents;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;

import java.util.List;

public abstract class AbstractSheeperEntity  extends CreeperEntity implements HissingEntity  {

	public final AnimationState startGrazingState = new AnimationState();
	public final AnimationState stopGrazingState = new AnimationState();

	private static final int MAX_WOOL_LAYERS = 4;

	private static final TrackedData<Integer> WOOL_LAYERS = DataTracker.registerData(SheeperEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Boolean> GRAZING = DataTracker.registerData(SheeperEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	public AbstractSheeperEntity(EntityType<? extends CreeperEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(WOOL_LAYERS, MAX_WOOL_LAYERS);
		this.dataTracker.startTracking(GRAZING, false);
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
		return this.dataTracker.get(WOOL_LAYERS);
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
		return getWoolLayers() <= MAX_WOOL_LAYERS;
	}

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
	public void explode() {
		if (this.getWorld().isClient) return;

		float multiplier = this.shouldRenderOverlay() ? 2.0F : 1.0F;
		this.dead = true;
		Explosion explosion = this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * multiplier, true, World.ExplosionSourceType.NONE);
		((ExplosionCollection)explosion).getAffectedEntities().forEach(entity -> entity.setFireTicks(60));
		this.discard();
		this.spawnEffectsCloud();
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.getWorld().isClient) return;

		if (this.isGrazing()) {
			this.startGrazingState.startIfNotRunning(this.age);
			this.stopGrazingState.stop();
		} else {
			this.stopGrazingState.startIfNotRunning(this.age);
			this.startGrazingState.stop();
		}
//		this.wasGrazing = this.isGrazing();
	}

	@Override
	public void onEatingGrass() {
		super.onEatingGrass();
		this.addWoolLayer();
	}

	@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (!stack.isOf(Items.SHEARS)) return super.interactMob(player, hand);

		if (!this.getWorld().isClient && this.isShearable()) {
			this.sheared();
			this.emitGameEvent(GameEvent.SHEAR, player);
			stack.damage(1, player, cPlayer -> cPlayer.sendToolBreakStatus(hand));
			return ActionResult.SUCCESS;
		} else {
			return ActionResult.CONSUME;
		}
	}

	public void sheared() {
		this.getWorld().playSoundFromEntity(null, this, ModSoundEvents.ENTITY_SHEEPER_SHEAR, SoundCategory.PLAYERS, 1.0F, 1.0F);
		this.removeWoolLayer();

		ServerWorld world = (ServerWorld)this.getWorld();
		LootTable table = world.getServer().getLootManager().getLootTable(ModLootTables.SHEEPER_SHEARED);
		LootContextParameterSet lootContextParameterSet = new LootContextParameterSet.Builder(world)
			.add(LootContextParameters.ORIGIN, this.getPos())
			.add(LootContextParameters.THIS_ENTITY, this)
			.build(LootContextTypes.GIFT);
		List<ItemStack> items = table.generateLoot(lootContextParameterSet);
		for (ItemStack droppedStack : items) {
			ItemEntity itemEntity = this.dropStack(droppedStack, 1);
			if (itemEntity != null) {
				Vec3d velocity = itemEntity.getVelocity();
				velocity = velocity.add(
					(this.random.nextFloat() - this.random.nextFloat()) * 0.1F,
					this.random.nextFloat() * 0.05F,
					(this.random.nextFloat() - this.random.nextFloat()) * 0.1F
				);
				itemEntity.setVelocity(velocity);
			}
		}
	}

	@Override
	public int getFuseTime() {
		return (int)(this.fuseTime * (1 + this.getWoolLayers() * 0.5));
	}


	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return CreeperEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15);
	}

}
