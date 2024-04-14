package nmccoy.legendgear.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity.PickupPermission;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import nmccoy.legendgear.LegendGear;
import nmccoy.legendgear.item.QuiverItem;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {
	
	public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tryPickup", at = @At("HEAD"), cancellable = true)
	public void tryPickup(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
		PersistentProjectileEntity thisProjectile = (PersistentProjectileEntity) (Object) this;
		
		if(!(thisProjectile instanceof ArrowEntity) && !(thisProjectile instanceof SpectralArrowEntity)) {
			return;
		}
		
		if(thisProjectile.pickupType == PickupPermission.ALLOWED) {
			// Check for quiver in inventory
			for (int i = 0; i < player.getInventory().size(); ++i) {
				ItemStack quiver = player.getInventory().getStack(i);
				
				if(quiver.isOf(LegendGear.QUIVER) && QuiverItem.getArrowCount(quiver) < QuiverItem.MAX_STORAGE) {
					ItemStack arrowStack = thisProjectile.getItemStack();
					int inserted = QuiverItem.insertArrows(quiver, arrowStack);
					
					if(inserted == arrowStack.getCount()) {
						// player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL);
						info.setReturnValue(true);
						return;
					} else {
						thisProjectile.getItemStack().decrement(inserted);
						return;
					}
				}
			}
		}
	}
	
}
