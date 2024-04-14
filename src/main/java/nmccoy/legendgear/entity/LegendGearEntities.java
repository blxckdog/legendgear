package nmccoy.legendgear.entity;

import static nmccoy.legendgear.LegendGear.id;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import nmccoy.legendgear.entity.medallion.ThrownMedallionEntity;

public interface LegendGearEntities {

	EntityType<MagicBoomerangEntity> MAGIC_BOOMERANG_ENTITY = Registry.register(
			Registries.ENTITY_TYPE, 
			id("magic_boomerang"),
			FabricEntityTypeBuilder.create(SpawnGroup.MISC, MagicBoomerangEntity::new)
					.dimensions(EntityDimensions.fixed(.7f, .7f))
					.build()
			);
	
	EntityType<ThrownMedallionEntity> THROWN_MEDALLION_ENTITY = Registry.register(
			Registries.ENTITY_TYPE, 
			id("thrown_medallion"),
			FabricEntityTypeBuilder.create(SpawnGroup.MISC, ThrownMedallionEntity::new)
					.dimensions(EntityDimensions.fixed(.6f, .6f))
					.build()
			);
	
	EntityType<HookshotBarbEntity> HOOKSHOT_BARB_ENTITY = Registry.register(
			Registries.ENTITY_TYPE, 
			id("hookshot_barb"),
			FabricEntityTypeBuilder.create(SpawnGroup.MISC, HookshotBarbEntity::new)
					.dimensions(EntityDimensions.fixed(.6f, .6f))
					.build()
			);
	
	EntityType<ShootingStarEntity> SHOOTING_STAR_ENTITY = Registry.register(
			Registries.ENTITY_TYPE, 
			id("shooting_star"),
			FabricEntityTypeBuilder.create(SpawnGroup.MISC, ShootingStarEntity::new)
					.dimensions(EntityDimensions.fixed(.5f, .7f))
					.build()
			);

	
	public static void registerAll() {
		// Static variable declarations will be called on first access of the class
	}
	
}
