package curtis.Cobbleworks.module.cobblegen;

import curtis.Cobbleworks.module.book.ContainerBook;
import curtis.Cobbleworks.module.book.GuiBook;
import curtis.Cobbleworks.module.book.ItemBook;
import curtis.Cobbleworks.module.farming.ContainerHarvester;
import curtis.Cobbleworks.module.farming.GuiHarvester;
import curtis.Cobbleworks.module.farming.TileHarvester;
import curtis.Cobbleworks.module.spawner.ContainerSpawner;
import curtis.Cobbleworks.module.spawner.GuiSummoner;
import curtis.Cobbleworks.module.spawner.TileEntityPendulumSummoner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

//I need to move this at some point, I know.
public class GuiProxy implements IGuiHandler {
	
	public static final int GUI_ID_COBBLEGEN = 0;
	public static final int GUI_ID_POCKET_FURNACE = 1;
	public static final int GUI_ID_SPAWNER = 2;
	public static final int GUI_ID_BOOK = 3;
	public static final int GUI_ID_HARVESTER = 4;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		if (ID == GUI_ID_COBBLEGEN) {
			return new ContainerCobblegen(player.inventory, (TileEntityCobbleGen)world.getTileEntity(new BlockPos(x, y, z)));
		}
		
		if (ID == GUI_ID_SPAWNER) {
			return new ContainerSpawner(player.inventory, (TileEntityPendulumSummoner)world.getTileEntity(new BlockPos(x,y,z)));
		}
		
		if (ID == GUI_ID_BOOK) {
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBook) {
				return new ContainerBook((ItemBook)player.getHeldItem(EnumHand.MAIN_HAND).getItem());
			}
		}
		
		if (ID == GUI_ID_HARVESTER) {
			return new ContainerHarvester(player.inventory, (TileHarvester)world.getTileEntity(new BlockPos(x, y, z)));
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		if (ID == GUI_ID_COBBLEGEN) {
			return new CobbleGenGui(player.inventory, (TileEntityCobbleGen)world.getTileEntity(new BlockPos(x, y, z)));
		}
		
		if (ID == GUI_ID_SPAWNER) {
			return new GuiSummoner(player.inventory, (TileEntityPendulumSummoner)world.getTileEntity(new BlockPos(x, y, z)));
		}
		
		if (ID == GUI_ID_BOOK) {
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBook) {
				return new GuiBook((ItemBook)player.getHeldItem(EnumHand.MAIN_HAND).getItem());
			}
		}
		
		if (ID == GUI_ID_HARVESTER) {
			return new GuiHarvester(player.inventory, (TileHarvester)world.getTileEntity(new BlockPos(x, y, z)));
		}
		
		return null;
	}

}