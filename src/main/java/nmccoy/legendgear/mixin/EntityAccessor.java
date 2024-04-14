package nmccoy.legendgear.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.Entity;

@Mixin(Entity.class)
public interface EntityAccessor {

	@Accessor("inNetherPortal")
	public void setInNetherPortal(boolean inNetherPortal);
	
}
