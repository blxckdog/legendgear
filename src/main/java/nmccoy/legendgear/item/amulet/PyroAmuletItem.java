package nmccoy.legendgear.item.amulet;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PyroAmuletItem extends Item {

	public PyroAmuletItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}

}
