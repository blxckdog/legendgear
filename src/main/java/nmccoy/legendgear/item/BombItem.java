package nmccoy.legendgear.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import nmccoy.legendgear.LegendGear;

public class BombItem extends Item {

	public BombItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		
		world.playSound(null, user.getBlockPos(), LegendGear.SOUND_BOOMERANG, SoundCategory.HOSTILE);
		
		if (!world.isClient) {
			SnowballEntity snowballEntity = new SnowballEntity(world, user);
			
			snowballEntity.setItem(stack);
			snowballEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0f, 0.5f, 1f);
			
			world.spawnEntity(snowballEntity);
		}

		user.incrementStat(Stats.USED.getOrCreateStat(this));
		
		if (!user.getAbilities().creativeMode) {
			stack.decrement(1);
		}

		return TypedActionResult.success(stack, world.isClient());
	}

}
