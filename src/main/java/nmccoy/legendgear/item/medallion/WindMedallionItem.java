package nmccoy.legendgear.item.medallion;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import nmccoy.legendgear.LegendGear;
import nmccoy.legendgear.entity.medallion.ThrownMedallionEntity;
import net.minecraft.item.Item.Settings;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WindMedallionItem extends AbstractMedallionItem {

	public WindMedallionItem(Settings settings) {
		super(settings);
	}

	@Override
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
		
		world.playSound(null, blockPos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.HOSTILE, 2f, 0.7f);
	}

}
