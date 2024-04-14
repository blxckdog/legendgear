package nmccoy.legendgear.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class ShootingStarEntityRenderLayer extends RenderLayer {
	
	protected static RenderLayer create(Identifier texture) {
		boolean affectsOutline = true;
		
		MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder()
				.program(ENTITY_TRANSLUCENT_PROGRAM)
				.texture((RenderPhase.TextureBase) new RenderPhase.Texture(texture, false, false))
				.transparency(LIGHTNING_TRANSPARENCY) // <-- Modified this 
				.cull(DISABLE_CULLING)
				.lightmap(ENABLE_LIGHTMAP)
				.overlay(ENABLE_OVERLAY_COLOR)
				//.depthTest(LEQUAL_DEPTH_TEST) // <-- Added this
				.writeMaskState(COLOR_MASK) // <-- And added this
				.build((boolean) affectsOutline);
		
		return RenderLayer.of("entity_shooting_star", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
				VertexFormat.DrawMode.QUADS, 1536, true, true, multiPhaseParameters);
	}

	// This is just boilerplate made necessary by extending RenderLayer and getting access on MultiPhaseParameters
	private ShootingStarEntityRenderLayer(String name, VertexFormat i, DrawMode g, int n, boolean o, boolean r, Runnable e, Runnable thiz) {
		super("never_used", i, g, n, o, r, e, thiz);
	}

}
