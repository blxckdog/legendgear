package nmccoy.legendgear.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

import nmccoy.legendgear.event.PlayerEntityEvents;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

	@Inject(at = @At("HEAD"), method = "onPlayerCollision", cancellable = true)
	private void pickupItem(PlayerEntity player, CallbackInfo info) {
		final ItemEntity thisEntity = (ItemEntity) (Object) this;
		
		if(player == null || thisEntity.cannotPickup()) {
			// Another test will be necessary to test if player has a free slot
			return;
		}
		
		ActionResult result = PlayerEntityEvents.ITEM_PICKUP.invoker()
				.onItemPickup(player, thisEntity);
		
		if(result == ActionResult.FAIL || result == ActionResult.CONSUME) {
			info.cancel();
		}
	}
	
}
