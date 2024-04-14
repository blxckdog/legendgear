package nmccoy.legendgear.item;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import nmccoy.legendgear.LegendGear;
import nmccoy.legendgear.mixin.EntityAccessor;

public class MagicMirrorItem extends Item {

	public MagicMirrorItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		
		if(world.isSkyVisible(user.getBlockPos())) {
			// Not usable under skylight
			world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS);
			return super.use(world, user, hand);
		} else {			
			user.setCurrentHand(hand);
			return TypedActionResult.consume(stack);
		}
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		NbtCompound nbt = stack.getOrCreateNbt();
		double lastSkyX = nbt.getDouble("lastSkyX");
		double lastSkyY = nbt.getDouble("lastSkyY");
		double lastSkyZ = nbt.getDouble("lastSkyZ");
		
		if(!world.isClient) {
			// Play particles on old position
			world.addParticle(ParticleTypes.EXPLOSION, true, user.getX(), user.getY(), user.getZ(), 0, 0, 0);
			
			user.teleport(lastSkyX, lastSkyY, lastSkyZ);
			user.clearActiveItem();
			// nbt.putLong("lastWarp", world.getTime());
		}
		
		// Play particles on new position
		world.addParticle(ParticleTypes.EXPLOSION, true, lastSkyX, lastSkyY, lastSkyZ, 0, 0, 0);
		world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 0.7f, 1f);
		return stack;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		
		if(!(entity instanceof PlayerEntity player)) {
			return;
		}

		if(!world.isClient && entity.age % 20 == 0) {
			if(world.isSkyVisible(player.getBlockPos())) {
				// Update last location under skylight
				NbtCompound nbt = stack.getOrCreateNbt();
				nbt.putDouble("lastSkyX", player.getX());
				nbt.putDouble("lastSkyY", player.getY());
				nbt.putDouble("lastSkyZ", player.getZ());
			}
		}
	}
	
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		int duration = getMaxUseTime(stack) - remainingUseTicks;
		float progress = duration * 1f / getMaxUseTime(stack);
		
		if(world.isClient) {
			if(user instanceof ClientPlayerEntity player) {
				((EntityAccessor) player).setInNetherPortal(true);
				player.setPortalCooldown(2);
			}
			
			double theta = Math.PI * 6 * progress;
			double radius = 2 * (1 - progress);
			
			for(int i=0; i<3; i++) {
				double x = user.getX() + Math.cos(theta) * radius;
				double y = user.getY() + (2.7 - 2.7*progress);
				double z = user.getZ() + Math.sin(theta) * radius;
				
				world.addParticle(LegendGear.MAGIC_RUNE, true, x, y, z, 0, 0.1, 0);
				theta += Math.PI * 2/3;
			}
		}
		
		if(duration % 8 == 0) {
			float pitch =  0.2f * (2 + duration/8);
			world.playSound(user, user.getBlockPos(), LegendGear.SOUND_SINE, SoundCategory.PLAYERS, 0.25f, pitch);
		}
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BLOCK;
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 70;
	}

}
