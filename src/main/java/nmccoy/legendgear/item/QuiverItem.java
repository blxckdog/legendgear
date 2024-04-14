package nmccoy.legendgear.item;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.client.item.BundleTooltipData;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import nmccoy.legendgear.LegendGear;
import nmccoy.legendgear.event.PlayerEntityEvents;

public class QuiverItem extends Item {

	public static final int MAX_STORAGE = 256;
	
	private static final String ARROWS_KEY = "Arrows";
	private static final String TOOLTIP_ID = "item.minecraft.bundle.fullness";
	private static final int ITEM_BAR_COLOR = MathHelper.packRgb(0.4f,0.4f, 1f);
	
	public QuiverItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean onStackClicked(ItemStack quiver, Slot slot, ClickType clickType, PlayerEntity user) {
		if (clickType != ClickType.RIGHT) {
			return false;
		}
		
		ItemStack stack = slot.getStack();
		
		if (stack.isEmpty()) {
			//playRemoveOneSound(user);
			removeFirstStack(quiver, always -> true).ifPresent(removedStack -> insertArrows(quiver, slot.insertStack(removedStack)));
		} else if (stack.isIn(ItemTags.ARROWS)) {
			int toInsert = Math.min(MAX_STORAGE - getArrowCount(quiver), stack.getCount());
			int inserted = insertArrows(quiver, slot.takeStackRange(stack.getCount(), toInsert, user));
			
			if (inserted > 0) {
				// this.playInsertSound((Entity) player);
			}
		}
		return true;
	}

	public boolean onClicked(ItemStack quiver, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity user, StackReference cursor) {
		if (clickType != ClickType.RIGHT || !slot.canTakePartial(user)) {
			return false;
		}
		
		if (otherStack.isEmpty()) {
			removeFirstStack(quiver, always -> true).ifPresent(itemStack -> {
				// playRemoveOneSound(user);
				cursor.set(itemStack);
			});
		} else {
			int inserted = insertArrows(quiver, otherStack);
			if (inserted > 0) {
				//this.playInsertSound(user);
				otherStack.decrement(inserted);
			}
		}
		return true;
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack quiver = user.getStackInHand(hand);
		
		if(removeAllArrows(quiver, user)) {
			// this.playDropContentsSound(user);
			return TypedActionResult.success(quiver, (boolean) world.isClient());
		}
		
		return TypedActionResult.fail(quiver);
	}

