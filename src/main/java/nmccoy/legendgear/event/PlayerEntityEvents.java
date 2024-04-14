package nmccoy.legendgear.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public interface PlayerEntityEvents {
	
	Event<ItemPickup> ITEM_PICKUP = EventFactory.createArrayBacked(ItemPickup.class,
			(callbacks) -> (player, item) -> {
				for (ItemPickup event : callbacks) {
					ActionResult result = event.onItemPickup(player, item);

					if (result != ActionResult.PASS) {
						return result;
					}
				}
				
				return ActionResult.PASS;
			}
	);
	
	@FunctionalInterface
	interface ItemPickup {
		ActionResult onItemPickup(PlayerEntity player, ItemEntity item);
	}
	
}
