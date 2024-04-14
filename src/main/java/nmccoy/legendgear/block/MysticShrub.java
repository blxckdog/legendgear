package nmccoy.legendgear.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import nmccoy.legendgear.LegendGear;

public class MysticShrub extends CropBlock {

	private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[] {
		Block.createCuboidShape(.5, 0, .5, 15.5, 2, 15.5),
		Block.createCuboidShape(.5, 0, .5, 15.5, 14, 15.5)
	};
	
	
	public MysticShrub(Settings settings) {
		super(settings);
	}
	
	
	@Override
	protected ItemConvertible getSeedsItem() {
		return LegendGear.MYSTIC_SHRUB_ITEM;
	}
	
	@Override
	public int getMaxAge() {
		return 1;
	}
	
	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return floor.isIn(BlockTags.DIRT) || floor.isOf(Blocks.FARMLAND);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return AGE_TO_SHAPE[(Integer)state.get(this.getAgeProperty())];
	}
	
	public boolean handleBreak(BlockState state, World world, BlockPos pos) {
		if(!state.isOf(this)) {
			return true;
		}
		
		if(getAge(state) == 0) {
			return true;
		}
		
		world.setBlockState(pos, withAge(0));
		if(world.isClient) {
			return false;
		}
		
		// TODO: Change this to a loot table
		if(world.getRandom().nextFloat() < 0.2) {
			ItemEntity drop = new ItemEntity(world, pos.getX()+0.5f, pos.getY(), pos.getZ()+0.5f, new ItemStack(LegendGear.EMERALD_SHARD, 1));
			world.spawnEntity(drop);
		}
		if(world.getRandom().nextFloat() < 0.2) {
			ItemEntity drop = new ItemEntity(world, pos.getX()+0.5f, pos.getY(), pos.getZ()+0.5f, new ItemStack(LegendGear.HEART, 1));
			world.spawnEntity(drop);
		}
		if(world.getRandom().nextFloat() < 0.2) {
			ItemEntity drop = new ItemEntity(world, pos.getX()+0.5f, pos.getY(), pos.getZ()+0.5f, new ItemStack(Items.ARROW, 1));
			world.spawnEntity(drop);
		}
		
		return false;
	}

}
