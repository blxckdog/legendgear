package nmccoy.legendgear.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class SugarCube extends Block {

	public SugarCube(Settings settings) {
		super(settings.ticksRandomly());
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if(world.hasRain(pos.up())) {
			// world.setBlockState(pos, Blocks.AIR.getDefaultState());
			world.breakBlock(pos, false);
		}
	}

}
