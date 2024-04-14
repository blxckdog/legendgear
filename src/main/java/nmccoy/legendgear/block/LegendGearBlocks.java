package nmccoy.legendgear.block;

import static nmccoy.legendgear.LegendGear.id;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

public interface LegendGearBlocks {

	// Mystic Shrub Block
	MysticShrub MYSTIC_SHRUB_BLOCK = new MysticShrub(FabricBlockSettings.create()
			.nonOpaque()
			.noCollision()
			.ticksRandomly()
			.breakInstantly()
			.sounds(BlockSoundGroup.GRASS)
			.luminance(state -> 5)
			.mapColor(MapColor.CYAN)
		);
	
	Item MYSTIC_SHRUB_ITEM = new BlockItem(MYSTIC_SHRUB_BLOCK, new FabricItemSettings());
	
	// Sword Pedestal Block
	SwordPedestal SWORD_PEDESTAL_BLOCK = new SwordPedestal(FabricBlockSettings.create()
			.nonOpaque()
			.requiresTool()
			.strength(1.5f, 6f)
			.sounds(BlockSoundGroup.STONE)
			.mapColor(MapColor.STONE_GRAY)
		);
	
	Item SWORD_PEDESTAL_ITEM = new BlockItem(SWORD_PEDESTAL_BLOCK, new FabricItemSettings());
	
	BlockEntityType<SwordPedestalBlockEntity> SWORD_PEDESTAL_BLOCK_ENTITY = Registry.register(
			Registries.BLOCK_ENTITY_TYPE, 
			id("sword_pedestal"),
			FabricBlockEntityTypeBuilder.create(SwordPedestalBlockEntity::new, SWORD_PEDESTAL_BLOCK).build()
		);
	
	// Misc Blocks
	Block STAR_STONE_BLOCK = new Block(FabricBlockSettings.create()
			.nonOpaque()
			.requiresTool()
			.strength(5f, 6f)
			.luminance(14)
			.sounds(BlockSoundGroup.METAL)
		);
	Item STAR_STONE_ITEM = new BlockItem(STAR_STONE_BLOCK, new FabricItemSettings());
	
	Block SUGAR_CUBE_BLOCK = new Block(FabricBlockSettings.create()
			.nonOpaque()
			.sounds(BlockSoundGroup.SAND)
			.mapColor(MapColor.WHITE)
		);
	Item SUGAR_CUBE_ITEM = new BlockItem(SUGAR_CUBE_BLOCK, new FabricItemSettings());
	
	// CALTROPS
	// STARBEAM_TORCH
	// BOMB_FLOWER
	
	
	public static void registerAll() {
		Registry.register(Registries.BLOCK, id("mystic_shrub"), MYSTIC_SHRUB_BLOCK);
		Registry.register(Registries.ITEM, id("mystic_shrub"), MYSTIC_SHRUB_ITEM);
		
		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, entity) -> 
				MYSTIC_SHRUB_BLOCK.handleBreak(state, world, pos));

		Registry.register(Registries.BLOCK, id("sword_pedestal"), SWORD_PEDESTAL_BLOCK);
		Registry.register(Registries.ITEM, id("sword_pedestal"), SWORD_PEDESTAL_ITEM);
		
		Registry.register(Registries.BLOCK, id("star_stone"), STAR_STONE_BLOCK);
		Registry.register(Registries.ITEM, id("star_stone"), STAR_STONE_ITEM);
		
		Registry.register(Registries.BLOCK, id("sugar_cube"), SUGAR_CUBE_BLOCK);
		Registry.register(Registries.ITEM, id("sugar_cube"), SUGAR_CUBE_ITEM);
	}
}
