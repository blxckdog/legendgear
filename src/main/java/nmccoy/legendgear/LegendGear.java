package nmccoy.legendgear;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

import nmccoy.legendgear.block.LegendGearBlocks;
import nmccoy.legendgear.entity.LegendGearEntities;
import nmccoy.legendgear.item.LegendGearItems;
import nmccoy.legendgear.world.MysticShrubFeature;
import nmccoy.legendgear.world.MysticShrubFeatureConfig;

public class LegendGear implements ModInitializer, LegendGearItems, LegendGearBlocks, LegendGearEntities {
	
	public static final String MOD_ID = "legendgear";
	
	public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(MAGIC_BOOMERANG))
			.displayName(Text.translatable("itemGroup.legendgear.all_group"))
			.entries((context, entries) -> {
				// Add block items
				entries.add(MYSTIC_SHRUB_ITEM);
				entries.add(SWORD_PEDESTAL_ITEM);
				entries.add(STAR_STONE_ITEM);
				entries.add(SUGAR_CUBE_ITEM);
				
				LegendGearItems.getDeclaredItems().values()
						.forEach(item -> entries.add(item));
			})
			.build();
	
	public static final DefaultParticleType MAGIC_RUNE = FabricParticleTypes.simple();
	
	public static final SoundEvent SOUND_BOOMERANG = SoundEvent.of(id("boomerang"));
	public static final SoundEvent SOUND_SWORD_TAKE = SoundEvent.of(id("sword_take"));
	public static final SoundEvent SOUND_SWORD_PLACE = SoundEvent.of(id("sword_place"));
	public static final SoundEvent SOUND_TITAN_LIFT = SoundEvent.of(id("titan_lift"));
	public static final SoundEvent SOUND_TITAN_THROW = SoundEvent.of(id("titan_throw"));
	public static final SoundEvent SOUND_HEART = SoundEvent.of(id("heart"));
	public static final SoundEvent SOUND_SINE = SoundEvent.of(id("sine"));
	
	public static final SoundEvent SOUND_MONEY_SMALL = SoundEvent.of(id("money_small"));
	public static final SoundEvent SOUND_MONEY_MID = SoundEvent.of(id("money_mid"));
	public static final SoundEvent SOUND_MONEY_BIG = SoundEvent.of(id("money_big"));
	
	public static final SoundEvent SOUND_STARFALL = SoundEvent.of(id("starfall"));
	public static final SoundEvent SOUND_STAR_CAUGHT = SoundEvent.of(id("star_caught"));
	public static final SoundEvent SOUND_STAR_TWINKLE = SoundEvent.of(id("star_twinkle"));

	public static final SoundEvent SOUND_FLUTE_ATTACK = SoundEvent.of(id("flute_attack"));
	public static final SoundEvent SOUND_FLUTE_SUSTAIN = SoundEvent.of(id("flute_sustain"));
	
	public static final MysticShrubFeature MYSTIC_SHRUB_FEATURE = new MysticShrubFeature(MysticShrubFeatureConfig.CODEC);

	
	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		LegendGearEntities.registerAll();
		LegendGearBlocks.registerAll();
		LegendGearItems.registerAll();
		
		Registry.register(Registries.ITEM_GROUP, id("all_group"), ITEM_GROUP);
		Registry.register(Registries.PARTICLE_TYPE, id("magic_rune"), MAGIC_RUNE);
		
		// Register all sound events
		Registry.register(Registries.SOUND_EVENT, id("boomerang"), SOUND_BOOMERANG);
		Registry.register(Registries.SOUND_EVENT, id("sword_take"), SOUND_SWORD_TAKE);
		Registry.register(Registries.SOUND_EVENT, id("sword_place"), SOUND_SWORD_PLACE);
		Registry.register(Registries.SOUND_EVENT, id("titan_lift"), SOUND_TITAN_LIFT);
		Registry.register(Registries.SOUND_EVENT, id("titan_throw"), SOUND_TITAN_THROW);
		Registry.register(Registries.SOUND_EVENT, id("heart"), SOUND_HEART);
		Registry.register(Registries.SOUND_EVENT, id("sine"), SOUND_SINE);
		
		Registry.register(Registries.SOUND_EVENT, id("money_small"), SOUND_MONEY_SMALL);
		Registry.register(Registries.SOUND_EVENT, id("money_mid"), SOUND_MONEY_MID);
		Registry.register(Registries.SOUND_EVENT, id("money_big"), SOUND_MONEY_BIG);

		Registry.register(Registries.SOUND_EVENT, id("starfall"), SOUND_STARFALL);
		Registry.register(Registries.SOUND_EVENT, id("star_caught"), SOUND_STAR_CAUGHT);
		Registry.register(Registries.SOUND_EVENT, id("star_twinkle"), SOUND_STAR_TWINKLE);
		
		Registry.register(Registries.SOUND_EVENT, id("flute_attack"), SOUND_FLUTE_ATTACK);
		Registry.register(Registries.SOUND_EVENT, id("flute_sustain"), SOUND_FLUTE_SUSTAIN);
		
		// Register worldgen features
		Registry.register(Registries.FEATURE, id("mystic_shrub_feature"), MYSTIC_SHRUB_FEATURE);
		RegistryKey<PlacedFeature> placedMysticShrubFeature = RegistryKey.of(RegistryKeys.PLACED_FEATURE, id("mystic_shrub_feature"));
		
		BiomeModifications.addFeature(
					BiomeSelectors.foundInOverworld(), 
					GenerationStep.Feature.VEGETAL_DECORATION, 
					placedMysticShrubFeature
				);
	}
	
}