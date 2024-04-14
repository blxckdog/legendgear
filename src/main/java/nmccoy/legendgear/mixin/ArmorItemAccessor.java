package nmccoy.legendgear.mixin;

import java.util.EnumMap;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorItem.Type;

@Mixin(ArmorItem.class)
public interface ArmorItemAccessor {

	@Accessor("MODIFIERS")
	public static EnumMap<Type, UUID> getModifiers() {
		throw new AssertionError();
	}
	
}