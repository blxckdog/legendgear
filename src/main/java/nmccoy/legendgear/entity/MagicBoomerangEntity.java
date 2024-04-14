package nmccoy.legendgear.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import nmccoy.legendgear.LegendGear;
import nmccoy.legendgear.block.MysticShrub;

public class MagicBoomerangEntity extends ThrownItemEntity {

	public static final TagKey<Block> BOOMERANG_DESTROYABLE_BLOCKS = TagKey.of(RegistryKeys.BLOCK, LegendGear.id("boomerang_destroyables"));
	
	public static final float BOOMERANG_SPEED = 1.5f;
	public static final float CORNERING_SPEED = 0.2f;
	public static final int MAX_RETURN_TIME = 20 * 10;
	
	public MagicBoomerangEntity(EntityType<MagicBoomerangEntity> type, World world) {
		super(type, world);
	}
	
	@Override
	public void tick() {
		super.tick();
		double motionX = getVelocity().getX();
		double motionY = getVelocity().getY();
		double motionZ = getVelocity().getZ();
		
		if(age % 3 == 0) {
			getWorld().playSound(this, getBlockPos(), LegendGear.SOUND_BOOMERANG, SoundCategory.HOSTILE, 0.8f, 1f);
		}
		
		if(getWorld().isClient) {
			// Spawn particle tail
			for(float i = 0; i < 4; ++i) {
				double x = getX() + motionX * i/4d;
				double y = getY() + motionY * i/4d;
				double z = getZ() + motionZ * i/4d;
				
				getWorld().addParticle(ParticleTypes.CRIT, x, y, z, -motionX, -motionY + .2d, -motionZ);
			}
		}
		
		if(!getWorld().isClient && !hasPassengers()) {
			// Pick up nearby items on ground
			Box roi = Box.of(getPos(), 1, 1, 1);
			getWorld().getEntitiesByClass(ItemEntity.class, roi, (always) -> true)
					.stream()
					.findFirst()
					.ifPresent((entity) -> {
						entity.startRiding(this);
					});
		}
		
		if(age >= 10) {
			Entity owner = getOwner();
			
			if(owner != null && owner.isAlive()) {
				// Move back to thrower
				double currentHeading = Math.atan2(motionZ, motionX);
				double headingToThrower = Math.atan2(owner.getZ() - getZ(), owner.getX() - getX());
				double curveScale = (age-10) * 0.007f;
				
				double newHeading = updateRotationRadians(currentHeading, headingToThrower, curveScale);
				
				double currentPitch = Math.atan2(motionY, Math.sqrt(motionX*motionX + motionZ*motionZ));
				double horizontalDistance = Math.sqrt((owner.getX() - getX())*(owner.getX() - getX())+(owner.getZ() - getZ())*(owner.getZ() - getZ()));
				double targetPitch = Math.atan2(owner.getEyeY() - getY(), horizontalDistance);
				
				double newPitch = updateRotationRadians(currentPitch, targetPitch, curveScale * 0.3f);
				
				motionX = Math.cos(newHeading) * Math.cos(newPitch);
				motionY = Math.sin(newPitch);
				motionZ = Math.sin(newHeading) * Math.cos(newPitch);
			}
		}
		
		setVelocity(motionX, motionY, motionZ, BOOMERANG_SPEED, 0f);
		
		if(age >= MAX_RETURN_TIME) {
			if(getStack() != null && !getStack().isEmpty()) {
				getWorld().spawnEntity(new ItemEntity(getWorld(), getX(), getY(), getZ(), getStack()));
			}
			remove(RemovalReason.DISCARDED);
		}
	}
	
	private double updateRotationRadians(double current, double intended, double maxDelta) {
		double delta = intended - current;
		delta %= Math.PI*2;
		
		if (delta >= Math.PI) delta -=  Math.PI*2;
		if (delta < -Math.PI) delta += Math.PI*2;

		if (delta > maxDelta) delta = maxDelta;
		if (delta < -maxDelta) delta = -maxDelta;

		return current + delta;
	}
	
	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		Entity hitEntity = entityHitResult.getEntity();
		
