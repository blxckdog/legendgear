package nmccoy.legendgear.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import nmccoy.legendgear.LegendGear;

public class ReedPipesItem extends Item {

	private static final int[] NOTES = {0, 3, 7, 9, 12};
	private static final int[] ALT_NOTES = {-1, 2, 5, 8, 11};
	
	public ReedPipesItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if(!user.getStackInHand(hand).isOf(LegendGear.REED_PIPES)) {
			return super.use(world, user, hand);
		}
		
		ItemStack stack = user.getStackInHand(hand);
		user.setCurrentHand(hand);
		
		int note = getNoteBySpan(
					(180 - (user.getPitch() + 90)) / 180, 
					(user.isSneaking()) ? ALT_NOTES : NOTES
				);
		note++;
		
		if(!world.isClient) {
			world.playSound(null, user.getBlockPos(), LegendGear.SOUND_FLUTE_ATTACK, SoundCategory.NEUTRAL, 1f, getPitchByNote(note));
		}
		
		stack.getOrCreateNbt().putInt("fluteNote", note);
		return TypedActionResult.success(stack, false);
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.NONE;
	}
	
	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		stack.getOrCreateNbt().putFloat("noteTime", 0);
	}
	
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		float noteTime = stack.getOrCreateNbt().getFloat("noteTime");
		int lastNote = stack.getOrCreateNbt().getInt("fluteNote");
		
		int note = getNoteBySpan(
				(180 - (user.getPitch() + 90)) / 180, 
				(user.isSneaking()) ? ALT_NOTES : NOTES
			);
		note++;
		
		float pitch = getPitchByNote(note);
		
		if(note != lastNote) {
			stack.getOrCreateNbt().putInt("fluteNote", note);
			noteTime = 0;
			
			if(!world.isClient) {
				world.playSound(null, user.getBlockPos(), LegendGear.SOUND_FLUTE_ATTACK, SoundCategory.NEUTRAL, 1f, pitch);
			}
		} else noteTime += pitch;
		
		if(noteTime >= 5) {
			noteTime -= 5;
			
			if(!world.isClient) {
				world.playSound(null, user.getBlockPos(), LegendGear.SOUND_FLUTE_SUSTAIN, SoundCategory.NEUTRAL, 1f, pitch);
			}
		}
		
		stack.getOrCreateNbt().putFloat("noteTime", noteTime);
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}
	
	
	private float getPitchByNote(int note) {
		return (float) Math.pow(2d, (note - 12) / 12d);
	}
	
	private int getNoteBySpan(float playerPitch, int[] notes) {
		int index = (int) (playerPitch * (notes.length-1) + 0.5);
		if(index < 0) index = 0;
		if(index > notes.length) index = notes.length - 1;
		return notes[index];
	}
	
}
