package nmccoy.legendgear.item;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import nmccoy.legendgear.LegendGear;
import nmccoy.legendgear.event.LivingEntityEvents;

public class TitanBandItem extends Item {

	private static final Random RANDOM = new Random();
	
	public static void registerEvents() {
		// Stop lifting entities if carrier or carried entity was damaged
		LivingEntityEvents.TAKE_DAMAGE.register((LivingEntity entity, DamageSource source, float amount) -> {
			if(entity.hasVehicle()) {
				// Check if damaged entity is carried by a Titan Band user
				if(entity.getVehicle() instanceof PlayerEntity holder) {
					ItemStack stack = holder.getMainHandStack();
					
					if(stack.isOf(LegendGear.TITAN_BAND)) {
						handleThrow(holder, true);
					}
				}
			} else if(entity.hasPassengers()) {
				// Check if damaged entity uses Titan Band
				if(entity instanceof PlayerEntity holder) {
					ItemStack stack = holder.getMainHandStack();
					
					if(stack.isOf(LegendGear.TITAN_BAND)) {
						handleThrow(holder, true);
					}
				}
			}
			
			return ActionResult.PASS;
		});
	}
	
	public static void handleThrow(LivingEntity holder, boolean justDrop) {
		Entity toThrow = holder.getFirstPassenger();
		if(toThrow != null) toThrow.stopRiding();
		
		if(!justDrop) {
			// Throw lifted entity away
			if(!holder.getWorld().isClient && toThrow != null) {
				float mod = 0.8f;
				double motionX = -Math.sin(holder.getYaw() / 180f * Math.PI) * Math.cos(holder.getPitch() / 180f * Math.PI) * mod;
				double motionZ = Math.cos(holder.getYaw() / 180f * Math.PI) * Math.cos(holder.getPitch() / 180f * Math.PI) * mod;
				double motionY = -Math.sin(holder.getPitch() / 180f * Math.PI) * mod + 0.2;
				
				toThrow.setVelocity(motionX, motionY, motionZ);
				toThrow.setYaw(holder.getYaw());
			}
		}

		holder.getWorld().playSound(null, holder.getBlockPos(), LegendGear.SOUND_TITAN_THROW, SoundCategory.HOSTILE, 0.5f, 1f);
		setLifting(holder, false);
	}
	
	private static boolean isLifting(LivingEntity entity) {
		return ((TitanLiftHolder) entity).isLifting();
	}
	
	private static void setLifting(LivingEntity entity, boolean isLifting) {
		((TitanLiftHolder) entity).setLifting(isLifting);
	}
	
	
	public TitanBandItem(Settings settings) {
		super(settings.maxDamageIfAbsent(64));
	}
	
	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		ItemStack activeStack = user.getStackInHand(hand);
		
		if(!activeStack.isOf(LegendGear.TITAN_BAND)) {
			return ActionResult.PASS;
		}
		
		if(!user.hasPassengers()) {
			if(!user.getWorld().isClient) {
				user.getWorld().playSound(null, user.getBlockPos(), LegendGear.SOUND_TITAN_LIFT, SoundCategory.HOSTILE, 0.5f, 1f);
			}
			setLifting(user, true);
			entity.startRiding(user, true);
			user.setCurrentHand(hand);
			return ActionResult.SUCCESS;
		}
		
		return ActionResult.PASS;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		
		if(!stack.isOf(LegendGear.TITAN_BAND)) {
			return super.use(world, user, hand);
		}
		
		if(user.hasPassengers() && isLifting(user)) {
			handleThrow(user, false); // Throw passenger away with motion
			return TypedActionResult.success(stack, true);
		}
		
		return super.use(world, user, hand);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if(!(entity instanceof PlayerEntity player)) {
			return;
		}
		
		// Do nothing when not lifting
		if(!isLifting(player)) {
			return;
		}
		
		ItemStack mainStack = player.getMainHandStack();
		
		if(mainStack != null && mainStack.isOf(LegendGear.TITAN_BAND)) {
			world.addParticle(LegendGear.MAGIC_RUNE, true, player.getX(), player.getBodyY(1)+1, player.getZ(),
					RANDOM.nextGaussian()*0.2,  RANDOM.nextGaussian()*0.2, RANDOM.nextGaussian()*0.2);
		} 
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}
	
	
	public static interface TitanLiftHolder {
		public boolean isLifting();
		public void setLifting(boolean isLifting);
	}

}
