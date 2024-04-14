package nmccoy.legendgear.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import nmccoy.legendgear.LegendGear;

public class HookshotBarbEntity extends ThrownEntity {

	private static final int MAX_AGE = 20 * 120;
	
	private boolean anchored = false;
	
	public HookshotBarbEntity(EntityType<HookshotBarbEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		// Discard if shooter is gone
		LivingEntity owner = (LivingEntity) getOwner();
		if(owner == null || !owner.isAlive()) {
			discard();
			return;
		}
		
		// Discard if hookshot isn't in use anymore
		if((age > 1 && owner.getItemUseTime() == 0) || !owner.getActiveItem().isOf(LegendGear.HOOKSHOT)) {
			discard();
			return;
		}
		
		if(anchored) {
			setVelocity(0, 0, 0);
			
			// Move user to barb entity
			Vec3d delta = getPos().subtract(owner.getEyePos());
			owner.setVelocity(delta.normalize());
			owner.fallDistance = 0;
			
			if(distanceTo(owner) < 0.5) {
				owner.setVelocity(0, 0, 0);
				owner.fallDistance = 0;
				discard();
				return;
			}
		} else {
			if(distanceTo(owner) > 20) {
				discard();
				return;
			}
		}
		
		// Discard if something went wrong
		if(age > MAX_AGE) {
			discard();
		}
	}
	
	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		if(!anchored) {
			anchored = true;
		}
	}
	
	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		// TODO Auto-generated method stub
		super.onEntityHit(entityHitResult);
	}

	public PlayerEntity getPlayerOwner() {
		if(getOwner() == null) {
			return null;
		}
		
		return getOwner() instanceof PlayerEntity player ? player : null;
	}

	@Override
	protected void initDataTracker() {}

}
