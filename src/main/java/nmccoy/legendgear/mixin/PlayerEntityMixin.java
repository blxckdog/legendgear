package nmccoy.legendgear.mixin;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import nmccoy.legendgear.LegendGear;
import nmccoy.legendgear.item.QuiverItem;
import nmccoy.legendgear.item.TitanBandItem;
import nmccoy.legendgear.item.TitanBandItem.TitanLiftHolder;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements TitanLiftHolder {

	private static final TrackedData<Boolean> TITAN_LIFT_TRACKER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(method = "initDataTracker", at = @At("HEAD"))
	public void initTracker(CallbackInfo info) {
		dataTracker.startTracking(TITAN_LIFT_TRACKER, false);
	}
	
	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void readNbt(NbtCompound tag, CallbackInfo info) {
		dataTracker.set(TITAN_LIFT_TRACKER, tag.getBoolean("isTitanLifting"));
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	public void writeNbt(NbtCompound tag, CallbackInfo info) {
		tag.putBoolean("isTitanLifting", dataTracker.get(TITAN_LIFT_TRACKER));
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo info) {
		if(isLifting()) {
			PlayerEntity thisPlayer = (PlayerEntity) ((Object) this);
			
			if(!hasPassengers()) {
				setLifting(false);
			} else if(!thisPlayer.getMainHandStack().isOf(LegendGear.TITAN_BAND)) {
				TitanBandItem.handleThrow(thisPlayer, true);
			}
		}
	}
	

	@Inject(method = "getProjectileType", at = @At("HEAD"), cancellable = true)
	private void checkQuiver(ItemStack weapon, CallbackInfoReturnable<ItemStack> info) {
		if (!(weapon.getItem() instanceof RangedWeaponItem rangedWeapon)) {
			return;
		}
		
		PlayerEntity thisPlayer = (PlayerEntity) ((Object) this);
		Predicate<ItemStack> isProjectile = rangedWeapon.getHeldProjectiles();
		
		// First look in main or off hand
		ItemStack projectiles = RangedWeaponItem.getHeldProjectile(thisPlayer, isProjectile);
		if (!projectiles.isEmpty()) {
			info.setReturnValue(projectiles);
			return;
		}
		
		// Else look for quiver in player's inventory
		ItemStack quiver = ItemStack.EMPTY;
		for (int i = 0; i < thisPlayer.getInventory().size(); i++) {
			ItemStack stack = thisPlayer.getInventory().getStack(i);
			
			if(stack.isOf(LegendGear.QUIVER)) {
				quiver = stack;
				break;
			}
		}
		
		if(quiver == null || quiver.isEmpty()) {
			return; // No quiver found
		}
		
		QuiverItem.getFirstStack(quiver, isProjectile)
				.ifPresent(projectilesInQuiver -> info.setReturnValue(projectilesInQuiver));
	}
	
	@Override
	public boolean isLifting() {
		return dataTracker.get(TITAN_LIFT_TRACKER);
	}
	
	@Override
	public void setLifting(boolean isLifting) {
		dataTracker.set(TITAN_LIFT_TRACKER, isLifting);
	}
	
}
