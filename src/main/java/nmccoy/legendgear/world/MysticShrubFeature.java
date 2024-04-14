package nmccoy.legendgear.world;

import java.util.HashSet;
import java.util.Iterator;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import nmccoy.legendgear.LegendGear;

public class MysticShrubFeature extends Feature<MysticShrubFeatureConfig> {
	
	private static final BlockState MYSTIC_SHRUB_BLOCK = LegendGear.MYSTIC_SHRUB_BLOCK.withAge(1);
	
	public MysticShrubFeature(Codec<MysticShrubFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(FeatureContext<MysticShrubFeatureConfig> context) {
		StructureWorldAccess world = context.getWorld();
		Random random = context.getRandom();
		
		if(random.nextFloat() < context.getConfig().starChance()) {
			return placeShrubStar(world, context);
		} else {
			return placeShrubPatch(world, context);
		}
	}
	
	private boolean placeShrubStar(StructureWorldAccess world, FeatureContext<MysticShrubFeatureConfig> context) {
		HashSet<BlockPos> placementPositions = new HashSet<>();
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		BlockPos origin = context.getOrigin();
		
		placementPositions.add(mutable.set(origin, -1, 0, 0).toImmutable());
		placementPositions.add(mutable.set(origin, 0, 0, -1).toImmutable());
		placementPositions.add(mutable.set(origin, 1, 0, 0).toImmutable());
		placementPositions.add(mutable.set(origin, 0, 0, 1).toImmutable());
		
		placementPositions.add(mutable.set(origin, -2, 0, -2).toImmutable());
		placementPositions.add(mutable.set(origin, 2, 0, -2).toImmutable());
		placementPositions.add(mutable.set(origin, -2, 0, 2).toImmutable());
		placementPositions.add(mutable.set(origin, 2, 0, 2).toImmutable());
		
		placementPositions.add(mutable.set(origin, -3, 0, 0).toImmutable());
		placementPositions.add(mutable.set(origin, 0, 0, -3).toImmutable());
		placementPositions.add(mutable.set(origin, 3, 0, 0).toImmutable());
		placementPositions.add(mutable.set(origin, 0, 0, 3).toImmutable());
		
		Iterator<BlockPos> posIter = placementPositions.iterator();
		while(posIter.hasNext()) {
			BlockPos pos = posIter.next();
			if(!placeShrub(world, pos, context)) posIter.remove();
		}
		
		return !placementPositions.isEmpty();
	}
	
	private boolean placeShrubPatch(StructureWorldAccess world, FeatureContext<MysticShrubFeatureConfig> context) {
		HashSet<BlockPos> placementPositions = new HashSet<>();
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		BlockPos origin = context.getOrigin();
		
		placementPositions.add(mutable.set(origin, 0, 0, 1).toImmutable());
		placementPositions.add(mutable.set(origin, 1, 0, 1).toImmutable());

		for(int i = -1; i < 3; i++) {
			placementPositions.add(mutable.set(origin, i, 0, 0).toImmutable());
		}
		for(int i = -1; i < 3; i++) {
			placementPositions.add(mutable.set(origin, i, 0, -1).toImmutable());
		}
		
		placementPositions.add(mutable.set(origin, 0, 0, -2).toImmutable());
		placementPositions.add(mutable.set(origin, 1, 0, -2).toImmutable());
		
		Iterator<BlockPos> posIter = placementPositions.iterator();
		while(posIter.hasNext()) {
			BlockPos pos = posIter.next();
			if(!placeShrub(world, pos, context)) posIter.remove();
		}
		
		return !placementPositions.isEmpty();
	}
	
	private boolean placeShrub(StructureWorldAccess world, BlockPos pos, FeatureContext<MysticShrubFeatureConfig> context) {
		BlockState ground = world.getBlockState(pos.down());
		if(!ground.isIn(BlockTags.DIRT) && !ground.isOf(Blocks.FARMLAND)) {
			return false;
		}
		
		BlockState before = world.getBlockState(pos);
		
		if(!before.isAir() && !before.isIn(BlockTags.REPLACEABLE_BY_TREES)) {
			return false;
		}
		
		return world.setBlockState(pos, MYSTIC_SHRUB_BLOCK, 2);
	}

}
