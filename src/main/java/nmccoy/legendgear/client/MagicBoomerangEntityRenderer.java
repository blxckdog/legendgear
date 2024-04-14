package nmccoy.legendgear.client;

import static net.minecraft.util.math.MathHelper.lerp;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import nmccoy.legendgear.entity.MagicBoomerangEntity;

public class MagicBoomerangEntityRenderer extends EntityRenderer<MagicBoomerangEntity> {

	private final ItemRenderer itemRenderer;
	
	public MagicBoomerangEntityRenderer(Context ctx) {
		super(ctx);
		this.itemRenderer = ctx.getItemRenderer();
	}
	
	@Override
	public void render(MagicBoomerangEntity boomerang, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();
		
		// General rotation
		float degY = lerp(tickDelta, boomerang.prevYaw, boomerang.getYaw()) - 90f;
		float degZ = lerp(tickDelta, boomerang.prevPitch, boomerang.getPitch());		
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(degY));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(degZ));

		// Spinning animation
		float spinRad = lerp(tickDelta, -boomerang.age + 1, -boomerang.age);
		matrices.multiply(RotationAxis.POSITIVE_Z.rotation(spinRad));
		
		this.itemRenderer.renderItem(
				boomerang.getStack(), 
				ModelTransformationMode.FIXED, 
				light,
				OverlayTexture.DEFAULT_UV, 
				matrices, 
				vertexConsumers, 
				boomerang.getWorld(), 
				boomerang.getId()
		);
		
		matrices.pop();
	}

	@Override
	public Identifier getTexture(MagicBoomerangEntity entity) {
		return null;
	}

}
