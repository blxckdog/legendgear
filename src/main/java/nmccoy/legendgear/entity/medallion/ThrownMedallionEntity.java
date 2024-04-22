package nmccoy.legendgear.entity.medallion;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public abstract class ThrownMedallionEntity extends ThrownItemEntity {

	protected boolean isActive = false;
	protected int activeAge = -1;
	
	public ThrownMedallionEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if(isActive) {
			activeTick();
		}
		
		if(age > 20*120) {
			discard();
		}
	}
	
	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		setItem(ItemStack.EMPTY);
		setVelocity(0, 0, 0);
		
		if(!isActive) {
			isActive = true;
			activeAge = age;
		}
	}
	
	
	protected abstract void activeTick();

}
