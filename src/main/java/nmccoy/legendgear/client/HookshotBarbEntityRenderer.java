package nmccoy.legendgear.client;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import nmccoy.legendgear.LegendGear;
import nmccoy.legendgear.entity.HookshotBarbEntity;

public class HookshotBarbEntityRenderer extends EntityRenderer<HookshotBarbEntity> {
	
	private static final Identifier TEXTURE = LegendGear.id("textures/item/hookshot_barb.png");
	private static final RenderLayer BARB_LAYER = RenderLayer.getEntityCutout(TEXTURE);
	
	public HookshotBarbEntityRenderer(Context ctx) {
		super(ctx);
	}
	
	@Override
	public void render(HookshotBarbEntity barbEntity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		// Render barb
		matrices.push();
		matrices.scale(0.5f, 0.5f, 0.5f);
		matrices.multiply(this.dispatcher.getRotation());
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f));
		
		Entry entry = matrices.peek();
		Matrix4f posMatrix = entry.getPositionMatrix();
		Matrix3f norMatrix = entry.getNormalMatrix();
		
		VertexConsumer buffer = vertexConsumers.getBuffer(BARB_LAYER);
		vertexTexture(buffer, posMatrix, norMatrix, light, 0f, 0, 0, 1);
		vertexTexture(buffer, posMatrix, norMatrix, light, 1f, 0, 1, 1);
		vertexTexture(buffer, posMatrix, norMatrix, light, 1f, 1, 1, 0);
		vertexTexture(buffer, posMatrix, norMatrix, light, 0f, 1, 0, 0);
		matrices.pop();

		// Render cord between hookshot barb and hookshot holder
		renderCord(barbEntity, tickDelta, matrices, vertexConsumers);
	}
	
	private void renderCord(HookshotBarbEntity barb,float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
		if(barb.getPlayerOwner() == null) {
			return;
		}
		PlayerEntity holder = barb.getPlayerOwner();
		
		// Radius offset relative to holder
		double handOffsetRadius = holder.getMainArm() == Arm.RIGHT ? -0.4d : 0.4d;
		if(!holder.getMainHandStack().isOf(LegendGear.HOOKSHOT)) {
			// Item is held in opposite hand
			handOffsetRadius = -handOffsetRadius;
		}
		
		double bodyYawRadians = toRadians(holder.bodyYaw);
		double holderX = holder.getX() + handOffsetRadius * cos(bodyYawRadians);
		double holderY = holder.getY() + holder.getHeight() / 2d;
		double holderZ = holder.getZ() + handOffsetRadius * sin(bodyYawRadians);
		
		float deltaX = (float) (holderX - barb.getX());
		float deltaY = (float) (holderY - barb.getY());
		float deltaZ = (float) (holderZ - barb.getZ());
		
		matrices.push();		
		Entry matrixEntry = matrices.peek();
		
		VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getLineStrip());
		vertexLine(deltaX, deltaY, deltaZ, buffer, matrixEntry, 0, 1);
		vertexLine(deltaX, deltaY, deltaZ, buffer, matrixEntry, 1, 1);
		matrices.pop();
	}
	
	private void vertexTexture(VertexConsumer buffer, Matrix4f matrix, Matrix3f normalMatrix, int light, float x, int y, int u, int v) {
		buffer.vertex(matrix, x - 0.5f, y - 0.5f, 0f)
				.color(255, 255, 255, 255)
				.texture((float) u, (float) v)
				.overlay(OverlayTexture.DEFAULT_UV)
				.light(light)
				.normal(normalMatrix, 0f, 1f, 0f)
				.next();
	}
	
	private void vertexLine(float x, float y, float z, VertexConsumer buffer, Entry matrices, float segmentStart, float segmentEnd) {
		float startX = x * segmentStart;
		float startY = y * (segmentStart * segmentStart + segmentStart) * 0.5f + 0.25f;
		float startZ = z * segmentStart;
		
		float dirX = x * segmentEnd - startX;
		float dirY = y * (segmentEnd * segmentEnd + segmentEnd) * 0.5f + 0.25f - startY;
		float dirZ = z * segmentEnd - startZ;
		
		float length = (float) sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
		dirX /= length;
		dirY /= length;
		dirZ /= length;
		
		buffer.vertex(matrices.getPositionMatrix(), startX, startY, startZ)
				.color(0, 0, 0, 255)
				.normal(matrices.getNormalMatrix(), dirX, dirY, dirZ)
				.next();
	}

	@Override
	public Identifier getTexture(HookshotBarbEntity entity) {
		return TEXTURE;
	}
	
}
