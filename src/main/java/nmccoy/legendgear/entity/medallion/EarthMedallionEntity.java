package nmccoy.legendgear.entity.medallion;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import nmccoy.legendgear.LegendGear;

public class EarthMedallionEntity extends ThrownMedallionEntity {
	
	public static ThrownMedallionEntity create(World world) {
		return new EarthMedallionEntity(LegendGear.THROWN_EARTH_MEDALLION_ENTITY, world);
	}
	
	public EarthMedallionEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void activeTick() {
		int activeDelta = age - activeAge;
		
		if(activeDelta == 1) {
			// Just activated
			if(getWorld().isClient) {
				for(int i=0; i<15; i++) {
					getWorld().addParticle(LegendGear.MAGIC_RUNE, getX() + random.nextGaussian()*0.3, getY(), getZ()+random.nextGaussian()*0.3, 0f, random.nextDouble(), 0f);
				}
				return;
			}
			
			getWorld().playSound(null, getBlockPos(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.HOSTILE, 2f, 0.3f);
			getWorld().playSound(null, getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.HOSTILE, 2f, 0.7f);
		}
		
		if(activeDelta > 60) {
			discard();
		}
	}
	
	private void doPulse(double radius) {
		if(getWorld().isClient) {
			for(int i=0; i<12; i++) {
				double theta = i * Math.PI * 2 / 12;
				double x = Math.cos(theta);
				double z = Math.sin(theta);
				
				getWorld().addParticle(ParticleTypes.EXPLOSION, getX()+x*radius, getY(), getZ()+z*radius, x*0.05f, 0.2f, z*0.05f);
			}
			return;
		}
		
		// Find nearby entities (exclude thrower) and damage those
		getWorld().getOtherEntities(this, getBoundingBox().expand(radius)).stream()
				.filter(entity -> entity instanceof LivingEntity)
				.filter(entity -> !entity.equals(getOwner()))
				.forEach(entity -> {
					
				});
	}
	
	@Override
	protected Item getDefaultItem() {
		return LegendGear.EARTH_MEDALLION;
	}

}
