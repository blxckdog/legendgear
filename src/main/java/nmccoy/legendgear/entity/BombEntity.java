package nmccoy.legendgear.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import nmccoy.legendgear.LegendGear;

public class BombEntity extends ThrownItemEntity {

	public BombEntity(EntityType<BombEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected Item getDefaultItem() {
		return LegendGear.BOMB;
	}
	
}
