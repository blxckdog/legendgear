package nmccoy.legendgear.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import nmccoy.legendgear.LegendGear;

public class SwordPedestal extends Block implements BlockEntityProvider {

	private static final VoxelShape SHAPE_NS = Block.createCuboidShape(4, 0, 0, 12, 8, 16);
	private static final VoxelShape SHAPE_EW = Block.createCuboidShape(0, 0, 4, 16, 8, 12);
	
	
	public SwordPedestal(Settings settings) {
		super(settings);
		setDefaultState(stateManager.getDefaultState()
				.with(Properties.HORIZONTAL_FACING, Direction.NORTH));
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		
		if(!world.isClient && hand == Hand.MAIN_HAND && blockEntity instanceof SwordPedestalBlockEntity) {
			SwordPedestalBlockEntity pedestal = ((SwordPedestalBlockEntity) blockEntity);
			ItemStack heldItem = player.getMainHandStack();
			
			if(canBeStored(heldItem)) {
				ItemStack storedBefore = ItemStack.EMPTY;
				
				if(pedestal.getSword() != null) {
					 storedBefore = pedestal.getSword().copy();
				}
				
				pedestal.setSword(heldItem.copy());
				player.setStackInHand(Hand.MAIN_HAND, storedBefore);
				blockEntity.markDirty();
				
				//world.playSound(pos.getX(), pos.getY(), pos.getZ(), LegendGear.SOUND_SWORD_PLACE, SoundCategory.NEUTRAL, 0.8f, 1f, false);
				world.playSound(null, pos, LegendGear.SOUND_SWORD_PLACE, SoundCategory.BLOCKS);
				world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
				return ActionResult.SUCCESS;
			} else if(heldItem == null || heldItem.isEmpty()) {
				// Take item when hand is empty
				ItemStack storedBefore = ItemStack.EMPTY;
				
				if(pedestal.getSword() != null) {
					 storedBefore = pedestal.getSword().copy();
				}
				
				if(!storedBefore.isEmpty()) {
					player.setStackInHand(Hand.MAIN_HAND, storedBefore);
					pedestal.setSword(ItemStack.EMPTY);
					blockEntity.markDirty();
					
					//world.playSound(pos.getX(), pos.getY(), pos.getZ(), LegendGear.SOUND_SWORD_TAKE, SoundCategory.NEUTRAL, 0.8f, 1f, false);
					world.playSound(null, pos, LegendGear.SOUND_SWORD_TAKE, SoundCategory.BLOCKS);
					world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
					return ActionResult.SUCCESS;
				}
			}
		}
		
		return ActionResult.PASS;
	}
	
	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if(!world.isClient) dropSword(world, pos);
		return super.onBreak(world, pos, state, player);
	}
	
	@Override
	public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
		if(!world.isClient) dropSword(world, pos);
		super.onDestroyedByExplosion(world, pos, explosion);
	}
	
	private void dropSword(World world, BlockPos pos) {
		if(world.getBlockEntity(pos) instanceof SwordPedestalBlockEntity pedestal) {
			if(pedestal.getSword() == null || pedestal.getSword().isEmpty()) return;
			world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), pedestal.getSword().copy()));
		}
	}
	
	private boolean canBeStored(ItemStack item) {
		if(item == null || item.isEmpty()) {
			return false;
		}
		
		return item.getItem() instanceof SwordItem;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		Direction facing = state.get(Properties.HORIZONTAL_FACING);
		
		switch(facing) {
			case NORTH:
			case SOUTH:
				return SHAPE_NS;
				
			case EAST:
			case WEST:
				return SHAPE_EW;
				
			default:
				return VoxelShapes.fullCube();
		}
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().rotateClockwise(Axis.Y));
	}
	
	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SwordPedestalBlockEntity(pos, state);
	}
	
}
