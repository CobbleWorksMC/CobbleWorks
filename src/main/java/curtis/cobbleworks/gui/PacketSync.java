package curtis.cobbleworks.gui;

import curtis.cobbleworks.cobblegen.TileEntityCobblegen;
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

public class PacketSync implements IMessage {

	NBTTagCompound nbt;
	BlockPos pos;
	int command;
	public int dimension;
	public PacketSync() {
	}
	
	public PacketSync(TileEntityCobblegen te, NBTTagCompound nbt) {
		
		this.dimension = te.getWorld().provider.getDimension();
		this.pos = te.getPos();
		this.nbt = nbt;
		this.command = nbt.getInteger("Toggle");
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

	public static class PacketSyncHandler implements IMessageHandler<PacketSync, IMessage> {

		@Override
		public IMessage onMessage(PacketSync message, MessageContext ctx) {
			World world = DimensionManager.getWorld(message.dimension);
			if(world != null) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			}
			return null;
		}

		public void handle(PacketSync message, MessageContext ctx) {
			TileEntity te = DimensionManager.getWorld(message.dimension).getTileEntity(message.pos);
			if(te instanceof TileEntityCobblegen) {
				((TileEntityCobblegen)te).receiveMessageFromClient(message.nbt);
			} 
		}
	}

}
