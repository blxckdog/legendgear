package nmccoy.legendgear.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import nmccoy.legendgear.LegendGear;
import nmccoy.legendgear.entity.HookshotBarbEntity;

public class HookshotItem extends Item {

	public HookshotItem(Settings settings) {
		super(settings.maxDamageIfAbsent(250));
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		
		if(!world.isClient) {
			HookshotBarbEntity barb = new HookshotBarbEntity(LegendGear.HOOKSHOT_BARB_ENTITY, world);
			barb.setOwner(user);
			barb.setPosition(user.getEyePos());
			barb.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 0f);
			world.spawnEntity(barb);
		}
		
		world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL);
		
		user.setCurrentHand(hand);
		stack.damage(1, user, unused -> {});
		return TypedActionResult.success(stack, true);
	}
	
	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL);
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BLOCK;
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 20 * 120;
	}

}
