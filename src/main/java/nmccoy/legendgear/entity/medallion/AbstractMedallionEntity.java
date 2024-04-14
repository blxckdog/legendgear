package nmccoy.legendgear.entity.medallion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Ownable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class AbstractMedallionEntity extends Entity implements Ownable {

	public AbstractMedallionEntity(EntityType<?> type, World world) {
		super(type, world);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Entity getOwner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initDataTracker() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		// TODO Auto-generated method stub
		
	}

}
