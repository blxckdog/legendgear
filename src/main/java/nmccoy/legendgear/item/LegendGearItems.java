package nmccoy.legendgear.item;

import static nmccoy.legendgear.LegendGear.id;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;

import nmccoy.legendgear.LegendGear;
import nmccoy.legendgear.event.PlayerEntityEvents;
import nmccoy.legendgear.item.amulet.AeroAmuletItem;
import nmccoy.legendgear.item.amulet.GeoAmuletItem;
import nmccoy.legendgear.item.amulet.PyroAmuletItem;
import nmccoy.legendgear.item.medallion.EarthMedallionItem;
import nmccoy.legendgear.item.medallion.EnderMedallionItem;
import nmccoy.legendgear.item.medallion.FireMedallionItem;
import nmccoy.legendgear.item.medallion.WindMedallionItem;

public interface LegendGearItems {
	
	// Amulets and medallions
	Item AERO_AMULET = new AeroAmuletItem(notStackable());
	Item GEO_AMULET = new GeoAmuletItem(notStackable());
	Item PYRO_AMULET = new PyroAmuletItem(notStackable());
	Item EARTH_MEDALLION = new EarthMedallionItem(notStackable());
	Item ENDER_MEDALLION = new EnderMedallionItem(notStackable());
	Item WIND_MEDALLION = new WindMedallionItem(notStackable());
	Item FIRE_MEDALLION = new FireMedallionItem(notStackable());

	// Combat items
	Item MAGIC_BOOMERANG = new MagicBoomerangItem(notStackable());
	Item HEADBAND_OF_VALOR = new HeadbandOfValorItem(notStackable());
	Item WHIRLWIND_BOOTS = new WhirlwindBoots(notStackable());
	Item HOOKSHOT = new HookshotItem(notStackable());
	Item QUIVER = new QuiverItem(notStackable());
	Item BOMB_BAG = new BombBagItem(notStackable());
	Item BOMB = new BombItem(basic().maxCount(16));
	// AUGMENTED_SWORDS ???
	// NEW: HEART_CONTAINER ???

	// Food items
	Item MILK_CHOCOLATE = new MilkChocolateItem(basic());
	Item EMERALD_ROCK_CANDY = new RockCandyItem(basic(), player
			-> player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 20*30, 3)));
	Item DIAMOND_ROCK_CANDY = new RockCandyItem(basic(), player
			-> player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20*30, 3)));
	Item REDSTONE_ROCK_CANDY = new RockCandyItem(basic(), player 
			-> player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 20*30, 0)));
	Item LAPIS_ROCK_CANDY = new RockCandyItem(basic(), player 
			-> player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20*30, 1)));
	
	// Misc items
	Item MAGIC_MIRROR = new MagicMirrorItem(notStackable());
	Item TITAN_BAND = new TitanBandItem(notStackable());
	Item EMERALD_PIECE = new EmeraldPieceItem(basic());
	Item EMERALD_SHARD = new EmeraldShardItem(basic());
	Item REED_PIPES = new ReedPipesItem(notStackable());
	Item HEART = new Item(notStackable());
	Item STAR_PIECE = new Item(basic());
	Item STAR_DUST = new Item(basic());
	// MAGIC_POWDER ???
	// STARBEAM_TORCH
	// JARS --> Pot
	// PHOENIX_FEATHER --> Totem of Undying
			
	
	public static void registerAll() {
		getDeclaredItems().forEach((name, item) -> {
			Registry.register(Registries.ITEM, id(name), item);
		});

		TitanBandItem.registerEvents();
		QuiverItem.registerEvents();
		registerItemSoundEvents();
	}
	
	
	private static void registerItemSoundEvents() {
		PlayerEntityEvents.ITEM_PICKUP.register((player, item) -> {
			ItemStack stack = item.getStack();
			
			if(stack.isOf(HEART)) {
				if(!player.getWorld().isClient) {
					player.setHealth(player.getHealth() + 2);
					item.discard();
				}
				player.getWorld().playSound(null, player.getBlockPos(), LegendGear.SOUND_HEART, SoundCategory.NEUTRAL, 0.7f, 1f);
				return ActionResult.CONSUME;
			}
			
			if(player.getInventory().getEmptySlot() < 0) {
				return ActionResult.PASS;
			}
			
			if(stack.isOf(EMERALD_SHARD)) {
				player.getWorld().playSound(null, player.getBlockPos(), LegendGear.SOUND_MONEY_SMALL, SoundCategory.NEUTRAL, 0.5f, 1f);
			} else if(stack.isOf(EMERALD_PIECE)) {
				player.getWorld().playSound(null, player.getBlockPos(), LegendGear.SOUND_MONEY_MID, SoundCategory.NEUTRAL, 0.5f, 1f);
			} else if(stack.isOf(Items.EMERALD)) {
				player.getWorld().playSound(null, player.getBlockPos(), LegendGear.SOUND_MONEY_BIG, SoundCategory.NEUTRAL, 0.5f, 1f);
			}
			
			return ActionResult.PASS;
		});
	}
	
	
	public static Map<String, Item> getDeclaredItems() {
		Field[] fields = LegendGearItems.class.getDeclaredFields();
		Map<String, Item> items = new HashMap<>();
		
		for(Field field : fields) {
			try {
				if(field.getType() == Item.class) {
					Item item = (Item) field.get(LegendGearItems.class);
					items.put(field.getName().toLowerCase(), item);
				}
			} catch (IllegalArgumentException | IllegalAccessException ignored) {}
		}
		
		return items;
	}
	
	
	private static Item.Settings notStackable() {
		return basic().maxCount(1);
	}
	
	private static Item.Settings basic() {
		return new FabricItemSettings();
	}
	
}
