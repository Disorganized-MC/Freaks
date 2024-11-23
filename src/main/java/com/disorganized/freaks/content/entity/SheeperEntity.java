package com.disorganized.freaks.content.entity;

import com.disorganized.freaks.content.entity.ai.goal.EatIronGoal;
import com.disorganized.freaks.registry.ModLootTables;
import com.disorganized.freaks.registry.ModSoundEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.CreeperEntity;
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
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.UnaryOperator;

public class SheeperEntity extends CreeperEntity implements HissingEntity {

	private static final int MAX_IRON_TIMER = 40;
	private static final int MAX_WOOL_LAYERS = 4;

	private static final TrackedData<Integer> WOOL_LAYERS = DataTracker.registerData(SheeperEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Boolean> FLAMING = DataTracker.registerData(SheeperEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	private int eatIronTimer;
	private EatIronGoal eatIronGoal;

	public SheeperEntity(EntityType<? extends CreeperEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(WOOL_LAYERS, MAX_WOOL_LAYERS);
		this.dataTracker.startTracking(FLAMING, false);
	}

	@Override
	protected void initGoals() {
		this.eatIronGoal = new EatIronGoal(this);
		this.goalSelector.add(1, new SwimGoal(this));
		this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
		this.goalSelector.add(2, new CreeperIgniteGoal(this));
		this.goalSelector.add(3, new FleeEntityGoal<>(this, PassiveEntity.class, 6.0F, 1.0, 1.2));
		this.goalSelector.add(3, new FleeEntityGoal<>(this, PlayerEntity.class, 6.0F, 1.0, 1.2));
		this.goalSelector.add(5, this.eatIronGoal);
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(6, new LookAroundGoal(this));
	}

	public int getWoolLayers() {
		return this.dataTracker.get(WOOL_LAYERS);
	}

	public void modifyWoolLayers(UnaryOperator<Integer> function) {
		this.dataTracker.set(WOOL_LAYERS, function.apply(this.dataTracker.get(WOOL_LAYERS)));
	}

	public boolean isShearable() {
		return this.isAlive() && this.getWoolLayers() != 0 && !this.isFlaming();
	}

	public boolean isFlaming() {
		return this.dataTracker.get(FLAMING);
	}

	public void setFlaming(boolean flaming) {
		this.dataTracker.set(FLAMING, flaming);
	}


	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("wool_layers", this.getWoolLayers());
		nbt.putBoolean("flaming", this.isFlaming());
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.modifyWoolLayers(i -> nbt.getInt("wool_layers"));
		this.setFlaming(nbt.getBoolean("flaming"));
	}


	@Override
	protected void mobTick() {
		this.eatIronTimer = this.eatIronGoal.getTimer();
		super.mobTick();
	}

	@Override
	public void tickMovement() {
		if (this.getWorld().isClient)
			this.eatIronTimer = Math.max(0, this.eatIronTimer - 1);
		super.tickMovement();
	}

	@Override
	public void handleStatus(byte status) {
		if (status == 10) {
			this.eatIronTimer = MAX_IRON_TIMER;
		} else {
			super.handleStatus(status);
		}
	}

	public float getNeckAngle(float delta) {
		int start = 4;
		int end = MAX_IRON_TIMER - start;
		if (this.eatIronTimer <= 0) {
			return 0.0F;
		} else if (this.eatIronTimer >= start && this.eatIronTimer <= end) {
			return 1.0F;
		} else {
			return this.eatIronTimer < start ? ((float)this.eatIronTimer - delta) / start : -((float)(this.eatIronTimer - MAX_IRON_TIMER) - delta) / start;
		}
	}

	public float getHeadAngle(float delta) {
		int start = 4;
		int end = MAX_IRON_TIMER - start;
		if (this.eatIronTimer > start && this.eatIronTimer <= end) {
			float f = ((float)(this.eatIronTimer - start) - delta) / 32.0F;
			return 0.62831855F + 0.21991149F * MathHelper.sin(f * 28.7F);
		} else {
			return this.eatIronTimer > 0 ? 0.62831855F : this.getPitch() * 0.017453292F;
		}
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
		this.modifyWoolLayers(i -> --i);

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
	protected SoundEvent getHurtSound(DamageSource source) {
		return ModSoundEvents.ENTITY_SHEEPER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ModSoundEvents.ENTITY_SHEEPER_DEATH;
	}

	@Override
	protected @Nullable SoundEvent getAmbientSound() {
		return ModSoundEvents.ENTITY_SHEEPER_AMBIENT;
	}

	@Override
	public SoundEvent getPrimedSound() {
		return ModSoundEvents.ENTITY_SHEEPER_PRIMED;
	}

	public static DefaultAttributeContainer.Builder createMobAttributes() {
		return CreeperEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1);
	}

}