		if(getWorld().isClient) {
			return;
		}
		
		if(getOwner() != null && hitEntity.getUuid() == getOwner().getUuid()) {
			// Boomerang caught by thrower
			if(hitEntity instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) hitEntity;
				
				if(getStack() != null && !getStack().isEmpty()) {
					boolean success = player.giveItemStack(getStack());
					
					if(!success) {
						getWorld().spawnEntity(new ItemEntity(getWorld(), getX(), getY(), getZ(), getStack()));
					}
				}
			} else {
				getWorld().spawnEntity(new ItemEntity(getWorld(), getX(), getY(), getZ(), getStack()));
			}
			
			remove(RemovalReason.DISCARDED);
		} else {
			// Other entity was hit
			if(getOwner() != null && getOwner().isAlive() && getOwner() instanceof LivingEntity && hitEntity instanceof LivingEntity) {
				LivingEntity owner = (LivingEntity) getOwner();
				LivingEntity attacked = (LivingEntity) hitEntity;
				
				if(getStack() != null) {
					getStack().damage(1, owner, unused -> {});
				}
				
				if(getStack() == null || getStack().isEmpty()) {
					getWorld().playSound(this, getBlockPos(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.NEUTRAL, 1f, 0.9f);
					remove(RemovalReason.DISCARDED);
				}
				
				float baseDamage = 6;
				//baseDamage = (float) owner.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
				
				float attackDamage = baseDamage + EnchantmentHelper.getAttackDamage(getStack(), attacked.getGroup());
				
				// Deal damage to hit entity
				attacked.damage(createDamageSource(owner), attackDamage);
			}
		}
	}
	
	private DamageSource createDamageSource(LivingEntity attacker) {
		return new DamageSource(
				getWorld().getRegistryManager()
						.get(RegistryKeys.DAMAGE_TYPE)
						.entryOf(DamageTypes.THROWN), 
				attacker
			);
	}
	
	@Override
	protected void onBlockCollision(BlockState state) {
		// Break grass and foliage blocks with no hardness
		if(state.isIn(BOOMERANG_DESTROYABLE_BLOCKS)) {
			// Crop mystic shrub and break all other blocks
			if(state.isOf(LegendGear.MYSTIC_SHRUB_BLOCK)) {
				MysticShrub shrub = (MysticShrub) state.getBlock();
				
				if(shrub.getAge(state) >= 1) {
					shrub.handleBreak(state, getWorld(), getBlockPos());
					getWorld().playSound(null, getBlockPos(), SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.NEUTRAL);
				}
			} else {
				getWorld().breakBlock(getBlockPos(), true);
			}
			
			if(getOwner() != null && getOwner().isAlive() && getOwner() instanceof LivingEntity) {
				LivingEntity owner = (LivingEntity) getOwner();
				if(getStack() != null) {
					getStack().damage(1, owner, unused -> {});
				}
				
				if(getStack() == null || getStack().isEmpty()) {
					getWorld().playSound(this, getBlockPos(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.NEUTRAL, 1f, 0.9f);
					remove(RemovalReason.DISCARDED);
				}
			}
		} 
	}
	
	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		
		BlockPos pos = blockHitResult.getBlockPos();
		BlockState state = getWorld().getBlockState(pos);
		
		if(!state.isAir() && state.isSolid()) {
			if(!noClip) {
				// First solid block bounces back boomerang
				setVelocity(getVelocity().multiply(-1));
				noClip = true;
			}
		}
	}
	
	public void setStack(ItemStack boomerangStack) {
		setItem(boomerangStack);
	}

	@Override
	protected Item getDefaultItem() {
		return LegendGear.MAGIC_BOOMERANG;
	}
	
	@Override
	public boolean hasNoGravity() {
		return true;
	}

}
