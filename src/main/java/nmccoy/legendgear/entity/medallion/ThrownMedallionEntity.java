package nmccoy.legendgear.entity.medallion;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

import nmccoy.legendgear.LegendGear;
import nmccoy.legendgear.item.medallion.AbstractMedallionItem;

public class ThrownMedallionEntity extends ThrownItemEntity {

	public ThrownMedallionEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		Item item = getStack().getItem();
		
		if(item instanceof AbstractMedallionItem) {
			((AbstractMedallionItem) item).onImpact(getWorld(), blockHitResult.getPos());
		}
		
		remove(RemovalReason.DISCARDED);
	}

	@Override
	protected Item getDefaultItem() {
		return LegendGear.EARTH_MEDALLION;
	}
	
	public void setStack(ItemStack medallionStack) {
		setItem(medallionStack);
	}

}
