package curtis.Cobbleworks.module.farming;

import curtis.Cobbleworks.PacketSync;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHarvesterSync implements IMessage {
	
	NBTTagCompound nbt;
	BlockPos pos;
	int command;
	public int dimension;
	
	public PacketHarvesterSync() {
	}
	
	public PacketHarvesterSync(TileHarvester th, NBTTagCompound nbt) {
		this.nbt = nbt;
		this.pos = th.getPos();
		this.dimension = th.getWorld().provider.getDimension();
		this.command = nbt.getInteger("b_id");
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
		this.dimension = buf.readInt();
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.nbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		buf.writeInt(this.dimension);
		buf.writeInt(this.pos.getX());
		buf.writeInt(this.pos.getY());
		buf.writeInt(this.pos.getZ());
		ByteBufUtils.writeTag(buf, this.nbt);
	}
	
	public static class PacketHarvesterSyncHandler implements IMessageHandler<PacketHarvesterSync, IMessage> {

		@Override
		public IMessage onMessage(PacketHarvesterSync message, MessageContext ctx) {
			
			World world = DimensionManager.getWorld(message.dimension);
			if(world != null) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			}
			
			return null;
		}
		
		public void handle(PacketHarvesterSync message, MessageContext ctx) {
			TileEntity te = DimensionManager.getWorld(message.dimension).getTileEntity(message.pos);
			if (te instanceof TileHarvester) {
				((TileHarvester) te).receiveMessageFromClient(message.nbt);
			}
		}
	}
}
