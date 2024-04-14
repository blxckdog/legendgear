package nmccoy.legendgear.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;

public interface LivingEntityEvents {
	
	Event<TakeDamage> TAKE_DAMAGE = EventFactory.createArrayBacked(TakeDamage.class,
			(callbacks) -> (entity, source, amount) -> {
				for (TakeDamage event : callbacks) {
					ActionResult result = event.onDamage(entity, source, amount);

					if (result != ActionResult.PASS) {
						return result;
					}
				}
				
				return ActionResult.PASS;
			}
	);
	
	@FunctionalInterface
	interface TakeDamage {
		ActionResult onDamage(LivingEntity damaged, DamageSource source, float amount);
	}
	
}
