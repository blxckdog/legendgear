package nmccoy.legendgear.item.amulet;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GeoAmuletItem extends Item {

	public GeoAmuletItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}

}
