package nmccoy.legendgear.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import nmccoy.legendgear.LegendGear;
import nmccoy.legendgear.event.LivingEntityEvents;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {

	protected LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Inject(method = "damage", at = @At("RETURN"))
	public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		final LivingEntity thisEntity = (LivingEntity) (Object) this;
		
		ActionResult result = LivingEntityEvents.TAKE_DAMAGE.invoker()
				.onDamage(thisEntity, source, amount);
		
		if(result == ActionResult.FAIL) {
			info.setReturnValue(false);
		}
	}
	
	@Inject(method = "getPassengerRidingPos", at = @At("RETURN"), cancellable = true)
	public void getPassengerRidingPos(Entity passenger, CallbackInfoReturnable<Vec3d> info) {
		final LivingEntity thisEntity = (LivingEntity) (Object) this;
		Vec3d offset = info.getReturnValue();
		
		if(!(thisEntity instanceof PlayerEntity player)) {
			return;
		}
		
		if(player.getMainHandStack().isOf(LegendGear.TITAN_BAND)) {
			info.setReturnValue(offset.add(0, 1, 0));
		}
	}

}
