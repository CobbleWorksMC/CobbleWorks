package curtis.Cobbleworks.module.cobblegen;

import curtis.Cobbleworks.module.spawner.ContainerSpawner;
import curtis.Cobbleworks.module.spawner.GuiSummoner;
import curtis.Cobbleworks.module.spawner.TileEntityPendulumSummoner;
import curtis.Cobbleworks.module.tool.PocketFurnace;
import curtis.Cobbleworks.module.tool.gui.ContainerPocketFurnace;
import curtis.Cobbleworks.module.tool.gui.GuiPocketFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

//I need to move this at some point, I know.
public class GuiProxy implements IGuiHandler {
	
	public static final int GUI_ID_COBBLEGEN = 0;
	public static final int GUI_ID_POCKET_FURNACE = 1;
	public static final int GUI_ID_SPAWNER = 2;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI_ID_COBBLEGEN) {
			return new ContainerCobblegen(player.inventory, (TileEntityCobbleGen)world.getTileEntity(new BlockPos(x, y, z)));
		}
		if (ID == GUI_ID_POCKET_FURNACE) {
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof PocketFurnace) {
				return new ContainerPocketFurnace(player.inventory, (PocketFurnace)player.getHeldItem(EnumHand.MAIN_HAND).getItem());
			}
		}
		if (ID == GUI_ID_SPAWNER) {
			return new ContainerSpawner(player.inventory, (TileEntityPendulumSummoner)world.getTileEntity(new BlockPos(x,y,z)));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == GUI_ID_COBBLEGEN) {
			return new CobbleGenGui(player.inventory, (TileEntityCobbleGen)world.getTileEntity(new BlockPos(x, y, z)));
		}
		if (ID == GUI_ID_POCKET_FURNACE) {
			if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof PocketFurnace) {
				return new GuiPocketFurnace(player.inventory, (PocketFurnace)player.getHeldItem(EnumHand.MAIN_HAND).getItem());
			}
		}
		if (ID == GUI_ID_SPAWNER) {
			return new GuiSummoner(player.inventory, (TileEntityPendulumSummoner)world.getTileEntity(new BlockPos(x, y, z)));
		}
		return null;
	}

}