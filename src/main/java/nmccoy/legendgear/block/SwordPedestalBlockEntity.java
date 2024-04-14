package nmccoy.legendgear.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

import nmccoy.legendgear.LegendGear;

public class SwordPedestalBlockEntity extends BlockEntity {

	private ItemStack sword;
	
	public SwordPedestalBlockEntity(BlockPos pos, BlockState state) {
		super(LegendGear.SWORD_PEDESTAL_BLOCK_ENTITY, pos, state);
	}
	
	
	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		
		if(sword != null) {
			nbt.put("displayed_sword", sword.writeNbt(new NbtCompound()));
		}
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		
		if(nbt.contains("displayed_sword")) {
			sword = ItemStack.fromNbt(nbt.getCompound("displayed_sword"));
		}
	}
	
	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return createNbt();
	}
	
	
	public void setSword(ItemStack sword) {
		this.sword = sword;
	}
	
	public ItemStack getSword() {
		return sword;
	}
	
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

}
