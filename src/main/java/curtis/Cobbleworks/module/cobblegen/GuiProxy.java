package curtis.Cobbleworks.module.cobblegen;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {
	
	public static final int GUI_ID_COBBLEGEN = 0;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		//System.out.print("getServerElement called");
		if (ID == GUI_ID_COBBLEGEN) {
			//System.out.print("ID was cobblegen");
			return new ContainerCobblegen(player.inventory, (TileEntityCobbleGen)world.getTileEntity(new BlockPos(x, y, z)));
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		//System.out.print("getClientElement called");
		if (ID == GUI_ID_COBBLEGEN) {
			//System.out.print("ID was cobblegen");
			return new CobbleGenGui(player.inventory, (TileEntityCobbleGen)world.getTileEntity(new BlockPos(x, y, z)));
		}
		
		return null;
	}

}