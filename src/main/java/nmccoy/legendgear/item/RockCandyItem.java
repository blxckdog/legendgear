package nmccoy.legendgear.item;

import java.util.function.Consumer;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

public class RockCandyItem extends Item {

	private static final FoodComponent ROCK_CANDY_FOOD_COMPONENT = new FoodComponent.Builder()
			.hunger(5)
			.saturationModifier(0.7f)
			.alwaysEdible()
			.build();
	
	private final Consumer<PlayerEntity> onEatCallback;
	
	
	public RockCandyItem(Settings settings, Consumer<PlayerEntity> onEat) {
		super(settings.food(ROCK_CANDY_FOOD_COMPONENT));
		this.onEatCallback = onEat;
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if(!world.isClient && user instanceof PlayerEntity player) {
			onEatCallback.accept(player);
		}
		
		ItemStack result = super.finishUsing(stack, world, user);
		
		if(user instanceof PlayerEntity player) {
			player.getInventory().insertStack(new ItemStack(Items.STICK, 1));
		}
		
		return result;
	}
	
	
	
}
