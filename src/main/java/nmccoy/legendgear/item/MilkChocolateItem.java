package nmccoy.legendgear.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MilkChocolateItem extends Item {

	private static final FoodComponent MILK_CHOCOLATE_FOOD_COMPONENT = new FoodComponent.Builder()
			.hunger(3)
			.saturationModifier(0.3f)
			.alwaysEdible()
			.build();
	
	
	public MilkChocolateItem(Settings settings) {
		super(settings.food(MILK_CHOCOLATE_FOOD_COMPONENT));
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (!world.isClient) {
			user.clearStatusEffects();
		}
		
		return super.finishUsing(stack, world, user);
	}
	
}
