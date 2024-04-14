package nmccoy.legendgear.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import nmccoy.legendgear.LegendGear;
import nmccoy.legendgear.entity.MagicBoomerangEntity;

public class MagicBoomerangItem extends Item {
	
	public MagicBoomerangItem(Settings settings) {
		super(settings.maxDamageIfAbsent(750));
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		
		if (!world.isClient) {			
			MagicBoomerangEntity thrownBoomerang = new MagicBoomerangEntity(LegendGear.MAGIC_BOOMERANG_ENTITY, world);
			thrownBoomerang.setOwner(user);
			thrownBoomerang.setStack(itemStack.copy());
			
			thrownBoomerang.setPos(user.getX(), user.getEyeY(), user.getZ());
			thrownBoomerang.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
			world.spawnEntity(thrownBoomerang);
		}

		user.incrementStat(Stats.USED.getOrCreateStat(this));
		if (!user.getAbilities().creativeMode) {
			itemStack.decrement(1);
		}

		return TypedActionResult.success(itemStack, world.isClient());
	}
	
	@Override
	public int getEnchantability() {
		return 1;
	}
	
	@Override
	public boolean isDamageable() {
		return true;
	}
	
	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return super.canRepair(stack, ingredient);
	}

}
