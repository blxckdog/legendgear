package nmccoy.legendgear.item.medallion;

import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import nmccoy.legendgear.LegendGear;

public class FireMedallionItem extends AbstractMedallionItem {

	public FireMedallionItem(Settings settings) {
		super(settings, null);
	}

	public void onImpact(World world, Vec3d pos) {
		if(world.isClient) {
			world.addParticle(LegendGear.MAGIC_RUNE, pos.getX(), pos.getY(), pos.getZ(), 0.5f, 1f, 0.5f);
			world.addParticle(LegendGear.MAGIC_RUNE, pos.getX(), pos.getY(), pos.getZ(), 0.5f, 1f, -0.5f);
			world.addParticle(LegendGear.MAGIC_RUNE, pos.getX(), pos.getY(), pos.getZ(), -0.5f, 1f, 0.5f);
			world.addParticle(LegendGear.MAGIC_RUNE, pos.getX(), pos.getY(), pos.getZ(), -0.5f, 1f, -0.5f);
			return;
		}
			
			
		//world.spawnEntity(null);
		BlockPos blockPos = new BlockPos((int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
		
		//world.playSound(null, blockPos, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.HOSTILE, 2f, 0.3f);
		world.playSound(null, blockPos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.HOSTILE, 2f, 0.7f);
	}

}
