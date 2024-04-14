package nmccoy.legendgear.entity;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.World.ExplosionSourceType;
import nmccoy.legendgear.LegendGear;

public class ShootingStarEntity extends Entity {

	public static final int MAX_DWINDLE_TIME = 20*22;

	private static final Random RANDOM = new Random();
	private static final TrackedData<Boolean> IMPACT = DataTracker.registerData(ShootingStarEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	//private static final TrackedData<Integer> DWINDLE_COUNTER = DataTracker.registerData(ShootingStarEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	public static ShootingStarEntity spawnNearbyPlayer(PlayerEntity player) {
		World world = player.getWorld();
		ShootingStarEntity star = new ShootingStarEntity(LegendGear.SHOOTING_STAR_ENTITY, world);

		double radius = 48;
		double theta = RANDOM.nextDouble() * Math.PI * 2;
		double posX = player.getX() + Math.cos(theta) * radius;
		double posY = player.getY() + 150;
		double posZ = player.getZ() + Math.sin(theta) * radius;
		
		star.setPosition(posX, posY, posZ);
		star.setVelocity(0, 0, 0);
		
		world.spawnEntity(star);
		return star;
	}

	private int dwindleCounter = MAX_DWINDLE_TIME;
	private float fallVelocity = -0.5f;
	
	public ShootingStarEntity(EntityType<ShootingStarEntity> entityType, World world) {
		super(entityType, world);
		noClip = true;
	}
	
	@Override
	public void tick() {
		super.tick();
		checkBlockCollision();
		
		if(!hasHit()) {
			if(fallVelocity > -2) fallVelocity -= 0.05f;
			setVelocity(0, fallVelocity, 0);
			move(MovementType.SELF, getVelocity());
		}
		
		if(age == 2 && getWorld().isClient && !hasHit()) {
			getWorld().playSoundFromEntity(this, LegendGear.SOUND_STARFALL, SoundCategory.NEUTRAL, 100f, 1f);
		}
		
		if(getWorld().isClient) {
			
			getWorld().addParticle(LegendGear.MAGIC_RUNE, true, 
						getX()-getVelocity().getX(), getY()-getVelocity().getY(), getZ()-getVelocity().getZ(), 
						RANDOM.nextGaussian() * 0.2, RANDOM.nextGaussian() * 0.2, RANDOM.nextGaussian() * 0.2
					);
			
			if(hasHit() && getDwindleTime() % 25 == 0 && getDwindleTime() > 30) {
				getWorld().playSoundFromEntity(this, LegendGear.SOUND_STAR_TWINKLE, SoundCategory.NEUTRAL, 2f, 1f);
			}
		} 
		
		if(age > 20*60) {
			discard();
			return;
		}
		
		if(hasHit() && --dwindleCounter <= 0) {
			
			discard();
		}
	}
	
	@Override
	protected void onBlockCollision(BlockState state) {
		if(state.isAir()) {
			return;
		}

		System.out.println("COLLISION NOT AIR");
		if(!hasHit()) {
			System.out.println("FIRST HIT");
			if(!getWorld().isClient) {
				System.out.println("IS CALLED");
				getWorld().createExplosion(this, getX(), getY(), getZ(), 5f, ExplosionSourceType.BLOW);
			}
			setVelocity(0, 0, 0);
			setPosition(getPos().add(0, 1, 0));
			setHit(true);
		}
	}

	@Override
	protected void initDataTracker() {
		dataTracker.startTracking(IMPACT, false);
		//dataTracker.startTracking(DWINDLE_COUNTER, MAX_DWINDLE_TIME);
	}
	
	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		setHit(nbt.getBoolean("StarHasHit"));
	}
	
	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.putBoolean("StarHasHit", hasHit());
	}
	
	public void setHit(boolean hasHit) {
		dataTracker.set(IMPACT, hasHit);
	}
	
	public boolean hasHit() {
		return dataTracker.get(IMPACT);
	}
	
	@Override
	public Packet<ClientPlayPacketListener> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}
	
	/*
	public void setDwindleTime(int dwindleTime) {
		dataTracker.set(DWINDLE_COUNTER, dwindleTime);
	}*/
	
	public int getDwindleTime() {
		//return dataTracker.get(DWINDLE_COUNTER);
		return dwindleCounter;
	}
	
	@Override
	public boolean shouldRender(double distance) {
		return super.shouldRender(distance / 5);
	}
	
}
