package nmccoy.legendgear;

import static nmccoy.legendgear.LegendGear.id;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

import nmccoy.legendgear.client.HookshotBarbEntityRenderer;
import nmccoy.legendgear.client.MagicBoomerangEntityRenderer;
import nmccoy.legendgear.client.MagicRuneParticleRenderer;
import nmccoy.legendgear.client.ShootingStarEntityRenderer;
import nmccoy.legendgear.client.SwordPedestalBlockEntityRenderer;

@Environment(EnvType.CLIENT)
public class LegendGearClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), LegendGear.MYSTIC_SHRUB_BLOCK);

		registerEntityRenderers();
		registerModelPredicates();
		
		// Magic Rune particle renderer
		/*ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
            registry.register(LegendGear.id("particle/magic_rune"));
        }));*/
		ParticleFactoryRegistry.getInstance().register(LegendGear.MAGIC_RUNE, MagicRuneParticleRenderer.Factory::new);
	}
	
	
	private void registerModelPredicates() {
		// Provide predicates for hookshot
		ModelPredicateProviderRegistry.register(LegendGear.HOOKSHOT, id("fired"), (stack, world, entity, seed) -> {
			if(entity == null || entity.getActiveItem() != stack) {
				return 0;
			}
			
			return stack.getMaxUseTime() > entity.getItemUseTimeLeft() ? 1 : 0;
		});
		
		// Provide predicates for magic mirror
		ModelPredicateProviderRegistry.register(LegendGear.MAGIC_MIRROR, id("active"), (stack, world, entity, seed) -> {
			if(entity == null || entity.getActiveItem() != stack) {
				return 0;
			}
			
			return stack.getMaxUseTime() > entity.getItemUseTimeLeft() ? 1 : 0;
		});
		
		ModelPredicateProviderRegistry.register(LegendGear.MAGIC_MIRROR, id("dim"), (stack, world, entity, seed) -> {
			if(entity == null) {
				return 1;
			}
			
			boolean underSkylight = world.isSkyVisible(entity.getBlockPos());
			return underSkylight ? 1 : 0;
		});
		
		// Provide predicates for bomb
		ModelPredicateProviderRegistry.register(LegendGear.BOMB, id("active"), (stack, world, entity, seed) -> {
			if(entity == null || entity.getActiveItem() != stack) {
				return 0;
			}
			
			return stack.getMaxUseTime() > entity.getItemUseTimeLeft() ? 1 : 0;
		});
	}
	
	private void registerEntityRenderers() {
		EntityRendererRegistry.register(LegendGear.MAGIC_BOOMERANG_ENTITY, (context) -> {
			return new MagicBoomerangEntityRenderer(context);
		});
		
		EntityRendererRegistry.register(LegendGear.THROWN_MEDALLION_ENTITY, (context) -> {
			return new FlyingItemEntityRenderer<>(context, 1.4f, false);
		});
		
		EntityRendererRegistry.register(LegendGear.HOOKSHOT_BARB_ENTITY, (context) -> {
			return new HookshotBarbEntityRenderer(context);
		});
		
		EntityRendererRegistry.register(LegendGear.SHOOTING_STAR_ENTITY, (context) -> {
			return new ShootingStarEntityRenderer(context);
		});
		
		// Block entity renderer
		BlockEntityRendererFactories.register(LegendGear.SWORD_PEDESTAL_BLOCK_ENTITY, SwordPedestalBlockEntityRenderer::new);
	}

}
