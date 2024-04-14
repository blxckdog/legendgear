package nmccoy.legendgear.client;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import nmccoy.legendgear.LegendGear;
import nmccoy.legendgear.entity.ShootingStarEntity;

public class ShootingStarEntityRenderer extends EntityRenderer<ShootingStarEntity> {

	private static final Identifier STAR_TEXTURE = LegendGear.id("textures/entity/star.png");
	private static final RenderLayer STAR_LAYER = ShootingStarEntityRenderLayer.create(STAR_TEXTURE);
	
	private static final Identifier BEAM_TEXTURE = LegendGear.id("textures/entity/star_beam.png");
	private static final RenderLayer BEAM_LAYER = ShootingStarEntityRenderLayer.create(BEAM_TEXTURE);
	
	public ShootingStarEntityRenderer(Context ctx) {
		super(ctx);
	}
	
	@Override
	public void render(ShootingStarEntity star, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();
		
		float phase = (float) (System.currentTimeMillis() % 900 / 900f);
		float scale = star.getDwindleTime() * 3f / ShootingStarEntity.MAX_DWINDLE_TIME;
		light = 240;
		
		int red = (int) (RainbowHelper.r(phase) * 255);
		int green = (int) (RainbowHelper.g(phase) * 255);
		int blue = (int) (RainbowHelper.b(phase) * 255);

		// Render beam while star is falling down
		if(star.getDwindleTime() > ShootingStarEntity.MAX_DWINDLE_TIME - 10) {
			matrices.push();
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f - this.dispatcher.camera.getYaw()));
			
			float scaling = (star.getDwindleTime() - (ShootingStarEntity.MAX_DWINDLE_TIME - 10)) * 0.1f;
			float wScaling = scale * 0.3f;
			matrices.scale(scaling, scaling, scaling);
			
			Entry entry = matrices.peek();
			Matrix4f posMatrix = entry.getPositionMatrix();
			Matrix3f norMatrix = entry.getNormalMatrix();
			
			VertexConsumer buffer = vertexConsumers.getBuffer(BEAM_LAYER);
			vertexTexture(buffer, posMatrix, norMatrix, red, green, blue, light, -wScaling, 0f, 0, 1);
			vertexTexture(buffer, posMatrix, norMatrix, red, green, blue, light, wScaling, 0f, 1, 1);
			vertexTexture(buffer, posMatrix, norMatrix, red, green, blue, light, wScaling, 30f, 1, 0);
			vertexTexture(buffer, posMatrix, norMatrix, red, green, blue, light, -wScaling, 30f, 0, 0);
			
			vertexTexture(buffer, posMatrix, norMatrix, red, green, blue, light, wScaling, 0f, 0, 1);
			vertexTexture(buffer, posMatrix, norMatrix, red, green, blue, light, -wScaling, 0f, 1, 1);
			vertexTexture(buffer, posMatrix, norMatrix, red, green, blue, light, -wScaling, 30f, 1, 0);
			vertexTexture(buffer, posMatrix, norMatrix, red, green, blue, light, wScaling, 30f, 0, 0);
			
			matrices.pop();
		}
		
		// Make this a billboard
		matrices.multiply(this.dispatcher.getRotation());
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f));

		// Main star
		VertexConsumer buffer = vertexConsumers.getBuffer(STAR_LAYER);
		matrices.push();
		matrices.scale(scale, scale, scale);
		renderStar(matrices, buffer, red, green, blue, light, 180 * phase);
		matrices.pop();

		// Second star
		matrices.push();
		scale *= 0.8;
		matrices.scale(scale, scale, scale);
		renderStar(matrices, buffer, green, blue, red, light, -270 * phase);
		matrices.pop();

		// Third star
		matrices.push();
		scale *= 0.8;
		matrices.scale(scale, scale, scale);
		renderStar(matrices, buffer, blue, red, green, light, 90 * phase);
		matrices.pop();
		
		matrices.pop();
	}
	
	private void renderStar(MatrixStack matrices, VertexConsumer buffer, int r, int g, int b, int light, float angle) {
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angle));
		matrices.translate(-0.5f, -0.5f, 0);
		
		Entry entry = matrices.peek();
		Matrix4f posMatrix = entry.getPositionMatrix();
		Matrix3f norMatrix = entry.getNormalMatrix();
		
		vertexTexture(buffer, posMatrix, norMatrix, r, g, b, light, 0f, 0f, 0, 1);
		vertexTexture(buffer, posMatrix, norMatrix, r, g, b, light, 1f, 0f, 1, 1);
		vertexTexture(buffer, posMatrix, norMatrix, r, g, b, light, 1f, 1f, 1, 0);
		vertexTexture(buffer, posMatrix, norMatrix, r, g, b, light, 0f, 1f, 0, 0);
	}
	
	private void vertexTexture(VertexConsumer buffer, Matrix4f matrix, Matrix3f normalMatrix, int r, int g, int b, int light, float x, float y, int u, int v) {
		buffer.vertex(matrix, x, y, 0f)
				.color(r, g, b, 255)
				.texture((float) u, (float) v)
				.overlay(OverlayTexture.DEFAULT_UV)
				.light(light, light)
				.normal(normalMatrix, 0f, 1f, 0f)
				.next();
	}

	@Override
	public Identifier getTexture(ShootingStarEntity entity) {
		return null;
	}

}