	@Override
	public int getItemBarColor(ItemStack stack) {
		return ITEM_BAR_COLOR;
	}
	
	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return getArrowCount(stack) > 0;
	}

	@Override
	public int getItemBarStep(ItemStack stack) {
		return Math.min(1 + 12 * getArrowCount(stack) / MAX_STORAGE, 13);
	}
	
	@Override
	public Optional<TooltipData> getTooltipData(ItemStack quiver) {
		DefaultedList<ItemStack> stackList = DefaultedList.of();
		getArrows(quiver).forEach(arrowStack -> stackList.add(arrowStack));
		
		return Optional.of(new BundleTooltipData(stackList, 64 * (getArrowCount(quiver) / MAX_STORAGE)));
	}
	
	@Override
	public void appendTooltip(ItemStack quiver, World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable(TOOLTIP_ID, getArrowCount(quiver), MAX_STORAGE).formatted(Formatting.GRAY));
	}
	
	public static int insertArrows(ItemStack quiver, ItemStack arrows) {
		if (arrows.isEmpty() || !arrows.isIn(ItemTags.ARROWS)) {
			return 0;
		}
		
		NbtCompound nbt = quiver.getOrCreateNbt();
		if (!nbt.contains(ARROWS_KEY)) {
			nbt.put(ARROWS_KEY, new NbtList());
		}
		
		int occupancy = getArrowCount(quiver);
		int insertCount = Math.min(arrows.getCount(), MAX_STORAGE - occupancy);
		if (insertCount <= 0) {
			return 0;
		}
		
		NbtList arrowList = nbt.getList(ARROWS_KEY, NbtElement.COMPOUND_TYPE);
		for(int toInsert = insertCount; toInsert > 0;) {
			Optional<NbtCompound> existingStack = findStackToMerge(arrows, arrowList);
			
			if (existingStack.isPresent()) {
				NbtCompound existingNbt = existingStack.get();
				ItemStack before = ItemStack.fromNbt(existingNbt);
				
				int toInsertNow = Math.min(toInsert, before.getMaxCount() - before.getCount());
				before.increment(toInsertNow);
				before.writeNbt(existingNbt);
				
				arrowList.remove(existingNbt);
				arrowList.add(0, existingNbt);
				toInsert -= toInsertNow;
			} else {
				ItemStack stack = arrows.copyWithCount(toInsert);
				NbtCompound stackNbt = new NbtCompound();
				stack.writeNbt(stackNbt);
				arrowList.add(0, stackNbt);
				toInsert -= stack.getCount();
			}
		}
		
		return insertCount;
	}
	
	private static Optional<NbtCompound> findStackToMerge(ItemStack arrows, NbtList items) {
		return items.stream()
				.filter(NbtCompound.class::isInstance)
				.map(NbtCompound.class::cast)
				.filter(item -> {
					ItemStack existing = ItemStack.fromNbt(item);
					return ItemStack.canCombine(existing, arrows) && existing.getCount() < existing.getMaxCount();
				})
				.findFirst();
	}
	
	public static Optional<ItemStack> removeFirstStack(ItemStack quiver, Predicate<ItemStack> predicate) {
		NbtCompound nbt = quiver.getOrCreateNbt();
		if (!nbt.contains(ARROWS_KEY)) {
			return Optional.empty();
		}
		
		NbtList nbtList = nbt.getList(ARROWS_KEY, NbtElement.COMPOUND_TYPE);
		ItemStack foundStack = null;
		
		for(int i=0; i<nbtList.size(); i++) {
			NbtCompound firstNbtStack = nbtList.getCompound(i);
			ItemStack nextStack = ItemStack.fromNbt(firstNbtStack);
			
			if(predicate.test(nextStack)) {
				foundStack = nextStack;
				nbtList.remove(i);
				break;
			}
		}
		
		if (nbtList.isEmpty()) {
			quiver.removeSubNbt(ARROWS_KEY);
		}
		
		return foundStack == null ? Optional.empty() : Optional.of(foundStack);
	}
	
	public static Optional<ItemStack> getFirstStack(ItemStack quiver, Predicate<ItemStack> predicate) {
		NbtCompound nbt = quiver.getNbt();
		
		if (nbt == null) {
			return Optional.empty();
		}
		
		return nbt.getList(ARROWS_KEY, NbtElement.COMPOUND_TYPE).stream()
				.map(NbtCompound.class::cast)
				.map(nbtStack -> QuiverItemStack.fromNbt(nbtStack, quiver))
				.filter(predicate)
				.findFirst();
	}
	
	public static void updateCount(ItemStack quiver, ItemStack arrowStack, int count) {
		removeFirstStack(quiver, stack -> ItemStack.canCombine(arrowStack, stack))
				.ifPresent(stack -> {
					stack.setCount(count);
					if(!stack.isEmpty() && stack.getCount() > 0 && count > 0) {
						insertArrows(quiver, stack);
					}
				});
	}
	
	private static boolean removeAllArrows(ItemStack quiver, PlayerEntity player) {
		NbtCompound nbt = quiver.getOrCreateNbt();
		
		if (!nbt.contains(ARROWS_KEY)) {
			return false;
		}
		
		if (player instanceof ServerPlayerEntity) {
			NbtList arrowList = nbt.getList(ARROWS_KEY, NbtElement.COMPOUND_TYPE);
			
			for (int i = 0; i < arrowList.size(); ++i) {
				NbtCompound arrowStackNbt = arrowList.getCompound(i);
				ItemStack arrowStack = ItemStack.fromNbt((NbtCompound) arrowStackNbt);
				player.dropItem(arrowStack, true);
			}
		}
		quiver.removeSubNbt(ARROWS_KEY);
		return true;
	}
	
	public static int getArrowCount(ItemStack quiver) {
		return getArrows(quiver)
				.mapToInt(itemStack -> itemStack.getCount())
				.sum();
	}
	
	private static Stream<ItemStack> getArrows(ItemStack quiver) {
		NbtCompound nbt = quiver.getNbt();
		
		if (nbt == null) {
			return Stream.empty();
		}
		
		return nbt.getList(ARROWS_KEY, NbtElement.COMPOUND_TYPE).stream()
				.map(NbtCompound.class::cast)
				.map(ItemStack::fromNbt);
	}
	
	
	public static void registerEvents() {
		PlayerEntityEvents.ITEM_PICKUP.register((PlayerEntity player, ItemEntity item) -> {		
			if(player.getWorld().isClient) {
				return ActionResult.PASS;
			}
			
			for (int i = 0; i < player.getInventory().size(); ++i) {
				ItemStack quiver = player.getInventory().getStack(i);
				
				if(quiver.isOf(LegendGear.QUIVER) && getArrowCount(quiver) < MAX_STORAGE) {
					ItemStack arrowStack = item.getStack();
					int inserted = insertArrows(quiver, arrowStack);
					
					if(inserted == arrowStack.getCount()) {
						item.discard();
						player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL);
						return ActionResult.CONSUME;
					} else {
						arrowStack.decrement(inserted);
						item.setStack(arrowStack);
						return ActionResult.CONSUME_PARTIAL;
					}
				}
			}
		
			return ActionResult.PASS;
		});
	}
}
