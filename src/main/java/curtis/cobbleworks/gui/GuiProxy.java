package curtis.cobbleworks.gui;

import curtis.cobbleworks.cobblegen.TileEntityCobblegen;
import curtis.cobbleworks.manual.ItemManual;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

	public static final int GUI_ID_COBBLEGEN = 0;
	public static final int GUI_ID_BOOK = 1;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		switch (ID) {
		case (GUI_ID_COBBLEGEN): { return new ContainerCobblegen(player.inventory, (TileEntityCobblegen)world.getTileEntity(new BlockPos(x, y, z))); }
		case (GUI_ID_BOOK): {
				if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemManual) {
					return new ContainerBook((ItemManual)player.getHeldItem(EnumHand.MAIN_HAND).getItem());
				}
			}
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		switch (ID) {
		case (GUI_ID_COBBLEGEN): { return new CobbleGenGui(player.inventory, (TileEntityCobblegen)world.getTileEntity(new BlockPos(x, y, z))); }
		case (GUI_ID_BOOK): {
				if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemManual) {
					return new GuiBook((ItemManual)player.getHeldItem(EnumHand.MAIN_HAND).getItem());
				}
			}
		}
		
		return null;
	}

}
