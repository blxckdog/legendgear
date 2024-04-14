package nmccoy.legendgear.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import nmccoy.legendgear.LegendGear;

public class EmeraldPieceItem extends Item {

	public static final int EMERALD_EXCHANGE_RATE = 9;
	
	public EmeraldPieceItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		
		if(!stack.isOf(LegendGear.EMERALD_PIECE)) {
			return TypedActionResult.pass(stack);
		}
		
		if(stack.getCount() >= EMERALD_EXCHANGE_RATE) {
			stack.setCount(stack.getCount() - EMERALD_EXCHANGE_RATE);
			ItemStack merged = new ItemStack(Items.EMERALD, 1);
			
			if(!user.getInventory().insertStack(merged)) {
				user.dropItem(merged, true, true);
			}
			
			user.getWorld().playSound(null, user.getBlockPos(), LegendGear.SOUND_MONEY_BIG, SoundCategory.NEUTRAL, 0.5f, 1f);
		}
		
		return TypedActionResult.success(stack);
	}
	
}
