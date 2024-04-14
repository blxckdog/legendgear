package nmccoy.legendgear.item.amulet;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AeroAmuletItem extends Item {

	public AeroAmuletItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}

}
