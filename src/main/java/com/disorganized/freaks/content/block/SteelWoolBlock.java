package com.disorganized.freaks.content.block;

import com.disorganized.freaks.mixin.FireBlockInvoker;
import com.disorganized.freaks.registry.ModDamageTypes;
import com.disorganized.freaks.registry.ModSoundEvents;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class SteelWoolBlock extends Block implements Ignitable {

    public static final BooleanProperty LIT = Properties.LIT;

    public SteelWoolBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LIT, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    public static int getLuminance(BlockState state) {
        return state.get(LIT) ? 4 : 0;
    }

	@Override
	public boolean onIgnited(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (!state.get(LIT)) {
			return !world.setBlockState(pos, world.getBlockState(pos).with(LIT, true));
		}
		List<BlockPos> neighbours = getNeighbours(pos);
		for (BlockPos nPos : neighbours) {
			BlockState nState = world.getBlockState(nPos);
			if (nState.getBlock() instanceof SteelWoolBlock && !nState.get(LIT)) return false;
		}
		return !world.removeBlock(pos, false);
	}




    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (!stack.isOf(Items.FLINT_AND_STEEL) && !stack.isOf(Items.FIRE_CHARGE) || state.get(LIT))
            return super.onUse(state, world, pos, player, hand, hit);

        this.onIgnited(world, pos);
        Item item = stack.getItem();
        if (!player.isCreative()) {
            if (stack.isOf(Items.FLINT_AND_STEEL)) {
                stack.damage(1, player, playerx -> playerx.sendToolBreakStatus(hand));
            } else {
                stack.decrement(1);
            }
        }

        player.incrementStat(Stats.USED.getOrCreateStat(item));
        return ActionResult.success(world.isClient);
    }

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (!state.get(LIT)) return;

		if (random.nextInt(24) == 0) world.playSound(
			(double)pos.getX() + 0.5,
			(double)pos.getY() + 0.5,
			(double)pos.getZ() + 0.5,
			SoundEvents.BLOCK_FIRE_AMBIENT,
			SoundCategory.BLOCKS,
			1.0F + random.nextFloat(),
			random.nextFloat() * 0.7F + 0.3F,
			false
		);

		for (int i = 0; i < 3; ++i) world.addParticle(
			ParticleTypes.LARGE_SMOKE,
			(double)pos.getX() + random.nextDouble(),
			(double)pos.getY() + random.nextDouble() * 0.5 + 0.5,
			(double)pos.getZ() + random.nextDouble(),
			0.0, 0.0, 0.0
		);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		world.scheduleBlockTick(pos, this, FireBlockInvoker.invokeGetFireTickDelay(world.random));
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.scheduleBlockTick(pos, this, FireBlockInvoker.invokeGetFireTickDelay(world.random));
		if (!state.get(LIT)) return;
		if (!world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) return;

		int spreadChance = world.getBiome(pos).isIn(BiomeTags.INCREASED_FIRE_BURNOUT) ? -50 : 0;
		List<BlockPos> neighbours = getNeighbours(pos);
		for (BlockPos nPos : neighbours) {
			this.trySpreadingFire(world, nPos, 300 + spreadChance, random);
		}
	}

	private void trySpreadingFire(World world, BlockPos pos, int spreadFactor, Random random) {
		int spreadChance = ((FireBlockInvoker)Blocks.FIRE).invokeGetSpreadChance(world.getBlockState(pos));
		if (random.nextInt(spreadFactor) >= spreadChance) return;

		if (random.nextInt(10) < 5 && !world.hasRain(pos)) {
			BlockState state = world.getBlockState(pos);
			if (state.getBlock() instanceof Ignitable block) {
				if (!block.onIgnited(world, pos)) return;
			}
			world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 3);
		}
	}

	@Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);
        if (!moved) return;

        List<BlockPos> neighbours = getNeighbours(pos);
        for (BlockPos nPos : neighbours) {
            BlockState nState = world.getBlockState(nPos);
            if (nState.getBlock() instanceof Oxidizable) scrubState(world, nState, nPos);
        }
    }

	public List<BlockPos> getNeighbours(BlockPos pos) {
		return List.of(
			pos.up(),
			pos.down(),
			pos.north(),
			pos.south(),
			pos.east(),
			pos.west()
		);
	}

    public void scrubState(World world, BlockState state, BlockPos pos) {
        Optional<BlockState> scrubbedState = Oxidizable.getDecreasedOxidationState(state);
        if (scrubbedState.isEmpty()) return;

        world.setBlockState(pos, scrubbedState.get());
        world.playSound(null, pos, ModSoundEvents.BLOCK_STEEL_WOOL_SCRUB, SoundCategory.BLOCKS, 1.0F, 1.0F);
		// i hate these magic bullshit methods
		world.syncWorldEvent(null, 3004, pos, 0);
	}

	// TODO make this actually work i give up for now
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!this.isMoving(entity)) return;
		RegistryEntry<DamageType> type = world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(ModDamageTypes.STEEL_WOOL);
		entity.damage(new DamageSource(type), 1.0F);
    }

	public boolean isMoving(Entity entity) {
		return entity.prevX != entity.getX()
			&& entity.prevY != entity.getY()
			&& entity.prevZ != entity.getZ();
	}

}
