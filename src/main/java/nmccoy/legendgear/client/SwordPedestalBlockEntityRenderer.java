package nmccoy.legendgear.client;

import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

import nmccoy.legendgear.block.SwordPedestalBlockEntity;

public class SwordPedestalBlockEntityRenderer implements BlockEntityRenderer<SwordPedestalBlockEntity> {
	
	private final ItemRenderer itemRenderer;
	
	
	public SwordPedestalBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		this.itemRenderer = ctx.getItemRenderer();
	}
	
	
	@Override
	public void render(SwordPedestalBlockEntity entity, float tickDelta, MatrixStack matrices,
			VertexConsumerProvider vertexConsumers, int light, int overlay) {
		ItemStack sword = entity.getSword();
		
		if(sword == null || sword.isEmpty()) {
			return;
		}
		
		Direction facing = entity.getCachedState().get(HorizontalFacingBlock.FACING);
		matrices.push();
		matrices.translate(0.5d, 0.75d, 0.5d);
		
		if(facing == Direction.WEST || facing == Direction.EAST) {
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(135f));
		} else if(facing == Direction.NORTH || facing == Direction.SOUTH) {
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90f));
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(135f));
		}
		
		itemRenderer.renderItem(
					sword, 
					ModelTransformationMode.FIXED, 
					light, 
					overlay, 
					matrices, 
					vertexConsumers, 
					entity.getWorld(), 
					999
				);
		
		matrices.pop();
	}

}
