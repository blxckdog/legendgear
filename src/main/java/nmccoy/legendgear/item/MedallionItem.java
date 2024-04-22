package nmccoy.legendgear.item;

import java.util.function.Function;

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

import nmccoy.legendgear.entity.medallion.ThrownMedallionEntity;

public class MedallionItem extends Item {

	public static final int MAX_DAMAGE = 51;
	public static final int THROW_TIME = 15;
	
	private final Function<World, ThrownMedallionEntity> thrownMedallionSupplier;
	
	
	public MedallionItem(Settings settings, Function<World, ThrownMedallionEntity> thrownMedallionSupplier) {
		super(settings.maxDamage(MAX_DAMAGE));
		this.thrownMedallionSupplier = thrownMedallionSupplier;
	}
	
	
	public void charge(ItemStack stack, int amount) {
		stack.setDamage(Math.max(stack.getDamage() - amount, 0));
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return stack.getDamage() == 0;
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		
		if(stack.getDamage() == 0) {
			user.setCurrentHand(hand);
			return TypedActionResult.consume(stack);
		}
		
		return TypedActionResult.fail(stack);
	}
	
	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if(72000 - remainingUseTicks < THROW_TIME) {
			return;
		}
		
		world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.HOSTILE, 0.5f, 0.7f);
		
		if(!world.isClient) {
			ThrownMedallionEntity thrownMedallion = thrownMedallionSupplier.apply(world);
			
			thrownMedallion.setOwner(user);
			thrownMedallion.setItem(stack);
			thrownMedallion.setPosition(user.getEyePos());
			thrownMedallion.setVelocity(user, user.getPitch(), user.getYaw(), 0f, 1.2f, 0.8f);
			
			world.spawnEntity(thrownMedallion);
		}
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}
	
	@Override
	public boolean isDamageable() {
		return true;
	}
	
	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return false;
	}
	
}
